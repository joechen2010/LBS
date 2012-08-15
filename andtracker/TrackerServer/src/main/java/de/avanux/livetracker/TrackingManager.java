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

import de.avanux.livetracker.admin.TrackingManagerState;
import de.avanux.livetracker.statistics.PeriodicStatistics;
import de.avanux.livetracker.statistics.StatisticsManager;

public class TrackingManager implements Runnable {

    private static Log log = LogFactory.getLog(TrackingManager.class);

    public static final String TRACKING_EXPIRATION_SECONDS = "trackingExpirationSeconds";
    
    private static int trackingExpirationSeconds = 24 * 60 * 60;
    
    private static Map<Integer, Tracking> trackings = new HashMap<Integer, Tracking>();

    private static DateTime nextCheck;
    
    private static Thread runThread;
    
    private static boolean configurationChanged = false;
    

    public static void setTrackingExpirationSeconds(int trackingExpirationSeconds) {
        TrackingManager.trackingExpirationSeconds = trackingExpirationSeconds;
        log.debug("Set trackingExpirationSeconds=" + trackingExpirationSeconds);
        if(runThread != null) {
            configurationChanged = true;
            runThread.interrupt();
        }
    }

    public static long getTrackingExpirationSeconds() {
        return trackingExpirationSeconds;
    }

    public static DateTime getNextCheck() {
        return nextCheck;
    }
    
    public void setRunThread(Thread runThread) {
        TrackingManager.runThread = runThread;
    }
    

    public static synchronized Tracking createTracking() {
        int trackingID = getTrackingID();
        Tracking tracking = new Tracking(trackingID);
        trackings.put(trackingID, tracking);
        return tracking;
    }

    private static int getTrackingID() {
        int trackingID = 0;
        int testTrackingID = 1;
        while(trackingID == 0) {
            if(! trackings.keySet().contains(testTrackingID)) {
                trackingID = testTrackingID;
            }
            else {
                testTrackingID++;
            }
        }
        log.debug(trackingID + " created.");
        return trackingID;
    }

    public static synchronized void removeTracking(Tracking tracking) {
        trackings.remove(tracking.getTrackingID());
        log.debug(tracking.getTrackingID() + " tracking removed.");
    }

    public static Tracking getTracking(int trackingID) {
        return trackings.get(trackingID);
    }

    public static TrackingManagerState getState() {
        int registeredTrackings = 0;
        int activeTrackings = 0;
        int activeTrackers = 0;
        for(Tracking tracking : trackings.values()) {
            if(! tracking.isExpired(trackingExpirationSeconds)) {
                registeredTrackings++;
            }
            if(! tracking.isOverdue()) {
                activeTrackings++;
            }
            activeTrackers+=tracking.getTrackerCount();
        }
        return new TrackingManagerState(registeredTrackings, activeTrackings, activeTrackers);
    }

    @Override
    public void run() {
        while (true) {
            long intervalMillis = trackingExpirationSeconds * 1000; 
            configurationChanged = false;
            log.debug("Expired trackings will be removed every " + trackingExpirationSeconds + " seconds.");
            synchronized (this) {
                try {
                    nextCheck = new DateTime().plusSeconds(trackingExpirationSeconds);
                    log.debug("Waiting for " + intervalMillis + " millis - next expiration check at " + nextCheck);
                    wait(intervalMillis);
                    removedExpiredTrackings();
                } catch (Exception e) {
                    log.warn("Interrupted.");
                    if(! configurationChanged) {
                        break;
                    }
                }
            }
        }
    }
    
    private void removedExpiredTrackings() {
        PeriodicStatistics periodicStatistics = new PeriodicStatistics();
        log.debug("There are " + trackings.size() + " trackings before clean-up.");
        Collection<Integer> expiredTrackingIDs = new HashSet<Integer>();
        for (Tracking tracking : trackings.values()) {
            if(tracking.isExpired(trackingExpirationSeconds)) {
                periodicStatistics.addTrackingStatistics(tracking.getStatistics());
                expiredTrackingIDs.add(tracking.getTrackingID());
            }
        }
        trackings.keySet().removeAll(expiredTrackingIDs);
        log.debug("Removed " + expiredTrackingIDs.size() + " trackings.");
        StatisticsManager.addPeriodicStatistics(periodicStatistics);
    }
}
