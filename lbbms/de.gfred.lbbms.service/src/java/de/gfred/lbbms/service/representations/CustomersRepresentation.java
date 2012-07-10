package de.gfred.lbbms.service.representations;

import java.net.URI;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.03
 */
@XmlRootElement(name="customerListElement")
public class CustomersRepresentation {
    private static final String TAG = "de.gfred.lbbms.service.representations.CustomersRepresentation";
    private static final boolean DEBUG = false;

    private String email;
    private URI customerUri;

    public URI getCustomerUri() {
        return customerUri;
    }

    public void setCustomerUri(URI customerUri) {
        this.customerUri = customerUri;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }   
}
