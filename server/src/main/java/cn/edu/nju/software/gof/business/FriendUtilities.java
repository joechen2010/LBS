package cn.edu.nju.software.gof.business;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import cn.edu.nju.software.gof.beans.FriendSearchCondition;
import cn.edu.nju.software.gof.beans.json.BriefFriendInfo;
import cn.edu.nju.software.gof.beans.json.FriendInfo;
import cn.edu.nju.software.gof.beans.json.FriendRequestInfo;
import cn.edu.nju.software.gof.beans.json.NearbyFriendInfo;
import cn.edu.nju.software.gof.beans.json.ProfileInfo;
import cn.edu.nju.software.gof.entity.Account;
import cn.edu.nju.software.gof.entity.FriendRequest;
import cn.edu.nju.software.gof.entity.Person;
import cn.edu.nju.software.gof.entity.PersonalLocation;
import cn.edu.nju.software.gof.entity.Profile;

import com.google.common.collect.Maps;

@Component
public class FriendUtilities extends BaseUtilities {

	private static final double RANGE = 1;

	public List<NearbyFriendInfo> getNearbyFriends(String sessionID,
			double latitude, double longitude) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return null;
		} else {
			List<NearbyFriendInfo> results = new LinkedList<NearbyFriendInfo>();
			//
			List<Long> friendIDs = person.getFriendIds();
			if (friendIDs.size() == 0) {
				return results;
			}
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			Date lastDay = calendar.getTime();
			
			List<PersonalLocation> personalLocations = personalLocationManager.findNearbyFriends(friendIDs, lastDay, new Date());
			for (PersonalLocation location : personalLocations) {
				double locationLatitude = location.getLatitude();
				double locationLongitude = location.getLongitude();
				if (locationLatitude >= latitude - RANGE
						&& locationLatitude <= latitude + RANGE
						&& locationLongitude >= longitude - RANGE
						&& locationLongitude <= longitude + RANGE) {
					Long friendID = location.getOwnerId();
					Person friend = location.getOwner();
					Profile friendProfile = friend.getProfile();
					String string_FriendID = friendID.toString();
					String friendName = friendProfile.getRealName();
					double friendLatitude = location.getLatitude();
					double friendLongitude = location.getLongitude();
					Date updateTime = location.getTime();
					String time = getTimePast(updateTime);
					NearbyFriendInfo nearbyFriend = new NearbyFriendInfo(
							string_FriendID, friendName, friendLatitude,
							friendLongitude, time);
					results.add(nearbyFriend);
				}
			}
			return results;
		}
	}

	private String getTimePast(Date date) {
		Date currentDate = new Date();
		long delta = currentDate.getTime() - date.getTime();
		delta /= (1000 * 60);
		long hour = delta / 60;
		long minute = delta % 60;
		String result = hour + "h " + minute + "m";
		return result;
	}

	public List<FriendInfo> getFriendsList(String sessionID) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return null;
		} else {
			List<FriendInfo> results = new LinkedList<FriendInfo>();
			// Get friend keys.
			List<Person> friends = person.getFriends();
			for (Person friend : friends) {
				Long friendID = friend.getId();
				Profile profile = friend.getProfile();
				String string_FriendID = friendID.toString();
				String friendRealName = profile.getRealName();
				String ipAddress = null;
				String ipPort = null;
				String lastPersonalLocation = null;
				FriendInfo friendInfo = new FriendInfo(string_FriendID,
						friendRealName, null, ipAddress, ipPort,
						lastPersonalLocation);
				results.add(friendInfo);
			}
			return results;
		}
	}

	public ProfileInfo getFriendProfile(String sessionID, Long friendId) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return null;
		} else {
			try {
				Person friend = personManager.findById(friendId);
				if (friend == null
						|| !CommonUtilities.beFriend(person, friend)) {
					return null;
				} else {
					Profile friendProfile = friend.getProfile();
					String realName = friendProfile.getRealName();
					String school = friendProfile.getSchool();
					String place = friendProfile.getCurrentPlace();
					String birthday = friendProfile.getBirthday();
					ProfileInfo profileInfo = new ProfileInfo(realName,
							school, place, birthday, null);
					return profileInfo;
				}
			} catch (IllegalArgumentException exception) {
				return null;
			}
		}
	}

	public byte[] getFriendAvatar(String sessionID, Long friendId) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return null;
		} else {
			try {
				Person friend = personManager.findById(friendId);
				if (friend == null) {
					return null;
				} else {
					Profile friendProfile = friend.getProfile();
					return friendProfile.getAvatar();
				}
			} catch (IllegalArgumentException exception) {
				return null;
			}
		}
	}

	public boolean sendFriendRequest(String sessionID, Long friendId) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return false;
		} else {
			try {
				Person friend = personManager.findById(friendId);
				if (friend != null
						&& !CommonUtilities.beFriend(person, friend)) {
					// Construct the friend request.
					FriendRequest friendRequest = new FriendRequest(friendId, person.getId());
					// Add friend quest to the DB.
					friendRequestManager.save(friendRequest);
				}
			} catch (IllegalArgumentException exception) {
				return false;
			}
		}
		return true;
	}

	public List<FriendRequestInfo> getFriendRequests(String sessionID) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return null;
		} else {
			List<FriendRequestInfo> result = new LinkedList<FriendRequestInfo>();
			Map<String, Object> params = Maps.newHashMap();
			params.put("targetPersonId", person.getId());
			List<FriendRequest> requests = friendRequestManager.find(params);
			for (FriendRequest request : requests) {
				Person sourcePerson = personManager.findById(request.getSourcePersonId());
				Profile profile = sourcePerson.getProfile();
				Account account = sourcePerson.getAccount();
				String realName = profile.getRealName();
				String userName = account.getUserName();
				FriendRequestInfo bean = new FriendRequestInfo(userName,
						request.getId().toString(), realName, sourcePerson.getId().toString());
				result.add(bean);
			}
			return result;
		}
	}

	public boolean agreeFriendRequest(String sessionID, Long requestId) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return false;
		} else {
			try {
				Map<String, Object> params = Maps.newHashMap();
				params.put("id", requestId);
				List<FriendRequest> requests = friendRequestManager.find(params);
				FriendRequest request = requests.size()> 0 ? requests.get(0) : null;
				if (request != null) {
					Long personID = person.getId();
					Long targetPersonID = request.getTargetPersonId();
					if (targetPersonID.equals(personID)) {
						// Remove the friend request from the database.
						Long sourcePersonID = request.getSourcePersonId();
						Person sourcePerson = personManager.findById(request.getSourcePersonId());
						Person targetPerson = person;
						friendRequestManager.delete(request);
						// setup relationship between the two user.
						List<Long> targets = targetPerson.getFriendIds();
						List<Long> sources = sourcePerson.getFriendIds();
						targets.add(sourcePersonID);
						sources.add(targetPersonID);
						targetPerson.setFriendIds(targets);
						sourcePerson.setFriendIds(sources);
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} catch (IllegalArgumentException exception) {
				return false;
			}
		}
	}

	public boolean rejectFriendRequest(String sessionID, Long requestId) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return false;
		} else {
			try {
				Map<String, Object> params = Maps.newHashMap();
				params.put("id", requestId);
				List<FriendRequest> requests = friendRequestManager.find(params);
				FriendRequest request = requests.size()> 0 ? requests.get(0) : null;
				if (request != null) {
					Long personID = person.getId();
					Long targetPersonId = request.getTargetPersonId();
					if (targetPersonId.equals(personID)) {
						// Remove the friend request from the database.
						friendRequestManager.delete(request);
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} catch (IllegalArgumentException exception) {
				return false;
			}

		}
	}

	public boolean deleteFriend(String sessionID, Long friendId) {
		Person person = accountManager.findBySessionId(sessionID).getOwner();
		if (person == null) {
			return false;
		} else {
			try {
				Person friend = personManager.findById(friendId);
				if (friend != null
						&& CommonUtilities.beFriend(person, friend)) {
					Long personID = person.getId();
					List<Long> persons = person.getFriendIds();
					List<Long> friends = friend.getFriendIds();
					persons.remove(friendId);
					friends.remove(personID);
					person.setFriendIds(persons);
					friend.setFriendIds(friends);
					return true;
				} else {
					return false;
				}
			} catch (IllegalArgumentException exception) {
				return false;
			}
		}
	}

	public List<BriefFriendInfo> searchFriends(String sessionID,
			FriendSearchCondition condition) {
		Person myself = accountManager.findBySessionId(sessionID).getOwner();
		if (myself == null) {
			return null;
		} else {
			List<BriefFriendInfo> results = new LinkedList<BriefFriendInfo>();
			//
			String userName = condition.getUserName();
			if (userName != null && !userName.equals("")) {
				Person resultPerson = accountManager.findByName(userName).getOwner();
				if (resultPerson != null) {
					Long personID = resultPerson.getId();
					Profile profile = resultPerson.getProfile();
					BriefFriendInfo friendInfo = new BriefFriendInfo(personID, userName,profile.getRealName());
					results.add(friendInfo);
				}
				return results;

			} else {
				Profile profile = new Profile();
				String realName = condition.getRealName();
				String place = condition.getPlace();
				String birthday = condition.getBirthday();
				String school = condition.getSchool();
				if (realName != null && !realName.trim().equals("")) {
					profile.setRealName(realName);
				}
				if (place != null && !place.trim().equals("")) {
					profile.setCurrentPlace(place);
				}
				if (school != null && !school.trim().equals("")) {
					profile.setSchool(school);
				}
				if (birthday != null && !birthday.trim().equals("")) {
					profile.setBirthday(birthday);
				}
				@SuppressWarnings("unchecked")
				List<Profile> friendProfiles = personManager.findProfiles(profile);
				for (Profile p : friendProfiles) {
					Person person = p.getOwner();
					Account account = person.getAccount();
					BriefFriendInfo info = new BriefFriendInfo(person.getId(),
							account.getUserName(), p.getRealName());
					results.add(info);
				}
			}
			return results;
		}
	}

	public List<BriefFriendInfo> getRecommendFriends(String sessionID) {
		Person myself = accountManager.findBySessionId(sessionID).getOwner();
		if (myself == null) {
			return null;
		} else {
			List<BriefFriendInfo> results = new LinkedList<BriefFriendInfo>();
			Profile profile = myself.getProfile();
			String school = profile.getSchool();
			if (school != null && !school.trim().equals("")) {
				Profile conditionProfile = new Profile();
				conditionProfile.setSchool(school);
				conditionProfile.setId(profile.getId());
				List<Profile> profiles = personManager.findProfiles(conditionProfile);
				for (Profile friendProfile : profiles) {
					Person person = friendProfile.getOwner();
					Account account = person.getAccount();
					BriefFriendInfo info = new BriefFriendInfo(person.getId(),
							account.getUserName(),
							friendProfile.getRealName());
					results.add(info);
				}
			}
			return results;
		}
	}

}
