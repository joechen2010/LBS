package cn.edu.nju.software.gof.processor;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

import cn.edu.nju.software.gof.business.BaseUtilities;
import cn.edu.nju.software.gof.entity.Person;
import cn.edu.nju.software.gof.entity.UserLocation;
import cn.edu.nju.software.gof.servlet.ServletParam;

public class UserLocationProcessor extends BaseUtilities implements RequestProcessor {

	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String action = request.getParameter(ServletParam.RequestParam.Action);
		String sessionID = request.getParameter(ServletParam.RequestParam.SessionID);
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if(ServletParam.RequestParam.ADD.equals(action)){
			String address = request.getParameter(ServletParam.RequestParam.ADDRESS);
			String city = request.getParameter(ServletParam.RequestParam.CITY);
			String street = request.getParameter(ServletParam.RequestParam.STREET);
			String latitude = request.getParameter(ServletParam.RequestParam.LATITUDE);
			String longitude = request.getParameter(ServletParam.RequestParam.LONGITUDE);
			String mobile = request.getParameter(ServletParam.RequestParam.MOBILE);
			UserLocation u = new UserLocation(person.getId(), address, city,street, Double.valueOf(latitude), Double.valueOf(longitude),mobile);
			userLocationManager.save(u);
			ResponseUtilities.writeMessage(response, 1,ResponseUtilities.TEXT);
		}else{
			List<UserLocation> locations = userLocationManager.findByOwnerId(person.getId());
			if (locations != null) {
				JSONObject json = new JSONObject();
				try {
					json.put(ServletParam.JsonParam.ListName, locations);
					ResponseUtilities.writeMessage(response, json.toString(),
							ResponseUtilities.JSON);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}


		
	}

}
