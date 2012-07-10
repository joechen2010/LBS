package de.gfred.lbbms.mobile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.gfred.lbbms.mobile.services.ILocationService;
import de.gfred.lbbms.mobile.services.IgnoreSelfCertificatesSocketFactory;
import de.gfred.lbbms.mobile.util.Converter;
import de.gfred.lbbms.mobile.util.Values;

/**
 * 
 * @author Frederik Goetz
 * @date 2011.02.03
 */
public class LocationBasedMessaging extends Activity {
    private static final String TAG = "de.gfred.lbbms.mobile.LocationBasedMessaging";
    private static final boolean DEBUG = false;

    private ILocationService locationService;
    private Intent locationServiceIntent;

    private EditText emailText;
    private EditText passwordText;
    private EditText messageText;
    private Button saveButton;
    private SharedPreferences preferences;

    private String email;
    private String password;

    private boolean isServiceBind = false;
    private boolean loginDataSet = false;

    private ServiceConnection locationServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            locationService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            locationService = ILocationService.Stub.asInterface(service);
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        locationServiceIntent = new Intent(ILocationService.class.getName());

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        messageText = (EditText) findViewById(R.id.messageText);
        saveButton = (Button) findViewById(R.id.SaveButton);

        email = preferences.getString(Values.CUSTOMER_EMAIL, null);
        if (email != null) {
            emailText.setText(email);
        }

        password = preferences.getString(Values.CUSTOMER_PASSWORD, null);
        if (password != null) {
            passwordText.setText(password);
        }

        if (email != null && password != null) {
            loginDataSet = true;
            emailText.setVisibility(View.GONE);
            passwordText.setVisibility(View.GONE);
            saveButton.setText("Edit Login");
        }

        isServiceRunning();
    }

    public void onClickSaveLogin(View view) {
        if (loginDataSet) {
            emailText.setVisibility(View.VISIBLE);
            passwordText.setVisibility(View.VISIBLE);
            saveButton.setText("Save Login");
            loginDataSet = false;
        } else {
            if (emailText.getText().toString().trim().length() == 0
                    || passwordText.getText().toString().trim().length() == 0) {
                Toast.makeText(this, "Set login data!", Toast.LENGTH_LONG);
            } else {
                Editor editor = preferences.edit();
                editor.putString(Values.CUSTOMER_EMAIL, emailText.getText().toString());
                editor.putString(Values.CUSTOMER_PASSWORD, passwordText.getText().toString());
                editor.commit();
            }
            emailText.setVisibility(View.GONE);
            passwordText.setVisibility(View.GONE);
            saveButton.setText("Edit Login");
            loginDataSet = true;
        }
    }

    public void onClickServiceStart(View view) {
        if (password == null || email == null) {
            Toast.makeText(this, "Set login data!", Toast.LENGTH_LONG);
        } else {
            getApplicationContext().startService(locationServiceIntent);

            bindService(locationServiceIntent, locationServiceConnection, Context.BIND_AUTO_CREATE);
            isServiceBind = true;
        }
    }

    public void onClickServiceStop(View view) {
        if (isServiceRunning()) {
            getApplicationContext().stopService(locationServiceIntent);

            if (isServiceBind) {
                unbindService(locationServiceConnection);
                isServiceBind = false;
            }

        }
    }

    public void onClickSendMessage(View view) {
        if (messageText != null && messageText.getText().toString().trim().length() > 0) {
            HttpClient client = new DefaultHttpClient();
            client.getConnectionManager().getSchemeRegistry()
                    .register(new Scheme("https", new IgnoreSelfCertificatesSocketFactory(), Values.SCHEME_PORT));

            try {
                HttpPost post = new HttpPost(Values.CUSTOMER_URI);
                post.setEntity(new StringEntity(Converter.createMSGJsonObject(messageText.getText().toString())
                        .toString()));
                post.addHeader(Values.HEADER_TYPE, Values.MIME_TYPE_JSON);
                HttpResponse response = client.execute(post);
                Log.d(TAG, "Send Message = Statuscode: " + response.getStatusLine().getStatusCode());
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isServiceRunning()) {
            if (isServiceBind) {
                unbindService(locationServiceConnection);
                isServiceBind = false;
            }
        }
    }

    private boolean isServiceRunning() {
        final ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        final List<RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (int i = 0; i < services.size(); i++) {
            if ("de.gfred.lbbms.mobile".equals(services.get(i).service.getPackageName())) {

                if ("de.gfred.lbbms.mobile.services.LocationService".equals(services.get(i).service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }
}