package de.gfred.lbbms.service.model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.03
 */
@NamedQueries({
    @NamedQuery(name="Location.findAll",query="SELECT s FROM Location s"),
    @NamedQuery(name="Location.findById",query="SELECT s FROM Location s WHERE s.id = :id")
})
@Entity
public class Location implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String TAG = "de.gfred.lbbms.service.entities.Location";
    private static final boolean DEBUG = false;

    public static final String FIND_ALL = "Location.findAll";
    public static final String FIND_BY_ID = "Location.findById";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false)
    private Double longitude;

    @Column(nullable=false)
    private Double latitude;

    @Column(nullable=true)
    private Double accurarcy;

    @OneToOne(mappedBy="currentLocation", cascade=CascadeType.ALL)
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAccurarcy() {
        return accurarcy;
    }

    public void setAccurarcy(Double accurarcy) {
        this.accurarcy = accurarcy;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Location)) {
            return false;
        }
        Location other = (Location) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.gfred.lbbms.service.entities.Location[id=" + id + "]";
    }

}
