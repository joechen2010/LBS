package cn.edu.nju.software.gof.processor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.nju.software.gof.business.AccountUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class UserCheckExistedProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String userName = request.getParameter(ServletParam.RequestParam.UserName);

		AccountUtilities utilities = new AccountUtilities();

		boolean existed = utilities.isUserExisted(userName);
		ResponseUtilities.writeMessage(response, existed ? 1 : 0,
				ResponseUtilities.TEXT);
	}

}
