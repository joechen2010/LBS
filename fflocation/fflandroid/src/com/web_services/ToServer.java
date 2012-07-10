package com.web_services;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import model.Photo;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.model.KAuth;
import com.model.KNote;
import com.model.KPhoto;
import com.model.KPosition;
import com.model.KUserInfo;
import android.util.Base64;

/**
 * This class is the one that accesses the FFLocation web service.
 * Provides access to all the API functions in the web service the android needs.
 * It also has a cache system not to overload the system or the net.
 * This kind of cache will also respond faster and make the android programmer
 * not to have to worry about the net overloading.
 */
public class ToServer {
	
	
	//Cache live 30 segs. 
	final static long cacheLive = 30000;
	
	//Session live 10 mins.
	final static long sessionLive = 600000;
	
	//Last session update
	static long lastSessionUpdate;
		
	//The web service's url.
	private static String URL = "http://192.168.2.2:8080/fflServer/services/FFLocationAPI";

	//The ip
	private static String ip = "192.168.2.2";
	
	//The namespace of the web service you want to access.
	private static final String NAMESPACE = "http://controller";
	
	//Not need to specify in our type of soap calls.
	private static final String SOAP_ACTION = "";
	private static KUserInfo myUser= null;
	
	private static String password;
	private static String nickname;
	
	//Notes cache system
	private static List<KNote> listNotes = null;
	private static long notesTime = System.currentTimeMillis();
	private static int selectedUserID = 0;
	
	//Friends cache system
	private static List<KUserInfo> listFriends = null;
	private static long friendsTime = System.currentTimeMillis();
	
	//User authentication for the server
	private static KAuth auth = null;
	
	/**
	 * Simple function to remove the user's authentication for the server.
	 */
	public static void logout(){
		setAuth(null);
	}
	

	/**
	 * Checks if you have ever logged into the server.
	 * @return whether you have or not a user authentication
	 * @throws Exception 
	 */
	public static boolean logged() throws Exception{
		return getAuth()!=null;
		
	}

	/**
	 * Set the ip
	 * @param ip server's ip.
	 */
	public static void setIP(String _ip)
	{
	
		if(ip.length()>0)
		{
			URL = "http://"+_ip+":8080/fflServer/services/FFLocationAPI";
			ip = _ip;
		}
	
	}
	
	
	/**
	 * This process authenticates the user in the server and
	 * stores the received authentication object.
	 * @param n User name
	 * @param p User password
	 * @return whether you have been authenticated correctly.
	 * @throws Exception if something fails, for example,
	 * connection to the server and soap serialization.
	 */
	public static boolean login(String n, String p) throws Exception {
		
		URL = "http://"+ip+":8080/fflServer/services/FFLocationAPI";

		try
		{
			
			password = p;
			nickname = n;
			p = getHash(p); 
	
			lastSessionUpdate = System.currentTimeMillis();
			
			
			String METHOD_NAME = "login";
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	
			request.addProperty("nick", n);
			request.addProperty("pw", p);
	
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
	
			HttpTransportSE transport = new HttpTransportSE(URL);
	
			envelope.addMapping("http://controller", "Auth", new KAuth().getClass());
	
			transport.call(SOAP_ACTION, envelope);
	
			setAuth((KAuth) envelope.getResponse());
			myUser = null;	
			
			
		}catch(Exception ex)
		{
			return loginAlternative(n, p);
		}
		return getAuth()!=null;
	}
	
	/**
	 * This process authenticates the user in the server and
	 * stores the received authentication object (For running only in Tomcat).
	 * @param n User name
	 * @param p User password
	 * @return whether you have been authenticated correctly.
	 * @throws Exception if something fails, for example,
	 * connection to the server and soap serialization.
	 */	
	public static boolean loginAlternative(String n, String p) throws Exception
	{
		//url Tomcat
		URL = "http://"+ip+":8080/fflServer/services/FFLocationAPI?wsdl";
		
		password = p;
		nickname = n;
		p = getHash(p); 

		lastSessionUpdate = System.currentTimeMillis();
		
		
		String METHOD_NAME = "login";
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		request.addProperty("nick", n);
		request.addProperty("pw", p);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);

