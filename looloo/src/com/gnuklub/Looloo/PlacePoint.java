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
 * Point class drown on a map that represent some place
 */

import com.google.googlenav.map.MapPoint;

public class PlacePoint extends InterestPoint {

	private int placeID = -1;
	private String placeName = null;
	private int typeID = -1;
	private String type;	// It shouldn't exist, but it's here because of rpc call
	private String category;
	private String description;
	private double rank;
	
	// Ovo nemam pojma sta mi salje hepek
	//private int NESTO = -1;
	
	PlacePoint() {
	}

	PlacePoint(int i, MapPoint p) {
		placeID = i;
		pointLocation = p;
	}

	PlacePoint(int i, int xx, int yy) {
		placeID = i;
		pointLocation = new MapPoint(xx, yy);
	}

	// Setters
	
	public void setPlaceID(int placeID) {
		this.placeID = placeID;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public void setType(String type) {
		this.type = type;
	}
	public void setTypeID(int typeID) {
		this.typeID = typeID;
	}
	
	public void setCategory(String category) {
    	this.category = category;
    }
	
	public void setDescription(String description) {
    	this.description = description;
    }
	
	public void setRank(double rank) {
    	this.rank = rank;
    }
/*
	public void setNESTO(int NESTO) {
		this.NESTO = NESTO;
	}
	*/
	// Getters

	public int getPlaceID() {
		return placeID;
	}

	public String getPlaceName() {
		return placeName;
	}

	public String getType() {
		return type;
	}
	
	public int getTypeID() {
		return typeID;
	}

	public String getCategory() {
    	return category;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public double getRank() {
    	return rank;
    }
	/*
	public int getNESTO() {
		return NESTO;
	}
*/
}
