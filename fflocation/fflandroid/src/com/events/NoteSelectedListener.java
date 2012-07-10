package com.events;

import java.util.EventObject;

import com.model.Group;


/**
 * 
 * @author Juan Javier García, Aitor Martin, Andreas Kapouranis
 *
 */
public interface NoteSelectedListener {
         public void handleNoteSelectedEvent(EventObject e, Group gr);
}
