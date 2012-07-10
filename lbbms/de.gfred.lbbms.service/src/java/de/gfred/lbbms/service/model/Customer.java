package de.gfred.lbbms.service.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import org.hibernate.validator.constraints.Email;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.03
 */
@NamedQueries({
    @NamedQuery(name="Customer.findAll",query="SELECT s FROM Customer s"),
    @NamedQuery(name="Customer.findById",query="SELECT s FROM Customer s WHERE s.id = :id"),
    @NamedQuery(name="Customer.findByEmail",query="SELECT s FROM Customer s WHERE s.email = :email"),
    @NamedQuery(name="Customer.findByName",query="SELECT s FROM Customer s WHERE s.name = :name"),
    @NamedQuery(name="Customer.findByMobileNr",query="SELECT s FROM Customer s WHERE s.mobile = :mobile")
})
@Entity
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String TAG = "de.gfred.lbbms.service.model.User";
    private static final boolean DEBUG = false;
    
    public static final String FIND_ALL = "Customer.findAll";
    public static final String FIND_BY_ID = "Customer.findById";
    public static final String FIND_BY_EMAIL = "Customer.findByEmail";
    public static final String FIND_BY_NAME = "Customer.findByName";
    public static final String FIND_BY_MOBILE = "Customer.findByMobileNr";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable=false)
    private String name;
    
    @Column(nullable=false)
    @Email
    private String email;

    @Column(nullable=false)
    private String password;

    @Column(nullable=true)
    private String mobile;

    @OneToOne
    @JoinColumn(name="CUSTOMER_LOCATION")
    private Location currentLocation;

    @OneToMany(mappedBy="customer", cascade=CascadeType.ALL)
    private List<Message> messages;

    @ManyToMany
    @JoinColumn(name="RECEIVED_MESSAGES")
    private Set<Message> receivedMessages;

    @OneToOne(mappedBy="customer", cascade=CascadeType.ALL)
    private Token token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void setReceivedMessages(Set<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public Set<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public void addReceivedMessages(Collection<Message> receivedMessages) {
        this.receivedMessages.addAll(receivedMessages);
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
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
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.gfred.lbbms.service.model.User[id=" + id + "]";
    }

}
