package com.model;


import java.util.Hashtable;

import model.Position;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import com.google.android.maps.GeoPoint;


/**
 * This class is extends the server model's Position
 * in order to get a serializable Position in this application
 */
public class KPosition extends Position implements KvmSerializable{
	
	@Override
	public Object getProperty(int arg0) {
		switch(arg0){
		case 0:
			return this.getLatitude();
		case 1:
			return this.getLongitude();
		case 2:
			return this.getDate();
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 3;
	}

	@Override
	public void getPropertyInfo(int arg0, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo arg2) {
		switch(arg0){
		case 0:
			arg2.type=PropertyInfo.STRING_CLASS;
			arg2.name="latitude";
			break;
		case 1:
			arg2.type=PropertyInfo.STRING_CLASS;
			arg2.name="longitude";
			break;
		case 2:
			arg2.type=PropertyInfo.STRING_CLASS;
			arg2.name="date";
			break;
		}
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		switch(arg0){
		case 0:
			this.setLatitude(Float.valueOf(arg1.toString()));
			break;
		case 1:
			this.setLongitude(Float.valueOf(arg1.toString()));
			break;
		case 2:
			this.setDate(arg1.toString());
			break;
		}
	}
	
	/**
	 * @return the latitude as a float.
	 */
	public Float getLatitudeFloat() {
		return Float.parseFloat(this.getLatitude());
	}
	/**
	 * 
	 * @return the longitude as a float.
	 */
	public Float getLongitudeFloat() {
		return Float.parseFloat(this.getLongitude());
	}

	/**
	 * Calculates the euclidian distance between two positions.
	 * @param p the position to be compared to.
	 * @return the distance between this position and p position.
	 */
	public float calcDistance(KPosition p){
		return (float) Math.sqrt(
			Math.pow((this.getLatitudeFloat()-p.getLatitudeFloat()),2)
			+
			Math.pow((this.getLongitudeFloat()-p.getLongitudeFloat()),2)
			);
		
	}
	
	/**
	 * @return a geopoint representing the position.
	 */
	public GeoPoint getGeoPoint()
	{
		return new GeoPoint((int)(this.getLatitudeFloat()*1E6), (int)(this.getLongitudeFloat()*1E6));
	}
	
	
}

