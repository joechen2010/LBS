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
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;

public class Configuration extends PropertiesStringParser implements OnSharedPreferenceChangeListener {

	private static final long serialVersionUID = 1L;
	
	private static final int SERVER_API_VERSION = 1;
	
	private static final String TAG = "LiveTracker:Configuration";

    private static String serverBaseUrl;

    private static String transmissionModeKey;

	private Long timeInterval;

    private Long minTimeInterval;

	private static String timeIntervalPreferenceKey;

	private Float distance;
	
	private static String distancePreferenceKey;

	
	public Configuration(String propertiesString) throws IOException {
		super(propertiesString);
		this.timeInterval = getDefaultTimeInterval();
        this.minTimeInterval = Long.parseLong(getProperties().getProperty(ConfigurationConstants.MIN_TIME_INTERVAL));
	}

	public static String getServerBaseUrl() {
        // FIXME: this is a hack during main development phase to switch easily between development server and deployment server
	    if(serverBaseUrl == null) {
//	        final String developmentHostName = "miraculix.localnet";
//	        String developmentHostAddress = null;
//	        try {
//	            InetAddress addr = InetAddress.getByName(developmentHostName);
//	            if(addr != null) {
//	                Log.d(TAG, "Getting IP address for " + developmentHostName);
//	                developmentHostAddress = addr.getHostAddress();
//	                if(developmentHostAddress.equals("192.168.70.5")) {
//	                    serverBaseUrl = "http://miraculix.localnet:8080/LiveTrackerServer";
//	                }
//	            }
//	        } catch (UnknownHostException e) {
//	            // ignore
//	        }
//	        if(serverBaseUrl == null) {
	            serverBaseUrl = "http://livetracker.dyndns.org";
//	        }
	        Log.d(TAG, "serverBaseUrl=" + serverBaseUrl);
	    }
	    return serverBaseUrl;
	}
	
    public boolean isMatchingServerApiVersion() {
		if(Integer.parseInt(getProperties().getProperty(ConfigurationConstants.SERVER_API_VERSION)) == SERVER_API_VERSION) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String getID() {
		return getProperties().getProperty(ConfigurationConstants.ID);
	}
	
	/**
     * Returns the time interval to be used currently.
	 * @return
	 */
    public long getTimeInterval() {
        // this check has to be done here (and not in PreferencesActivity) since minTimeInterval may be changed by the server at any time,
        // e.g. the preference may be 1 second but due to server load 3 second intervals are required
        if (this.timeInterval < this.minTimeInterval) {
            return this.minTimeInterval;
        } else {
            return this.timeInterval;
        }
    }

	public void setTimeInterval(long timeInterval) {
		this.timeInterval = timeInterval;
	}
	
	public void setTimeInterval(SharedPreferences sharedPreferences) {
	    if(getTransmissionMode(sharedPreferences) == TransmissionMode.MANUAL) {
	        setTimeInterval(Long.parseLong(sharedPreferences.getString(timeIntervalPreferenceKey, "" + getDefaultTimeInterval())));
	    }
	}
	
	

    /**
     * Returns the time interval to be used if none is configured so far.
     * @return
     */
    public static long getDefaultTimeInterval() {
        return 0;
    }
	
    public void setMinTimeInterval(long minTimeInterval) {
        this.minTimeInterval = minTimeInterval;
    }
	
	/**
     * Returns the required distance between location messages to be used actually.
	 * @return
	 */
	public float getDistance() {
	    return distance;
	}
	
	public void setDistance(float distance) {
		this.distance = distance;
	}
	
	public void setDistance(SharedPreferences sharedPreferences) {
        setDistance(Float.parseFloat(sharedPreferences.getString(distancePreferenceKey, "" + getDefaultDistance())));
	}

    /**
     * Returns the required distance between location messages to be used if none is configured so far.
     * @return
     */
    public static long getDefaultDistance() {
        return 0;
    }
	
    public String getMessageToUsers() {
        return getProperties().getProperty(ConfigurationConstants.MESSAGE_TO_USERS);
    }

	public String getLocationReceiverUrl() {
        return getProperties().getProperty(ConfigurationConstants.LOCATION_RECEIVER_URL);
	}

	//
    // ~ Preferences ----------------------------------------------------------------------------------------------------
	//

    private TransmissionMode getTransmissionMode(SharedPreferences sharedPreferences) {
        return TransmissionMode.valueOf(sharedPreferences.getString(transmissionModeKey, TransmissionMode.REALTIME.toString()));
    }
	
	public static void setTransmissionModeKey(String transmissionModeKey) {
        Configuration.transmissionModeKey = transmissionModeKey;
	}
	
    public static void setTimeIntervalPreferenceKey(String timeIntervalPreferenceKey) {
        Configuration.timeIntervalPreferenceKey = timeIntervalPreferenceKey;
    }

    public static void setDistancePreferenceKey(String distancePreferenceKey) {
        Configuration.distancePreferenceKey = distancePreferenceKey;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(timeIntervalPreferenceKey)) {
            setTimeInterval(sharedPreferences);
        }
        else if (key.equals(distancePreferenceKey)) {
            setDistance(sharedPreferences);
        }
        else if(key.equals(transmissionModeKey)) {
            // preferences should always contain manual values in order to preserve them when switching between the transmission mode;
            // if transmission mode gets set to real-time only the configuration gets set to default time interval/default distance; preference values remain unchanged
            if(getTransmissionMode(sharedPreferences) == TransmissionMode.MANUAL) {
                setTimeInterval(sharedPreferences);
                setDistance(sharedPreferences);
            }
            else {
                setTimeInterval(getDefaultTimeInterval());
                setDistance(getDefaultDistance());
            }
        }
    }
}
