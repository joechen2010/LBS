package cn.edu.nju.software.dao;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import cn.edu.nju.software.gof.entity.Profile;

@Component
public class ProfileDao extends SqlSessionDaoSupport {


	public void insert(Profile profile) {
        getSqlSession().insert("Profile.insert", profile);
    }

	public List<Profile> findProfiles(Profile profile){
		return (List<Profile>) getSqlSession().selectList("Profile.find", profile);
	}

}
