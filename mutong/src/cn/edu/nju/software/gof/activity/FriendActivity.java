package cn.edu.nju.software.gof.activity;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;
import cn.edu.nju.software.gof.adapter.FriendExpandableListAdapter;
import cn.edu.nju.software.gof.beans.FriendInformationBean;
import cn.edu.nju.software.gof.beans.FriendRequestBean;
import cn.edu.nju.software.gof.requests.CachUtilities;
import cn.edu.nju.software.gof.requests.FriendUtilities;
import cn.edu.nju.software.gof.viewbeans.FriendInfo;

public class FriendActivity extends ExpandableListActivity {

	private static final int WAITTING_DIALOG = 0;
	//
	private static final int ON_LINE_GROUP = 0;

	private List<FriendInfo> friends = new ArrayList<FriendInfo>();
	private List<FriendRequestBean> requests = new ArrayList<FriendRequestBean>();
	//
	private List<String> groupTitle;
	private List<List<FriendInfo>> childDatas;
	//
	private ExpandableListView listView = null;
	private TextView requestsNofity = null;
	//
	private FriendExpandableListAdapter adapter = null;
	//
	private Resources resources;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_tab);
		//
		prepareDate();
		//
		adapter = new FriendExpandableListAdapter(this, groupTitle, childDatas);
		this.setListAdapter(adapter);
		//
		retriveViews();
		registerEventHandler();
		//
		getFriends();
		getFriendRequests();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_friend_tab, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.add_friends:
			intent = new Intent(getApplicationContext(),
					AddFriendActivity.class);
			startActivity(intent);
			return true;
		case R.id.refresh_friend:
			getFriends();
			return true;
		case R.id.refresh_request:
			getFriendRequests();
			return true;
		case R.id.recommend_friends: {
			intent = new Intent(getApplicationContext(),
					FriendRecommandActivity.class);
			startActivity(intent);
			return true;
		}
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

	private void clearFriendData() {
		friends.clear();
		childDatas.get(ON_LINE_GROUP).clear();
	}

	private void clearRequestData() {
		requests.clear();
	}

	private void prepareDate() {
		groupTitle = Arrays.asList(getResources().getStringArray(
				R.array.friend_list_type));
		childDatas = new LinkedList<List<FriendInfo>>();
		childDatas.add(new LinkedList<FriendInfo>());
	}

	private void retriveViews() {
		requestsNofity = (TextView) findViewById(R.id.friend_request);
		listView = getExpandableListView();
		resources = getResources();
	}

	private void registerEventHandler() {

		listView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Intent intent = new Intent(getApplicationContext(),
						FriendManagerActivity.class);
				FriendInfo info = (FriendInfo) adapter.getChild(groupPosition,
						childPosition);
				intent.putExtra("userID", info.getFriendID());
				intent.putExtra("realName", info.getFriendRealName());
				if (info.getAvatar() != null) {
					intent.putExtra("avatar", info.getAvatar().getBitmap());
				}
				startActivityForResult(intent, 0);
				return true;
			}
		});

		requestsNofity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				requestsNofity.setVisibility(View.GONE);
				Intent intent = new Intent(getApplicationContext(),
						FriendRequestActivity.class);
				intent.putExtra("requests", (Serializable) requests);
				startActivity(intent);
			}
		});
	}

	private void getFriendRequests() {
		clearRequestData();
		//
		MyApplication application = (MyApplication) getApplication();
		final String sessionID = (String) application.getData("sessionID");
		//
		(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				requests = FriendUtilities.getFriendRequests(sessionID);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if (requests.size() != 0) {
					requestsNofity.setText(R.string.new_friend_request);
					requestsNofity.setVisibility(View.VISIBLE);
				}
			}
		}).execute();
	}

	private void getFriends() {
		clearFriendData();
		//
		MyApplication application = (MyApplication) getApplication();
		final String sessionID = (String) application.getData("sessionID");
		//
		showDialog(WAITTING_DIALOG);
		(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				List<FriendInformationBean> beans = FriendUtilities
						.getFriendList(sessionID);
				for (FriendInformationBean bean : beans) {
					FriendInfo friendInfo = new FriendInfo(bean);
					friends.add(friendInfo);
					childDatas.get(ON_LINE_GROUP).add(friendInfo);
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
				for (FriendInfo info : friends) {
					String friendID = info.getFriendID();
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

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case WAITTING_DIALOG:
			return NotifyUtilities.createProgressDialog(this);
		}
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			getFriends();
		}

	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && 
				event.getRepeatCount() == 0) {
			Dialog dialog = new AlertDialog.Builder(FriendActivity.this)
			.setTitle("确定要退出吗？")
			.setPositiveButton("确定", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							FriendActivity.this.finish();
						}
					})
					.setNegativeButton("取消", 
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
								}
							}).create();
			dialog.show();
		}
    	return false;
    }
}
