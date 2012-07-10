package cn.edu.nju.software.gof.processor;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

import cn.edu.nju.software.gof.beans.FriendSearchCondition;
import cn.edu.nju.software.gof.beans.json.BriefFriendInfo;
import cn.edu.nju.software.gof.business.FriendUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class FriendSearchProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String sessionID = request.getParameter(ServletParam.RequestParam.SessionID);

		String userName = request.getParameter(ServletParam.RequestParam.UserName);
		String realName = request.getParameter(ServletParam.RequestParam.RealName);
		String place = request.getParameter(ServletParam.RequestParam.Place);
		String school = request.getParameter(ServletParam.RequestParam.School);
		String birthday = request.getParameter(ServletParam.RequestParam.Birthday);

		FriendSearchCondition condition = new FriendSearchCondition(userName,
				realName, place, school, birthday);
		FriendUtilities utilities = new FriendUtilities();
		List<BriefFriendInfo> list = utilities.searchFriends(sessionID,
				condition);
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
