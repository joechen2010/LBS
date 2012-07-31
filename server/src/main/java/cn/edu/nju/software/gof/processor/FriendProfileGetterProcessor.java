package cn.edu.nju.software.gof.processor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.nju.software.gof.beans.json.ProfileInfo;
import cn.edu.nju.software.gof.business.FriendUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class FriendProfileGetterProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String sessionID = request.getParameter(ServletParam.RequestParam.SessionID);
		String friendID = request.getParameter(ServletParam.RequestParam.FriendID);

		FriendUtilities utilities = new FriendUtilities();
		ProfileInfo info = utilities.getFriendProfile(sessionID, friendID);

		if (info != null) {
			ResponseUtilities.writeMessage(response, info.toJSONString(),
					ResponseUtilities.JSON);
		}
	}

}
