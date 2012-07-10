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
 * Shows selected point information
 */

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PointInfo extends Activity{
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        setContentView(R.layout.interest_point_focus);
        Bundle extras = getIntent().getExtras();
        
        TextView name = (TextView) findViewById(R.id.pointInfoName);
        TextView description = (TextView) findViewById(R.id.pointInfoDescription);
        TextView mark = (TextView) findViewById(R.id.pointInfoMark);
        ImageView image = (ImageView) findViewById(R.id.pointFocusImage);
        //double rank = extras.get("com.gnuklub.Looloo.PLACEINFORANK");
        
        name.setText(extras.getString("com.gnuklub.Looloo.PLACEINFONAME"));
        description.setText(extras.getString("com.gnuklub.Looloo.PLACEINFODESCRIPTION"));
        mark.setText(String.valueOf(extras.getDouble("com.gnuklub.Looloo.PLACEINFORANK")));
        String typeOfPlace = extras.getString("com.gnuklub.Looloo.PLACEINFOTYPE");
        Log.v("TIP MESTA",typeOfPlace);
        
        if (typeOfPlace.equals("Restaurant"))
                image.setImageResource(R.drawable.restaurant_big);
        if (typeOfPlace.equals("Bar"))
                image.setImageResource(R.drawable.party_big);
        if (typeOfPlace.equals("Monument"))
                image.setImageResource(R.drawable.monument_big);
        if (typeOfPlace.equals("Soport"))
                image.setImageResource(R.drawable.sport_big);
        if (typeOfPlace.equals("Education"))
                image.setImageResource(R.drawable.education_big);
        if (typeOfPlace.equals("Shopping"))
                image.setImageResource(R.drawable.shopping_big);

        Button back = (Button) findViewById(R.id.PointInfoButtonBack);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                PointInfo.this.finish();
            }
        });
        //sessid = extras.getInt("com.gnuklub.Looloo.SESSIONID");
    }
}