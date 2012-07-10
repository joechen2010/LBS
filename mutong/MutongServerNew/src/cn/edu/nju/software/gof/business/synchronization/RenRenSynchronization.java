package cn.edu.nju.software.gof.business.synchronization;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import cn.edu.nju.software.gof.entity.EMF;
import cn.edu.nju.software.gof.entity.RenRen;

import com.google.appengine.api.datastore.Key;

public class RenRenSynchronization implements Synchronizationable {

	@Override
	public void synchronize(Key personID, String placeName) {
		String userName = null;
		String password = null;
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			String sqlCmd = "SELECT R FROM RenRen AS R WHERE R.ownerID = :ownerID";
			Query query = em.createQuery(sqlCmd);
			query.setParameter("ownerID", personID);
			try {
				RenRen renren = (RenRen) query.getSingleResult();
				userName = renren.getUserName();
				password = renren.getPassword();
			} catch (Exception exception) {
				System.out.println(exception);
			}
		} finally {
			em.close();
		}
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
