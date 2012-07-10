package cn.edu.nju.software.lv.requests;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import cn.edu.nju.software.lv.beans.PersonInformationBean;
import cn.edu.nju.software.lv.network.NetworkClient;

public class AccountUtilities {

	public boolean checkExisted(String userName) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "check_user"));
		parList.add(new BasicNameValuePair("user_name", userName));
		
		return new NetworkClient().callPost(parList);
	}
	
	public boolean register(String userName,
			String password, PersonInformationBean info) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "register"));
		parList.add(new BasicNameValuePair("user_name",userName));
		parList.add(new BasicNameValuePair("password",password));
		parList.add(new BasicNameValuePair("real_name",info.getRealName()));
		parList.add(new BasicNameValuePair("school",info.getSchool()));
		parList.add(new BasicNameValuePair("place",info.getPlace()));
		parList.add(new BasicNameValuePair("birthday",info.getBirthday()));
		
		return new NetworkClient().callPost(parList);
	}
	
	public String login(String userName, String password, int userState) {
		//https
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "login"));
		parList.add(new BasicNameValuePair("userName",userName));
		parList.add(new BasicNameValuePair("password",password));
		parList.add(new BasicNameValuePair("state",String.valueOf(userState)));
		
		return new NetworkClient().callGet(parList);
	}
	
	public boolean modifyPassword(String sessionID,
			String prePassword, String newPassword) {
		//https

		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair("processor_name", "change_password"));
		parList.add(new BasicNameValuePair("session_id",sessionID));
		parList.add(new BasicNameValuePair("old_password",prePassword));
		parList.add(new BasicNameValuePair("new_password",newPassword));
		
		return new NetworkClient().callPost(parList);
	}
	
}
