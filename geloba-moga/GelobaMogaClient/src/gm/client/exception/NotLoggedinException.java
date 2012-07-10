package gm.client.exception;

import android.util.Log;

public class NotLoggedinException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6828329154508873074L;
	
	public NotLoggedinException() {
		super();
		Log.e("NotLoggedinException", this.getMessage());
	}
}
