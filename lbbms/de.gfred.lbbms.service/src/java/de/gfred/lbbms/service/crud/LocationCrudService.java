package de.gfred.lbbms.service.crud;

import de.gfred.lbbms.service.crud.interfaces.ILocationCrudServiceLocal;
import de.gfred.lbbms.service.model.Location;
import java.util.Collection;
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
public class LocationCrudService implements ILocationCrudServiceLocal {
    private static final String TAG = "de.gfred.lbbms.service.crud.LocationCrudService";
    private static final boolean DEBUG = false;

    @PersistenceContext(unitName="de.gfred.lbbms.servicePU")
    private EntityManager em;

    @Resource
    private UserTransaction ut;

    public LocationCrudService() {

    }

    @Override
    public Boolean delete(final Location location) {
        try {
            ut.begin();
            if (findById(location.getId()) != null) {
                em.remove(findById(location.getId()));
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
    public Collection<Location> findAll() {
       try {
            TypedQuery<Location> q = em.createNamedQuery(Location.FIND_ALL, Location.class);
            return q.getResultList();
        } catch (Exception e) {
        } finally {
        }
        return null;
    }

    @Override
    public Location findById(final Long id) {
        try {
            if (id == null) {
                return null;
            }
            TypedQuery<Location> q = em.createNamedQuery(Location.FIND_BY_ID, Location.class).setParameter("id", id);
            return q.getSingleResult();
        } catch (Exception e) {
        } finally {
        }
        return null;
    }

    @Override
    public Boolean save(final Location location) {
        try {
            ut.begin();
            if (findById(location.getId()) != null) {
                em.merge(location);
            } else {
                em.persist(location);
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
    public Location getLocationByLongAndLat(final long longitude, final long latitude) {
        return null;
    }

}