		HttpTransportSE transport = new HttpTransportSE(URL);

		envelope.addMapping("http://controller", "Auth", new KAuth().getClass());

		transport.call(SOAP_ACTION, envelope);

		setAuth((KAuth) envelope.getResponse());
		myUser = null;
		
		return getAuth() != null;
				
	}

	/**
	 * This method return the current Auth, if the method warns that the current session has expired
	 * we do another login
	 * @return the Auth
	 * @throws Exception if something fails.
	 */
	private static KAuth getAuth() throws Exception
	{
		long currentTime = System.currentTimeMillis();
		
		
		//If has been more time than sessionLive without update
		if((currentTime - lastSessionUpdate) > sessionLive)
		{
			login(nickname, password);
		}
		lastSessionUpdate = currentTime;
		
		return auth;
	}
	private static void setAuth(KAuth au) {
		auth = au;
		
	}
	
	/**
	 * This function request the logged user information.
	 * @return The logged in user's information.
	 * @throws Exception if something fails, for example,
	 * connection to the server and soap serialization.
	 * Other exception thrown is when not logged.
	 */
	public static KUserInfo myUser() throws Exception {
		
		if(myUser == null)
		{
		
			if(getAuth()==null) throw new Exception("Not logged");
			KAuth a = getAuth();
			String METHOD_NAME = "myUser";
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

			request.addProperty("a", a);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.addMapping("http://controller", "KAuth", KAuth.class);

			envelope.addMapping("http://model", "UserInfo",
				new KUserInfo().getClass());
			envelope.addMapping("http://model", "Position",
				new KPosition().getClass());

			HttpTransportSE transport = new HttpTransportSE(URL);
			transport.call(SOAP_ACTION, envelope);

			
			
			myUser =  (KUserInfo) envelope.getResponse();
			
			
			return myUser;
		}
		else
			return myUser;
	}

	/**
	 * Gets one note from the server
	 * if you have rights to do it.
	 * @param noteID
	 * @return The note with the photo attached, if it has.
	 * null if you have no rights to get it.
	 * @throws Exception if something fails, for example,
	 * connection to the server and soap serialization.
	 * Other exception thrown is when not logged.
	 */
	public static KNote getNote(int noteID) throws Exception {
		if(getAuth()==null) throw new Exception("Not logged");
		KAuth a = getAuth();
		String METHOD_NAME = "getNote";
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		request.addProperty("a", a);
		request.addProperty("noteID", noteID);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.addMapping("http://controller", "KAuth", KAuth.class);


		envelope.addMapping("http://model", "Note",
				new KNote().getClass());
		envelope.addMapping("http://model", "Position",
				new KPosition().getClass());
		envelope.addMapping("http://model", "Photo",
				new KPhoto().getClass());

		HttpTransportSE transport = new HttpTransportSE(URL);
		transport.call(SOAP_ACTION, envelope);
		System.out.println(envelope.getResponse());
		return (KNote) envelope.getResponse();
	}
	
	/**
	 * Gets a given user's last n positions.
	 * @param id The users id.
	 * @param c The n count of positions.
	 * @return The list of positions
	 * null if you have no rights to get it.
	 * @throws Exception if something fails, for example,
	 * connection to the server and soap serialization.
	 * Other exception thrown is when not logged.
	 */
	public static List<KPosition> getPositions(int id, int c) throws Exception {
		if(getAuth()==null) throw new Exception("Not logged");
		KAuth a = getAuth();
		String METHOD_NAME = "getPositions";
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		request.addProperty("a", a);
		request.addProperty("id", id);
		request.addProperty("c", c);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.addMapping("http://controller", "KAuth", KAuth.class);

		envelope.addMapping("http://model", "UserInfo",
				new KUserInfo().getClass());
		envelope.addMapping("http://model", "Position",
				new KPosition().getClass());

		HttpTransportSE transport = new HttpTransportSE(URL);
		transport.call(SOAP_ACTION, envelope);

		SoapObject so = (SoapObject) envelope.bodyIn;
		List<KPosition> us = new ArrayList<KPosition>();
		for (int i = 0; i < so.getPropertyCount(); i++)
			us.add((KPosition) so.getProperty(i));
		return us;
	}

	/**
	 * This function get the authenticated user's friend list.
	 * @return The list of friends
	 * @throws Exception if something fails, for example,
	 * connection to the server and soap serialization.
	 * Other exception thrown is when not logged.
	 */
	public static List<KUserInfo> getFriends() throws Exception {
		
		if(getAuth()==null) throw new Exception("Not logged");
		
		long currentTime = System.currentTimeMillis();
		if(listFriends == null || (currentTime-friendsTime)>cacheLive)
		{
			friendsTime=currentTime;
			KAuth a = getAuth();
			String METHOD_NAME = "getFriends";
			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	
			request.addProperty("a", a);
	
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.setOutputSoapObject(request);
			envelope.addMapping("http://controller", "KAuth", KAuth.class);
	
			envelope.addMapping("http://model", "UserInfo",
					new KUserInfo().getClass());
			envelope.addMapping("http://model", "Position",
					new KPosition().getClass());
	
			HttpTransportSE transport = new HttpTransportSE(URL);
			transport.call(SOAP_ACTION, envelope);
	
			SoapObject so = (SoapObject) envelope.bodyIn;
			listFriends = new ArrayList<KUserInfo>();
			for (int i = 0; i < so.getPropertyCount(); i++)
				listFriends.add((KUserInfo) so.getProperty(i));
			return listFriends;
		}
		else
		{
			return listFriends;
		}
	}

	/**
	 * This function registers a new user in the system
	 * @param u The new user's information object
	 * @param pw The new user's password
	 * @return whether the process succeed
	 * @throws Exception if something fails, for example,
	 * connection to the server and soap serialization.
	 * Other exception thrown is when not logged.
	 */
	public static boolean newUser(KUserInfo u, String pw) throws Exception {
		pw = getHash(pw); 
		
		String METHOD_NAME = "newUser";
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		request.addProperty("u", u);
		request.addProperty("pw", pw);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);

		envelope.addMapping("http://controller", "KUserInfo", KUserInfo.class);
		envelope.addMapping("http://controller", "KPosition", KPosition.class);
		
		HttpTransportSE transport = new HttpTransportSE(URL);

		envelope.addMapping("http://controller", "Auth", new KAuth().getClass());

		transport.call(SOAP_ACTION, envelope);

		
		
		return (envelope.getResponse() != null);
	}

	/**
	 * This function allows the programmer to change the authenticated user's information.
	 * @param u The user's new information
	 * @param pw The user's new password
	 * @return whether the process succeed success
	 * @throws Exception if something fails, for example,
	 * connection to the server and soap serialization.
	 * Other exception thrown is when not logged.
	 */
	public static boolean changeUser(KUserInfo u, String pw) throws Exception {
		
		pw = getHash(pw); 
		
		if(getAuth()==null) throw new Exception("Not logged");
		KAuth a = getAuth();
		String METHOD_NAME = "changeUser";
		
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		request.addProperty("a", a);
		request.addProperty("u", u);
		request.addProperty("pw", pw);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);

		envelope.addMapping("http://controller", "KAuth", KAuth.class);
		envelope.addMapping("http://controller", "KUserInfo", KUserInfo.class);
		envelope.addMapping("http://controller", "KPosition", KPosition.class);
		
		HttpTransportSE transport = new HttpTransportSE(URL);

		envelope.addMapping("http://controller", "Auth", new KAuth().getClass());

		transport.call(SOAP_ACTION, envelope);

		myUser = null;
		
		return Boolean.parseBoolean(envelope.getResponse().toString());
		
	}
	
	/**
	 * Registers a new position in the logged user's name.
	 * @param longitude of the GPS
	 * @param latitude of the GPS
	 * @return whether it succeed
	 * @throws Exception if something fails, for example,
	 * connection to the server and soap serialization.
	 * Other exception thrown is when not logged.
	 */
	public static boolean logPosition(float longitude, float latitude)
			throws Exception {
		if(getAuth()==null) throw new Exception("Not logged");
		KAuth a = getAuth();
		String METHOD_NAME = "logPosition";
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		KPosition p = new KPosition();
		p.setLongitude(longitude);
		p.setLatitude(latitude);
		request.addProperty("a", a);
		request.addProperty("p", p);
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		
		envelope.addMapping("http://controller", "KAuth", KAuth.class);
		envelope.addMapping("http://controller", "KPosition", KPosition.class);

		HttpTransportSE transport = new HttpTransportSE(URL);
		transport.call(SOAP_ACTION, envelope);
		
		myUser= null;

		return Boolean.parseBoolean(envelope.getResponse().toString());
	}

	/**
	 * Ask a user to be the logged user's friend.
	 * @param to The id of the user that will receive the request.
	 * @return whether the process succeed
	 * @throws Exception if something fails, for example,
	 * connection to the server and soap serialization.
	 * Other exception thrown is when not logged.
	 */
	public static boolean askFriend(int to) throws Exception {
		if(getAuth()==null) throw new Exception("Not logged");
		KAuth a = getAuth();
		String METHOD_NAME = "askFriend";
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		request.addProperty("a", a);
		request.addProperty("to", to);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.addMapping("http://controller", "KAuth", KAuth.class);

		HttpTransportSE transport = new HttpTransportSE(URL);
		transport.call(SOAP_ACTION, envelope);

		
		//Clear the listFriends
		listFriends = null;
		
		return Boolean.parseBoolean(envelope.getResponse().toString());
	}
	
	/**
	 * This function checks is a user name is already been use.
	 * @param nick The user nick
	 * @return whether it is been used.
	 * @throws Exception if something fails, for example,
	 * connection to the server and soap serialization.
	 */
	public static boolean exists(String nick) throws Exception {
		String METHOD_NAME = "exists";
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		request.addProperty("nick", nick);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);

		HttpTransportSE transport = new HttpTransportSE(URL);
		
		transport.call(SOAP_ACTION, envelope);

		return Boolean.parseBoolean(envelope.getResponse().toString());
	}

	/**
	 * This function is used to search for users in the system
	 * that are not already you friends.
	 * @param nick String the text should contain
	 * @param name String the name should contain
	 * @param surname String the surname should contain
	 * @param country String the country should contain
	 * @return the users list that match the fields
	 * @throws Exception if something fails, for example,
	 * connection to the server and soap serialization.
	 * Other exception thrown is when not logged.
	 */
	public static List<KUserInfo> searchFriend(String nick, String name,
			String surname, String country) throws Exception {
		if(getAuth()==null) throw new Exception("Not logged");
		KAuth a = getAuth();
		String METHOD_NAME = "searchFriend";
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		request.addProperty("a", a);
		request.addProperty("nick", nick);
		request.addProperty("name", name);
		request.addProperty("surname", surname);
		request.addProperty("country", country);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.addMapping("http://controller", "KAuth", KAuth.class);

		envelope.addMapping("http://model", "UserInfo",
				new KUserInfo().getClass());

		HttpTransportSE transport = new HttpTransportSE(URL);
		transport.call(SOAP_ACTION, envelope);
		System.out.println(envelope.bodyIn.getClass());
		SoapObject so = (SoapObject) envelope.bodyIn;
		List<KUserInfo> us = new ArrayList<KUserInfo>();
		for (int i = 0; i < so.getPropertyCount(); i++)
			us.add((KUserInfo) so.getProperty(i));
		return us;
	}
	
	/**
	 * Sends a new note to the server.
	 * Attaching the photo is optional.
	 * @param longitude of the GPS.
	 * @param latitude of the GPS.
	 * @param note The text of the note.
	 * @param photo The photo to be attached or null if none.
	 * @return whether the process succeed.
	 * @throws Exception if something fails, for example,
	 * connection to the server and soap serialization.
	 * Other exception thrown is when not logged.
	 */
	public static boolean setNote(float longitude, float latitude, String note, Photo photo)
	throws Exception {
		if(getAuth()==null) throw new Exception("Not logged");
		KAuth a = getAuth();
		String METHOD_NAME = "setNote";
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		
		KPosition p = new KPosition();
		p.setLatitude(latitude);
		p.setLongitude(longitude);
		
		request.addProperty("a", a);
		request.addProperty("position", p);
		request.addProperty("note", note);
		if(photo!=null){
			request.addProperty("photo", Base64.encodeToString(photo.getPhoto(), Base64.DEFAULT));
		}else{
			request.addProperty("photo", "null");
		}
		
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		
		envelope.addMapping("http://controller", "KAuth", KAuth.class);
		envelope.addMapping("http://controller", "KPosition", KPosition.class);
		
		HttpTransportSE transport = new HttpTransportSE(URL);
		transport.call(SOAP_ACTION, envelope);
		
		return Boolean.parseBoolean(envelope.getResponse().toString());
	}
	
	/**
	 * This function gets a users last n notes with or without photos.
	 * @param id The user's id.
	 * @param c The n count of notes wanted.
	 * @param attachPhotos Whether you want the photos attached to the notes.
	 * @return List of notes.
	 * @throws Exception if something fails, for example,
	 * connection to the server and soap serialization.
	 * Other exception thrown is when not logged.
	 */
	public static List<KNote> getNotes(int id, int c, boolean attachPhotos) throws Exception {
		if(getAuth()==null) throw new Exception("Not logged");
		long currentTime = System.currentTimeMillis();
		
		if(listNotes==null || selectedUserID!=id || (currentTime-notesTime)>cacheLive){
		notesTime=currentTime;
		selectedUserID=id;
		KAuth a = getAuth();
		String METHOD_NAME = "getNotes";
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		request.addProperty("a", a);
		request.addProperty("id", id);
		request.addProperty("c", c);
		request.addProperty("attachPhotos", attachPhotos);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.addMapping("http://controller", "KAuth", KAuth.class);

		envelope.addMapping("http://model", "Note",
				new KNote().getClass());
		envelope.addMapping("http://model", "Position",
				new KPosition().getClass());
		envelope.addMapping("http://model", "Photo",
				new KPhoto().getClass());

		HttpTransportSE transport = new HttpTransportSE(URL);
		transport.call(SOAP_ACTION, envelope);
		SoapObject so = (SoapObject) envelope.bodyIn;
		listNotes = new ArrayList<KNote>();
		for (int i = 0; i < so.getPropertyCount(); i++)
			listNotes.add((KNote) so.getProperty(i));
		}
		
		
		return listNotes;
	}

	/**
	 * This function returns the list of users that have
	 * asked the logged user to be friends.
	 * @return The list of user info.
	 * @throws Exception if something fails, for example,
	 * connection to the server and soap serialization.
	 * Other exception thrown is when not logged.
	 */
	public static List<KUserInfo> getRequests() throws Exception {
		if(getAuth()==null) throw new Exception("Not logged");
		KAuth a = getAuth();
		String METHOD_NAME = "getRequests";
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

		request.addProperty("a", a);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.addMapping("http://controller", "KAuth", KAuth.class);

		envelope.addMapping("http://model", "UserInfo",
				new KUserInfo().getClass());
		envelope.addMapping("http://model", "Position",
				new KPosition().getClass());

		HttpTransportSE transport = new HttpTransportSE(URL);
		transport.call(SOAP_ACTION, envelope);

		SoapObject so = (SoapObject) envelope.bodyIn;
		List<KUserInfo> us = new ArrayList<KUserInfo>();
		for (int i = 0; i < so.getPropertyCount(); i++)
			us.add((KUserInfo) so.getProperty(i));
		return us;
	}


	/**
	 * Simple hash function using the SHA1 algorithm.
	 * This is used because the server specifies that the password
	 * must be hashed with SHA1.
	 * @param message to be hashed.
	 * @return sha1(message)
	 * @throws NoSuchAlgorithmException if the the algorithm is not available in the system.
	 */
	private static String getHash(String message) throws NoSuchAlgorithmException {
		MessageDigest md;
		byte[] buffer, digest;
		String hash = "";
		
		buffer = message.getBytes();
		md = MessageDigest.getInstance("SHA1");
		md.update(buffer);
		digest = md.digest();

		for(byte aux : digest) {
			int b = aux & 0xff;
			if (Integer.toHexString(b).length() == 1) hash += "0";
				hash += Integer.toHexString(b);
		}

		return hash;
	}	
	


}
