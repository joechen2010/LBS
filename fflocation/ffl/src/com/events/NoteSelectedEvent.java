package com.events;

import java.util.EventObject;

/**
 * 
 * @author Juan Javier García, Aitor Martin, Andreas Kapouranis
 *
 */
public class NoteSelectedEvent extends EventObject{


	private static final long serialVersionUID = 1L;

	public NoteSelectedEvent(Object source) {
		super(source);
	}

}
