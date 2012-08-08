package cn.edu.nju.software.dao;

import java.util.List;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import cn.edu.nju.software.gof.entity.OAuthAccessKey;
import cn.edu.nju.software.gof.entity.RenRen;

@Component
public class AccessKeyDao extends SqlSessionDaoSupport {



    public OAuthAccessKey findAccessKeyById(Long id) {
        return (OAuthAccessKey) getSqlSession().selectOne("OAuthAccessKey.findById", id);
    }
    
    public void insertAccessKey(OAuthAccessKey oAuthAccessKey) {
        getSqlSession().insert("OAuthAccessKey.insert", oAuthAccessKey);
    }

    public RenRen findRenRenById(Long id) {
        return (RenRen) getSqlSession().selectOne("Renren.findById", id);
    }
    
    public List<RenRen> findRenRen(RenRen renRen) {
        return (List<RenRen>) getSqlSession().selectList("Renren.find", renRen);
    }
    
    public List<OAuthAccessKey> findOAuthAccessKey(OAuthAccessKey oAuthAccessKey) {
        return (List<OAuthAccessKey>) getSqlSession().selectList("OAuthAccessKey.find", oAuthAccessKey);
    }
    
    public void insertRenRen(RenRen renRen) {
        getSqlSession().insert("Renren.insert", renRen);
    }


}
