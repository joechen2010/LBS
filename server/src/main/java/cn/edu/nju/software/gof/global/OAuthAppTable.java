package cn.edu.nju.software.gof.global;

import java.util.HashMap;
import java.util.Map;

import cn.edu.nju.software.gof.beans.oauth.OAuthApp;
import cn.edu.nju.software.gof.type.SynchronizationProvider;

public class OAuthAppTable {

	private OAuthAppTable() {

	}

	private static Map<SynchronizationProvider, OAuthApp> apps = new HashMap<SynchronizationProvider, OAuthApp>();

	static {
		apps.put(SynchronizationProvider.SINA, new OAuthApp("916182457",
				"1a1639d14a55db78a7cd8d5618bdaf79",
				"http://api.t.sina.com.cn/oauth/request_token",
				"http://api.t.sina.com.cn/oauth/authorize",
				"http://api.t.sina.com.cn/oauth/access_token",
				"http://api.t.sina.com.cn/statuses/update.json", "status"));
		apps.put(SynchronizationProvider.TWITTER,
				new OAuthApp("dQLm4jpymtqe2hSoH1R8uA",
						"RtCiq9Up9Om7SaZnDjmNjAMjXVn6jGTdn3ckw1EBw",
						"https://api.twitter.com/oauth/request_token",
						"https://api.twitter.com/oauth/authorize",
						"https://api.twitter.com/oauth/access_token",
						"http://api.twitter.com/1/statuses/update.json",
						"status"));
	}

	public static OAuthApp getOAuthApps(SynchronizationProvider type) {
		return apps.get(type);
	}
}
