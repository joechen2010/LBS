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
 * User settings view
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Settings extends Activity {
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.settings);
		Bundle extras = getIntent().getExtras();
		final int sessid = extras.getInt("com.gnuklub.Looloo.SESSIONID");
		final Spinner matchPercentage = (Spinner) findViewById(R.id.settingsUpdateInt);
		final Spinner radius = (Spinner) findViewById(R.id.settingsRadius);
		Button ok = (Button) findViewById(R.id.settingsOK);
		final DB db = new DB(this);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.radius, android.R.layout.simple_spinner_item);
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		radius.setAdapter(adapter);

		String num = db.getSetting("SEARCH_RADIUS");
		String[] r = Settings.this.getResources().getStringArray(
				R.array.radiusValues);
		for (int i = 0; i < r.length; i++) {
			if (r[i].equals(num)) {
				radius.setSelection(i);
			}
		}

		adapter = ArrayAdapter.createFromResource(this,
				R.array.MatchingPercentage,
				android.R.layout.simple_spinner_item);
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		matchPercentage.setAdapter(adapter);

		num = db.getSetting("MATCHING_PERCENTAGE");
		r = Settings.this.getResources().getStringArray(
				R.array.MatchingPercentageValues);
		for (int i = 0; i < r.length; i++) {
			if (r[i].equals(num)) {
				matchPercentage.setSelection(i);
			}
		}
		radius.getSelectedItemPosition();

		ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {

				String[] r = Settings.this.getResources().getStringArray(
						R.array.radiusValues);
				db.setSetting("SEARCH_RADIUS", Integer.parseInt(r[radius
						.getSelectedItemPosition()]));
				r = Settings.this.getResources().getStringArray(
						R.array.MatchingPercentageValues);
				db
						.setSetting("MATCHING_PERCENTAGE", Integer
								.parseInt(r[matchPercentage
										.getSelectedItemPosition()]));
				Intent i = new Intent(Settings.this, SearchView.class);
				i.putExtra("com.gnuklub.Looloo.SESSIONID", sessid);
				i.putExtra("com.gnuklub.Looloo.REFRESH_ON_START", true);
				startActivity(i);
				Settings.this.finish();

			}
		});
	}
}
