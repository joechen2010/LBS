package model;

/**
 * This class represents a note with it's own id, owner, text, position and photo.
 */
public class Note {

	private int id;
	private String note;
	private int owner;
	private Position pos;
	private boolean hasPhoto;
	private Photo photo;
	
	public String toString(){
		return id + ":" + note + "(" + pos + ") " + hasPhoto + photo;
	}
	
	public Note(){
		super();
		id=0;
		note=null;
		owner=0;
		pos=null;
		hasPhoto=false;
		photo=null;
	}
	
	public Note(int id, String note, int owner, Position position, boolean hasPhoto)
	{
		this.id = id;
		this.note = note;
		this.owner = owner;
		this.pos = position;
		this.hasPhoto=hasPhoto;
	}

	public void setHasPhoto(boolean hp) {
		this.hasPhoto = hp;
	}
	public boolean getHasPhoto() {
		return hasPhoto;
	}
	
	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	public Photo getPhoto() {
		return photo;
	}

	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setOwner(int id) {
		this.owner = id;
	}
	public int getOwner() {
		return owner;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	public String getNote() {
		return note;
	}
	public void setPosition(Position pos) {
		this.pos = pos;
	}
	public Position getPosition() {
		return pos;
	}
}
