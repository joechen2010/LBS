/**
 * 
 */
package gm.client.communication;

import gm.client.exception.HTTPClientException;
import gm.server.persistence.User;
import gm.shared.actions.GetUserAction;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.util.Log;

/**
 * UserManager Singleton
 * 
 * @author stefan
 *
 */
public class UserManager {
	/**
	 * TAG the current Activity
	 * used my logging
	 */
	private static final String TAG = "UserManager";
	
	private static UserManager me = new UserManager();
	private Communicator com = null;
	
	private String username = null;
	private String password = null;
	private boolean isLoggedIn = false;
	
	private UserManager () { 
		Log.v(TAG, "constructor");
		this.com = Communicator.get();
	}
	
	public static UserManager get() {
		Log.v(TAG, "returning myself");
		if(UserManager.me == null) 
			UserManager.me = new UserManager();
		return UserManager.me;
	}
	
	public boolean isLoggedIn() {
		Log.v(TAG, "isLoggedIn");
		return this.isLoggedIn;
	}
	
	public boolean login(String username, String password) throws ClientProtocolException, IOException, HTTPClientException {
		Log.v(TAG, "login..");
		if(com.login(username, password)) {
			this.isLoggedIn = true;
			this.username = username;
			this.password = password;
			return true;
		} else {
			this.isLoggedIn = false;
		}
		return this.isLoggedIn;
	}
	
	public User getUserByID(int id) {
		// TODO: GELOBAMOGA getUserByID
		return new User();
	}
	
	public User getUserByName(String username) {
		// TODO: GELOBAMOGA getUserByName
		return new User();
	}
	
	public User getMe() throws ClientProtocolException, IllegalStateException, IOException, ClassNotFoundException, HTTPClientException{
		return ((GetUserAction) 
				  Communicator.get().applyAction(
							new GetUserAction()
						)
				).getUser();
	}
	
}
