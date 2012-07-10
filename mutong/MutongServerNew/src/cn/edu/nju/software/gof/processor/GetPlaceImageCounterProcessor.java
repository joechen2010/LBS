package cn.edu.nju.software.gof.processor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.nju.software.gof.business.FriendUtilities;
import cn.edu.nju.software.gof.business.PlaceUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class GetPlaceImageCounterProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String sessionID = request
				.getParameter(ServletParam.RequestParam.SessionID);
		String placeID = request
				.getParameter(ServletParam.RequestParam.PlaceID);

		PlaceUtilities utilities = new PlaceUtilities();

		Long counter = utilities.getPlaceImageCounter(sessionID, placeID);

		if (counter != null) {
			ResponseUtilities.writeMessage(response, counter,
					ResponseUtilities.TEXT);
		}

	}

}
