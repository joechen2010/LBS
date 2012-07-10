package cn.edu.nju.software.gof.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.edu.nju.software.gof.adapter.CommentsAdapter;
import cn.edu.nju.software.gof.beans.CommentBean;
import cn.edu.nju.software.gof.beans.FriendRequestBean;
import cn.edu.nju.software.gof.beans.PlaceCreationBean;
import cn.edu.nju.software.gof.requests.CachUtilities;
import cn.edu.nju.software.gof.requests.InformationUtilities;
import cn.edu.nju.software.gof.requests.PlaceUtilities;
import cn.edu.nju.software.gof.viewbeans.CommentWithAvatarBean;
import cn.edu.nju.software.gof.viewbeans.NearbyFriendInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class PlaceCommentActivity extends Activity {
	private final static int WAITTING_DIALOG_ID = 2;

	private ListView list;
	Resources resources = null;
	private CommentsAdapter adapter_comments;

	private List<CommentBean> replies = new ArrayList<CommentBean>();
	private List<CommentWithAvatarBean> commentwithAvatar = new ArrayList<CommentWithAvatarBean>();
	private String placeID, sessionid;
	private String commentContent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.placecomment_list);
		resources = getResources();
		getPlaceID();

		adapter_comments = new CommentsAdapter(this, commentwithAvatar);
		list.setAdapter(adapter_comments);

		prepareData();
	}

	private void getPlaceID() {
		list = (ListView) findViewById(R.id.MyListView);
		Bundle bundle = getIntent().getExtras();
		placeID = bundle.getString("placeID");
	}

	private void prepareData() {
		showDialog(WAITTING_DIALOG_ID);
		MyApplication application = (MyApplication) PlaceCommentActivity.this
				.getApplication();
		final String sessionID = (String) application.getData("sessionID");
		sessionid = sessionID;
		(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				replies = PlaceUtilities.getComments(sessionID, placeID);
				commentwithAvatar.clear();
				for (CommentBean bean : replies) {
					CommentWithAvatarBean cwab = new CommentWithAvatarBean(bean);
					commentwithAvatar.add(cwab);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				adapter_comments.notifyDataSetChanged();
				updateAvatars(sessionID);
				dismissDialog(WAITTING_DIALOG_ID);
			}
		}).execute();
	}

	private void updateAvatars(final String sessionID) {
		(new AsyncTask<Void, InputStream, Void>() {
			private int counter = 0;

			@Override
			protected Void doInBackground(Void... params) {
				for (CommentWithAvatarBean info : commentwithAvatar) {
					String friendID = info.getOwnerID();
					InputStream avatarStream = CachUtilities.getFriendAvatar(
							sessionID, friendID, getExternalCacheDir());
					publishProgress(avatarStream);
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(InputStream... progress) {
				commentwithAvatar.get(counter)
						.setAvatar(progress[0], resources);
				counter++;
				adapter_comments.notifyDataSetChanged();
			}
		}).execute();
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
		inflater.inflate(R.menu.menu_place_comment, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.homepage:
			Intent intent = new Intent(getApplicationContext(),
					MainTabActivity.class);
			this.startActivity(intent);
			PlaceCommentActivity.this.finish();
		case R.id.addnewone:
			createNewComment();
			return true;
		case R.id.refresh_comments:
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

	private void createNewComment() {
		// TODO Auto-generated method stub
		LayoutInflater factory = LayoutInflater.from(this);
		final View dialogView = factory.inflate(R.layout.create_comment, null);
		AlertDialog dlg = new AlertDialog.Builder(this)
				.setTitle(R.string.create_title)
				.setView(dialogView)
				.setPositiveButton(R.string.create_ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								showDialog(WAITTING_DIALOG_ID);
								EditText newplacename = (EditText) dialogView
										.findViewById(R.id.create_comment);
								commentContent = newplacename.getText()
										.toString();
								(new AsyncTask<Void, Void, Void>() {
									boolean creatCommentOk = false;
									InputStream avatarStream;
									@Override
									protected Void doInBackground(
											Void... params) {
										creatCommentOk = PlaceUtilities
												.commentPlace(sessionid,
														placeID, commentContent);
										avatarStream = CachUtilities
												.getPersonalAvater(sessionid,
														getExternalCacheDir());
										return null;
									}

									@Override
									protected void onPostExecute(Void result) {
										super.onPostExecute(result);
										dismissDialog(WAITTING_DIALOG_ID);
										if (creatCommentOk) {
											CommentWithAvatarBean bean = new CommentWithAvatarBean(new CommentBean());
											bean.setContent(commentContent);
											bean.setOwnerName("me");
											bean.setTime("justnow");
											bean.setAvatar(avatarStream, resources);
											commentwithAvatar.add(bean);
											adapter_comments
													.notifyDataSetChanged();
										} else {
											Toast toast = NofityUtilities
													.createMessageToast(
															PlaceCommentActivity.this,
															getResources()
																	.getString(
																			R.string.commentfake));
											toast.show();
										}

									}
								}).execute();
							}
						})
				.setNegativeButton(R.string.create_cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						}).create();
		dlg.show();
	}
}
