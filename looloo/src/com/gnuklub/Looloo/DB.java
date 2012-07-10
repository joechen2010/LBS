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
 * Sqlite manipulation where we store user data
 */

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DB {

	private static final String CREATE_TABLE_UP = "create table USER_PREFERENCES (USERID integer primary key, "
			+ "USERNAME text not null, PASSWORD text not null, GENDER integer not null, "
			+ "DOB int, OCCUPATION text, EMAIL text, "
			+ "PLACES_OF_INTEREST integer not null,"
			+ "PREFERENCES integer not null);";
	private static final String CREATE_TABLE_TOP = "create table TYPES_OF_PLACES (TYPEID integer primary key, "
			+ "PARENTID integer, TYPE_NAME text not null);";
	private static final String CREATE_TABLE_MP = "create table MY_PLACES (PLACEID integer primary key, "
			+ "TYPEID integer not null, X real not null, Y real not null, NAME text not null, "
			+ "RATING real, DESCRIPTION text);";
	private static final String CREATE_TABLE_S = "create table SETTINGS (UPDATE_INTERVAL integer not null, "
			+ "SEARCH_RADIUS integer not null, MATCHING_PERCENTAGE integer not null);";

	private static final String DATABASE_NAME = "looloodb";

	private static final String userPreferences = "USER_PREFERENCES";
	private static final String typesOfPlaces = "TYPES_OF_PLACES";
	// private static final String myPlaces = "MY_PLACES";
	private static final String settings = "SETTINGS";
	private static final int DATABASE_VERSION = 1;

	private SQLiteDatabase db;

	public DB(Context ctx) {
		try {
			db = ctx.openDatabase(DATABASE_NAME, null);
		} catch (FileNotFoundException e) {
			try {
				db = ctx.createDatabase(DATABASE_NAME, DATABASE_VERSION, 0,
						null);
				db.execSQL(CREATE_TABLE_UP);
				db.execSQL(CREATE_TABLE_TOP);
				db.execSQL(CREATE_TABLE_MP);
				db.execSQL(CREATE_TABLE_S);

				ContentValues iv = new ContentValues();
				iv.put("USERID", 0);
				iv.put("USERNAME", "NULL");
				iv.put("PASSWORD", "NULL");
				iv.put("GENDER", 0);
				iv.put("DOB", 0);
				iv.put("OCCUPATION", "NULL");
				iv.put("EMAIL", "NULL");
				iv.put("PLACES_OF_INTEREST", 0);
				iv.put("PREFERENCES", 0);
				db.insert(userPreferences, null, iv);

				iv = new ContentValues();
				iv.put("TYPEID", 10);
				iv.put("PARENTID", 10);
				iv.put("TYPE_NAME", "Restorants");
				db.insert(typesOfPlaces, null, iv);

				iv = new ContentValues();
				iv.put("TYPEID", 100);
				iv.put("PARENTID", 10);
				iv.put("TYPE_NAME", "Pizza");
				db.insert(typesOfPlaces, null, iv);

				iv = new ContentValues();
				iv.put("TYPEID", 101);
				iv.put("PARENTID", 10);
				iv.put("TYPE_NAME", "Sushi");
				db.insert(typesOfPlaces, null, iv);

				iv = new ContentValues();
				iv.put("TYPEID", 11);
				iv.put("PARENTID", 11);
				iv.put("TYPE_NAME", "Night bars");
				db.insert(typesOfPlaces, null, iv);

				iv = new ContentValues();
				iv.put("TYPEID", 100);
				iv.put("PARENTID", 11);
				iv.put("TYPE_NAME", "Discoteques");
				db.insert(typesOfPlaces, null, iv);

				iv = new ContentValues();
				iv.put("TYPEID", 101);
				iv.put("PARENTID", 11);
				iv.put("TYPE_NAME", "Caffe bars");
				db.insert(typesOfPlaces, null, iv);

				iv = new ContentValues();
				iv.put("TYPEID", 102);
				iv.put("PARENTID", 11);
				iv.put("TYPE_NAME", "Billiard clubs");
				db.insert(typesOfPlaces, null, iv);

				iv = new ContentValues();
				iv.put("TYPEID", 12);
				iv.put("PARENTID", 12);
				iv.put("TYPE_NAME", "Monuments");
				db.insert(typesOfPlaces, null, iv);

				iv = new ContentValues();
				iv.put("TYPEID", 100);
				iv.put("PARENTID", 12);
				iv.put("TYPE_NAME", "All monuments");
				db.insert(typesOfPlaces, null, iv);

				iv = new ContentValues();
				iv.put("TYPEID", 13);
				iv.put("PARENTID", 13);
				iv.put("TYPE_NAME", "Sport places");
				db.insert(typesOfPlaces, null, iv);

				iv = new ContentValues();
				iv.put("TYPEID", 100);
				iv.put("PARENTID", 13);
				iv.put("TYPE_NAME", "Sports arenas");
				db.insert(typesOfPlaces, null, iv);

				iv = new ContentValues();
				iv.put("TYPEID", 101);
				iv.put("PARENTID", 13);
				iv.put("TYPE_NAME", "Tennis courts");
				db.insert(typesOfPlaces, null, iv);

				iv = new ContentValues();
				iv.put("TYPEID", 102);
				iv.put("PARENTID", 13);
				iv.put("TYPE_NAME", "Recreational parks");
				db.insert(typesOfPlaces, null, iv);

				iv = new ContentValues();
				iv.put("TYPEID", 14);
				iv.put("PARENTID", 14);
				iv.put("TYPE_NAME", "Educational places");
				db.insert(typesOfPlaces, null, iv);

				iv = new ContentValues();
				iv.put("TYPEID", 100);
				iv.put("PARENTID", 14);
				iv.put("TYPE_NAME", "Universities");
				db.insert(typesOfPlaces, null, iv);

				iv = new ContentValues();
				iv.put("TYPEID", 101);
				iv.put("PARENTID", 14);
				iv.put("TYPE_NAME", "Colleges");
				db.insert(typesOfPlaces, null, iv);

				iv = new ContentValues();
				iv.put("TYPEID", 102);
				iv.put("PARENTID", 14);
				iv.put("TYPE_NAME", "High schools");
				db.insert(typesOfPlaces, null, iv);

				iv = new ContentValues();
				iv.put("TYPEID", 15);
				iv.put("PARENTID", 15);
				iv.put("TYPE_NAME", "Shopping");
				db.insert(typesOfPlaces, null, iv);

				iv = new ContentValues();
				iv.put("TYPEID", 100);
				iv.put("PARENTID", 15);
				iv.put("TYPE_NAME", "Shopping malls");
				db.insert(typesOfPlaces, null, iv);

				iv = new ContentValues();
				iv.put("TYPEID", 101);
				iv.put("PARENTID", 15);
				iv.put("TYPE_NAME", "Open markets");
				db.insert(typesOfPlaces, null, iv);

				// --- Default settings

				iv = new ContentValues();
				iv.put("UPDATE_INTERVAL", 30);
				iv.put("SEARCH_RADIUS", 1000);
				iv.put("MATCHING_PERCENTAGE", 20);
				db.insert(settings, null, iv);

			} catch (FileNotFoundException e1) {
				db = null;
			}
		}
	}

	public void close() {
		db.close();
	}

	public String getSetting(String rowName) {
		try {
			Cursor c = db.query(settings, new String[] { rowName }, null, null,
					null, null, null);
			c.first();
			return c.getString(0);
		} catch (SQLException e) {
			Log.e("SQLEXception", e.getMessage());
			return null;
		}
	}

	public void setSetting(String rowName, int value) {
		ContentValues iv = new ContentValues();
		iv.put(rowName, value);
		db.update(settings, iv, null, null);
	}
	
	public int getPeoplePreferences() {
		try {
			Cursor c = db.query(userPreferences, new String[] { "PREFERENCES" }, null, null,
					null, null, null);
			c.first();
			return c.getInt(0);
		} catch (SQLException e) {
			Log.e("SQLEXception", e.getMessage());
			return -1;
		}
	}

	public void setUserPrefs(Integer userID, String username, String password,
			Integer gender, Integer dob, String occupation, String email) {
		ContentValues iv = new ContentValues();
		iv.put("USERID", userID);
		iv.put("USERNAME", username);
		iv.put("PASSWORD", password);
		iv.put("GENDER", gender);
		iv.put("DOB", dob);
		iv.put("OCCUPATION", occupation);
		iv.put("EMAIL", email);

		db.update(userPreferences, iv, null, null);
	}

	public void setPlacesOfInterest(Integer placesOfInterest) {
		ContentValues iv = new ContentValues();
		iv.put("PLACES_OF_INTEREST", placesOfInterest);

		db.update(userPreferences, iv, null, null);
	}

	public void setPreferences(Integer preferences) {
		ContentValues iv = new ContentValues();
		iv.put("PREFERENCES", preferences);

		db.update(userPreferences, iv, null, null);
	}

	public List<Row> getUserPrefs() {
		ArrayList<Row> result = new ArrayList<Row>();

		try {
			Cursor c = db.query(userPreferences, new String[] { "USERID",
					"USERNAME", "PASSWORD", "GENDER", "DOB", "OCCUPATION",
					"EMAIL", "PLACES_OF_INTEREST", "PREFERENCES" }, null, null,
					null, null, null);
			int numRows = c.count();
			c.first();
			for (int i = 0; i < numRows; ++i) {
				Row row = new Row();
				row.userID = c.getInt(0);
				row.username = c.getString(1);
				row.password = c.getString(2);
				row.gender = c.getInt(3);
				row.dob = c.getInt(4);
				row.occupation = c.getString(5);
				row.email = c.getString(6);
				row.placesOfInterest = c.getInt(7);
				row.preferences = c.getInt(8);

				result.add(row);
				c.next();
			}
		} catch (SQLException e) {
			Log.v("booga", e.toString());
		}
		return result;
	}

}