package de.gfred.lbbms.service.representations;

import de.gfred.lbbms.service.model.Token;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.22
 */
@XmlRootElement(name="token")
public class TokenRepresentation {
    private static final String TAG = "de.gfred.lbbms.service.representations.TokenRepresentation";
    private static final boolean DEBUG = false;

    private String token;
    private String uri;

    public TokenRepresentation() {
    }

    public TokenRepresentation(Token token) {
        this.token = token.getToken();
    }

    public TokenRepresentation(String token,Long id) {
        this.token = token;
        uri = "/customer/"+id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
}
