package cn.edu.nju.software.gof.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.software.gof.adapter.PlacesAdapter;
import cn.edu.nju.software.gof.beans.BreifPlaceInformationBean;
import cn.edu.nju.software.gof.beans.RichManInfo;
import cn.edu.nju.software.gof.requests.AccountUtilities;
import cn.edu.nju.software.gof.requests.CachUtilities;
import cn.edu.nju.software.gof.viewbeans.NearbyPlaceInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class PlaceActivity extends Activity {

	private final static int WAITTING_DIALOG_ID = 2;

	private ListView list;
	private long myMoney = 0;
	private RichManInfo richMan = null;
	private List<NearbyPlaceInfo> myPlaces = new ArrayList<NearbyPlaceInfo>();

	private PlacesAdapter place_adapter = null;
	Resources resources = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.place_list_main);
		resources = getResources();

		place_adapter = new PlacesAdapter(this, myPlaces);
		list = (ListView) findViewById(R.id.MyListView);
		list.setAdapter(place_adapter);
		
		registerEventHandler(list);

		prepareData();
	}

	private void prepareData() {
		showDialog(WAITTING_DIALOG_ID);
		MyApplication application = (MyApplication) PlaceActivity.this
				.getApplication();
		final String sessionID = (String) application.getData("sessionID");
		(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				richMan = AccountUtilities.getRichmaninfo(sessionID);
				myMoney = richMan.getMoney();
				List<BreifPlaceInformationBean> myPlacestmp = richMan
						.getPlaces();
				myPlaces.clear();
				for (BreifPlaceInformationBean bplace : myPlacestmp) {
					NearbyPlaceInfo np = new NearbyPlaceInfo(null, bplace, null);
					myPlaces.add(np);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				refresh();
				updateAvatars(sessionID);
				dismissDialog(WAITTING_DIALOG_ID);
			}
		}).execute();
	}

	private void refresh() {
		// TODO Auto-generated method stub
		TextView money = (TextView) findViewById(R.id.myMoney);
		money.setText(Long.toString(myMoney));
		place_adapter.notifyDataSetChanged();
	}

	private void updateAvatars(final String sessionID) {
		(new AsyncTask<Void, InputStream, Void>() {
			private int counter = 0;

			@Override
			protected Void doInBackground(Void... params) {
				for (NearbyPlaceInfo info : myPlaces) {
					String placeID = info.getPlaceID();
					InputStream avatarStream = CachUtilities.getPlaceAvater(
							sessionID, placeID, getExternalCacheDir());
					publishProgress(avatarStream);
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(InputStream... progress) {
				myPlaces.get(counter).setAvatar(progress[0], resources);
				counter++;
				place_adapter.notifyDataSetChanged();
			}
		}).execute();
	}

	private void registerEventHandler(ListView lv) {
		// TODO Auto-generated method stub
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				int itemId = arg2 + 1;
				goToDetail(itemId);
			}
		});
	}

	private void goToDetail(int id) {
		Intent intent = new Intent(getApplicationContext(),
				PlaceInformationActivity.class);
		intent.putExtra("placeID", myPlaces.get(id - 1).getPlaceID());
		intent.putExtra("FROM_MAP", "FALSE");
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_place_tab, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh_my_places:
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

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case WAITTING_DIALOG_ID:
			return NotifyUtilities.createProgressDialog(this);
		}
		return null;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && 
				event.getRepeatCount() == 0) {
			Dialog dialog = new AlertDialog.Builder(PlaceActivity.this)
			.setTitle("确定要退出吗？")
			.setPositiveButton("确定", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							PlaceActivity.this.finish();
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
