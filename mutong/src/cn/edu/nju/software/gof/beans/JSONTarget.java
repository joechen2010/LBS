/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.nju.software.gof.beans;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author lidejia
 */
public abstract class JSONTarget implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5161681110486160864L;

	public void parseJSON(JSONObject root) {
		Class<?> prototype = this.getClass();
		Field[] fields = prototype.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String fieldName = field.getName();
			Class<?> type = field.getType();
			if (type.isPrimitive()) {
				if (isDoubleType(type)) {
					try {
						Double value = root.getDouble(fieldName);
						field.set(this, value);
					} catch (Exception ex) {
					}
				} else if (isIntegerType(type)) {
					try {
						Integer value = root.getInt(fieldName);
						field.set(this, value);
					} catch (Exception e) {
					}

				}
			} else {
				if (isStringType(type)) {
					try {
						String value = root.getString(fieldName);
						field.set(this, value);
					} catch (Exception ex) {
					}
				} else if (isListType(type)) {
					try {
						@SuppressWarnings("rawtypes")
						List list = (List) field.get(this);
						Class<?> genericType = getGenericType(type, list);
						JSONArray array = root.getJSONArray(fieldName);
						for (int i = 0; i < array.length(); i++) {
							Constructor<?> constructor = genericType
									.getConstructor();
							Object item = constructor.newInstance();
							@SuppressWarnings({ "unused", "unchecked" })
							boolean success = list.add(item);
							JSONObject json = array.getJSONObject(i);
							Method method = item.getClass().getMethod(
									"parseJSON", JSONObject.class);
							method.invoke(item, json);
						}
					} catch (Exception ex) {
						System.out.println(ex);
					}
				} else if (isLongType(type)) {
					Long value;
					try {
						value = root.getLong(fieldName);
						field.set(this, value);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {
						JSONObject json = root.getJSONObject(fieldName);
						Constructor<?> constructor = type.getConstructor();
						Object item = constructor.newInstance();
						field.set(this, item);
						Method method = item.getClass().getMethod("parseJSON",
								JSONObject.class);
						method.invoke(item, json);
					} catch (Exception ex) {
					}
				}
			}
		}
	}

	private boolean isLongType(Class<?> type) {
		return type.getName().equals(Long.class.getName())
				|| type.getName().equals(long.class.getName());
	}

	private boolean isListType(Class<?> type) {

		return type.getName().equals(List.class.getName());
	}

	private boolean isStringType(Class<?> type) {
		return type.getName().equals(String.class.getName());
	}

	private boolean isDoubleType(Class<?> type) {
		return type.getName().equals(Double.class.getName())
				|| type.getName().equals(double.class.getName());
	}

	private boolean isIntegerType(Class<?> type) {
		return type.getName().equals(Integer.class.getName())
				|| type.getName().equals(int.class.getName());
	}

	private Class<?> getGenericType(Class<?> listType,
			@SuppressWarnings("rawtypes") List list) throws Exception {
		Method method = listType.getMethod("get", int.class);
		Class<?> genericType = method.invoke(list, 0).getClass();
		method = listType.getMethod("remove", int.class);
		method.invoke(list, 0);
		return genericType;
	}
}
