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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.avanux.livetracker.ConfigurationConstants;

public class Configuration {

    private static final long serialVersionUID = 1L;
    
    private static Log log = LogFactory.getLog(Configuration.class);

    private static Configuration instance;
    
    private Properties properties;
    
    private Long minTimeInterval = null;
    
    private String messageToUsers = "";
    
    
    private Configuration() {
    }
    
    public Configuration(String propertiesString) throws IOException {
        this.properties = parsePropertiesFromString(propertiesString);
    }

    public static Configuration getInstance() {
        if(instance == null) {
            instance = new Configuration();
        }
        return instance;
    }
    
    public String getID() {
        return this.properties.getProperty(ConfigurationConstants.ID);
    }

    public long getMinTimeInterval() {
        if(this.minTimeInterval != null) {
            return this.minTimeInterval.longValue();
        }
        else if(this.properties != null) {
            return Long.parseLong(this.properties.getProperty(ConfigurationConstants.MIN_TIME_INTERVAL));
        }
        else {
            return 1;
        }
    }
    public void setMinTimeInterval(long minTimeInterval) {
        this.minTimeInterval = minTimeInterval;
        log.debug("Set minTimeInterval = " + minTimeInterval);
    }

    public String getPositionReceiverUrl() {
        return this.properties.getProperty(ConfigurationConstants.LOCATION_RECEIVER_URL);
    }

    public String getTrackerCount() {
        return this.properties.getProperty(ConfigurationConstants.TRACKER_COUNT);
    }
    
    public String getMessageToUsers() {
        return messageToUsers;
    }
    public void setMessageToUsers(String messageToUsers) {
        this.messageToUsers = messageToUsers;
    }

    private Properties parsePropertiesFromString(String propertiesString) throws IOException {
        Properties properties = null;
        if (propertiesString != null) {
            if(propertiesString.charAt(0) == '<') {
                throw new IOException("Reveived HTML instead of properties!");
            }
            
            ByteArrayInputStream input = null;
            try {
                input = new ByteArrayInputStream(propertiesString.getBytes());
                properties = new Properties();
                properties.load(input);
            } catch (IOException e) {
                log.error(e);
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        log.error(e);
                    }
                }
            }
        }
        else {
            throw new IOException("Properties string must not be null!");
        }
        return properties;
    }

}
