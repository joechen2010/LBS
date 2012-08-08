/*
 * $HeadURL: $
 * $Id: $
 * Copyright (c) 2010 by Ericsson, all rights reserved.
 */

package cn.edu.nju.software.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.edu.nju.software.dao.AccountDao;
import cn.edu.nju.software.dao.PersonDao;
import cn.edu.nju.software.dao.ProfileDao;
import cn.edu.nju.software.gof.entity.Person;
import cn.edu.nju.software.gof.entity.Profile;
import cn.edu.nju.software.util.UUIDUtils;

@Component
public class PersonManager {

    private PersonDao personDao;
    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private AccountDao accountDao;
    
    @Transactional(propagation = Propagation.REQUIRED)
    public Person save(Person person) {
        boolean isNew = person.getId() == null || person.getId() == 0;
        if (isNew) {
        	person.setId(UUIDUtils.generate());
        	person.getAccount().setId(UUIDUtils.generate());
        	person.getAccount().setOwner(person);
        	person.getProfile().setId(UUIDUtils.generate());
        	person.getProfile().setOwner(person);
        	personDao.insert(person);
        	profileDao.insert(person.getProfile());
        	accountDao.insert(person.getAccount());
        } else {
            
        }
        return personDao.findById(person.getId());
    }
    
    @Transactional(readOnly = true)
    public Person findById(Long personId) {
        return personDao.findById(personId);
    }
    
    @Transactional(readOnly = true)
    public List<Profile> findProfiles(Profile profile) {
        return profileDao.findProfiles(profile);
    }

    
    @Autowired
    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }


	public ProfileDao getProfileDao() {
		return profileDao;
	}


	public void setProfileDao(ProfileDao profileDao) {
		this.profileDao = profileDao;
	}


	public AccountDao getAccountDao() {
		return accountDao;
	}


	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}


	public PersonDao getPersonDao() {
		return personDao;
	}

}
