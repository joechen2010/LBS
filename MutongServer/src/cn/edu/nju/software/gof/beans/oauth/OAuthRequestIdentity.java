package cn.edu.nju.software.gof.beans.oauth;

import cn.edu.nju.software.gof.type.SynchronizationProvider;

import com.google.appengine.api.datastore.Key;

public class OAuthRequestIdentity {

	private Key personID;

	private SynchronizationProvider provider;

	public OAuthRequestIdentity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OAuthRequestIdentity(Key personID, SynchronizationProvider provider) {
		super();
		this.personID = personID;
		this.provider = provider;
	}

	public Key getPersonID() {
		return personID;
	}

	public void setPersonID(Key personID) {
		this.personID = personID;
	}

	public SynchronizationProvider getProvider() {
		return provider;
	}

	public void setProvider(SynchronizationProvider provider) {
		this.provider = provider;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((personID == null) ? 0 : personID.hashCode());
		result = prime * result
				+ ((provider == null) ? 0 : provider.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OAuthRequestIdentity other = (OAuthRequestIdentity) obj;
		if (personID == null) {
			if (other.personID != null)
				return false;
		} else if (!personID.equals(other.personID))
			return false;
		if (provider != other.provider)
			return false;
		return true;
	}

}
