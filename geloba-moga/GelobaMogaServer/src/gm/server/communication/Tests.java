package gm.server.communication;


import gm.server.persistence.Item;
import gm.server.persistence.Mapableitem;
import gm.server.persistence.Mapableposition;
import gm.server.persistence.PersistenceManager;
import gm.server.persistence.User;
import gm.server.session.SessionManager;
import gm.shared.utils.HashMaker;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;

@Path("/test/{session}")
public class Tests {
	static Logger l = Logger.getLogger(Tests.class);
	
	public void createUser(String user){
		l.info("Started creating test for user " + user);
		
		l.info("Creating item");
		Item i = new Item();
		i.setName(user);
		PersistenceManager.getInstance().persist(i);
		
		l.info("Creating position");
		Mapableposition mp = new Mapableposition();
		mp.setLatitude((float) 0.6);
		mp.setLongitude((float) 0.9);
		mp.setTimestamp(new Date());
		PersistenceManager.getInstance().persist(mp);
	
		l.info("Creating MapableItem");
		Mapableitem mi = new Mapableitem();
		mi.setItem(i);
		mi.setCurrentPosition(mp);
		PersistenceManager.getInstance().persist(mi);
		
		l.info("Creating User");
		User u = new User();
		u.setPassword(user);
		u.setName(user);
		u.setMapableitem(mi);
		
		l.info("Writing to db");
		PersistenceManager.getInstance().persist(u);	
		PersistenceManager.getInstance().commit();
	}
	
	public void login(String user){
		l.info("Started Login test Test for getting user " + user);
		String sid;
		try {
			l.info("Starting Session");
			sid = SessionManager.getInstance().startSessionForUser("asdf");
			l.info("Confirming Session");
			String hash = HashMaker.md5(sid + HashMaker.md5("asdf"));
			boolean x = SessionManager.getInstance().getSessionForSessionId(sid).confirm(hash);
			if(x) l.info("Login successful");
			else l.info("Fail");
		} catch (Exception e1) {
			
		} 
	}
	
	@GET
	@Produces("text/plain")
	public String getSessionID(@PathParam("session") String user){
		
		createUser(user);

		return "Thus spoke Zarathustra";
		
//		try {
//			return PersistenceManager.getInstance().getUserForName(user).toString();
//		} catch (UserNotFoundException e) {
//			return "User " + e.getMessage() + " was not found.";
//		}
	}
}
