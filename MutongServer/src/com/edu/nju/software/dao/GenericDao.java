package com.edu.nju.software.dao;
import java.io.Serializable;
import java.util.List;

    public interface GenericDao<T, ID extends Serializable> {  
        public T save(T entity);  
      
        public T update(T entity);  
      
        public Integer updateBySql(final String sql);  
          
        public void delete(T entity);  
          
        public T findById(ID id);  
          
        public List findByJPQL(String jpql);  
          
        public List findBySQL(String sql);  
          
        public List findAll();  
          
        public int findRowCount();  
    }  