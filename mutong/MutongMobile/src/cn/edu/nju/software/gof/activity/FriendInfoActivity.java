package cn.edu.nju.software.gof.activity;

import java.io.InputStream;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import cn.edu.nju.software.gof.beans.PersonInformationBean;
import cn.edu.nju.software.gof.requests.CachUtilities;
import cn.edu.nju.software.gof.requests.FriendUtilities;
import cn.edu.nju.software.gof.viewbeans.PersonalInfo;

public class FriendInfoActivity extends Activity {

	private final static int WAITTING_DIALOG_ID = 2;

	private ImageView avatarEdit;
	private EditText realNameEdit = null;
	private EditText birthdayEdit = null;
	private EditText schoolEdit = null;
	private EditText placeEdit = null;
	//

	private String friendID = null;
	private Bitmap avatar = null;

	//

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_info);
		//
		retriveViews();
		Bundle bundle = getIntent().getExtras();
		friendID = bundle.getString("friendID");
		avatar = bundle.getParcelable("avatar");
		//
		getPersonalInfo();
	}

	private void getPersonalInfo() {
		showDialog(WAITTING_DIALOG_ID);
		MyApplication application = (MyApplication) FriendInfoActivity.this
				.getApplication();
		final String sessionID = (String) application.getData("sessionID");
		(new AsyncTask<Void, Void, Void>() {

			private PersonalInfo personInfo = null;
			InputStream friendAvatar = null;

			@Override
			protected Void doInBackground(Void... params) {

				//
				PersonInformationBean information = FriendUtilities
						.getFriendInformtion(sessionID, friendID);
				friendAvatar = CachUtilities.getFriendAvatar(sessionID,
						friendID, getExternalCacheDir());
				personInfo = new PersonalInfo(information);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				//
				dismissDialog(WAITTING_DIALOG_ID);
				//
				realNameEdit.setText(personInfo.getRealName());
				String birthday = personInfo.getBirthday();
				birthdayEdit.setText(birthday);
				schoolEdit.setText(personInfo.getSchool());
				placeEdit.setText(personInfo.getPlace());
				if (avatar == null) {
					avatarEdit.setImageBitmap(BitmapFactory
							.decodeStream(friendAvatar));
				}else{
					avatarEdit.setImageBitmap(avatar);
				}
			}

		}).execute();
	}

	private void retriveViews() {
		avatarEdit = (ImageView) findViewById(R.id.avatar_edit);
		birthdayEdit = (EditText) findViewById(R.id.birthday_edit);
		realNameEdit = (EditText) findViewById(R.id.real_name_edit);
		schoolEdit = (EditText) findViewById(R.id.school_edit);
		placeEdit = (EditText) findViewById(R.id.place_edit);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case WAITTING_DIALOG_ID:
			return NotifyUtilities.createProgressDialog(this);
		}
		return null;
	}
}
