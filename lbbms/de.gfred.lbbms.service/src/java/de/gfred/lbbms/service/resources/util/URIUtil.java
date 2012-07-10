package de.gfred.lbbms.service.resources.util;

import de.gfred.lbbms.service.model.Customer;
import de.gfred.lbbms.service.resources.SingleCustomerResource;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.UriBuilder;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.03
 */
public class URIUtil {
    private static final String TAG = "de.gfred.lbbms.service.resources.util.URIUtil";
    private static final boolean DEBUG = false;

    public static URI getCreatedCustomerURI(final Customer customer){
        Map<String, String> uriParameters = new HashMap<String, String>();
        uriParameters.put(ResourceValues.CUSTOMER_ID, customer.getId().toString());

        return UriBuilder.fromPath("/{"+ResourceValues.CUSTOMER_ID+"}").buildFromMap(uriParameters);
    }

    public static URI getUriFromCustomer(final Customer customer){
        Map<String, String> uriParameters = new HashMap<String, String>();
        uriParameters.put(ResourceValues.CUSTOMER_ID, customer.getId().toString());

        return UriBuilder.fromResource(SingleCustomerResource.class).buildFromMap(uriParameters);
    }

}
