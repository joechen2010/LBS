package cn.edu.nju.software.gof.viewbeans;

import java.io.InputStream;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;

public class BriefFriendInfo {

	private String ID;
	private String userName;
	private String realName;
	private BitmapDrawable avatar;

	public BriefFriendInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BriefFriendInfo(String iD, String userName, String realName) {
		super();
		ID = iD;
		this.userName = userName;
		this.realName = realName;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
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
