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
 * Point class drown on a map that represent some person
 */

public class PersonPoint extends InterestPoint {

	private int userID;
	private String uname;
	private int gender;
	private int birth;
	private String occupation;
	private int interest;
	
	PersonPoint() {
		
	}
	
	// Setters
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public void setUserName(String uname) {
		this.uname = uname;
	}
	
	public void setGender(int gender) {
		this.gender = gender;
	}
	
	public void setBirth(int birth) {
		this.birth = birth;
	}
	
	public void  setOccupation(String occupation) {
		this.occupation = occupation;
	}
	
	public void setInterest(int interest) {
		this.interest = interest;
	}
	
	// Getters
	
	public int getUserID() {
		return userID;
	}
	
	public String getUserName() {
		return uname;
	}
	
	public int getGender() {
		return gender;
	}
	
	public int getBirth() {
		return birth;
	}
	
	public String  getOccupation() {
		return occupation;
	}
	
	public int getInterest() {
		return interest;
	}
}
