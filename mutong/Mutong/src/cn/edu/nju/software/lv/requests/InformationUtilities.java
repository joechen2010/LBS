package cn.edu.nju.software.lv.requests;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.nju.software.lv.beans.PersonInformationBean;
import cn.edu.nju.software.lv.network.NetworkClient;

public class InformationUtilities {

	public boolean changeOnLineState(String sessionID, int userState) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "change_state"));
		parList.add(new BasicNameValuePair("session_id", sessionID));
		parList.add(new BasicNameValuePair("state", String.valueOf(userState)));

		return new NetworkClient().callPost(parList);
	}

	public PersonInformationBean getUserInformation(String sessionID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "get_personal_profile"));
		parList.add(new BasicNameValuePair("session_id", sessionID));
		
		PersonInformationBean info = new PersonInformationBean();
		try {
			JSONObject json = new JSONObject(new NetworkClient().callGet(parList));
			info.parseJSON(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return info;
	}

	public boolean setUserInformation(String sessionID,
			PersonInformationBean info) {
		// null means don't change
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "set_personal_profile"));
		parList.add(new BasicNameValuePair("session_id", sessionID));
		parList.add(new BasicNameValuePair("real_name", info.getRealName()));
		parList.add(new BasicNameValuePair("school", info.getSchool()));
		parList.add(new BasicNameValuePair("place", info.getPlace()));
		parList.add(new BasicNameValuePair("birthday", info.getBirthday()));

		return new NetworkClient().callPost(parList);
	}

	public InputStream getPersonalAvatar(String sessionID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "get_personal_avatar"));
		parList.add(new BasicNameValuePair("session_id", sessionID));
		
		InputStream image = null;
		String result = new NetworkClient().callGet(parList, image);
		if(result.equals("failed")) {
			return null;
		} else {
			return image;
		}
	}

	public boolean uploadAvatar(String sessionID, InputStream image) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "upload_avatar"));
		parList.add(new BasicNameValuePair("session_id", sessionID));

		return new NetworkClient().callPost(parList, "photo", image);
	}

}
