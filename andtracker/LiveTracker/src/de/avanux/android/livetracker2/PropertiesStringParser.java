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
package de.avanux.android.livetracker2;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

import android.util.Log;

public abstract class PropertiesStringParser {

    private static final long serialVersionUID = 1L;

    private static final String TAG = "LiveTracker:PropertiesStringParser";
    
    private Properties properties;
    
    
    public PropertiesStringParser(String propertiesString) throws IOException {
        this.properties = parsePropertiesFromString(propertiesString);
    }
    
    
    protected Properties getProperties() {
        return properties;
    }

    protected Properties parsePropertiesFromString(String propertiesString) throws IOException {
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
                Log.e(TAG, e.getMessage());
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
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
