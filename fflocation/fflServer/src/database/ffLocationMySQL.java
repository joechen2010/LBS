package database;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Note;
import model.Photo;
import model.Position;
import model.User;
import model.UserInfo;



public class ffLocationMySQL implements ffLocationDBIface{

	/*Singleton pattern*/
	private Connection connection;
	private static ffLocationMySQL instance = new ffLocationMySQL();
	public static ffLocationMySQL getInstance() {return instance;}
	
	private ffLocationMySQL(){
		try
        {
        String url = "jdbc:mysql://localhost/fflocation";
        connection = DriverManager.getConnection(url, "fflocation", "fflocation");
        connection.setAutoCommit(true);
        }catch (SQLException anException){
	        while (anException != null){
	            System.out.println("SQL Exception:  " + anException.getMessage());
	            anException = anException.getNextException();
	        }
	    }catch (java.lang.Exception anException){
	        anException.printStackTrace();
	    }
	}
	
	@Override
	public boolean newUser(User us) {
		PreparedStatement insertSentence = null;
		PreparedStatement selectSentence = null;
		ResultSet resultSet = null;
		try{
			insertSentence = connection.prepareStatement(
					"INSERT INTO `fflocation`.`user` " +
					"(`ID`, `Nick`, `Password`, `Name`, `Surname`, `Email`, " +
					"`Phone`, `Country`, `Address`, `Administrator`) " +
					"VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
			insertSentence.setString(1, us.getNick());
			insertSentence.setString(2, us.getPassword());
			insertSentence.setString(3, us.getName());
			insertSentence.setString(4, us.getSurname());
			insertSentence.setString(5, us.getEmail());
			insertSentence.setInt(6, us.getPhone());
			insertSentence.setString(7, us.getCountry());
			insertSentence.setString(8, us.getAddress());
			insertSentence.setBoolean(9, us.isAdministrator());
            insertSentence.executeUpdate();
      
            selectSentence =
                connection.prepareStatement("SELECT LAST_INSERT_ID()");
            resultSet = selectSentence.executeQuery();
            if (resultSet.next()) us.setId(resultSet.getInt(1));
			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean saveUser(User us) {
		PreparedStatement updateSentence = null;
		try{
			updateSentence = connection.prepareStatement(
					"UPDATE `fflocation`.`user` SET" +
					"`Nick` = ?, " +
					"`Password` = ?, " +
					"`Name` = ?, " +
					"`Surname` = ?, " +
					"`Email` = ?, " +
					"`Phone` = ?, " +
					"`Country` = ?, " +
					"`Address` = ?, " +
					"`Administrator` = ? " +
					"WHERE `user`.`ID` =?;");
			updateSentence.setString(1, us.getNick());
			updateSentence.setString(2, us.getPassword());
			updateSentence.setString(3, us.getName());
			updateSentence.setString(4, us.getSurname());
			updateSentence.setString(5, us.getEmail());
			updateSentence.setInt(6, us.getPhone());
			updateSentence.setString(7, us.getCountry());
			updateSentence.setString(8, us.getAddress());
			updateSentence.setBoolean(9, us.isAdministrator());
			updateSentence.setInt(10, us.getId());
            updateSentence.executeUpdate();			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public User loadUser(int id) {
		try{
			PreparedStatement selectSentence = null;
			ResultSet resultSet = null;
			selectSentence =
	            connection.prepareStatement(
"SELECT `ID` , `Nick` , `Password` , `Name` , `Surname` , `Email` , `Phone` , `Country` , `Address` , `Administrator`" +
"FROM `user`" +
"WHERE ID =?");
			selectSentence.setInt(1,id);
	        resultSet = selectSentence.executeQuery();
	        if(!resultSet.next()) return null;
	        return new User(resultSet.getInt("ID"),
	        		resultSet.getString("Nick"),
	        		resultSet.getString("Password"),
	        		resultSet.getString("Name"),
	        		resultSet.getString("Surname"),
	        		resultSet.getString("Email"),
	        		resultSet.getInt("Phone"),
	        		resultSet.getString("Country"),
	        		resultSet.getString("Address"),
	        		resultSet.getBoolean("Administrator"));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public User loadUser(String nick) {
		try{
			PreparedStatement selectSentence = null;
			ResultSet resultSet = null;
			selectSentence =
	            connection.prepareStatement(
"SELECT `ID` , `Nick` , `Password` , `Name` , `Surname` , `Email` , `Phone` , `Country` , `Address` , `Administrator`" +
"FROM `user`" +
"WHERE Nick LIKE ?");
			selectSentence.setString(1,nick);
	        resultSet = selectSentence.executeQuery();
	        if(!resultSet.next()) return null;
	        return new User(resultSet.getInt("ID"),
	        		resultSet.getString("Nick"),
	        		resultSet.getString("Password"),
	        		resultSet.getString("Name"),
	        		resultSet.getString("Surname"),
	        		resultSet.getString("Email"),
	        		resultSet.getInt("Phone"),
	        		resultSet.getString("Country"),
	        		resultSet.getString("Address"),
	        		resultSet.getBoolean("Administrator"));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<UserInfo> getFriends(int id) {
		try{
			PreparedStatement selectSentence = null;
			ResultSet resultSet = null;
			selectSentence =
	            connection.prepareStatement(
	            		"(SELECT IDUser1 ID " +
	            				"FROM friend " +
	            				"WHERE IDUser2=?) " +
	            				"UNION " +
	            				"(SELECT IDUser2 ID " +
	            				"FROM friend " +
	            				"WHERE IDUser1=?)");
	        			selectSentence.setInt(1, id);
	        			selectSentence.setInt(2, id);
	        resultSet = selectSentence.executeQuery();
	        ArrayList<UserInfo> ul = new ArrayList<UserInfo>();
	        while(resultSet.next()){
	        	User tu = loadUser(Integer.parseInt(resultSet.getString("ID")));
	        	ul.add(tu);
	        }
	        return ul;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<UserInfo> getFriends(int id, int count, int page) {
		try{
			PreparedStatement selectSentence = null;
			ResultSet resultSet = null;
			selectSentence =
	            connection.prepareStatement(
	            		"(SELECT IDUser1 ID " +
	            				"FROM friend " +
	            				"WHERE IDUser2=?) " +
	            				"UNION " +
	            				"(SELECT IDUser2 ID " +
	            				"FROM friend " +
	            				"WHERE IDUser1=?) " +
	            				"LIMIT ?,?");
	        			selectSentence.setInt(1, id);
	        			selectSentence.setInt(2, id);
	        			selectSentence.setInt(3, page*count);
	        			selectSentence.setInt(4, count);
	        resultSet = selectSentence.executeQuery();
	        ArrayList<UserInfo> ul = new ArrayList<UserInfo>();
	        while(resultSet.next()){
	        	User tu = loadUser(Integer.parseInt(resultSet.getString("ID")));
	        	ul.add(tu);
	        }
	        return ul;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<Note> getNotes(int id, int c) {
		try{
			PreparedStatement selectSentence = null;
			ResultSet resultSet = null;
			selectSentence =
	            connection.prepareStatement(
	            		"SELECT note.ID, Note, IDUser, DateTime, Longitude, Latitude, IDNote IS NOT NULL HasPhoto, Photo " +
	            		"FROM note LEFT OUTER JOIN photo ON note.ID=photo.IDNote " +
	            		"WHERE IDUser=? " +
	            		"GROUP BY note.ID" + " ORDER BY DateTime DESC LIMIT 0," + c);
	        			selectSentence.setInt(1, id);
	        resultSet = selectSentence.executeQuery();
	        ArrayList<Note> nl = new ArrayList<Note>();
	        while(resultSet.next()){
	        	Note n = new Note(resultSet.getInt("ID"),
	        			resultSet.getString("Note"),
	        			resultSet.getInt("IDUser"),
	        			new Position(resultSet.getFloat("Longitude"),
	        					resultSet.getFloat("Latitude"),
	        					resultSet.getDate("DateTime").toString()),
	        					resultSet.getBoolean("HasPhoto")
	        					);
	        	if(n.getHasPhoto()){
	        		Blob b = resultSet.getBlob("Photo");
		        	byte[] bs = b.getBytes(1, (int) b.length());
	        		n.setPhoto(new Photo(bs));
	        	}
	        	nl.add(n);
	        }
	        return nl;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Note getNote(int id) {
		try{
			PreparedStatement selectSentence = null;
			ResultSet resultSet = null;
			selectSentence =
	            connection.prepareStatement(
	            		"SELECT note.ID, Note, IDUser, DateTime, Longitude, Latitude, IDNote IS NOT NULL HasPhoto, Photo " +
	            		"FROM note LEFT OUTER JOIN photo ON note.ID=photo.IDNote " +
	            		"WHERE ID=?");
	        			selectSentence.setInt(1, id);
	        resultSet = selectSentence.executeQuery();
	        if(!resultSet.next()) return null;

	        	Note n = new Note(resultSet.getInt("ID"),
	        			resultSet.getString("Note"),
	        			resultSet.getInt("IDUser"),
	        			new Position(resultSet.getFloat("Longitude"),
	        					resultSet.getFloat("Latitude"),
	        					resultSet.getDate("DateTime").toString()),
	        					resultSet.getBoolean("HasPhoto")
	        					);
	        	if(n.getHasPhoto()){
	        		Blob b = resultSet.getBlob("Photo");
		        	byte[] bs = b.getBytes(1, (int) b.length());
	        		n.setPhoto(new Photo(bs));
	        	}
	        	return n;

		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<Position> getPositions(int id, int c) {
		try{
			PreparedStatement selectSentence = null;
			ResultSet resultSet = null;
			selectSentence =
	            connection.prepareStatement(
	            		"SELECT history.When, Latitude, Longitude " +
	            		"FROM history " +
	            		"WHERE ID=? " + " ORDER BY history.When DESC LIMIT 0," + c);
	        selectSentence.setInt(1, id);
	        resultSet = selectSentence.executeQuery();
			ArrayList<Position> pl = new ArrayList<Position>();
	        while(resultSet.next()){
	        	pl.add(new Position(resultSet.getFloat(2), 
	        			resultSet.getFloat(3),
	        			resultSet.getTimestamp(1).toString()));
	        }
	        return pl;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public boolean removeNote(int id) {
		PreparedStatement deleteSentence = null;
		try{
			deleteSentence = connection.prepareStatement(
					 "DELETE FROM `fflocation`.`user` " +
					 "WHERE ID=?;");
			deleteSentence.setInt(1, id);
            deleteSentence.executeUpdate();			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean addFriends(int tid1, int tid2) {
		int id1,id2;
		if(tid1==tid2) return true;
		if(tid1<tid2){
			id1=tid1;
			id2=tid2;
		}else{
			id1=tid2;
			id2=tid1;
		}
		PreparedStatement insertSentence = null;
		try{
			insertSentence = connection.prepareStatement(
					 "INSERT INTO `fflocation`.`friend` (`IDUser1`, `IDUser2`)" +
					 "VALUES ( ?, ?);");
			insertSentence.setInt(1, id1);
			insertSentence.setInt(2, id2);
            insertSentence.executeUpdate();			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean setNote(Note not) {
		PreparedStatement insertSentence = null;
		PreparedStatement selectSentence = null;
		ResultSet resultSet = null;
		if(not.getPosition().getDate()==null)
			not.getPosition().setDate(new Date().toString());
		try{
			insertSentence = connection.prepareStatement(
					 " INSERT INTO `fflocation`.`note` "+
					 "( `ID` ,`Note` ,`IDUser` ,`DateTime` ,`Latitude` ,`Longitude`) " +
					 "VALUES (NULL, ?, ?, NOW(), ?, ?)");
			insertSentence.setString(1, not.getNote());
			insertSentence.setInt(2, not.getOwner());
			insertSentence.setFloat(3, Float.parseFloat(not.getPosition().getLatitude()));
			insertSentence.setFloat(4, Float.parseFloat(not.getPosition().getLongitude()));
            insertSentence.executeUpdate();		
            
            selectSentence =
                connection.prepareStatement("SELECT LAST_INSERT_ID()");
            resultSet = selectSentence.executeQuery();
            if (resultSet.next()) not.setId(resultSet.getInt(1));
            return saveImage(not.getId(), not.getPhoto());
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean newRequest(int u1, int u2) {
		PreparedStatement insertSentence = null;
		try{
			insertSentence = connection.prepareStatement(
					 "INSERT INTO `fflocation`.`frequest` (`ID1`, `ID2`)" +
					 "VALUES ( ?, ?);");
			insertSentence.setInt(1, u1);
			insertSentence.setInt(2, u2);
            insertSentence.executeUpdate();			
		}catch(Exception e){
			//e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public List<UserInfo> getRequests(int u) {
		try{
			PreparedStatement selectSentence = null;
			ResultSet resultSet = null;
			selectSentence =
	            connection.prepareStatement(
	            		"SELECT ID1 FROM frequest WHERE ID2=?");
	        			selectSentence.setInt(1, u);
	        resultSet = selectSentence.executeQuery();
	        List<UserInfo> ul = new ArrayList<UserInfo>();
	        while(resultSet.next()){
	        	User tu = loadUser(Integer.parseInt(resultSet.getString("ID1")));
	        	ul.add(tu);
	        }
	        return ul;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public boolean exists(String nick) {
		try{
			PreparedStatement selectSentence = null;
			ResultSet resultSet = null;
			selectSentence =
	            connection.prepareStatement(
	            		"SELECT ID FROM user WHERE nick like ?");
	        			selectSentence.setString(1,nick);
	        resultSet = selectSentence.executeQuery();
	        return resultSet.next();
		}catch(Exception e){
			e.printStackTrace();
			return true;
		}
	}

	@Override
	public boolean delRequest(int u1, int u2) {
		PreparedStatement deleteSentence = null;
		try{
			deleteSentence = connection.prepareStatement(
					 "DELETE FROM `fflocation`.`frequest` " +
					 "WHERE ID1=? AND ID2=?;");
			deleteSentence.setInt(1, u1);
			deleteSentence.setInt(2, u2);
            deleteSentence.executeUpdate();			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean areFriends(int tid1, int tid2) {
		int id1,id2;
		if(tid1==tid2) return true;
		if(tid1<tid2){
			id1=tid1;
			id2=tid2;
		}else{
			id1=tid2;
			id2=tid1;
		}
		try{
			PreparedStatement selectSentence = null;
			ResultSet resultSet = null;
			selectSentence =
	            connection.prepareStatement(
	            		"SELECT * FROM friend WHERE IDUser1=? AND IDUser2=?");
	        			selectSentence.setInt(1, id1);
		        		selectSentence.setInt(2, id2);
	        resultSet = selectSentence.executeQuery();
	        if(resultSet.next()) return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean newPosition(int userID, float lat, float lon) {
		PreparedStatement insertSentence = null;
		try{
			insertSentence = connection.prepareStatement(
					 "INSERT INTO `fflocation`.`history` (`ID`, `When`, `Latitude`, `Longitude`) VALUES (" +
					 "?, NOW(), ?, ?);");
			insertSentence.setInt(1,userID);
			insertSentence.setFloat(2,lat);
			insertSentence.setFloat(3,lon);
            insertSentence.executeUpdate();		
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public List<UserInfo> searchFriend(String nick, String name,
			String surname, String country) {
		if(nick==null && name==null && surname==null && country==null) return null;
		
		try{
			PreparedStatement selectSentence = null;
			ResultSet resultSet = null;
			String select = "SELECT ID FROM user WHERE ";
			if(nick!=null) select+="(Nick LIKE '%" + nick + "%') AND ";
			if(name!=null) select+="(Name LIKE '%" + name + "%') AND ";
			if(surname!=null) select+="(Surname LIKE '%" + surname + "%') AND ";
			if(country!=null) select+="(Country LIKE '%" + country + "%') AND ";
			select=select.substring(0,select.length()-5);
			selectSentence =
	            connection.prepareStatement(select);
	        resultSet = selectSentence.executeQuery();
	        ArrayList<UserInfo> ul = new ArrayList<UserInfo>();
	        while(resultSet.next()){
	        	User tu = loadUser(Integer.parseInt(resultSet.getString("ID")));
	        	ul.add(tu);
	        }
	        return ul;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<UserInfo> getUsers(int count, int page) {
		try{
			PreparedStatement selectSentence = null;
			ResultSet resultSet = null;
			selectSentence =
	            connection.prepareStatement(
	            		"(SELECT ID " +
	            				"FROM user " +
	            				"LIMIT ?,?)");
	        			selectSentence.setInt(1, page*count);
	        			selectSentence.setInt(2, count);
	        resultSet = selectSentence.executeQuery();
	        ArrayList<UserInfo> ul = new ArrayList<UserInfo>();
	        while(resultSet.next()){
	        	User tu = loadUser(Integer.parseInt(resultSet.getString("ID")));
	        	ul.add(tu);
	        }
	        return ul;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	

	
	private boolean saveImage(int noteID, Photo p) {
		if(p==null) return true;
		byte[] photo = p.getPhoto();
		PreparedStatement insertSentence = null;
		try{
			insertSentence = connection.prepareStatement(
					"INSERT INTO `fflocation`.`photo` " +
					"(`IDNote`, `Photo`) " +
					"VALUES (?, ?);");
			insertSentence.setInt(1, noteID);
			insertSentence.setBlob(2, new ByteArrayInputStream(photo));
            insertSentence.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public boolean delUser(int id) {
		PreparedStatement deleteSentence = null;
		try{
			deleteSentence = connection.prepareStatement(
					 "DELETE FROM `fflocation`.`user` " +
					 "WHERE ID=?;");
			deleteSentence.setInt(1, id);
            deleteSentence.executeUpdate();			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
