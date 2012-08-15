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
package de.avanux.livetracker.ui;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.avanux.livetracker.LocationMessage;
import de.avanux.livetracker.Tracker;
import de.avanux.livetracker.Tracking;
import de.avanux.livetracker.TrackingManager;
import de.avanux.livetracker.admin.Configuration;

/**
 * Servlet implementation class TrackDisplay
 */
public class LocationMessageProvider extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
	private static Log log = LogFactory.getLog(LocationMessageProvider.class);

	public static final String HTTP_PARAM_TRACKING_ID = "trackingID";

    public static final String HTTP_PARAM_TRACKER_ID = "trackerID";

    public static final String HTTP_PARAM_MODE = "mode";

    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Location message requested.");
        String trackingID = request.getParameter(HTTP_PARAM_TRACKING_ID);
        String trackerID = request.getParameter(HTTP_PARAM_TRACKER_ID);
        if (trackingID != null) {
            if(trackerID != null) {
                PrintWriter out = null;
                try {
                    Tracking tracking = TrackingManager.getTracking(Integer.parseInt(trackingID));
                    if(tracking != null) {
                        Tracker tracker = new Tracker(trackerID);
                        tracking.addTracker(tracker);
                        tracking.getStatistics().updateLocationRequestStatistics(tracking, tracker);
                        out = response.getWriter();
                        response.setContentType("text/plain");
                        printLocation(tracking, out);
                    }
                    else {
                        log.debug(trackingID + " No tracking found.");
                    }
                }
                catch(Exception e) {
                    log.error("Error providing location message:", e);
                }
                finally {
                    if(out != null) {
                        out.close();
                    }
                }
            }
            else {
                log.error("Param " + HTTP_PARAM_TRACKER_ID + " not found.");
            }
        }
        else {
            log.error("Param " + HTTP_PARAM_TRACKING_ID + " not found.");
        }
    }

    private void printLocation(Tracking tracking, PrintWriter out) {
        log.debug(tracking.getTrackingID() + " Printing location message.");
        LocationMessage locationMessage = tracking.getLocationMessage();
        if(locationMessage != null) {
            SingleLocationMessageFormat format = new SingleLocationMessageFormat(locationMessage);
            format.setZoom(getZoom(locationMessage.getSpeed()));
            format.setSecondsUtilNextRefresh(Configuration.getInstance().getMinTimeInterval());
            out.print(format.toString());
            log.debug(tracking.getTrackingID() + " Location message printed: " + format.toString());
        }        
    }


    private static final int getZoom(double speed) {
        int zoom = 3;
        if(speed < 5.5) {       // <  20 km/h
            zoom = 17;
        }
        else if(speed < 11.1) { // <  40 km/h
            zoom = 16;
        }
        else if(speed < 13.8) { // <  50 km/h
            zoom = 15;
        }
        else if(speed < 22.2) { // <  80 km/h
            zoom = 14;
        }
        else if(speed < 33.3) { // < 120 km/h
            zoom = 13;
        }
        else {                  // > 120 km/h
            zoom = 12;
        }
        return zoom;
    }
    
}
