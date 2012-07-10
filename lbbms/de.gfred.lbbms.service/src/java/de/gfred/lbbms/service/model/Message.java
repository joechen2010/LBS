package de.gfred.lbbms.service.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.03
 */
@NamedQueries({
    @NamedQuery(name="Message.findAll",query="SELECT s FROM Message s"),
    @NamedQuery(name="Message.findById",query="SELECT s FROM Message s WHERE s.id = :id"),
    @NamedQuery(name="Message.findByType",query="SELECT s FROM Message s WHERE s.type = :type"),
    @NamedQuery(name="Message.findByDate",query="SELECT s FROM Message s WHERE s.sendDate = :date"),
    @NamedQuery(name="Message.findByLocation",query="Select s from Message s WHERE (s.location.longitude >= :lon1 and s.location.longitude <= :lon2) and (s.location.latitude >= :lat1 and s.location.latitude <= :lat2)")
})
@Entity
public class Message implements Serializable, Comparable<Message> {
    private static final long serialVersionUID = 1L;
    private static final String TAG = "de.gfred.lbbms.service.entities.Message";
    private static final boolean DEBUG = false;

    public static final String FIND_ALL = "Message.findAll";
    public static final String FIND_BY_ID = "Message.findById";
    public static final String FIND_BY_TYPE = "Message.findByType";
    public static final String FIND_BY_DATE = "Message.findByDate";
    public static final String FIND_BY_LOCATION = "Message.findByLocation";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false)
    private String type;

    @Column(nullable=false)
    private String content;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="MESSAGE_LOCATION")
    private Location location;

    @ManyToOne
    @JoinColumn(name="customer")
    private Customer customer;

    @Column(nullable=false)
    private String receiver;

    @Column(nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendDate;

    @ManyToMany(mappedBy="receivedMessages", cascade=CascadeType.ALL)
    private List<Customer> customers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
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
        if (!(object instanceof Message)) {
            return false;
        }
        Message other = (Message) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.gfred.lbbms.service.model.Message[id=" + id + "]";
    }

    @Override
    public int compareTo(Message o) {
        return sendDate.compareTo(o.getSendDate());
    }    
}
