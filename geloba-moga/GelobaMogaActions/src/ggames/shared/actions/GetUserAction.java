package ggames.shared.actions;

import ggames.server.controller.ServerController;
import ggames.server.persistence.User;
import ggames.server.session.Session;
import ggames.shared.action.AbstractAction;

public class GetUserAction extends AbstractAction {
	private static final long serialVersionUID = -4002728592426336617L;
	private User u = null;

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
