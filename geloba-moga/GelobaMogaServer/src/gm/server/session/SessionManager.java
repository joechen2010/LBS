package gm.server.session;

import gm.server.communication.CommunicationsManager;
import gm.server.exception.SessionNotFoundException;
import gm.server.exception.UserNotFoundException;
import gm.server.geomanager.GeoManager;
import gm.server.persistence.PersistenceManager;
import gm.shared.utils.HashMaker;

import java.util.Calendar;
import java.util.HashSet;
import org.apache.log4j.Logger;

/**
 * Manages all logged in users.
 * 
 * @author voidStern
 */
public class SessionManager {
	
	private static SessionManager s = null;
	static Logger l = Logger.getLogger(SessionManager.class);
	private HashSet<Session> sessions;
	
	private SessionManager(){
		sessions = new HashSet<Session>();
	}
	
	/**
	 * Singelton!
	 * 
	 * @return the SessionManager Instance
	 */
	public static SessionManager getInstance(){
		if(s == null) return s = new SessionManager();
		else return s;
	}
	
	/**
	 * Starts a session for a specific user, but does not log
	 * him in. Call confirmSession() to do that.
	 * 
	 * @param username
	 * @return Sessionid
	 * @throws UserNotFoundException 
	 */
	public String startSessionForUser(String user) throws UserNotFoundException{
		String sid = HashMaker.md5(user + Calendar.getInstance().getTimeInMillis());
		Session s = null;
		
		for(Session se : sessions){
			if(se.getUser().getName().equals(user)){
				l.info("Session for user "+ user +" already in use. Removing.");
				sessions.remove(se);
			}
		}
		
		l.info("Starting session ( " + sid + " )  for User " + user + ".");
		s = new Session(sid, PersistenceManager.getInstance().getUserForName(user), false);
		sessions.add(s);
		return sid;
	}
	
	
	/**
	 * Ends the session with the given session id
	 * 
	 * @param sid
	 * @throws SessionNotFoundException
	 */
	public void deleteSession(String sid) throws SessionNotFoundException{
		
		Session s = getSessionForSessionId(sid);
		
		sessions.remove(s);

		CommunicationsManager.getInstance().userDidLogout(s);
		GeoManager.getInstance().removeMapable(s.getUser());
		
		l.info("Succesfully logged " + s.getUser() + " out.");
	}
	
	/**
	 * 
	 * @param sid
	 * @return The session object for the supplied session id
	 * @throws SessionNotFoundException
	 */
	public Session getSessionForSessionId(String sid) throws SessionNotFoundException{
		Session r = null;
		for(Session s : sessions){
			if(s.getSessionId().equalsIgnoreCase(sid)){
				r = s;
				l.info("Found User " + r.getUser());
			}
		}
		if(r == null) throw new SessionNotFoundException(sid);
		else return r;
	}
	
	/**
	 * Sends a message to all connected clients
	 * 
	 * @param m Message
	 */
	public void notfiyAllClients(String m){
		for(Session s : sessions){
			s.addMessage(m);
		}
	}
	
	/**
	 * Sends a message to a connected client with
	 * a specific session id.
	 * 
	 * @param sid Session ID
	 * @param m  Message
	 * @throws SessionNotFoundException
	 */
	public void notifyClient(String sid, String m) throws SessionNotFoundException{
		getSessionForSessionId(sid).addMessage(m);
	}
}
