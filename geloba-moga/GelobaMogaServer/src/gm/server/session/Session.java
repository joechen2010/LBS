package gm.server.session;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import gm.server.communication.CommunicationsManager;
import gm.server.exception.SessionNotFoundException;
import gm.server.exception.WrongPasswordException;
import gm.server.geomanager.GeoManager;
import gm.server.persistence.User;
import gm.shared.utils.HashMaker;

/**
 * Repressents an active session in the system
 * 
 * @author voidStern
 */
public class Session {
	private String sessionId;
	private User user;
	private boolean online;
	private ArrayList<String> messages;
	static Logger l = Logger.getLogger(Session.class);
	
	public ArrayList<String> getMessages() {
		return messages;
	}
	
	public void addMessage(String m){
		messages.add(m);
	}

	public Session(String sessionId, User user) {
		this.sessionId = sessionId;
		this.user = user;
	}
	
	public Session(String sessionId, User user, boolean online) {
		this.sessionId = sessionId;
		this.user = user;
		this.online = online;
	}
	
	public boolean isOnline() {
		return online;
	}
	
	public void setOnline(boolean online) {
		this.online = online;
	}
	public String getSessionId() {
		return sessionId;
	}
	public User getUser() {
		return user;
	}
	
	/**
	 * Activates a session, if the correct hash is supplied
	 * 
	 * @param session
	 * @param hash
	 * @return wherever everything was ok
	 * @throws SessionNotFoundException 
	 * @throws WrongPasswordException 
	 */
	public boolean confirm(String hash) throws SessionNotFoundException, WrongPasswordException{
		
		// Remove MD5 from Password as soon as passwords are no longer saved as clear text
		
		if(hash.equalsIgnoreCase(HashMaker.md5(sessionId + HashMaker.md5(user.getPassword())))){
			online = true;
			l.info("Succesfully logged " + user + " in.");
			CommunicationsManager.getInstance().userDidLogin(this);
			GeoManager.getInstance().addMapable(user);
		} else {
			throw new WrongPasswordException(user.getName());
		}
			
		
		return online;
	}

	@Override
	public boolean equals(Object o) {
		if(o.getClass() != Session.class) return false;
		return this.sessionId == ((Session)o).getSessionId();
	}

	@Override
	public String toString() {
		return this.sessionId;
	}

}
