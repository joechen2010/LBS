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

import cn.edu.nju.software.dao.CheckInDao;
import cn.edu.nju.software.gof.entity.CheckIn;
import cn.edu.nju.software.gof.entity.CheckInCounter;
import cn.edu.nju.software.util.UUIDUtils;

@Component
public class CheckInManager {

    private CheckInDao checkInDao;

    @Transactional(readOnly = true)
    public CheckIn findById(Long id) {
        return checkInDao.findById(id);
    }
    
    @Transactional(propagation = Propagation.REQUIRED)
    public CheckIn save(CheckIn checkIn) {
        boolean isNew = checkIn.getId() == null || checkIn.getId() == 0;
        if (isNew) {
        	checkIn.setId(UUIDUtils.generate());
        	checkInDao.insert(checkIn);
        } else {
            
        }
        return checkInDao.findById(checkIn.getId());
    }

    @Transactional(readOnly = true)
    public List<CheckIn> findByOwerId(Long ownerId) {
        return checkInDao.findByOwerId(ownerId);
    }
    
	public CheckInDao getCheckInDao() {
		return checkInDao;
	}
	
	@Autowired
	public void setCheckInDao(CheckInDao checkInDao) {
		this.checkInDao = checkInDao;
	}

    
    

}
