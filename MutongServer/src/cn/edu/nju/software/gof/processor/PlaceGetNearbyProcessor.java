package cn.edu.nju.software.gof.processor;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

import cn.edu.nju.software.gof.beans.json.NearbyPlaceInfo;
import cn.edu.nju.software.gof.business.PlaceUtilities;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class PlaceGetNearbyProcessor implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String sessionID = request.getParameter(ServletParam.RequestParam.SessionID);
		double longitude = 0;
		double latitude = 0;
		try {
			longitude = Double.parseDouble(request.getParameter(ServletParam.RequestParam.Longitude));
			latitude = Double.parseDouble(request.getParameter(ServletParam.RequestParam.Latitude));
		} catch (Exception e) {
			longitude = latitude = -1;
		}

		PlaceUtilities utilities = new PlaceUtilities();
		List<NearbyPlaceInfo> list = utilities.getNearbyPlaces(sessionID,
				latitude, longitude);
		JSONObject jso = new JSONObject();
		try {
			jso.put(ServletParam.JsonParam.ListName, list);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		ResponseUtilities.writeMessage(response, jso.toString(),
				ResponseUtilities.JSON);
	}

}
