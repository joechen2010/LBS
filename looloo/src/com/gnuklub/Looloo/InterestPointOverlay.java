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
 * Class for drawing various (both people and places) icons
 * on a map. Also handles focus event
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.google.android.maps.MapActivity;
import com.google.android.maps.Overlay;
import com.google.android.maps.Point;

public class InterestPointOverlay extends Overlay {
	private Paint bitmapPaint = null;
	private MapActivity m;
	private Bitmap bm = null;
	private PlacePoint placePoint;
	private PersonPoint personPoint;
	private int pointType;

	public InterestPointOverlay(MapActivity map, PlacePoint p) {
		bitmapPaint = new Paint();
		m = map;
		placePoint = p;
		pointType = Constants.POINT_TYPE_PLACE;
	}

	public InterestPointOverlay(MapActivity map, PersonPoint p) {
		bitmapPaint = new Paint();
		m = map;
		personPoint = p;
		pointType = Constants.POINT_TYPE_HUMAN;
	}

	@Override
	public void draw(Canvas canvas, PixelCalculator pxC, boolean b) {
		super.draw(canvas, pxC, b);
		//bitmapPaint.setAlpha(255);
		int[] p = new int[2];
		switch (pointType) {
		case Constants.POINT_TYPE_HUMAN:
			MapPointToScreenCoords(personPoint, p, pxC);
			if (personPoint.isFocused()) {
				bitmapPaint.setAlpha(255);
				if (personPoint.getGender() == 0) {
					bm = BitmapFactory.decodeResource(this.m.getResources(),
							R.drawable.male_red);
				} else {
					bm = BitmapFactory.decodeResource(this.m.getResources(),
							R.drawable.female_red);
				}
			} else {
				bitmapPaint.setAlpha(125);
				if (personPoint.getGender() == 0) {
					bm = BitmapFactory.decodeResource(this.m.getResources(),
							R.drawable.male);
				} else {
					bm = BitmapFactory.decodeResource(this.m.getResources(),
							R.drawable.female);
				}
			}
			break;
		case Constants.POINT_TYPE_PLACE:
			MapPointToScreenCoords(placePoint, p, pxC);
			if (placePoint.isFocused()) {
				bitmapPaint.setAlpha(255);
				switch (placePoint.getTypeID()) {
				case Constants.PLACE_TYPE_RESTAURANT:
					bm = BitmapFactory.decodeResource(this.m.getResources(),
							R.drawable.restaurant_red);
					break;
				case Constants.PLACE_TYPE_BAR:
					bm = BitmapFactory.decodeResource(this.m.getResources(),
							R.drawable.party_red);
					break;
				case Constants.PLACE_TYPE_EDUCATION:
					bm = BitmapFactory.decodeResource(this.m.getResources(),
							R.drawable.education_red);
					break;
				case Constants.PLACE_TYPE_MONUMENT:
					bm = BitmapFactory.decodeResource(this.m.getResources(),
							R.drawable.monument_red);
					break;
				case Constants.PLACE_TYPE_SHOPPING:
					bm = BitmapFactory.decodeResource(this.m.getResources(),
							R.drawable.shopping_red);
					break;
				case Constants.PLACE_TYPE_SPORT:
					bm = BitmapFactory.decodeResource(this.m.getResources(),
							R.drawable.sport_red);
					break;
				}
			} else {
				bitmapPaint.setAlpha(125);
				switch (placePoint.getTypeID()) {
				case Constants.PLACE_TYPE_RESTAURANT:
					bm = BitmapFactory.decodeResource(this.m.getResources(),
							R.drawable.restaurant);
					break;
				case Constants.PLACE_TYPE_BAR:
					bm = BitmapFactory.decodeResource(this.m.getResources(),
							R.drawable.party);
					break;
				case Constants.PLACE_TYPE_EDUCATION:
					bm = BitmapFactory.decodeResource(this.m.getResources(),
							R.drawable.education);
					break;
				case Constants.PLACE_TYPE_MONUMENT:
					bm = BitmapFactory.decodeResource(this.m.getResources(),
							R.drawable.monument);
					break;
				case Constants.PLACE_TYPE_SHOPPING:
					bm = BitmapFactory.decodeResource(this.m.getResources(),
							R.drawable.shopping);
					break;
				case Constants.PLACE_TYPE_SPORT:
					bm = BitmapFactory.decodeResource(this.m.getResources(),
							R.drawable.sport);
					break;
				}
			}
			
			break;
		}

		canvas.drawBitmap(bm, p[0], p[1] - bm.getHeight(), bitmapPaint);
	}

	private void MapPointToScreenCoords(InterestPoint mp,
			int[] targetScreenCoords, PixelCalculator pxc) {
		Point p = new Point(mp.getX(), mp.getY());
		pxc.getPointXY(p, targetScreenCoords);
	}
}
