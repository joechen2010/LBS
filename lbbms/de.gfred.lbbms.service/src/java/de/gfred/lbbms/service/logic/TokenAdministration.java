package de.gfred.lbbms.service.logic;

import de.gfred.lbbms.service.logic.interfaces.ITokenAdministrationLocal;
import com.sun.jersey.core.util.Base64;
import de.gfred.lbbms.service.crud.interfaces.ITokenCrudServiceLocal;
import de.gfred.lbbms.service.logic.interfaces.ICustomerAdministrationLocal;
import de.gfred.lbbms.service.model.Customer;
import de.gfred.lbbms.service.model.Token;
import de.gfred.lbbms.service.util.TokenGenerator;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.17
 */
@Stateless
public class TokenAdministration implements ITokenAdministrationLocal {
    private static final String TAG = "de.gfred.lbbms.service.logic.TokenAdministration";
    private static final boolean DEBUG = false;

    @EJB
    private ITokenCrudServiceLocal tokenCrud;

    @EJB
    private ICustomerAdministrationLocal customerBean;

    @Override
    public String getToken(final String email, final String password){
        if(customerBean.verifyCustomer(email, password)){
            Customer customer = customerBean.getCustomerByEmail(email);
            if(customer.getToken()!=null && customer.getToken().getToken()!=null){
                 String token = customer.getToken().getToken();
                 if(isTokenValid(customer.getId(),token)){
                     return token;
                 }
            }
            
            return createAndPersistNewToken(email).getToken();
        }else{
            return null;
        }
    }

    @Override
    public boolean isTokenValid(final Long userId, final String token){
        Token tokenObj = tokenCrud.findByToken(token);
        Customer customer = customerBean.getCustomerById(userId);
        
        if(tokenObj==null || customer==null){
            return false;
        }
        
        return tokenObj.getUser()!=null && tokenObj.getUser().getId().equals(userId);
    }

    private Token createAndPersistNewToken(String email){
        Customer customer = customerBean.getCustomerByEmail(email);

        Token token = new Token();
        token.setToken(TokenGenerator.generateToken(email));
        token.setCreateDate(new Date());
        token.setCustomer(customer);
        tokenCrud.save(token);

        customer.setToken(token);
        customerBean.updateCustomer(customer);

        return token;
    }
}
