/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oauth.signpost.basic;

import java.net.HttpURLConnection;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpRequest;

/**
 *
 * @author lidejia
 */
public class PostEnabledOAuthConsumer extends DefaultOAuthConsumer {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2176060619367935344L;

	public PostEnabledOAuthConsumer(String consumerKey, String consumerSecret) {
        super(consumerKey, consumerSecret);
    }

    protected HttpRequest wrap(Object request, String body) {
        if (!(request instanceof HttpURLConnection)) {
            throw new IllegalArgumentException(
                    "The default consumer expects requests of type java.net.HttpURLConnection");
        }
        return new PostEnabledHttpURLConnectionRequestAdapter((HttpURLConnection) request, body);
    }

    public HttpRequest sign(Object request, String body) throws OAuthMessageSignerException,
            OAuthExpectationFailedException, OAuthCommunicationException {
        return sign(wrap(request, body));
    }
}
