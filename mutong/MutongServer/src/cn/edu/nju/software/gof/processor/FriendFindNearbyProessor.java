package cn.edu.nju.software.gof.processor;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

import cn.edu.nju.software.gof.beans.json.NearbyFriendInfo;
import cn.edu.nju.software.gof.business.FriendUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class FriendFindNearbyProessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String sessionID = request.getParameter(ServletParam.RequestParam.SessionID);
		double latitude = 0;
		double longitude = 0;
		try {
			latitude = Double.parseDouble(request.getParameter(ServletParam.RequestParam.Latitude));
			longitude = Double.parseDouble(request.getParameter(ServletParam.RequestParam.Longitude));
		} catch (Exception e) {
			latitude = longitude = -1;
		}
		FriendUtilities utilities = new FriendUtilities();
		List<NearbyFriendInfo> list = utilities.getNearbyFriends(sessionID,
				latitude, longitude);
		JSONObject json = new JSONObject();
		try {
			json.put(ServletParam.JsonParam.ListName, list);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		ResponseUtilities.writeMessage(response, json.toString(),
				ResponseUtilities.JSON);
	}

}
