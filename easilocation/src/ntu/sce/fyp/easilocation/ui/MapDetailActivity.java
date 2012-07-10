package ntu.sce.fyp.easilocation.ui;

import java.util.List;

import ntu.sce.fyp.easilocation.R;
import ntu.sce.fyp.easilocation.util.UIUtils;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

public class MapDetailActivity extends MapActivity{

	private MapView mView;
	private MapController mController;
	private MyLocationOverlay mCurrentLocation;
	private Location currentLoc;
	
	/** Override some methods of an {@link Activity}*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_detail);
		initMapView();
		
		/** obtain overlay items list which will be held by this map view*/
		List<Overlay> mapOverlays = mView.getOverlays();
		mCurrentLocation = new MyLocationOverlay(getApplicationContext(), mView);
		mapOverlays.add(mCurrentLocation);
		
		/** read location fix from bundle */
//		savedInstanceState.get
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (mCurrentLocation.enableMyLocation()) {
			mCurrentLocation.enableCompass();
			/*mCurrentLocation.runOnFirstFix(new Runnable() {
				@Override
				public void run() {
					Location temp = mCurrentLocation.getLastFix();
					if (temp != null) {
						moveToLocation(temp);
					}
				}
			});*/
		} else {
			Intent locationSetting = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(locationSetting);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mCurrentLocation.disableMyLocation();
		mCurrentLocation.disableCompass();
	}
	/** End of overriding default methods */
	
	private void initMapView() {
		((TextView)findViewById(R.id.title_text)).setText("Map");
		mView = (MapView) findViewById(R.id.mapview);
		mView.setSatellite(false);
		mView.setTraffic(false);
		mView.setStreetView(false);
		mView.setBuiltInZoomControls(true);
		mController = mView.getController();
		mController.setZoom(15);
	}
	
	private void moveToLocation (Location loc) {
		Double latitude = loc.getLatitude() * 1E6;
		Double longitude = loc.getLongitude() * 1E6;
		GeoPoint location = new GeoPoint(latitude.intValue(), longitude.intValue());
		mController.animateTo(location);
		Toast.makeText(getApplicationContext(), "We are currently at " + location.getLatitudeE6() + " " + location.getLongitudeE6(), Toast.LENGTH_SHORT).show();
	}
	
	/** Handle "home" title-bar action. */
    public void onHomeClick(View v) {
        UIUtils.goHome(this);
    }
    
    /** Handle "search" title-bar action. */
    public void onSearchClick(View v) {
    	UIUtils.goSearch(this);
    }
    
    /** Handle "refresh" title-bar action. */
	public void onMyLocationClick(View v) {
		currentLoc = mCurrentLocation.getLastFix();
		if (currentLoc!=null) {
			moveToLocation(currentLoc);	
		} else {
			Toast.makeText(getApplicationContext(), "Location currently unavailable", Toast.LENGTH_SHORT).show();
		}
	}
	
	/** Handles menu items' creation and responses to menu items' click */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_map_activity, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.submenu_button_1:
			if (!item.isChecked()){
				mView.setSatellite(false);
				mView.setTraffic(false);
				mView.setStreetView(false);
				item.setChecked(true);
			}
			return true;
		case R.id.submenu_button_2:
			if (!item.isChecked()) {
				mView.setSatellite(true);
				item.setChecked(true);
			}
			return true;
		case R.id.submenu_button_3:
			if (!item.isChecked()) {
				mView.setTraffic(true);
				item.setChecked(true);
			}
			return true;
		case R.id.submenu_button_4:
			if (!item.isChecked()) {
				mView.setStreetView(true);
				item.setChecked(true);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Every MapActivity requires this method
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	protected boolean isLocationDisplayed() {
		return true;
	}

	/**
	 * To manipulate some key-down events
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_I:
			mController.zoomIn();
			break;
		case KeyEvent.KEYCODE_O:
			mController.zoomOut();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}