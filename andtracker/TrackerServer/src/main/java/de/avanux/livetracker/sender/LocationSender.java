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
package de.avanux.livetracker.sender;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.avanux.livetracker.LocationMessage;
import de.avanux.livetracker.admin.Configuration;

public class LocationSender {

    private static Log log = LogFactory.getLog(LocationSender.class);
    
//    private final static String CONFIGURATION_URL = "http://miraculix.localnet:8080/LiveTrackerServer/ConfigurationProvider";
//    private final static String CONFIGURATION_URL = "http://livetracker.dyndns.org/ConfigurationProvider";
    
    public static void main(String[] args) {
        try {
            LocationSender sender = new LocationSender();
            Configuration configuration = sender.requestConfiguration(args[0]);
            float lon = 8.9842f;
            for(int i=0;i<10;i++) {
                sender.sendPositionData(configuration, 50.2911f, lon);
//                lon += 0.002;
                lon += 0.005;
                log.debug("Waiting for " + configuration.getMinTimeInterval() + " seconds.");
                Thread.sleep(configuration.getMinTimeInterval() * 1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Configuration requestConfiguration(String url) {
        log.debug("Requesting configuration ...");
        Configuration configuration = null;
        GetMethod method = new GetMethod(url);
        executeHttpMethod(method);
        try {
            byte[] responseBody = method.getResponseBody();
            if(responseBody.length > 0) {
                String response = new String(responseBody);
                log.debug("Response: " + response);
                configuration = new Configuration(response);
            }
            else {
                log.error("No configuration received.");
            }
        } catch (IOException e) {
            log.error(e);
        }
        log.debug("... configuration received.");
        return configuration;
    }

    private void sendPositionData(Configuration configuration, float lat, float lon) {
        String id = configuration.getID();
        log.debug("Using URL " + configuration.getPositionReceiverUrl());
        log.debug("Sending position data: id=" + id + " lat=" + lat + " lon=" + lon);
        PostMethod method = new PostMethod(configuration.getPositionReceiverUrl());
        NameValuePair[] data = {
                new NameValuePair(LocationMessage.TRACKING_ID, id),
                new NameValuePair(LocationMessage.LAT, "" + lat),
                new NameValuePair(LocationMessage.LON, "" + lon),
                new NameValuePair(LocationMessage.TIME, "" + System.currentTimeMillis()),
                new NameValuePair(LocationMessage.SPEED, "15") };
        method.setRequestBody(data);
        String response = executeHttpMethod(method);
        log.debug("Response: " + response);
        
        try {
            Configuration newConfiguration = new Configuration(response);
            log.debug("New min time interval: " + newConfiguration.getMinTimeInterval());
            configuration.setMinTimeInterval(newConfiguration.getMinTimeInterval());
            log.debug("Tracker count: " + newConfiguration.getTrackerCount());
        } catch (IOException e) {
            log.error("Error parsing configuration", e);
        } 
        
    }
    
    private String executeHttpMethod(HttpMethodBase method) {
        String response = null;
        HttpClient client = new HttpClient();
        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);

            if (statusCode != HttpStatus.SC_OK) {
                System.err.println("Method failed: " + method.getStatusLine());
            }

            // Read the response body.
            byte[] responseBody = method.getResponseBody();
            response = new String(responseBody);
            
        } catch (HttpException e) {
            System.err.println("Fatal protocol violation: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Fatal transport error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Release the connection.
            method.releaseConnection();
        }
        return response;
    }
}
