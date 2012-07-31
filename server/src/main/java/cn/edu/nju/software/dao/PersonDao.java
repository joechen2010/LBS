package cn.edu.nju.software.dao;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import cn.edu.nju.software.gof.entity.Account;
import cn.edu.nju.software.gof.entity.Person;

@Component
public class PersonDao extends SqlSessionDaoSupport {


	public void insert(Person person) {
        getSqlSession().insert("Person.insert", person);
    }


	public Person findById(Long id) {
        return (Person) getSqlSession().selectOne("Person.findById", id);
    }
}
