/**
 * 
 */
package gm.client.communication;

import java.util.EventObject;

/**
 * PollingAction used by the Communicator
 * @author stefan
 *
 */
public class PollingEvent extends EventObject {
	
	private static final long serialVersionUID = 6236817529781125198L;
	private String message;
	
	/**
	 * Constructs an PollingEvent with the given source (should be the Communicator)
	 * and the message send by the server
	 * @param source
	 * @param message
	 */
	public PollingEvent(Object source, String message) {
		super(source);
		this.message = message;
	}
	
	/**
	 * 
	 * @return the Message
	 */
	public String getMessage() {
		return this.message;
	}
	

}
