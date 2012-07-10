package cn.edu.nju.software.gof.processor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.nju.software.gof.business.FriendUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class FriendAvatarGetterProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String sessionID = request
				.getParameter(ServletParam.RequestParam.SessionID);
		String friendID = request
				.getParameter(ServletParam.RequestParam.FriendID);

		FriendUtilities utilities = new FriendUtilities();
		byte[] image = utilities.getFriendAvatar(sessionID, friendID);

		if (image != null) {
			ResponseUtilities.writeImage(response, image);
		}
	}

}
