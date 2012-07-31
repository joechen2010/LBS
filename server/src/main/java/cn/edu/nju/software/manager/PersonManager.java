/*
 * $HeadURL: $
 * $Id: $
 * Copyright (c) 2010 by Ericsson, all rights reserved.
 */

package cn.edu.nju.software.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.edu.nju.software.dao.AccountDao;
import cn.edu.nju.software.dao.PersonDao;
import cn.edu.nju.software.dao.ProfileDao;
import cn.edu.nju.software.gof.entity.Person;
import cn.edu.nju.software.util.UUIDUtils;

@Component
public class PersonManager {

    private PersonDao personDao;
    private ProfileDao profileDao;
    private AccountDao accountDao;
    
    @Transactional(propagation = Propagation.REQUIRED)
    public Person save(Person person) {
        boolean isNew = person.getID() == null || person.getID() == 0;
        if (isNew) {
        	person.setID(UUIDUtils.generate());
        	person.getAccount().setID(UUIDUtils.generate());
        	person.getAccount().setOwner(person);
        	person.getProfile().setID(UUIDUtils.generate());
        	person.getProfile().setOwner(person);
        	personDao.insert(person);
        	profileDao.insert(person.getProfile());
        	accountDao.insert(person.getAccount());
        } else {
            
        }
        return personDao.findById(person.getID());
    }

    
    @Autowired
    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

}
