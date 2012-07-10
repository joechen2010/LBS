package de.gfred.lbbms.mobile.util;

public interface Values {
    String CUSTOMER_EMAIL = "customerEmail";
    String CUSTOMER_PASSWORD = "customerPassword";
    String SERVER_URI = "http://10.0.2.2:41584/de.gfred.lbbms.service";
    // String CUSTOMER_URI = "http://10.0.2.2:41584/de.gfred.lbbms.service/customer/1/location";
    String CUSTOMER_URI = "http://lbbms.dyndns.org:34900/de.gfred.lbbms.service/customer/1/location";
    String MESSAGE_URI = "http://lbbms.dyndns.org:34900/de.gfred.lbbms.service/customer/1/messages";

    String HEADER_ACCEPT = "accept";
    String HEADER_TYPE = "content-type";
    String MIME_TYPE_JSON = "application/json";

    String JSON_LOCATION_LATITUDE = "lat";
    String JSON_LOCATION_LONGITUDE = "lon";

    String MSG_LOCATION = "location";
    String MSG_TYPE = "type";
    String MSG_CONTENT = "content";
    String MSG_RECEIVER = "receiver";
    String MSG_SENDATE = "sendDate";

    int SCHEME_PORT = 443;
}
