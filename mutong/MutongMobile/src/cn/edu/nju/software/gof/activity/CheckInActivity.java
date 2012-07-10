package cn.edu.nju.software.gof.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.software.gof.adapter.CheckInPlacesListAdapter;
import cn.edu.nju.software.gof.beans.CheckInInformationBean;
import cn.edu.nju.software.gof.beans.PersonInformationBean;
import cn.edu.nju.software.gof.requests.CachUtilities;
import cn.edu.nju.software.gof.requests.CheckInUtilities;
import cn.edu.nju.software.gof.requests.InformationUtilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class CheckInActivity extends Activity {

	private final static int WAITTING_DIALOG = 4;
	private TabHost tabs;
	private List<CheckInInformationBean> checkins = new ArrayList<CheckInInformationBean>();
	private List<CheckInInformationBean> honors = new ArrayList<CheckInInformationBean>();

	private CheckInPlacesListAdapter adapter_checkin, adapter_honors;
	private ListView listview_checkins, listview_honors;
	private ImageView myheadimage;
	private TextView myname;

	private PersonInformationBean personalInfo;
	private InputStream head_photo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkin);

		// initial the tab and the list view
		initialTabs();
		initialLists();
		// initial the list adapter
		adapter_checkin = new CheckInPlacesListAdapter(this, checkins);
		listview_checkins.setAdapter(adapter_checkin);
		adapter_honors = new CheckInPlacesListAdapter(this, honors);
		listview_honors.setAdapter(adapter_honors);
		//
		listRequestHandler();
		refresh();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case WAITTING_DIALOG:
			return NotifyUtilities.createProgressDialog(this);
		}

		return null;
	}

	private void listRequestHandler() {
		// TODO Auto-generated method stub
		listview_checkins.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				// TODO Auto-generated method stub
				String placeID = checkins.get(index).getPlaceID();
				Intent intent = new Intent(getApplicationContext(),
						PlaceInformationActivity.class);
				intent.putExtra("placeID", placeID);
				intent.putExtra("FROM_MAP", "FALSE");
				startActivity(intent);
			}
		});
		listview_honors.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
				// TODO Auto-generated method stub
				String placeID = honors.get(index).getPlaceID();
				Intent intent = new Intent(getApplicationContext(),
						PlaceInformationActivity.class);
				intent.putExtra("placeID", placeID);
				intent.putExtra("FROM_MAP", "FALSE");
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_checkin_tab, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refresh_my_checkins:
			refresh();
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

	public void initialTabs() {
		tabs = (TabHost) findViewById(R.id.tabhost);
		tabs.setup();
		TabHost.TabSpec spec = tabs.newTabSpec("tag1");
		spec.setContent(R.id.tab1);
		spec.setIndicator(getResources().getText(R.string.my_checkins),
				getResources().getDrawable(R.drawable.tab041));
		tabs.addTab(spec);
		spec = tabs.newTabSpec("tag2");
		spec.setContent(R.id.tab2);
		spec.setIndicator(getResources().getText(R.string.my_honors),
				getResources().getDrawable(R.drawable.tab042));
		tabs.addTab(spec);
		TabWidget tabWidget = tabs.getTabWidget();
		int count = tabWidget.getChildCount();
		for (int i = 0; i < count; i++) {
			View view = tabWidget.getChildTabViewAt(i);
			view.getLayoutParams().height = 57;

		}
	}

	public void initialLists() {
		listview_checkins = (ListView) findViewById(R.id.my_checkins);
		listview_honors = (ListView) findViewById(R.id.my_honors);
		myheadimage = (ImageView) findViewById(R.id.my_head_photo);
		myname = (TextView) findViewById(R.id.my_name);
	}

	public void refresh() {
		showDialog(WAITTING_DIALOG);
		MyApplication application = (MyApplication) getApplication();
		final String sessionID = (String) application.getData("sessionID");
		//
		(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				personalInfo = InformationUtilities
						.getUserInformation(sessionID);
				head_photo = CachUtilities.getPersonalAvater(sessionID,
						getExternalCacheDir());
				List<CheckInInformationBean> bean_checkins = CheckInUtilities
						.getCheckInInformation(sessionID);
				checkins.clear();
				checkins.addAll(bean_checkins);
				List<CheckInInformationBean> bean_honors = CheckInUtilities
						.getMyTopCheckIns(sessionID);
				honors.clear();
				honors.addAll(bean_honors);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if (head_photo != null) {
					myheadimage.setImageDrawable(new BitmapDrawable(
							getResources(), head_photo));
				}
				myname.setText(personalInfo.getRealName());
				if (checkins.size() != 0) {
					adapter_checkin.notifyDataSetChanged();
				}
				if (honors.size() != 0) {
					adapter_honors.notifyDataSetChanged();
				}
				dismissDialog(WAITTING_DIALOG);
			}
		}).execute();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && 
				event.getRepeatCount() == 0) {
			Dialog dialog = new AlertDialog.Builder(CheckInActivity.this)
			.setTitle("确定要退出吗？")
			.setPositiveButton("确定", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							CheckInActivity.this.finish();
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
