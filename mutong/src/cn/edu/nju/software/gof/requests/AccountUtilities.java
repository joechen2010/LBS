package cn.edu.nju.software.gof.requests;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.nju.software.gof.beans.PersonInformationBean;
import cn.edu.nju.software.gof.beans.RichManInfo;
import cn.edu.nju.software.gof.network.NetworkClient;

public class AccountUtilities {

	public static boolean checkExisted(String userName) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.CheckUser));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.UserName,
				userName));

		return NetworkClient.getAsBoolean(parList);
	}

	public static boolean register(String userName, String password,
			PersonInformationBean info) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.Register));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.UserName,
				userName));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.Password,
				password));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.RealName,
				info.getRealName()));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.School,
				info.getSchool()));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.Place,
				info.getPlace()));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.Birthday,
				info.getBirthday()));

		return NetworkClient.postMessage(parList);
	}

	public static String login(String userName, String password) {
		// https
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.Login));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.UserName,
				userName));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.Password,
				password));

		return NetworkClient.getAsString(parList);
	}

	public static boolean modifyPassword(String sessionID, String prePassword,
			String newPassword) {
		// https
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.ChangePassword));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(
				ServletParam.RequestParam.OldPassword, prePassword));
		parList.add(new BasicNameValuePair(
				ServletParam.RequestParam.NewPassword, newPassword));

		return NetworkClient.postMessage(parList);
	}

	public static RichManInfo getRichmaninfo(String sessionID) {
		// new implemented
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.GetRichmanInfo));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));

		RichManInfo info = new RichManInfo();
		try {
			JSONObject json = new JSONObject(NetworkClient.getAsString(parList));
			info.parseJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}
}
