/*
 * $HeadURL: $
 * $Id: $
 * Copyright (c) 2010 by Ericsson, all rights reserved.
 */

package cn.edu.nju.software.manager;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.edu.nju.software.dao.AccountDao;
import cn.edu.nju.software.dao.FriendRequestDao;
import cn.edu.nju.software.gof.entity.Account;
import cn.edu.nju.software.gof.entity.FriendRequest;
import cn.edu.nju.software.gof.entity.Person;

@Component
public class FriendRequestManager {

    private FriendRequestDao friendRequestDao;

    @Transactional(readOnly = true)
    public List<FriendRequest> find(Map<String, Object> params) {
        return friendRequestDao.find(params);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(FriendRequest friendRequest) {
    	friendRequestDao.insert(friendRequest);
    }
    
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(FriendRequest friendRequest) {
    	friendRequestDao.delete(friendRequest);
    }

	public FriendRequestDao getFriendRequestDao() {
		return friendRequestDao;
	}
	@Autowired
	public void setFriendRequestDao(FriendRequestDao friendRequestDao) {
		this.friendRequestDao = friendRequestDao;
	}

    
    
}
