package cn.edu.nju.software.gof.beans.oauth;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;

public class OAuthRequest {

	private OAuthConsumer consumer;
	private OAuthProvider provider;

	public OAuthRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OAuthRequest(OAuthConsumer consumer, OAuthProvider provider) {
		super();
		this.consumer = consumer;
		this.provider = provider;
	}

	public OAuthConsumer getConsumer() {
		return consumer;
	}

	public void setConsumer(OAuthConsumer consumer) {
		this.consumer = consumer;
	}

	public OAuthProvider getProvider() {
		return provider;
	}

	public void setProvider(OAuthProvider provider) {
		this.provider = provider;
	}
}
