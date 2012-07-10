package cn.edu.nju.software.gof.requests;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.nju.software.gof.beans.CheckInInformationBean;
import cn.edu.nju.software.gof.network.NetworkClient;

public class CheckInUtilities {

	public static boolean userUpdateLocation(String sessionID, double latitude,
			double longitude) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.UpdateLocation));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.Latitude,
				String.valueOf(latitude)));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.Longitude,
				String.valueOf(longitude)));

		return NetworkClient.postMessage(parList);
	}

	public static boolean checkInPlace(String sessionID, String placeID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.CheckIn));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.PlaceID,
				placeID));

		return NetworkClient.postMessage(parList);
	}

	public static List<CheckInInformationBean> getCheckInInformation(
			String sessionID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.GetPersonlCheckIn));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));

		List<CheckInInformationBean> rList = new ArrayList<CheckInInformationBean>();
		try {
			JSONObject json = new JSONObject(NetworkClient.getAsString(parList));
			JSONArray jsonArray = json
					.getJSONArray(ServletParam.JsonParam.ListName);
			for (int i = 0; i < jsonArray.length(); i++) {
				CheckInInformationBean tempBean = new CheckInInformationBean();
				tempBean.parseJSON(jsonArray.getJSONObject(i));
				rList.add(tempBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rList;
	}
	
	public static List<CheckInInformationBean> getMyTopCheckIns(
			String sessionID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.GET_MY_TOP_CHECKINS));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));

		List<CheckInInformationBean> rList = new ArrayList<CheckInInformationBean>();
		try {
			JSONObject json = new JSONObject(NetworkClient.getAsString(parList));
			JSONArray jsonArray = json
					.getJSONArray(ServletParam.JsonParam.ListName);
			for (int i = 0; i < jsonArray.length(); i++) {
				CheckInInformationBean tempBean = new CheckInInformationBean();
				tempBean.parseJSON(jsonArray.getJSONObject(i));
				rList.add(tempBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rList;
	}

	public static List<CheckInInformationBean> getFriendCheckInInformation(
			String sessionID, String friendID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.GetFriendCheckIn));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.FriendID,
				friendID));

		List<CheckInInformationBean> rList = new ArrayList<CheckInInformationBean>();
		try {
			JSONObject json = new JSONObject(NetworkClient.getAsString(parList));
			JSONArray jsonArray = json
					.getJSONArray(ServletParam.JsonParam.ListName);
			for (int i = 0; i < jsonArray.length(); i++) {
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
