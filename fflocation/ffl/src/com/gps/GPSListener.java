package com.gps;

import com.web_services.ToServer;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * This class is used to know the position
 * @author Juan Javier García, Aitor Martin, Andreas Kapouranis
 * 
 */
public class GPSListener implements LocationListener
{
    
	LocationManager mLocationManager;
	Location mLocation;
	private static Location currentLocation = null;
	private Context context;
	
	public GPSListener(Context ctx)
	{
		this.context = ctx;
	}
	
    /**
     * Sets the current location
     * @param loc location
     */
    public void setCurrentLocation(Location loc)
    {
    	currentLocation = loc;
    }
    
    /**
     * Get the current location
     * @return current location
     */
    public Location getCurrentLocation()
    {
    	return currentLocation;
    	
    }

    /**
     * When the location changed we should update the current position.
     */
	@Override
	public void onLocationChanged(Location location) {
		if(location != null)
		{
			//Sets the current position
			setCurrentLocation(location);
			try {
				boolean succesfully = ToServer.logPosition((float)(location.getLongitude()), (float)(location.getLatitude()));				
				//If the position has been updated succesfully.
				if(succesfully)
				{
					 Toast.makeText(context, "The position has been updated succesfully.", Toast.LENGTH_LONG).show();
				}
				else
				{
					 Toast.makeText(context, "Failed to update the position.", Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				 Toast.makeText(context, "Failed to update the position: " + e.getMessage(), Toast.LENGTH_LONG).show();

			}
		}
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
	
		
	}

	/**
	 * Get the current latitude 1E6
	 * @return the current latitude
	 */
	public int getCurrentLatitude1E6()
	{
		return ((int) (currentLocation.getLatitude()*1E6));
		
	}
	/**
	 * Get the current longitude 1E6
	 * @return the current longitude
	 */	
	public int getCurrentLongitude1E6()
	{
		return ((int) (currentLocation.getLongitude()*1E6));
		
	}
	/**
	 * Get the current latitude
	 * @return the current latitude
	 */
	public int getCurrentLatitude()
	{
		return ((int) (currentLocation.getLatitude()));
		
	}
	/**
	 * Get the current longitude 1E6
	 * @return the current longitude
	 */	
	public int getCurrentLongitude()
	{
		return ((int) (currentLocation.getLongitude()));
		
	}

	/**
	 * Return if we updated in this session the position
	 * @return if the position has been updated in this session return true, otherwise return false.
	 */
	public boolean hasPosition()
	{
		if(currentLocation == null)
			return false;
		return true;
	}
	
	
}
