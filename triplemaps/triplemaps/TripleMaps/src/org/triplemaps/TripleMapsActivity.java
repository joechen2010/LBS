package org.triplemaps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.triplemaps.utils.GeoCoderUtils;
import org.triplemaps.utils.TripleMapOverlay;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class TripleMapsActivity extends MapActivity{

	private static final int TWO_MINUTES = 1000 * 60 * 2;
	private final String SEARCH_CONSTANT = "Mexican restaurants near ";
	
	private MapView mapView;
	private MapController mapController;
	private LocationManager locationManager;
	private LocationListener listner;
	private Location previousLocation;
	private TripleMapOverlay overlay;
	private Drawable pinIcon;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initMap();
	}
	
	public void initMap(){
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
		pinIcon = getResources().getDrawable(R.drawable.androidlogo);
		pinIcon.setBounds(0, 0, pinIcon.getIntrinsicWidth(), pinIcon.getIntrinsicHeight());
		initLocationManager();
	}
	
	protected void initLocationManager(){
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		listner = new LocationListener() {			
			public void onLocationChanged(Location location) {
				if(isBetterLocation(location, previousLocation)){
					previousLocation = location;
					showPinOverlayOnMap(location);
				}
			}
			
			public void onProviderDisabled(String provider) {
				if(provider.equalsIgnoreCase(LocationManager.GPS_PROVIDER))
					disableLocationUpdates();
			}
			
			public void onProviderEnabled(String provider) {
				if(provider.equalsIgnoreCase(LocationManager.GPS_PROVIDER))
					requestLocationUpdates(provider);
			}
			
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				
			}
		};
		requestLocationUpdates(LocationManager.GPS_PROVIDER);
	}
	
	protected void requestLocationUpdates(String provider){
		locationManager.requestLocationUpdates(provider, 0, 0, listner);
	}
	
	protected void disableLocationUpdates(){
		locationManager.removeUpdates(listner);
	}
	/**
	 * 
	 * @param geoPoint
	 */
	protected void drawMapToScreen(GeoPoint geoPoint){
		mapController.animateTo(geoPoint);
		mapController.setZoom(18);
		mapView.postInvalidate();
	}
	
	public void mapViewClickHandler(View target) {
		switch (target.getId()) {
		case R.id.sat:
			mapView.setSatellite(true);
			break;
		case R.id.street:
			mapView.setStreetView(true);
			break;
		case R.id.traffic:
			mapView.setTraffic(true);
			break;
		case R.id.normal:
			mapView.setSatellite(false);
			mapView.setStreetView(false);
			mapView.setTraffic(false);
			break;
		}
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
	    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
	    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer)
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    else if (isSignificantlyOlder)
	        return false;

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
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	/**
	 * 
	 * @param location
	 */
	protected void showPinOverlayOnMap(Location location){
		List<Overlay> overlays = mapView.getOverlays();
		if (overlays.size() > 0) {
			for (Iterator<Overlay> iterator = overlays.iterator(); iterator.hasNext();) {
				iterator.next(); iterator.remove();
			}
		}
		GeoPoint geoPoint = new GeoPoint((int)(location.getLatitude()*1E6),(int)(location.getLongitude()*1E6));
		
		overlay = new TripleMapOverlay(pinIcon, this);
		OverlayItem item = new OverlayItem(geoPoint, geoPoint.getLongitudeE6() + ","+geoPoint.getLatitudeE6(), null);
		overlay.addOverlay(item);
		
		new SearchLocationTask().execute(location);
	}
	
	/**
	 * 
	 * @param currentLocation
	 * @return List<GeoPoint>
	 */
	protected List<GeoPoint> searchBestLocations(Location currentLocation){
		StringBuffer localityName = new StringBuffer(SEARCH_CONSTANT);
		List<GeoPoint> geoPointList = new ArrayList<GeoPoint>(0);
		
		Geocoder geoCoder = new Geocoder(this);
		try {
			localityName.append(GeoCoderUtils.reverseGeocode(currentLocation));
			List<Address> addressList = geoCoder.getFromLocationName(localityName.toString(), 10);
			if(addressList!=null && addressList.size() > 0)
			{
				for(int index = 0; index < addressList.size(); index++){
					int lat = (int)(addressList.get(index).getLatitude()*1000000);
					int lng = (int)(addressList.get(index).getLongitude()*1000000);
					geoPointList.add(new GeoPoint(lat, lng));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return geoPointList;
	}
	
	/**
	 * 
	 * @param geoPointList
	 * @return List<OverlayItem>
	 */
	protected List<OverlayItem> addGeoPointsToOverlay(List<GeoPoint> geoPointList){
		List<OverlayItem> overlayItemList = new ArrayList<OverlayItem>(0);
		for (Iterator<GeoPoint> iterator = geoPointList.iterator(); iterator.hasNext();) {
			GeoPoint geoPoint = (GeoPoint) iterator.next();
			OverlayItem item = new OverlayItem(geoPoint, "", "");
			overlayItemList.add(item);
		}
		return overlayItemList;
	}
	
	private class SearchLocationTask extends AsyncTask<Location, Void, String> {
		private final ProgressDialog dialog = new ProgressDialog(TripleMapsActivity.this);

		// can use UI thread here
		protected void onPreExecute() {
			this.dialog.setTitle("Please wait..");
			this.dialog.setMessage("Searching for mexican restaurants..");
			this.dialog.show();
		}

		// automatically done on worker thread (separate from UI thread)
		protected String doInBackground(Location... args) {
			overlay.addOverlay(addGeoPointsToOverlay(searchBestLocations(args[0])));
			mapView.getOverlays().add(overlay);
			drawMapToScreen(overlay.getItem(0).getPoint());
			return "";
		}

		// can use UI thread here
		protected void onPostExecute(final String result) {
			if (this.dialog.isShowing()) {
				this.dialog.dismiss();
			}
		}
	}
}