package cn.edu.nju.software.gof.viewbeans;

import java.io.InputStream;

import cn.edu.nju.software.gof.beans.FriendInformationBean;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;

public class FriendInfo {

	private FriendInformationBean friendInfo;
	private BitmapDrawable avatar;

	public FriendInfo() {
		super();
	}

	public FriendInfo(FriendInformationBean friendInfo) {
		super();
		this.friendInfo = friendInfo;
	}

	public String getFriendID() {
		return friendInfo.getFriendID();
	}

	public String getFriendRealName() {
		return friendInfo.getFriendRealName();
	}

	public String getFriendState() {
		return friendInfo.getFriendState();
	}

	public String getIpAddress() {
		return friendInfo.getIpAddress();
	}

	public String getIpPort() {
		return friendInfo.getIpPort();
	}

	public String getLastPersonalLocation() {
		return friendInfo.getLastCheckinLocation();
	}

	public BitmapDrawable getAvatar() {
		return avatar;
	}

	public void setAvatar(InputStream avatarStream, Resources resources) {
		if (avatarStream != null) {
			avatar = new BitmapDrawable(resources, avatarStream);
		}
	}
}
