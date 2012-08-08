package cn.edu.nju.software.gof.business.synchronization;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import oauth.signpost.basic.PostEnabledOAuthConsumer;
import cn.edu.nju.software.gof.beans.oauth.OAuthApp;
import cn.edu.nju.software.gof.business.BaseUtilities;
import cn.edu.nju.software.gof.entity.OAuthAccessKey;
import cn.edu.nju.software.gof.global.OAuthAppTable;

public class OAuthSynchronization extends BaseUtilities implements Synchronizationable {

	@SuppressWarnings("unchecked")
	@Override
	public void synchronize(Long personID, String placeName) {
		List<OAuthAccessKey> accessKeys = null;
		OAuthAccessKey o = new OAuthAccessKey();
		o.setOwnerId(personID);
		accessKeys = this.authAccessKeyManager.findOAuthAccessKey(o);
		//
//		accessKeys = new LinkedList<OAuthAccessKey>();
//		accessKeys.add(new OAuthAccessKey(personID, "1ac86b17228b25aff236217bca544834", "a559221c987516c531836c5708697510", SynchronizationProvider.SINA));
		//
		for (OAuthAccessKey key : accessKeys) {
			synchronizeTo(placeName, key);
		}

	}

	private void synchronizeTo(String placeName, OAuthAccessKey key) {
		OAuthApp app = OAuthAppTable.getOAuthApps(key.getProvider());
		PostEnabledOAuthConsumer consumer = new PostEnabledOAuthConsumer(
				app.getAppKey(), app.getAppKeySecret());
		consumer.setTokenWithSecret(key.getAccessKey(),
				key.getAccessKeySecret());

		try {
			URL url = new URL(app.getUpdateURL());
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			String body = app.getParamName()
					+ "="
					+ URLEncoder.encode("@ " + placeName + "  --- By Mutong.",
							"UTF-8");

			consumer.sign(connection, body);

			connection.setDoOutput(true);
			connection.getOutputStream().write(body.getBytes("UTF-8"));
			connection.connect();
			connection.getResponseCode();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
