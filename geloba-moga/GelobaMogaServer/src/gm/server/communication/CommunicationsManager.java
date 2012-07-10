package gm.server.communication;

import gm.server.controller.SampleServerController;
import gm.server.controller.ServerController;
import gm.server.session.Session;
import gm.shared.action.Action;

public class CommunicationsManager {
	private static CommunicationsManager cm = null;
	private ServerController sc;
	
	private CommunicationsManager () {
		ServerController s = new SampleServerController();
		this.sc = s;
	}
	
	public static CommunicationsManager getInstance(){
		if(cm == null) cm = new CommunicationsManager();
		return cm;
	}
	
	public void registerServerController(ServerController sc){
		this.sc = sc;
	}
	
	public Action runAction(Action a, Session s){
		return sc.runAction(a, s);
	}
	
	public void receiveNotification(Session s, String m){
		sc.receiveNotification(s, m);
	}
	
	public void userDidLogin(Session s){
		sc.userDidLogin(s);
	}
	
	public void userDidLogout(Session s){
		sc.userDidLogout(s);
	}

}
