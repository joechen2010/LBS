/*
 * $HeadURL: $
 * $Id: $
 * Copyright (c) 2010 by Ericsson, all rights reserved.
 */

package cn.edu.nju.software.manager;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.edu.nju.software.dao.PersonalLocationDao;
import cn.edu.nju.software.gof.entity.CheckIn;
import cn.edu.nju.software.gof.entity.PersonalLocation;
import cn.edu.nju.software.util.UUIDUtils;

@Component
public class PersonalLocationManager {

	@Autowired
    private PersonalLocationDao personalLocationDao;

    @Transactional(readOnly = true)
    public List<PersonalLocation> findNearbyFriends(List<Long> friendIds, Date lastDay, Date current) {
        return personalLocationDao.findNearbyFriends(friendIds, lastDay, current);
    }
    
    @Transactional(propagation = Propagation.REQUIRED)
    public PersonalLocation save(PersonalLocation personalLocation) {
        boolean isNew = personalLocation.getId() == null || personalLocation.getId() == 0;
        if (isNew) {
        	personalLocation.setId(UUIDUtils.generate());
        	personalLocationDao.insert(personalLocation);
        } else {
            
        }
        return personalLocationDao.findById(personalLocation.getId());
    }
    
    @Transactional(readOnly = true)
    public List<PersonalLocation> findByOwerId(Long ownerId) {
        return personalLocationDao.findByOwerId(ownerId);
    }

	public PersonalLocationDao getPersonalLocationDao() {
		return personalLocationDao;
	}

	public void setPersonalLocationDao(PersonalLocationDao personalLocationDao) {
		this.personalLocationDao = personalLocationDao;
	}


}
