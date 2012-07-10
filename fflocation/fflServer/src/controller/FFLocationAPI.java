package controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis.encoding.Base64;


import model.Note;
import model.Photo;
import model.Position;
import model.User;
import model.UserInfo;
import database.ffLocationDBIface;
import database.ffLocationMySQL;

public class FFLocationAPI {

	//Static field for the database connection
	private static ffLocationDBIface dbc;
	//Singleton pattern function
	
	public FFLocationAPI() {
		dbc = ffLocationMySQL.getInstance();
	}
	

	/**
	 * Logs in a user into the authentication system.
	 * This log in function does not allow administrators.
	 * @param nick is user's nick.
	 * @param pw is the user's password hashed with sha1.
	 * @return the authentication object or null if failed.
	 */
	public Auth login(String nick, String pw) {
		User u = dbc.loadUser(nick);
		if(u==null || u.isAdministrator()) return null;
		Auth a = Auths.getInstance().login(u, pw);
		if(a==null) return null;
		return new SAuth(a);
	}
	
	/**
	 * Logs in a user or an administrator into the authentication system.
	 * @param nick is user's or admin's nick.
	 * @param pw is the user's or admin's password hashed with sha1.
	 * @return the authentication object or null if failed.
	 */
	public Auth loginWP(String nick, String pw) {
		User u = dbc.loadUser(nick);
		if(u==null) return null;
		Auth a = Auths.getInstance().login(u, pw);
		if(a==null) return null;
		return new SAuth(a);
	}

	/**
	 * This function returns to a logged user he's own information.
	 * @param a the authentication object
	 * @return the user information
	 * or null if wrong authentication
	 */
	public UserInfo myUser(Auth a) {
		Integer uid = Auths.getInstance().getUser(a);
		if(uid==null) return null;
		UserInfo user = dbc.loadUser(uid);
		List<Position> ps = dbc.getPositions(user.getId(), 1);
		if (ps.size() > 0)
			user.setPosition(ps.get(0));
		return new SUserInfo(user);
	}
	
