package cn.edu.nju.software.gof.processor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.nju.software.gof.beans.PlaceCreation;
import cn.edu.nju.software.gof.business.PlaceUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class PlaceCreationProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String sessionID = (String) request
				.getAttribute(ServletParam.RequestParam.SessionID);
		String placeName = (String) request
				.getAttribute(ServletParam.RequestParam.PlaceName);

		String parentID = (String) request
				.getAttribute(ServletParam.RequestParam.ParentID);
		String placeDescription = (String) request
				.getAttribute(ServletParam.RequestParam.PLACE_DESCRIPTION);

		PlaceCreation placeCreation = null;

		if (parentID == null) {
			Double latitude = Double.parseDouble((String) request
					.getAttribute(ServletParam.RequestParam.Latitude));
			Double longitude = Double.parseDouble((String) request
					.getAttribute(ServletParam.RequestParam.Longitude));
			placeCreation = new PlaceCreation(placeName, latitude, longitude);
		} else {
			placeCreation = new PlaceCreation(placeName, parentID);
		}
		//
		placeCreation.setPlaceDescription(placeDescription);
		placeCreation.setImages((byte[]) request
				.getAttribute(ServletParam.RequestParam.Image));
		PlaceUtilities utilities = new PlaceUtilities();
		boolean succ = utilities.createPlace(sessionID, placeCreation);

		ResponseUtilities.writeMessage(response, succ ? 1 : 0,
				ResponseUtilities.TEXT);
	}
}
