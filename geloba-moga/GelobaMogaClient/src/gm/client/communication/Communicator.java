/**
 * 
 */
package gm.client.communication;

import gm.client.exception.HTTPClientException;
import gm.client.exception.NotLoggedinException;
import gm.shared.action.Action;
import gm.shared.utils.HashMaker;
import gm.shared.utils.Serializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

/**
 * Communicator Singleton
 * 
 * @author stefan
 *
 */
public class Communicator implements Runnable {
	/**
	 * TAG the current Activity
	 * used my logging
	 */
	private static final String TAG = "Communicator";
	
	/**
	 * Holds the one and only Communicator instance
	 */
	private static Communicator me = new Communicator();
	
	/**
	 * The Session ID of the session with the server
	 */
	private String session = "not";
	
	/**
	 * The Uniform Resource Identifier of the GelobaMoga-Server
	 * mind the "/" at the end!
	 */
	private String serverURI = "http://dev.easycoders.org/GelobaMogaServer/";
	
	/**
	 * Holds the Listeners and patterns to be called in case of 
	 * pattern-match on the message returned by the server while polling
	 */
	private HashMap<Pattern, PollingListener> pollingListenerRX = new HashMap<Pattern, PollingListener>();
	private HashMap<String, PollingListener> pollingListener = new HashMap<String, PollingListener>();
	
	/**
	 * Polling-Stuff
	 */
	private Thread poller = null;
	private boolean do_polling = false;
	private int polling_interval = 10;
	
	/**
	 * Apache httpClient stuff
	 */
	private HttpClient httpClient = null;
	HttpContext localContext = new BasicHttpContext();
	
/////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Private conscructor (singleton!)
	 */
	private Communicator () { 
		Log.v(TAG, "constructor");
		if (null == this.httpClient) {
			 // Apache HTTPClient
			 this.httpClient = new DefaultHttpClient();
			 // Connection Timeout  setzen
			 HttpConnectionParams.setSoTimeout(this.httpClient.getParams(), 5000);
			 //HttpConnectionParams.setConnectionTimeout(this.httpClient.getParams(), 5000);
		 }
	}
	
	/**
	 * 
	 * @return the one and only Communicator instance
	 */
	public static Communicator get() {
		Log.v(TAG, "returning myself");
		if(Communicator.me == null) 
			Communicator.me = new Communicator();
		return Communicator.me;
	}
	
	/**
	 * 
	 * @return the one and only UserManager instance
	 */
	public UserManager getUserManager() {
		Log.v(TAG, "returning UserManager..");
		return UserManager.get();
	}
	
	/**
	 * @return the one and only GeoManager instance
	 */
	public GeoManager getGeoManager() {
		Log.v(TAG, "returning GeoManager..");
		return GeoManager.get();
	}
	
	/**
	 * Sets the Uniform Resource Identifier of the GelobaMoga-Server
	 * (for example http://dev.easycoders.org/GelobaMogaServer/)
	 * @param serverURI
	 */
	public void setEndpoint(String suri) {
		suri = suri.trim();
		if (!suri.endsWith("/")) suri = suri + "/";
		
		Log.v(TAG, "setEndpoint to " +suri);
		this.serverURI = suri;
	}
	/**
	 * @return the Uniform Resource Identifier of the GelobaMoga-Server 
	 * (for example http://dev.easycoders.org/GelobaMogaServer/)
	 * mind the "/" at the end!
	 */
	public String getEndpoint() {
		Log.v(TAG, "getEndpoint =" +this.serverURI);
		return this.serverURI;
	}
	
	/**
	 * Sets the timeout of the HttpClient
	 * @param ms timeout in miliseconds
	 */
	public void setTimeout(int ms) {
		Log.v(TAG, "setTimeout to " +ms);
		HttpConnectionParams.setSoTimeout(this.httpClient.getParams(), ms);
	}
	
	public int getTimeout() {
		Log.v(TAG, "getTimeout = ");
		return HttpConnectionParams.getSoTimeout(this.httpClient.getParams());
	}
	
	/**
	 * nottifies the server with the given string
	 * @param message
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws NotLoggedinException 
	 * @throws HTTPClientException 
	 */
	public boolean notifyServer(String message) throws ClientProtocolException, IOException, NotLoggedinException, HTTPClientException {
		Log.v(TAG, "notify the server: " +message);
		
		try {
		
			if (UserManager.get().isLoggedIn()) {
				
				String[][] params = {{"messageText",message}};
				BasicHttpResponse r = doPostRequest("notifications", params);
				
				if (getStatusCode(r) == HttpURLConnection.HTTP_OK) {
					
					String res = getResponseContent(r);
					if (res.toLowerCase().trim().equals("true")) return true;
					else return false;
					
				} else throw new HTTPClientException(getStatusCode(r));
				
			} else throw new NotLoggedinException();
		
		} catch(IllegalStateException e) {
			Log.e(TAG, "IllegalStateException");
			throw new HTTPClientException(-1);
		}
	}
	
