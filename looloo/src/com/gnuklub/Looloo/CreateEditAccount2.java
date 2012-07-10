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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class CreateEditAccount2 extends Activity {

	public Integer preferences;
	String preferencesSelection;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.create_edit_account_2);

		final ArrayList<CheckBox> cbList1 = new ArrayList<CheckBox>(14);
		cbList1.add((CheckBox) findViewById(R.id.preferencesLookingForMale));
		cbList1.add((CheckBox) findViewById(R.id.preferencesLookingForFemale));
		cbList1.add((CheckBox) findViewById(R.id.preferencesFilmTasteAction));
		cbList1.add((CheckBox) findViewById(R.id.preferencesFilmTasteDrama));
		cbList1.add((CheckBox) findViewById(R.id.preferencesFilmTasteHorror));
		cbList1.add((CheckBox) findViewById(R.id.preferencesFilmTasteComedy));
		cbList1.add((CheckBox) findViewById(R.id.preferencesMusicTastePop));
		cbList1.add((CheckBox) findViewById(R.id.preferencesMusicTasteRock));
		cbList1.add((CheckBox) findViewById(R.id.preferencesMusicTasteTechno));
		cbList1.add((CheckBox) findViewById(R.id.preferencesMusicTasteMetal));
		cbList1.add((CheckBox) findViewById(R.id.preferencesBookTasteClassics));
		cbList1.add((CheckBox) findViewById(R.id.preferencesBookTasteModern));
		cbList1
				.add((CheckBox) findViewById(R.id.preferencesFavoritePastimeComputers));
		cbList1
				.add((CheckBox) findViewById(R.id.preferencesFavoritePastimeSports));
		Button next = (Button) findViewById(R.id.preferencesNext);

		next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				String pref = new String();
				for (int i = 0; i < 14; i++) {
					if (cbList1.get(i).isChecked())
						pref = pref.concat("1");
					else
						pref = pref.concat("0");
				}

				preferences = bin2int(pref);

				DB db = new DB(CreateEditAccount2.this);
				db.setPreferences(preferences);

				Intent i = new Intent(CreateEditAccount2.this,
						CreateEditAccount3.class);
				Bundle extras = getIntent().getExtras();
				i.putExtra("com.gnuklub.Looloo.ACCOUNTUSERNAME", extras
						.getString("com.gnuklub.Looloo.ACCOUNTUSERNAME"));
				i.putExtra("com.gnuklub.Looloo.ACCOUNTPASSWORD", extras
						.getString("com.gnuklub.Looloo.ACCOUNTPASSWORD"));
				i.putExtra("com.gnuklub.Looloo.ACCOUNTGENDER", extras
						.getInt("com.gnuklub.Looloo.ACCOUNTGENDER"));
				i.putExtra("com.gnuklub.Looloo.ACCOUNTDOB", extras
						.getInt("com.gnuklub.Looloo.ACCOUNTDOB"));
				i.putExtra("com.gnuklub.Looloo.ACCOUNTOCCUPATION", extras
						.getString("com.gnuklub.Looloo.ACCOUNTOCCUPATION"));
				i.putExtra("com.gnuklub.Looloo.ACCOUNTEMAIL", extras
						.getString("com.gnuklub.Looloo.ACCOUNTEMAIL"));
				i.putExtra("com.gnuklub.Looloo.ACCOUNTUSERPREFS", preferences);
				i.putExtra("com.gnuklub.Looloo.ORIGIN", extras
						.getInt("com.gnuklub.Looloo.ORIGIN"));
				i.putExtra("com.gnuklub.Looloo.SESSID", extras
						.getInt("com.gnuklub.Looloo.SESSID"));

				startActivity(i);

			}
		});

		DB db = new DB(this);
		List<Row> dataRefresh = db.getUserPrefs();
		preferences = dataRefresh.get(0).preferences;
		preferencesSelection = int2bin(preferences);

		for (int i = 0; i < preferencesSelection.length(); i++) {
			if (preferencesSelection.charAt(i) == '1')
				cbList1.get(i).setChecked(true);
			else
				cbList1.get(i).setChecked(false);
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