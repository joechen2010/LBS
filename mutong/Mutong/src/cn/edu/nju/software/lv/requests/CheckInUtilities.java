package cn.edu.nju.software.lv.requests;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.nju.software.lv.beans.CheckInInformationBean;
import cn.edu.nju.software.lv.network.NetworkClient;

public class CheckInUtilities {

	public boolean userUpdateLocation(String sessionID, double latitude,
			double longitude) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "update_location"));
		parList.add(new BasicNameValuePair("session_id", sessionID));
		parList.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
		parList.add(new BasicNameValuePair("longitude", String
				.valueOf(longitude)));

		return new NetworkClient().callPost(parList);
	}

	public boolean checkInPlace(String sessionID, String placeID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "check_in"));
		parList.add(new BasicNameValuePair("session_id", sessionID));
		parList.add(new BasicNameValuePair("place_id", placeID));

		return new NetworkClient().callPost(parList);
	}

	public List<CheckInInformationBean> getCheckInInformation(String sessionID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name",
				"get_personal_check_in"));
		parList.add(new BasicNameValuePair("session_id", sessionID));

		List<CheckInInformationBean> rList = new ArrayList<CheckInInformationBean>();
		try {
			JSONObject json = new JSONObject(new NetworkClient().callGet(parList));
			JSONArray jsonArray = json.getJSONArray("checkIns");
			for(int i = 0;i < jsonArray.length();i++) {
				CheckInInformationBean tempBean = new CheckInInformationBean();
				tempBean.parseJSON(jsonArray.getJSONObject(i));
				rList.add(tempBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return rList;
	}

	public List<CheckInInformationBean> getFriendCheckInInformation(
			String sessionID, String friendID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name",
				"get_friend_check_in"));
		parList.add(new BasicNameValuePair("session_id", sessionID));
		parList.add(new BasicNameValuePair("friend_id",friendID));

		List<CheckInInformationBean> rList = new ArrayList<CheckInInformationBean>();
		try {
			JSONObject json = new JSONObject(new NetworkClient().callGet(parList));
			JSONArray jsonArray = json.getJSONArray("checkIns");
			for(int i = 0;i < jsonArray.length();i++) {
				CheckInInformationBean tempBean = new CheckInInformationBean();
				tempBean.parseJSON(jsonArray.getJSONObject(i));
				rList.add(tempBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return rList;
	}
}
