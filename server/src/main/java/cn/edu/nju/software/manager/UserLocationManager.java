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

import cn.edu.nju.software.dao.UserLocationDao;
import cn.edu.nju.software.gof.entity.UserLocation;
import cn.edu.nju.software.util.UUIDUtils;

@Component
public class UserLocationManager {

    private UserLocationDao userLocationDao;

    @Transactional(readOnly = true)
    public UserLocation findById(Long id) {
        return userLocationDao.findById(id);
    }

    @Transactional(readOnly = true)
    public List<UserLocation> findByOwnerId(Long ownerId) {
        return userLocationDao.findByOwnerId(ownerId);
    }
    
    @Transactional(propagation = Propagation.REQUIRED)
    public UserLocation save(UserLocation userLocation) {
        boolean isNew = userLocation.getId() == null || userLocation.getId() == 0;
        if (isNew) {
        	userLocation.setId(UUIDUtils.generate());
        	userLocationDao.insert(userLocation);
        } else {
            
        }
        return userLocationDao.findById(userLocation.getId());
    }


    @Autowired
    public void setUserLocationDao(UserLocationDao userLocationDao) {
        this.userLocationDao = userLocationDao;
    }

}
