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
 * Generic point class that is drawing on a map.
 */

import com.google.android.maps.Point;
import com.google.googlenav.map.MapPoint;

public class InterestPoint {
	private int X = -1;
	private int Y = -1;
	public MapPoint pointLocation;
	private boolean isFocused;

	InterestPoint() {
	}

	InterestPoint(MapPoint p) {
		pointLocation = p;
	}

	InterestPoint(int xx, int yy) {
		pointLocation = new MapPoint(xx, yy);
	}

	public MapPoint getPointLocation() {
		return pointLocation;
	}

	public void setX(int x) {
		X = x;
	}

	public void setY(int y) {
		Y = y;
	}

	public int getX() {
		return X;
	}

	public int getY() {
		return Y;
	}

	public void setInterestPointFromXY() {
		pointLocation = new MapPoint(X, Y);
	}
	
	public Point interestPointToPoint() {
		return new Point(this.getX(), this.getY());
	}

	public void setFocus(boolean focus) {
		isFocused = focus;
	}

	public boolean isFocused() {
		return isFocused;
	}

}
