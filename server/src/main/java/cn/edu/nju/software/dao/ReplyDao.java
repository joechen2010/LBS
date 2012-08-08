package cn.edu.nju.software.dao;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import cn.edu.nju.software.gof.entity.Place;
import cn.edu.nju.software.gof.entity.Reply;

@Component
public class ReplyDao extends SqlSessionDaoSupport {


    public Reply findById(Long id) {
        return (Reply) getSqlSession().selectOne("Reply.findById", id);
    }
    
    public void insert(Reply reply) {
        getSqlSession().insert("Reply.insert", reply);
    }

    public List<Reply> findBypalceId(Long palceId){
    	return (List<Reply>) getSqlSession().selectList("Reply.findBypalceId", palceId);
    }

}
