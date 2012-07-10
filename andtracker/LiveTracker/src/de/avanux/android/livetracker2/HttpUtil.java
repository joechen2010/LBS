/* Copyright (C) 2009  Axel Müller <axel.mueller@avanux.de> 
 * 
 * This file is part of LiveTracker.
 * 
 * LiveTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * LiveTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with LiveTracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.avanux.android.livetracker2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

public class HttpUtil {

	// lat=50.2911 lon=8.9842

	private final static String TAG = "LiveTracker:HttpUtil";

	public static String get(String url) throws ClientProtocolException, IOException {
		Log.d(TAG, "HTTP GET " + url);
		HttpGet method = new HttpGet(url);
		HttpResponse response = executeMethod(method);
		return getResponseAsString(response);
	}

	public static String post(String url, Map<String, String> httpParameters) throws ClientProtocolException, IOException {
		Log.d(TAG, "HTTP POST " + url);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(httpParameters.size());
		Set<String> httpParameterKeys = httpParameters.keySet();
		for (String httpParameterKey : httpParameterKeys) {
			nameValuePairs.add(new BasicNameValuePair(httpParameterKey, httpParameters.get(httpParameterKey)));
		}

		HttpPost method = new HttpPost(url);
		method.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse response = executeMethod(method);
		return getResponseAsString(response);
	}

	private static HttpResponse executeMethod(HttpRequestBase method) throws ClientProtocolException, IOException {
		HttpResponse response = null;
		HttpClient client = new DefaultHttpClient();
		response = client.execute(method);
		Log.d(TAG, "executeMethod=" + response.getStatusLine());
		return response;
	}

	private static String getResponseAsString(HttpResponse response) throws IllegalStateException, IOException {
		String content = null;
		InputStream stream = null;
		try {
			if (response != null) {
				stream = response.getEntity().getContent();
				InputStreamReader reader = new InputStreamReader(stream);
				BufferedReader buffer = new BufferedReader(reader);
				StringBuilder sb = new StringBuilder();
				String cur;
				while ((cur = buffer.readLine()) != null) {
					sb.append(cur + "\n");
				}
				content = sb.toString();
			}
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
		return content;
	}

}
