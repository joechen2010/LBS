package ntu.sce.fyp.easilocation.ui;

import ntu.sce.fyp.easilocation.R;
import ntu.sce.fyp.easilocation.service.LocationHelper;
import ntu.sce.fyp.easilocation.service.ReverseGeocoderTask;
import ntu.sce.fyp.easilocation.util.UIUtils;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity implements LocationListener {

	private static final String TAG = "HomeActivity";
	
	private static final int MENU_UPDATE_LOCATION = 0;
	private static final int MENU_LOGOUT = 1;

	
	private static LocationHelper locHelper;
	private static LocationManager locManager;
	private static Location currentLoc;
	private static TextView locText;
	
	private SharedPreferences mSharedPreferences;
	private boolean remember_login = false;
	private String username;
	private String password;
	
	/** override some methods for {@link Activity} */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		((TextView)findViewById(R.id.title_text)).setText("EasiLocation");
		locText = (TextView) findViewById(R.id.location_text);
		
		// obtain user's remember login preference
		mSharedPreferences = getSharedPreferences("CurrentUser",MODE_PRIVATE);
		username = mSharedPreferences.getString("username", "");
		password = mSharedPreferences.getString("password", "");
		if (mSharedPreferences.contains("remember_login")) {
			String remember = mSharedPreferences.getString("remember_login", "");
			if (remember.equalsIgnoreCase("yes")) {
				remember_login = true;
			} else {
				remember_login = false;
			}
		}
		
		locHelper = new LocationHelper(this);
		locManager = locHelper.getLocationManager();
		currentLoc = locManager.getLastKnownLocation(locHelper.getLowAccProvider().getName());
		if (currentLoc != null) {
			new ReverseGeocoderTask(locText).execute(currentLoc);	
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		locManager.requestLocationUpdates(locHelper.getLowAccProvider().getName(), 0, 0, this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		locManager.removeUpdates(this);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (!remember_login) {
			removePrefs();
			finish();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(Menu.NONE, MENU_UPDATE_LOCATION, Menu.NONE, "Check in").setIcon(android.R.drawable.ic_menu_mylocation);
		menu.add(Menu.NONE, MENU_LOGOUT, Menu.NONE, "Log out").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		switch (item.getItemId()) {
		
		case MENU_LOGOUT:
			removePrefs();
			finish();
			return true;

		case MENU_UPDATE_LOCATION:
			boolean flag = false;
			
			return flag;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	/** End of overriding activity's life cycle methods */
	
	/** Helper methods */
	private void removePrefs() {
		Editor mEditor = mSharedPreferences.edit();
		mEditor.remove("username");
		mEditor.remove("password");
		mEditor.remove("remember_login");
		mEditor.commit();
	}
	/** End of helper methods */
	
	/** Some onClick methods used by various buttons */
	public void onFriendsClick(View v) {
//		startActivity(new Intent(this, FriendsActivity.class));
		startActivity(new Intent(this, FriendsTabActivity.class));
	}

	public void onMapClick(View v) {
//		startActivity(new Intent(this, MapDetailActivity.class));
		Double latitude = currentLoc.getLatitude();
		Double longitude = currentLoc.getLongitude();
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+ latitude.toString() + "," + longitude.toString() + "?z=17"));
		startActivity(intent);
	}
	
	public void onNearBy(View v) {
//		startActivity(new Intent(this, NearByActivity.class));
		startActivity(new Intent(this, MyLocationActivity.class));
	}
	
	public void onAboutClick(View v) {
		startActivity(new Intent(this, AboutActivity.class));
	}
	
	public void onSearchClick(View v) {
		UIUtils.goSearch(this);
	}
	/** End of onClick methods */
	
	/** Start of implemented methods for LocationListener */
	@Override
	public void onLocationChanged(Location location) {
		// update location text with meaningful address
		currentLoc = location;
		new ReverseGeocoderTask(locText).execute(currentLoc);
		Toast.makeText(getApplicationContext(), "New location received", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}
	/** End of LocationListener's required methods */	
}
