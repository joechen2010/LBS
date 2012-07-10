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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.Duration;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import de.avanux.livetracker.LocationMessage;
import de.avanux.livetracker.Tracker;
import de.avanux.livetracker.Tracking;

public class TrackingStatistics {

    private static Log log = LogFactory.getLog(TrackingStatistics.class);
    
    private LocationMessage firstLocationMessage;
    
    private boolean firstLocationQualitySufficient = false;

    private LocationMessage lastLocationMessage;
    
    private int locationMessagesCount = 0;

    private int locationRequestsCount = 0;
    
    private int maxTrackerCount = 0;
    
    private float maxSpeed = 0;
    
    private float avgSpeed = 0;
    
    private String countryCode;
    
    
    public void updateLocationMessageStatistics(LocationMessage locationMessage) {
        this.locationMessagesCount++;

        if(this.firstLocationQualitySufficient) {
            if(locationMessage.getSpeed() > this.maxSpeed) {
                this.maxSpeed = locationMessage.getSpeed();
            }

            // first 2 location messages are not considered for average calculation ! 
            this.avgSpeed = (this.avgSpeed * (this.locationMessagesCount - 3) + locationMessage.getSpeed()) / (this.locationMessagesCount - 2); 
            
            if(this.countryCode == null) {
                setCountryCode(locationMessage.getLatitude(), locationMessage.getLongitude());
            }
        }
        else {
            if(this.firstLocationMessage != null) {
                // consider quality sufficient if distance between 2 location is less than 1 km
                double distance = calculateDistance(this.firstLocationMessage.getLatitude(), this.firstLocationMessage.getLongitude(), locationMessage.getLatitude(), locationMessage.getLongitude());
                if(distance < 1000) {
                    this.firstLocationQualitySufficient = true;
                }
                else {
                    // firstLocationMessage is crap - use the current location message as firstLocationMessage
                    this.locationMessagesCount--;
                    this.firstLocationMessage = locationMessage;
                }
            }
        }
        
        if(this.firstLocationMessage == null) {
            this.firstLocationMessage = locationMessage;
        }
        this.lastLocationMessage = locationMessage;
    }

    public int getLocationMessagesCount() {
        return locationMessagesCount;
    }

    public void updateLocationRequestStatistics(Tracking tracking, Tracker tracker) {
        this.locationRequestsCount++;
    }

    public int getLocationRequestCount() {
        return locationRequestsCount;
    }
    
    
    public int getMaxTrackerCount() {
        return maxTrackerCount;
    }

    public void updateTrackerStatistics(Map<String,Tracker> trackers) {
        if(trackers.size() > this.maxTrackerCount) {
            this.maxTrackerCount = trackers.size();
        }
    }
    
    public float getMaxSpeed() {
        return maxSpeed;
    }

    public float getAvgSpeed() {
        return avgSpeed;
    }

    public Duration getDuration() {
        Duration duration = null;
        if((this.firstLocationMessage != null) && (this.lastLocationMessage != null)) {
            duration = new Duration(this.firstLocationMessage.getDate(), this.lastLocationMessage.getDate());
        }
        return duration;
    }
    
    public float getAvgLocationMessagePeriod() {
        Duration duration = getDuration();
        if(duration != null) {
            return new Float(duration.getStandardSeconds()).floatValue() / new Float(this.locationMessagesCount - 1).floatValue(); 
        }
        else {
            return 0;
        }
    }
    
    public long getDistance() {
        long distance = 0;
        if((this.firstLocationMessage != null) && (this.lastLocationMessage != null)) {
            double distanceAsDouble = calculateDistance(this.firstLocationMessage.getLatitude(), this.firstLocationMessage.getLongitude(), this.lastLocationMessage.getLatitude(), this.lastLocationMessage.getLongitude());
            distance = new Double(distanceAsDouble).longValue();
        }
        return distance;
    }
    
    public String getCountryCode() {
        return countryCode;
    }

    private void setCountryCode(float lat, float lon) {
        Document doc = null;
        HttpMethod method = null;            
        InputStream responseBodyStream = null;
        String responseBodyString = null;
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true); // never forget this!
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            
            String uri = "http://www.geoplugin.net/extras/location.gp?lat=" + lat + "&long=" + lon + "&format=xml";
            log.debug("Retrieving country from " + uri);
            
            HttpClient client = new HttpClient();
            method = new GetMethod(uri);
            client.executeMethod(method);
            byte[] responseBodyBytes = method.getResponseBody();
            
            responseBodyString = new String(responseBodyBytes);
            log.debug("Content retrieved: " + responseBodyString);
            
            // the content is declared as UTF-8 but it seems to be iso-8859-1
            responseBodyString = new String(responseBodyBytes, "iso-8859-1");
            
            responseBodyStream = new StringBufferInputStream(responseBodyString);
            doc = builder.parse(responseBodyStream);
            
            XPath xpath = XPathFactory.newInstance().newXPath();
            this.countryCode = ((Node) xpath.evaluate("/geoPlugin/geoplugin_countryCode/text()", doc, XPathConstants.NODE)).getNodeValue();
            log.debug("countryCode=" + this.countryCode);
        }
        catch(Exception e) {
            if(responseBodyString != null) {
                log.error("unparsed xml=" + responseBodyString);
            }
            if(doc != null) {
                log.error("parsed xml=" + getDocumentAsString(doc));
            }
            log.error("Error getting country code.", e);
        }
        finally {
            try {
                if (responseBodyStream != null) {
                    responseBodyStream.close();
                }
                if (method != null) {
                    method.releaseConnection();
                }
            } catch (IOException e) {
                log.error("Error releasing resources: ", e);
            }
        }
    }

    private String getDocumentAsString(Document doc) {
        String xmlString = null;
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer serializer = factory.newTransformer();
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            StringWriter writer = new StringWriter();
            DOMSource xmlSource = new DOMSource(doc);
            StreamResult outputTarget = new StreamResult(writer);
            serializer.transform(xmlSource, outputTarget);
        } catch (TransformerException e) {
            log.error("Error transforming XML:", e);
        }
        return xmlString;
    }
    
    /**
     * Distance calculation between 2 Geopoint by Haversine formula
     * 
     * Origin:
     * http://www.anddev.org/distance_calculation_between_2_geopoint_by_haversine_formula-t3062.html
     * 
     * @param lat_a
     * @param lon_a
     * @param lat_b
     * @param lon_b
     * @return distance in meters
     */
    private double calculateDistance(float lat_a, float lon_a, float lat_b, float lon_b) {
        float pk = (float) (180/3.14169);

        float a1 = lat_a / pk;
        float a2 = lon_a / pk;
        float b1 = lat_b / pk;
        float b2 = lon_b / pk;

        double t1 = Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2);
        double t2 = Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2);
        double t3 = Math.sin(a1)*Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);
       
        return 6366000*tt;
    }    
}
