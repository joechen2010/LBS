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
package de.avanux.livetracker.admin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import de.avanux.livetracker.Tracking;
import de.avanux.livetracker.TrackingManager;

public class LoadManager implements Runnable {

    private static Log log = LogFactory.getLog(LoadManager.class);

    public static final String CHECK_INTERVAL_SECONDS = "checkIntervalSeconds";
    
    private static int checkIntervalSeconds = 20;
    
    private static DateTime nextCheck;
    
    private static Thread runThread;
    
    private static boolean configurationChanged = false;

    private static TrackingManagerState state;

    
    public static int getCheckIntervalSeconds() {
        return checkIntervalSeconds;
    }

    public static void setCheckIntervalSeconds(int checkIntervalSeconds) {
        LoadManager.checkIntervalSeconds = checkIntervalSeconds;
        log.debug("Set checkIntervalSeconds=" + checkIntervalSeconds);
        if(runThread != null) {
            configurationChanged = true;
            runThread.interrupt();
        }
    }

    public static DateTime getNextCheck() {
        return nextCheck;
    }
    
    public void setRunThread(Thread runThread) {
        LoadManager.runThread = runThread;
    }

    @Override
    public void run() {
        while (true) {
            long intervalMillis = checkIntervalSeconds * 1000; 
            configurationChanged = false;
            log.debug("Load will be checked every " + checkIntervalSeconds + " seconds.");
            synchronized (this) {
                try {
                    nextCheck = new DateTime().plusSeconds(checkIntervalSeconds);
                    log.debug("Waiting for " + intervalMillis + " millis - next check at " + nextCheck);
                    wait(intervalMillis);
                    checkLoad();
                } catch (Exception e) {
                    log.warn("Interrupted.");
                    if(! configurationChanged) {
                        break;
                    }
                }
            }
        }
    }

    public void checkLoad() {
        log.debug("Checking load ...");
        LoadManager.state = TrackingManager.getState();
        log.info("Current usage: registered trackings=" + state.getRegisteredTrackings() + " / active trackings=" + state.getActiveTrackings() + " / active trackers=" + state.getActiveTrackers());
    }

    public static int getRegisteredTrackings() {
        return state.getRegisteredTrackings();
    }
    
    public static int getActiveTrackings() {
        return state.getActiveTrackings();
    }

    public static int getActiveTrackers() {
        return state.getActiveTrackers();
    }
}
