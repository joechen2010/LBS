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
 * Constants used through all of the source
 */

import android.content.Intent;

public class Constants {
	static final int PARSE_MODE_REFRESH = 0;
	static final int PARSE_MODE_LOGIN = 1;
	static final int PARSE_MODE_LOGOUT = 2;
	static final int PARSE_MODE_PLACES = 3;
	static final int PARSE_MODE_POINT_INFO = 4;
	static final int PARSE_MODE_CREATE_ACCOUNT = 5;
	static final int PARSE_MODE_PEOPLE = 6;
	static final int PARSE_MODE_PERSON_INFO = 7;
	
	static final int NUM_OF_PLACE_TYPES = 14;
	
	static final int POINT_TYPE_PLACE = 0;
	static final int POINT_TYPE_HUMAN = 1;
	
	static final int PLACE_TYPE_RESTAURANT = 1000;
	static final int PLACE_TYPE_BAR = 1001;
	static final int PLACE_TYPE_MONUMENT = 1002;
	static final int PLACE_TYPE_SPORT = 1003;
	static final int PLACE_TYPE_EDUCATION = 1004;
	static final int PLACE_TYPE_SHOPPING = 1005;
	Intent i = new Intent();
}
