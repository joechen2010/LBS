package cn.edu.nju.software.gof.beans.oauth;

public class OAuthApp {

	private String appKey;
	private String appKeySecret;
	//
	private String requestURL;
	private String accessURL;
	private String authorizeURL;
	//
	private String updateURL;
	private String paramName;

	public OAuthApp() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OAuthApp(String appKey, String appKeySecret, String requestURL,
			String authorizeURL, String accessURL, String updateURL, String paramName) {
		super();
		this.appKey = appKey;
		this.appKeySecret = appKeySecret;
		this.requestURL = requestURL;
		this.authorizeURL = authorizeURL;
		this.accessURL = accessURL;
		this.updateURL = updateURL;
		this.paramName = paramName;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppKeySecret() {
		return appKeySecret;
	}

	public void setAppKeySecret(String appKeySecret) {
		this.appKeySecret = appKeySecret;
	}

	public String getRequestURL() {
		return requestURL;
	}

	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

	public String getAuthorizeURL() {
		return authorizeURL;
	}

	public void setAuthorizeURL(String authorizeURL) {
		this.authorizeURL = authorizeURL;
	}

	public String getAccessURL() {
		return accessURL;
	}

	public void setAccessURL(String accessURL) {
		this.accessURL = accessURL;
	}

	public String getUpdateURL() {
		return updateURL;
	}

	public void setUpdateURL(String updateURL) {
		this.updateURL = updateURL;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
}
