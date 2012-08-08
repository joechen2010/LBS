package cn.edu.nju.software.gof.processor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.nju.software.gof.business.FriendUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class FriendRequestRejectProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String sessionID = request.getParameter(ServletParam.RequestParam.SessionID);
		String requestID = request.getParameter(ServletParam.RequestParam.RequestID);
		
		FriendUtilities utilities = new FriendUtilities();
		
		boolean succ = utilities.rejectFriendRequest(sessionID, Long.valueOf(requestID));
		
		ResponseUtilities.writeMessage(response, succ ? 1 : 0,
				ResponseUtilities.TEXT);
	}

}
