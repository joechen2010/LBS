package cn.edu.nju.software.gof.processor;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

import cn.edu.nju.software.gof.beans.json.CheckInfo;
import cn.edu.nju.software.gof.business.CheckInUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class FriendGetOnesCheckInProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String sessionID = request.getParameter(ServletParam.RequestParam.SessionID);
		String friendID = request.getParameter(ServletParam.RequestParam.FriendID);
		CheckInUtilities checkInUtilities = new CheckInUtilities();
		List<CheckInfo> list = checkInUtilities.getFriendCheckIns(sessionID, Long.valueOf(friendID));
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
