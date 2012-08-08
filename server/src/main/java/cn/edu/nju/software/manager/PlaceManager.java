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

import cn.edu.nju.software.dao.PlaceDao;
import cn.edu.nju.software.gof.entity.Place;
import cn.edu.nju.software.util.UUIDUtils;

@Component
public class PlaceManager {

    private PlaceDao placeDao;

    @Transactional(readOnly = true)
    public Place findById(Long id) {
        return placeDao.findById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Place save(Place place) {
        boolean isNew = place.getId() == null || place.getId() == 0;
        if (isNew) {
        	place.setId(UUIDUtils.generate());
        	placeDao.insert(place);
        } else {
            
        }
        return placeDao.findById(place.getId());
    }
    
    @Transactional(readOnly = true)
    public List<Place> findNearByPlaces(double lowLatitude, double highLatitude) {
        return placeDao.findNearByPlaces(lowLatitude, highLatitude);
    }
    
    @Transactional(readOnly = true)
    public List<Place> find(Place place) {
        return placeDao.find(place);
    }
    
    @Transactional(readOnly = true)
    public List<Place> findSubPlacesById(Long id) {
        return placeDao.findSubPlacesById(id);
    }
    
	public PlaceDao getPlaceDao() {
		return placeDao;
	}
	@Autowired
	public void setPlaceDao(PlaceDao placeDao) {
		this.placeDao = placeDao;
	}



  
    

}
