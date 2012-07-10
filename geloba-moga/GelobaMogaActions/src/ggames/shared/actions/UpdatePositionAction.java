package ggames.shared.actions;

import ggames.server.controller.ServerController;
import ggames.server.session.Session;
import ggames.shared.action.AbstractAction;

public class UpdatePositionAction extends AbstractAction {
	private static final long serialVersionUID = -7898107305104436406L;
	private long latitude;
	private long longitude;

	public UpdatePositionAction() { 
		// do nothing. use the setter
	}
	
	public UpdatePositionAction(long latitude, long longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	@Override
	public void afterServer() {

	}

	@Override
	public void atServer(Session s, ServerController sc) {
		s.getUser().getMapableitem().getCurrentPosition().setLatitude(latitude);
		s.getUser().getMapableitem().getCurrentPosition().setLongitude(longitude);
	}

	public void setLatitude(long latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(long longitude) {
		this.longitude = longitude;
	}

	@Override
	public void beforeServer() {

	}

}
