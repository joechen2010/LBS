package gm.shared.action;

import gm.server.controller.ServerController;
import gm.server.session.Session;

/**
 * Abstract class, that implements the Action interface and
 * the apply method, so only the three *Server() methods should be 
 * be overwritten
 * 
 * @author voidstern
 * @author stefan
 */
public abstract class AbstractAction implements Action {	
	private static final long serialVersionUID = 2904832879775278747L;

	/*
	 * Transfers all data to the server
	 * and receives the processed data.
	 * 
	 * @return Wherever communication was successful
	 */
	/*public boolean apply(){
		return false;
	}*/

	public abstract void atServer(Session s, ServerController sc);
	public abstract void beforeServer();
	public abstract void afterServer();		

}
