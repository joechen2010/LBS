package cn.edu.nju.software.gof.processor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.nju.software.gof.beans.json.ProfileInfo;
import cn.edu.nju.software.gof.business.ProfileUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class UserGetProfileProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String sessionID = request.getParameter(ServletParam.RequestParam.SessionID);

		ProfileUtilities utilities = new ProfileUtilities();
		ProfileInfo info = utilities.getUserProfile(sessionID);

		if (info != null) {
			ResponseUtilities.writeMessage(response, info.toJSONString(),
					ResponseUtilities.JSON);
		}
	}

}
