package ntu.sce.fyp.easilocation.ui;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ntu.sce.fyp.easilocation.R;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MyLocationActivity extends Activity implements LocationListener{
	private static final String TAG = "MyLocationActivity";
	private static final int LOC_UPDATE_INTERVAL = 1000 * 60 * 2;
	private static final int LOC_UPDATE_RADIUS = 1000 * 60 * 2;
	
	private TextView mLocationText;
	private Context appContext;
	private LocationManager mLocManager;
	private String provider;
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		appContext = getApplicationContext();
		
		mLocationText = (TextView) findViewById(R.id.testText);
		
		mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		Criteria mCriteria = new Criteria();
		mCriteria.setAccuracy(Criteria.ACCURACY_FINE);
		mCriteria.setAltitudeRequired(false);
		mCriteria.setBearingRequired(false);
		mCriteria.setCostAllowed(false);
		mCriteria.setPowerRequirement(Criteria.POWER_MEDIUM);
		mCriteria.setSpeedRequired(false);
		
//		provider = LocationManager.NETWORK_PROVIDER;
		provider = mLocManager.getBestProvider(mCriteria, true);
		Location curLocation = mLocManager.getLastKnownLocation(provider);
		
		updateWithNewLocation (curLocation);
		
	}
	
	@Override
	public void onResume() {
		mLocManager.requestLocationUpdates(provider, LOC_UPDATE_INTERVAL, LOC_UPDATE_RADIUS, this);
		super.onResume();
	}
	
	@Override
	public void onPause() {
		mLocManager.removeUpdates(this);
		super.onPause();
	}
	
	private void updateWithNewLocation (Location location) {
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		int maxResults = 1;
		
		StringBuilder sb = new StringBuilder();
		sb.append("We are at:\n");
		sb.append("Accuracy: " + location.getAccuracy() + "\n");
		sb.append("Altitude: " + location.getAltitude() + "\n");
		sb.append("Bearing: " + location.getBearing() + "\n");
		sb.append("Latitude: " + location.getLatitude() + "\n");
		sb.append("Longitude: " + location.getLongitude() + "\n");
		sb.append("Provider: " + location.getProvider() + "\n");
		sb.append("Speed: " + location.getSpeed() + "\n");
		sb.append("Time: " + location.getTime() + "\n\n");
		
		/*// reverse geocoding: translate GeoPoint into meaningful address
		Geocoder reverseGeocoding = new Geocoder(getApplicationContext(), Locale.getDefault());
		try {
			List<Address> addresses = reverseGeocoding.getFromLocation(latitude, longitude, maxResults);
			if (addresses.size() > 0) {
				sb.append("Street address:\n");
				Address address = addresses.get(0);
				for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
					sb.append(address.getAddressLine(i) + "\n");
				}
				sb.append("Locality: " + address.getLocality() + "\n");
				sb.append("Admin area: " + address.getAdminArea() + "\n");
				sb.append("Country code: " + address.getCountryCode() + "\n");
				sb.append("Country name: " + address.getCountryName() + "\n");
				sb.append("Feature name: " + address.getFeatureName() + "\n");
				sb.append("Premises: " + address.getPremises() + "\n");
				sb.append("Phone: " + address.getPhone() + "\n");
				sb.append("Postal code: " + address.getPostalCode() + "\n");
				sb.append("Through fare: " + address.getThoroughfare() + "\n");
			} else {

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		mLocationText.setText(sb.toString());
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Toast.makeText(appContext, "New location found",Toast.LENGTH_SHORT).show();
		updateWithNewLocation (location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Toast.makeText(appContext, "Location Provider disabled",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Toast.makeText(appContext, "Location Provider enabled",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		Toast.makeText(appContext, "Location Provider Hardware status changed",Toast.LENGTH_SHORT).show();
	}
	
	/** Determines whether one Location reading is better than the current Location fix
	  * @param location  The new Location that you want to evaluate
	  * @param currentBestLocation  The current Location fix, to which you want to compare the new one
	  */
	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    boolean isSignificantlyNewer = timeDelta > LOC_UPDATE_INTERVAL;
	    boolean isSignificantlyOlder = timeDelta < -LOC_UPDATE_INTERVAL;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	    // Check if the old and new location are from the same provider
	    boolean isFromSameProvider = isSameProvider(location.getProvider(),
	            currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isLessAccurate) {
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	        return true;
	    }
	    return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}
	
}
