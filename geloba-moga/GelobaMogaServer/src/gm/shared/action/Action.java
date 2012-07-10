package gm.shared.action;

import gm.server.controller.ServerController;
import gm.server.session.Session;

import java.io.Serializable;


/**
 * Classes implementing this interface can be used for
 * two-way communication to the server.
 * @author voidstern
 * @author stefan
 *
 */
public interface Action extends Serializable {
	void beforeServer();
	void atServer(Session s, ServerController sc);
	void afterServer();
	//boolean apply();
}
