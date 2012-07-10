package cn.edu.nju.software.gof.requests;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.nju.software.gof.beans.PersonInformationBean;
import cn.edu.nju.software.gof.network.NetworkClient;

public class InformationUtilities {

	public static boolean changeOnLineState(String sessionID, int userState) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.ChangeState));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.State,
				String.valueOf(userState)));

		return NetworkClient.postMessage(parList);
	}

	public static PersonInformationBean getUserInformation(String sessionID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.GetPersonalProfile));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));

		PersonInformationBean info = new PersonInformationBean();
		try {
			String string = NetworkClient.getAsString(parList);
			JSONObject json = new JSONObject(string);
			info.parseJSON(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return info;
	}

	public static boolean setUserInformation(String sessionID,
			PersonInformationBean info) {
		// null means don't change
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.SetPersonalProfile));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.RealName,
				info.getRealName()));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.School,
				info.getSchool()));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.Place,
				info.getPlace()));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.Birthday,
				info.getBirthday()));

		boolean ok = NetworkClient.postMessage(parList);
		return ok;
	}

	public static boolean getPersonalAvatar(String sessionID, File file) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.GetPersonalAvatar));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));

		return NetworkClient.getToFile(parList, file);
	}

	public static boolean uploadAvatar(String sessionID, InputStream image,
			int contentLength) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.UploadAvatar));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));

		return NetworkClient.postImage(parList,
				ServletParam.RequestParam.Image, contentLength, image);
	}

}
