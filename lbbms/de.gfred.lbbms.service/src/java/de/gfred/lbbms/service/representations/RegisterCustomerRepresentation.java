package de.gfred.lbbms.service.representations;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.03
 */
@XmlRootElement(name="customerRegister")
public class RegisterCustomerRepresentation {
    private static final String TAG = "de.gfred.lbbms.service.representations.RegisterCustomerRepresentation";
    private static final boolean DEBUG = false;

    private String name;
    private String email;
    private String password;
    private String mobile;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }       
}
