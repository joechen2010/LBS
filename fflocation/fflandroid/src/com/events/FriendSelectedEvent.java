package com.events;

import java.util.EventObject;

/**
 * @author Juan Javier García, Aitor Martin, Andreas Kapouranis
 * Event class that alerts when the user select a friend in the map
 *
 */
public class FriendSelectedEvent extends EventObject{

	
	private static final long serialVersionUID = 1L;

	/**
	 * Event
	 * @param source the object that raised the event.
	 */
	public FriendSelectedEvent(Object source) {
		super(source);
	}

}
