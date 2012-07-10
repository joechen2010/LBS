package cn.edu.nju.software.gof.viewbeans;

import java.io.InputStream;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import cn.edu.nju.software.gof.beans.CommentBean;

public class CommentWithAvatarBean {
	
	private CommentBean commentbean = null;
	private BitmapDrawable avatar;
	
	public CommentWithAvatarBean(CommentBean comment){
		this.commentbean = comment;
	}
	
	public String getOwnerID() {
		return this.commentbean.getOwnerID();
	}

	public void setOwnerID(String ownerID) {
		this.commentbean.setOwnerID(ownerID);
	}

	public String getOwnerName() {
		return this.commentbean.getOwnerName();
	}

	public void setOwnerName(String ownerName) {
		this.commentbean.setOwnerName(ownerName);
	}

	public String getContent() {
		return this.commentbean.getContent();
	}

	public void setContent(String content) {
		this.commentbean.setContent(content);
	}

	public String getTime() {
		return this.commentbean.getTime();
	}

	public void setTime(String time) {
		this.commentbean.setTime(time);
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
