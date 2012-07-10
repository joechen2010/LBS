/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oauth.signpost.basic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 *
 * @author lidejia
 */
public class PostEnabledHttpURLConnectionRequestAdapter extends HttpURLConnectionRequestAdapter {

    private String body;

    public PostEnabledHttpURLConnectionRequestAdapter(HttpURLConnection request, String body) {
        super(request);
        this.body = body;
    }

    public InputStream getMessagePayload() throws IOException {
        return new ByteArrayInputStream(body.getBytes("UTF-8"));
    }
}
