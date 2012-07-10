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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import de.avanux.livetracker.statistics.TrackingStatistics;

public class Tracking {

    private final static int ADDITIONAL_OVERDUE_SECONDS = 5;
    
    private static Log log = LogFactory.getLog(Tracking.class);
    
    private int trackingID;

    private DateTime creationTime;
    
    private LocationMessage locationMessage;
    
    private long updateInterval;
    
    private Map<String,Tracker> trackers = new HashMap<String,Tracker>();
    
    private TrackingStatistics statistics;
    
    
    protected Tracking(int trackingID) {
        this.trackingID = trackingID;
        this.creationTime = new DateTime();
        this.statistics = new TrackingStatistics();
    }

    public int getTrackingID() {
        return trackingID;
    }

    public LocationMessage getLocationMessage() {
        return locationMessage;
    }
    
    public TrackingStatistics getStatistics() {
        return statistics;
    }

    public void setLocationMessage(LocationMessage locationMessage) {
        if(this.locationMessage != null) {
            Duration duration = new Duration(this.locationMessage.getDate(), locationMessage.getDate());
            this.updateInterval = duration.getStandardSeconds();
            log.debug(this.trackingID + " updateInterval=" + this.updateInterval);
        }
        this.statistics.updateLocationMessageStatistics(locationMessage);
        this.locationMessage = locationMessage;
    }

    public long getUpdateInterval() {
        return updateInterval;
    }

    public void addTracker(Tracker tracker) {
        Tracker returningTracker = this.trackers.get(tracker.getTrackerID());
        if(returningTracker != null) {
            returningTracker.setLastSeen(tracker.getLastSeen());
        }
        else{
            this.trackers.put(tracker.getTrackerID(), tracker);
            this.statistics.updateTrackerStatistics(this.trackers);
        }
    }
    
    /**
     * Return the number of current trackers.
     * 
     * @return
     */
    public int getTrackerCount() {
        removeExpiredTrackers();
        return this.trackers.size();
    }
    
    private void removeExpiredTrackers() {
        Collection<String> expiredTrackerIDs = new HashSet<String>();
        for (Tracker tracker : this.trackers.values()) {
            if(tracker.isExpired()) {
                expiredTrackerIDs.add(tracker.getTrackerID());
            }
        }
        this.trackers.keySet().removeAll(expiredTrackerIDs);
    }

    public boolean isExpired(int expirationSeconds) {
        DateTime present = new DateTime();
        Duration inactivityPeriod = null;
        if(this.locationMessage != null) {
            inactivityPeriod = new Duration(this.locationMessage.getDate(), present);
        }
        else {
            inactivityPeriod = new Duration(this.creationTime, present);
        }
        if(inactivityPeriod.getStandardSeconds() > expirationSeconds) {
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * Returns true, if no location message has been received for the average location message period plus additional 5 seconds.
     * If no location message has been received yet we are overdue anyway.
     * @return
     */
    public boolean isOverdue() {
        if(this.locationMessage == null) {
            // we are overdue until we receive location messages
            return true;
        }
        else {
            Duration age = new Duration(this.locationMessage.getDate(), new DateTime());
            if(age.getStandardSeconds() > (this.statistics.getAvgLocationMessagePeriod() + ADDITIONAL_OVERDUE_SECONDS)) {
                return true;
            }
            else {
                return false;
            }
        }
    }
}
