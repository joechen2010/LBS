/**
 * 
 */
package cn.edu.nju.software.gof.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

public class HttpUtils {
    
	
	public static String post(String url, String jsonParam) {
        String json = "";
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpPost httpost = new HttpPost(url);
            httpost.setEntity(new StringEntity(jsonParam, "application/json", HTTP.UTF_8));
            HttpResponse response = httpclient.execute(httpost);
            HttpEntity entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK && statusCode != HttpStatus.SC_NO_CONTENT && entity != null) {
                json = extractJsonFromContent(entity);
            }else{
            	json = "Error : " + statusCode;
            }
        } catch (UnsupportedEncodingException e) {
           // throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            //throw new RuntimeException(e);
        } catch (IOException e) {
            //throw new RuntimeException(e);
        } finally {
            // shut down the connection manager to ensure
            httpclient.getConnectionManager().shutdown();
        }
        return json;
    }
	
	public static String post(String url, String jsonParam,String proxyHost, Integer port) {
        String json = "";
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost httpost = new HttpPost(url);;
        try {
        	if (proxyHost != null) {
        		HttpHost proxy = new HttpHost(proxyHost, port);
    			httpclient.getParams().setParameter(ConnRouteParams.DEFAULT_PROXY,
    					proxy);
    		}
    			
            httpost.setEntity(new StringEntity(jsonParam, "application/json", HTTP.UTF_8));
            HttpResponse response = httpclient.execute(httpost);
            HttpEntity entity = response.getEntity();
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK && statusCode != HttpStatus.SC_NO_CONTENT && entity != null) {
                json = extractJsonFromContent(entity);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return json;
    }
	
    public static String extractJsonFromContent(HttpEntity entity) throws IOException {
        String json = "";
        InputStream instream = entity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(instream, HTTP.UTF_8), 1024);
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[1024];
        int charsRead = 0;
        while ((charsRead = reader.read(buffer)) != -1) {
            sb.append(buffer, 0, charsRead);
        }
        json = sb.toString();
        reader.close();
        instream.close();
        return json;
    }
}
