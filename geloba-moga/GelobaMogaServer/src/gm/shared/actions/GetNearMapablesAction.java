package gm.shared.actions;

import java.util.HashSet;

import gm.server.controller.ServerController;
import gm.server.geomanager.GeoManager;
import gm.server.session.Session;
import gm.shared.action.AbstractAction;
import gm.shared.mapable.Mapable;

public class GetNearMapablesAction extends AbstractAction {
	private static final long serialVersionUID = 5298058606194887941L;
	private Mapable m;
	private float x;
	private float y;
	HashSet<Mapable> result;

	public HashSet<Mapable> getResult() {
		return result;
	}
	
	public GetNearMapablesAction() {  }

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

	/**
	 * @return the m
	 */
	public Mapable getM() {
		return m;
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(HashSet<Mapable> result) {
		this.result = result;
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
