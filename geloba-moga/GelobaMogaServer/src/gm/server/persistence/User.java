package gm.server.persistence;

import gm.shared.mapable.Mapable;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name="user")
public class User implements Serializable, Mapable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="user_id", unique=true, nullable=false)
	private int userId;

	@Column(nullable=false, length=45)
	private String name;

	@Column(nullable=false, length=45)
	private String password;

	//uni-directional many-to-one association to Mapableitem
    @ManyToOne
	@JoinColumn(name="mapable_fk", nullable=false)
	private Mapableitem mapableitem;

    public User() {
    }

	public int getUserId() {
		return this.userId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Mapableitem getMapableitem() {
		return this.mapableitem;
	}

	public void setMapableitem(Mapableitem mapableitem) {
		this.mapableitem = mapableitem;
	}
	

	public float getLatitude() {
		return this.getMapableitem().getCurrentPosition().getLatitude();
	}


	public float getLongitude() {
		return this.getMapableitem().getCurrentPosition().getLongitude();
	}


	public Date getTimestamp() {
		return this.getMapableitem().getCurrentPosition().getTimestamp();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!obj.getClass().equals(User.class)) return false;
		return this.userId == ((User) obj).getUserId();
	}

	@Override
	public String toString() {
		return this.name;
	}
}