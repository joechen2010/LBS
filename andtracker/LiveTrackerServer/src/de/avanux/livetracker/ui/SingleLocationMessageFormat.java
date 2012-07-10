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

import de.avanux.livetracker.LocationMessage;

public class SingleLocationMessageFormat extends LocationMessageFormat {

    private static final String MESSAGE_VALUE_SEPARATOR = ",";
    
    private LocationMessage locationMessage;
    
    public SingleLocationMessageFormat(LocationMessage locationMessage) {
        this.locationMessage = locationMessage;
    }

    @Override
    public String toString() {
        return this.locationMessage.getLongitude()
        + MESSAGE_VALUE_SEPARATOR
        + this.locationMessage.getLatitude()
        + MESSAGE_VALUE_SEPARATOR
        + getZoom()
        + MESSAGE_VALUE_SEPARATOR
        + getSecondsUtilNextRefresh()
        + MESSAGE_VALUE_SEPARATOR
        + this.locationMessage.getSpeed()
        + MESSAGE_VALUE_SEPARATOR
        + this.locationMessage.getDate().toDate().getTime()
        ;
    }
}
