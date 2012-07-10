package com.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.widget.Toast;
import com.events.FriendSelectedEvent;
import com.events.FriendSelectedListener;
import com.fflandroid.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.model.KUserInfo;
import com.web_services.ToServer;


/**
 * This class is used to paint users on top of the map
 * @author Juan Javier García, Aitor Martin, Andreas Kapouranis
 * 
 */
public class MapUsersOverlay extends Overlay{
	
    private Bitmap bubble, shadow, greenBubble;
    private MapViewer mapLocationViewer;
    private Boolean showMessages = false;    
	private Paint innerPaint, textPaint;
	public static KUserInfo selectedUser = null;
	
	private Context context;

	/**
	 * Constructor
	 * @param mapLocationViewer the Map Viewer.
	 * @param ctx The context where the map is shown.
	 */
	public MapUsersOverlay(MapViewer	mapLocationViewer, Context ctx) {
	
		init(mapLocationViewer);
	}



    public void init(MapViewer mapLocationViewer) {
		this.mapLocationViewer = mapLocationViewer;
		try
		{
    		KUserInfo myUser = ToServer.myUser();
    		
    		if(myUser != null)
    		{
    		
    			//Gets the position from the server (if the user has a position)
    			if(myUser.getPosition() != null)
    			{
    				GeoPoint gp = new GeoPoint((int)(myUser.getPosition().getLatitudeFloat()*1E6), (int)(myUser.getPosition().getLongitudeFloat()*1E6));
    				this.mapLocationViewer.getMapView().getController().setCenter(gp);    		
  	
    			}
    		}
    	
		}catch(Exception ex)
		{
			Toast.makeText(context, "Failed while trying to load user.", Toast.LENGTH_LONG).show();
		}
		
	
		bubble = BitmapFactory.decodeResource(mapLocationViewer.getResources(),R.drawable.bubble);
		shadow = BitmapFactory.decodeResource(mapLocationViewer.getResources(),R.drawable.shadow);
		greenBubble = BitmapFactory.decodeResource(mapLocationViewer.getResources(),R.drawable.greenbubble);

		this.innerPaint = this.getInnerNickPaint();
		this.textPaint = this.getTextNickPaint();
		this.innerPaint = this.getInnerMessagePaint();
		this.textPaint = this.getTextMessagePaint();
		
		
	}



	@Override
	public void draw(Canvas canvas, MapView	mapView, boolean shadow) {
     	//Draw the friends
    	try {   		
    		drawMapLocations(canvas, mapView, ToServer.getFriends(), ToServer.myUser());
    	} catch (Exception e) {
			Toast.makeText(context, "Failed while trying to draw the users.", Toast.LENGTH_LONG).show();
		}
 
    }

	@Override
	public boolean onTap(GeoPoint p, MapView mapView)  {
		
		selectedUser = getClickLocation(mapView,p);
		if ( selectedUser != null) {
			//If the user select a user, we send a event
			fireEvent(selectedUser);
		}
		
		//  Lastly return true if we handled this onTap()
		return selectedUser != null;
	}
	/**
	 * This method calculates the area occupied by the users icons on the screen and compares them with the 
  	 * location of the click. If they match, we found a selected user.
	 * @param mapView the map
	 * @param clickPoint the point in the screen where the user click.
	 * @return KUserInfo the friend that the user select or null if the user does not select anyone.
	 */
	private KUserInfo getClickLocation(MapView mapView, GeoPoint clickPoint) {
	    
	    	RectF hitRec = new RectF();
			Point screenCoords = new Point();
	    	Iterator<KUserInfo> itUsers;
	    	
	    	try
	    	{
	    		//Load the friends
	    		List<KUserInfo> userList = ToServer.getFriends();
	    		
	    		//Add the user to the friends. 
	    		userList.add(ToServer.myUser());
			
				itUsers = userList.iterator();
		
				//For each user
				while(itUsers.hasNext()) {
		    		KUserInfo us = itUsers.next();
		    		
		    		if(us != null && us.getPosition() != null)
		    		{
		    			
			    		//  Translate lat and long coordinates to screen coordinates
			    		mapView.getProjection().toPixels(us.getPosition().getGeoPoint(), screenCoords);

			    		
				    	// Create the hit testing Rectangle 
			    		hitRec.set(-bubble.getWidth()/2,-bubble.getHeight(),bubble.getWidth()/2,0);
			    		hitRec.offset(screenCoords.x,screenCoords.y);
		
				    	//  Finally test for a match between our hit Rectangle and the location clicked by the user
			    		mapView.getProjection().toPixels(clickPoint, screenCoords);
			    		if (hitRec.contains(screenCoords.x,screenCoords.y)) {
			    			//We found a selected user
			    			selectedUser = us;
			    			break;
			    		}
			    		else
			    			selectedUser = null;
		    		}
		    	}
		    	
		    	// Clear the newMouseSelection
		    	clickPoint = null;
			} catch (Exception e) {
				Toast.makeText(context, "Failed while trying to get the click location: ." + e.getMessage(), Toast.LENGTH_LONG).show();			
			}

	    	return selectedUser;		   
	    }
	
