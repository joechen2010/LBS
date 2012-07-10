package gm.server.communication;

import java.io.IOException;

import gm.server.controller.SampleServerController;
import gm.server.exception.SessionNotFoundException;
import gm.server.session.Session;
import gm.server.session.SessionManager;
import gm.shared.action.Action;
import gm.shared.utils.Serializer;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

@Path("/action/{session}")
public class Actions {
	static Logger l = Logger.getLogger(SampleServerController.class);
	
	@POST
	@Produces("text/xml")
	public String computeAction(@PathParam("session") String sid, String action){
		Action a;
		String ret = "";
		Session s;
		
		
		try {
			s = SessionManager.getInstance().getSessionForSessionId(sid);
			if(!s.isOnline())		
				throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		} catch (SessionNotFoundException e1) {
			throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		}
		
		try {
			l.info("Got Action String " + action);
			a = (Action) Serializer.deserialize(action);
			l.info("Serialized to " + a);
			ret = Serializer.serialize(CommunicationsManager.getInstance().runAction(a, s));
			l.info("Returning " + ret);
		} 
		catch (IOException e){
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
		catch (ClassNotFoundException e){
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
		
		return ret;
	}
}
