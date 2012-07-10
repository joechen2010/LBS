package cn.edu.nju.software.gof.business;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import cn.edu.nju.software.gof.beans.oauth.OAuthApp;
import cn.edu.nju.software.gof.beans.oauth.OAuthRequest;
import cn.edu.nju.software.gof.beans.oauth.OAuthRequestIdentity;
import cn.edu.nju.software.gof.business.synchronization.SynchronizationTable;
import cn.edu.nju.software.gof.business.synchronization.Synchronizationable;
import cn.edu.nju.software.gof.entity.EMF;
import cn.edu.nju.software.gof.entity.Person;
import cn.edu.nju.software.gof.entity.RenRen;
import cn.edu.nju.software.gof.global.OAuthAppTable;
import cn.edu.nju.software.gof.global.OAuthRequestTable;
import cn.edu.nju.software.gof.type.SynchronizationProvider;

public class SynchronizationUtilities {

	public String getAuthorizeURL(String sessionID,
			SynchronizationProvider providerType) {
		// Get the person ID.
		Key personID = null;
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person != null) {
				personID = person.getID();
			}
		} finally {
			em.close();
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
			callback.append("http://mutong-gof.appspot.com/callback?");
			callback.append("person_id=");
			callback.append(KeyFactory.keyToString(personID));
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
					requests.put(new OAuthRequestIdentity(personID,
							providerType), new OAuthRequest(consumer, provider));
				}
				//
			} catch (Exception exception) {
				authorizeURL = exception.toString();
			}
			//
			return authorizeURL;
		}
	}

	public void doSynchronization(Key personID, String placeName) {
		List<Synchronizationable> synchronizations = SynchronizationTable
				.getSynchronizationList();
		for (Synchronizationable synchronization : synchronizations) {
			synchronization.synchronize(personID, placeName);
		}
	}

	public boolean synchronizationToRenRen(String sessionID, String password,
			String userName) {
		EntityManager em = EMF.getInstance().createEntityManager();
		try {
			Person person = CommonUtilities.getPersonBySessionID(sessionID, em);
			if (person == null) {
				return false;
			} else {
				Key personID = person.getID();
				RenRen renren = new RenRen(personID, userName, password);
				em.persist(renren);
				return true;
			}
		} finally {
			em.close();
		}
	}
}
