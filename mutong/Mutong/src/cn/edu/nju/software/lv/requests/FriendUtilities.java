package cn.edu.nju.software.lv.requests;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.nju.software.lv.beans.BreifInformationBean;
import cn.edu.nju.software.lv.beans.FriendInformationBean;
import cn.edu.nju.software.lv.beans.FriendNearbyInformationBean;
import cn.edu.nju.software.lv.beans.FriendRequestBean;
import cn.edu.nju.software.lv.beans.FriendSearchConditionBean;
import cn.edu.nju.software.lv.beans.PersonInformationBean;
import cn.edu.nju.software.lv.network.NetworkClient;

public class FriendUtilities {

	public List<FriendNearbyInformationBean> findNearbyFriend(String sessionID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "show_nearby_friend"));
		parList.add(new BasicNameValuePair("session_id",sessionID));
		
		List<FriendNearbyInformationBean> rList = new ArrayList<FriendNearbyInformationBean>();
		try {
			JSONObject json = new JSONObject(new NetworkClient().callGet(parList));
			JSONArray jsonArray = json.getJSONArray("results");
			for(int i = 0;i < jsonArray.length();i++) {
				FriendNearbyInformationBean tempBean = new FriendNearbyInformationBean();
				tempBean.parseJSON(jsonArray.getJSONObject(i));
				rList.add(tempBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return rList;
	}
	
	public List<FriendInformationBean> getFriendList(String sessionID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "get_friend_list"));
		parList.add(new BasicNameValuePair("session_id",sessionID));
		
		List<FriendInformationBean> rList = new ArrayList<FriendInformationBean>();
		try {
			JSONObject json = new JSONObject(new NetworkClient().callGet(parList));
			JSONArray jsonArray = json.getJSONArray("results");
			for(int i = 0;i < jsonArray.length();i++) {
				FriendInformationBean tempBean = new FriendInformationBean();
				tempBean.parseJSON(jsonArray.getJSONObject(i));
				rList.add(tempBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return rList;
	}
	
	public PersonInformationBean getFriendInformtion(
			String sessionID, String friendID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "get_friend_profile"));
		parList.add(new BasicNameValuePair("session_id", sessionID));
		parList.add(new BasicNameValuePair("friend_id", friendID));
		
		PersonInformationBean info = new PersonInformationBean();
		try {
			JSONObject json = new JSONObject(new NetworkClient().callGet(parList));
			info.parseJSON(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return info;
	}
	
	public InputStream getFriendAvatar(String sessionID, String friendID) {

		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "get_friend_avatar"));
		parList.add(new BasicNameValuePair("session_id", sessionID));
		parList.add(new BasicNameValuePair("friend_id", friendID));
		
		InputStream image = null;
		
		String result = new NetworkClient().callGet(parList, image);
		if(result.equals("failed")) {
			return null;
		} else {
			return image;
		}
	}
	
	public boolean sendFriendRequest(String sessionID, String friendID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "request_friend"));
		parList.add(new BasicNameValuePair("session_id", sessionID));
		parList.add(new BasicNameValuePair("friend_id", friendID));

		return new NetworkClient().callPost(parList);
	}
	
	public List<FriendRequestBean> getFriendRequests(String sessionID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "get_friend_request"));
		parList.add(new BasicNameValuePair("session_id", sessionID));
		
		List<FriendRequestBean> rList = new ArrayList<FriendRequestBean>();
		try {
			JSONObject json = new JSONObject(new NetworkClient().callGet(parList));
			JSONArray jsonArray = json.getJSONArray("requests");
			for(int i = 0;i < jsonArray.length();i++) {
				FriendRequestBean tempBean = new FriendRequestBean();
				tempBean.parseJSON(jsonArray.getJSONObject(i));
				rList.add(tempBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return rList;
	}
	
	public boolean agreeFriendRequest(String sessionID, String requestID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "agree_friend_request"));
		parList.add(new BasicNameValuePair("session_id", sessionID));
		parList.add(new BasicNameValuePair("request_id", requestID));

		return new NetworkClient().callPost(parList);
	}
	
	public boolean rejectFriendRequest(String sessionID, String requestID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "reject_friend_request"));
		parList.add(new BasicNameValuePair("session_id", sessionID));
		parList.add(new BasicNameValuePair("request_id", requestID));

		return new NetworkClient().callPost(parList);
	}
	
	public boolean deleteFriend(String sessionID, String friendID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "delete_friend"));
		parList.add(new BasicNameValuePair("session_id", sessionID));
		parList.add(new BasicNameValuePair("friend_id", friendID));

		return new NetworkClient().callPost(parList);
	}
	
	public List<BreifInformationBean> searchFriend(String sessionID, FriendSearchConditionBean condition) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "search_friend"));
		parList.add(new BasicNameValuePair("session_id", sessionID));
		parList.add(new BasicNameValuePair("friend_user_name", condition.getUserName()));
		parList.add(new BasicNameValuePair("friend_real_name", condition.getRealName()));
		parList.add(new BasicNameValuePair("friend_place", condition.getPlace()));
		parList.add(new BasicNameValuePair("friend_school", condition.getSchool()));
		parList.add(new BasicNameValuePair("friend_birthday", condition.getBirthday()));
		
		List<BreifInformationBean> rList = new ArrayList<BreifInformationBean>();
		try {
			JSONObject json = new JSONObject(new NetworkClient().callGet(parList));
			JSONArray jsonArray = json.getJSONArray("results");
			for(int i = 0;i < jsonArray.length();i++) {
				BreifInformationBean tempBean = new BreifInformationBean();
				tempBean.parseJSON(jsonArray.getJSONObject(i));
				rList.add(tempBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return rList;
	}
	
	public List<BreifInformationBean> getRecommendFriends(String sessionID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "recommond_friend"));
		parList.add(new BasicNameValuePair("session_id", sessionID));
		
		List<BreifInformationBean> rList = new ArrayList<BreifInformationBean>();
		try {
			JSONObject json = new JSONObject(new NetworkClient().callGet(parList));
			JSONArray jsonArray = json.getJSONArray("results");
			for(int i = 0;i < jsonArray.length();i++) {
				BreifInformationBean tempBean = new BreifInformationBean();
				tempBean.parseJSON(jsonArray.getJSONObject(i));
				rList.add(tempBean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return rList;
	}
}
