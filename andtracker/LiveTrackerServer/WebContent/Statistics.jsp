<!--
 Copyright (C) 2009  Axel Müller <axel.mueller@avanux.de> 
 
 This file is part of LiveTracker.
 
 LiveTracker is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.
 
 LiveTracker is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with LiveTracker.  If not, see <http://www.gnu.org/licenses/>.
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="de.avanux.livetracker.*"%>
<%@page import="de.avanux.livetracker.admin.*"%>
<%@page import="de.avanux.livetracker.statistics.*"%>

<%
String requestUrl = request.getRequestURL().toString();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Live Tracker Statistics</title>
        <link rel="shortcut icon" href="<%=requestUrl.substring(0, requestUrl.lastIndexOf('/'))%>/favicon.ico"/>
        <link rel="icon" href="<%=requestUrl.substring(0, requestUrl.lastIndexOf('/'))%>/favicon.ico"/>
    </head>
    <body>
        <h1>Live Tracker Statistics</h1>
        <h2>Current load</h2>
        <table border="1">
            <tr>
                <td>Registered trackings</td>
                <td>Active trackings</td>
                <td>Active trackers</td>
            </tr>
            <tr>
                <td><%=LoadManager.getRegisteredTrackings()%></td>
                <td><%=LoadManager.getActiveTrackings()%></td>
                <td><%=LoadManager.getActiveTrackers()%></td>
            </tr>
        </table>
        
        <h2>Periodic statistics</h2>
        <table border="1">
            <tr>
                <th>Date</th>
                <th>Trackings</th>
                <th>Location<br/>messages</th>
                <th>Location<br/>requests</th>
                <th>Max<br/>tracker<br/>count</th>
                <th>Avg<br/>tracker<br/>count</th>
                <th>Avg<br/>location<br/>message<br/>period</th>
                <th>Max<br/>speed</th>
                <th>Avg<br/>speed</th>
                <th>Max<br/>duration</th>
                <th>Avg<br/>duration</th>
                <th>Max<br/>distance</th>
                <th>Avg<br/>distance</th>
                <th>Countries</th>
            </tr>
<%for(PeriodicStatistics statistics : StatisticsManager.getPeriodicStatistics()) {%>
            <tr>
                <td><%=statistics.getCreationTime()%></td>
                <td><%=statistics.getTrackingsCount()%></td>
                <td><%=statistics.getLocationMessagesCount()%></td>
                <td><%=statistics.getLocationRequestsCount()%></td>
                <td><%=statistics.getMaxTrackerCount()%></td>
                <td><%=statistics.getAvgTrackerCount()%></td>
                <td><%=statistics.getAvgLocationMessagePeriod()%></td>
                <td><%=statistics.getMaxSpeed()%></td>
                <td><%=statistics.getAvgSpeed()%></td>
                <td><%=statistics.getMaxDuration()%></td>
                <td><%=statistics.getAvgDuration()%></td>
                <td><%=statistics.getMaxDistance()%></td>
                <td><%=statistics.getAvgDistance()%></td>
                <td><%=statistics.getCountryCodes()%></td>
            </tr>
<%}%>
        </table>
   </body>
</html>