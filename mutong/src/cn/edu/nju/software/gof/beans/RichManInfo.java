package cn.edu.nju.software.gof.beans;

import java.util.LinkedList;
import java.util.List;

public class RichManInfo extends JSONTarget {

	private Long money;
	private List<BreifPlaceInformationBean> places = new LinkedList<BreifPlaceInformationBean>();

	public RichManInfo() {
		super();
		places.add(new BreifPlaceInformationBean("10", "Nanjing0", new Long(256)));
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

	public List<BreifPlaceInformationBean> getPlaces() {
		return places;
	}

	public void setPlaces(List<BreifPlaceInformationBean> places) {
		this.places = places;
	}

	public void addPlace(BreifPlaceInformationBean place) {
		places.add(place);
	}

}
