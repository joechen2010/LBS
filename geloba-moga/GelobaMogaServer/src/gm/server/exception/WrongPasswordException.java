package gm.server.exception;

import org.apache.log4j.Logger;

public class WrongPasswordException extends Exception {
	private static final long serialVersionUID = -8521696532384857548L;

	public WrongPasswordException(String user) {
		super(user);
		Logger l = Logger.getLogger(WrongPasswordException.class);
		l.info("User " + user + " used a wrong password");
	}	
}
