package cn.edu.nju.software.gof.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.nju.software.manager.PlaceManager;
import cn.edu.nju.software.manager.ReplyManager;
import cn.edu.nju.software.util.SpringContextHolder;

public class Place {

	public static final Long START_MONEY = 256L;

	private Long id;

	private String placeName;

	private Double latitude;

	private Double longutide;

	private Long creatorId;

	private Long topUserId;
	
	private Long parentId; 

	private Long checkInTimes = 0L;

	private Long currentMoney = START_MONEY;

	private byte[] image;
	
	private Person creator;
	
	private Person topUser;
	
	private Place Parent;
	
	private Date create_Time;

	private List<Reply> replies = new ArrayList<Reply>();

	private List<Place> subPlaces;

	public Place() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Place(String placeName, Long creatorId) {
		super();
		this.placeName = placeName;
		this.creatorId = creatorId;
	}

	public Place(String placeName, Double latitude, Double longutide,
			Long creator) {
		super();
		this.placeName = placeName;
		this.latitude = latitude;
		this.longutide = longutide;
		this.creatorId = creator;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public void increaseCheckInTimes() {
		checkInTimes++;
	}

	public Long getCurrentMoney() {
		return currentMoney;
	}

	public void setCurrentMoney(Long currentMoney) {
		this.currentMoney = currentMoney;
	}

	public Long getCheckInTimes() {
		return checkInTimes;
	}

	public void setCheckInTimes(Long checkInTimes) {
		this.checkInTimes = checkInTimes;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Long getTopUserId() {
		return topUserId;
	}

	public void setTopUserId(Long topUserId) {
		this.topUserId = topUserId;
	}

	public static Long getStartMoney() {
		return START_MONEY;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongutide() {
		return longutide;
	}

	public void setLongutide(Double longutide) {
		this.longutide = longutide;
	}

	public List<Reply> getReplies() {
		if(replies == null){
			ReplyManager replyManager = SpringContextHolder.getBean("replyManager");
			replies = replyManager.findBypalceId(id);
		}
		return replies;
	}

	public void setReplies(List<Reply> replies) {
		this.replies = replies;
	}

	public List<Place> getSubPlaces() {
		if(subPlaces == null){
			PlaceManager placeManager = SpringContextHolder.getBean("placeManager");
			subPlaces = placeManager.findSubPlacesById(id);
		}
		return subPlaces;
	}

	public void setSubPlaces(List<Place> subPlaces) {
		this.subPlaces = subPlaces;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Person getCreator() {
		return creator;
	}

	public void setCreator(Person creator) {
		this.creator = creator;
	}

	public Person getTopUser() {
		return topUser;
	}

	public void setTopUser(Person topUser) {
		this.topUser = topUser;
	}

	public Date getCreate_Time() {
		return create_Time;
	}

	public void setCreate_Time(Date create_Time) {
		this.create_Time = create_Time;
	}

	public Place getParent() {
		return Parent;
	}

	public void setParent(Place parent) {
		Parent = parent;
	}
	
}
