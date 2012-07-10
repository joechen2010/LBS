package ntu.sce.fyp.easilocation.map;

import android.content.Context;
import android.widget.Toast;

import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class CurrentLocationOverlay extends MyLocationOverlay {
	
	private Context mContext;
	
	public CurrentLocationOverlay(Context context, MapView mapView) {
		super(context, mapView);
		this.mContext = context;
	}

	@Override
	protected boolean dispatchTap() {
		// TODO handle a tap on "my location point", eg. display an option to send SMS, 
		// make a call, add a picture to current location point
		Toast.makeText(mContext, "My current location tapped", Toast.LENGTH_SHORT).show();
		return true;
	}

}
