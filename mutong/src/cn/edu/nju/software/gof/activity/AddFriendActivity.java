package cn.edu.nju.software.gof.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddFriendActivity extends Activity {

	private static final int AT_LEAST_ONE_INFO = 0;
	private static final int TEST = 1;
	private EditText userNameEdit = null;
	private EditText realNameEdit = null;
	private EditText schoolEdit = null;
	private EditText placeEdit = null;
	private Button searchButton = null;
	//
	private Resources resources;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_friend);
		//
		retriveViews();
		registerEventHandler();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case AT_LEAST_ONE_INFO:
			return NofityUtilities.createMessageDialog(this,
					resources.getString(R.string.at_least_one_info));
		}
		return null;
	}

	private void registerEventHandler() {
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				searchFriend();
			}
		});

	}

	private void retriveViews() {
		userNameEdit = (EditText) findViewById(R.id.user_name_edit);
		realNameEdit = (EditText) findViewById(R.id.real_name_edit);
		schoolEdit = (EditText) findViewById(R.id.school_edit);
		placeEdit = (EditText) findViewById(R.id.place_edit);
		searchButton = (Button) findViewById(R.id.search);
		//
		resources = getResources();
	}

	private void searchFriend() {
		String userName = userNameEdit.getText().toString();
		String realName = realNameEdit.getText().toString();
		String school = schoolEdit.getText().toString();
		String place = placeEdit.getText().toString();
		if (userName.equals("") && realName.equals("") && school.equals("")
				&& place.equals("")) {
			showDialog(AT_LEAST_ONE_INFO);
		} else {
			Intent intent = new Intent(getApplicationContext(),
					FriendSearchResultActivity.class);
			Bundle extras = new Bundle();
			extras.putString("userName", userName);
			extras.putString("realName", realName);
			extras.putString("place", place);
			extras.putString("school", school);
			intent.putExtras(extras);
			startActivity(intent);
		}
	}
}
