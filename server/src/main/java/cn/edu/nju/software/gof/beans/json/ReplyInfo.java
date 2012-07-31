package cn.edu.nju.software.gof.beans.json;

import java.util.Date;

public class ReplyInfo extends JSONAble {

	private String ownerID;
	private String ownerName;
	private String content;
	private Date time;

	public ReplyInfo() {
		super();
	}

	public ReplyInfo(String ownerID, String ownerName, String content, Date time) {
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

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
