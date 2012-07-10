package gm.server.exception;

import org.apache.log4j.Logger;

public class UserNotFoundException extends Exception {
	private static final long serialVersionUID = -8521696532384857548L;

	public UserNotFoundException(String user) {
		super(user);
		Logger l = Logger.getLogger(UserNotFoundException.class);
		l.info("User " + user + " was not found");
	}	
}