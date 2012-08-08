package cn.edu.nju.software.gof.processor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.nju.software.gof.beans.json.PlaceGeneral;
import cn.edu.nju.software.gof.business.PlaceUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class GetPlaceGeneralProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String sessionID = request.getParameter(ServletParam.RequestParam.SessionID);
		String placeID = request.getParameter(ServletParam.RequestParam.PlaceID);

		PlaceUtilities utilities = new PlaceUtilities();
		PlaceGeneral place = utilities.getPlaceGeneralInfo(sessionID, Long.valueOf(placeID));
		if (place != null) {
			ResponseUtilities.writeMessage(response, place.toJSONString(),
					ResponseUtilities.JSON);
		}

	}

}
