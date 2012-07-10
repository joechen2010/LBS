package cn.edu.nju.software.gof.activity;

import cn.edu.nju.software.gof.requests.FriendUtilities;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendManagerActivity extends ListActivity {

	private ImageView avatarView = null;
	private TextView realNameView = null;
	//
	private String friendID = null;
	private Bitmap avatar = null;

	private static final int GET_FRIENDINFO = 0;
	private static final int GET_FRIENDCHECKINS = 1;
	private static final int DELETE_FRIEND = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_manager);
		//
		avatarView = (ImageView) findViewById(R.id.friend_avatar);
		realNameView = (TextView) findViewById(R.id.friend_name);
		//
		Bundle bundle = getIntent().getExtras();
		friendID = bundle.getString("userID");
		avatar = (Bitmap) bundle.getParcelable("avatar");
		if (avatar != null) {
			avatarView.setImageBitmap(avatar);
		}
		realNameView.setText(bundle.getString("realName"));

		setHandler();
	}

	private void setHandler() {
		this.getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				switch (position) {
				case GET_FRIENDINFO:
					getFriendInfo();
					break;
				case GET_FRIENDCHECKINS:
					getFriendCheckins();
					break;
				case DELETE_FRIEND:
					deleteFriend();
					break;
				}
			}
		});
	}
	
	private void getFriendInfo(){
		Intent intent = new Intent(getApplicationContext(),
				FriendInfoActivity.class);
		intent.putExtra("friendID", friendID);
		intent.putExtra("avatar", avatar);
		startActivity(intent);
	}
	
	private void getFriendCheckins(){
		Intent intent = new Intent(getApplicationContext(),
				FriendCheckinsActivity.class);
		intent.putExtra("friendID", friendID);
		startActivity(intent);
	}
	
	private void deleteFriend(){
		MyApplication application = (MyApplication) getApplication();
		final String sessionID = (String) application.getData("sessionID");
		
		(new AsyncTask<Void, Void, Void>() {
			boolean ok = false;
			@Override
			protected Void doInBackground(Void... params) {
				ok = FriendUtilities.deleteFriend(sessionID, friendID);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if(ok){
					Toast toast = NofityUtilities.createMessageToast(
							FriendManagerActivity.this,
							getResources().getString(R.string.delete_friend_ok));
					toast.show();
					setResult(Activity.RESULT_OK);
					finish();
				}else{
					Toast toast = NofityUtilities.createMessageToast(
							FriendManagerActivity.this,
							getResources().getString(R.string.delete_friend_fail));
					toast.show();
				}
			}
		}).execute();
	}
}
