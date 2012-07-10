package cn.edu.nju.software.gof.activity;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.nju.software.gof.beans.FriendNearbyInformationBean;
import cn.edu.nju.software.gof.beans.PlaceNearbyInformationBean;
import cn.edu.nju.software.gof.map.DynamicMyLocationOverlay;
import cn.edu.nju.software.gof.map.NearbyFriendOverlayItem;
import cn.edu.nju.software.gof.map.NearbyPlaceOverlayItem;
import cn.edu.nju.software.gof.requests.CachUtilities;
import cn.edu.nju.software.gof.requests.CheckInUtilities;
import cn.edu.nju.software.gof.requests.FriendUtilities;
import cn.edu.nju.software.gof.requests.PlaceUtilities;
import cn.edu.nju.software.gof.utilities.ImageUtilities;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

import de.android1.overlaymanager.ManagedOverlay;
import de.android1.overlaymanager.ManagedOverlayGestureDetector.OnOverlayGestureListener;
import de.android1.overlaymanager.ManagedOverlayItem;
import de.android1.overlaymanager.OverlayManager;
import de.android1.overlaymanager.ZoomEvent;

public class GoogleMapActivity extends MapActivity {

	private static final String MAIL_ADDRESS = "gofnju@gmail.com";
	private static final int WAITTING_DIALOG = 0;
	private static final double RANGE = 0.0006;
	private static final String FROM_MAP = "TRUE";

	private LocationProvider locationProvider;
	private LocationManager locationManager;

	private MapView map = null;
	private MapController controller = null;
	//
	private DynamicMyLocationOverlay locationOverlay = null;
	//
	private OverlayManager overlayManager = null;
	private ManagedOverlay friendManagedOverlay = null;
	private ManagedOverlay placeManagedOverlay = null;
	//
	private Resources resources = null;
	private Drawable defaultMarker = null;

	private List<PlaceNearbyInformationBean> placeInfos = null;
	private List<FriendNearbyInformationBean> friendInfos = null;

