package ntu.sce.fyp.easilocation.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import ntu.sce.fyp.easilocation.R;
import ntu.sce.fyp.easilocation.io.LoginHandler;
import ntu.sce.fyp.easilocation.io.ParsedLoginDataSet;
import ntu.sce.fyp.easilocation.io.SignupHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WebActivity extends Activity {
	
	private static final String TAG = "WebActivity";
	private static final URI SIGNUP_URL = URI.create("http://easilocation.appspot.com/create");

	private static final int SIGNUP_DIALOG = 0;
	private static final int ERROR_DIALOG = 1;

	
	private EditText username;
	private EditText password;
	private EditText email;
	private EditText handphone;
	private DatePicker datePicker1;

	private ProgressDialog loginDialog;
	private AlertDialog alertDialog;

	private SignupThread mSignUpThread;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		((TextView) findViewById(R.id.title_text)).setText(R.string.create);
		
		username = (EditText) findViewById(R.id.editText1);
		password = (EditText) findViewById(R.id.editText2);
		email = (EditText) findViewById(R.id.editText3);
		handphone = (EditText) findViewById(R.id.editText4);
		datePicker1 = (DatePicker) findViewById(R.id.datePicker1);
		
		// direct user to web login
		/*WebView mWebView = (WebView) findViewById(R.id.webview);
		WebSettings mWebSetting = mWebView.getSettings();
		mWebSetting.setJavaScriptEnabled(true);
		mWebView.loadUrl(DEFAULT_URL);*/
		
		// handle login inside the app
		
	}
	
	public void onSignUpClick(View v) {
		String editText1 = username.getText().toString();
		String editText2 = password.getText().toString();
		String editText3 = email.getText().toString();
		String editText4 = handphone.getText().toString();
		if (editText1 == null) {
			Toast.makeText(getApplicationContext(), "Please specify a username", Toast.LENGTH_SHORT).show();
		} else if (editText2 == null) {
			Toast.makeText(getApplicationContext(), "Please choose a password", Toast.LENGTH_SHORT).show();
		} else if (editText3 == null) {
			Toast.makeText(getApplicationContext(), "Please fill in email address", Toast.LENGTH_SHORT).show();
		} else if (editText4 == null) {
			Toast.makeText(getApplicationContext(), "Please provide handphone number", Toast.LENGTH_SHORT).show();
		} else {
			showDialog(SIGNUP_DIALOG);
		}
	}
	
	public void onBackButtonClick(View v) {
		this.finish();
	}
	
	/** handles creation of dialogs */
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		
		case SIGNUP_DIALOG:
			loginDialog = new ProgressDialog(this);
			loginDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            loginDialog.setMessage("Creating user...");
            loginDialog.setIndeterminate(true);
            loginDialog.setCancelable(false);
            return loginDialog;

		case ERROR_DIALOG:
			AlertDialog.Builder b1 = new Builder(getApplicationContext());
			b1.setTitle("Error");
			b1.setMessage("Cannot create user profile at this moment, please try again later");
			b1.setCancelable(false);
			b1.setPositiveButton("OK", new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			return b1.create();
			
		default:
			return null;
		}
	}
	
	@Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch(id) {
        
        case SIGNUP_DIALOG:
        	mSignUpThread = new SignupThread();
        	mSignUpThread.start();
        	break;
        }
    }
	
	/** private class to do background activity: sign up for user */
	private class SignupThread extends Thread {
		public void run() {
			tryToCreateUser();
		}
	}

	private void tryToCreateUser() {
		Log.v(TAG, "Trying to login to system");
		/** remember user preferences */
		String editText1 = username.getText().toString();
		String editText2 = password.getText().toString();
		String editText3 = email.getText().toString();
		String editText4 = handphone.getText().toString();
		String birthday = getDate(datePicker1);
		
		/** Send a HTTP POST login request */
		HttpClient client = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(SIGNUP_URL);
        List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
        pairs.add(new BasicNameValuePair("username", editText1));
        pairs.add(new BasicNameValuePair("password", editText2));
        pairs.add(new BasicNameValuePair("email", editText3));
        pairs.add(new BasicNameValuePair("handphone", editText4));
        pairs.add(new BasicNameValuePair("birthday", birthday));
        
        try {
			UrlEncodedFormEntity urlEntity= new UrlEncodedFormEntity(pairs, "UTF-8");
			httppost.setEntity(urlEntity);
			Log.v(TAG, "Sending HTTP POST message to server and waiting for reply");
			HttpResponse response = client.execute(httppost);
			HttpEntity responseEntity= response.getEntity();
			
			// TODO handles response after register nick
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			XMLReader xr = sp.getXMLReader();

			SignupHandler mSignupHandler = new SignupHandler();
			xr.setContentHandler(mSignupHandler);
			xr.parse(retrieveInputStream(responseEntity));
			ParsedLoginDataSet parsedLoginDataSet = mSignupHandler
					.getParsedLoginData();
			
			if (parsedLoginDataSet.getExtractedString().equals("SUCCESS")) {
				// send a message back to handler of 2nd working thread
				Message myMessage = new Message();
				myMessage.obj = "SUCCESS";
				mHandler.sendMessage(myMessage);

			} else if (parsedLoginDataSet.getExtractedString().equals("ERROR")) {
				// failed to create user, display error dialog box
				removeDialog(SIGNUP_DIALOG);
				showDialog(ERROR_DIALOG);
			}
		} catch (Exception e) {
			removeDialog(SIGNUP_DIALOG);
			showDialog(ERROR_DIALOG);
			e.printStackTrace();
		}
	}

	private String getDate(DatePicker dp) {
		StringBuilder sb = new StringBuilder();
		sb.append(dp.getDayOfMonth() + "/");
		sb.append(dp.getMonth()+1);
		sb.append("/" + dp.getYear());
		return sb.toString();
	}
	
	private InputSource retrieveInputStream(HttpEntity httpEntity) {
		InputSource is = null;
		try {
			is = new InputSource(httpEntity.getContent());
		} catch (Exception e) {
		}
		return is;
	}
	
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String loginmsg = (String) msg.obj;
			if (loginmsg.equalsIgnoreCase("SUCCESS")) {
				removeDialog(SIGNUP_DIALOG);
				finish();
			}
		}
	};
}
