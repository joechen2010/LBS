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
 * Parser that transforms received server's data into nice
 * wanted data structure
 */

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParser extends DefaultHandler {

	private int mode;
	private String s;
	private int loginResponse = -1;
	private int logoutResponse = -1;
	private int createAccountResponse = -1;

	private boolean double_in = false;
	private boolean value_in = false;
	private boolean int_in = false;
	private boolean array_in = false;
	private boolean member_in = false;
	private boolean data_in = false;
	private boolean struct_in = false;
	private boolean string_in = false;
	private boolean fault_in = false;
	private boolean params_in = false;

	private int memberCounter = 0;
	private int intCounter = 0;
	private int stringCounter = 0;
	private int doubleCounter = 0;

	public void setParserMode(int mode) {
		this.mode = mode;
	}

	private ArrayList<PersonPoint> peopleList;
	private ArrayList<PlacePoint> placesList;
	private PlacePoint ps;
	private PersonPoint pp;

	@Override
	public void startDocument() throws SAXException {
		if (mode == Constants.PARSE_MODE_PLACES) {
			placesList = new ArrayList<PlacePoint>(25);
		}
		if (mode == Constants.PARSE_MODE_PEOPLE) {
			peopleList = new ArrayList<PersonPoint>(25);
		}
		if (mode == Constants.PARSE_MODE_POINT_INFO) {
			ps = new PlacePoint();
		}
		if (mode == Constants.PARSE_MODE_PERSON_INFO) {
			pp = new PersonPoint();
		}
	}

	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		if (localName.equals("double")) {
			double_in = true;
			doubleCounter++;
		} else if (localName.equals("int")) {
			int_in = true;
			intCounter++;
		} else if (localName.equals("value")) {
			value_in = true;
		} else if (localName.equals("data")) {
			data_in = true;
		} else if (localName.equals("array")) {
			array_in = true;
		} else if (localName.equals("member")) {
			member_in = true;
			intCounter = 0;
			if (mode == Constants.PARSE_MODE_PLACES) {
				placesList.add(memberCounter, new PlacePoint());
			}
			if (mode == Constants.PARSE_MODE_PEOPLE) {
				doubleCounter = 0;
				peopleList.add(memberCounter, new PersonPoint());
			}
		} else if (localName.equals("struct")) {
			struct_in = true;
		} else if (localName.equals("string")) {
			string_in = true;
			stringCounter++;
		} else if (localName.equals("fault")) {
			fault_in = true;
		} else if (localName.equals("params")) {
			params_in = true;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) {
		switch (mode) {
		case Constants.PARSE_MODE_PERSON_INFO:
			if (int_in && member_in) {
				switch (intCounter) {
				case 1:
					pp.setGender(Integer.valueOf(new String(ch, start, length)));
					break;
				case 2:
					pp.setBirth(Integer.valueOf(new String(ch, start, length)));
					break;
				case 3:
					pp.setInterest(Integer.valueOf(new String(ch, start, length)));
					break;
				}
			}
			if (string_in && member_in) {
				pp.setOccupation(new String(ch, start, length));
			}
			break;
		case Constants.PARSE_MODE_PEOPLE:
			if (double_in && member_in) {
				switch (doubleCounter) {
				case 1:
					peopleList.get(memberCounter).setX((int) (Double.valueOf(new String(ch, start, length)) * 1000000));
					break;
				case 2:
					peopleList.get(memberCounter).setY((int) (Double.valueOf(new String(ch, start, length)) * 1000000));
					peopleList.get(memberCounter).setInterestPointFromXY();
					break;
				}
			}
			
			if (int_in && member_in) {
				switch (intCounter) {
				case 1:
					peopleList.get(memberCounter).setUserID(Integer.valueOf(new String(ch, start, length)));
					break;
				case 2:
					peopleList.get(memberCounter).setGender(Integer.valueOf(new String(ch, start, length)));
					break;
				case 3:
					peopleList.get(memberCounter).setInterest(Integer.valueOf(new String(ch, start, length)));
					break;
				}
			}
			break;
		case Constants.PARSE_MODE_POINT_INFO:
			if (value_in && int_in && array_in) {
				ps.setPlaceID(Integer.valueOf(new String(ch, start, length)));
			}
			if (value_in && string_in && array_in) {
				switch (stringCounter) {
				case 1:
					ps.setPlaceName(new String(ch, start, length));
					break;
				case 2:
					ps.setType(new String(ch, start, length));
					break;
				case 3:
					ps.setCategory(new String(ch, start, length));
					break;
				case 4:
					ps.setDescription(new String(ch, start, length));
					break;
				}
			}
			if (value_in && double_in && array_in) {
				ps.setRank(Double.valueOf(new String(ch, start, length)));
			}
			break;
		case Constants.PARSE_MODE_LOGIN:
			if (value_in && int_in) {
				s = new String(ch, start, length);
				loginResponse = Integer.valueOf(s);
			}
			break;
		case Constants.PARSE_MODE_LOGOUT:
			if (value_in && int_in) {
				s = new String(ch, start, length);
				logoutResponse = Integer.valueOf(s);
			}
			break;
		case Constants.PARSE_MODE_PLACES:
			if (double_in && value_in && data_in && array_in && member_in
					&& struct_in) {
				s = new String(ch, start, length);

				/*
				 * OpeGLati ovo sa case kao za int
				 */

				if (placesList.get(memberCounter).getX() == -1) {
					placesList.get(memberCounter).setX((int) (Double.valueOf(s) * 1000000));
				} else {
					placesList.get(memberCounter).setY((int) (Double.valueOf(s) * 1000000));
					placesList.get(memberCounter).setInterestPointFromXY();
				}
			}
			if (int_in && value_in && data_in && array_in && member_in
					&& struct_in) {
				s = new String(ch, start, length);
				int num = Integer.valueOf(s);

				switch (intCounter) {
				case 1:
					placesList.get(memberCounter).setPlaceID(num);
					break;
					/*
				case 2:
					placesList.get(memberCounter).setNESTO(num);
					break;
					*/
				case 3:
					placesList.get(memberCounter).setTypeID(num);
					break;
				}
			}
			if (string_in && value_in && data_in && array_in && member_in
					&& struct_in)
				placesList.get(memberCounter).setPlaceName(new String(ch, start, length));
			break;
		case Constants.PARSE_MODE_CREATE_ACCOUNT:
			// For login ok
			if (int_in && value_in && params_in)
				createAccountResponse = Integer.valueOf(new String(ch, start, length));
			
			// For username already exists
			if (string_in && value_in && member_in && fault_in)
				createAccountResponse = -1;
			break;
		}
	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		if (localName.equals("double")) {
			double_in = false;
		} else if (localName.equals("int")) {
			int_in = false;
		} else if (localName.equals("value")) {
			value_in = false;
		} else if (localName.equals("data")) {
			data_in = false;
		} else if (localName.equals("array")) {
			array_in = false;
		} else if (localName.equals("member")) {
			member_in = false;
			memberCounter++;
		} else if (localName.equals("struct")) {
			struct_in = false;
		} else if (localName.equals("string")) {
			string_in = false;
		} else if (localName.equals("fault")) {
			fault_in = false;
		} else if (localName.equals("params")) {
			params_in = false;
		}
	}

	@Override
	public void endDocument() throws SAXException {

	}

	public PlacePoint getPlacePointInfo() {
		return ps;
	}
	
	public PersonPoint getPersonPointInfo() {
		return pp;
	}

	public int getLoginResponse() {
		return loginResponse;
	}
	
	public int getCreateAccountResponse() {
		return createAccountResponse;
	}

	public int getLogoutResponse() {
		return logoutResponse;
	}

	public ArrayList<PlacePoint> getPlaces() {
		return placesList;
	}
	
	public ArrayList<PersonPoint> getPeople() {
		return peopleList;
	}
}
