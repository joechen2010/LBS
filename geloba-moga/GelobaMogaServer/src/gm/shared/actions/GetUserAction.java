package gm.shared.actions;

import gm.server.controller.ServerController;
import gm.server.persistence.User;
import gm.server.session.Session;
import gm.shared.action.AbstractAction;

public class GetUserAction extends AbstractAction {
	private static final long serialVersionUID = -4002728592426336617L;
	private User u = null;

	public GetUserAction() { }
	
	public User getU() {
		return u;
	}

	public void setU(User u) {
		this.u = u;
	}

	@Override
	public void afterServer() {

	}

	@Override
	public void atServer(Session s, ServerController sc) {
		this.u = s.getUser();
	}

	@Override
	public void beforeServer() {
	}
	
	public User getUser(){
		return u;
	}

}
