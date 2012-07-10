package cn.edu.nju.software.gof.requests;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.nju.software.gof.beans.BreifPlaceInformationBean;
import cn.edu.nju.software.gof.beans.CommentBean;
import cn.edu.nju.software.gof.beans.PlaceCheckInfomationBean;
import cn.edu.nju.software.gof.beans.PlaceCreationBean;
import cn.edu.nju.software.gof.beans.PlaceGeneral;
import cn.edu.nju.software.gof.beans.PlaceInformationBean;
import cn.edu.nju.software.gof.beans.PlaceModification;
import cn.edu.nju.software.gof.beans.PlaceNearbyInformationBean;
import cn.edu.nju.software.gof.network.NetworkClient;

public class PlaceUtilities {

	public static List<PlaceNearbyInformationBean> getNearbyPlace(
			String sessionID, double longitude, double latitude) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.GetNearbyPlace));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.Latitude,
				String.valueOf(latitude)));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.Longitude,
				String.valueOf(longitude)));

		List<PlaceNearbyInformationBean> rList = new ArrayList<PlaceNearbyInformationBean>();
		try {
			JSONObject json = new JSONObject(NetworkClient.getAsString(parList));
			JSONArray jsonArray = json
					.getJSONArray(ServletParam.JsonParam.ListName);
			for (int i = 0; i < jsonArray.length(); i++) {
				PlaceNearbyInformationBean tempBean = new PlaceNearbyInformationBean();
				tempBean.parseJSON(jsonArray.getJSONObject(i));
				rList.add(tempBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rList;
	}

	public static PlaceInformationBean getPlaceInformation(String sessionID,
			String placeID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.GetPlaceInfo));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.PlaceID,
				placeID));

		PlaceInformationBean info = new PlaceInformationBean();
		try {
			JSONObject json = new JSONObject(NetworkClient.getAsString(parList));
			info.parseJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	public static boolean createNewPlace(String sessionID,
			PlaceCreationBean newPlace) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.CreatePlace));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.PlaceName,
				newPlace.getPlaceName()));
		parList.add(new BasicNameValuePair(
				ServletParam.RequestParam.PLACE_DESCRIPTION, newPlace
						.getPlaceDescription()));

		String parentID = newPlace.getParentID();
		if (parentID != null) {
			parList.add(new BasicNameValuePair(
					ServletParam.RequestParam.ParentID, newPlace.getParentID()));
		} else {
			parList.add(new BasicNameValuePair(
					ServletParam.RequestParam.Latitude, String.valueOf(newPlace
							.getLatitude())));
			parList.add(new BasicNameValuePair(
					ServletParam.RequestParam.Longitude, String
							.valueOf(newPlace.getLongitude())));
		}

		byte[] image = newPlace.getImage();

		return NetworkClient.postImage(parList,
				ServletParam.RequestParam.Image, image.length,
				new ByteArrayInputStream(image));
	}

	public static boolean modifyPlace(String sessionID, String placeID,
			PlaceModification modification) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.MODIFY_PLACE));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.PlaceName,
				modification.getPlaceName()));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.PlaceID,
				placeID));
		parList.add(new BasicNameValuePair(
				ServletParam.RequestParam.PLACE_DESCRIPTION, modification
						.getPlaceDescription()));

		byte[] image = modification.getImage();

		return NetworkClient.postImage(parList,
				ServletParam.RequestParam.Image, image.length,
				new ByteArrayInputStream(image));
	}

	public static boolean commentPlace(String sessionID, String placeID,
			String content) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.CommentPlace));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.PlaceID,
				placeID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.Content,
				content));

		return NetworkClient.postMessage(parList);
	}

	public static PlaceGeneral getPlaceGenetal(String sessionID, String placeID) {
		// new implemented
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.GetPlaceGeneral));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.PlaceID,
				placeID));

		PlaceGeneral placeGeneral = new PlaceGeneral();
		try {
			JSONObject json = new JSONObject(NetworkClient.getAsString(parList));
			placeGeneral.parseJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return placeGeneral;
	}

	public static boolean buyPlace(String sessionID, String placeID) {
		// new implemented
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.BuyExistedPlace));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.PlaceID,
				placeID));

		return NetworkClient.postMessage(parList);
	}

	public static List<CommentBean> getComments(String sessionID, String placeID) {
		// new implemented
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.GetPlaceReplies));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.PlaceID,
				placeID));

		List<CommentBean> rList = new ArrayList<CommentBean>();
		try {
			JSONObject json = new JSONObject(NetworkClient.getAsString(parList));
			JSONArray jsonArray = json
					.getJSONArray(ServletParam.JsonParam.ListName);
			for (int i = 0; i < jsonArray.length(); i++) {
				CommentBean tempBean = new CommentBean();
				tempBean.parseJSON(jsonArray.getJSONObject(i));
				rList.add(tempBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rList;
	}

	public static List<BreifPlaceInformationBean> getSubPlaces(
			String sessionID, String placeID) {
		// new implemented
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.GetSubPlaces));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.PlaceID,
				placeID));

		List<BreifPlaceInformationBean> rList = new ArrayList<BreifPlaceInformationBean>();
		try {
			JSONObject json = new JSONObject(NetworkClient.getAsString(parList));
			JSONArray jsonArray = json
					.getJSONArray(ServletParam.JsonParam.ListName);
			for (int i = 0; i < jsonArray.length(); i++) {
				BreifPlaceInformationBean tempBean = new BreifPlaceInformationBean();
				tempBean.parseJSON(jsonArray.getJSONObject(i));
				rList.add(tempBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rList;
	}

	public static PlaceCheckInfomationBean getPlaceCheckInfomationBean(
			String sessionID, String placeID) {

		// new implemented
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.GetPlaceCheckIn));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.PlaceID,
				placeID));

		PlaceCheckInfomationBean info = new PlaceCheckInfomationBean();
		try {
			JSONObject json = new JSONObject(NetworkClient.getAsString(parList));
			info.parseJSON(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return info;
	}

	public static boolean getPlaceImage(String sessionID, String placeID,
			File file) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.GetPlaceImage));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.PlaceID,
				placeID));

		return NetworkClient.getToFile(parList, file);
	}

	public static Long getPlaceImageCounter(String sessionID, String placeID) {
		List<NameValuePair> parList = new ArrayList<NameValuePair>();
		parList.add(new BasicNameValuePair(ServletParam.ProcessorName,
				ServletParam.GET_PLACE_IMAGE_COUNTER));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.SessionID,
				sessionID));
		parList.add(new BasicNameValuePair(ServletParam.RequestParam.PlaceID,
				placeID));
		String result = NetworkClient.getAsString(parList);

		if (result == null) {
			return null;
		} else {
			try {
				Long counter = Long.parseLong(result);
				return counter;
			} catch (Exception e) {
				return null;
			}
		}
	}
}
