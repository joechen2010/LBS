package cn.edu.nju.software.gof.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;
import cn.edu.nju.software.gof.adapter.FriendRequestAdapter;
import cn.edu.nju.software.gof.beans.FriendRequestBean;
import cn.edu.nju.software.gof.requests.CachUtilities;
import cn.edu.nju.software.gof.requests.FriendUtilities;
import cn.edu.nju.software.gof.viewbeans.FriendRequestInfo;

public class FriendRequestActivity extends ListActivity {

	private static final int WAITTING_DIALOG = 0;

	private List<FriendRequestInfo> requests = new ArrayList<FriendRequestInfo>();

	private FriendRequestAdapter adapter = null;
	private ListView listView = null;

	private Resources resources = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		resources = getResources();
		listView = getListView();
		//
		adapter = new FriendRequestAdapter(this, requests);
		setListAdapter(adapter);
		//
		registerForContextMenu(listView);
		//
		prepareData();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_agree_reject_friend, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.agree:
			int position = info.position;
			agreeRequest(position);
			return true;
		case R.id.reject:
			position = info.position;
			rejectRequest(position);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case WAITTING_DIALOG:
			return NotifyUtilities.createProgressDialog(this);
		}
		return null;
	}

	private void rejectRequest(final int position) {
		MyApplication application = (MyApplication) getApplication();
		final String sessionID = (String) application.getData("sessionID");
		FriendRequestInfo request = requests.get(position);
		final String requestID = request.getRequestID();
		showDialog(WAITTING_DIALOG);
		(new AsyncTask<Void, Void, Void>() {

			private boolean success = false;

			@Override
			protected Void doInBackground(Void... params) {
				success = FriendUtilities.rejectFriendRequest(sessionID,
						requestID);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				dismissDialog(WAITTING_DIALOG);
				if (success) {
					Toast toast = NofityUtilities
							.createMessageToast(
									FriendRequestActivity.this,
									resources
											.getString(R.string.reject_friend_request));
					toast.show();
					requests.remove(position);
					adapter.notifyDataSetChanged();
				} else {

				}
			}
		}).execute();

	}

	private void agreeRequest(final int position) {
		MyApplication application = (MyApplication) getApplication();
		final String sessionID = (String) application.getData("sessionID");
		FriendRequestInfo request = requests.get(position);
		final String requestID = request.getRequestID();
		showDialog(WAITTING_DIALOG);
		(new AsyncTask<Void, Void, Void>() {

			private boolean success = false;

			@Override
			protected Void doInBackground(Void... params) {
				success = FriendUtilities.agreeFriendRequest(sessionID,
						requestID);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				dismissDialog(WAITTING_DIALOG);
				if (success) {
					Toast toast = NofityUtilities.createMessageToast(
							FriendRequestActivity.this,
							resources.getString(R.string.agree_friend_request));
					toast.show();
					requests.remove(position);
					adapter.notifyDataSetChanged();
				} else {

				}
			}
		}).execute();

	}

	private void prepareData() {
		@SuppressWarnings("unchecked")
		List<FriendRequestBean> beans = (List<FriendRequestBean>) getIntent()
				.getExtras().getSerializable("requests");
		for (FriendRequestBean bean : beans) {
			requests.add(new FriendRequestInfo(bean));
		}
		adapter.notifyDataSetChanged();
		getAvatars();
	}

	private void getAvatars() {
		MyApplication application = (MyApplication) getApplication();
		final String sessionID = (String) application.getData("sessionID");
		(new AsyncTask<Void, InputStream, Void>() {

			private int counter = 0;

			@Override
			protected Void doInBackground(Void... params) {
				for (FriendRequestInfo info : requests) {
					String friendID = info.getFriendID();
					InputStream avatarStream = CachUtilities.getFriendAvatar(
							sessionID, friendID, getExternalCacheDir());
					publishProgress(avatarStream);
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(InputStream... progress) {
				requests.get(counter).setAvatar(progress[0], resources);
				counter++;
				adapter.notifyDataSetChanged();
			}
		}).execute();

	}
}
