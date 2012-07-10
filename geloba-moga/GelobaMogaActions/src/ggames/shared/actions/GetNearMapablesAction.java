package ggames.shared.actions;

import java.util.HashSet;

import ggames.server.controller.ServerController;
import ggames.server.geomanager.GeoManager;
import ggames.server.session.Session;
import ggames.shared.action.AbstractAction;
import ggames.shared.mapable.Mapable;

public class GetNearMapablesAction extends AbstractAction {
	private static final long serialVersionUID = 5298058606194887941L;
	private Mapable m;
	private float x;
	private float y;
	HashSet<Mapable> result;

	public HashSet<Mapable> getResult() {
		return result;
	}

	public GetNearMapablesAction(Mapable m, float x, float y) {
		super();
		this.m = m;
		this.x = x;
		this.y = y;
	}

	public void setM(Mapable m) {
		this.m = m;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	@Override
	public void afterServer() {

	}

	@Override
	public void atServer(Session s, ServerController sc) {
		result = GeoManager.getInstance().getMapablesNear(m, x, y);
	}

	@Override
	public void beforeServer() {

	}
}
