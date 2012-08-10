package cn.edu.nju.software.gof.business;

import java.util.List;
import java.util.Map;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;

import org.springframework.stereotype.Component;

import cn.edu.nju.software.gof.beans.oauth.OAuthApp;
import cn.edu.nju.software.gof.beans.oauth.OAuthRequest;
import cn.edu.nju.software.gof.beans.oauth.OAuthRequestIdentity;
import cn.edu.nju.software.gof.business.synchronization.SynchronizationTable;
import cn.edu.nju.software.gof.business.synchronization.Synchronizationable;
import cn.edu.nju.software.gof.entity.Person;
import cn.edu.nju.software.gof.entity.RenRen;
import cn.edu.nju.software.gof.global.OAuthAppTable;
import cn.edu.nju.software.gof.global.OAuthRequestTable;
import cn.edu.nju.software.gof.type.SynchronizationProvider;

public class SynchronizationUtilities  extends BaseUtilities{

	public String getAuthorizeURL(String sessionID,
			SynchronizationProvider providerType) {
		// Get the person ID.
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		Long personID = null;
		if (person != null) {
			personID = person.getId();
		}
		if (personID == null) {
			return null;
		} else {
			OAuthApp app = OAuthAppTable.getOAuthApps(providerType);
			OAuthProvider provider = new DefaultOAuthProvider(
					app.getRequestURL(), app.getAccessURL(),
					app.getAuthorizeURL());
			OAuthConsumer consumer = new DefaultOAuthConsumer(app.getAppKey(),
					app.getAppKeySecret());
			// Build the call_back URL.
			StringBuilder callback = new StringBuilder();
			callback.append("http://do.jhost.cn/pathlife/callback?");
			callback.append("person_id=");
			callback.append(personID);
			callback.append("&");
			callback.append("provider=");
			callback.append(providerType.toString());
			//
			String authorizeURL = null;
			try {
				authorizeURL = provider.retrieveRequestToken(consumer,
						callback.toString());
				Map<OAuthRequestIdentity, OAuthRequest> requests = OAuthRequestTable
						.getOAuthRequests();
				synchronized (requests) {
					requests.put(new OAuthRequestIdentity(personID,	providerType), new OAuthRequest(consumer, provider));
				}
				//
			} catch (Exception exception) {
				authorizeURL = exception.toString();
			}
			//
			return authorizeURL;
		}
	}

	public void doSynchronization(Long personID, String placeName) {
		List<Synchronizationable> synchronizations = SynchronizationTable
				.getSynchronizationList();
		for (Synchronizationable synchronization : synchronizations) {
			synchronization.synchronize(personID, placeName);
		}
	}

	public boolean synchronizationToRenRen(String sessionID, String password,String userName) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return false;
		} else {
			Long personID = person.getId();
			RenRen renren = new RenRen(personID, userName, password);
			authAccessKeyManager.saveRenRen(renren);
			return true;
		}
	}
}
