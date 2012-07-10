/* Copyright (C) 2009  Axel Müller <axel.mueller@avanux.de> 
 * 
 * This file is part of LiveTracker.
 * 
 * LiveTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * LiveTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with LiveTracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.avanux.android.livetracker2;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class LocationTracker extends Service implements LocationListener {

	private final static String TAG = "LiveTracker:LocationTracker";
	
	private Configuration configuration;
	
    private final IBinder locationTrackerBinder = new LocationTrackerBinder();

    private Thread thread;
    
	private boolean running = false;
    
    private UpdatableDisplay updatableDisplay;

    private final Handler uiThreadCallback = new Handler();

    private LocationManager locationManager;
    
    private LocationHandler locationHandler;
    

	//~ Life cycle callbacks -------------------------------------------------------------------------------------------
    
	@Override
	public IBinder onBind(Intent intent) {
    	Log.d(TAG, "Bound.");
		return locationTrackerBinder;
	}
	
    public class LocationTrackerBinder extends Binder {
        LocationTracker getService() {
            return LocationTracker.this;
        }
    }

    
	//~ Service interaction --------------------------------------------------------------------------------------------
	
	public void start() {
    	Log.d(TAG, "Starting.");
        this.locationHandler = new LocationHandler();
    	setRunning(true);
        
        thread = new Thread(this.locationHandler);
        thread.setName("LocationHandler");
        thread.start();

        getLocationManager().requestLocationUpdates(getGpsProvider(), configuration.getTimeInterval() * 1000, configuration.getDistance(), this);
	}
	
	public void stop() {
    	Log.d(TAG, "Stopping.");
    	setRunning(false);
    	if(locationManager != null) {
        	locationManager.removeUpdates(this);
    	}
	}

	public synchronized boolean isRunning() {
		return running;
	}

	public synchronized void setRunning(boolean running) {
		this.running = running;
	}
	
	public Configuration getConfiguration() {
		return configuration;
	}
    
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	
	//~ Location handling ----------------------------------------------------------------------------------------------

	private LocationManager getLocationManager() {
		if(locationManager == null) {
	        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		}
		return locationManager;
	}
	
	public String getGpsProvider() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        // criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setSpeedRequired(true);
		
        return getLocationManager().getBestProvider(criteria, true);
	}
	
	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "Location changed: " + location.toString());
		this.locationHandler.setCurrentLocation(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	private class LocationHandler implements Runnable {

		private final static String TAG = "LiveTracker:LocationHandler";
		
		private Location currentLocation;
		
		private Long lastTimePosted;
		
		private int locationsSent = 0;

		
		public synchronized void setCurrentLocation(Location location) {
			this.currentLocation = location;
			Log.d(TAG, "setCurrentLocation=" + printShortLocation(location));
		}
		
		public int getLocationsSent() {
			return locationsSent;
		}
		
		public Long getLastTimePosted() {
			return lastTimePosted;
		}

		@Override
		public void run() {
		    try {
	            Log.d(TAG, "Start running ...");
	            long configuredTimeIntervalMillis = configuration.getTimeInterval() * 1000;

	            // until the first fix we wait only 500ms until we check again for a fix
	            long timeIntervalMillis = 500;
	            
	            while (isRunning()) {
	                if(this.currentLocation != null) {
	                    try {
	                        long beforePostMillis = System.currentTimeMillis();
	                        
	                        String postResponseString = postLocation(this.currentLocation);
	                        PostResponse response = new PostResponse(postResponseString);

	                        // don't use the timestamp of location since there is an offset between
	                        // times when locations are received and times when locations are posted
	                        // even though both use the same interval length
	                        // -> Users most likely would like to see the time when the location has been posted!
	                        lastTimePosted = System.currentTimeMillis();
	                        locationsSent++;
	                        
	                        // the server may change the min time interval at any
	                        // time - make sure we use it
	                        configuration.setMinTimeInterval(response.getMinTimeInterval());

	                        updateDisplay(locationsSent, lastTimePosted, response.getTrackerCount());
	                        
	                        // we don't wait for the configured time interval but subtract the time already spent for posting the location
	                        timeIntervalMillis = configuredTimeIntervalMillis - (System.currentTimeMillis() - beforePostMillis);
	                    } catch (Exception e) {
	                        Log.e(TAG, e.getMessage());
	                    }
	                }
                    
                    synchronized (this) {
                        try {
                            Log.d(TAG, "Waiting for " + timeIntervalMillis + " millis");
                            wait(timeIntervalMillis);
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
	            }
		    }
		    catch(Exception e) {
                Log.e(TAG, "Unexpected error in run(): " + e.getMessage());
		    }
		    finally {
	            Log.d(TAG, "... stop running");
		    }
		}
		
		private String postLocation(Location location) throws ClientProtocolException, IOException {
			Log.d(TAG, "postLocation=" + printShortLocation(location));
			Map<String,String> httpParameters = new HashMap<String,String>(); 
			httpParameters.put( "id", configuration.getID());
			httpParameters.put( "lat", "" + location.getLatitude());
			httpParameters.put( "lon", "" + location.getLongitude());
			httpParameters.put( "time", "" + location.getTime());
			httpParameters.put( "speed", "" + location.getSpeed());
			return HttpUtil.post(configuration.getLocationReceiverUrl(), httpParameters);
		}
		
	}

	
	//~ UI interaction -------------------------------------------------------------------------------------------------
	
	public void setUpdatableDisplay(UpdatableDisplay updatableDisplay) {
		this.updatableDisplay = updatableDisplay;
	}

	public Integer getLocationsSent() {
	    if(this.locationHandler != null) {
	        return this.locationHandler.getLocationsSent();
	    }
	    else {
	        return null;
	    }
	}
	
	public Long getLastTimePosted() {
        if(this.locationHandler != null) {
            return this.locationHandler.getLastTimePosted();
        }
        else {
            return null;
        }
	}
	
	private void updateDisplay(int locationsSentCount, long lastLocationSentTime, int trackerCount) {
		DisplayUpdater updater = new DisplayUpdater(this.updatableDisplay, locationsSentCount, lastLocationSentTime, trackerCount);
		this.uiThreadCallback.post(updater);
	}
	
	private class DisplayUpdater implements Runnable {
		
		private UpdatableDisplay updatableDisplay;
		
		private int locationsSentCount;
		
		private long lastLocationSentTime;
		
		private int trackerCount;


		public DisplayUpdater(UpdatableDisplay updatableDisplay, int locationsSentCount, long lastLocationSentTime, int trackerCount) {
			this.updatableDisplay = updatableDisplay;
			this.locationsSentCount = locationsSentCount;
			this.lastLocationSentTime = lastLocationSentTime;
			this.trackerCount = trackerCount;
		}
		
		@Override
		public void run() {
			this.updatableDisplay.updateLocationsSentCount(locationsSentCount);
			this.updatableDisplay.updateLastLocationSentTime(lastLocationSentTime);
			this.updatableDisplay.updateTrackerCount(trackerCount);
		}
		
	}

	//~ Helper ---------------------------------------------------------------------------------------------------------
	
	private String printShortLocation(Location location) {
		StringBuffer text = new StringBuffer();
		DecimalFormat doubleFormat = new DecimalFormat("##.##");
        Date date = new Date(location.getTime());
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		
		text.append(doubleFormat.format(location.getLongitude()));
		text.append("/");
		text.append(doubleFormat.format(location.getLatitude()));
		text.append("/");
		text.append(doubleFormat.format(location.getSpeed()));
		text.append("/");
		text.append(formatter.format(date));
		
		return text.toString();
	}
}
