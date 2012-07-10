package cn.edu.nju.software.gof.beans;

import java.util.Date;

public class CommentBean extends JSONTarget{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5654795942512518155L;
	private String ownerID;
	private String ownerName;
	private String content;
	private String time;

	public CommentBean() {
		super();
	}

	public CommentBean(String ownerID, String ownerName, String content, String time) {
		super();
		this.ownerID = ownerID;
		this.ownerName = ownerName;
		this.content = content;
		this.time = time;
	}

	public String getOwnerID() {
		return ownerID;
	}

	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
