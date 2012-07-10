package cn.edu.nju.software.gof.map;

import android.content.Context;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class DynamicMyLocationOverlay extends MyLocationOverlay{
	
	private MapController controller;

	public DynamicMyLocationOverlay(Context context, MapView mapView, MapController controller) {
		super(context, mapView);
		this.controller = controller;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		Double latitude = location.getLatitude() * 1E6;
		Double longitude = location.getLongitude() * 1E6;
		GeoPoint currentPoint = new GeoPoint(latitude.intValue(),
				longitude.intValue());
		controller.animateTo(currentPoint);
		controller.setCenter(currentPoint);
	}

}
