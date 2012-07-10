package cn.edu.nju.software.gof.requests;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import cn.edu.nju.software.gof.network.NetworkClient;

public class SynchronizationUtilities {

	public static String getOauthAddress(String sessionID, String provider) {
		return getOauthAddressA(sessionID, provider, null, null);
	}

	public static String getOauthAddressA(String sessionID, String provider,
			String userName, String password) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.SetSynchronization));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.Provider,
				provider));

		if (userName != null) {
			parList.add(new BasicNameValuePair(
					ServletParam.RequestParam.UserName, userName));
			parList.add(new BasicNameValuePair(
					ServletParam.RequestParam.Password, password));
		}

		return NetworkClient.getAsString(parList);
	}
}
