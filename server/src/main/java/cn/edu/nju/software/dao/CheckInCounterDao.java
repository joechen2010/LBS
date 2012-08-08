package cn.edu.nju.software.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import cn.edu.nju.software.gof.entity.CheckInCounter;

@Component
public class CheckInCounterDao extends SqlSessionDaoSupport {


    public CheckInCounter findById(Long id) {
        return (CheckInCounter) getSqlSession().selectOne("CheckInCounter.findById", id);
    }
    
    public List<CheckInCounter> findByOwerId(Long ownerId) {
        return (List<CheckInCounter>) getSqlSession().selectList("CheckInCounter.findByOwerId", ownerId);
    }
    
    public void insert(CheckInCounter checkInCounter) {
        getSqlSession().insert("CheckInCounter.insert", checkInCounter);
    }
    
    public void update(CheckInCounter checkInCounter) {
        getSqlSession().update("CheckInCounter.update", checkInCounter);
    }
    
    public CheckInCounter find(Map<String,Object> params){
    	return (CheckInCounter) getSqlSession().selectOne("CheckInCounter.find", params);
    }


}
