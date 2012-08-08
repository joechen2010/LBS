package cn.edu.nju.software.dao;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import cn.edu.nju.software.gof.entity.UserLocation;

@Component
public class UserLocationDao extends SqlSessionDaoSupport {


    public UserLocation findById(Long id) {
        return (UserLocation) getSqlSession().selectOne("UserLocation.findById", id);
    }
    
    public void insert(UserLocation userLocation) {
        getSqlSession().insert("UserLocation.insert", userLocation);
    }
    
    
    public List<UserLocation> findByOwnerId(Long ownerId) {
        return (List<UserLocation>) getSqlSession().selectList("UserLocation.findByOwnerId", ownerId);
    }
    


}
