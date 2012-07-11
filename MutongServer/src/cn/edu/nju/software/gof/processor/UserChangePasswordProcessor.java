package cn.edu.nju.software.gof.processor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.nju.software.gof.business.AccountUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class UserChangePasswordProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String sessionID = request.getParameter(ServletParam.RequestParam.SessionID);
		String oldPassword = request.getParameter(ServletParam.RequestParam.OldPassword);
		String newPassword = request.getParameter(ServletParam.RequestParam.NewPassword);

		AccountUtilities utilities = new AccountUtilities();
		boolean succ = utilities.changePassword(sessionID, oldPassword,
				newPassword);

		ResponseUtilities.writeMessage(response, succ ? 1 : 0,
				ResponseUtilities.TEXT);

	}

}
