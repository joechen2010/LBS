/*
 * $HeadURL: $
 * $Id: $
 * Copyright (c) 2010 by Ericsson, all rights reserved.
 */

package cn.edu.nju.software.manager;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.edu.nju.software.dao.AccountDao;
import cn.edu.nju.software.gof.entity.Account;

@Component
public class AccountManager {

    private AccountDao accountDao;

    @Transactional(readOnly = true)
    public Account findById(Long id) {
        return accountDao.findById(id);
    }

    @Transactional(readOnly = true)
    public Account findByName(String name) {
        if (StringUtils.isNotBlank(name)) {
            return accountDao.findByUserName(name);
        } else {
            return null;
        }
    }
    
    @Transactional(readOnly = true)
    public Account findBySessionId(String sessionId) {
        if (StringUtils.isNotBlank(sessionId)) {
            return accountDao.findBySessionId(sessionId);
        } else {
            return null;
        }
    }


    @Autowired
    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

}
