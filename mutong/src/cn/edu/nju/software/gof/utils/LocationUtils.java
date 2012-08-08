package cn.edu.nju.software.gof.utils;

import org.json.JSONArray;
import org.json.JSONObject;

public class LocationUtils {
	
	public static JSONObject getGpsParams(double lat, double lng) throws Exception {
		JSONObject holder = createJsonObject();
		JSONObject data = new JSONObject();
		data.put("latitude", lat);
		data.put("longitude", lng);
		holder.put("location", data);
		return holder;
	}
	
	public static JSONObject getApnParams(int cid, int lac, int mcc, int mnc) throws Exception {
		JSONObject holder = createJsonObject();
		JSONArray array = new JSONArray();
		JSONObject data = new JSONObject();
		data.put("cell_id", cid);
		data.put("location_area_code", lac);
		data.put("mobile_country_code", mcc);
		data.put("mobile_network_code", mnc);
		array.put(data);
		holder.put("cell_towers", array);
		
		return holder;
	}
	
	public static JSONObject getWifiParams(String bssid) throws Exception {
		JSONObject holder = createJsonObject();
		
		JSONArray array = new JSONArray();
		JSONObject data = new JSONObject();
		data.put("mac_address", bssid);  
        data.put("signal_strength", 8);  
        data.put("age", 0);  
		array.put(data);
		holder.put("wifi_towers", array);
		
		return holder;
	}

	
	public static JSONObject createJsonObject()  throws Exception {
		JSONObject holder = new JSONObject();
		holder.put("version", "1.1.0");
		holder.put("host", "maps.google.com");
		holder.put("address_language", "zh_CN");
		holder.put("request_address", true);
		return holder;
	}
}
