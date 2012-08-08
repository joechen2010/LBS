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

import cn.edu.nju.software.dao.AccessKeyDao;
import cn.edu.nju.software.gof.entity.OAuthAccessKey;
import cn.edu.nju.software.gof.entity.RenRen;
import cn.edu.nju.software.util.UUIDUtils;

@Component
public class AuthAccessKeyManager {

    private AccessKeyDao accessKeyDao;

    @Transactional(readOnly = true)
    public OAuthAccessKey findAccessKeyById(Long id) {
        return accessKeyDao.findAccessKeyById(id);
    }
    
    @Transactional(readOnly = true)
    public RenRen findRenRenById(Long id) {
        return accessKeyDao.findRenRenById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OAuthAccessKey saveOAuthAccessKeye(OAuthAccessKey oAuthAccessKey) {
        boolean isNew = oAuthAccessKey.getId() == null || oAuthAccessKey.getId() == 0;
        if (isNew) {
        	oAuthAccessKey.setId(UUIDUtils.generate());
        	accessKeyDao.insertAccessKey(oAuthAccessKey);
        } else {
            
        }
        return accessKeyDao.findAccessKeyById(oAuthAccessKey.getId());
    }
    
    @Transactional(propagation = Propagation.REQUIRED)
    public RenRen saveRenRen(RenRen renRen) {
        boolean isNew = renRen.getId() == null || renRen.getId() == 0;
        if (isNew) {
        	renRen.setId(UUIDUtils.generate());
        	accessKeyDao.insertRenRen(renRen);
        } else {
            
        }
        return accessKeyDao.findRenRenById(renRen.getId());
    }
    
    @Transactional(readOnly = true)
    public List<RenRen> findRenRen(RenRen renRen) {
        return accessKeyDao.findRenRen(renRen);
    }
    
    @Transactional(readOnly = true)
    public List<OAuthAccessKey> findOAuthAccessKey(OAuthAccessKey oAuthAccessKey) {
        return accessKeyDao.findOAuthAccessKey(oAuthAccessKey);
    }
    
	public AccessKeyDao getAccessKeyDao() {
		return accessKeyDao;
	}
	@Autowired
	public void setAccessKeyDao(AccessKeyDao accessKeyDao) {
		this.accessKeyDao = accessKeyDao;
	}


    

}
