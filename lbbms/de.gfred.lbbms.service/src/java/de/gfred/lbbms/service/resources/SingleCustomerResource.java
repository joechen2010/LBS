package de.gfred.lbbms.service.resources;

import de.gfred.lbbms.service.logic.interfaces.ICustomerAdministrationLocal;
import de.gfred.lbbms.service.logic.interfaces.ITokenAdministrationLocal;
import de.gfred.lbbms.service.model.Customer;
import de.gfred.lbbms.service.model.Location;
import de.gfred.lbbms.service.representations.SingleCustomerRepresentation;
import de.gfred.lbbms.service.resources.util.ResourceUtil;
import de.gfred.lbbms.service.resources.util.ResourceValues;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.03
 */
@Stateless
@Path(SingleCustomerResource.URI_TEMPLATE)
public class SingleCustomerResource {
    private static final String TAG = "de.gfred.lbbms.service.resources.SingleCustomerResource";
    private static final boolean DEBUG = false;
    public static final String URI_TEMPLATE = CustomersResource.URI_TEMPLATE + "/{" + ResourceValues.CUSTOMER_ID + ": [0-9]+}";

    @EJB
    private ICustomerAdministrationLocal customerAdmin;

    @EJB
    private ITokenAdministrationLocal tokenBean;

    @GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public SingleCustomerRepresentation getSingleCustomerById(@HeaderParam(ResourceValues.TOKEN) String token, @PathParam(ResourceValues.CUSTOMER_ID) Long id){
        if(!tokenBean.isTokenValid(id, token)){
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }

        if(id==null || customerAdmin.getCustomerById(id)==null){
            throw new WebApplicationException(Status.NOT_FOUND);
        }

        Customer customer = customerAdmin.getCustomerById(id);

        return ResourceUtil.convertCustomerIntoRepresentation(customer);
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public SingleCustomerRepresentation updateCustomer(@HeaderParam(ResourceValues.TOKEN) String token, @PathParam(ResourceValues.CUSTOMER_ID) Long id, SingleCustomerRepresentation reps ){
        if(!tokenBean.isTokenValid(id, token)){
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }

        if(id==null || customerAdmin.getCustomerById(id)==null){
            throw new WebApplicationException(Status.NOT_FOUND);
        }

        if(reps == null){
            throw new WebApplicationException(Status.BAD_REQUEST);
        }

        Customer customer = customerAdmin.getCustomerById(id);
        customer.setEmail(reps.getEmail());
        customer.setName(reps.getName());
        customer.setPassword(reps.getPassword());

        if(reps.getCurrentLocation()!=null){
            Location loc = new Location();
            loc.setLatitude(reps.getCurrentLocation().getLat());
            loc.setLongitude(reps.getCurrentLocation().getLon());
            customer.setCurrentLocation(loc);
        }

        if(customerAdmin.updateCustomer(customer)){
            Customer newCustomer = customerAdmin.getCustomerById(id);
            return ResourceUtil.convertCustomerIntoRepresentation(newCustomer);
        }else{
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }
    }

}
