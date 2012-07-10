package de.gfred.lbbms.service.crud;


import de.gfred.lbbms.service.crud.interfaces.IMessageCrudServiceLocal;
import de.gfred.lbbms.service.model.Message;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.03
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class MessageCrudService implements IMessageCrudServiceLocal {
    private static final String TAG = "de.gfred.lbbms.service.crud.MessageCrudService";
    private static final boolean DEBUG = false;

    @PersistenceContext(unitName="de.gfred.lbbms.servicePU")
    private EntityManager em;

    @Resource
    private UserTransaction ut;

    public MessageCrudService() {
    }

    @Override
    public Boolean delete(final Message message) {
        try {
            ut.begin();
            if (findById(message.getId()) != null) {
                em.remove(findById(message.getId()));
            } else {
                em.getTransaction().rollback();
                return false;
            }
            ut.commit();
        } catch (Exception e) {
             try {
                ut.rollback();
            } catch (IllegalStateException ex) {
                Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
            } catch (SystemException ex) {
                Logger.getLogger(CustomerCrudService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        } finally {
        }
        return true;
    }

    @Override
    public Collection<Message> findAll() {
       try {
            TypedQuery<Message> q = em.createNamedQuery(Message.FIND_ALL, Message.class);
            return q.getResultList();
        } catch (Exception e) {
        } finally {
        }
        return null;
    }

    @Override
    public Message findById(final Long id) {
        try {
            if (id == null) {
                return null;
            }
            TypedQuery<Message> q = em.createNamedQuery(Message.FIND_BY_ID, Message.class).setParameter("id", id);
            return q.getSingleResult();
        } catch (Exception e) {
        } finally {
        }
        return null;
    }

    @Override
    public Message findByDate(final Date date) {
        try {
            if (date == null) {
                return null;
            }
            TypedQuery<Message> q = em.createNamedQuery(Message.FIND_BY_DATE, Message.class).setParameter("date", date);
            return q.getSingleResult();
        } catch (Exception e) {
        } finally {
        }
        return null;
    }

    @Override
    public Message findByType(final String type) {
        try {
            if (type == null) {
                return null;
            }
            TypedQuery<Message> q = em.createNamedQuery(Message.FIND_BY_TYPE, Message.class).setParameter("type", type);
            return q.getSingleResult();
        } catch (Exception e) {
        } finally {
        }
        return null;
    }

    @Override
    public Long save(final Message message) {
        try {
            ut.begin();
            if (findById(message.getId()) != null) {    
                em.merge(message);
            } else {
                em.persist(message);
            }
            em.flush();
            ut.commit();
            
        } catch (Exception e) {
            try {
                ut.rollback();
            } catch (IllegalStateException ex) {
                Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(TAG).log(Level.SEVERE, null, ex);
            } catch (SystemException ex) {
                Logger.getLogger(CustomerCrudService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return -1l;
        } finally {
        }
        return message.getId();
    }

    @Override
    public Collection<Message> findAllByLocationRange(final Double minLatitude, final Double minLongitude,final Double maxLatitude, final Double maxLongitude){
        try {            
            TypedQuery<Message> q = em.createNamedQuery(Message.FIND_BY_LOCATION, Message.class)
                    .setParameter("lon1", minLongitude).setParameter("lon2", maxLongitude)
                    .setParameter("lat1", minLatitude).setParameter("lat2", maxLatitude);
            return q.getResultList();
        } catch (Exception e) {
        } finally {
        }
        return null;
    }


}
