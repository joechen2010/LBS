package de.gfred.lbbms.service.crud;

import de.gfred.lbbms.service.crud.interfaces.ICustomerCrudServiceLocal;
import de.gfred.lbbms.service.model.Customer;
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
public class CustomerCrudService implements ICustomerCrudServiceLocal {
    private static final String TAG = "de.gfred.lbbms.service.crud.CustomerCrudService";
    private static final boolean DEBUG = false;

    @PersistenceContext(unitName="de.gfred.lbbms.servicePU")
    private EntityManager em;

    @Resource
    private UserTransaction ut;

    public CustomerCrudService() {
    }

    @Override
    public Boolean delete(final Customer customer) {
        try {
            ut.begin();
            if (findById(customer.getId()) != null) {
                em.remove(findById(customer.getId()));
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
    public Collection<Customer> findAll() {
       try {
            TypedQuery<Customer> q = em.createNamedQuery(Customer.FIND_ALL, Customer.class);
            return q.getResultList();
        } catch (Exception e) {
        } finally {
        }
        return null;
    }

    @Override
    public Customer findByEmail(final String email) {
        try {
            if (email == null) {
                return null;
            }
            TypedQuery<Customer> q = em.createNamedQuery(Customer.FIND_BY_EMAIL, Customer.class).setParameter("email", email);
            return q.getSingleResult();
        } catch (Exception e) {
        } finally {
        }
        return null;
    }

    @Override
    public Customer findById(final Long id) {
        try {
            if (id == null) {
                return null;
            }
            TypedQuery<Customer> q = em.createNamedQuery(Customer.FIND_BY_ID, Customer.class).setParameter("id", id);
            return q.getSingleResult();
        } catch (Exception e) {
        } finally {
        }
        return null;
    }

    @Override
    public Customer findByMobileNr(final String mobilenr) {
        try {
            if (mobilenr == null) {
                return null;
            }
            TypedQuery<Customer> q = em.createNamedQuery(Customer.FIND_BY_MOBILE, Customer.class).setParameter("mobile", mobilenr);
            return q.getSingleResult();
        } catch (Exception e) {
        } finally {
        }
        return null;
    }

    @Override
    public Customer findByName(final String name) {
        try {
            if (name == null) {
                return null;
            }
            TypedQuery<Customer> q = em.createNamedQuery(Customer.FIND_BY_NAME, Customer.class).setParameter("name", name);
            return q.getSingleResult();
        } catch (Exception e) {
        } finally {
        }
        return null;
    }

    @Override
    public Boolean save(final Customer customer) {
        try {
            ut.begin();
            if (findById(customer.getId()) != null) {
                em.merge(customer);
            } else {
                em.persist(customer);
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
}
