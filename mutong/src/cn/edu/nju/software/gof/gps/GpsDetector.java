package cn.edu.nju.software.gof.gps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import cn.edu.nju.software.gof.beans.User;
import cn.edu.nju.software.gof.requests.LocationUtilities;
import cn.edu.nju.software.gof.utils.AndroidUtils;


public class GpsDetector{
	
    private static final Long CHECK_INTERVAL             = 1000*10L;
	
	private LocationManager locationManager;
	private TelephonyManager tm;
	private static GpsDetector detector;
	private Context context;
	private String result;
	private Long checkTime = 300000L;
	private Long minDistance = 500L;
	
	protected GpsDetector() {
	      // Exists only to defeat instantiation.
   }
	
   public static GpsDetector getInstance(Context context) {
      if(detector == null) {
    	  detector = new GpsDetector(context);
      }
      return detector;
   }
	
	public GpsDetector(Context context) {
		this.context = context;
	}

	public void detect(){
		setupMobile();
		setupLocation();
	}

	public void setupMobile(){
		tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		//User.getInstance().setMobile(tm.getLine1Number());
		User.getInstance().setMobile(tm.getDeviceId());
		User.getInstance().setName(tm.getDeviceId());
		/*GsmCellLocation gcl = (GsmCellLocation) tm.getCellLocation();
		int cid = gcl.getCid();
		int lac = gcl.getLac();
		int mcc = Integer.valueOf(tm.getNetworkOperator().substring(0,
				3));
		int mnc = Integer.valueOf(tm.getNetworkOperator().substring(3,
				5));*/
	}
	
	public void setupLocation(){
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		String provider=LocationManager.GPS_PROVIDER;
	    Location l = locationManager.getLastKnownLocation(provider); 
	    if (l != null) {
	    	User.getInstance().setLat(l.getLatitude());
	    	User.getInstance().setLng(l.getLongitude());
        }
	    //监听位置变化，2秒一次，距离100米以上 
	    LocationListener locationListener = new MyLocationListner();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,checkTime, minDistance, locationListener);
	}
	
	
	private class MyLocationListner implements LocationListener{
		@Override
		public void onLocationChanged(Location l) { //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
	    	if (l != null) {
	    		double oldLat = User.getInstance().getLat();
	    		double oldLng = User.getInstance().getLng();
	    		User.getInstance().setLat(l.getLatitude());
		    	User.getInstance().setLng(l.getLongitude());
		    	if(AndroidUtils.GetDistance(oldLat, oldLng, User.getInstance().getLat(), User.getInstance().getLng()) > minDistance){
		    		LocationUtilities.saveUserLocation(User.getInstance());
		    	}
	        }
	    }

	    public void onProviderDisabled(String provider) {
	    // Provider被disable时触发此函数，比如GPS被关闭
	    }

	    public void onProviderEnabled(String provider) {
	    //  Provider被enable时触发此函数，比如GPS被打开
	    }

	    public void onStatusChanged(String provider, int status, Bundle extras) {
	    // Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
	    }
	};
	
	
	protected boolean isBetterLocation(Location location,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}
 
		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > CHECK_INTERVAL;
		boolean isSignificantlyOlder = timeDelta < -CHECK_INTERVAL;
		boolean isNewer = timeDelta > 0;
 
		// If it's been more than two minutes since the current location,
		// use the new location
		// because the User.getInstance() has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must
			// be worse
		} else if (isSignificantlyOlder) {
			return false;
		}
 
		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;
 
		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());
 
		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
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
	
	public static void setDetector(GpsDetector detector) {
		GpsDetector.detector = detector;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}


	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public LocationManager getLocationManager() {
		return locationManager;
	}

	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	public TelephonyManager getTm() {
		return tm;
	}

	public void setTm(TelephonyManager tm) {
		this.tm = tm;
	}

	public Long getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(Long checkTime) {
		this.checkTime = checkTime;
	}

	public Long getMinDistance() {
		return minDistance;
	}

	public void setMinDistance(Long minDistance) {
		this.minDistance = minDistance;
	}
	 
	
}