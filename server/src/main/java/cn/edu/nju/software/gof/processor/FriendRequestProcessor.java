package cn.edu.nju.software.gof.processor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.nju.software.gof.business.FriendUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class FriendRequestProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String sessionID = request.getParameter(ServletParam.RequestParam.SessionID);
		String friendID = request.getParameter(ServletParam.RequestParam.FriendID);

		FriendUtilities utilities = new FriendUtilities();
		boolean succ = utilities.sendFriendRequest(sessionID, Long.valueOf(friendID));

		ResponseUtilities.writeMessage(response, succ ? 1 : 0,
				ResponseUtilities.TEXT);
	}

}
