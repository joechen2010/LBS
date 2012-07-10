package gm.server.communication;

import java.io.IOException;

import gm.server.exception.SessionNotFoundException;
import gm.server.session.SessionManager;
import gm.shared.utils.Serializer;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Path("/notification/{session}")
public class Notifications {
	
	@GET
	@Produces("text/xml")
	public String getNotifications(@PathParam("session") String sid){
		
		try {
			if(!SessionManager.getInstance().getSessionForSessionId(sid).isOnline())
				throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		} catch (SessionNotFoundException e1) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		
		try {
			
			return Serializer.serialize(SessionManager.getInstance().getSessionForSessionId(sid).getMessages());
			
		} catch (IOException e) {
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		} catch (SessionNotFoundException e) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
	
	@POST
	@Produces("text/plain")
	@Consumes("text/plain")
	public String sendNotification(@PathParam("session") String sid, String m){
		
		try {
			if(!SessionManager.getInstance().getSessionForSessionId(sid).isOnline())
				throw new WebApplicationException(Response.Status.UNAUTHORIZED);
		} catch (SessionNotFoundException e1) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		
		try {
			
			SessionManager.getInstance().getSessionForSessionId(sid).addMessage(m);
			
		} catch (SessionNotFoundException e) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
		
		return "";
	}

}
