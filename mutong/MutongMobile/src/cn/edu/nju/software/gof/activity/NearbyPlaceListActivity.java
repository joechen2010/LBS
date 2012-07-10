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
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.edu.nju.software.gof.adapter.PlacesAdapter;
import cn.edu.nju.software.gof.beans.PlaceNearbyInformationBean;
import cn.edu.nju.software.gof.requests.CachUtilities;
import cn.edu.nju.software.gof.viewbeans.NearbyPlaceInfo;

public class NearbyPlaceListActivity extends ListActivity {
	private static final int WAITTING_DIALOG = 0;
	private static final String FROM_MAP = "TRUE";
	
	private ListView listView = null;
	private Resources resources = null;
	private List<PlaceNearbyInformationBean> places = null;
	
	private List<NearbyPlaceInfo> myPlaces = new ArrayList<NearbyPlaceInfo>();
	
	private PlacesAdapter adapter = null;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//
		listView = getListView();
		resources = getResources();
		
		
		adapter = new PlacesAdapter(this, myPlaces);
		this.setListAdapter(adapter);
		
		getPlaces();
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				String placeID = places.get(position).getPlaceID();
				Intent intent = new Intent(getApplicationContext(),
						PlaceInformationActivity.class);
				intent.putExtra("placeID", placeID);
				intent.putExtra("FROM_MAP", FROM_MAP);
				startActivity(intent);
			}
		});
	}
	
	private void getPlaces() {
		myPlaces.clear();
		places = (List<PlaceNearbyInformationBean>) getIntent()
		.getSerializableExtra("places");
		//
		MyApplication application = (MyApplication) getApplication();
		final String sessionID = (String) application.getData("sessionID");
		//
		showDialog(WAITTING_DIALOG);
		(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				for (PlaceNearbyInformationBean bean : places) {
					NearbyPlaceInfo np = new NearbyPlaceInfo(bean, null, null);
					myPlaces.add(np);
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
