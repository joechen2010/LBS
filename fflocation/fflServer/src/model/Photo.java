package model;

/**
 * This class holds a photo in it.
 */
public class Photo {

	byte[] photo = null;
	

	public String toString(){
		if(photo==null) return "{null}";
		return "{" + org.kobjects.base64.Base64.encode(photo).subSequence(0, 10) + "}";
	}
	
	public Photo(){
		photo=null;
	}
	
	public Photo(byte[] photo)
	{
		this.photo = photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	public byte[] getPhoto() {
		return photo;
	}
}
