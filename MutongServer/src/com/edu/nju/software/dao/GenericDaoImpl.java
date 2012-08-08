package com.edu.nju.software.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

public class GenericDaoImpl<T, ID extends Serializable> 
//extends JpaDaoSupport  
//implements GenericDao<T, ID> 
{  
	/*
private Class persistentClass;  

public GenericDaoImpl() {  
this.persistentClass = (Class) ((ParameterizedType) getClass()  
        .getGenericSuperclass()).getActualTypeArguments()[0];  
}  

public Class getPersistentClass() {  
return persistentClass;  
}  

public T save(Object entity) {  
getJpaTemplate().persist(entity);  
return (T)entity;  
}  

public T update(Object entity) {  
getJpaTemplate().merge(entity);  
return (T)entity;  
}  

public Integer updateBySql(final String sql){  
return (Integer)getJpaTemplate().execute(new JpaCallback(){  
    public Integer doInJpa(EntityManager em) throws PersistenceException{  
        return em.createNativeQuery(sql).executeUpdate();  
    }  
});  
}  

public void delete(Object entity) {  
getJpaTemplate().remove(entity);  
}  

public T findById(Serializable id) {  
return (T)getJpaTemplate().find(this.getPersistentClass(), id);  
}  

public List findByJPQL(String jpql) {  
return getJpaTemplate().find(jpql);  
}  

public List findBySQL(final String sql) {  
return  getJpaTemplate().executeFind(new JpaCallback(){  
    public List doInJpa(EntityManager em) throws PersistenceException{  
        return em.createNativeQuery(sql).getResultList();  
    }  
});  
}  

public List findAll() {  
return getJpaTemplate().executeFind(new JpaCallback() {  
    public Object doInJpa(EntityManager em) throws PersistenceException {  
        StringBuffer jpql = new StringBuffer("from ");  
        jpql.append(getPersistentClass().getName());  
        jpql.append(" obj");  
        return em.createQuery(jpql.toString()).getResultList();  
    }  
});  
}  


public int findRowCount() {  
return ((Long) getJpaTemplate().execute(new JpaCallback() {  
    public Object doInJpa(EntityManager em) throws PersistenceException {  
        StringBuffer strBuff = new StringBuffer("select count(*) from ");  
        strBuff.append(getPersistentClass().getName());  
        return em.createQuery(strBuff.toString()).getResultList().get(0);  
    }  
})).intValue();  
}  
*/
}  