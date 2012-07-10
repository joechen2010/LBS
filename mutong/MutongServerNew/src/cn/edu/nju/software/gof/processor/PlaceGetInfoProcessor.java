package cn.edu.nju.software.gof.processor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.nju.software.gof.beans.json.PlaceInfo;
import cn.edu.nju.software.gof.business.PlaceUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class PlaceGetInfoProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String sessionID = request.getParameter(ServletParam.RequestParam.SessionID);
		String placeID = request.getParameter(ServletParam.RequestParam.PlaceID);

		PlaceUtilities utilities = new PlaceUtilities();
		PlaceInfo info = utilities.getPlaceInfo(sessionID, placeID);

		if (info != null) {
			ResponseUtilities.writeMessage(response, info.toJSONString(),
					ResponseUtilities.JSON);
		}
	}

}
