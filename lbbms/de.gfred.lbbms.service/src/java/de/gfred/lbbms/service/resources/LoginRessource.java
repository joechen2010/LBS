package de.gfred.lbbms.service.resources;

import de.gfred.lbbms.service.crud.interfaces.ITokenCrudServiceLocal;
import de.gfred.lbbms.service.logic.interfaces.ICustomerAdministrationLocal;
import de.gfred.lbbms.service.logic.interfaces.ITokenAdministrationLocal;
import de.gfred.lbbms.service.model.Token;
import de.gfred.lbbms.service.representations.LoginRepresentation;
import de.gfred.lbbms.service.representations.TokenRepresentation;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.22
 */
@Stateless
@Path(LoginRessource.URI_TEMPLATE)
public class LoginRessource {
    private static final String TAG = "de.gfred.lbbms.service.resources.LoginRessource";
    private static final boolean DEBUG = false;
    public static final String URI_TEMPLATE = "/login";


    @EJB
    private ICustomerAdministrationLocal customerBean;

    @EJB
    private ITokenAdministrationLocal tokenBean;


    @POST
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public TokenRepresentation login(LoginRepresentation login){
        if(login == null){
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        if(login.getEmail()!=null && login.getPassword()!=null){
            if(customerBean.getCustomerByEmail(login.getEmail())==null){
                throw new WebApplicationException(Status.NOT_FOUND);
            }
            if(customerBean.verifyCustomer(login.getEmail(), login.getPassword())){
                return new TokenRepresentation(tokenBean.getToken(login.getEmail(), login.getPassword()),customerBean.getCustomerByEmail(login.getEmail()).getId());
            }else{
                throw new WebApplicationException(Status.FORBIDDEN);
            }
        }else{
            throw new WebApplicationException(Status.BAD_REQUEST);
        }
    }
}
