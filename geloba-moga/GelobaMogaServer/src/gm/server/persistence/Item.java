package gm.server.persistence;

import gm.server.exception.KeyNotFoundException;
import gm.server.persistence.Item;
import gm.server.persistence.Itemproperty;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the item database table.
 * 
 */
@Entity
@Table(name="item")
public class Item implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="item_id", unique=true, nullable=false)
	private int itemId;

	@Column(nullable=false, length=45)
	private String name;

	//bi-directional many-to-one association to Itemproperty
	@OneToMany(mappedBy="item", fetch=FetchType.EAGER)
	private Set<Itemproperty> properties;

    public Item() {
    }

	public int getItemId() {
		return this.itemId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Itemproperty> getProperties() {
		return this.properties;
	}	
	
	/**
	 * 
	 * @param key
	 * @return Itemproperty for Key
	 * @throws KeyNotFoundException
	 */
	public Itemproperty getItempropertyForKey(String key) throws KeyNotFoundException{
		for(Itemproperty i : properties){
			if(i.getKey().equals(key)) return i;
		}
		throw new KeyNotFoundException(key);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!obj.getClass().equals(Item.class)) return false;
		return this.itemId == ((Item) obj).getItemId();
	}

	@Override
	public String toString() {
		return this.name;
	}
}