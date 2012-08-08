package cn.edu.nju.software.dao;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import cn.edu.nju.software.gof.entity.Account;

@Component
public class AccountDao extends SqlSessionDaoSupport {


    public Account findByUserName(String name) {
        return (Account) getSqlSession().selectOne("Account.findByUserName", name);
    }
    
    public Account findBySessionId(String sessionId) {
        return (Account) getSqlSession().selectOne("Account.findBySessionId", sessionId);
    }

    public Account findById(Long id) {
        return (Account) getSqlSession().selectOne("Account.findById", id);
    }
    
    public void insert(Account account) {
        getSqlSession().insert("Account.insert", account);
    }


}
