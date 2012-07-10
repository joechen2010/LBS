package gm.server.communication;

import gm.server.exception.SessionNotFoundException;
import gm.server.exception.UserNotFoundException;
import gm.server.exception.WrongPasswordException;
import gm.server.session.SessionManager;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;


@Path("/session/{session}")
public class Sessions {
	
	@GET
	@Produces("text/plain")
	public String getSessionID(@PathParam("session") String user){
		try {
			
			return SessionManager.getInstance().startSessionForUser(user);
			
		} catch (UserNotFoundException e) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
	
	
	@POST
	@Produces("text/plain")
	@Consumes("text/plain")
	public String start(@PathParam("session") String sid, String hash){
		try {
			if(SessionManager.getInstance().getSessionForSessionId(sid).confirm(hash))
				return "true";
		} catch (SessionNotFoundException e) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		} catch (WrongPasswordException e) {
			throw new WebApplicationException(Response.Status.FORBIDDEN);
		}
		return "false";
	}
	
	
	@DELETE
	@Produces("text/plain")
	public String delete(@PathParam("session") String sid){
		try {
			SessionManager.getInstance().deleteSession(sid);
			return "true";
		} catch (SessionNotFoundException e) {
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
}
