/**
 * 
 */
package gm.client.communication;

import java.util.EventListener;

/**
 *  PollingListener used by the Communicator
 * @author stefan
 *
 */
public interface PollingListener extends EventListener {
	public void receive(PollingEvent event);
}
