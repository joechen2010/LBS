package gm.server.controller;


import org.apache.log4j.Logger;

import gm.server.session.Session;
import gm.shared.action.Action;

public abstract class AbstractServerController implements ServerController{
	
	static Logger l = Logger.getLogger(AbstractServerController.class);
	
	public AbstractServerController() {
		
	}

	public abstract void receiveNotification(Session s, String m);

	public Action runAction(Action a, Session s){
		l.info("Running action " + a.getClass().getName());
		a.atServer(s, this);	
		return a;	
	}

	public abstract void userDidLogin(Session s);

	public abstract void userDidLogout(Session s);
	
}