	/**
	 * This method translates the positions of all friends and the user to screen coordinates 
  	 * and paints them on the map.
	 * @param canvas where paint
	 * @param mapView map view
	 * @param listFriends list of friends to paint
	 * @param mainUser the user 
	 */
    private void drawMapLocations(Canvas canvas, MapView mapView, List<KUserInfo> listFriends, KUserInfo mainUser) {
    	
    	//Iterator to iterate the friends
		Iterator<KUserInfo> iterator = listFriends.iterator();
    	Point screenCoords = new Point();
    
    	//For each friend
    	while(iterator.hasNext()) {
    		
    		KUserInfo kui = iterator.next();
    		if(kui != null)
    		{
    			if(kui.getPosition() != null)
    			{
    				//Translate the friend position to screen coordinates
    				mapView.getProjection().toPixels(kui.getPosition().getGeoPoint(), screenCoords);
    			
    				//  Only offset the shadow in the y-axis
    				canvas.drawBitmap(shadow, screenCoords.x, screenCoords.y - shadow.getHeight(),null);
    				canvas.drawBitmap(bubble, screenCoords.x - bubble.getWidth()/2, screenCoords.y - bubble.getHeight(),null);
    			
    				//Draw the nick window
    				drawNickWindow(kui, canvas, mapView);
    			}
    		}
    	}
    	if(mainUser != null)
    	{
    		//The main user has other icon
    		if(mainUser.getPosition() != null)
    		{
    	
	    		//Translate the map position of the main user to screen coordinates
	    		mapView.getProjection().toPixels(mainUser.getPosition().getGeoPoint(), screenCoords);
			
	    		//  Only offset the shadow in the y-axis
	    		canvas.drawBitmap(shadow, screenCoords.x, screenCoords.y - shadow.getHeight(),null);
	    		canvas.drawBitmap(greenBubble, screenCoords.x - greenBubble.getWidth()/2, screenCoords.y - greenBubble.getHeight(),null);
			
	    		//Draw the nick window of the main user
	    		drawNickWindow(mainUser, canvas, mapView);
    		}
    	}
    }

