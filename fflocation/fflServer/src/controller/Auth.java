package controller;

import java.util.Random;

/**
 * This class holds a authentication object information.
 * This class creates objects that are going to be sent
 * to the user so this can be authenticated.
 * This authentication information consists of
 * the user ID and a randomly generated integer.
 */
public class Auth {
	
	public Auth(){}

	public void setSession(int session) {
		this.session = session;
	}
	public int getSession() {
		return session;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getUserID() {
		return userID;
	}

	private int session;
	private int userID;
	
	public Auth(int uID){
		userID = uID;
		Random generator = new Random();
		session=generator.nextInt();
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Auth))
			return false;
		Auth a = (Auth) o;
		return (userID==a.userID && session==a.session);
	}
	
	@Override
	public int hashCode(){
		return this.userID;
	}
	
	public String toString() {
		return "Auth -> " + userID + "(" + session + ")";
	}

}
