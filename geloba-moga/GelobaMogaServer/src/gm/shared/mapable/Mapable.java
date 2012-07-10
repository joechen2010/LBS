package gm.shared.mapable;

import java.io.Serializable;
import java.util.Date;

/**
 * All classes implementing this interface can
 * be displayed on a map, and can be managed by
 * the GeoManager
 * 
 * @author voidstern
 *
 */
public interface Mapable extends Serializable{
	public float getLatitude();
	public float getLongitude();
	public Date getTimestamp();
}
