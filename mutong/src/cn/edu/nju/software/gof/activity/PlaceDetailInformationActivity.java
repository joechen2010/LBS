package cn.edu.nju.software.gof.activity;

import java.io.InputStream;

import cn.edu.nju.software.gof.beans.PlaceGeneral;
import cn.edu.nju.software.gof.requests.CachUtilities;
import cn.edu.nju.software.gof.requests.PlaceUtilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceDetailInformationActivity extends Activity {
	private final static int WAITTING_DIALOG_ID = 2;

	private TextView placeName, placeOwnerName, placeMoney, plaseDescription;
	ImageView placePhoto;
	private Button buyButton;
	private Button changeInfoButton;

	private String placeID, sessionid;
	private InputStream place_photo;
	private PlaceGeneral placeGeneral;
	private BitmapDrawable photoPlace;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.place_detailinfo);

		getPlaceID();
		getWidgetsAndRegistHandlers();

		getPlaceInfo();
	}

	private void getPlaceID() {
		// TODO Auto-generated method stub
		Bundle bundle = getIntent().getExtras();
		placeID = bundle.getString("placeID");
	}

	private void getWidgetsAndRegistHandlers() {
		placePhoto = (ImageView) findViewById(R.id.place_photo);
		placeName = (TextView) findViewById(R.id.place_name);
		buyButton = (Button) findViewById(R.id.buyIt);
		changeInfoButton = (Button) findViewById(R.id.changeIt);
		placeOwnerName = (TextView) findViewById(R.id.placeOwnerName);
		placeMoney = (TextView) findViewById(R.id.placeMoney);
		plaseDescription = (TextView) findViewById(R.id.placeDescription);

		buyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(WAITTING_DIALOG_ID);
				(new AsyncTask<Void, Void, Void>() {
					boolean buyok = false;

					@Override
					protected Void doInBackground(Void... params) {
						buyok = PlaceUtilities.buyPlace(sessionid, placeID);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						dismissDialog(WAITTING_DIALOG_ID);
						if (buyok) {
							Toast toast = NofityUtilities.createMessageToast(
									PlaceDetailInformationActivity.this,
									getResources().getString(R.string.buyok));
							toast.show();
							buyButton.setEnabled(false);
						} else {
							Toast toast = NofityUtilities.createMessageToast(
									PlaceDetailInformationActivity.this,
									getResources().getString(R.string.buyfake));
							toast.show();
						}
					}
				}).execute();
			}
		});

		changeInfoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2 = new Intent(getApplicationContext(),
						ChangePlaceInfoActivity.class);
				intent2.putExtra("placeID", placeID);
				intent2.putExtra("placeComment", placeGeneral.getPlaceDescription());
				intent2.putExtra("placeName", placeGeneral.getPlaceName());
				intent2.putExtra("avatar", photoPlace.getBitmap());
				startActivityForResult(intent2, 0);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			getPlaceInfo();
		}

	}

	private void getPlaceInfo() {
		// TODO Auto-generated method stub
		showDialog(WAITTING_DIALOG_ID);
		MyApplication application = (MyApplication) PlaceDetailInformationActivity.this
				.getApplication();
		final String sessionID = (String) application.getData("sessionID");
		final String userID = (String) application.getData("userID");
		sessionid = sessionID;
		(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				placeGeneral = PlaceUtilities.getPlaceGenetal(sessionID,
						placeID);
				place_photo = CachUtilities.getPlaceAvater(sessionID, placeID,
						getExternalCacheDir());
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				refreshContent();
				String owenrID = placeGeneral.getOwnerID();
				if (userID.equals(owenrID)) {
					buyButton.setEnabled(false);
					changeInfoButton.setEnabled(true);
				} else {
					buyButton.setEnabled(true);
					changeInfoButton.setEnabled(false);
				}
				dismissDialog(WAITTING_DIALOG_ID);
			}
		}).execute();
	}

	private void refreshContent() {
		// TODO Auto-generated method stub
		if (place_photo != null) {
			photoPlace = new BitmapDrawable(getResources(),place_photo);
			placePhoto.setImageDrawable(photoPlace);
		}
		placeName.setText(placeGeneral.getPlaceName());
		placeOwnerName.setText(placeGeneral.getOwnerName());
		placeMoney.setText(Long.toString(placeGeneral.getCurrentMoney()));
		plaseDescription.setText(placeGeneral.getPlaceDescription());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_place_information, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.homepage:
			Intent intent = new Intent(getApplicationContext(),
					MainTabActivity.class);
			this.startActivity(intent);
			PlaceDetailInformationActivity.this.finish();
			return true;
		case R.id.refresh_placeinfo:
			getPlaceInfo();
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
}
