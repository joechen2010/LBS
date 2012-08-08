package cn.edu.nju.software.gof.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;
import cn.edu.nju.software.gof.beans.PersonInformationBean;
import cn.edu.nju.software.gof.beans.User;
import cn.edu.nju.software.gof.requests.AccountUtilities;

public class RegisterActivity extends Activity {

	private final static int DATE_DIALOG_ID = 0;
	private final static int USER_NAME_EXISTED_DIALOG_ID = 1;
	private final static int PASSWORD_NOT_SAME = 2;
	private final static int INFO_MISSING = 3;
	private final static int WAITTING_DIALOG = 4;

	private EditText userNameEdit = null;
	private EditText passwordEdit = null;
	private EditText conformPasswordEdit = null;
	private EditText realNameEdit = null;
	//
	private EditText birthdayEdit = null;
	private EditText schoolEdit = null;
	private EditText placeEdit = null;
	//
	private ImageView moreItem = null;
	private TableLayout hidedItems = null;
	//
	private CheckBox agreeRules = null;
	private Button registerButton = null;
	//
	private Resources resources = null;
	//
	private boolean isUserExisted = false;
	//
	private MyDatePicker datePicker = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		//
		retriveViews();
		registerEventHandler();
	}

	private void retriveViews() {
		resources = getResources();
		//
		userNameEdit = (EditText) findViewById(R.id.user_name_edit);
		passwordEdit = (EditText) findViewById(R.id.password_edit);
		conformPasswordEdit = (EditText) findViewById(R.id.conform_password_edit);
		realNameEdit = (EditText) findViewById(R.id.real_name_edit);
		//
		birthdayEdit = (EditText) findViewById(R.id.birthday_edit);
		placeEdit = (EditText) findViewById(R.id.place_edit);
		schoolEdit = (EditText) findViewById(R.id.school_edit);
		//
		hidedItems = (TableLayout) findViewById(R.id.hide_info);
		moreItem = (ImageView) findViewById(R.id.more_info);
		agreeRules = (CheckBox) findViewById(R.id.aggree_rules);
		registerButton = (Button) findViewById(R.id.register_button);
		datePicker = new MyDatePicker(this, birthdayEdit);
	}

	private void registerEventHandler() {

		userNameEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					checkUserName();
				}

			}
		});

		birthdayEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					RegisterActivity.this.showDialog(DATE_DIALOG_ID);
				}
			}
		});

		moreItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleMoreItems();
			}
		});

		agreeRules.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				registerButton.setEnabled(isChecked);
			}
		});

		registerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				register();
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return datePicker.getDialog();

		case USER_NAME_EXISTED_DIALOG_ID:
			return NotifyUtilities.createMessageDialog(this,
					resources.getString(R.string.user_name_existed));
		case PASSWORD_NOT_SAME:
			return NotifyUtilities.createMessageDialog(this,
					resources.getString(R.string.password_not_same));
		case INFO_MISSING:
			return NotifyUtilities.createMessageDialog(this,
					resources.getString(R.string.info_missing));
		case WAITTING_DIALOG:
			return NotifyUtilities.createProgressDialog(this);
		}

		return null;
	}

	private void checkUserName() {
		final String userName = userNameEdit.getText().toString();
		(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				isUserExisted = AccountUtilities.checkExisted(userName);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if (isUserExisted) {
					userNameEdit.setTextColor(resources
							.getColor(R.color.user_name_existed));
				} else {
					userNameEdit.setTextColor(resources
							.getColor(android.R.color.black));
				}
			}

		}).execute();
	}

	private void handleMoreItems() {
		int visibility = hidedItems.getVisibility();
		hidedItems.setVisibility(visibility == View.GONE ? View.VISIBLE
				: View.GONE);
		moreItem.setImageResource(visibility == View.GONE ? R.drawable.arrow_right
				: R.drawable.arrow_down);
	}

	private void register() {
		final String userName = userNameEdit.getText().toString();
		final String password = passwordEdit.getText().toString();
		String conformPassword = conformPasswordEdit.getText().toString();
		String realName = realNameEdit.getText().toString();
		if (userName.equals("") || password.equals("")
				|| conformPassword.equals("") || realName.equals("")) {
			showDialog(INFO_MISSING);
		} else if (isUserExisted) {
			showDialog(USER_NAME_EXISTED_DIALOG_ID);
		} else if (!password.equals(conformPassword)) {
			showDialog(PASSWORD_NOT_SAME);
		} else {
			String school = schoolEdit.getText().toString();
			String place = placeEdit.getText().toString();
			String birthday = datePicker.getDateStr();
			String mobile = User.getInstance().getMobile();
			final PersonInformationBean information = new PersonInformationBean(
					realName, birthday, school, place, mobile);
			showDialog(WAITTING_DIALOG);
			(new AsyncTask<Void, Void, Void>() {

				private boolean success = false;

				@Override
				protected Void doInBackground(Void... params) {
					success = AccountUtilities.register(userName, password,
							information);
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
					dismissDialog(WAITTING_DIALOG);
					if (success) {
						Context context = getApplicationContext();
						Toast toast = NotifyUtilities.createMessageToast(
								context,
								resources.getString(R.string.register_success));
						toast.show();
						finish();
					} else {
						
					}
				}
			}).execute();
		}

	}

}
