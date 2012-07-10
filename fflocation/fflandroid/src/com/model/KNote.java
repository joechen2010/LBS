package com.model;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import model.Note;
import model.Photo;
import model.Position;

/**
 * This class is extends the server model's Note
 * in order to get a serializable Note in this application
 */
public class KNote extends Note implements KvmSerializable{

	public KNote(){}
	
	@Override
	public Object getProperty(int arg0) {
		switch(arg0){
		case 0:
			return this.getId();
		case 1:
			return this.getNote();
		case 2:
			return this.getPosition();
		case 3:
			return this.getHasPhoto();
		case 4:
			return this.getPhoto();
		case 5:
			return this.getOwner();
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 6;
	}

	@Override
	public void getPropertyInfo(int arg0, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo arg2) {
		switch(arg0){
		case 0:
			arg2.type=PropertyInfo.INTEGER_CLASS;
			arg2.name="id";
			break;
		case 1:
			arg2.type=PropertyInfo.STRING_CLASS;
			arg2.name="note";
			break;
		case 2:
			arg2.type=PropertyInfo.OBJECT_TYPE;
			arg2.name="position";
			break;
		case 3:
			arg2.type=PropertyInfo.BOOLEAN_CLASS;
			arg2.name="hasPhoto";
			break;
		case 4:
			arg2.type=PropertyInfo.OBJECT_TYPE;
			arg2.name="photo";
			break;
		case 5:
			arg2.type=PropertyInfo.INTEGER_CLASS;
			arg2.name="owner";
			break;
		}
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		switch(arg0){
		case 0:
			this.setId(Integer.parseInt(arg1.toString()));
			break;
		case 1:
			this.setNote(arg1.toString());
			break;
		case 2:
			this.setPosition((Position)arg1);
			break;
		case 3:
			this.setHasPhoto(Boolean.parseBoolean(arg1.toString()));
			break;
		case 4:
			this.setPhoto((Photo)arg1);
			break;
		case 5:
			this.setOwner(Integer.valueOf(arg1.toString()));
		}
	}


	@Override
	/**
	 * Overrides the function to return a KPosition instead of Position class.
	 */
	public KPosition getPosition(){
		return (KPosition)super.getPosition();
	}


}