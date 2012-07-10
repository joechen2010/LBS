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
 * Just a simple about view.
 */
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class About extends Activity {
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.about);

		TextView about = (TextView) findViewById(R.id.aboutText);
		String text;
		text = "\n LooLoo represents modern, location-based service designed to support and utilize all of the advantages and cool new features of Google Android platform. It has strong integration with Google maps and delivers a fantastic mobile experience."
				+ "\n\n"
				+ "All that you have to do is to create an account by choosing your personal preferences and then you can enter the fabulous world of LooLoo. Locate people with same passions, hobbies, same fears and dreams, same favorite TV shows or ice cream flavors. Exchange information, meet your soul-mate, fall in love. Find restaurants with your favorite meal or night clubs that suit your music taste, find which monuments and sites are must-see locations. Write reviews of places, rank them, promote your favorite ones. It is all here and on the palm of your hand, whole new chapter in the world of mobile applications. LooLoo is designed to be all that and much more... "
				+ "\n"
				+ "\nCurrent developers: \n\n"
				+ "Mladen Djordjevic <mladen.djordjevic@gmail.com>\n"
				+ "Dragan Jovev <dragan.jovev@gmail.com>\n"
				+ "Milan Markovic <zivotinja@gmail.com>\n"
				+ "Aleksandar Tosovic <atosovic@gmail.com>\n";
		about.setText(text);
	}
}