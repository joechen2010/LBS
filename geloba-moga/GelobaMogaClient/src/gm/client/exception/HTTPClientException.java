package gm.client.exception;

import android.util.Log;


public class HTTPClientException extends Exception {
	private static final long serialVersionUID = -8521696532384857548L;

	public HTTPClientException(int statusCode) {
		super("GelobaMoga-Server returned status-code " +statusCode);
		Log.e("HTTPClientException", this.getMessage());
	}	
	
}