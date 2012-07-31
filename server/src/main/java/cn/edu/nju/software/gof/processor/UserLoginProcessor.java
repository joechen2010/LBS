package cn.edu.nju.software.gof.processor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.nju.software.gof.beans.LoginInfo;
import cn.edu.nju.software.gof.business.AccountUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;
import cn.edu.nju.software.gof.type.UserState;

public class UserLoginProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		
//debug url : http://localhost:9000/api?processor_name=login&user_name=joechen&password=123456
		String userName = request.getParameter(ServletParam.RequestParam.UserName);
		String password = request.getParameter(ServletParam.RequestParam.Password);
		int state = -1;
		try {
			state = Integer.parseInt(request.getParameter(ServletParam.RequestParam.State));
		} catch(Exception e) {
			
		}
		UserState us = UserState.OFF_LINE;
		switch (state) {
		case 1:
			us = UserState.HIDE;
			break;
		case 0:
			us = UserState.ON_LINE;
			break;
		default:
		}
		String ipAddress = request.getRemoteAddr();
		String ipPort = String.valueOf(request.getRemotePort());

		AccountUtilities utilities = new AccountUtilities();
		LoginInfo loginInfo = new LoginInfo(userName, password, us, ipAddress,
				ipPort);
		String sessionID = utilities.login(loginInfo);

		ResponseUtilities.writeMessage(response, sessionID == null ? 0 : sessionID,
				ResponseUtilities.TEXT);
	}

}
