package cn.edu.nju.software.dao;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import cn.edu.nju.software.gof.entity.RichMan;

@Component
public class RichManDao extends SqlSessionDaoSupport {


	public void insert(RichMan richMan) {
        getSqlSession().insert("RichMan.insert", richMan);
    }

	public RichMan findByPersonId(Long id) {
        return (RichMan) getSqlSession().selectOne("RichMan.findByPersonId", id);
    }

}
