package cn.edu.nju.software.gof.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import cn.edu.nju.software.gof.beans.oauth.OAuthRequest;
import cn.edu.nju.software.gof.beans.oauth.OAuthRequestIdentity;
import cn.edu.nju.software.gof.entity.OAuthAccessKey;
import cn.edu.nju.software.gof.global.OAuthRequestTable;
import cn.edu.nju.software.gof.type.SynchronizationProvider;
import cn.edu.nju.software.manager.AuthAccessKeyManager;
import cn.edu.nju.software.util.SpringContextHolder;

@SuppressWarnings("serial")
public class Callback extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doGetAndPost(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		doGetAndPost(req, resp);
	}

	private void doGetAndPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String oauthVerifier = req.getParameter("oauth_verifier");
		String string_PersonID = req.getParameter("person_id");
		String string_ProviderType = req.getParameter("provider");
		//
		Long personID = Long.valueOf(string_PersonID);
		SynchronizationProvider providerType = SynchronizationProvider
				.valueOf(string_ProviderType);
		OAuthRequestIdentity oauthRequestIdentity = new OAuthRequestIdentity(personID, providerType);
		//
		Map<OAuthRequestIdentity, OAuthRequest> requests = OAuthRequestTable
				.getOAuthRequests();
		OAuthProvider provider = null;
		OAuthConsumer consumer = null;
		synchronized (requests) {
			OAuthRequest request = requests.get(oauthRequestIdentity);
			consumer = request.getConsumer();
			provider = request.getProvider();
			requests.remove(oauthRequestIdentity);
		}
		if (provider == null) {
			return;
		}
		//
		try {
			provider.setOAuth10a(true);
			provider.retrieveAccessToken(consumer, oauthVerifier);
			//
			OAuthAccessKey key = null;//new OAuthAccessKey(personID,					consumer.getToken(), consumer.getTokenSecret(),					providerType);
			AuthAccessKeyManager authAccessKeyManager = SpringContextHolder.getBean("authAccessKeyManager");
			authAccessKeyManager.saveOAuthAccessKeye(key);
		} catch (Exception exception) {
		}
	}
}
