package gm.server.exception;

import org.apache.log4j.Logger;

public class SessionNotFoundException extends Exception {
	private static final long serialVersionUID = -8521696532384857548L;

	public SessionNotFoundException(String sid) {
		super(sid);
		Logger l = Logger.getLogger(SessionNotFoundException.class);
		l.warn("Session " + sid + " was not found");
	}	
}
