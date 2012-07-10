package com.model;

import java.util.Hashtable;

import model.UserInfo;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;


/**
 * This class is extends the server model's UserInfo
 * in order to get a serializable UserInfo in this application
 */
public class KUserInfo extends UserInfo implements KvmSerializable {

    public KUserInfo() {}
    
    public KUserInfo(UserInfo ui){
    	setId(ui.getId());
    	setNick(ui.getNick());
    	setAddress(ui.getAddress());
    	setCountry(ui.getCountry());
    	setEmail(ui.getEmail());
    	setName(ui.getName());
    	setAdministrator(ui.isAdministrator());
    	setPhone(ui.getPhone());
    	setSurname(ui.getSurname());
    }
    
	@Override
	public Object getProperty(int arg0) {
		switch(arg0){
		case 0:
			return this.getId();
		case 1:
			return this.getNick();
		case 2:
			return this.getAddress();
		case 3:
			return this.getCountry();
		case 4:
			return this.getEmail();
		case 5:
			return this.getName();
		case 6:
			return this.isAdministrator();
		case 7:
			return this.getPhone();
		case 8:
			return this.getPosition();
		case 9:
			return this.getSurname();
		}
		return null;
	}
	@Override
	public int getPropertyCount() {
		return 10;
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
			arg2.name="nick";
			break;
		case 2:
			arg2.type=PropertyInfo.STRING_CLASS;
			arg2.name="address";
			break;
		case 3:
			arg2.type=PropertyInfo.STRING_CLASS;
			arg2.name="country";
			break;
		case 4:
			arg2.type=PropertyInfo.STRING_CLASS;
			arg2.name="email";
			break;
		case 5:
			arg2.type=PropertyInfo.STRING_CLASS;
			arg2.name="name";
			break;
		case 6:
			arg2.type=PropertyInfo.BOOLEAN_CLASS;
			arg2.name="administrator";
			break;
		case 7:
			arg2.type=PropertyInfo.INTEGER_CLASS;
			arg2.name="phone";
			break;
		case 8:
			arg2.type=PropertyInfo.OBJECT_TYPE;
			arg2.name="position";
			break;
		case 9:
			arg2.type=PropertyInfo.STRING_CLASS;
			arg2.name="surname";
			break;
			  
		}
	}
	@Override
	public void setProperty(int arg0, Object arg1) {
		switch(arg0){
		case 0:
			this.setId((Integer)arg1);
			break;
		case 1:
			this.setNick((String)arg1);
			break;
		case 2:
			this.setAddress((String)arg1);
			break;
		case 3:
			this.setCountry((String)arg1);
			break;
		case 4:
			this.setEmail((String)arg1);
			break;
		case 5:
			this.setName((String)arg1);
			break;
		case 6:
			this.setAdministrator((Boolean)arg1);
			break;
		case 7:
			this.setPhone((Integer)arg1);
			break;
		case 8:
			this.setPosition((KPosition)arg1);
			break;
		case 9:
			this.setSurname((String)arg1);
			break;
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
