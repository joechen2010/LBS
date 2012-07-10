package cn.edu.nju.software.lv.requests;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.nju.software.lv.beans.FriendInformationBean;
import cn.edu.nju.software.lv.beans.FriendRequestBean;
import cn.edu.nju.software.lv.network.NetworkClient;

public class NotificationUtilities {

	public void checkOnLine(String sessionID) {

		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "be_online"));
		parList.add(new BasicNameValuePair("session_id", sessionID));

		new NetworkClient().callPost(parList);
	}
	
	public List<FriendInformationBean> refeshOnlineFriendList(String sessionID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "friend_state_listen"));
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
	
	public List<FriendRequestBean> checkOnlineFriendRequest(String sessionID) {
		//friend request listen
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "friend_request_listen"));
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
}
