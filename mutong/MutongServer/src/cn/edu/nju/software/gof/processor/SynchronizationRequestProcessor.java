package cn.edu.nju.software.gof.processor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.nju.software.gof.business.SynchronizationUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;
import cn.edu.nju.software.gof.type.SynchronizationProvider;

public class SynchronizationRequestProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String sessionID = request.getParameter(ServletParam.RequestParam.SessionID);
		String provider = request.getParameter(ServletParam.RequestParam.Provider);
		SynchronizationProvider type = SynchronizationProvider
				.valueOf(provider);
		SynchronizationUtilities utilities = new SynchronizationUtilities();
		if (type == SynchronizationProvider.RENREN) {
			String userName = request.getParameter(ServletParam.RequestParam.UserName);
			String password = request.getParameter(ServletParam.RequestParam.Password);
			boolean success = false;
			if (password == null || userName == null) {
				success = false;
			} else {
				success = utilities.synchronizationToRenRen(sessionID,
						password, userName);
			}
			ResponseUtilities.writeMessage(response, success ? 1 : 0,
					ResponseUtilities.TEXT);
		} else {
			String url = utilities.getAuthorizeURL(sessionID, type);
			if (url == null) {
				ResponseUtilities.writeMessage(response, 0,
						ResponseUtilities.TEXT);
			} else {
				ResponseUtilities.writeMessage(response, url,
						ResponseUtilities.TEXT);
			}
		}
	}

}
