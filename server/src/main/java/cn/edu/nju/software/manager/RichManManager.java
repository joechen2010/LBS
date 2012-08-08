/*
 * $HeadURL: $
 * $Id: $
 * Copyright (c) 2010 by Ericsson, all rights reserved.
 */

package cn.edu.nju.software.manager;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.edu.nju.software.dao.RichManDao;
import cn.edu.nju.software.gof.entity.Account;
import cn.edu.nju.software.gof.entity.RichMan;
import cn.edu.nju.software.util.UUIDUtils;

@Component
public class RichManManager {

    private RichManDao richManDao;
    
    @Transactional(propagation = Propagation.REQUIRED)
    public RichMan save(RichMan richMan) {
        boolean isNew = richMan.getId() == null || richMan.getId() == 0;
        if (isNew) {
        	richMan.setId(UUIDUtils.generate());
        	richManDao.insert(richMan);
        } else {
            
        }
        return richManDao.findByPersonId(richMan.getPersonId());
    }
    
    @Transactional(readOnly = true)
    public RichMan findByPersonId(Long personId) {
    	return richManDao.findByPersonId(personId);
    }

	public RichManDao getRichManDao() {
		return richManDao;
	}
	 @Autowired
	public void setRichManDao(RichManDao richManDao) {
		this.richManDao = richManDao;
	}

    
   
    

}
