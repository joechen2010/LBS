package gm.server.controller;

import gm.server.session.Session;
import gm.shared.action.Action;

public interface ServerController {
	public void userDidLogin(Session s);
	public void userDidLogout(Session s);
	public void receiveNotification(Session s, String m);
	public Action runAction(Action a, Session s);
}
