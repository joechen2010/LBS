package com.model;


import java.io.ByteArrayInputStream;
import java.util.Hashtable;

import model.Photo;

import org.kobjects.base64.Base64;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import android.graphics.drawable.BitmapDrawable;


/**
 * This class is extends the server model's Photo
 * in order to get a serializable Photo in this application
 */
public class KPhoto extends Photo implements KvmSerializable{
	
	public KPhoto(){}
	
	@Override
	public Object getProperty(int arg0) {
		switch(arg0){
		case 0:
			return Base64.encode(this.getPhoto());
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 1;
	}

	@Override
	public void getPropertyInfo(int arg0, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo arg2) {
		switch(arg0){
		case 1:
			arg2.type=PropertyInfo.STRING_CLASS;
			arg2.name="photo";
			break;
		}
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		switch(arg0){
		case 0:
			this.setPhoto(Base64.decode(arg1.toString()));
			break;
		}
	}
	
	public String toString(){
		return "{" + Base64.encode(this.getPhoto()).subSequence(0, 10) + "}";
	}
	
	/**
	 * This function converts the photo into a BitmapDrawable
	 * @return The BitmapDrawable object representing the photo
	 */
	public BitmapDrawable getBitMap(){
		return new BitmapDrawable(new ByteArrayInputStream(super.getPhoto()));
	}

}
