package ntu.sce.fyp.easilocation.ui;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.*;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import ntu.sce.fyp.easilocation.R;
import ntu.sce.fyp.easilocation.io.LoginHandler;
import ntu.sce.fyp.easilocation.io.ParsedLoginDataSet;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author DuongDangChien
 *
 */
public class LoginActivity extends Activity {
	// VERBOSE debug log is complied in but stripped at runtime
	private static final String TAG = "LoginActivity";

	private static final int INTERNET_DIALOG = 0;
	private static final int LOGIN_DIALOG = 1;
	private static final int ERROR_DIALOG = 2;
	private static final String LOGIN_URL = "http://2.easilocation.appspot.com/signIn";
	
	private State mState;
	private EditText username;
	private EditText password;
	private CheckBox checkbox;
	private SharedPreferences mSharedPreferences;
	
	private ProgressDialog loginDialog;
	private LoginThread mLoginThread;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mSharedPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
		
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		checkbox = (CheckBox) findViewById(R.id.check_box);
		mState = (State) getLastNonConfigurationInstance();
		final boolean previousState = mState != null;
		
		/** Perform a check whether user can access Internet */
		if (previousState) {
			if (mState.mWifiConnection) {
				Toast.makeText(this, "Connected to the Internet 1", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Still not connected to the Internet", Toast.LENGTH_SHORT).show();
				showDialog(INTERNET_DIALOG);
			}
		} else {
			mState = new State();
			if (isConnectedToInternet()) {
//				Toast.makeText(this, "Connected to the Internet 2", Toast.LENGTH_SHORT).show();
				mState.mWifiConnection = true;
			} else {
//				Toast.makeText(this, "Not connected to the Internet", Toast.LENGTH_SHORT).show();
				showDialog(INTERNET_DIALOG);
			}
		}
		
		/** If user logged in, come straight to {@link HomeActivity} */
		if (checkLoginInfo()) {
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	@Override
    public Object onRetainNonConfigurationInstance() {
        return mState;
    }
	
	private final boolean checkLoginInfo() {
		boolean username_set = mSharedPreferences.contains("username");
		boolean password_set = mSharedPreferences.contains("password");
		boolean remember_login = mSharedPreferences.contains("remember_login");
		if (username_set || password_set ||remember_login) {
			return true;
		}
		return false;
	}
	
	/** Handles "Signing in..." action */
	public void onSigninClick (View v) {
		String user = username.getText().toString();
		String pass = password.getText().toString();
		if (user.length() == 0) {
			Toast.makeText(this, "Enter a username and a password", Toast.LENGTH_SHORT).show();
		} else if (pass.length() == 0){
			Toast.makeText(this, "Enter a password", Toast.LENGTH_SHORT).show();
		} else{
			showDialog(LOGIN_DIALOG);
		}
	}
	
	/** Handles "Creating new user..." action */
	public void onCreateClick (View v) {
		startActivity(new Intent(this, WebActivity.class));
	}
	
	/** 
	 * Handles displaying various dialog:
	 * - no Internet connection
	 * - logging in dialog
	 * - login error dialog
	 */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		
		case INTERNET_DIALOG:
			AlertDialog.Builder b1 = new Builder(this);
			b1.setTitle(R.string.no_internet_connection_title);
			b1.setMessage(R.string.no_internet_connection_message);
			b1.setCancelable(false);
			b1.setPositiveButton("Settings", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
				}
			});
			b1.setNegativeButton("Exit", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					LoginActivity.this.finish();
				}
			});
			mState.mWifiConnection = true;
			return b1.create();
			
		case LOGIN_DIALOG:
			loginDialog = new ProgressDialog(this);
			loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loginDialog.setMessage("Logging in...");
            loginDialog.setIndeterminate(true);
            loginDialog.setCancelable(false);
            return loginDialog;
            
		case ERROR_DIALOG:
			AlertDialog.Builder b2 = new Builder(this);
			b2.setTitle("Login error!");
			b2.setMessage("Please try again...");
			b2.setNeutralButton("OK", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					removeDialog(ERROR_DIALOG);
				}
			});
			b2.setCancelable(false);
			return b2.create();
            
		default:
			return null;
		}
	}
	
	@Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch(id) {
        
        case LOGIN_DIALOG:
        	mLoginThread = new LoginThread();
        	mLoginThread.start();
        	break;
        }
    }
	
	/** Nested class that performs login process */
	private class LoginThread extends Thread {
		@Override
		public void run() {
			// running login process here
			tryLogin();
		}
	}
	
	/** Make a connection to server and log user into the system */
	private void tryLogin() {
		Log.v(TAG, "Trying to login to system");
		/** remember user preferences */
		String user = username.getText().toString();
		String pass = password.getText().toString();
		String remember_login = checkbox.isChecked() ? "yes" : "no";

		/** Send a HTTP POST login request */
		HttpClient client = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(LOGIN_URL);
		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("username", user));
		pairs.add(new BasicNameValuePair("password", pass));

		try {
			UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(pairs,
					HTTP.UTF_8);
			httppost.setEntity(p_entity);

			// send request and retrieve response from server
			HttpResponse response = client.execute(httppost);
			Log.v(TAG, response.getStatusLine().toString());
			HttpEntity responseEntity = response.getEntity();
			Log.v(TAG, "Set response to responseEntity");

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			LoginHandler mLoginHandler = new LoginHandler();
			xr.setContentHandler(mLoginHandler);
			xr.parse(retrieveInputStream(responseEntity));
			ParsedLoginDataSet parsedLoginDataSet = mLoginHandler
					.getParsedLoginData();

			if (parsedLoginDataSet.getExtractedString().equals("SUCCESS")) {
				// Store the username and password in SharedPreferences after
				// the successful login
				SharedPreferences.Editor editor = mSharedPreferences.edit();
				editor.putString("username", user);
				editor.putString("password", pass);
				editor.putString("remember_login", remember_login);
				editor.commit();
				// send a message back to handler of 2nd working thread
				Message myMessage = new Message();
				myMessage.obj = "SUCCESS";
				mHandler.sendMessage(myMessage);

			} else if (parsedLoginDataSet.getExtractedString().equals("ERROR")) {
				Message myMessage = new Message();
				myMessage.obj = "ERROR";
				mHandler.sendMessage(myMessage);
			}
		} catch (Exception e) {
			Message myMessage = new Message();
			myMessage.obj = "ERROR";
			mHandler.sendMessage(myMessage);
		}

	}

	private InputSource retrieveInputStream(HttpEntity httpEntity) {
		InputSource is = null;
		try {
			is = new InputSource(httpEntity.getContent());
		} catch (Exception e) {
		}
		return is;
	}

	/**
	 * Define the Handler that receives messages from login thread and end the
	 * login process
	 */
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String loginmsg = (String) msg.obj;
			if (loginmsg.equalsIgnoreCase("SUCCESS")) {
				removeDialog(LOGIN_DIALOG);
				Intent intent = new Intent(getApplicationContext(),
						HomeActivity.class);
				intent.putExtra("username", mSharedPreferences.getString(
						"username", ""));
				intent.putExtra("password", mSharedPreferences.getString(
						"password", ""));
				startActivity(intent);
				finish();
			} else if (loginmsg.equalsIgnoreCase("ERROR")) {
				removeDialog(LOGIN_DIALOG);
				showDialog(ERROR_DIALOG);
			}
		}
	};
	
	/**
	 * @return boolean return true if the application can access the internet
	 */
	private boolean isConnectedToInternet() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
		if (info == null) {
			return false;
		} else if (!info.isConnected()) {
			return false;
		} /*else if (info.getType() != ConnectivityManager.TYPE_WIFI) {
			return false;
		}*/ else if (info.getType() != ConnectivityManager.TYPE_MOBILE) {
			return false;
		} 
		return true;
	}
	
	/** The state of this application (preferences....) */
	private static class State {
		public boolean mWifiConnection = false;
	}
}
