package cn.edu.nju.software.gof.beans.json;

import java.lang.reflect.Field;

import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.appengine.repackaged.org.json.JSONString;

public abstract class JSONAble implements JSONString {

	@Override
	public String toJSONString() {
		JSONObject json = new JSONObject();
		Class<?> currentClass = this.getClass();
		Field[] fields = currentClass.getDeclaredFields();
		try {
			for (Field field : fields) {
				field.setAccessible(true);
				String fieldName = field.getName();
				Object fieldValue = field.get(this);
				if (fieldValue != null) {
					try {
						json.put(fieldName, fieldValue);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return json.toString();
	}

}
