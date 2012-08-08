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

import cn.edu.nju.software.dao.ReplyDao;
import cn.edu.nju.software.gof.entity.Reply;
import cn.edu.nju.software.util.UUIDUtils;

@Component
public class ReplyManager {

    private ReplyDao replyDao;

    @Transactional(readOnly = true)
    public Reply findById(Long id) {
        return replyDao.findById(id);
    }
    
    @Transactional(readOnly = true)
    public List<Reply> findBypalceId(Long palceId) {
        return replyDao.findBypalceId(palceId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Reply save(Reply reply) {
        boolean isNew = reply.getId() == null || reply.getId() == 0;
        if (isNew) {
        	reply.setId(UUIDUtils.generate());
        	replyDao.insert(reply);
        } else {
            
        }
        return replyDao.findById(reply.getId());
    }
    
	public ReplyDao getReplyDao() {
		return replyDao;
	}
	@Autowired
	public void setReplyDao(ReplyDao replyDao) {
		this.replyDao = replyDao;
	}



  
    

}
