package cn.edu.nju.software.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import cn.edu.nju.software.gof.entity.FriendRequest;

import com.google.common.collect.Maps;

@Component
public class FriendRequestDao extends SqlSessionDaoSupport {


    public void delete(FriendRequest friendRequest) {
        getSqlSession().delete("FriendRequest.delete", friendRequest);
    }
    

    public List<FriendRequest> find(Map<String, Object> params) {
        return (List<FriendRequest>) getSqlSession().selectList("FriendRequest.find", params);
    }
    
    public void insert(FriendRequest friendRequest) {
        getSqlSession().insert("FriendRequest.insert", friendRequest);
    }

}
