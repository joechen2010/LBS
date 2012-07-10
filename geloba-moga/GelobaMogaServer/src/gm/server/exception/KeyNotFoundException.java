package gm.server.exception;

import org.apache.log4j.Logger;

public class KeyNotFoundException extends Exception {
	private static final long serialVersionUID = -8521696532384857548L;

	public KeyNotFoundException(String key) {
		super(key);
		Logger l = Logger.getLogger(KeyNotFoundException.class);
		l.warn("Property with key " + key + " was not found");
	}	
}
