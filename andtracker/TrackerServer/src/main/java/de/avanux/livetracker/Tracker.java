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

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class Tracker {
    
    private final static int ADDITIONAL_REFRESH_SECONDS = 5;
    
    private final static int EXPIRATION_SECONDS = 5;
    
    private String trackerID;
    
    private DateTime lastSeen;
    
//    private long refreshSeconds;
    
    
    public Tracker(String trackerID) {
        this.trackerID = trackerID;
        this.lastSeen = new DateTime();
    }


    public String getTrackerID() {
        return trackerID;
    }


    public DateTime getLastSeen() {
        return lastSeen;
    }


    public void setLastSeen(DateTime lastSeen) {
//        if(this.lastSeen != null) {
//            Duration duration = new Duration(this.lastSeen, lastSeen);
//            this.refreshSeconds = duration.getStandardSeconds();
//        }
        this.lastSeen = lastSeen;
    }
    
    public boolean isExpired() {
        Duration age = new Duration(this.lastSeen, new DateTime());
        if(age.getStandardSeconds() > (EXPIRATION_SECONDS + ADDITIONAL_REFRESH_SECONDS)) {
            return true;
        }
        else {
            return false;
        }
    }

}
