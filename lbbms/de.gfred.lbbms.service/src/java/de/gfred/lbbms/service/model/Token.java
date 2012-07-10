package de.gfred.lbbms.service.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.22
 */
@NamedQueries({
    @NamedQuery(name="Token.findAll",query="SELECT s FROM Token s"),
    @NamedQuery(name="Token.findById",query="SELECT s FROM Token s WHERE s.id = :id"),
    @NamedQuery(name="Token.findByToken",query="SELECT s FROM Token s WHERE s.token = :token")
})
@Entity
public class Token implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String TAG = "de.gfred.lbbms.service.entities.Token";
    private static final boolean DEBUG = false;

    public static final String FIND_ALL = "Token.findAll";
    public static final String FIND_BY_ID = "Token.findById";
    public static final String FIND_BY_TOKEN = "Token.findByToken";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationDate;

    @Column(length = 32)
    private String token;

    @OneToOne
    @JoinColumn(name="CUSTOMER")
    private Customer customer;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getUser() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        hash += (token != null ? token.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Token)) {
            return false;
        }
        Token other = (Token) object;
        if ((this.token == null && other.token != null) || (this.token != null && !this.token.equals(other.token))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.gfred.lbbms.service.model.Token[id=" + id + "]";
    }
}
