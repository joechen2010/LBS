package controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import model.User;

/**
 * This class takes care of the client's sessions.
 * Keeps the information about the authentication objects
 * and makes the client's session be destroyed after a while.
 */
public class Auths extends Thread {
	
	private final static long sessionLive = 900000; //How much does the Auth object last
	private final static long checkLive = 5000; //How often do you check for old Auth objects
	
	
	private Map<Auth, Long> authList = new ConcurrentHashMap<Auth, Long>();
	
	private Auths(){};
	
	/**
	 * Checks if an authentication object is correct and
	 * returns the owners id.
	 * @param a The authentication object.
	 * @return The user's id or null if information is wrong.
	 */
	public Integer getUser(Auth a) {
		Integer uid = a.getUserID();
			if(authList.put(a, System.currentTimeMillis())==null){
				authList.remove(a);
				uid = null;
			}
		return uid;
	}
	
	/**
	 * Logs in a user in the web service.
	 * If the authentication data is correct
	 * a authentication object is returned.
	 * @param u The user in the database.
	 * @param pw The password that was sent.
	 * @return The authentication object
	 * or null if the information was wrong.
	 */
	public Auth login(User u, String pw) {
		if (u != null && u.getPassword().equals(pw)) {			
			Auth a = new Auth(u.getId());
				while(authList.containsKey(a))
					a = new Auth(u.getId());
				authList.put(a, System.currentTimeMillis());
			return a;
		}
		return null;
	}

	
	/**
	 * The thread that keeps on checking the authentications.
	 */
	@Override
	public void run(){
		while(true){
			try {
				Thread.sleep(checkLive);
				check();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/* Singleton pattern */
	private static Auths instance = null;
	public synchronized static Auths getInstance(){
			if(instance==null){
				instance = new Auths();
				instance.start();
			}
		return instance;
	}
	
	/**
	 * Check with authentications had expired and removes them.
	 */
	private void check(){
		long limit = System.currentTimeMillis() - sessionLive;
		for(Auth a : authList.keySet())
			if(authList.get(a)<limit)
				authList.remove(a);
	}
}
