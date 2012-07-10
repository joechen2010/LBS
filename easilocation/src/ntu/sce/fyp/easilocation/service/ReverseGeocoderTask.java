package ntu.sce.fyp.easilocation.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.TextView;


/** 
 * This class does the time consuming process that decodes the location's name 
 * of a given {@link Location} and updates the location {@link TextView} passed in 
 * 
 **/
public class ReverseGeocoderTask extends AsyncTask<Location, Void, List<Address>> {
	private static final int MAX_RESULTS = 10;
	private TextView locText;
	
	public ReverseGeocoderTask(TextView view) {
		this.locText = view;
	}
	
	@Override
	protected List<Address> doInBackground(Location... params) {
		Geocoder geo = new Geocoder(locText.getContext(), Locale.getDefault());
		List<Address> addresses = null;
		for (int i = 0; i < params.length; i++) {
			Location location = params[i];
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
				try {
					addresses = geo.getFromLocation(latitude, longitude, MAX_RESULTS);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (addresses == null) {
						addresses = new ArrayList<Address>();
					}
				}
		}
		return addresses;
	}
	
	@Override
	protected void onPostExecute(List<Address> addresses) {
		if (!addresses.isEmpty()) {
			Address curLocation = addresses.get(0);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < curLocation.getMaxAddressLineIndex(); i++) 
				sb.append(curLocation.getAddressLine(i));
			locText.setText(sb.toString());
		} else {
			locText.setText("Current location unavailable");
		}
	}
}
