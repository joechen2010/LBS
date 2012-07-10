package cn.edu.nju.software.gof.network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.net.http.AndroidHttpClient;

public class NetworkClient {

	private final static String host = "mutong-gof.appspot.com";
	private final static int port = 80;
	private final static String path = "/api";
	private final static String http = "http";
	private final static String encoding = "UTF-8";
	private final static String agent = "Opera/9.80 (Android; Opera Mini/5.1.22460/20.2485; U; en) Presto/2.5.25";

	private static void allowGzip(AbstractHttpClient client) {
		// client.addRequestInterceptor(new HttpRequestInterceptor() {
		//
		// @Override
		// public void process(HttpRequest request, HttpContext context)
		// throws HttpException, IOException {
		// if (!request.containsHeader("Accept-Encoding")) {
		// request.addHeader("Accept-Encoding", "gzip");
		// request.setHeader(
		// "User-Agent",
		// "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.2.12) Gecko/20101027 Ubuntu/10.10 (maverick) Firefox/3.6.12");
		// }
		// }
		// });
		//
		// client.addResponseInterceptor(new HttpResponseInterceptor() {
		//
		// @Override
		// public void process(HttpResponse response, HttpContext context)
		// throws HttpException, IOException {
		// HttpEntity entity = response.getEntity();
		// Header header = entity.getContentEncoding();
		// if (header != null) {
		// HeaderElement[] elements = header.getElements();
		// for (HeaderElement element : elements) {
		// if (element.getName().equalsIgnoreCase("gzip")) {
		// response.setEntity(new GzipDecompressingEntity(
		// entity));
		// return;
		// }
		// }
		// } else {
		// response.setEntity(entity);
		// }
		// }
		// });
	}

	private static URI createURI(List<NameValuePair> parList)
			throws URISyntaxException {
		return URIUtils.createURI(http, host, port, path,
				URLEncodedUtils.format(parList, encoding), null);
	}

	private static HttpEntity doGet(DefaultHttpClient client,
			List<NameValuePair> parList) {
		try {
			HttpGet get = new HttpGet(createURI(parList));
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			return entity;
		} catch (Exception e) {
			return null;
		}
	}

	private static HttpEntity doPost(DefaultHttpClient client,
			List<NameValuePair> parList) {
		try {
			HttpPost post = new HttpPost(createURI(parList));
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			return entity;
		} catch (Exception e) {
			for (int i = 0; i < 100; i++) {
				i++;
			}
			return null;
		}

	}

	private static HttpEntity doPost(DefaultHttpClient client,
			List<NameValuePair> parList, String parName, int contentLength,
			InputStream image) {
		MultipartEntity postEntity = new MultipartEntity();
		for (NameValuePair pair : parList) {
			try {
				String name = pair.getName();
				StringBody value = new StringBody(pair.getValue(),
						Charset.forName("UTF-8"));
				postEntity.addPart(name, value);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (image != null) {
			postEntity.addPart(parName, new InputStreamKnownSizeBody(image,
					contentLength, "image/*", "avatar"));
		}

		try {
			HttpPost post = new HttpPost(
					createURI(new LinkedList<NameValuePair>()));
			post.setEntity(postEntity);
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			return entity;
		} catch (Exception e) {
			return null;
		}
	}

	public static String getAsString(List<NameValuePair> parList) {
		AndroidHttpClient client = AndroidHttpClient.newInstance(agent);
		try {
			HttpGet get = new HttpGet(createURI(parList));
			HttpResponse response = client.execute(get);
			response.getStatusLine();
			String returnbean = EntityUtils.toString(response.getEntity());
			return returnbean;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			client.close();
		}
		// SchemeRegistry schemeRegistry = new SchemeRegistry();
		// schemeRegistry.register(new Scheme("http", PlainSocketFactory
		// .getSocketFactory(), 80));
		//
		// HttpParams params = new BasicHttpParams();
		//
		// SingleClientConnManager mgr = new SingleClientConnManager(params,
		// schemeRegistry);
		//
		// DefaultHttpClient client = new DefaultHttpClient(mgr, params);
		// DefaultHttpClient client = new DefaultHttpClient();
		// allowGzip(client);
		// HttpEntity entity = doGet(client, parList);
		// if (entity != null) {
		// try {
		// String result = EntityUtils.toString(entity).trim();
		// return result;
		// } catch (Exception e) {
		// } finally {
		// client.getConnectionManager().shutdown();
		// }
		// }
		// return null;
	}

	public static boolean getAsBoolean(List<NameValuePair> parList) {
		DefaultHttpClient client = new DefaultHttpClient();
		allowGzip(client);
		HttpEntity entity = doGet(client, parList);
		if (entity != null) {
			try {
				String result = EntityUtils.toString(entity);
				if (result.trim().equals("1")) {
					return true;
				}
			} catch (Exception e) {

			} finally {
				client.getConnectionManager().shutdown();
			}
		}
		return false;
	}

	public static boolean getToFile(List<NameValuePair> parList, File file) {
		DefaultHttpClient client = new DefaultHttpClient();
		allowGzip(client);
		HttpEntity entity = doGet(client, parList);
		if (entity != null) {
			try {
				if (entity.getContentLength() > 0) {
					FileOutputStream stream = new FileOutputStream(file);
					try {
						entity.writeTo(stream);
					} finally {
						stream.close();
					}
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
			} finally {
				client.getConnectionManager().shutdown();
			}
		}
		return false;
	}

	public static boolean postMessage(List<NameValuePair> parList) {
		DefaultHttpClient client = new DefaultHttpClient();
		allowGzip(client);
		HttpEntity entity = doPost(client, parList);
		if (entity != null) {
			try {
				String result = EntityUtils.toString(entity);
				if (result.trim().equals("1")) {
					return true;
				}else{
					return false;
				}
			} catch (Exception e) {
				return false;
			} finally {
				client.getConnectionManager().shutdown();
			}
		}else{
			return false;
		}
	}

	public static boolean postImage(List<NameValuePair> parList,
			String parName, int contentLength, InputStream in) {
		DefaultHttpClient client = new DefaultHttpClient();
		allowGzip(client);
		HttpEntity entity = doPost(client, parList, parName, contentLength, in);
		if (entity != null) {
			try {
				String result = EntityUtils.toString(entity);
				if (result.trim().equals("1")) {
					return true;
				}
			} catch (Exception e) {

			} finally {
				client.getConnectionManager().shutdown();
			}
		}
		return false;
	}
}
