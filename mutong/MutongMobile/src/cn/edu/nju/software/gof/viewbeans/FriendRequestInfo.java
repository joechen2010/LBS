package cn.edu.nju.software.gof.viewbeans;

import java.io.InputStream;
import java.io.Serializable;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import cn.edu.nju.software.gof.beans.FriendRequestBean;

public class FriendRequestInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4925512465326862087L;
	private FriendRequestBean friendRequest;
	private BitmapDrawable avatar;

	public FriendRequestInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FriendRequestInfo(FriendRequestBean friendRequest) {
		super();
		this.friendRequest = friendRequest;
	}

	public String getFriendID() {
		return friendRequest.getRequesterID();
	}

	public String getFriendRealName() {
		return friendRequest.getRealName();
	}

	public String getFriendUserName() {
		return friendRequest.getUserName();
	}

	public String getRequestID() {
		return friendRequest.getID();
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
