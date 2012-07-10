package gm.shared.actions;

import gm.server.controller.ServerController;
import gm.server.session.Session;
import gm.shared.action.AbstractAction;

public class UpdatePositionAction extends AbstractAction {
	private static final long serialVersionUID = -7898107305104436406L;
	private long latitude;
	private long longitude;

	public UpdatePositionAction() { }
	
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

	/**
	 * @return the latitude
	 */
	public long getLatitude() {
		return latitude;
	}

	/**
	 * @return the longitude
	 */
	public long getLongitude() {
		return longitude;
	}

}
