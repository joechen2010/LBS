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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StatisticsManager {

    private static Log log = LogFactory.getLog(StatisticsManager.class);
    
    private static StatisticsManager instance = null;
    
    private List<PeriodicStatistics> periodicStatistics = new ArrayList<PeriodicStatistics>();
    
    
    private static StatisticsManager getInstance() {
        if(instance == null) {
            instance = new StatisticsManager();
        }
        return instance;
    }
    
    public static void addPeriodicStatistics(PeriodicStatistics periodicStatistics) {
        getInstance().periodicStatistics.add(periodicStatistics);
        log.debug("Aggregated statistics added.");
    }
    
    public static List<PeriodicStatistics> getPeriodicStatistics() {
        return getInstance().periodicStatistics;
    }
    
}
