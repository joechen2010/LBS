/*
  Authors 
  Dragan Jovev <dragan.jovev@gmail.com>
  Mladen Djordjevic <mladen.djordjevic@gmail.com>
  
  Released under the GPL, as follows:

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.gnuklub.Looloo;

/*
 * Search View
 */

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
import android.widget.Button;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayController;
import com.google.android.maps.Point;

public class SearchView extends MapActivity {

	private static final int REFRESH_ID = Menu.FIRST;
	private static final int PROFILE_ID = Menu.FIRST + 1;
	private static final int SETTINGS_ID = Menu.FIRST + 2;
	private static final int ABOUT_ID = Menu.FIRST + 3;
	private static final int POINT_INFO_ID = Menu.FIRST + 4;
	private static final int LOGOUT_ID = Menu.FIRST + 5;

	// Map-related variables
	private MapView m;
	private MapController mc;
	private OverlayController mOC;

	// Points related variables
	private ArrayList<PersonPoint> peoplePoints;
	private ArrayList<PlacePoint> placePoints;
	private PlacePoint placePoint;
	private PersonPoint personPoint;
	private int focusedPoint = 0;
	private int focusedPointType = Constants.POINT_TYPE_HUMAN;

	private int sessid;
	private int selectedMenuItem;

	private ProgressDialog pd;

	private RPC rpc;
	private List<Row> prefs;

	private LocationManager lm;
	private Location loc;

	private Handler h;
	private Runnable r1;
	private Runnable r2;

	private DB db;

