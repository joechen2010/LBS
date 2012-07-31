package cn.edu.nju.software.gof.processor;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.nju.software.gof.business.ProfileUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class UserModifyAvatarProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String sessionID = (String) request.getAttribute(ServletParam.RequestParam.SessionID);
		byte[] avatar = (byte[]) request.getAttribute(ServletParam.RequestParam.Image);
		ProfileUtilities utilities = new ProfileUtilities();
		boolean success = utilities.setUserAvatar(sessionID, avatar);
		ResponseUtilities.writeMessage(response, success ? 1 : 0,
				ResponseUtilities.TEXT);
	}
}
