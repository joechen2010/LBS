package de.gfred.lbbms.service.representations;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.03
 */
@XmlRootElement(name = "customer")
public class SingleCustomerRepresentation {
    private static final String TAG = "de.gfred.lbbms.service.representations.SingleCustomerRepresentation";
    private static final boolean DEBUG = false;

    private String name;
    private String email;
    private String password;
    private LocationRepresentation currentLocation;

    public LocationRepresentation getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LocationRepresentation currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
    
}
