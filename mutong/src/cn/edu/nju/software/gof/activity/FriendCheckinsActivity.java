package cn.edu.nju.software.gof.activity;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.software.gof.adapter.CheckInPlacesListAdapter;
import cn.edu.nju.software.gof.beans.CheckInInformationBean;
import cn.edu.nju.software.gof.beans.FriendNearbyInformationBean;
import cn.edu.nju.software.gof.requests.CheckInUtilities;
import cn.edu.nju.software.gof.viewbeans.NearbyFriendInfo;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

public class FriendCheckinsActivity extends ListActivity {
	private static final int WAITTING_DIALOG = 0;

	private ListView checkin_list;

	private List<CheckInInformationBean> checkInfo = new ArrayList<CheckInInformationBean>();
	private CheckInPlacesListAdapter fCheck_adapter = null;

	private String friendID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		checkin_list = getListView();

		fCheck_adapter = new CheckInPlacesListAdapter(this, checkInfo);
		checkin_list.setAdapter(fCheck_adapter);

		getFriendCheckIns();
	}

	private void getFriendCheckIns() {
		friendID = getIntent().getStringExtra("friendID");
		checkInfo.clear();

		MyApplication application = (MyApplication) getApplication();
		final String sessionID = (String) application.getData("sessionID");

		showDialog(WAITTING_DIALOG);
		(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				List<CheckInInformationBean> checkin_tmp = CheckInUtilities
						.getFriendCheckInInformation(sessionID, friendID);
				checkInfo.addAll(checkin_tmp);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				fCheck_adapter.notifyDataSetChanged();
				dismissDialog(WAITTING_DIALOG);
			}
		}).execute();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case WAITTING_DIALOG:
			return NotifyUtilities.createProgressDialog(this);
		}
		return null;
	}
}
