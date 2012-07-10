package com.events;

import java.util.EventObject;

import com.model.KUserInfo;


/**
 * Friend selected listener
 * @author Juan Javier García, Aitor Martin, Andreas Kapouranis
 *
 */
public interface FriendSelectedListener {
		/**
		 * Handle the Friend Selected event
		 * @param e event
		 * @param us selected friend.
		 */
         public void handleFriendSelectedEvent(EventObject e, KUserInfo us);
}
