package de.gfred.lbbms.service.resources;

import de.gfred.lbbms.service.logic.interfaces.ICustomerAdministrationLocal;
import de.gfred.lbbms.service.logic.interfaces.ITokenAdministrationLocal;
import de.gfred.lbbms.service.model.Customer;
import de.gfred.lbbms.service.model.Location;
import de.gfred.lbbms.service.representations.LocationRepresentation;
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
@Path(CurrentLocationRessource.URI_TEMPLATE)
public class CurrentLocationRessource {
    private static final String TAG = "de.gfred.lbbms.service.resources.CurrentLocationRessource";
    private static final boolean DEBUG = false;
    public static final String URI_TEMPLATE = SingleCustomerResource.URI_TEMPLATE + "/location";

    @EJB
    private ICustomerAdministrationLocal customerAdmin;

    @EJB
    private ITokenAdministrationLocal tokenBean;

    /**
     * Get Last Location from user
     * 
     * @param id
     * @return
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public LocationRepresentation getCurrentLocationByCustomerId(@HeaderParam(ResourceValues.TOKEN) String token, @PathParam(ResourceValues.CUSTOMER_ID) Long id){
        if(!tokenBean.isTokenValid(id, token)){
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }

        if(id==null || customerAdmin.getCustomerById(id)==null){
            throw new WebApplicationException(Status.NOT_FOUND);
        }

        Customer customer = customerAdmin.getCustomerById(id);
        if(customer.getCurrentLocation()!=null){
            LocationRepresentation rep = new LocationRepresentation();
            rep.setLat(customer.getCurrentLocation().getLatitude());
            rep.setLon(customer.getCurrentLocation().getLongitude());
            return rep;
        }else{
            throw new WebApplicationException(Status.NO_CONTENT);
        }
    }

    /**
     * Update Location
     * 
     * @param id
     * @param rep
     * @return
     */
    @PUT
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public LocationRepresentation updateCurrentLocationByCustomerId(@HeaderParam(ResourceValues.TOKEN) String token, @PathParam(ResourceValues.CUSTOMER_ID) Long id, LocationRepresentation rep){
        if(!tokenBean.isTokenValid(id, token)){
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }

        if(id==null || customerAdmin.getCustomerById(id)==null){
            throw new WebApplicationException(Status.NOT_FOUND);
        }

        if(rep == null){
            throw new WebApplicationException(Status.BAD_REQUEST);
        }



        Customer customer = customerAdmin.getCustomerById(id);
        Location loc = new Location();
        loc.setLatitude(rep.getLat());
        loc.setLongitude(rep.getLon());
        customer.setCurrentLocation(loc);

        if(customerAdmin.updateCustomer(customer)){
            return rep;
        }else{
            throw new WebApplicationException(Status.NO_CONTENT);
        }
    }

}
