package cn.edu.nju.software.gof.entity;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import cn.edu.nju.software.gof.type.SynchronizationProvider;

import com.google.appengine.api.datastore.Key;

public class OAuthAccessKey {

	private Long ID;

	private Long ownerID;

	private String accessKey;

	private String accessKeySecret;

	private SynchronizationProvider provider;

	public OAuthAccessKey() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OAuthAccessKey(Long ownerID, String accessKey,
			String accessKeySecret, SynchronizationProvider provider) {
		super();
		this.ownerID = ownerID;
		this.accessKey = accessKey;
		this.accessKeySecret = accessKeySecret;
		this.provider = provider;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public Person getOwner(EntityManager em) {
		if (ownerID != null) {
			return em.find(Person.class, ownerID);
		} else {
			return null;
		}
	}

	public Long getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(Long ownerID) {
		this.ownerID = ownerID;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public SynchronizationProvider getProvider() {
		return provider;
	}

	public void setProvider(SynchronizationProvider provider) {
		this.provider = provider;
	}

}
