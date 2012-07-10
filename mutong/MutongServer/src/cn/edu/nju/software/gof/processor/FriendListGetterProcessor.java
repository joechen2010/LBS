package cn.edu.nju.software.gof.processor;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

import cn.edu.nju.software.gof.beans.json.FriendInfo;
import cn.edu.nju.software.gof.business.FriendUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class FriendListGetterProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String sessionID = request.getParameter(ServletParam.RequestParam.SessionID);

		FriendUtilities utilities = new FriendUtilities();
		List<FriendInfo> list = utilities.getFriendsList(sessionID);
		JSONObject js = new JSONObject();
		try {
			js.put(ServletParam.JsonParam.ListName, list);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		ResponseUtilities.writeMessage(response, js.toString(),
				ResponseUtilities.JSON);
	}

}
