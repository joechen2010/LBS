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
package de.avanux.livetracker.statistics;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

public class PeriodicStatistics {

    private static Log log = LogFactory.getLog(PeriodicStatistics.class);
    
    private DateTime creationTime = new DateTime();
    
    private int trackingsCount = 0;
    
    private int locationMessagesCount = 0;

    private int locationRequestsCount = 0;

    private int maxTrackerCount = 0;

    private int avgTrackerCount = 0;
    
    private float maxSpeed = 0;
    
    private float avgSpeed = 0;

    private long maxDuration = 0;
    
    private long avgDuration = 0;
    
    private float avgLocationMessagePeriod = 0;

    private long maxDistance = 0;
    
    private long avgDistance = 0;
    
    private Map<String,Integer> countryCodesWithCount = new HashMap<String,Integer>();
    
    
    public void addTrackingStatistics(TrackingStatistics trackingStatistics) {
        if(trackingStatistics.getLocationMessagesCount() > 0) {
            log.debug("Adding new tracking to periodic statistics:");
            this.trackingsCount++;
            log.debug("trackingsCount=" + trackingsCount);
            
            // location messages
            
            this.locationMessagesCount += trackingStatistics.getLocationMessagesCount();
            log.debug("locationMessagesCount=" + locationMessagesCount);
            
            // location requests
            
            this.locationRequestsCount += trackingStatistics.getLocationRequestCount();
            log.debug("locationRequestsCount=" + locationRequestsCount);
         
            // tracker count
            
            if(trackingStatistics.getMaxTrackerCount() > this.maxTrackerCount) {
                this.maxTrackerCount = trackingStatistics.getMaxTrackerCount(); 
                log.debug("maxTrackerCount=" + maxTrackerCount);
            }
            
            this.avgTrackerCount = (this.avgTrackerCount * (this.trackingsCount - 1) + trackingStatistics.getMaxTrackerCount()) / this.trackingsCount; 
            log.debug("avgTrackerCount=" + avgTrackerCount);
            
            // speed
            
            if(trackingStatistics.getMaxSpeed() > this.maxSpeed) {
                this.maxSpeed = trackingStatistics.getMaxSpeed();
                log.debug("maxSpeed=" + maxSpeed);
            }
            
            this.avgSpeed = (this.avgSpeed * (this.trackingsCount - 1) + trackingStatistics.getAvgSpeed()) / this.trackingsCount;
            log.debug("avgSpeed=" + avgSpeed);

            // duration
            
            if (trackingStatistics.getDuration().getStandardSeconds() > this.maxDuration) {
                this.maxDuration = trackingStatistics.getDuration().getStandardSeconds();
                log.debug("maxDuration=" + maxDuration);
            }

            this.avgDuration = (this.avgDuration * (this.trackingsCount - 1) + trackingStatistics.getDuration().getStandardSeconds()) / this.trackingsCount;
            log.debug("avgDuration=" + avgDuration);
            
            // location message period

            this.avgLocationMessagePeriod = (this.avgLocationMessagePeriod * (this.trackingsCount - 1) + trackingStatistics.getAvgLocationMessagePeriod()) / this.trackingsCount;
            log.debug("avgLocationMessagePeriod=" + avgLocationMessagePeriod);
            
            // distance
            
            if(trackingStatistics.getDistance() > this.maxDistance) {
                this.maxDistance = trackingStatistics.getDistance(); 
                log.debug("maxDistance=" + maxDistance);
            }
            
            this.avgDistance = (this.avgDistance * (this.trackingsCount - 1) + trackingStatistics.getDistance()) / this.trackingsCount;
            log.debug("avgDistance=" + avgDistance);

            // country codes
            
            log.debug("country=" + trackingStatistics.getCountryCode());
            if(trackingStatistics.getCountryCode() != null) { // we don't want to see "null" as country code in case there location message quality was not sufficient
                Integer countryCodeCount = this.countryCodesWithCount.get(trackingStatistics.getCountryCode());
                if(countryCodeCount != null) {
                    countryCodeCount++;
                }
                else {
                    countryCodeCount = 1;
                }
                this.countryCodesWithCount.put(trackingStatistics.getCountryCode(), countryCodeCount);
            }
        }
        else {
            log.warn("Ignoring empty tracking statistics.");
        }
    }

    public DateTime getCreationTime() {
        return creationTime;
    }

    public int getTrackingsCount() {
        return trackingsCount;
    }

    public int getLocationMessagesCount() {
        return locationMessagesCount;
    }


    public int getLocationRequestsCount() {
        return locationRequestsCount;
    }


    public int getMaxTrackerCount() {
        return maxTrackerCount;
    }


    public int getAvgTrackerCount() {
        return avgTrackerCount;
    }


    public float getAvgLocationMessagePeriod() {
        return avgLocationMessagePeriod;
    }

    
    public float getMaxSpeed() {
        return maxSpeed;
    }


    public float getAvgSpeed() {
        return avgSpeed;
    }


    public long getMaxDuration() {
        return maxDuration;
    }


    public long getAvgDuration() {
        return avgDuration;
    }


    public long getMaxDistance() {
        return maxDistance;
    }


    public long getAvgDistance() {
        return avgDistance;
    }


    public String getCountryCodes() {
        String text = "";
        for(String countryCode : this.countryCodesWithCount.keySet()) {
            text += countryCode + "(" + this.countryCodesWithCount.get(countryCode) + ")"  + ",";
        }
        return text;
    }
}
