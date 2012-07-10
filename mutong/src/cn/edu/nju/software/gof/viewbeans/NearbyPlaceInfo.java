package cn.edu.nju.software.gof.viewbeans;

import java.io.InputStream;
import java.io.Serializable;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import cn.edu.nju.software.gof.beans.BreifPlaceInformationBean;
import cn.edu.nju.software.gof.beans.PlaceNearbyInformationBean;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class NearbyPlaceInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1324167997057417134L;
	private PlaceNearbyInformationBean placeInfo = null;
	private BreifPlaceInformationBean breifInfo = null;
	private BitmapDrawable avatar = null;

	public NearbyPlaceInfo(PlaceNearbyInformationBean bean,
			BreifPlaceInformationBean breifInfo, BitmapDrawable avatar) {
		placeInfo = bean;
		this.breifInfo = breifInfo;
		this.avatar = avatar;
	}

	public String getPlaceID() {
		String id = null;
		if(placeInfo == null){
			id = breifInfo.getID();
		}else{
			id = placeInfo.getPlaceID();
		}
		return id;
	}

	public String getPlaceName() {
		String name = null;
		if(placeInfo == null){
			name = breifInfo.getPlaceName();
		}else{
			name = placeInfo.getPalceName();
		}
		return name;
	}

	public GeoPoint getPoint() {
		Double latitude = placeInfo.getLatitude() * 1E6;
		Double longitude = placeInfo.getLongitude() * 1E6;
		return new GeoPoint(latitude.intValue(), longitude.intValue());
	}

	public void setAvatar(InputStream avatarStream, Resources resources) {
		if (avatarStream != null) {
			avatar = new BitmapDrawable(resources, avatarStream);
		}
	}
	
	public BitmapDrawable getAvatar(){
		return this.avatar;
	}
	
	public String getCurrentMoney(){
		String out = null;
		if(this.breifInfo == null){
			double lat = this.placeInfo.getLatitude();
			double lon = this.placeInfo.getLongitude();
			out = "经伟度："+Double.toString(lat)+" "+Double.toString(lon);
		}else{
			out = "价格："+Long.toString(this.breifInfo.getCurrentMoney());
		}
		return out;
	}

}
