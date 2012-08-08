package cn.edu.nju.software.gof.gps;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

public abstract class IAddressTask {

	protected Context context;
	
	public IAddressTask(Context context) {
		this.context = context;
	}
	
	public abstract HttpResponse execute(JSONObject params) throws Exception;
	
	public MLocation doWifiPost() throws Exception {
		return transResponse(execute(doWifi()));
	}
	
	public MLocation doApnPost() throws Exception  {
		return transResponse(execute(doApn()));
	}
	
	public MLocation doGpsPost(double lat, double lng) throws Exception {
		return transResponse(execute(doGps(lat, lng)));
	}

	private MLocation transResponse(HttpResponse response) {
		MLocation location = null;
		if (response.getStatusLine().getStatusCode() == 200) {
			location = new MLocation();
			HttpEntity entity = response.getEntity();
			BufferedReader br;
			try {
				br = new BufferedReader(new InputStreamReader(
						entity.getContent()));
				StringBuffer sb = new StringBuffer();
				String result = br.readLine();
				while (result != null) {
					sb.append(result);
					result = br.readLine();
				}
				JSONObject json = new JSONObject(sb.toString());
				JSONObject lca = json.getJSONObject("location");

				location.Access_token = json.getString("access_token");
				if (lca != null) {
					if(lca.has("accuracy"))
						location.Accuracy = lca.getString("accuracy");
					if(lca.has("longitude"))
						location.Longitude = lca.getDouble("longitude");
					if(lca.has("latitude"))
						location.Latitude = lca.getDouble("latitude");
					if(lca.has("address")) {
						JSONObject address = lca.getJSONObject("address");
						if (address != null) {
							if(address.has("region"))
								location.Region = address.getString("region");
							if(address.has("street_number"))
								location.Street_number = address
										.getString("street_number");
							if(address.has("country_code"))
								location.Country_code = address
										.getString("country_code");
							if(address.has("street"))
								location.Street = address.getString("street");
							if(address.has("city"))
								location.City = address.getString("city");
							if(address.has("country"))
								location.Country = address.getString("country");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				location = null;
			}
		}
		return location;
	}

	private JSONObject doGps(double lat, double lng) throws Exception {
		JSONObject holder = new JSONObject();
		holder.put("version", "1.1.0");
		holder.put("host", "maps.google.com");
		holder.put("address_language", "zh_CN");
		holder.put("request_address", true);
		
		JSONObject data = new JSONObject();
		data.put("latitude", lat);
		data.put("longitude", lng);
		holder.put("location", data);

		return holder;
	}
	
	private JSONObject doApn() throws Exception {
		JSONObject holder = new JSONObject();
		holder.put("version", "1.1.0");
		holder.put("host", "maps.google.com");
		holder.put("address_language", "zh_CN");
		holder.put("request_address", true);
		
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		GsmCellLocation gcl = (GsmCellLocation) tm.getCellLocation();
		int cid = gcl.getCid();
		int lac = gcl.getLac();
		int mcc = Integer.valueOf(tm.getNetworkOperator().substring(0,
				3));
		int mnc = Integer.valueOf(tm.getNetworkOperator().substring(3,
				5));
		
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
	
	private JSONObject doWifi() throws Exception {
		JSONObject holder = new JSONObject();
		holder.put("version", "1.1.0");
		holder.put("host", "maps.google.com");
		holder.put("address_language", "zh_CN");
		holder.put("request_address", true);
		
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		
		if(wifiManager.getConnectionInfo().getBSSID() == null) {
			throw new RuntimeException("bssid is null");
		}
		
		JSONArray array = new JSONArray();
		JSONObject data = new JSONObject();
		data.put("mac_address", wifiManager.getConnectionInfo().getBSSID());  
        data.put("signal_strength", 8);  
        data.put("age", 0);  
		array.put(data);
		holder.put("wifi_towers", array);
		
		return holder;
	}
}
