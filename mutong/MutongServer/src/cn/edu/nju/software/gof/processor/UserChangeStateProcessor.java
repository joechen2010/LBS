package cn.edu.nju.software.gof.processor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.nju.software.gof.business.ProfileUtilities;
import cn.edu.nju.software.gof.processor.RequestProcessor;
import cn.edu.nju.software.gof.servlet.ServletParam;
import cn.edu.nju.software.gof.type.UserState;

public class UserChangeStateProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String sessionID = request.getParameter(ServletParam.RequestParam.SessionID);
		int state = -1;
		try {
			state = Integer.parseInt(request.getParameter(ServletParam.RequestParam.State));
		} catch (Exception e) {
		}
		UserState us = UserState.OFF_LINE;
		switch (state) {
		case 1:
			us = UserState.HIDE;
			break;
		case 2:
			us = UserState.ON_LINE;
			break;
		default:
		}

		ProfileUtilities utilities = new ProfileUtilities();
		boolean succ = utilities.changeOnlineState(sessionID, us);

		ResponseUtilities.writeMessage(response, succ ? 1 : 0,
				ResponseUtilities.TEXT);
	}

}
