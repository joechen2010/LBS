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
package de.avanux.livetracker;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

public class LocationMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    
    public static final String TRACKING_ID = "id";

    public static final String LAT = "lat";

    public static final String LON = "lon";

    public static final String TIME = "time";
    
    public static final String SPEED = "speed";

    
    private int trackingID;

    private DateTime date;

    private float latitude;

    private float longitude;
    
    private float speed;
    

    public LocationMessage(int id, DateTime date, float latitude, float longitude, float speed) {
        this.trackingID = id;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
    }

    public int getTrackingID() {
        return trackingID;
    }
    public void setTrackingID(int trackingID) {
        this.trackingID = trackingID;
    }

    public DateTime getDate() {
        return date;
    }
    public void setDate(DateTime date) {
        this.date = date;
    }

    public float getLatitude() {
        return latitude;
    }
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getSpeed() {
        return speed;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
    public String toID() {
        return this.trackingID + "/" + this.date;
    }
}