	boolean refreshOnStart;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);

		db = new DB(this);
		prefs = db.getUserPrefs();
		// Bad workaround
		if (prefs.get(0).preferences == 0) {
			prefs.get(0).preferences = 1;
		}
		if (prefs.get(0).placesOfInterest == 0) {
			prefs.get(0).placesOfInterest = 1;
		}
		Bundle extras = getIntent().getExtras();
		sessid = extras.getInt("com.gnuklub.Looloo.SESSIONID");
		refreshOnStart = extras
				.getBoolean("com.gnuklub.Looloo.REFRESH_ON_START");
		m = (MapView) findViewById(R.id.map);
		rpc = new RPC(this);
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		List<LocationProvider> providers = lm.getProviders();

		// Make it get the best provider
		// not the first one
		LocationProvider provider = providers.get(0);
		loc = lm.getCurrentLocation(provider.getName());

		mc = m.getController();
		mOC = m.createOverlayController();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (refreshOnStart) {
			refreshOnStart = false;
			Point myLoc = new Point((int) (loc.getLatitude() * 1000000),
					(int) (loc.getLongitude() * 1000000));
			mc.centerMapTo(myLoc, true);// mc.zoomTo(Integer.valueOf(db.getSetting("SEARCH_RADIUS"))
										// + 12);
			switch (Integer.valueOf(db.getSetting("SEARCH_RADIUS"))) {
			case 100:
				mc.zoomTo(17);
				break;
			case 200:
				mc.zoomTo(16);
				break;
			case 500:
				mc.zoomTo(15);
				break;
			case 1000:
				mc.zoomTo(14);
				break;
			case 2000:
				mc.zoomTo(13);
				break;
			case 5000:
				mc.zoomTo(12);
				break;
			case 10000:
				mc.zoomTo(11);
				break;
			case 20000:
				mc.zoomTo(10);
				break;
			}
			refresh();
		}
	}

	/*
	 * @Override protected void onDestroy() { pd = ProgressDialog.show(this,
	 * "Please wait", "Logging out...", false, false); h = new Handler(); r1 =
	 * new Runnable() { public void run() {
	 * 
	 * try { rpc.logout(sessid); SearchView.this.finish(); } catch
	 * (ConnectionException ce) { pd.dismiss(); } } }; h.post(r1); r2 = new
	 * Runnable() { public void run() { pd.dismiss(); } }; h.post(r2); }
	 */

	/*
	 * Treba doraditi da se seta i kroz ljude i kroz mesta
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent msg) {

		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			if (focusedPointType == Constants.POINT_TYPE_PLACE
					&& placePoints != null && placePoints.size() > 0) {
				try {
					placePoints.get(focusedPoint + 1);
					placePoints.get(focusedPoint++).setFocus(false);
					placePoints.get(focusedPoint).setFocus(true);
				} catch (IndexOutOfBoundsException e) {
					focusedPoint = 0;
					placePoints.get(placePoints.size() - 1).setFocus(false);
					if (peoplePoints != null && peoplePoints.size() > 0) {
						peoplePoints.get(focusedPoint).setFocus(true);
						focusedPointType = Constants.POINT_TYPE_HUMAN;
					} else {
						placePoints.get(focusedPoint).setFocus(true);
					}
				}
			} else if (focusedPointType == Constants.POINT_TYPE_HUMAN
					&& peoplePoints != null && peoplePoints.size() > 0) {
				try {
					peoplePoints.get(focusedPoint + 1);
					peoplePoints.get(focusedPoint++).setFocus(false);
					peoplePoints.get(focusedPoint).setFocus(true);
				} catch (IndexOutOfBoundsException e) {
					focusedPoint = 0; // Ovde je ok
					peoplePoints.get(peoplePoints.size() - 1).setFocus(false);
					if (placePoints != null && placePoints.size() > 0) { // /
						placePoints.get(focusedPoint).setFocus(true);
						focusedPointType = Constants.POINT_TYPE_PLACE;
					} else {
						peoplePoints.get(focusedPoint).setFocus(true);
					}
				}
			} else {
				focusedPointType = Constants.POINT_TYPE_PLACE;
			}
			m.invalidate();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			if (focusedPointType == Constants.POINT_TYPE_PLACE
					&& placePoints != null) {
				try {
					placePoints.get(focusedPoint - 1);
					placePoints.get(focusedPoint--).setFocus(false);
					placePoints.get(focusedPoint).setFocus(true);
				} catch (IndexOutOfBoundsException e) {
					placePoints.get(0).setFocus(false);
					if (peoplePoints != null && peoplePoints.size() > 0) {
						focusedPoint = peoplePoints.size() - 1;
						peoplePoints.get(focusedPoint).setFocus(true);
						focusedPointType = Constants.POINT_TYPE_HUMAN;
					} else {
						focusedPoint = placePoints.size() - 1;
						placePoints.get(focusedPoint).setFocus(true);
					}
				}
			} else if (focusedPointType == Constants.POINT_TYPE_HUMAN
					&& peoplePoints != null && peoplePoints.size() > 0) {
				try {
					peoplePoints.get(focusedPoint - 1);
					peoplePoints.get(focusedPoint--).setFocus(false);
					peoplePoints.get(focusedPoint).setFocus(true);
				} catch (IndexOutOfBoundsException e) {
					peoplePoints.get(0).setFocus(false);
					if (placePoints != null && placePoints.size() > 0) {
						focusedPoint = placePoints.size() - 1;
						placePoints.get(focusedPoint).setFocus(true);
						focusedPointType = Constants.POINT_TYPE_PLACE;
					} else {
						focusedPoint = peoplePoints.size() - 1;
						peoplePoints.get(focusedPoint).setFocus(true);
					}
				}
			} else {
				focusedPointType = Constants.POINT_TYPE_PLACE;
			}
			m.invalidate();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, POINT_INFO_ID, R.string.svmenu_point_info, R.drawable.info);
		menu.add(0, REFRESH_ID, R.string.svmenu_refresh, R.drawable.refresh);
		menu
				.add(0, PROFILE_ID, R.string.svmenu_editprofile,
						R.drawable.profile);
		menu.add(0, SETTINGS_ID, R.string.svmenu_settings, R.drawable.settings);
		menu.add(0, ABOUT_ID, R.string.svmenu_about, R.drawable.about);
		menu.add(0, LOGOUT_ID, R.string.svmenu_logout, R.drawable.logout);
		return true;
	}

	/*
	 * @Override public boolean onPrepareOptionsMenu(Menu menu) { if
	 * (placePoints != null || peoplePoints != null) {
	 * menu.get(menu.findItemIndex(POINT_INFO_ID)).setShown(true); } else {
	 * menu.get(menu.findItemIndex(POINT_INFO_ID)).setShown(false); } return
	 * true; }
	 */

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		super.onOptionsMenuClosed(menu);
		switch (selectedMenuItem) {
		case REFRESH_ID:
			refresh();
			break;
		case LOGOUT_ID:
			logout();
			break;
		case POINT_INFO_ID:
			getPointInfo();
			break;
		}
		selectedMenuItem = -1;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, Item item) {
		super.onMenuItemSelected(featureId, item);
		switch (item.getId()) {
		case REFRESH_ID:
			selectedMenuItem = REFRESH_ID;
			break;
		case ABOUT_ID:
			selectedMenuItem = ABOUT_ID;
			startActivity(new Intent(this, About.class));
			break;
		case PROFILE_ID:
			selectedMenuItem = PROFILE_ID;
			Intent i = new Intent(this, CreateEditAccount1.class);
			i.putExtra("com.gnuklub.Looloo.ORIGIN", 1);
			i.putExtra("com.gnuklub.Looloo.SESSID", sessid);
			startActivity(i);
			break;
		case SETTINGS_ID:
			selectedMenuItem = SETTINGS_ID;
			i = new Intent(this, Settings.class);
			i.putExtra("com.gnuklub.Looloo.SESSIONID", sessid);
			i.putExtra("com.gnuklub.Looloo.REFRESH_ON_START", true);
			startActivity(i);
			break;
		case POINT_INFO_ID:
			selectedMenuItem = POINT_INFO_ID;
			break;
		case LOGOUT_ID:
			selectedMenuItem = LOGOUT_ID;
			break;
		}
		return true;
	}

	private void clearMap() {
		mOC.clear();
	}

	private void refresh() {
		clearMap();
		pd = ProgressDialog.show(this, "Please wait", "Getting data...", false,
				false);
		h = new Handler();
		r1 = new Runnable() {
			public void run() {
				try {
					double distanceNum = Integer.valueOf(db
							.getSetting("SEARCH_RADIUS"))
							* 180 / 6378137.0 / Math.PI;
					int numberOfCheckedChoices = 0;
					int choices = db.getPeoplePreferences();
					String choicesString = int2bin(choices);

					for (int i = 0; i < choicesString.length(); i++) {
						if (choicesString.charAt(i) == '1') {
							numberOfCheckedChoices++;
						}
					}
					int numberOfDesiredMatches = (int) (Integer.valueOf(db
							.getSetting("MATCHING_PERCENTAGE")) / 100.0 * numberOfCheckedChoices);
					rpc.updatePosition(sessid, loc.getLatitude(), loc
							.getLongitude());
					peoplePoints = rpc.getPeopleByInterest(sessid,
							prefs.get(0).preferences, numberOfDesiredMatches,
							distanceNum);
					for (int i = 0; i < peoplePoints.size(); i++) {
						InterestPointOverlay ipOverlay = new InterestPointOverlay(
								SearchView.this, peoplePoints.get(i));
						if (i == 0
								&& focusedPointType == Constants.POINT_TYPE_HUMAN) {
							peoplePoints.get(i).setFocus(true);
							focusedPoint = 0;
						}
						mOC.add(ipOverlay, false);
						mOC.activate(ipOverlay, false);
					}
					m.invalidate();
				} catch (ConnectionException e) {
					e.printStackTrace();
				}
			}
		};
		h.post(r1);

		r1 = new Runnable() {
			public void run() {
				try {
					double distanceNum = Integer.valueOf(db
							.getSetting("SEARCH_RADIUS"))
							* 180 / 6378137.0 / Math.PI;
					placePoints = rpc.getPlacesByInterest(sessid,
							prefs.get(0).placesOfInterest, distanceNum); // ///////////

					if (placePoints != null && placePoints.size() != 0) {
						// MapView m = (MapView) findViewById(R.id.map);
						mOC = m.createOverlayController();

						for (int i = 0; i < placePoints.size(); i++) {
							InterestPointOverlay ipOverlay = new InterestPointOverlay(
									SearchView.this, placePoints.get(i));
							if (i == 0
									&& focusedPointType == Constants.POINT_TYPE_PLACE) {
								placePoints.get(i).setFocus(true);
							}
							mOC.add(ipOverlay, false);
							mOC.activate(ipOverlay, false);
						}
						// MapController mc = m.getController();
						// mc.centerMapTo(placePoints.get(1)
						// .interestPointToPoint(), true);
						// mc.zoomTo(15);
					}
				} catch (ConnectionException ce) {
					placePoints = null;
					pd.dismiss();
					final Dialog dialog = new Dialog(SearchView.this);
					dialog.setContentView(R.layout.error_dialog);
					dialog.setTitle("Server not responding");
					dialog.show();
					Button ok = (Button) dialog
							.findViewById(R.id.errorDialogOk);
					ok.setOnClickListener(new View.OnClickListener() {
						public void onClick(View arg0) {
							dialog.dismiss();
						}
					});
				}
			}
		};
		h.post(r1);
		r2 = new Runnable() {
			public void run() {
				// refresh();
				pd.dismiss();
			}
		};
		h.post(r2);
	}

	private void logout() {
		pd = ProgressDialog.show(this, "Please wait", "Logging out...", false,
				false);
		h = new Handler();
		r1 = new Runnable() {
			public void run() {

				try {
					rpc.logout(sessid);
					SearchView.this.finish();
				} catch (ConnectionException ce) {
					pd.dismiss();
				}

			}
		};
		h.post(r1);
		r2 = new Runnable() {
			public void run() {
				pd.dismiss();
			}
		};
		h.post(r2);
	}

	private void getPointInfo() {
		if (placePoints.size() == 0 && peoplePoints.size() == 0) {
			final Dialog dialog2 = new Dialog(SearchView.this);
			dialog2.setContentView(R.layout.error_dialog);
			dialog2.setTitle("You must select point first");
			dialog2.show();
			Button ok = (Button) dialog2.findViewById(R.id.errorDialogOk);
			ok.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg0) {
					dialog2.dismiss();
				}
			});
		} else {
			if (focusedPointType == Constants.POINT_TYPE_HUMAN) {
				pd = ProgressDialog.show(SearchView.this, "Please wait",
						"Getting person details...", false, false);
				h = new Handler();
				r1 = new Runnable() {
					public void run() {
						try {
							personPoint = new PersonPoint();
							// Log.v("OVVVDEE SAM", String.valueOf();
							personPoint = rpc.getPersonDetails(sessid,
									peoplePoints.get(focusedPoint).getUserID());
							String occupation = personPoint.getOccupation();
							if (occupation == null || occupation.length() == 0)
								personPoint.setOccupation("No occupation");

							Log.v("POL", String
									.valueOf(personPoint.getGender()));
							Log.v("GODINA", String.valueOf(personPoint
									.getBirth()));
							Log.v("OCCUPATION", personPoint.getOccupation());
						} catch (ConnectionException ce) {
							pd.dismiss();
						}
					}
				};
			} else if (focusedPointType == Constants.POINT_TYPE_PLACE) {
				pd = ProgressDialog.show(SearchView.this, "Please wait",
						"Getting point info...", false, false);
				h = new Handler();
				r1 = new Runnable() {
					public void run() {
						try {
							placePoint = new PlacePoint();
							placePoint = rpc.getPlaceDetails(sessid,
									placePoints.get(focusedPoint).getPlaceID());
						} catch (ConnectionException ce) {
							pd.dismiss();
						}
					}
				};
			}

			h.post(r1);
			r2 = new Runnable() {
				public void run() {

					pd.dismiss();

					if (focusedPointType == Constants.POINT_TYPE_PLACE) {
						Intent i = new Intent(SearchView.this, PointInfo.class);
						i.putExtra("com.gnuklub.Looloo.PLACEINFONAME",
								placePoint.getPlaceName());
						i.putExtra("com.gnuklub.Looloo.PLACEINFODESCRIPTION",
								placePoint.getDescription());
						i.putExtra("com.gnuklub.Looloo.PLACEINFOTYPE",
								placePoint.getCategory());
						i.putExtra("com.gnuklub.Looloo.PLACEINCATEGORY",
								placePoint.getCategory());
						i.putExtra("com.gnuklub.Looloo.PLACEINFORANK",
								placePoint.getRank());
						i.putExtra("com.gnuklub.Looloo.PLACEINFOPOINTID",
								placePoint.getPlaceID());
						startActivity(i);
					} else {

						Log.v("POL", String.valueOf(personPoint.getGender()));
						Log.v("GODINA", String.valueOf(personPoint.getBirth()));
						Log.v("OCCUPATION", personPoint.getOccupation());

						Intent i = new Intent(SearchView.this,
								PersonDetails.class);
						i.putExtra("com.gnuklub.Looloo.PERSONDETAILSGENDER",
								personPoint.getGender());
						i.putExtra(
								"com.gnuklub.Looloo.PERSONDETAILSYEAROFBIRTH",
								personPoint.getBirth());
						i.putExtra(
								"com.gnuklub.Looloo.PERSONDETAILSOCCUPATION",
								personPoint.getOccupation());
						i.putExtra("com.gnuklub.Looloo.PERSONDETAILSINTERESTS",
								personPoint.getInterest());
						startActivity(i);
					}
				}
			};
			h.post(r2);
		}
	}

	private String int2bin(int integer) {
		String ret = Integer.toBinaryString(integer);
		int diff;
		int numOfPlaceTypes = Integer.valueOf(Constants.NUM_OF_PLACE_TYPES);
		diff = numOfPlaceTypes - ret.length();
		if (diff > 0) {
			String zeros = new String();
			for (int i = 0; i < diff; i++) {
				zeros += "0";
			}
			ret = zeros.concat(ret);
		}
		return ret;
	}
}