package de.gfred.lbbms.service.resources;

import de.gfred.lbbms.service.logic.interfaces.ICustomerAdministrationLocal;
import de.gfred.lbbms.service.model.Customer;
import de.gfred.lbbms.service.representations.CustomersRepresentation;
import de.gfred.lbbms.service.representations.RegisterCustomerRepresentation;
import de.gfred.lbbms.service.resources.util.ResourceUtil;
import de.gfred.lbbms.service.resources.util.URIUtil;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.03
 */
@Stateless
@Path(CustomersResource.URI_TEMPLATE)
public class CustomersResource {
    private static final String TAG = "de.gfred.lbbms.service.resources.CustomersResource";
    private static final boolean DEBUG = false;
    public static final String URI_TEMPLATE = "/customer";

    @EJB
    private ICustomerAdministrationLocal customerAdmin;

    @GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Collection<CustomersRepresentation> getAllCustomers(){
        Collection<Customer> customers = customerAdmin.getAllCustomers();
        Collection<CustomersRepresentation> repCustomers = new ArrayList<CustomersRepresentation>();
        for(Customer customer : customers){
            repCustomers.add(ResourceUtil.convertCustomerIntoListItemRepresentation(customer));
        }
        
        return repCustomers;
    }


    /**
     * Register User
     * 
     * @param data
     * @return
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response registerCustomer(final RegisterCustomerRepresentation data){
        if(data==null){
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }

        if(data.getEmail() != null && !customerAdmin.isCustomerAvailable(data.getEmail())){
            return  Response.status(Response.Status.CONFLICT).build();
        }

        Customer customer = new Customer();
        customer.setEmail(data.getEmail());
        customer.setMobile(data.getMobile());
        customer.setName(data.getName());
        customer.setPassword(data.getPassword());

        if(customerAdmin.registerCustomer(customer)){ 
            return Response.created(URIUtil.getCreatedCustomerURI(customer)).build();
        }

        return  Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

}
