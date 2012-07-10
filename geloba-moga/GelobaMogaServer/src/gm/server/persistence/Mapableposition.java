package gm.server.persistence;

import gm.shared.mapable.Mapable;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the mapableposition database table.
 * 
 */
@Entity
@Table(name="mapableposition")
public class Mapableposition implements Serializable, Mapable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="mp_id", unique=true, nullable=false)
	private int mpId;

	@Column(nullable=false)
	private float latitude;

	@Column(nullable=false)
	private float longitude;

	@Column(name="mapable_fk", nullable=false)
	private int mapableFk;

    @Temporal( TemporalType.DATE)
	@Column(nullable=false)
	private Date timestamp;

    public Mapableposition() {
    }

	public int getId() {
		return this.mpId;
	}

	public float getLatitude() {
		return this.latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return this.longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public int getMapableFk() {
		return this.mapableFk;
	}

	public void setMapableFk(int mapableFk) {
		this.mapableFk = mapableFk;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public boolean equals(Object obj) {
		if(!obj.getClass().equals(Mapableposition.class)) return false;
		return this.mpId == ((Mapableposition) obj).getId();
	}

	@Override
	public String toString() {
		return "("+latitude+"/"+longitude+")";
	}
}