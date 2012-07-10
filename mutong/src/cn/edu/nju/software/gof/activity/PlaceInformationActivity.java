package cn.edu.nju.software.gof.activity;

import android.app.Dialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class PlaceInformationActivity extends TabActivity {
	private final static int WAITTING_DIALOG_ID = 2;

	private int number = 0;

	private String placeID;
	private String from_map;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tab);
		// get place id than get the information details
		getPlaceID();
		updateUI();
	}

	private void getPlaceID() {
		// TODO Auto-generated method stub
		Bundle bundle = getIntent().getExtras();
		placeID = bundle.getString("placeID");
		from_map = bundle.getString("FROM_MAP");
	}
	
	public void updateUI() {

		Bundle bundle = getIntent().getExtras();
		number = bundle.getInt("number");

		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent = null; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this,
				PlaceDetailInformationActivity.class);
		intent.putExtra("placeID", placeID);
		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost
				.newTabSpec("detail_information")
				.setIndicator(res.getString(R.string.place_information_tab),
						res.getDrawable(R.drawable.tab11)).setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, PlaceCommentActivity.class);
		intent.putExtra("placeID", placeID);
		spec = tabHost
				.newTabSpec("comment")
				.setIndicator(res.getString(R.string.place_comment_tab),
						res.getDrawable(R.drawable.tab12)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, PlaceCheckInActivity.class);
		intent.putExtra("placeID", placeID);
		intent.putExtra("FROM_MAP",from_map);
		spec = tabHost
				.newTabSpec("check_in")
				.setIndicator(res.getString(R.string.place_check_in_tab),
						res.getDrawable(R.drawable.tab13)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, PlaceSubplaceActivity.class);
		intent.putExtra("placeID", placeID);
		spec = tabHost
				.newTabSpec("subplace")
				.setIndicator(res.getString(R.string.place_subplace_tab),
						res.getDrawable(R.drawable.tab14)).setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(number);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case WAITTING_DIALOG_ID:
			return NotifyUtilities.createProgressDialog(this);
		}
		return null;
	}

}
