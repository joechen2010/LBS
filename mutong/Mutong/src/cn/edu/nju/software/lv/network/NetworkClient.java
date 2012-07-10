package cn.edu.nju.software.lv.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class NetworkClient {

	public String callGet(List<NameValuePair> parList) {
		return callGet(parList, "http");
	}
	
	public String callGet(List<NameValuePair> parList, InputStream image) {
		return callGet(parList, image, "http");
	}

	public boolean callPost(List<NameValuePair> parList) {
		return callPost(parList, "http");
	}

	public boolean callPost(List<NameValuePair> parList, String parName,
			InputStream image) {
		return callPost(parList, parName, image, "http");
	}

	public String callGet(List<NameValuePair> parList, String scheme) {
		HttpClient client = new DefaultHttpClient();
		String result = null;
		try {
			HttpGet get = new HttpGet(createURI(parList, scheme));
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String callGet(List<NameValuePair> parList, InputStream image, String scheme) {
		HttpClient client = new DefaultHttpClient();
		String result = "failed";
		try {
			HttpGet get = new HttpGet(createURI(parList, scheme));
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			image = entity.getContent();
			result = "done";
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean callPost(List<NameValuePair> parList, String scheme) {
		HttpClient client = new DefaultHttpClient();
		boolean result = false;
		try {
			HttpPost post = new HttpPost(createURI(parList, scheme));
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String sResult = EntityUtils.toString(entity);
			result = Integer.parseInt(sResult) == 1 ? true : false;
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean callPost(List<NameValuePair> parList, String parName,
			InputStream image, String scheme) {
		HttpClient client = new DefaultHttpClient();
		boolean result = false;

		String uri = new String(scheme + "://"
				+ CommonNetWorkParamters.getHost() + ":"
				+ CommonNetWorkParamters.getPort() + "/"
				+ CommonNetWorkParamters.getPath());

		MultipartEntity postEntity = new MultipartEntity();
		for (NameValuePair pair : parList) {
			try {
				String name = pair.getName();
				StringBody value = new StringBody(pair.getValue());
				postEntity.addPart(name, value);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		postEntity.addPart(parName, new InputStreamBody(image, null));

		HttpPost post = new HttpPost(uri);
		post.setEntity(postEntity);

		try {
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String sResult = EntityUtils.toString(entity);
			result = Integer.parseInt(sResult) == 1 ? true : false;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private URI createURI(List<NameValuePair> parList, String scheme)
			throws URISyntaxException {
		return URIUtils.createURI(
				scheme,
				CommonNetWorkParamters.getHost(),
				CommonNetWorkParamters.getPort(),
				CommonNetWorkParamters.getPath(),
				URLEncodedUtils.format(parList,
						CommonNetWorkParamters.getEncoding()), null);
	}

}
