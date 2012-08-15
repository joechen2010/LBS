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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.avanux.livetracker.ConfigurationConstants;
import de.avanux.livetracker.TrackingManager;
import de.avanux.livetracker.admin.Configuration;

/**
 * Servlet implementation class ConfigurationProvider
 */
public class ConfigurationProvider extends HttpServlet {
    
    private Log log = LogFactory.getLog(LocationReceiver.class);
    
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Configuration request received.");
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        Properties configuration = buildConfiguration();
        configuration.store(out, null);
        out.close();
        log.debug("Configuration returned.");
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	
	private Properties buildConfiguration() {
	    Properties configuration = new Properties();
	    configuration.put(ConfigurationConstants.ID, "" + TrackingManager.createTracking().getTrackingID());
        configuration.put(ConfigurationConstants.SERVER_API_VERSION, "1");
        configuration.put(ConfigurationConstants.MIN_TIME_INTERVAL, "" + Configuration.getInstance().getMinTimeInterval());
        configuration.put(ConfigurationConstants.MESSAGE_TO_USERS, Configuration.getInstance().getMessageToUsers());

        // FIXME: this is a hack during main development phase to switch easily between development server and deployment server
        String hostname = null;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        
        if((hostname != null) && hostname.equals("miraculix")) {
            configuration.put(ConfigurationConstants.LOCATION_RECEIVER_URL, "http://miraculix.localnet:8080/LiveTrackerServer/LocationReceiver");
        }
        else {
            configuration.put(ConfigurationConstants.LOCATION_RECEIVER_URL, "http://livetracker.dyndns.org/LocationReceiver");
        }
	    return configuration;
	}
}