    /**
     * This method paint the nick of the users on the map, for this aim, the system paints a grey rectangle on the 
 	 * map up the user icon and write the text into it. 
     * @param us the user
     * @param canvas where paint
     * @param mapView the map view
     */
    private void drawNickWindow(KUserInfo us, Canvas canvas, MapView mapView) {
    	
    	//If the user has position
    	if ( us.getPosition() != null) {
 
			//  First determine the screen coordinates of the user's location
			Point destinationOffset = new Point();
			mapView.getProjection().toPixels(us.getPosition().getGeoPoint(), destinationOffset);

			//Determine the measures of the window
		 	float nickWindowWidth = textPaint.measureText(us.getNick());
		 	float nickWindowHeight = textPaint.descent()-textPaint.ascent();	
				
		 	//Add offset
		 	nickWindowWidth += 4;
		 	
			RectF nickWindowRect = new RectF(0,0,nickWindowWidth,nickWindowHeight);				
			float nickWindowOffsetX = destinationOffset.x-nickWindowWidth/2;
			float nickWindowOffsetY = destinationOffset.y-nickWindowHeight-bubble.getHeight();
			nickWindowRect.offset(nickWindowOffsetX,nickWindowOffsetY);

			//  Draw inner nick window with round borders
			canvas.drawRoundRect(nickWindowRect, 5, 5, getInnerNickPaint());
						
			//  Draw the nick	
			int textOffsetY = 10;
			int textOffsetX = 2;
			
			//Draw the window
			canvas.drawText(us.getNick(),nickWindowOffsetX + textOffsetX,nickWindowOffsetY+textOffsetY,getTextNickPaint());

			//If we should show the user name
			if(showMessages)
			{
				//Determine the message measures
			 	float messageWindowWidth = textPaint.measureText(us.getName());
			 	float messageWindowHeight = textPaint.descent()-textPaint.ascent();	
					
			 	//Add offset to the rectangle
			 	messageWindowWidth += 4;
			 	
				RectF messageWindowRect = new RectF(0,0,messageWindowWidth,messageWindowHeight);				
				float messageWindowOffsetX = destinationOffset.x-messageWindowWidth/2;
				float messageWindowOffsetY = destinationOffset.y+2;
				messageWindowRect.offset(messageWindowOffsetX,messageWindowOffsetY);

				//Draw inner nick window
				canvas.drawRoundRect(messageWindowRect, 5, 5, getInnerMessagePaint());
			
				//Draw the message  
				canvas.drawText(us.getName(),messageWindowOffsetX + textOffsetX,messageWindowOffsetY+textOffsetY,getTextMessagePaint());				
			
			}
  
    	}
	
    }

    /**
     * This method modifies the properties of the innerPaint of the rectangle for the nick
     * @return the paint
     */    
	public Paint getInnerNickPaint() {
		if ( innerPaint == null) {
			innerPaint = new Paint();
			innerPaint.setARGB(225, 75, 75, 75); //gray
			innerPaint.setAntiAlias(true);
		}
		return innerPaint;
	}
	/**
	 * This method modifies the properties of the nick text
	 * @return the paint
	 */
	public Paint getTextNickPaint() {
		if ( textPaint == null) {
			textPaint = new Paint();
			textPaint.setARGB(255, 255, 255, 255);
			textPaint.setAntiAlias(true);
		}
		return textPaint;
	}
	
	  /**
     * This method modifies the properties of the innerPaint window for the message
     * @return the paint
     */
	public Paint getInnerMessagePaint() {
		if ( innerPaint == null) {
			innerPaint = new Paint();
			innerPaint.setARGB(225, 75, 75, 75); //gray
			innerPaint.setAntiAlias(true);
		}
		return innerPaint;
	}	
	
	/**
	 * This method modifies the properties of the message text
	 * @return the paint
	 */		
	public Paint getTextMessagePaint() {
		if ( textPaint == null) {
			textPaint = new Paint();
			textPaint.setARGB(255, 255, 255, 255);
			textPaint.setAntiAlias(true);
		}
		return textPaint;
	}
	
	
	//Friend selected listeners
	private List<FriendSelectedListener> _listeners = new ArrayList<FriendSelectedListener>();
	
	/**
	 * Method to add friend selected event listener
	 * @param listener listener
	 */
	public synchronized void addEventListener(FriendSelectedListener listener)	{
		    _listeners.add(listener);
	}
	/**
	 * Method to remove Friend selected event listener
	 * @param listener
	 */
	public synchronized void removeEventListener(FriendSelectedListener listener)	{
		    _listeners.remove(listener);
		  
	}

	/**
	 * Fire an event when the user selects a user	  
	 * @param us Selected user
	 */
	private synchronized void fireEvent(KUserInfo us)	{
		//Create the event
		FriendSelectedEvent event = new FriendSelectedEvent(this);
		
		//For each listener    
		Iterator<FriendSelectedListener> i = _listeners.iterator();
		    
		//Notice the listeners
		while(i.hasNext())	{
		      ((FriendSelectedListener) i.next()).handleFriendSelectedEvent(event, us);    
		}
		  
	}

}

	


