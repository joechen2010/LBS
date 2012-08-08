package cn.edu.nju.software.gof.business.synchronization;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import cn.edu.nju.software.gof.business.BaseUtilities;
import cn.edu.nju.software.gof.entity.RenRen;

public class RenRenSynchronization extends BaseUtilities implements Synchronizationable{

	@Override
	public void synchronize(Long personID, String placeName) {
		String userName = null;
		String password = null;
		RenRen r = new RenRen();
		r.setOwnerId(personID);
		List<RenRen> renrens = authAccessKeyManager.findRenRen(r);
		RenRen renren = renrens.size() > 0 ? renrens.get(0) : null;
		userName = renren.getUserName();
		password = renren.getPassword();
		if (userName != null && password != null) {
			String message = "@ " + placeName + " --- By Mutong";
			doSynchronize(userName, password, message);
		}

	}

	private void doSynchronize(String userName, String password, String message) {
		String baseURL = "http://lidejia-renren.appspot.com/?";
		String query;
		try {
			query = "user_name=" + URLEncoder.encode(userName, "UTF-8")
					+ "&password=" + URLEncoder.encode(password, "UTF-8")
					+ "&message=" + URLEncoder.encode(message, "UTF-8");
			URL url = new URL(baseURL + query);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.connect();
			connection.getResponseCode();
		} catch (Exception exception) {
			System.out.println(exception);
		}

	}

}
