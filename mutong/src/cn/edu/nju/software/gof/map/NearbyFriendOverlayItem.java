package cn.edu.nju.software.gof.map;

import cn.edu.nju.software.gof.beans.FriendNearbyInformationBean;
import de.android1.overlaymanager.ManagedOverlayItem;

public class NearbyFriendOverlayItem extends ManagedOverlayItem {

	private FriendNearbyInformationBean bean;

	public NearbyFriendOverlayItem(FriendNearbyInformationBean bean) {
		super(bean.getGeoPoint(), bean.getFriendName(), bean.getTime());
		this.bean = bean;
	}

	public String getFriendID() {
		return bean.getFriendID();
	}

}
