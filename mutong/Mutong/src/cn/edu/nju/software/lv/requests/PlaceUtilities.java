package cn.edu.nju.software.lv.requests;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.nju.software.lv.beans.PlaceNearbyInformationBean;
import cn.edu.nju.software.lv.beans.PlaceCreationBean;
import cn.edu.nju.software.lv.beans.PlaceInformationBean;
import cn.edu.nju.software.lv.network.NetworkClient;

public class PlaceUtilities {

	public List<PlaceNearbyInformationBean> getNearbyPlace(String sessionID,
			double longitude, double latitude) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "get_nearby_place"));
		parList.add(new BasicNameValuePair("session_id", sessionID));
		parList.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
		parList.add(new BasicNameValuePair("longitude", String
				.valueOf(longitude)));

		List<PlaceNearbyInformationBean> rList = new ArrayList<PlaceNearbyInformationBean>();
		try {
			JSONObject json = new JSONObject(
					new NetworkClient().callGet(parList));
			JSONArray jsonArray = json.getJSONArray("replies");
			for (int i = 0; i < jsonArray.length(); i++) {
				PlaceNearbyInformationBean tempBean = new PlaceNearbyInformationBean();
				tempBean.parseJSON(jsonArray.getJSONObject(i));
				rList.add(tempBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return rList;
	}

	public PlaceInformationBean getPlaceInformation(String sessionID,
			String placeID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "get_place_info"));
		parList.add(new BasicNameValuePair("session_id", sessionID));
		parList.add(new BasicNameValuePair("place_id", placeID));
		
		PlaceInformationBean info = new PlaceInformationBean();
		try {
			JSONObject json = new JSONObject(new NetworkClient().callGet(parList));
			info.parseJSON(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return info;
	}

	public boolean createNewPlace(String sessionID, PlaceCreationBean newPlace) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "create_place"));
		parList.add(new BasicNameValuePair("session_id", sessionID));
		parList.add(new BasicNameValuePair("place_name", newPlace.getPlaceName()));
		
		String parentID = newPlace.getParentID();
		if(parentID != null) {
			parList.add(new BasicNameValuePair("parent_id", newPlace.getParentID()));
		} else {
			parList.add(new BasicNameValuePair("latitude", String.valueOf(newPlace.getLatitude())));
			parList.add(new BasicNameValuePair("longitude", String.valueOf(newPlace.getLongitude())));
		}
		
		return new NetworkClient().callPost(parList);
	}

	public boolean commentPlace(String sessionID, String placeID, String content) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "comment_place"));
		parList.add(new BasicNameValuePair("session_id", sessionID));
		parList.add(new BasicNameValuePair("place_id", placeID));
		parList.add(new BasicNameValuePair("content", content));
		
		return new NetworkClient().callPost(parList);
	}
}
