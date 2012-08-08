package cn.edu.nju.software.dao;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import cn.edu.nju.software.gof.entity.Place;
import cn.edu.nju.software.gof.entity.RenRen;

import com.google.common.collect.Maps;

@Component
public class PlaceDao extends SqlSessionDaoSupport {


    public Place findById(Long id) {
        return (Place) getSqlSession().selectOne("Place.findById", id);
    }
    
    public void insert(Place place) {
        getSqlSession().insert("Place.insert", place);
    }
    
    public List<Place> findNearByPlaces(double lowLatitude, double highLatitude){
    	Map<String, Object> params = Maps.newHashMap();
    	params.put("lowLatitude", lowLatitude);
    	params.put("highLatitude", highLatitude);
    	return (List<Place>) getSqlSession().selectList("Place.findNearByPlaces", params);
    }
    
    public List<Place> find(Place place) {
        return (List<Place>) getSqlSession().selectList("Place.find", place);
    }
    
    public List<Place> findSubPlacesById(Long id){
    	return (List<Place>) getSqlSession().selectList("Place.findSubPlacesById", id);
    }


}
