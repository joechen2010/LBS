package cn.edu.nju.software.gof.viewbeans;

import java.io.InputStream;

import cn.edu.nju.software.gof.beans.PersonInformationBean;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;

public class PersonalInfo {
	private String realName;
	private String birthday;
	private String school;
	private String place;
	private BitmapDrawable avatar;

	public PersonalInfo() {
		super();
	}
	
	public PersonalInfo(PersonInformationBean bean) {
		this.realName = bean.getRealName();
		this.birthday = bean.getBirthday();
		this.school = bean.getSchool();
		this.place = bean.getPlace();
	}

	public PersonalInfo(String realName, String birthday, String school,
			String place) {
		super();
		this.realName = realName;
		this.birthday = birthday;
		this.school = school;
		this.place = place;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public BitmapDrawable getAvatar() {
		return avatar;
	}

	public void setAvatar(Resources resources, InputStream in) {
		if (in != null) {
			avatar = new BitmapDrawable(resources, in);
		}
	}
}
