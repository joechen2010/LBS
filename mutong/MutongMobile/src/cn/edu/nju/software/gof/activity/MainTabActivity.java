package cn.edu.nju.software.gof.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TabHost;

public class MainTabActivity extends TabActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tab);

		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent = null; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, GoogleMapActivity.class);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost
				.newTabSpec("map")
				.setIndicator(res.getString(R.string.map_tab),
						res.getDrawable(R.drawable.tab01)).setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, FriendActivity.class);
		spec = tabHost
				.newTabSpec("friend")
				.setIndicator(res.getString(R.string.friend_tab),
						res.getDrawable(R.drawable.tab02)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, PlaceActivity.class);
		spec = tabHost
				.newTabSpec("place")
				.setIndicator(res.getString(R.string.place_tab),
						res.getDrawable(R.drawable.tab03)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, CheckInActivity.class);
		spec = tabHost
				.newTabSpec("check_in")
				.setIndicator(res.getString(R.string.check_in_tab),
						res.getDrawable(R.drawable.tab04)).setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
	}
	


}
