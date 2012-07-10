package controller;

import model.Photo;

/**
 * This class makes the Photo object type ready to be sent.
 */
public class SPhoto extends Photo{
	public SPhoto(Photo n){
		setPhoto(n.getPhoto());
	}
}
