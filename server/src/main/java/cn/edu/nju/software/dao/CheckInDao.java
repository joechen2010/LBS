package cn.edu.nju.software.dao;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import cn.edu.nju.software.gof.entity.CheckIn;
import cn.edu.nju.software.gof.entity.CheckInCounter;

@Component
public class CheckInDao extends SqlSessionDaoSupport {


    public CheckIn findById(Long id) {
        return (CheckIn) getSqlSession().selectOne("CheckIn.findById", id);
    }
    
    public void insert(CheckIn checkIn) {
        getSqlSession().insert("CheckIn.insert", checkIn);
    }

    public List<CheckIn> findByOwerId(Long ownerId) {
        return (List<CheckIn>) getSqlSession().selectList("CheckIn.findByOwerId", ownerId);
    }

}
