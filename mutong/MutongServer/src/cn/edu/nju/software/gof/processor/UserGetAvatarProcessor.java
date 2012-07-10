package cn.edu.nju.software.gof.processor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.nju.software.gof.business.ProfileUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class UserGetAvatarProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String sessionID = request.getParameter(ServletParam.RequestParam.SessionID);

		ProfileUtilities utilities = new ProfileUtilities();
		byte[] image = utilities.getUserAvatar(sessionID);

		if (image != null) {
			ResponseUtilities.writeImage(response, image);
		}
	}

}
