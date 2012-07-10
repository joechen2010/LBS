package cn.edu.nju.software.gof.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.edu.nju.software.gof.adapter.FriendExpandableListAdapter;
import cn.edu.nju.software.gof.adapter.FriendListAdaper;
import cn.edu.nju.software.gof.beans.FriendInformationBean;
import cn.edu.nju.software.gof.beans.FriendNearbyInformationBean;
import cn.edu.nju.software.gof.beans.PlaceNearbyInformationBean;
import cn.edu.nju.software.gof.requests.CachUtilities;
import cn.edu.nju.software.gof.requests.FriendUtilities;
import cn.edu.nju.software.gof.viewbeans.FriendInfo;
import cn.edu.nju.software.gof.viewbeans.NearbyFriendInfo;

public class NearbyFriendListActivity extends ListActivity {

	private static final int WAITTING_DIALOG = 0;
	ListView listView = null;
	Resources resources = null;

	private List<FriendNearbyInformationBean> friends = null;
	private List<NearbyFriendInfo> friendWithAvactors = new ArrayList<NearbyFriendInfo>();;
	
	FriendListAdaper adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		listView = getListView();
		resources = getResources();
		
		adapter = new FriendListAdaper(this, friendWithAvactors);
		this.setListAdapter(adapter);
		
		getFriends();

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				String friendID = friendWithAvactors.get(position).getFriendID();
				Bitmap avatar = friendWithAvactors.get(position).getAvatar().getBitmap();
				Intent intent = new Intent(getApplicationContext(),
						FriendInfoActivity.class);
				intent.putExtra("friendID", friendID);
				intent.putExtra("avatar", avatar);
				startActivity(intent);
			}
		});
	}

	private void getFriends() {
		friendWithAvactors.clear();
		friends  = (List<FriendNearbyInformationBean>) getIntent()
		.getSerializableExtra("friends");
		//
		MyApplication application = (MyApplication) getApplication();
		final String sessionID = (String) application.getData("sessionID");
		//
		showDialog(WAITTING_DIALOG);
		(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				for (FriendNearbyInformationBean bean : friends) {
					NearbyFriendInfo friendInfo = new NearbyFriendInfo(bean);
					friendWithAvactors.add(friendInfo);
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

	private void updateAvatars(final String sessionID) {
		(new AsyncTask<Void, InputStream, Void>() {
			private int counter = 0;
			@Override
			protected Void doInBackground(Void... params) {
				for (NearbyFriendInfo info : friendWithAvactors) {
					String friendID = info.getFriendID();
					InputStream avatarStream = CachUtilities.getFriendAvatar(
							sessionID, friendID, getExternalCacheDir());
					publishProgress(avatarStream);
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(InputStream... progress) {
				friendWithAvactors.get(counter).setAvatar(progress[0], resources);
				counter++;
				adapter.notifyDataSetChanged();
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
