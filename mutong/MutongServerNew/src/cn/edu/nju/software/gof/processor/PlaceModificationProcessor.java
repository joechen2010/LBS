package cn.edu.nju.software.gof.processor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.nju.software.gof.beans.PlaceModification;
import cn.edu.nju.software.gof.business.PlaceUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class PlaceModificationProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String sessionID = (String) request
				.getAttribute(ServletParam.RequestParam.SessionID);
		String placeID = (String) request
				.getAttribute(ServletParam.RequestParam.PlaceID);
		//
		String placeDescription = (String) request
				.getAttribute(ServletParam.RequestParam.PLACE_DESCRIPTION);
		String placeName = (String) request
				.getAttribute(ServletParam.RequestParam.PlaceName);
		byte[] image = (byte[]) request
				.getAttribute(ServletParam.RequestParam.Image);

		PlaceModification modification = new PlaceModification(placeName,
				placeDescription, image);

		PlaceUtilities utilities = new PlaceUtilities();

		boolean success = utilities.modifyPlace(sessionID, placeID,
				modification);

		ResponseUtilities.writeMessage(response, success ? 1 : 0,
				ResponseUtilities.TEXT);

	}

}
