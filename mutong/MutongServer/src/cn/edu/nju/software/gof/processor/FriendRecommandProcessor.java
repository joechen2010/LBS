package cn.edu.nju.software.gof.processor;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

import cn.edu.nju.software.gof.beans.json.BriefFriendInfo;
import cn.edu.nju.software.gof.business.FriendUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class FriendRecommandProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String sessionID = request.getParameter(ServletParam.RequestParam.SessionID);

		FriendUtilities utilities = new FriendUtilities();
		List<BriefFriendInfo> list = utilities.getRecommendFriends(sessionID);
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
