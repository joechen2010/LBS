package cn.edu.nju.software.gof.global;

import java.util.HashMap;
import java.util.Map;

import cn.edu.nju.software.gof.beans.oauth.OAuthRequest;
import cn.edu.nju.software.gof.beans.oauth.OAuthRequestIdentity;

public class OAuthRequestTable {
	
	private static Map<OAuthRequestIdentity, OAuthRequest> requests = new HashMap<OAuthRequestIdentity, OAuthRequest>();
	
	private OAuthRequestTable() {
		
	}
	
	public static Map<OAuthRequestIdentity, OAuthRequest> getOAuthRequests() {
		return requests;
	}
}
