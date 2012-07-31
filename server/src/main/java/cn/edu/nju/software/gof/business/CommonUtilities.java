package cn.edu.nju.software.gof.business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import cn.edu.nju.software.gof.entity.Account;
import cn.edu.nju.software.gof.entity.Person;
import cn.edu.nju.software.manager.AccountManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

public class CommonUtilities {
	
    @Autowired
	private AccountManager accountManager;

	public static Person getPersonBySessionID(String sessionID, EntityManager em) {
		String sqlCmd = "SELECT A FROM Account AS A WHERE A.sessionID = :sessionID";
		Query query = em.createQuery(sqlCmd);
		query.setParameter("sessionID", sessionID);
		try {
			Account account = (Account) query.getSingleResult();
			return account.getOwner();
		} catch (NoResultException exception) {
			return null;
		}
	}

	public static Person getPersonByUserName(String userName, EntityManager em) {
		
		String sqlCmd = "SELECT A FROM Account AS A WHERE A.userName = :userName";
		Query query = em.createQuery(sqlCmd);
		query.setParameter("userName", userName);
		try {
			Account account = (Account) query.getSingleResult();
			return account.getOwner();
		} catch (NoResultException exception) {
			return null;
		}
	}

	/**
	 * Get a human readable name of the given place which is decided by the
	 * latitude and longitude of the place.
	 * 
	 * @param latitude
	 * @param longitude
	 * @return The place name.
	 */
	public static String getPlaceNameByLL(double latitude, double longitude) {

		String requestURL = "http://maps.google.com/maps/api/geocode/json?sensor=false&language=zh-CN&latlng="
				+ Double.toString(latitude) + "," + Double.toString(longitude);
		String placeName = null;
		try {
			URL url = new URL(requestURL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "UTF-8"));
			StringBuilder json = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				json.append(line);
			}
			JSONObject root = new JSONObject(json.toString());
			JSONArray results = root.getJSONArray("results");
			JSONObject place = results.getJSONObject(0);
			placeName = place.getString("formatted_address");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		return placeName;
	}

	public static boolean beFriend(Person one, Person another, EntityManager em) {
		List<Key> friends = one.getFriendIDs();
		return friends.contains(another.getID());
	}
}
