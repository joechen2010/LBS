package de.gfred.lbbms.service.resources.util;

import de.gfred.lbbms.service.model.Customer;
import de.gfred.lbbms.service.representations.CustomersRepresentation;
import de.gfred.lbbms.service.representations.LocationRepresentation;
import de.gfred.lbbms.service.representations.SingleCustomerRepresentation;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.03
 */
public class ResourceUtil {
    private static final String TAG = "de.gfred.lbbms.service.resources.util.ResourceUtil";
    private static final boolean DEBUG = false;

    public static CustomersRepresentation convertCustomerIntoListItemRepresentation(Customer customer){
        CustomersRepresentation rep = new CustomersRepresentation();
        rep.setEmail(customer.getEmail());
        rep.setCustomerUri(URIUtil.getUriFromCustomer(customer));
        return rep;
    }

    public static SingleCustomerRepresentation convertCustomerIntoRepresentation(Customer customer){
        SingleCustomerRepresentation rep = new SingleCustomerRepresentation();
        rep.setEmail(customer.getEmail());
        rep.setName(customer.getName());

        if(customer.getCurrentLocation()!=null){
            LocationRepresentation loc = new LocationRepresentation();
            loc.setLat(customer.getCurrentLocation().getLatitude());
            loc.setLon(customer.getCurrentLocation().getLongitude());
            rep.setCurrentLocation(loc);
        }
        
        return rep;
    }
    
}
