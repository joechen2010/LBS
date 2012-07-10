package gm.server.persistence;

import gm.server.exception.KeyNotFoundException;
import gm.server.persistence.Itemproperty;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the itemproperty database table.
 * 
 */
@Entity
@Table(name="itemproperty")
public class Itemproperty implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ip_id", unique=true, nullable=false)
	private int ipId;

	@Column(name="float_property")
	private float floatProperty;

	@Column(name="int_property")
	private int intProperty;

	@Column(nullable=false, length=45)
	private String key;

    @Lob()
	@Column(name="string_property")
	private String stringProperty;

	//bi-directional many-to-one association to Itemproperty
    @ManyToOne
	@JoinColumn(name="super_property_fk")
	private Itemproperty superproperty;

	//bi-directional many-to-one association to Itemproperty
	@OneToMany(mappedBy="superproperty", fetch=FetchType.EAGER)
	private Set<Itemproperty> subproperties;

	//bi-directional many-to-one association to Item
    @ManyToOne
	@JoinColumn(name="item_fk")
	private Item item;

    public Itemproperty() {
    }

	public int getId() {
		return this.ipId;
	}

	public float getFloatProperty() {
		return this.floatProperty;
	}

	public void setFloatProperty(float floatProperty) {
		this.floatProperty = floatProperty;
	}

	public int getIntProperty() {
		return this.intProperty;
	}

	public void setIntProperty(int intProperty) {
		this.intProperty = intProperty;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getStringProperty() {
		return this.stringProperty;
	}

	public void setStringProperty(String stringProperty) {
		this.stringProperty = stringProperty;
	}

	public Itemproperty getSuperproperty() {
		return this.superproperty;
	}

	public void setSuperproperty(Itemproperty superproperty) {
		this.superproperty = superproperty;
	}
	
	public Set<Itemproperty> getSubproperties() {
		return this.subproperties;
	}

	
	public Itemproperty getItempropertyForKey(String key) throws KeyNotFoundException{
		for(Itemproperty i : subproperties){
			if(i.getKey().equals(key)) return i;
		}
		throw new KeyNotFoundException(key);
	}
	
	public Item getItem() {
		return this.item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(!obj.getClass().equals(Itemproperty.class)) return false;
		return this.ipId == ((Itemproperty) obj).getId();
	}

	@Override
	public String toString() {
		return this.key + ": (String) " + stringProperty + ", (Int) " + intProperty + ", (Float) " + floatProperty;
	}
	
}