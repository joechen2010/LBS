package cn.edu.nju.software.gof.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import cn.edu.nju.software.gof.adapter.FriendSearchAdapter;
import cn.edu.nju.software.gof.beans.BreifInformationBean;
import cn.edu.nju.software.gof.beans.FriendSearchConditionBean;
import cn.edu.nju.software.gof.requests.CachUtilities;
import cn.edu.nju.software.gof.requests.FriendUtilities;
import cn.edu.nju.software.gof.viewbeans.BriefFriendInfo;

public class FriendRecommandActivity extends ListActivity {

	private static final int WAITTING_DIALOG = 0;

	protected List<BriefFriendInfo> friends = new ArrayList<BriefFriendInfo>();

	protected FriendSearchAdapter adapter = null;

	protected ListView listView = null;

	protected Resources resources = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		listView = getListView();
		resources = getResources();
		//
		adapter = new FriendSearchAdapter(this, friends);
		this.setListAdapter(adapter);
		//
		registerForContextMenu(listView);
		//
		getFriends();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_request_friend, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.request_friend:
			int position = info.position;
			sendFriendRequest(position);
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

	private void sendFriendRequest(int position) {
		MyApplication application = (MyApplication) getApplication();
		final String sessionID = (String) application.getData("sessionID");
		BriefFriendInfo friend = friends.get(position);
		final String friendID = friend.getID();
		showDialog(WAITTING_DIALOG);
		(new AsyncTask<Void, Void, Void>() {

			private boolean success = false;

			@Override
			protected Void doInBackground(Void... params) {
				success = FriendUtilities
						.sendFriendRequest(sessionID, friendID);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				dismissDialog(WAITTING_DIALOG);
				if (success) {
					Toast toast = NofityUtilities.createMessageToast(
							FriendRecommandActivity.this,
							resources.getString(R.string.request_sent));
					toast.show();
				}
			}
		}).execute();

	}

	protected void updateAvatars(final String sessionID) {
		(new AsyncTask<Void, InputStream, Void>() {

			private int counter = 0;

			@Override
			protected Void doInBackground(Void... params) {
				for (BriefFriendInfo info : friends) {
					String friendID = info.getID();
					InputStream avatarStream = CachUtilities.getFriendAvatar(
							sessionID, friendID, getExternalCacheDir());
					publishProgress(avatarStream);
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(InputStream... progress) {
				friends.get(counter).setAvatar(progress[0], resources);
				counter++;
				adapter.notifyDataSetChanged();
			}

		}).execute();
	}

	protected void getFriends() {

		showDialog(WAITTING_DIALOG);
		MyApplication application = (MyApplication) getApplication();
		final String sessionID = (String) application.getData("sessionID");

		(new AsyncTask<Void, Void, Void>() {

			private List<BreifInformationBean> results = null;

			@Override
			protected Void doInBackground(Void... params) {
				results = FriendUtilities.getRecommendFriends(sessionID);
				for (BreifInformationBean bean : results) {
					BriefFriendInfo friend = new BriefFriendInfo(bean.getID(),
							bean.getUserName(), bean.getRealName());
					friends.add(friend);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				dismissDialog(WAITTING_DIALOG);
				adapter.notifyDataSetChanged();
				updateAvatars(sessionID);
			}
		}).execute();
	}

}
