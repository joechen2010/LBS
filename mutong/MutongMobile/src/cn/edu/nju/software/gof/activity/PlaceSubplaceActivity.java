package cn.edu.nju.software.gof.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.edu.nju.software.gof.adapter.PlacesAdapter;
import cn.edu.nju.software.gof.beans.BreifPlaceInformationBean;
import cn.edu.nju.software.gof.beans.CommentBean;
import cn.edu.nju.software.gof.beans.PlaceCreationBean;
import cn.edu.nju.software.gof.beans.PlaceInformationBean;
import cn.edu.nju.software.gof.requests.CachUtilities;
import cn.edu.nju.software.gof.requests.PlaceUtilities;
import cn.edu.nju.software.gof.viewbeans.NearbyPlaceInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class PlaceSubplaceActivity extends ListActivity {
	private ListView list;

	private final static int WAITTING_DIALOG_ID = 2;

	private List<BreifPlaceInformationBean> subPlaces = null;
	private String placeID, sessionid;
	private List<NearbyPlaceInfo> myPlaces = new ArrayList<NearbyPlaceInfo>();
	private PlacesAdapter place_adapter = null;
	Resources resources = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		resources = getResources();

		place_adapter = new PlacesAdapter(this, myPlaces);
		list = getListView();
		this.setListAdapter(place_adapter);
		
		registerEventHandler();
		
		getPlaceID();
		prepareData();
	}

	private void getPlaceID() {
		// TODO Auto-generated method stub
		Bundle bundle = getIntent().getExtras();
		placeID = bundle.getString("placeID");
	}

	private void prepareData() {
		showDialog(WAITTING_DIALOG_ID);
		MyApplication application = (MyApplication) PlaceSubplaceActivity.this
				.getApplication();
		final String sessionID = (String) application.getData("sessionID");
		sessionid = sessionID;
		(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				subPlaces = PlaceUtilities.getSubPlaces(sessionid, placeID);
				myPlaces.clear();
				for (BreifPlaceInformationBean bplace : subPlaces) {
					NearbyPlaceInfo np = new NearbyPlaceInfo(null, bplace, null);
					myPlaces.add(np);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				place_adapter.notifyDataSetChanged();
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

	
	private void registerEventHandler() {
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				goToDetail(arg2);
			}
		});
	}

	private void goToDetail(int id) {
		Bundle bundle = new Bundle();
		bundle.putInt("number", 0);
		Intent intent = new Intent();
		intent.putExtras(bundle);
		intent.putExtra("placeID", subPlaces.get(id).getID());
		intent.setClass(getApplicationContext(), PlaceInformationActivity.class);
		this.startActivity(intent);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_place_subplaces, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.homepage:
			Intent intent = new Intent(getApplicationContext(),
					MainTabActivity.class);
			this.startActivity(intent);
			PlaceSubplaceActivity.this.finish();
		case R.id.refresh_subplaces:
			prepareData();
			return true;
		case R.id.addnewone:
			createNewSubPlace();
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

	private void createNewSubPlace() {
		// TODO Auto-generated method stub
		Intent intent2 = new Intent(getApplicationContext(),
				CreatNewPlaceActivity.class);
		intent2.putExtra("placeID", placeID);
		startActivityForResult(intent2, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			prepareData();
		}

	}
}
