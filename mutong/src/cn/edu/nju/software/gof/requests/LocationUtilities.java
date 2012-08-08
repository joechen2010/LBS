package cn.edu.nju.software.gof.requests;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.edu.nju.software.gof.beans.DetectType;
import cn.edu.nju.software.gof.beans.FriendInformationBean;
import cn.edu.nju.software.gof.beans.User;
import cn.edu.nju.software.gof.beans.UserLocation;
import cn.edu.nju.software.gof.gps.Address;
import cn.edu.nju.software.gof.network.NetworkClient;
import cn.edu.nju.software.gof.utils.LocationUtils;
import cn.edu.nju.software.gof.web.HttpUtils;
import cn.edu.nju.software.gof.web.JsonMapper;

public class LocationUtilities {

	private static final JsonMapper mapper             = JsonMapper.buildNormalMapper();
	public static final String location_url = "http://74.125.71.147/loc/json";
	
	public static boolean saveUserLocation(User user) {
		  cn.edu.nju.software.gof.gps.Location location = purseLocationDetail(user, DetectType.GPS);
		  String addr = (location == null || location.getLocation().getAddress() == null ) ? 
				  "" : location.getLocation().getAddress().getAddreStr();
		
		
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,ServletParam.USERLOCATION));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.Action,ServletParam.RequestParam.ADD));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.ADDRESS, addr));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.MOBILE, user.getMobile()));
		if(location != null && location.getLocation().getAddress() != null){
			  Address address = user.getLocation().getLocation().getAddress();
			  parList.add(new BasicNameValuePair(ServletParam.RequestParam.CITY, address.getCity()));
			  parList.add(new BasicNameValuePair(ServletParam.RequestParam.STREET, address.getStreet()+address.getStreet_number()));
			  parList.add(new BasicNameValuePair(ServletParam.RequestParam.LATITUDE, user.getLocation().getLocation().getLatitude()));
			  parList.add(new BasicNameValuePair(ServletParam.RequestParam.LONGITUDE, user.getLocation().getLocation().getLongitude()));
			  parList.add(new BasicNameValuePair(ServletParam.RequestParam.CITY, address.getCity()));
		}
		return NetworkClient.postMessage(parList);
	}

	public static List<UserLocation> getUserLocations(String sessionID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,ServletParam.USERLOCATION));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.Action,ServletParam.RequestParam.SELECT));
		List<UserLocation> rList = new ArrayList<UserLocation>();
		try {
			JSONObject json = new JSONObject(NetworkClient.getAsString(parList));
			JSONArray jsonArray = json.getJSONArray(ServletParam.JsonParam.ListName);
			for (int i = 0; i < jsonArray.length(); i++) {
				UserLocation tempBean = new UserLocation();
				tempBean.parseJSON(jsonArray.getJSONObject(i));
				rList.add(tempBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rList;
	}
	
	public static cn.edu.nju.software.gof.gps.Location purseLocationDetail(User user,DetectType type){
		 String params = "";
		try {
			if(DetectType.GPS.equals(type))
				params = LocationUtils.getGpsParams(user.getLat(), user.getLng()).toString();
			if(DetectType.VPN.equals(type))
				params = LocationUtils.getApnParams(user.getCid(), user.getLac(), user.getMcc(), user.getMnc()).toString();
			else if(DetectType.WIFI.equals(type))
				params = LocationUtils.getWifiParams(user.getBssid()).toString();
		} catch (Exception e) {
		}
		 //String json = HttpUtils.post(HttpUtils.location_url, params , "cngzip01.mgmt.ericsson.se",8080);
		 String json = HttpUtils.post(location_url, params);
		 cn.edu.nju.software.gof.gps.Location location = mapper.fromJson(json, cn.edu.nju.software.gof.gps.Location.class);
		 user.setLocation(location);
		 return location;
	 }
}
