/*
 * $HeadURL: $
 * $Id: $
 * Copyright (c) 2010 by Ericsson, all rights reserved.
 */

package cn.edu.nju.software.manager;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.edu.nju.software.dao.CheckInCounterDao;
import cn.edu.nju.software.gof.entity.CheckInCounter;
import cn.edu.nju.software.util.UUIDUtils;

@Component
public class CheckInCounterManager {

    private CheckInCounterDao checkInCounterDao;

    @Transactional(readOnly = true)
    public CheckInCounter findById(Long id) {
        return checkInCounterDao.findById(id);
    }
    
    @Transactional(propagation = Propagation.REQUIRED)
    public CheckInCounter save(CheckInCounter checkInCounter) {
        boolean isNew = checkInCounter.getId() == null || checkInCounter.getId() == 0;
        if (isNew) {
        	checkInCounter.setId(UUIDUtils.generate());
        	checkInCounterDao.insert(checkInCounter);
        } else {
        	checkInCounterDao.update(checkInCounter);
        }
        return checkInCounterDao.findById(checkInCounter.getId());
    }

    @Transactional(readOnly = true)
    public List<CheckInCounter> findByOwerId(Long ownerId) {
        return checkInCounterDao.findByOwerId(ownerId);
    }
    
    @Transactional(readOnly = true)
    public CheckInCounter findBy(Map<String,Object> params) {
        return checkInCounterDao.find(params);
    }
    

	public CheckInCounterDao getCheckInCounterDao() {
		return checkInCounterDao;
	}
	@Autowired
	public void setCheckInCounterDao(CheckInCounterDao checkInCounterDao) {
		this.checkInCounterDao = checkInCounterDao;
	}

    
    

}
