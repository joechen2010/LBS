package de.gfred.lbbms.service.representations;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.03
 */
@XmlRootElement(name="location")
public class LocationRepresentation {
    private static final String TAG = "de.gfred.lbbms.service.representations.LocationRepresentation";
    private static final boolean DEBUG = false;

    private Double lon;
    private Double lat;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
