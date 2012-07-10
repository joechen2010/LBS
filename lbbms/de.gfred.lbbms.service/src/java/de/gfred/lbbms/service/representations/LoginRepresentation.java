package de.gfred.lbbms.service.representations;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.22
 */
@XmlRootElement(name="login")
public class LoginRepresentation {
    private static final String TAG = "de.gfred.lbbms.service.representations.LoginRepresentation";
    private static final boolean DEBUG = false;

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
}
