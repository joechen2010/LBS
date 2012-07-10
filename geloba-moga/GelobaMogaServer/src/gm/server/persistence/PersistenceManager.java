package gm.server.persistence;

import gm.server.exception.UserNotFoundException;
import gm.server.persistence.Item;
import gm.server.persistence.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.log4j.Logger;

public class PersistenceManager {
	
	private static PersistenceManager pm =  null;
	static Logger l = Logger.getLogger(PersistenceManager.class);
	private EntityManagerFactory emf = null;
	private EntityManager em = null;
	
	private PersistenceManager() {
		super();
		emf = Persistence.createEntityManagerFactory("GenericServer");
		em = emf.createEntityManager();
		em.getTransaction().begin();
	}
	
	public static PersistenceManager getInstance(){
		if(pm == null) pm = new PersistenceManager();
		return pm;
	}
	
	public User getUserForName(String name) throws UserNotFoundException{
		
		try{
		
			l.info("Getting user for name: " + name);
			
			String hql = "FROM User WHERE name is '" + name + "'";
			User u = null;
			
			Query query = em.createQuery(hql);
			query.setMaxResults(1);
			
			u = (User) query.getSingleResult();
			
			if(u == null)
				throw new UserNotFoundException(name);
			
			l.info("Found User " + name);
			
			return u;
		} catch (Exception e){
			throw new UserNotFoundException(name);
		}
	}
	
	public Item getEnviroment(){
		
		String hql = "FROM Item WHERE item_id is 0";
		Item i;
		
		Query query = em.createQuery(hql);
		query.setMaxResults(1);
		
		i = (Item) query.getSingleResult();
		
		return i;
	}
	
	public void persist(Object o){
		em.persist(o);
	}
	
	public void commit(){
		em.getTransaction().commit();
	}
}
