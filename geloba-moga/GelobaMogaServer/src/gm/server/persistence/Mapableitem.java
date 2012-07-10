package gm.server.persistence;

import gm.server.communication.Tests;
import gm.shared.mapable.Mapable;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import org.apache.log4j.Logger;


/**
 * The persistent class for the mapableitem database table.
 * 
 */
@Entity
@Table(name="mapableitem")
public class Mapableitem implements Serializable, Mapable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="mi_id", unique=true, nullable=false)
	private int miId;

	//uni-directional many-to-one association to Mapableposition
    @ManyToOne
	@JoinColumn(name="position_fk", nullable=false)
	private Mapableposition mapableposition;

	//uni-directional many-to-one association to Item
    @ManyToOne
	@JoinColumn(name="item_fk", nullable=false)
	private Item item;

    public Mapableitem() {
    }

	public int getId() {
		return this.miId;
	}


	public Mapableposition getCurrentPosition() {
		return this.mapableposition;
	}

	public void setCurrentPosition(Mapableposition mapableposition) {
		this.mapableposition = mapableposition;
	}
	
	/**
	 * Updates the current position to mapableposition
	 * and saves the old one
	 * 
	 * @param mapableposition
	 */
	public void saveCurrentPosition(Mapableposition mapableposition){
		Mapableposition mp = new Mapableposition();
		mp.setLatitude(mapableposition.getLatitude());
		mp.setLongitude(mapableposition.getLongitude());
		mp.setTimestamp(new Date());
		PersistenceManager.getInstance().persist(mp);
		this.mapableposition = mp;
	}
	
	public Item getItem() {
		return this.item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public float getLatitude() {
		return this.getCurrentPosition().getLatitude();
	}

	public float getLongitude() {
		return this.getCurrentPosition().getLongitude();
	}


	public Date getTimestamp() {
		return this.getCurrentPosition().getTimestamp();
	}
	

	public boolean equals(Object obj) {
		if(!obj.getClass().equals(Mapableitem.class)) return false;
		return this.miId == ((Mapableitem) obj).getId();
	}

	@Override
	public String toString() {
		return this.item.getName();
	}
}
