package gm.server.geomanager;

import gm.shared.mapable.Mapable;

import java.util.HashSet;

import org.apache.log4j.Logger;

public class GeoManager {
	
	static private GeoManager gm = null;
	private HashSet<Mapable> hs = null;
	static Logger l = Logger.getLogger(GeoManager.class);
	
	private GeoManager(){
		hs = new HashSet<Mapable>();	
	}
	
	/**
	 * 
	 * @return the GeoManager instance
	 */
	public static GeoManager getInstance(){
		if(gm == null) gm = new GeoManager();
		return gm;
	}
	
	private boolean isInRect(Mapable m, Mapable r, float x, float y){
		return getPositiveDiffernce(m.getLatitude(), r.getLatitude()) <= x && getPositiveDiffernce(m.getLongitude(), r.getLongitude()) <= y;
	}
	
	private boolean isInCircle(Mapable m, Mapable c, float r){
		float a = getPositiveDiffernce(m.getLatitude(), c.getLatitude());
		float b = getPositiveDiffernce(m.getLongitude(), c.getLongitude());
		return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2)) <= r;
	}
	
	/**
	 * Adds a mapable to the GeoManager
	 * @param m the mapable to add
	 */
	public void addMapable(Mapable m){
		hs.add(m);
		l.info("Added Mapable: " + m);
	}
	
	/**
	 * Removes a mapable from the GeoManager
	 * @param m mapable to remove
	 */
	public void removeMapable(Mapable m){
		hs.remove(m);
		l.info("Removed Mapable: " + m);
	}
	
	/**
	 * Returns the absolute difference between a and b
	 * @return positive difference between a and b 
	 */
	private float getPositiveDiffernce(float a, float b){
		if (a < 0 && b >= 0)
			return b - a;
		else if (a >= 0 && b < 0)
			return a - b;
		else return Math.abs(a - b);
	}
	
	/**
	 * Returns all mapables near a specific mapable
	 * 
	 * @param m near this mapable
	 * @param x max x (latitude) distance
	 * @param y max y (longitude) distance
	 * @return HashSet of all near mapables
	 */
	public HashSet<Mapable> getMapablesNear(Mapable m, float x, float y){
		HashSet<Mapable> ret = new HashSet<Mapable>();
		for(Mapable a : hs){
			if(isInRect(a, m, x, y))
				ret.add(a);
		}
		return ret;
	}
	
	/**
	 * Returns all mapables in a circle around a specific mapable
	 * 
	 * @param m near this mapable
	 * @param r max radius
	 * @return HashSet of all near mapables
	 */
	public HashSet<Mapable> getMapablesNear(Mapable m, float r){
		HashSet<Mapable> ret = new HashSet<Mapable>();
		for(Mapable a : hs){
			if(isInCircle(a, m, r))
				ret.add(a);		
		}
		
		return ret;
	}

}