	/**
	 * begins to poll the server in the given interval
	 * @param interval in seconds
	 */
	public void startPolling(int interval) {
		Log.v(TAG, "startPolling in the interval of " +interval +" seconds..");
		this.polling_interval = interval;
		this.poller.start();
	}
	
	/**
	 * stops to poll the server
	 */
	public void stopPolling() {
		Log.v(TAG, "stopPolling");
		this.do_polling = false;
		//this.poller.stop();
	}
	
	/**
	 * The polling-thread
	 */
	public void run() {
		int error_counter = 0;
		
		while (this.do_polling) {
			try {
				
				String[][] getParams = {{}};
				BasicHttpResponse r = doGetRequest("notifications", getParams);
				
				if (getStatusCode(r) == HttpURLConnection.HTTP_OK) {
					
					processPolling(getResponseContent(r));
					
				} else throw new HTTPClientException(getStatusCode(r));
				
				Thread.sleep(this.polling_interval*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Log.e(TAG, e.getMessage());
				error_counter++;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				Log.e(TAG, e.getMessage());
				error_counter++;
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(TAG, e.getMessage());
				error_counter++;
			} catch (HTTPClientException e) {
				e.printStackTrace();
				Log.e(TAG, e.getMessage());
				error_counter++;
			} finally {
				// stop the thread if more then 10 errors/exceptions occurred 
				this.do_polling = (error_counter < 10);
				if (!this.do_polling) Log.w(TAG, "Polling stoped: To many (>10) erros/exceptions. See Error-Log.");
			}
		}
		
	}
	/**
	 * Process the Response of the GelobaMoga-Server on the polling request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	private void processPolling(String response) {
		ArrayList<String> messages = new ArrayList<String>();
		
		try {
			messages = (ArrayList<String>)Serializer.deserialize(response);
			
			for (String msg : messages) {
				notifyPollingListener(msg);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		}
	}
	
	/**
	 * Notifies the polling-listener saved in pollingListener and pollingListenerRX
	 * @param msg
	 */
	private void notifyPollingListener(String msg) {
		
		// check if a listener defined by a string matches:
		
		Set<Map.Entry<String, PollingListener>> set = this.pollingListener.entrySet();
	    Iterator<Map.Entry<String, PollingListener>> i = set.iterator();

	    while(i.hasNext()){
	      Map.Entry<String, PollingListener> me = (Map.Entry<String, PollingListener>)i.next();
	      
	      if (msg.equals(me.getKey())) {
	    	  PollingEvent event = new PollingEvent(this, msg);
	    	  me.getValue().receive(event);
	      }
	    }
	    
	    // check if a listener defined by a regular expression matches:
	    
	    Set<Map.Entry<Pattern, PollingListener>> setRX = this.pollingListenerRX.entrySet();
	    Iterator<Map.Entry<Pattern, PollingListener>> iRK = setRX.iterator();

	    while(iRK.hasNext()){
		      Map.Entry<Pattern, PollingListener> meRK = (Map.Entry<Pattern, PollingListener>)iRK.next();
		      
		      if (meRK.getKey().matcher(msg).matches()) {
		    	  PollingEvent event = new PollingEvent(this, msg);
		    	  meRK.getValue().receive(event);
		      }
		    }

	}

	/**
	 * Sets a PollingListener.
	 * The given PollingListener is called if a received message matches the given pattern.
	 * @param listener
	 * @param pattern
	 */
	public void setPollingListener(PollingListener listener, String pattern) {
		Log.v(TAG, "setPollingListener by String Pattern");
		this.pollingListener.put(pattern, listener);
	}
	
	/**
	 * Sets a PollingListener.
	 * The given PollingListener is called if a received message matches the given pattern.
	 * 
	 * @param listener
	 * @param pattern (regular expression)
	 */
	public void setPollingListener(PollingListener listener, Pattern pattern) {
		Log.v(TAG, "setPollingListener by RegEx Pattern");
		this.pollingListenerRX.put(pattern, listener);
	}
	
	/**
	 * Tries to login the given user with the given password
	 * @param username
	 * @param password
	 * @return login status
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws HTTPClientException 
	 */
	public boolean login(String username, String password) throws ClientProtocolException, IOException, HTTPClientException {
		boolean status = false;
		Log.v(TAG, "login...");
		
		String[][] getParams = {{}};
		this.session = username;
		BasicHttpResponse r = doGetRequest("session", getParams);
		
		if (getStatusCode(r) == HttpURLConnection.HTTP_OK) {
			// -> user exists
			this.session = getResponseContent(r);
			
			// Passwort verschlsseln
			String passToSend = HashMaker.md5(this.session + HashMaker.md5(password));
			
			BasicHttpResponse r2 = doSimplePostRequest("session", passToSend);
			
			if (getStatusCode(r2) == HttpURLConnection.HTTP_OK) {
				// password also correct
				Log.d(TAG, "logged in");
				status = true;
			} else throw new HTTPClientException(getStatusCode(r2));
		} else throw new HTTPClientException(getStatusCode(r));
		
		return status;
	}
	
	public Action applyAction(Action a) throws ClientProtocolException, IOException, IllegalStateException, ClassNotFoundException, HTTPClientException {
		Action res = a;
		a.beforeServer();
		
		String actionString = Serializer.serialize(a);
		
		Log.d(TAG, "action before server request:");
		Log.d(TAG, actionString);
		
		Log.d(TAG, "do action-request to server..");
		BasicHttpResponse r = doSimplePostRequest("action", actionString);
		
		if (getStatusCode(r) == HttpURLConnection.HTTP_OK) {
			
			// res = a.getClass().cast(Serializer.deserialize(getResponseContent(r)));
			res = (Action)(Serializer.deserialize(getResponseContent(r)));
		
			Log.d(TAG, "action after server request:");
			Log.d(TAG, getResponseContent(r));
			
			a.afterServer();
			
		} else throw new HTTPClientException(getStatusCode(r));
		
		return res;
	}

////////////////////////////////////////////////////////////////////////////////////////////////
	
	private BasicHttpResponse doGetRequest(String resource, String[][] params) throws ClientProtocolException, IOException {
		
		
		//also encode the resource to be used in the request:
		resource = URLEncoder.encode(resource);

		// build the request-object
		HttpGet g = new HttpGet(this.serverURI +resource +"/" +this.session +prepareParams(params));
		
		Log.d(TAG, "do get request to " +g.getURI().toString());
		
		// do the request
		BasicHttpResponse r = (BasicHttpResponse)this.httpClient.execute(g, this.localContext);
		
		return r;
	}
	
	private BasicHttpResponse doPostRequest(String resource, String[][] params) throws ClientProtocolException, IOException {
		
		// encode the resource to be used in the request:
		resource = URLEncoder.encode(resource);

		// build the request-object
		HttpPost p = new HttpPost(this.serverURI +resource +"/" +this.session);
		
		Log.d(TAG, "do post request to " +p.getURI().toString());
		
		// build the param object for the http-post-request
		 List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		 for (int i = 0; i < params.length; i++) {
			 if (params[i].length >= 2) {
				nameValuePairs.add(new BasicNameValuePair(params[i][0], params[i][1])); 
			 }
		 }
		 p.setEntity(new UrlEncodedFormEntity(nameValuePairs));  
		 
		// do the request
		BasicHttpResponse r = (BasicHttpResponse)this.httpClient.execute(p, this.localContext);
		
		return r;
	}
	
private BasicHttpResponse doSimplePostRequest(String resource, String content) throws ClientProtocolException, IOException {
		
		// encode the resource to be used in the request:
		resource = URLEncoder.encode(resource);

		// build the request-object
		HttpPost p = new HttpPost(this.serverURI +resource +"/" +this.session);
		
		Log.d(TAG, "do post request to " +p.getURI().toString());
		
		// build the param object for the http-post-request
		
		 p.setEntity(new StringEntity(content));  
		 
		// do the request
		BasicHttpResponse r = (BasicHttpResponse)this.httpClient.execute(p, this.localContext);
		
		return r;
	}

	private String prepareParams(String[][] params) {
		String param = "";
		
		// build the param string for the http-get-request
		for (int i = 0; i < params.length; i++) {
			if (params[i].length >= 2) {
				if (param.equals("")) param = "?";
				else param += "&";
				param += URLEncoder.encode(params[i][0]);
				param += "=";
				param += URLEncoder.encode(params[i][1]);
			}
		}
		return param;
	}

	private int getStatusCode(BasicHttpResponse r) {
		return r.getStatusLine().getStatusCode();
	}
	/**
	 * Helper-Methode zum generieren eines String aus einem HTTP-Response Stream
	 * @param stream
	 * @return
	 * @throws IOException 
	 */
	private String getResponseContent(InputStream stream) throws IOException {
		 Log.v(TAG, "helping the Communicator to get HTTP response content...");
		  InputStreamReader reader = new InputStreamReader(stream);
		  BufferedReader buffer = new BufferedReader(reader);
		  StringBuilder sb = new StringBuilder();

		      String cur;
		      while ((cur = buffer.readLine()) != null) {
		          sb.append(cur + System.getProperty("line.separator"));
		        }
		    sb.setLength(sb.length()-1);
		    Log.v(TAG, "helping the Communicator to get HTTP response content... DONE");
		    return sb.toString();
		
			/*BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
			StringBuilder stringBuilder = new StringBuilder();
			String line = null;
	
			while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line + System.getProperty("line.separator"));
			}
	
			bufferedReader.close();
			return stringBuilder.toString();*/
		}
	private String getResponseContent(BasicHttpResponse r) throws IllegalStateException, IOException {
		return getResponseContent(r.getEntity().getContent());
	}
////////////////////////////////////////////////////////////////////////////////////////////////
}
