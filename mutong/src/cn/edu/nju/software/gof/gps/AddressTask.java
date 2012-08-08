package cn.edu.nju.software.gof.gps;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import android.content.Context;
import android.net.Proxy;


public class AddressTask extends IAddressTask {

	public static final int DO_APN = 0;
	public static final int DO_WIFI = 1;
	public static final int DO_GPS = 2;
	private int postType = -1;
	
	public AddressTask(Context context, int postType) {
		super(context);
		this.postType = postType;
	}
	
	@Override
	public HttpResponse execute(JSONObject params) throws Exception {
		HttpClient httpClient = new DefaultHttpClient();

		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
				20 * 1000);
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 20 * 1000);

		HttpPost post = new HttpPost("http://74.125.71.147/loc/json");
		// ���ô���
		if (postType == DO_APN) {
			String proxyHost = Proxy.getDefaultHost();
			if(proxyHost != null) {
				HttpHost proxy = new HttpHost(proxyHost, 80);
				httpClient.getParams().setParameter(
						ConnRouteParams.DEFAULT_PROXY, proxy);
			}
		}
		
		StringEntity se = new StringEntity(params.toString());
		post.setEntity(se);
		HttpResponse response = httpClient.execute(post);
		return response;
	}

}
