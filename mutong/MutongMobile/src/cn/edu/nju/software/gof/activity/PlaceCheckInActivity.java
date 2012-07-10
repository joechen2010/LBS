package cn.edu.nju.software.gof.activity;

import java.util.List;

import cn.edu.nju.software.gof.beans.CommentBean;
import cn.edu.nju.software.gof.beans.PersonInformationBean;
import cn.edu.nju.software.gof.beans.PlaceCheckInfomationBean;
import cn.edu.nju.software.gof.requests.CheckInUtilities;
import cn.edu.nju.software.gof.requests.FriendUtilities;
import cn.edu.nju.software.gof.requests.InformationUtilities;
import cn.edu.nju.software.gof.requests.PlaceUtilities;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceCheckInActivity extends Activity {

	private final static int WAITTING_DIALOG_ID = 2;
	MyApplication application;

	private TextView topuser_name, my_times;
	private Button checkin, addasfriend;

	private String placeID, sessionID, userID, from_map;
	private int myCheckinTimes;
	private PlaceCheckInfomationBean placeCheckinInfo = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.place_checkin);

		application = (MyApplication) PlaceCheckInActivity.this
				.getApplication();
		sessionID = (String) application.getData("sessionID");
		userID = (String) application.getData("userID");

		getAllDatas();
		// get widgets
		getWidgets();
		prepareData();
	}

	private void getAllDatas() {
		// TODO Auto-generated method stub
		Bundle bundle = getIntent().getExtras();
		placeID = bundle.getString("placeID");
		from_map = bundle.getString("FROM_MAP");
	}

	private void prepareData() {
		showDialog(WAITTING_DIALOG_ID);
		(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				placeCheckinInfo = PlaceUtilities.getPlaceCheckInfomationBean(
						sessionID, placeID);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				// set Texts
				setTextViewContent();
				if (placeCheckinInfo.getTopUserID().equals(userID)
						|| placeCheckinInfo.getTopUserID().equals("")) {
					addasfriend.setEnabled(false);
				} else {
					addasfriend.setEnabled(true);
				}
				dismissDialog(WAITTING_DIALOG_ID);
			}
		}).execute();
	}

	public void getWidgets() {
		checkin = (Button) findViewById(R.id.placecheckin_button);
		topuser_name = (TextView) findViewById(R.id.topuser_name);
		addasfriend = (Button) findViewById(R.id.add_as_friend);
		my_times = (TextView) findViewById(R.id.my_times);

		checkin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(WAITTING_DIALOG_ID);
				(new AsyncTask<Void, Void, Void>() {
					boolean could_not_checkin = false;
					boolean has_checkedin = false;
					boolean checkin_ok = false;

					@Override
					protected Void doInBackground(Void... params) {
						could_not_checkin = canNotCheckIn();
						has_checkedin = checkIfHasCheckedin();
						if (!could_not_checkin && !has_checkedin) {
							checkin_ok = CheckInUtilities.checkInPlace(
									sessionID, placeID);
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						if (checkin_ok) {
							myCheckinTimes++;
							application.addCheckedPlace(placeID);
							my_times.setText(Integer.toString(myCheckinTimes));
							prepareData();
						} else if (could_not_checkin) {
							Toast toast = NofityUtilities.createMessageToast(
									PlaceCheckInActivity.this, getResources()
											.getString(R.string.cannot_checkin));
							toast.show();
						} else {
							Toast toast = NofityUtilities.createMessageToast(
									PlaceCheckInActivity.this, getResources()
											.getString(R.string.has_checkedin));
							toast.show();
						}
						dismissDialog(WAITTING_DIALOG_ID);
					}
				}).execute();
			}
		});

		addasfriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(WAITTING_DIALOG_ID);
				(new AsyncTask<Void, Void, Void>() {
					boolean addOK = false;

					@Override
					protected Void doInBackground(Void... params) {
						addOK = FriendUtilities.sendFriendRequest(sessionID,
								placeCheckinInfo.getTopUserID());
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						if (addOK) {
							Toast toast = NofityUtilities.createMessageToast(
									PlaceCheckInActivity.this, getResources()
											.getString(R.string.request_sent));
							toast.show();
						} else {
							Toast toast = NofityUtilities.createMessageToast(
									PlaceCheckInActivity.this, getResources()
											.getString(R.string.request_fail));
							toast.show();
						}
						dismissDialog(WAITTING_DIALOG_ID);
					}
				}).execute();
			}
		});
	}

	private boolean canNotCheckIn() {
		if (from_map.equals("TRUE")) {
			return false;
		} else {
			return true;
		}
	}

	private boolean checkIfHasCheckedin() {
		if (application.hasChecked(placeID)) {
			return true;
		} else {
			return false;
		}
	}

	public void setTextViewContent() {
		myCheckinTimes = placeCheckinInfo.getMyCheckinTimes();
		my_times.setText(Integer.toString(myCheckinTimes));
		topuser_name.setText(placeCheckinInfo.getTopUserName());
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case WAITTING_DIALOG_ID:
			return NotifyUtilities.createProgressDialog(this);
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_place_checkin, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.homepage:
			Intent intent = new Intent(getApplicationContext(),
					MainTabActivity.class);
			this.startActivity(intent);
			PlaceCheckInActivity.this.finish();
			return true;
		case R.id.refresh_topcheckin:
			prepareData();
			return true;
		case R.id.logout: {
			Intent intent5 = new Intent(getApplicationContext(),
					LoginActivity.class);
			startActivity(intent5);
			finish();
			return true;
		}
		case R.id.exit_program:
			finish();
		}
		return false;
	}
}
