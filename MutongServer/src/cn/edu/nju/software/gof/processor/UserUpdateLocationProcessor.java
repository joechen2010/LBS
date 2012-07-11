package cn.edu.nju.software.gof.processor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.nju.software.gof.business.CheckInUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class UserUpdateLocationProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String sessionID = request.getParameter(ServletParam.RequestParam.SessionID);
		double latitude = 0;
		double longitude = 0;
		try {
			latitude = Double.parseDouble(request.getParameter(ServletParam.RequestParam.Latitude));
			longitude = Double.parseDouble(request.getParameter(ServletParam.RequestParam.Longitude));
		} catch (Exception e) {
			latitude = longitude = -1;
		}

		CheckInUtilities utilities = new CheckInUtilities();
		boolean succ = utilities.updateLocation(sessionID, latitude, longitude);

		ResponseUtilities.writeMessage(response, succ ? 1 : 0,
				ResponseUtilities.TEXT);
	}
}
