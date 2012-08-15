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
package de.avanux.livetracker.mobile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import de.avanux.livetracker.ConfigurationConstants;
import de.avanux.livetracker.LocationMessage;
import de.avanux.livetracker.Tracking;
import de.avanux.livetracker.TrackingManager;
import de.avanux.livetracker.admin.Configuration;

/**
 * Servlet implementation class LocationReceiver
 */
public class LocationReceiver extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private Log log = LogFactory.getLog(LocationReceiver.class);
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String trackingID = request.getParameter(LocationMessage.TRACKING_ID);
		String lat = request.getParameter(LocationMessage.LAT);
		String lon = request.getParameter(LocationMessage.LON);
        String time = request.getParameter(LocationMessage.TIME);
        String speed = request.getParameter(LocationMessage.SPEED);

        if((trackingID != null) && (lat != null) && (lon != null) && (time != null) && (speed != null)) {
            PrintWriter out = null;
            try {
                LocationMessage locationMessage = new LocationMessage(
                        Integer.parseInt(trackingID),
                        new DateTime(Long.parseLong(time)),
                        Float.parseFloat(lat),
                        Float.parseFloat(lon),
                        Float.parseFloat(speed)
                        );
                log.debug(locationMessage.getTrackingID() + " Location message received: " + locationMessage);
                
                Tracking tracking = TrackingManager.getTracking(locationMessage.getTrackingID());
                tracking.setLocationMessage(locationMessage);
                
                out = response.getWriter();
                Properties configuration = buildResponse(tracking);
                configuration.store(out, null);
            }
            catch(Exception e) {
                log.error("Error receiving location message:", e);
            }
            finally {
                if(out != null) {
                    out.close();
                }
            }
        }
        else {
            log.error("Incomplete location message ignored.");
        }
	}

	
    private Properties buildResponse(Tracking tracking) {
        Properties configuration = new Properties();
        configuration.put(ConfigurationConstants.MIN_TIME_INTERVAL, "" + Configuration.getInstance().getMinTimeInterval());
        configuration.put(ConfigurationConstants.TRACKER_COUNT, "" + tracking.getTrackerCount());
        return configuration;
    }
}
