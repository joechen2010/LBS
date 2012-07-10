package gm.client.controls;

import gm.client.R;
import gm.server.persistence.User;
import gm.shared.mapable.Mapable;

import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class GoogleMapControl  {

	public static int TYPE_SAT = 1;
	public static int TYPE_NORMAL = 0;
	
	private MapView view = null;
	
	private Context c = null;
	private String api_key = null;
	
	private int type;
	private int zoomLevel;
	private long center_lat;
	private long center_lon;
	private int radius;
	private int timer;
	private Collection<Mapable> mapables;
	
	public GoogleMapControl(Context c, String api_key) {
		this.c = c;
		this.api_key = api_key;
		this.view = new MapView(c, api_key);
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public void setZoomLevel(int level) {
		this.zoomLevel = level;
	}
	
	public void setCenter(long lat, long lon) {
		this.center_lat = lat;
		this.center_lon = lon;
	}
	
	public void display(Collection<Mapable> mapables) {
		this.mapables = mapables;
	}
	
	public void setRadius(int r) {
		this.radius = r;
	}
	
	public void setTimer(int time) {
		this.timer = time;
	}
	
	public MapView getView() {
		buildView();
		return this.view;
	}
	
	/*public MapView getAutomaticView() {
		// TODO: GELOBAMOGA don't forget to start a thread
		return this.view;
	}*/
	
	private void buildView() {
		MapController mapController = view.getController();
		this.view.setSatellite((this.type==GoogleMapControl.TYPE_SAT));
		mapController.setZoom(this.zoomLevel);
		mapController.setCenter(new GeoPoint((int) (this.center_lat * 1000000), (int) (this.center_lon * 1000000)));
		
		List<Overlay> list = view.getOverlays();

		for (Mapable m : this.mapables) {
			list.add(new GelobaMogaOverlay(m));
		}
		
	}
	
	// TODO: GELOBAMOGA Subklasse evtl auslagern (package gm.client.controls.helper)
	class GelobaMogaOverlay extends com.google.android.maps.Overlay {
		
		private GeoPoint geopoint = null;
		private int marker = 0;
		
		public GelobaMogaOverlay(Mapable m) {
			this.geopoint = new GeoPoint((int) (m.getLatitude() * 1000000), (int) (m.getLongitude() * 1000000));
			
			// TODO: GELOBAMOGA bestimmen, wie genau ein Mapable gezeichent wird
			if (m instanceof User) {
				this.marker = R.drawable.green_marker;
			} else {
				this.marker = R.drawable.purple_marker;
			}
		}
		
		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {

			super.draw(canvas, mapView, shadow);
			Paint paint = new Paint();
			// Converts lat/lng-Point to OUR coordinates on the screen.
			Point myScreenCoords = new Point();
			mapView.getProjection().toPixels(this.geopoint, myScreenCoords);
			paint.setStrokeWidth(1);
			paint.setARGB(255, 255, 255, 255);
			paint.setStyle(Paint.Style.STROKE);
			
			Bitmap bmp = BitmapFactory.decodeResource(c.getResources(), marker);
			
			canvas.drawBitmap(bmp, myScreenCoords.x, myScreenCoords.y, paint);
			//canvas.drawText(, myScreenCoords.x, myScreenCoords.y, paint);
			return true;

		} } 
}
