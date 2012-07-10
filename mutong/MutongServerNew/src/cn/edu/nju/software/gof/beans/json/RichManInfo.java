package cn.edu.nju.software.gof.beans.json;

import java.util.LinkedList;
import java.util.List;

public class RichManInfo extends JSONAble {

	private Long money;
	private List<BriefPlaceInfo> places = new LinkedList<BriefPlaceInfo>();

	public RichManInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RichManInfo(Long money) {
		super();
		this.money = money;
	}

	public Long getMoney() {
		return money;
	}

	public void setMoney(Long money) {
		this.money = money;
	}

	public List<BriefPlaceInfo> getPlaces() {
		return places;
	}

	public void setPlaces(List<BriefPlaceInfo> places) {
		this.places = places;
	}

	public void addPlace(BriefPlaceInfo place) {
		places.add(place);
	}

}