	/**
	 * Allows the user to change he's own user information
	 * @param a the authentication object
	 * @param ui the new user information
	 * @param pw the new password
	 * @return whether it succeed
	 */
	public boolean changeUser(Auth a, UserInfo ui, String pw) {
		Integer uid = Auths.getInstance().getUser(a);
		if(uid==null) return false;
		if(uid!=ui.getId()) return false;
		User u = new User(ui);		
		try {
			if(pw.equals(getHash("")))
				pw=dbc.loadUser(ui.getId()).getPassword();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		u.setPassword(pw);
		return dbc.saveUser(u);
	}
	
	/**
	 * Gets the last n position of a user.
	 * The user must be administrator or friend of the target
	 * to have rights to get this.
	 * @param a the authentication object
	 * @param id the target user's id
	 * @param c the n count of positions
	 * @return the array of positions
	 */
	public Position[] getPositions(Auth a, int id, int c){
		Integer uid = Auths.getInstance().getUser(a);
		if(uid==null) return null;
		if(!dbc.loadUser(uid).isAdministrator() && !dbc.areFriends(uid, id)) return null;
		List<Position> ps = dbc.getPositions(id, c);
		SPosition[] sp = new SPosition[ps.size()];
		for (int i = 0; i < ps.size(); i++) 
			sp[i] = new SPosition(ps.get(i));
		return sp;
	}
	
	/**
	 * This method registers a new user in the system
	 * @param ui the new user's information
	 * @param pw the new user's password
	 * @return the authentication of the registered user
	 */
	public Auth newUser(UserInfo ui, String pw) {
		User u = new User(ui);
		u.setPassword(pw);
		u.setAdministrator(false);
		if(dbc.newUser(u)){
			User nu = dbc.loadUser(u.getId());
			return new SAuth(Auths.getInstance().login(nu, nu.getPassword()));
		}
		return null;
	}
	
	/**
	 * This method registers a new administrator in the system.
	 * The authentication object must be from an administrator.
	 * @param a the authentication object
	 * @param ui the new user's information
	 * @param pw the new user's password
	 * @return the authentication of the registered user
	 */
	public boolean newAdmin(Auth a, UserInfo ui, String pw) {
		if(!dbc.loadUser(a.getUserID()).isAdministrator()) return false;
		User u = new User(ui);
		u.setPassword(pw);
		if(dbc.newUser(u)){
			User nu = dbc.loadUser(u.getId());
			return nu!=null;
		}
		return false;
	}
	
	/**
	 * This function allows to check if a nick is in use.
	 * @param nick is the text.
	 * @return whether the nick is in use or not.
	 */
	public boolean exists(String nick) {
		return dbc.exists(nick);
	}
	
	/**
	 * This method creates a new position in the user's history
	 * @param a the authentication object
	 * @param p the position object to be stored
	 * @return whether the process succeed
	 */
	public boolean logPosition(Auth a, Position p){
		Integer uid = Auths.getInstance().getUser(a);
		if (uid==null) return false;
		if(dbc.newPosition(uid,
			Float.parseFloat(p.getLatitude()), Float.parseFloat(p.getLongitude())))
					return true;
		return false;
	}
	
	/**
	 * This method creates a new note in the system.
	 * This note can have a photo, or be null.
	 * @param a the authentication object
	 * @param position the position object containing the latitude and longitude
	 * @param note the text of the note
	 * @param photo the photo if there is one, otherwise null
	 * @return whether the process succeed
	 */
	public boolean setNote(Auth a, Position position, String note, String photo){
		if(photo.equals("null")) photo=null;
		Integer uid = Auths.getInstance().getUser(a);
		if (uid==null) return false;
		Note n = new Note(0,note, uid, position,photo!=null);
		Photo oPhoto = new Photo();
		if(photo!=null) {
			oPhoto.setPhoto(Base64.decode(photo));
			n.setPhoto(oPhoto);
		}
		boolean r = dbc.setNote(n);
		return r;
	}
	
	/**
	 * This function returns the last n notes of an specific user.
	 * This notes can be requested with or with out photos.
	 * @param a the authentication object
	 * @param id the owner of the notes
	 * @param c the n count of notes
	 * @param attachPhotos whether you need the photos.
	 * @return the array of notes
	 */
	public Note[] getNotes(Auth a, int id, int c, boolean attachPhotos) {
		Integer uid = Auths.getInstance().getUser(a);
		if(uid==null) return null;
		if(!dbc.areFriends(uid, id) || dbc.loadUser(a.getUserID()).isAdministrator()) return null;
		List<Note> ns = dbc.getNotes(id, c);
		SNote[] sn = new SNote[ns.size()];
		for (int i = 0; i < ns.size(); i++) {
			sn[i] = new SNote(ns.get(i), attachPhotos);
		}
		return sn;
	}

	/**
	 * This function returns the logged user's friend list.
	 * @param a the authentication object
	 * @return an array of user's information
	 */
	public UserInfo[] getFriends(Auth a) {
		Integer uid = Auths.getInstance().getUser(a);
		if(uid==null) return null;
		List<UserInfo> fs = dbc.getFriends(uid);
		SUserInfo[] sf = new SUserInfo[fs.size()];
		for (int i = 0; i < fs.size(); i++) {
			sf[i] = new SUserInfo(fs.get(i));
			List<Position> ps = dbc.getPositions(sf[i].getId(), 1);
			if (ps.size() > 0)
				sf[i].setPosition(ps.get(0));
		}
		return sf;
	}
	
	/**
	 * This function returns your friend list page by page.
	 * @param a the authentication object
	 * @param count the count of users per page
	 * @param page the page number
	 * @return the array of user's list in the page
	 */
	public UserInfo[] getFriendsPage(Auth a, int count, int page) {
		Integer uid = Auths.getInstance().getUser(a);
		if(uid==null) return null;
		List<UserInfo> fs = dbc.getFriends(uid, count, page);
		SUserInfo[] sf = new SUserInfo[fs.size()];
		for (int i = 0; i < fs.size(); i++) {
			sf[i] = new SUserInfo(fs.get(i));
			List<Position> ps = dbc.getPositions(sf[i].getId(), 1);
			if (ps.size() > 0)
				sf[i].setPosition(ps.get(0));
		}
		return sf;
	}
	
	/**
	 * This function gets all the users of the system page by page
	 * @param a the authentication object
	 * @param count the count of users per page
	 * @param page the page number
	 * @return the array of user's list in the page
	 */
	public UserInfo[] getUsers(Auth a, int count, int page) {
		Integer uid = Auths.getInstance().getUser(a);
		if(uid==null) return null;
		if(!dbc.loadUser(uid).isAdministrator()) return null;
		List<UserInfo> fs = dbc.getUsers(count,page);
		SUserInfo[] sf = new SUserInfo[fs.size()];
		for (int i = 0; i < fs.size(); i++) {
			sf[i] = new SUserInfo(fs.get(i));
			List<Position> ps = dbc.getPositions(sf[i].getId(), 1);
			if (ps.size() > 0)
				sf[i].setPosition(ps.get(0));
		}
		return sf;
	}
	
	/**
	 * This function returns the users friend request list.
	 * @param a the authentication object
	 * @return the array of user information
	 */
	public UserInfo[] getRequests(Auth a) {
		Integer uid = Auths.getInstance().getUser(a);
		if(uid==null) return null;
		List<UserInfo> fs = dbc.getRequests(uid);
		SUserInfo[] sf = new SUserInfo[fs.size()];
		for (int i = 0; i < fs.size(); i++)
			sf[i] = new SUserInfo(fs.get(i));
		return sf;
	}

	/**
	 * This method allows a user to make friendship requests to other users.
	 * @param a the authentication object.
	 * @param to the id of the user that will receive the request.
	 * @return whether the process succeed.
	 */
	public boolean askFriend(Auth a, int to) {
		Integer from = Auths.getInstance().getUser(a);
		if (from == null)
			return false;
		if (dbc.areFriends(from, to))
			return false;
		List<UserInfo> l = dbc.getRequests(from);
		boolean response=false;
		for(int i=0; i<l.size(); i++){
			response=response||(l.get(i).getId()==to);
		}		
		if (response) {
			if (!dbc.delRequest(to, from))
				return false;
			return dbc.addFriends(from, to);
		} else {
			return dbc.newRequest(from, to);
		}
	}
	
	/**
	 * This method allows an administrator to delete another account.
	 * @param a the authentication object
	 * @param id the user's id
	 * @return whether the process succeed
	 */
	public boolean delUser(Auth a, int id) {
		Integer uid = Auths.getInstance().getUser(a);
		if(!dbc.loadUser(uid).isAdministrator())return false;
		return dbc.delUser(id);
	}
	
	/**
	 * This method allows a user to search for other users
	 * that are not his friends or administrators
	 * @param a the authentication object
	 * @param nick text the nick should contain
	 * @param name text the name should contain
	 * @param surname text the surname should contain
	 * @param country text the country should contain
	 * @return the array of user's info that matches the fields
	 */
	public UserInfo[] searchFriend(Auth a, String nick, String name, String surname, String country) {
		Integer uid = Auths.getInstance().getUser(a);
		if(uid==null) return null;
		List<UserInfo> fs = dbc.searchFriend(nick, name, surname, country);
		List<UserInfo> fsf = new ArrayList<UserInfo>();
		for (int i = 0; i < fs.size(); i++)
			if(!dbc.areFriends(uid,fs.get(i).getId()) && !fs.get(i).isAdministrator())
				fsf.add(fs.get(i));		
		SUserInfo[] sf = new SUserInfo[fsf.size()];
		for (int i = 0; i < fsf.size(); i++)
			sf[i] = new SUserInfo(fsf.get(i));
		return sf;
	}
	
	/**
	 * This function returns a note with the photo attached if the note has one.
	 * @param a the authentication object
	 * @param noteID the note's id
	 * @return the note object
	 */
	public Note getNote(Auth a, int noteID){
		Integer id = Auths.getInstance().getUser(a);
		if(id==null) return null;
		Note n = dbc.getNote(noteID);
		if(!dbc.areFriends(n.getOwner(), id) && !dbc.loadUser(id).isAdministrator()) return null;
		return new SNote(n, true);
	}
	
	
	/**
	 * This function calculates the hash code of a String into another one.
	 * It uses the SHA1 algorithm.
	 * The code has been modified, but the original can be found at
	 * the url "http://es.debugmodeon.com/articulo/generar-hash-sha-1-de-una-cadena-en-java"
	 * @param message String to be hashed
	 * @return SHA1(message)
	 * @throws NoSuchAlgorithmException
	 */
	 private static String getHash(String message) throws NoSuchAlgorithmException {
	    	MessageDigest md = MessageDigest.getInstance("SHA1");
	        byte[] buffer=message.getBytes();
	        String hash = "";
	        md.update(buffer);
	        byte[] digest = md.digest();
	        for(byte aux : digest) {
	            int b = aux & 0xff;
	            if (Integer.toHexString(b).length() == 1) hash += "0";
	            hash += Integer.toHexString(b);
	        }
	        return hash;
	 }
}
