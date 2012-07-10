package gm.client.communication;

import java.io.IOException;
import java.util.HashSet;

import org.apache.http.client.ClientProtocolException;

import gm.client.exception.HTTPClientException;
import gm.client.geo.GPS;
import gm.server.persistence.User;
import gm.shared.actions.GetNearMapablesAction;
import gm.shared.actions.GetUserAction;
import gm.shared.actions.UpdatePositionAction;
import gm.shared.mapable.Mapable;

/**
 * GeoManager Singleton
 * @author stefan
 *
 */
public class GeoManager {
	/**
	 * TAG the current Activity
	 * used my logging
	 */
	private static final String TAG = "GeoManager";
	
	private static GeoManager me = new GeoManager();
	
	private GeoManager() {	}
	
	public static GeoManager get() {
		return GeoManager.me;
	}
	
	/**
	 * See foc of @see gm.shared.actions.UpdatePositionAction.UpdatePositionAction
	 * 
	 * @throws ClientProtocolException
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws HTTPClientException
	 */
	public void setPos() throws ClientProtocolException, IllegalStateException, IOException, ClassNotFoundException, HTTPClientException {
		this.setPos(GPS.getLat(), GPS.getLon());
	}
	
	/**
	 * See doc of @see gm.shared.actions.UpdatePositionAction.UpdatePositionAction
	 * 
	 * @param lat
	 * @param lon
	 * @throws ClientProtocolException
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws HTTPClientException
	 */
	public void setPos(long lat, long lon) throws ClientProtocolException, IllegalStateException, IOException, ClassNotFoundException, HTTPClientException {
		UpdatePositionAction myPos = new UpdatePositionAction();
    
    	myPos.setLatitude(lat);
    	myPos.setLongitude(lon);
    	
    	Communicator.get().applyAction(myPos);
	}
	
	/**
	 * See doc of @see gm.shared.actions.GetNearMapablesAction
	 * @param me
	 * @param x
	 * @param y
	 * @return
	 * @throws ClientProtocolException
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws HTTPClientException
	 */
	
	public HashSet<Mapable> getNearUser(User me, int x, int y) throws ClientProtocolException, IllegalStateException, IOException, ClassNotFoundException, HTTPClientException {
		return ((GetNearMapablesAction) 
				Communicator.get().applyAction(
						new GetNearMapablesAction(
									me,x,y
							)
						)
				).getResult();
	}
}
