package cn.edu.nju.software.gof.processor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.nju.software.gof.beans.json.ProfileInfo;
import cn.edu.nju.software.gof.business.ProfileUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class UserModifyProfileProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
		String sessionID = request.getParameter(ServletParam.RequestParam.SessionID);
		String realName = request.getParameter(ServletParam.RequestParam.RealName);
		String school = request.getParameter(ServletParam.RequestParam.School);
		String place = request.getParameter(ServletParam.RequestParam.Place);
		String birthday = request.getParameter(ServletParam.RequestParam.Birthday);
		
		ProfileUtilities utilities = new ProfileUtilities();
		ProfileInfo info = new ProfileInfo(realName, school, place, birthday, null);
		boolean succ = utilities.setUserProfile(sessionID, info);
		
		ResponseUtilities.writeMessage(response, succ ? 1 : 0,
				ResponseUtilities.TEXT);
	}

}
