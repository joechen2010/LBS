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
 * View showing selected person's details
 */

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonDetails extends Activity {
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.person_details);
		Bundle extras = getIntent().getExtras();
		ArrayList<String> cbList1 = new ArrayList<String>(14);
		cbList1.add("Looking for: Friendship");
		cbList1.add("Looking for: Relationship");
		cbList1.add("Film: Action");
		cbList1.add("Film: Drama");
		cbList1.add("Film: Horror");
		cbList1.add("Film: Comedy");
		cbList1.add("Music: Pop");
		cbList1.add("Music: Rock");
		cbList1.add("Music: Techno");
		cbList1.add("Music: Metal");
		cbList1.add("Books: Classics");
		cbList1.add("Books: Modern");
		cbList1.add("Passtime: Computers");
		cbList1.add("Passtime: Sport");

		ImageView genderImage = (ImageView) findViewById(R.id.personDetailsImage);

		if (extras.getInt("com.gnuklub.Looloo.PERSONDETAILSGENDER") == 1)
			genderImage.setImageResource(R.drawable.female_big);
		else
			genderImage.setImageResource(R.drawable.male_big);

		TextView gender = (TextView) findViewById(R.id.personDetailsGender);
		if (extras.getInt("com.gnuklub.Looloo.PERSONDETAILSGENDER") == 1)
			gender.setText("Female");
		else
			gender.setText("Male");

		TextView occupation = (TextView) findViewById(R.id.personDetailsOccupation);
		occupation.setText(extras
				.getCharSequence("com.gnuklub.Looloo.PERSONDETAILSOCCUPATION"));

		TextView yearOfBirth = (TextView) findViewById(R.id.personDetailsYearOfBirth);
		yearOfBirth.setText(String.valueOf(extras
				.getInt("com.gnuklub.Looloo.PERSONDETAILSYEAROFBIRTH")));

		TextView interests = (TextView) findViewById(R.id.personDetailsInterests);
		String interestsChoices = int2bin(extras
				.getInt("com.gnuklub.Looloo.PERSONDETAILSINTERESTS"));
		String interestsText = "";

		for (int i = 0; i < interestsChoices.length(); i++) {
			if (interestsChoices.charAt(i) == '1') {
				interestsText = interestsText.concat(cbList1.get(i));
				interestsText = interestsText.concat("\n");
			}
		}

		if (interestsText == "" || interests.length() == 0)
			interests.setText("No interests");
		else
			interests.setText(interestsText);

		Button back = (Button) findViewById(R.id.personDetailsButtonBack);
		back.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				PersonDetails.this.finish();
			}
		});
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