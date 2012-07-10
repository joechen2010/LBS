package de.gfred.lbbms.service.crud;

import de.gfred.lbbms.service.crud.interfaces.ITokenCrudServiceLocal;
import de.gfred.lbbms.service.model.Message;
import de.gfred.lbbms.service.model.Token;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.22
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class TokenCrudService implements ITokenCrudServiceLocal {
    private static final String TAG = "de.gfred.lbbms.service.crud.TokenCrudService";
    private static final boolean DEBUG = false;

    @PersistenceContext(unitName="de.gfred.lbbms.servicePU")
    private EntityManager em;

    @Resource
    private UserTransaction ut;
 
    @Override
    public Boolean delete(final Token token) {
        try {
            ut.begin();
            if (findById(token.getId()) != null) {
                em.remove(findById(token.getId()));
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
                Logger.getLogger(TokenCrudService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        } finally {
        }
        return true;
    }

    @Override
    public Collection<Token> findAll() {
       try {
            TypedQuery<Token> q = em.createNamedQuery(Token.FIND_ALL, Token.class);
            return q.getResultList();
        } catch (Exception e) {
        } finally {
        }
        return null;
    }

    @Override
    public Token findById(final Long id) {
        try {
            if (id == null) {
                return null;
            }
            TypedQuery<Token> q = em.createNamedQuery(Token.FIND_BY_ID, Token.class).setParameter("id", id);
            return q.getSingleResult();
        } catch (Exception e) {
        } finally {
        }
        return null;
    }
    
    @Override
    public Token findByToken(final String token) {
        try {
            if (token == null) {
                return null;
            }
            TypedQuery<Token> q = em.createNamedQuery(Token.FIND_BY_TOKEN, Token.class).setParameter("token", token);
            return q.getSingleResult();
        } catch (Exception e) {
        } finally {
        }
        return null;
    }

    @Override
    public Boolean save(final Token token) {
        try {
            ut.begin();
            if (findById(token.getId()) != null) {
                em.merge(token);
            } else {
                em.persist(token);
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
                Logger.getLogger(TokenCrudService.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
        } finally {
        }
        return true;
    }

}
