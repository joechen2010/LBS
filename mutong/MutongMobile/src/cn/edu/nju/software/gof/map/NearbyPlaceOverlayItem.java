package cn.edu.nju.software.gof.map;

import cn.edu.nju.software.gof.beans.PlaceNearbyInformationBean;
import de.android1.overlaymanager.ManagedOverlayItem;

public class NearbyPlaceOverlayItem extends ManagedOverlayItem {

	private PlaceNearbyInformationBean bean;

	public NearbyPlaceOverlayItem(PlaceNearbyInformationBean bean) {
		super(bean.getGeoPoint(), bean.getPalceName(), null);
		this.bean = bean;
	}

	public String getPlaceID() {
		return bean.getPlaceID();
	}
}
