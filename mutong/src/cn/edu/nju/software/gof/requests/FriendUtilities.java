package cn.edu.nju.software.gof.requests;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.nju.software.gof.beans.BreifInformationBean;
import cn.edu.nju.software.gof.beans.FriendInformationBean;
import cn.edu.nju.software.gof.beans.FriendNearbyInformationBean;
import cn.edu.nju.software.gof.beans.FriendRequestBean;
import cn.edu.nju.software.gof.beans.FriendSearchConditionBean;
import cn.edu.nju.software.gof.beans.PersonInformationBean;
import cn.edu.nju.software.gof.network.NetworkClient;

public class FriendUtilities {

	public static List<FriendNearbyInformationBean> findNearbyFriend(
			String sessionID, double latitude, double longitude) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.ShowNearbyFriend));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.Latitude,
				Double.toString(latitude)));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.Longitude,
				Double.toString(longitude)));

		List<FriendNearbyInformationBean> rList = new ArrayList<FriendNearbyInformationBean>();
		try {
			JSONObject json = new JSONObject(NetworkClient.getAsString(parList));
			JSONArray jsonArray = json
					.getJSONArray(ServletParam.JsonParam.ListName);
			for (int i = 0; i < jsonArray.length(); i++) {
				FriendNearbyInformationBean tempBean = new FriendNearbyInformationBean();
				tempBean.parseJSON(jsonArray.getJSONObject(i));
				rList.add(tempBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rList;
	}

	public static List<FriendInformationBean> getFriendList(String sessionID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.GetFriendList));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));

		List<FriendInformationBean> rList = new ArrayList<FriendInformationBean>();
		try {
			JSONObject json = new JSONObject(NetworkClient.getAsString(parList));
			JSONArray jsonArray = json
					.getJSONArray(ServletParam.JsonParam.ListName);
			for (int i = 0; i < jsonArray.length(); i++) {
				FriendInformationBean tempBean = new FriendInformationBean();
				tempBean.parseJSON(jsonArray.getJSONObject(i));
				rList.add(tempBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rList;
	}

	public static PersonInformationBean getFriendInformtion(String sessionID,
			String friendID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.GetFriendProfile));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.FriendID,
				friendID));

		PersonInformationBean info = new PersonInformationBean();
		try {
			JSONObject json = new JSONObject(NetworkClient.getAsString(parList));
			info.parseJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	public static boolean getFriendAvatar(String sessionID, String friendID,
			File file) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.GetFriendAvatar));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.FriendID,
				friendID));

		return NetworkClient.getToFile(parList, file);
	}

	public static boolean sendFriendRequest(String sessionID, String friendID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.RequestFriend));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.FriendID,
				friendID));

		return NetworkClient.postMessage(parList);
	}

	public static List<FriendRequestBean> getFriendRequests(String sessionID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.GetFriendRequest));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));

		List<FriendRequestBean> rList = new ArrayList<FriendRequestBean>();
		try {
			JSONObject json = new JSONObject(NetworkClient.getAsString(parList));
			JSONArray jsonArray = json
					.getJSONArray(ServletParam.JsonParam.ListName);
			for (int i = 0; i < jsonArray.length(); i++) {
				FriendRequestBean tempBean = new FriendRequestBean();
				tempBean.parseJSON(jsonArray.getJSONObject(i));
				rList.add(tempBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rList;
	}

	public static boolean agreeFriendRequest(String sessionID, String requestID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.AgreeFriendRequest));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.RequestID,
				requestID));

		return NetworkClient.postMessage(parList);
	}

	public static boolean rejectFriendRequest(String sessionID, String requestID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.RejectFriendRequest));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.RequestID,
				requestID));

		return NetworkClient.postMessage(parList);
	}

	public static boolean deleteFriend(String sessionID, String friendID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.DeleteFriend));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.FriendID,
				friendID));

		return NetworkClient.postMessage(parList);
	}

	public static List<BreifInformationBean> searchFriend(String sessionID,
			FriendSearchConditionBean condition) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.SearchFriend));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.UserName,
				condition.getUserName()));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.RealName,
				condition.getRealName()));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.Place,
				condition.getPlace()));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.School,
				condition.getSchool()));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.Birthday,
				condition.getBirthday()));

		List<BreifInformationBean> rList = new ArrayList<BreifInformationBean>();
		try {
			JSONObject json = new JSONObject(NetworkClient.getAsString(parList));
			JSONArray jsonArray = json
					.getJSONArray(ServletParam.JsonParam.ListName);
			for (int i = 0; i < jsonArray.length(); i++) {
				BreifInformationBean tempBean = new BreifInformationBean();
				tempBean.parseJSON(jsonArray.getJSONObject(i));
				rList.add(tempBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rList;
	}

	public static List<BreifInformationBean> getRecommendFriends(
			String sessionID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.RecommandFriend));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));

		List<BreifInformationBean> rList = new ArrayList<BreifInformationBean>();
		try {
			JSONObject json = new JSONObject(NetworkClient.getAsString(parList));
			JSONArray jsonArray = json
					.getJSONArray(ServletParam.JsonParam.ListName);
			for (int i = 0; i < jsonArray.length(); i++) {
				BreifInformationBean tempBean = new BreifInformationBean();
				tempBean.parseJSON(jsonArray.getJSONObject(i));
				rList.add(tempBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rList;
	}

	public static Long getFriendAvatarCounter(String sessionID, String friendID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.GET_FRIEND_AVATAR_COUNTER));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.FriendID,
				friendID));
		String result = NetworkClient.getAsString(parList);

		if (result == null) {
			return null;
		} else {
			try {
				Long counter = Long.parseLong(result);
				return counter;
			} catch (Exception e) {
				return null;
			}
		}
	}
}
