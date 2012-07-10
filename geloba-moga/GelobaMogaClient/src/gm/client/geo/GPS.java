package gm.client.geo;

import android.content.Context; 
import android.location.Criteria; 
import android.location.Location; 
import android.location.LocationManager; 
import android.location.LocationListener; 
import android.os.Bundle;
import android.util.Log;

/**
 * Location-Klasse
 * Stellt die aktuelle Position des Clients bereit.
 * @author stefan
 * 
 */
public class GPS {
	
	private static LocationManager lm = null; 
	private static String provider = null;
	
	private static long lat = 0;
	private static long lon = 0;
	
	private static int load_time = 5000;
	
	private static int load_distance = 5;
	
	//private GPS me = new GPS();
	
	//private Context parent = null;
	private GPS() { }
	
	public static void init(Context context) {
		lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		//this.parent = context;
		
		findProvider();
		
		LocationListener mll = new LocationListener() { 
			  public void onLocationChanged(Location location) { 
				  Log.d("GPS", "locationChanged");
				  refresh();
				  Log.d("GPS", " now at " +lat + "/" +lon);
			  } 
			 
			  public void onProviderDisabled(String provider){ 
				  Log.d("GPS", "ProviderDisabled");
				  refresh();
				  Log.d("GPS", "provider changed to " +provider.toString());
				  Log.d("GPS", " now at " +lat + "/" +lon);
			  } 
			  public void onProviderEnabled(String provider){ 
				  Log.d("GPS", "ProviderEnabled");
				  refresh();
				  Log.d("GPS", "provider changed to " +provider);
				  Log.d("GPS", " now at " +lat + "/" +lon);
			  } 
			  public void onStatusChanged(String provider, int status, Bundle extras){ 
				  Log.d("GPS", "StatusChanged");
				  refresh();
				  Log.d("GPS", "provider changed to " +provider);
				  Log.d("GPS", " now at " +lat + "/" +lon);
			  } 
			  
			  
			  private void refresh() {
				  Log.d("GPS","refresh");
				  findProvider();
				  findLocation();
				  //parent.locationChanged();
			  }
			}; 
			
			// Werte nach denen Location neu geladen werden soll:
			int t = load_time;
			int distance = load_distance;
			
			lm.requestLocationUpdates(provider, t, distance, mll); 
			//lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, t, distance, mll);
	}
	
	protected static void findProvider() {
		// Kriterien setzen:
		Criteria criteria = new Criteria(); 
		criteria.setAccuracy(Criteria.ACCURACY_COARSE); 
		criteria.setPowerRequirement(Criteria.POWER_LOW); 
		criteria.setAltitudeRequired(false); 
		criteria.setBearingRequired(false); 
		criteria.setSpeedRequired(false); 
		criteria.setCostAllowed(false); 
		
		provider = lm.getBestProvider(criteria, true); 
	}
	
	protected static void findLocation() {
		Location location = lm.getLastKnownLocation(provider); 
		
		if (location != null)
		{
			lat = (long) location.getLatitude();
			lon = (long) location.getLongitude();
		}
	}
	
	/*public String test() {
		String foo = "";
		
		findLocation();
		
		foo = "(" + lat + "/" + lon + ")";
		return foo;
	}*/

	/**
	 * @return the lat
	 */
	public static long getLat() {
		return lat;
	}

	/**
	 * @return the lon
	 */
	public static long getLon() {
		return lon;
	}
}



