package controller;

import model.Note;

/**
 * This class makes the Note object type ready to be sent.
 */
public class SNote extends Note{
	/**
	 * Copies the original photo with or without photo attached.
	 * @param n The original Note
	 * @param attachPhotos true if you want to attach the photo, if exists,
	 * false otherwise.
	 */
	public SNote(Note n, boolean attachPhotos){
		setId(n.getId());
		setNote(n.getNote());
		setPosition(n.getPosition());
		setHasPhoto(n.getHasPhoto());
		if(attachPhotos && n.getPhoto()!=null)
			setPhoto(new SPhoto(n.getPhoto()));
	}
}
