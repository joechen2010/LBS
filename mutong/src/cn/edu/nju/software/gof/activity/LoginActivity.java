package cn.edu.nju.software.gof.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import cn.edu.nju.software.gof.intent.CommonIntents;
import cn.edu.nju.software.gof.requests.AccountUtilities;

public class LoginActivity extends Activity {

	private static final int INFO_MISSING = 0;
	private static final int WAITTING_DIALOG = 1;
	private static final int LOGIN_FAILED = 2;
	private static final String SETTING_INFOS = "SETTING_Infos";
	private static String userName = "";
	private static String password = "";

	private Button registerButton = null;
	private Button loginButton = null;
	private CheckBox saveUserName = null;
	private CheckBox savePassword = null;
	private EditText userNameEdit = null;
	private EditText passwordEdit = null;
	//
	private Resources resources = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		//
		retriveViews();
		autoEnterNP();
		registerEventHandler();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.network_setting:
			CommonIntents.goToNetworkSetting(this);
			return true;
		case R.id.exit_program:
			LoginActivity.this.finish();
			return true;
		}

		return false;
	}

	private void retriveViews() {
		resources = getResources();
		registerButton = (Button) findViewById(R.id.register_button);
		loginButton = (Button) findViewById(R.id.login_button);
		saveUserName = (CheckBox) findViewById(R.id.save_user_name);
		savePassword = (CheckBox) findViewById(R.id.save_password);
		userNameEdit = (EditText) findViewById(R.id.user_name_edit);
		passwordEdit = (EditText) findViewById(R.id.password_edit);
	}

	private void autoEnterNP() {
		SharedPreferences settings = getSharedPreferences(SETTING_INFOS, 0);
		userNameEdit.setText(settings.getString("USERNAME", ""));
		passwordEdit.setText(settings.getString("PASSWORD", ""));
		if(!(settings.getString("USERNAME", "").equals("") || 
				settings.getString("PASSWORD", "").equals(""))){
			//login();
		}
	}

	private void registerEventHandler() {
		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(intent);
			}

		});

		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				login();
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case INFO_MISSING:
			return NotifyUtilities.createMessageDialog(this,
					resources.getString(R.string.info_missing));
		case WAITTING_DIALOG:
			return NotifyUtilities.createProgressDialog(this);
		case LOGIN_FAILED:
			return NotifyUtilities.createMessageDialog(this,
					resources.getString(R.string.login_failed));
		}
		return null;
	}

	private void login() {
		userName = userNameEdit.getText().toString();
		password = passwordEdit.getText().toString();
		SharedPreferences settings = getSharedPreferences(SETTING_INFOS, 0);
		if (saveUserName.isChecked()) {
			settings.edit().putString("USERNAME", userName).commit();

			if (savePassword.isChecked()) {
				settings.edit().putString("PASSWORD", userName).commit();
			}
		}
		if (userName.equals("") || password.equals("")) {
			showDialog(INFO_MISSING);
		} else {
			showDialog(WAITTING_DIALOG);
			(new AsyncTask<Void, Void, Void>() {

				private String response = null;

				@Override
				protected Void doInBackground(Void... params) {
					response = AccountUtilities.login(userName, password);
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
					dismissDialog(WAITTING_DIALOG);
					if (response == null || !response.contains("#")) {
						showDialog(LOGIN_FAILED);
					} else {
						String[] ids = response.split("#");
						String sessionID = ids[0];
						String userID = ids[1];
						MyApplication application = (MyApplication) getApplication();
						application.setData("sessionID", sessionID);
						application.setData("userID", userID);
						Intent intent = new Intent(getApplicationContext(),
								MainTabActivity.class);
						startActivity(intent);
						finish();
					}
				}
			}).execute();
		}
	}
}