	Button checkin_button;
	MapView.LayoutParams screenLP;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		resources = getResources();
		defaultMarker = resources.getDrawable(R.drawable.place);
		//
		prepareLocationProvider();
		prepareMap();
		refreshMap();
	}

	@Override
	public void onResume() {
		super.onResume();
		locationOverlay.enableCompass();
		locationOverlay.enableMyLocation();
	}

	@Override
	public void onPause() {
		super.onPause();
		locationOverlay.disableCompass();
		locationOverlay.disableMyLocation();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_map_tab, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.setting:
			intent = new Intent(getApplicationContext(), SettingActivity.class);
			startActivity(intent);
			return true;
		case R.id.view_friends:
			viewFriends();
			return true;
		case R.id.update_location:
			updateLocation();
			return true;
		case R.id.refresh_map:
			refreshMap();
			return true;
		case R.id.create_place: {
			creat_new_place();
			return true;
		}
		case R.id.view_places:
			viewPlaces();
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
		case WAITTING_DIALOG:
			return NotifyUtilities.createProgressDialog(this);
		}
		return null;
	}

	private void creat_new_place() {
		Location currentLocation = locationManager
				.getLastKnownLocation(locationProvider.getName());
		if (currentLocation != null) {
			final double latitude = currentLocation.getLatitude();
			final double longitude = currentLocation.getLongitude();

			if (checkIfCanCreate(latitude, longitude)) {
				Intent intent2 = new Intent(getApplicationContext(),
						CreatNewPlaceActivity.class);
				intent2.putExtra("latitude", latitude);
				intent2.putExtra("longitude", longitude);
				startActivityForResult(intent2, 0);
			} else {
				Toast toast = NofityUtilities.createMessageToast(
						GoogleMapActivity.this,
						resources.getString(R.string.can_not_create));
				toast.show();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			refreshMap();
		}

	}

	private boolean checkIfCanCreate(double latitude, double longitude) {
		boolean canCreate = true;
		for (PlaceNearbyInformationBean placeinfo : placeInfos) {
			if (((placeinfo.getLatitude() - RANGE) < latitude)
					&& (latitude < (placeinfo.getLatitude() + RANGE))) {
				canCreate = false;
			} else if (((placeinfo.getLatitude() - RANGE) < longitude)
					&& (longitude < (placeinfo.getLatitude() + RANGE))) {
				canCreate = false;
			} else {
				canCreate = true;
			}

			if (!canCreate) {
				break;
			}
		}
		return canCreate;
	}

	public void viewPlaces() {
		Intent intent = new Intent(getApplicationContext(),
				NearbyPlaceListActivity.class);
		intent.putExtra("places", (Serializable) placeInfos);
		startActivity(intent);
	}

	private void viewFriends() {
		Intent intent = new Intent(getApplicationContext(),
				NearbyFriendListActivity.class);
		intent.putExtra("friends", (Serializable) friendInfos);
		startActivity(intent);
	}

	private void refreshMap() {
		MyApplication application = (MyApplication) getApplication();
		final String sessionID = (String) application.getData("sessionID");
		//
		Location currentLocation = locationManager
				.getLastKnownLocation(locationProvider.getName());
		if (currentLocation != null) {
			final double latitude = currentLocation.getLatitude();
			final double longitude = currentLocation.getLongitude();
			getNearbyFriend(sessionID, latitude, longitude);
			getNearbyPlace(sessionID, latitude, longitude);
		}
	}

	private void updateLocation() {
		Location currentLocation = locationManager
				.getLastKnownLocation(locationProvider.getName());
		if (currentLocation != null) {
			MyApplication application = (MyApplication) getApplication();
			final String sessionID = (String) application.getData("sessionID");
			final double latitude = currentLocation.getLatitude();
			final double longitude = currentLocation.getLongitude();
			showDialog(WAITTING_DIALOG);
			(new AsyncTask<Void, Void, Void>() {

				private boolean success = false;

				@Override
				protected Void doInBackground(Void... params) {
					success = CheckInUtilities.userUpdateLocation(sessionID,
							latitude, longitude);
					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					super.onPostExecute(result);
					dismissDialog(WAITTING_DIALOG);
					if (success) {
						Toast toast = NofityUtilities
								.createMessageToast(
										GoogleMapActivity.this,
										resources
												.getString(R.string.update_location_success));
						toast.show();
					}
				}
			}).execute();

		}
	}

	private void prepareLocationProvider() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationProvider = locationManager
					.getProvider(LocationManager.GPS_PROVIDER);
		} else {
			locationProvider = locationManager
					.getProvider(LocationManager.NETWORK_PROVIDER);
		}

	}

	private void prepareMap() {
		map = (MapView) findViewById(R.id.map_view);
		map.setBuiltInZoomControls(true);
		controller = map.getController();
		Location currentLocation = locationManager
				.getLastKnownLocation(locationProvider.getName());
		if (currentLocation != null) {
			Double latitude = currentLocation.getLatitude() * 1E6;
			Double longitude = currentLocation.getLongitude() * 1E6;
			GeoPoint currentPoint = new GeoPoint(latitude.intValue(),
					longitude.intValue());
			controller.setCenter(currentPoint);
		}
		controller.setZoom(16);
		//
		locationOverlay = new DynamicMyLocationOverlay(this, map, controller);
		CheckinOverLay checkinOverLay = new CheckinOverLay(getResources()
				.getDrawable(R.drawable.checkin));
		map.getOverlays().add(checkinOverLay);
		map.getOverlays().add(locationOverlay);
		//
		overlayManager = new OverlayManager(this, map);
		friendManagedOverlay = overlayManager.createOverlay("friends",
				defaultMarker);
		placeManagedOverlay = overlayManager.createOverlay("places",
				defaultMarker);
		overlayManager.populate();

		friendManagedOverlay
				.setOnOverlayGestureListener(new OnOverlayGestureListener() {

					private View root = null;

					@Override
					public boolean onZoom(ZoomEvent arg0, ManagedOverlay arg1) {
						return false;
					}

					@Override
					public boolean onSingleTap(MotionEvent event,
							ManagedOverlay overlay, GeoPoint point,
							ManagedOverlayItem item) {
						if (item == null) {
							return false;
						} else {
							map.removeAllViews();
							Context context = map.getContext();
							if (root == null) {
								LayoutInflater inflater = (LayoutInflater) context
										.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
								root = inflater
										.inflate(R.layout.map_item, null);
							}
							TextView text = (TextView) root
									.findViewById(R.id.title);
							text.setText(item.getTitle());
							MapView.LayoutParams screenLP;
							Drawable drawable = item
									.getMarker(OverlayItem.ITEM_STATE_FOCUSED_MASK);
							if (drawable == null) {
								drawable = defaultMarker;
							}
							Projection projection = map.getProjection();
							Point bottomPoint = projection.toPixels(
									item.getPoint(), null);
							bottomPoint.offset(0, -drawable.getMinimumHeight());
							GeoPoint geoPoint = projection.fromPixels(
									bottomPoint.x, bottomPoint.y);
							screenLP = new MapView.LayoutParams(
									MapView.LayoutParams.WRAP_CONTENT,
									MapView.LayoutParams.WRAP_CONTENT,
									geoPoint,
									MapView.LayoutParams.BOTTOM_CENTER);
							map.addView(root, screenLP);
							return true;
						}

					}

					@Override
					public boolean onScrolled(MotionEvent arg0,
							MotionEvent arg1, float arg2, float arg3,
							ManagedOverlay arg4) {
						return false;
					}

					@Override
					public void onLongPressFinished(MotionEvent arg0,
							ManagedOverlay arg1, GeoPoint arg2,
							ManagedOverlayItem item) {
						if (item == null) {
							return;
						} else {
							NearbyFriendOverlayItem friendItem = (NearbyFriendOverlayItem) item;
							String friendID = friendItem.getFriendID();
							Intent intent = new Intent(getApplicationContext(),
									FriendInfoActivity.class);
							intent.putExtra("friendID", friendID);
							startActivity(intent);
						}

					}

					@Override
					public void onLongPress(MotionEvent arg0,
							ManagedOverlay arg1) {
					}

					@Override
					public boolean onDoubleTap(MotionEvent arg0,
							ManagedOverlay arg1, GeoPoint arg2,
							ManagedOverlayItem arg3) {
						return false;
					}
				});

		placeManagedOverlay
				.setOnOverlayGestureListener(new OnOverlayGestureListener() {

					private View root = null;

					@Override
					public boolean onZoom(ZoomEvent arg0, ManagedOverlay arg1) {
						return false;
					}

					@Override
					public boolean onSingleTap(MotionEvent event,
							ManagedOverlay overlay, GeoPoint point,
							ManagedOverlayItem item) {
						if (item == null) {
							return false;
						} else {
							map.removeAllViews();
							Context context = map.getContext();
							if (root == null) {
								LayoutInflater inflater = (LayoutInflater) context
										.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
								root = inflater
										.inflate(R.layout.map_item, null);
							}
							TextView text = (TextView) root
									.findViewById(R.id.title);
							text.setText(item.getTitle());
							MapView.LayoutParams screenLP;
							Drawable drawable = item
									.getMarker(OverlayItem.ITEM_STATE_FOCUSED_MASK);
							if (drawable == null) {
								drawable = defaultMarker;
							}
							Projection projection = map.getProjection();
							Point bottomPoint = projection.toPixels(
									item.getPoint(), null);
							bottomPoint.offset(0, -drawable.getMinimumHeight());
							GeoPoint geoPoint = projection.fromPixels(
									bottomPoint.x, bottomPoint.y);
							screenLP = new MapView.LayoutParams(
									MapView.LayoutParams.WRAP_CONTENT,
									MapView.LayoutParams.WRAP_CONTENT,
									geoPoint,
									MapView.LayoutParams.BOTTOM_CENTER);
							map.addView(root, screenLP);
							return true;
						}

					}

					@Override
					public boolean onScrolled(MotionEvent arg0,
							MotionEvent arg1, float arg2, float arg3,
							ManagedOverlay arg4) {
						// TODO Auto-generated method stub
						return false;
					}

					@Override
					public void onLongPressFinished(MotionEvent arg0,
							ManagedOverlay arg1, GeoPoint arg2,
							ManagedOverlayItem item) {
						if (item == null) {
							return;
						} else {
							NearbyPlaceOverlayItem placeItem = (NearbyPlaceOverlayItem) item;
							String placeID = placeItem.getPlaceID();
							Intent intent = new Intent(getApplicationContext(),
									PlaceInformationActivity.class);
							intent.putExtra("placeID", placeID);
							intent.putExtra("FROM_MAP", FROM_MAP);
							startActivity(intent);
						}

					}

					@Override
					public void onLongPress(MotionEvent arg0,
							ManagedOverlay arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public boolean onDoubleTap(MotionEvent arg0,
							ManagedOverlay arg1, GeoPoint arg2,
							ManagedOverlayItem arg3) {
						// TODO Auto-generated method stub
						return false;
					}
				});

	}

	private void getNearbyFriend(final String sessionID, final double latitude,
			final double longitude) {
		friendManagedOverlay.getOverlayItems().clear();
		showDialog(WAITTING_DIALOG);
		(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				friendInfos = FriendUtilities.findNearbyFriend(sessionID,
						latitude, longitude);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				//
				for (FriendNearbyInformationBean bean : friendInfos) {
					NearbyFriendOverlayItem item = new NearbyFriendOverlayItem(
							bean);
					friendManagedOverlay.add(item);
				}
				dismissDialog(WAITTING_DIALOG);
				Toast toast = NofityUtilities.createMessageToast(
						GoogleMapActivity.this,
						resources.getString(R.string.refresh_map_success));
				toast.show();
				updateAvatars(sessionID);
			}
		}).execute();

	}

	private void updateAvatars(final String sessionID) {
		final List<ManagedOverlayItem> items = friendManagedOverlay
				.getOverlayItems();
		(new AsyncTask<Void, InputStream, Void>() {

			private NearbyFriendOverlayItem friendItem = null;

			@Override
			protected Void doInBackground(Void... params) {
				for (ManagedOverlayItem item : items) {
					friendItem = (NearbyFriendOverlayItem) item;
					String friendID = friendItem.getFriendID();
					InputStream avatarStream = CachUtilities.getFriendAvatar(
							sessionID, friendID, getExternalCacheDir());
					publishProgress(avatarStream);
				}
				return null;
			}

			@Override
			protected void onProgressUpdate(InputStream... progress) {
				Drawable avatar = null;
				if (progress[0] != null) {
					avatar = ImageUtilities.resizeImage(70, 70, progress[0],
							resources);
				} else {
					avatar = defaultMarker;
				}
				ManagedOverlay.boundToCenterBottom(avatar);
				friendItem.setMarker(avatar);
			}
		}).execute();
	}

	private void getNearbyPlace(final String sessionID, final double latitude,
			final double longitude) {
		placeManagedOverlay.getOverlayItems().clear();
		(new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				placeInfos = PlaceUtilities.getNearbyPlace(sessionID,
						longitude, latitude);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				for (PlaceNearbyInformationBean bean : placeInfos) {
					NearbyPlaceOverlayItem item = new NearbyPlaceOverlayItem(
							bean);
					placeManagedOverlay.add(item);
				}
			}
		}).execute();
	}

	public class CheckinOverLay extends Overlay {

		BitmapDrawable bmp;
		int w, h;

		public CheckinOverLay(Drawable d) {
			// TODO Auto-generated constructor stub
			this.bmp = (BitmapDrawable) d;
			w = bmp.getIntrinsicWidth();
			h = bmp.getIntrinsicHeight();
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			bmp.setBounds(5, 5, w, h + 5);
			bmp.draw(canvas);
		}

		@Override
		public boolean onTap(GeoPoint p, MapView mapView) {
			Projection projection = mapView.getProjection();
			Point point = projection.toPixels(p, null);
			int x = point.x;
			int y = point.y;
			if (5 < x && x < w && 5 < y && y < h + 5) {
				viewPlaces();
			}
			return true;
		}
	}
	
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && 
				event.getRepeatCount() == 0) {
			Dialog dialog = new AlertDialog.Builder(GoogleMapActivity.this)
			.setTitle("确定要退出吗？")
			.setPositiveButton("确定", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							GoogleMapActivity.this.finish();
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
