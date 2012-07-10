package gm.server.controller;

import org.apache.log4j.Logger;

import gm.server.session.Session;

public class SampleServerController extends AbstractServerController{
	
	static Logger l = Logger.getLogger(SampleServerController.class);

	@Override
	public void receiveNotification(Session s, String m) {
		l.info("Received Notification " + m + " from User " + s.getUser());
	}

	@Override
	public void userDidLogin(Session s) {
		l.info("User logged in " + s.getUser());
	}

	@Override
	public void userDidLogout(Session s) {
		l.info("User logged in " + s.getUser());
	}

}
