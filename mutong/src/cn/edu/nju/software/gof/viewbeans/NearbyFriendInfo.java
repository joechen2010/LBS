package cn.edu.nju.software.gof.viewbeans;

import java.io.InputStream;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import cn.edu.nju.software.gof.beans.FriendNearbyInformationBean;

public class NearbyFriendInfo {
	
	private FriendNearbyInformationBean infoBean;
	private BitmapDrawable avatar;
	
	public NearbyFriendInfo(FriendNearbyInformationBean info){
		this.infoBean = info;
	}
	
	public String getFriendID() {
		return infoBean.getFriendID();
	}

	public String getFriendName() {
		return infoBean.getFriendName();
	}

	public String getTime() {
		return infoBean.getTime();
	}
	
	public BitmapDrawable getAvatar(){
		return this.avatar;
	}
	
	public void setAvatar(InputStream avatarStream, Resources resources) {
		if (avatarStream != null) {
			avatar = new BitmapDrawable(resources, avatarStream);
		}
	}
}
