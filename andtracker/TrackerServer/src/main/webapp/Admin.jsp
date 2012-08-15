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

<%
String requestUrl = request.getRequestURL().toString();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>Live Tracker Administration</title>
        <link rel="shortcut icon" href="<%=requestUrl.substring(0, requestUrl.lastIndexOf('/'))%>/favicon.ico"/>
        <link rel="icon" href="<%=requestUrl.substring(0, requestUrl.lastIndexOf('/'))%>/favicon.ico"/>
	</head>
	<body>
		<h1>Live Tracker Administration</h1>

		<form action="<%=requestUrl.substring(0, requestUrl.lastIndexOf('/'))%>/AdminServlet" method="post">
            <h2>Server configuration</h2>
            <table>
                <tr>
                    <td>Next Load check</td>
                    <td><%=LoadManager.getNextCheck()%></td>
                </tr>
                <tr>
                    <td>Load check interval</td>
                    <td>
                        <input type="text" 
                            name="<%=LoadManager.CHECK_INTERVAL_SECONDS%>"
                            value="<%=LoadManager.getCheckIntervalSeconds()%>"
                            />
                    </td>
                </tr>
                <tr>
                    <td>Tracking expiration</td>
                    <td>
                        <input type="text" 
                            name="<%=TrackingManager.TRACKING_EXPIRATION_SECONDS%>"
                            value="<%=TrackingManager.getTrackingExpirationSeconds()%>"
                            />
                    </td>
                </tr>
            </table>
            
            <h2>Mobile configuration</h2>
			<table>
				<tr>
					<td>Minimum time interval</td>
                    <td>
                        <input type="text" 
                            name="<%=ConfigurationConstants.MIN_TIME_INTERVAL%>"
                            value="<%=Configuration.getInstance().getMinTimeInterval()%>"
                            />
                    </td>
				</tr>
				<tr>
					<td>Message to users</td>
                    <td>
                        <textarea cols="80" rows="5" name="<%=ConfigurationConstants.MESSAGE_TO_USERS%>"><%= Configuration.getInstance().getMessageToUsers()%></textarea>
                    </td>
				</tr>
			</table>
			<input type="submit" value="Save changes" />		
		</form>
	</body>
</html>