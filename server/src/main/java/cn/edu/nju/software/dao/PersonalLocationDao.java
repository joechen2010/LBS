package cn.edu.nju.software.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import cn.edu.nju.software.gof.entity.Account;
import cn.edu.nju.software.gof.entity.PersonalLocation;

import com.google.common.collect.Maps;

@Component
public class PersonalLocationDao extends SqlSessionDaoSupport {


	@SuppressWarnings("unchecked")
    public List<PersonalLocation> findNearbyFriends(List<Long> friendIds, Date lastDay, Date current) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("friendIds", friendIds);
        params.put("lastDay", lastDay);
        params.put("current", current);
        return getSqlSession().selectList("PersonalLocation.findNearbyFriends", params);
    }
	
	public List<PersonalLocation> findByOwerId(Long ownerId) {
        return getSqlSession().selectList("PersonalLocation.findByOwerId", ownerId);
    }

	public void insert(PersonalLocation personalLocation) {
        getSqlSession().insert("PersonalLocation.insert", personalLocation);
    }
	
	public PersonalLocation findById(Long id) {
        return (PersonalLocation) getSqlSession().selectOne("PersonalLocation.findById", id);
    }
}
