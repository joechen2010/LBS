package cn.edu.nju.software.gof.business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.springframework.stereotype.Component;

import cn.edu.nju.software.gof.entity.Person;

import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

@Component
public class CommonUtilities {
	
	/**
	 * Get a human readable name of the given place which is decided by the
	 * latitude and longitude of the place.
	 * 
	 * @param latitude
	 * @param longitude
	 * @return The place name.
	 */
	public static String getPlaceNameByLL(double latitude, double longitude) {

		String requestURL = "http://maps.google.com/maps/api/geocode/json?sensor=false&language=zh-CN&latlng="
				+ Double.toString(latitude) + "," + Double.toString(longitude);
		String placeName = null;
		try {
			URL url = new URL(requestURL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "UTF-8"));
			StringBuilder json = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				json.append(line);
			}
			JSONObject root = new JSONObject(json.toString());
			JSONArray results = root.getJSONArray("results");
			JSONObject place = results.getJSONObject(0);
			placeName = place.getString("formatted_address");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		return placeName;
	}

	public static boolean beFriend(Person one, Person another) {
		List<Long> friends = one.getFriendIds();
		return friends.contains(another.getId());
	}
}
