package controller;

/**
 * This class makes the Auth object type ready to be sent.
 */
public class SAuth extends Auth {
	public SAuth(Auth a){
		this.setUserID(a.getUserID());
		this.setSession(a.getSession());
	}
}
