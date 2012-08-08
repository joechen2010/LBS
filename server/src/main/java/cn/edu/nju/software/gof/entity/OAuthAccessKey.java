package cn.edu.nju.software.gof.entity;

import cn.edu.nju.software.gof.type.SynchronizationProvider;

public class OAuthAccessKey {

	private Long id;

	private Long ownerId;

	private String accessKey;

	private String accessKeySecret;
	
	private Person owner;

	private SynchronizationProvider provider;

	public OAuthAccessKey() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OAuthAccessKey(Long ownerId, String accessKey,
			String accessKeySecret, SynchronizationProvider provider) {
		super();
		this.ownerId = ownerId;
		this.accessKey = accessKey;
		this.accessKeySecret = accessKeySecret;
		this.provider = provider;
	}


	public Person getOwner() {
		return owner;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public void setOwner(Person owner) {
		this.owner = owner;
	}

	
}
