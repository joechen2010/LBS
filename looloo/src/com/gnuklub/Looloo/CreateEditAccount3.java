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
 * Creation and editing of LooLoo account
 */

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class CreateEditAccount3 extends Activity {

	public Integer placesOfInterest;
	private int res;
	private int origin;
	String placesOfInterestSelection;
	ArrayList<CheckBox> cbList = new ArrayList<CheckBox>();
	private ProgressDialog pd;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.create_edit_account_3);

		final ArrayList<CheckBox> cbList = new ArrayList<CheckBox>(14);
		cbList.add((CheckBox) findViewById(R.id.accountCheckRestaurantPizza));
		cbList.add((CheckBox) findViewById(R.id.accountCheckRestaurantSushi));
		cbList
				.add((CheckBox) findViewById(R.id.accountCheckNightBarsDiscotheques));
		cbList
				.add((CheckBox) findViewById(R.id.accountCheckNightBarsCoffeeBars));
		cbList
				.add((CheckBox) findViewById(R.id.accountCheckNightBarsBilliardClubs));
		cbList
				.add((CheckBox) findViewById(R.id.accountCheckMonumentsAllMonuments));
		cbList
				.add((CheckBox) findViewById(R.id.accountCheckSportPlacesSportArenas));
		cbList
				.add((CheckBox) findViewById(R.id.accountCheckSportPlacesTennisCourts));
		cbList
				.add((CheckBox) findViewById(R.id.accountCheckSportPlacesRecreationalParks));
		cbList
				.add((CheckBox) findViewById(R.id.accountCheckEducationalPlacesUniversities));
		cbList
				.add((CheckBox) findViewById(R.id.accountCheckEducationalPlacesColleges));
		cbList
				.add((CheckBox) findViewById(R.id.accountCheckEducationalPlacesHighSchools));
		cbList
				.add((CheckBox) findViewById(R.id.accountCheckShoppingShoppingMalls));
		cbList
				.add((CheckBox) findViewById(R.id.accountCheckShoppingOpenMarkets));

		Button apply = (Button) findViewById(R.id.apply);

		apply.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {

				String check = new String();
				for (int i = 0; i < 14; i++) {
					if (cbList.get(i).isChecked())
						check = check.concat("1");
					else
						check = check.concat("0");
				}
				placesOfInterest = bin2int(check);

				DB db = new DB(CreateEditAccount3.this);
				db.setPlacesOfInterest(placesOfInterest);
				final Bundle extras = getIntent().getExtras();
				origin = extras.getInt("com.gnuklub.Looloo.ORIGIN");
				if (origin == 0)
					pd = ProgressDialog.show(CreateEditAccount3.this,
							"Please wait", "Creating new account...", false,
							false);
				else
					pd = ProgressDialog.show(CreateEditAccount3.this,
							"Please wait", "Updating account...", false, false);
				Handler h = new Handler();
				Runnable r1 = new Runnable() {
					public void run() {

						try {
							RPC rpc = new RPC(CreateEditAccount3.this);
							Location loc;
							LocationManager lm;
							lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
							List<LocationProvider> providers = lm
									.getProviders();
							LocationProvider provider = providers.get(0);
							loc = lm.getCurrentLocation(provider.getName());

							if (origin == 0) {
								res = rpc
										.createAccount(
												extras
														.getString("com.gnuklub.Looloo.ACCOUNTUSERNAME"),
												extras
														.getString("com.gnuklub.Looloo.ACCOUNTPASSWORD"),
												extras
														.getInt("com.gnuklub.Looloo.ACCOUNTGENDER"),
												extras
														.getInt("com.gnuklub.Looloo.ACCOUNTDOB"),
												extras
														.getString("com.gnuklub.Looloo.ACCOUNTOCCUPATION"),
												extras
														.getString("com.gnuklub.Looloo.ACCOUNTEMAIL"),
												extras
														.getInt("com.gnuklub.Looloo.ACCOUNTUSERPREFS"),
												loc);
							} else {
								res = rpc
										.updateAccount(
												extras
														.getInt("com.gnuklub.Looloo.SESSID"),
												extras
														.getString("com.gnuklub.Looloo.ACCOUNTPASSWORD"),
												extras
														.getInt("com.gnuklub.Looloo.ACCOUNTGENDER"),
												extras
														.getInt("com.gnuklub.Looloo.ACCOUNTDOB"),
												extras
														.getString("com.gnuklub.Looloo.ACCOUNTOCCUPATION"),
												extras
														.getString("com.gnuklub.Looloo.ACCOUNTEMAIL"),
												extras
														.getInt("com.gnuklub.Looloo.ACCOUNTUSERPREFS"));
							}

							if (res == -1) {
								pd.dismiss();
								final Dialog dialog1 = new Dialog(
										CreateEditAccount3.this);
								dialog1.setContentView(R.layout.error_dialog);
								dialog1.setTitle("Username already exists");
								dialog1.show();
								Button ok = (Button) dialog1
										.findViewById(R.id.errorDialogOk);
								ok
										.setOnClickListener(new View.OnClickListener() {
											public void onClick(View arg0) {
												dialog1.dismiss();
											}
										});
							} else {
								Intent i = new Intent(CreateEditAccount3.this,
										SearchView.class);
								if (origin == 0)
									i.putExtra("com.gnuklub.Looloo.SESSIONID",
											res);
								else
									i
											.putExtra(
													"com.gnuklub.Looloo.SESSIONID",
													extras
															.getInt("com.gnuklub.Looloo.SESSID"));
								startActivity(i);

							}
						} catch (ConnectionException ce) {
							pd.dismiss();
							final Dialog dialog = new Dialog(
									CreateEditAccount3.this);
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
				Runnable r2 = new Runnable() {
					public void run() {
						pd.dismiss();
					}
				};
				h.post(r2);
			}
		});

		DB db = new DB(this);
		List<Row> dataRefresh = db.getUserPrefs();
		placesOfInterest = dataRefresh.get(0).placesOfInterest;
		placesOfInterestSelection = int2bin(placesOfInterest);

		for (int i = 0; i < placesOfInterestSelection.length(); i++) {
			if (placesOfInterestSelection.charAt(i) == '1')
				cbList.get(i).setChecked(true);
			else
				cbList.get(i).setChecked(false);
		}

	}

	private int bin2int(String bin) {
		return Integer.parseInt(bin, 2);
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
