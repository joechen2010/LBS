package cn.edu.nju.software.gof.activity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.app.Application;

public class MyApplication extends Application {

	private Map<String, Object> datas = new HashMap<String, Object>();
	private Set<String> places = new HashSet<String>();

	public Object getData(String name) {
		return datas.get(name);
	}

	public void setData(String name, Object value) {
		datas.put(name, value);
	}
	
	public void addCheckedPlace(String placeID){
		places.add(placeID);
	}
	
	public boolean hasChecked(String placeID){
		if(places.contains(placeID)){
			return true;
		}else{
			return false;
		}
	}

}
