package ntu.sce.fyp.easilocation.service;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;

public class LocationHelper {

	private static final int MAX_RESULTS = 10;
	
	private Context context;
	private LocationManager manager;
	private LocationProvider lowAccProvider;
	private LocationProvider highAccProvider;

	public LocationHelper(Activity main) {
		context = main.getApplicationContext();
		manager = (LocationManager) main.getSystemService(Context.LOCATION_SERVICE);
		// get low accuracy provider
		lowAccProvider = manager.getProvider(manager.getBestProvider(createCoarseCriteria(), true));
		// get high accuracy provider
		highAccProvider = manager.getProvider(manager.getBestProvider(createFineCriteria(), true));
	}

	/** this criteria will settle for less accuracy, power, and cost (Cell-Id, Wifi) */
	private static Criteria createCoarseCriteria() {
		Criteria c = new Criteria();
		c.setAccuracy(Criteria.ACCURACY_COARSE);
		c.setAltitudeRequired(false);
		c.setBearingRequired(false);
		c.setSpeedRequired(false);
		c.setCostAllowed(true);
		c.setPowerRequirement(Criteria.POWER_HIGH);
		return c;
	}

	/** this criteria will settle for high accuracy, power, and cost (GPS, AGPS) */
	private static Criteria createFineCriteria() {
		Criteria c = new Criteria();
		c.setAccuracy(Criteria.ACCURACY_FINE);
		c.setAltitudeRequired(false);
		c.setBearingRequired(false);
		c.setSpeedRequired(false);
		c.setCostAllowed(true);
		c.setPowerRequirement(Criteria.POWER_HIGH);
		return c;
	}

	public List<Address> reverseGeoCoding(Location loc) {
		double latitude = loc.getLatitude();
		double longitude = loc.getLongitude();
		
		Geocoder engine = new Geocoder(context, Locale.getDefault());
		try {
			List<Address> list = engine.getFromLocation(latitude, longitude, MAX_RESULTS);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public LocationManager getLocationManager() {
		return manager;
	}

	public LocationProvider getLowAccProvider() {
		return lowAccProvider;
	}

	public LocationProvider getHighAccProvider() {
		return highAccProvider;
	}
	
}
