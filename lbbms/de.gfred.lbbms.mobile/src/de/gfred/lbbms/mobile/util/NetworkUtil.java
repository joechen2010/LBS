package de.gfred.lbbms.mobile.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

public class NetworkUtil {
    public static boolean checkResponseCode(final HttpResponse response, final int expectedCode) {
        if (response.getStatusLine().getStatusCode() == expectedCode) {
            return true;
        }

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
            // throw new AuthException();
        }
        return false;
    }
}
