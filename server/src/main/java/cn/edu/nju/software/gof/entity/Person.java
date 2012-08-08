package cn.edu.nju.software.gof.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.edu.nju.software.manager.CheckInCounterManager;
import cn.edu.nju.software.manager.CheckInManager;
import cn.edu.nju.software.manager.FriendRequestManager;
import cn.edu.nju.software.manager.PersonManager;
import cn.edu.nju.software.manager.PersonalLocationManager;
import cn.edu.nju.software.util.SpringContextHolder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Person implements Serializable{
	
	private PersonalLocationManager personalLocationManager = SpringContextHolder.getBean("personalLocationManager");
	private PersonManager personManager = SpringContextHolder.getBean("personManager");
	private FriendRequestManager friendRequestManager = SpringContextHolder.getBean("friendRequestManager");
	private CheckInManager checkInManager = SpringContextHolder.getBean("checkInManager");
	private CheckInCounterManager checkInCounterManager = SpringContextHolder.getBean("checkInCounterManager");
    private static final long serialVersionUID = -6832688344476748958L;
    private static final String IDS_SPLIT = ",";

	private Long id;

	private String lastPersonalLocation;
	
	private Long lastLocationId;
	
	private Account account;

	private Profile profile;

	private String friendIds;

	private String topPlaceIds;
	
	private String mobile;

	public Person() {
		super();
	}


	public Account getAccount() {
		return account;
	}




	public void setAccount(Account account) {
		this.account = account;
	}




	public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}

	public List<Person> getFriends() {
		List<Person> friends = new LinkedList<Person>();
		for (Long friendID : this.getFriendIds()) {
			Person person = personManager.findById(friendID);
			friends.add(person);
		}
		return friends;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<Long> getFriendIds() {
		List<Long> ids = Lists.newArrayList();
		for(String id : friendIds.split(IDS_SPLIT)){
			ids.add(Long.valueOf(id));
		}
		return ids;
	}


	public void setFriendIds(List<Long> friendIds) {
		this.friendIds = StringUtils.join(friendIds, IDS_SPLIT);
	}


	public List<Long> getTopPlaceIds() {
		List<Long> ids = Lists.newArrayList();
		for(String id : topPlaceIds.split(IDS_SPLIT)){
			ids.add(Long.valueOf(id));
		}
		return ids;
	}


	public void setTopPlaceIds(List<Long> topPlaceIds) {
		this.topPlaceIds = StringUtils.join(topPlaceIds, IDS_SPLIT);
	}


	public Long getLastLocationId() {
		return lastLocationId;
	}


	public void setLastLocationId(Long lastLocationId) {
		this.lastLocationId = lastLocationId;
	}


	public Profile getProfile() {
		return profile;
	}
	
	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public List<PersonalLocation> getPersonalLocations() {
		return personalLocationManager.findByOwerId(id);
	}

	 
	public List<FriendRequest> getFriendRequests() {
		Map<String,Object> params = Maps.newHashMap();
		params.put("targetPersonId", account.getId());
		return friendRequestManager.find(params);
	}

	public String getLastPersonalLocation() {
		return lastPersonalLocation;
	}

	public void setLastPersonalLocation(String lastPersonalLocation) {
		this.lastPersonalLocation = lastPersonalLocation;
	}

	public List<CheckIn> getCheckIns() {
		return checkInManager.findByOwerId(account.getId());
	}

	public int getCheckInTimes(Long placeID) {
		Map<String,Object> params = Maps.newHashMap();
		params.put("ownerId", account.getId());
		params.put("placeId", placeID);
		return checkInCounterManager.findBy(params).getCounter();
	}

	public int increaseCheckInTimes(Long placeID) {
		Map<String,Object> params = Maps.newHashMap();
		params.put("ownerId", account.getId());
		params.put("placeId", placeID);
		CheckInCounter counter = checkInCounterManager.findBy(params);
		if(counter == null){
			counter = new CheckInCounter(id, placeID, 1);
			checkInCounterManager.save(counter);
			return 1;
		}else{
			counter.setCounter(counter.getCounter() + 1);
			checkInCounterManager.save(counter);
			return counter.getCounter();
		}
	}

	 
}
