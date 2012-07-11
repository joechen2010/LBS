package com.map;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.widget.Toast;
import com.events.NoteSelectedEvent;
import com.events.NoteSelectedListener;
import com.fflandroid.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.model.Group;
import com.model.Groups;
import com.model.KNote;
import com.model.KUserInfo;

import com.web_services.ToServer;


/**
 * This class is used to paint the user's notes on top of the map.
 * @author Juan Javier García, Aitor Martin, Andreas Kapouranis
 *
 */
public class MapNotesOverlay extends Overlay{
	
	//Icons to paint in the map
    private Bitmap iconGroup, photoIcon, noteIcon;
    private MapViewer mapNotesViewer;
    private Boolean showMessages = false;
	private Paint innerPaint, textPaint;
	
	//public static MapLocation selectedLocation =null;
	private KUserInfo selectedUser;
	public static Group selectedGroup = null;
	private Groups groups;

	
	public MapNotesOverlay(MapViewer mapNotesViewer) {
		
		//Assign values to local variables
		this.mapNotesViewer = mapNotesViewer;
		this.selectedUser = mapNotesViewer.getSelectedUser();
		
		//Fit the icons to variables.
		noteIcon = BitmapFactory.decodeResource(mapNotesViewer.getResources(),R.drawable.note);
		iconGroup = BitmapFactory.decodeResource(mapNotesViewer.getResources(),R.drawable.photosicon);
		photoIcon = BitmapFactory.decodeResource(mapNotesViewer.getResources(),R.drawable.photoicon);
				
		
		//Paint the messages
		this.innerPaint = this.getInnerNickPaint();
		this.textPaint = this.getTextNickPaint();
		this.innerPaint = this.getInnerMessagePaint();
		this.textPaint = this.getTextMessagePaint();
		

	}
	
	
	
    @Override
	public void draw(Canvas canvas, MapView	mapView, boolean shadow) {
   	
    	
    	//Obtain the grades per pixel for the current zoom
		double gradesPerPixel = getGradesPerPixel();
		
		//Calculate the grades which the icon on the map.
		float rate = (float)(photoIcon.getHeight() * gradesPerPixel); 
		

		//Get the last 200 notes
		try {
			if(selectedUser != null)
			{
				List<KNote> listNotes = ToServer.getNotes(selectedUser.getId(), 200, false);
				
				if(listNotes.size()>0)
				{
					clasifyNotes(rate, listNotes);
					//Draw the notes
					drawMapLocations(canvas, mapView, groups);
				}
			
			}
			
		} catch (Exception e) {
			 Toast.makeText(mapNotesViewer.getContext(), "Fail while trying to read user notes: " + e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
	
		}
		


	}
    


	private void clasifyNotes(float rate, List<KNote> listNotes) {
		//Generate the groups according to the rate.
		groups = new Groups(rate);
		
		//Classify the notes
		if(listNotes != null)
		{
			Iterator<KNote> it = listNotes.iterator();
		
			while(it.hasNext())
			{
				groups.addKNote(it.next());	
			}

		}
		
	}



	@Override
	public boolean onTap(GeoPoint p, MapView mapView)  {
		
		//Obtain the selected group
		selectedGroup = getClickLocation(mapView,p, groups);
		
		if ( selectedGroup != null) {
			//If the user select a group we send a event
			fireEvent(selectedGroup);
		}
		
		//  Lastly return true if we handled this onTap()
		return selectedGroup!= null;
	
	}
	/**
	 * Get the user's click location   
	 * @param mapView the map view.
	 * @param clickPoint the point where the user clicked.
	 * @param grps groups to search.
	 * @return the selected group or null if the user did not click in a group
	 */
	private Group getClickLocation(MapView mapView, GeoPoint clickPoint, Groups grps) {
	     	
	    	RectF hitRec = new RectF();
			Point screenCoords = new Point();
	    	Iterator<Group> itGroup;
	    	
	    	Group selGroup = null;

    		//Get the group iterator
			itGroup = grps.iterator();
	
			//for each group
			while(itGroup.hasNext()) {
	    		Group gr = itGroup.next();
	    		
	    		//check if the group has position
	    		if(gr.getPosition() != null)
	    		{
		    		//  Translate lat and long group's coordinates to screen coordinates
		    		mapView.getProjection().toPixels(gr.getPosition().getGeoPoint(), screenCoords);
		    		
		    		//Check if the group has more than one element 
		    		//(because the weight and height can change between a group or a note)
		    		if(gr.isGroup())
		    		{
		    		
				    	// Create the hit testing Rectangle 
			    		hitRec.set(-iconGroup.getWidth()/2,-iconGroup.getHeight(),iconGroup.getWidth()/2,0);
			    		hitRec.offset(screenCoords.x,screenCoords.y);
	
		    		}
		    		else
		    		{

		    			// Create the hit testing Rectangle 
		    			//The icon weight and height can change between a photo or a note
		    			if(gr.hasPhoto())
		    				hitRec.set(-photoIcon.getWidth()/2,-photoIcon.getHeight(),photoIcon.getWidth()/2,0);
		    			else
		    				hitRec.set(-noteIcon.getWidth()/2,-noteIcon.getHeight(),noteIcon.getWidth()/2,0);
		 
		    			hitRec.offset(screenCoords.x,screenCoords.y);		    			
		    			
		    		}
		
		    		//Translate to screenCoords the user's click
		    		mapView.getProjection().toPixels(clickPoint, screenCoords);

		    		//  Finally test for a match between our hit Rectangle and the location clicked by the user
		    		if (hitRec.contains(screenCoords.x,screenCoords.y)) {
		    			//If the user click this group we have selected group
		    			selGroup = gr;
		    			break;
		    		}
	    		}
		    	
			}
	    	
	    	// Clear the newMouseSelection
	    	clickPoint = null;
	    	
		
	    	
			//Return the selected group
	    	return selGroup;		   
	    }
	   
	/**
	 * Draw the notes location
	 * @param canvas the canvas
	 * @param mapView the map view
	 * @param grps the groups to paint 
	 */
	private void drawMapLocations(Canvas canvas, MapView mapView, Groups grps) {
	    	
    	
		Point screenCoords = new Point();
		Iterator<Group> it = grps.iterator();
	   
		//For each group
		while(it.hasNext())
		{
			//Get the next group
			Group gr = it.next();
			
			//Get the screen coords of the group
    		mapView.getProjection().toPixels(gr.getPosition().getGeoPoint(), screenCoords);
			
    		//If is the group have only one element
    		if(!gr.isGroup())
    		{
    			//If is a photo
    			if(gr.hasPhoto())
    			{
    				//Draw the photo icon
    				canvas.drawBitmap(photoIcon, screenCoords.x - photoIcon.getWidth()/2, screenCoords.y - photoIcon.getHeight(),null);
    			}
    			//If is a note
    			else
    			{
    				//Draw the note icon (screenCoords.x - noteIcon.getWidth()/2) to center the note icon
    				canvas.drawBitmap(noteIcon, screenCoords.x - noteIcon.getWidth()/2 , screenCoords.y - noteIcon.getHeight(),null);

    			}
    		}
    		//Is a group
    		else
    		{
    			//Draw the group
    			canvas.drawBitmap(iconGroup, screenCoords.x - iconGroup.getWidth()/2, screenCoords.y - iconGroup.getHeight(),null);
    		}
    		
    		//Draw the message
    		drawMessageWindow(gr, canvas, mapView);
		}
	  
	}
	   
	/**
	 * Calculate the grades per pixel in the map for the current zoom
	 * @return the grades per pixel in the map
	 */
	private double getGradesPerPixel() {
		
		//Get the map center
		GeoPoint mapCenter = this.mapNotesViewer.getMapView().getMapCenter();   
		Point screenCoordsCenter = new Point();
	    	
		//Obtain the screen coords of the map's center.
	    this.mapNotesViewer.getMapView().getProjection().toPixels(mapCenter, screenCoordsCenter);
	   	
	    //Calculate the map's height.	
	    int height = this.mapNotesViewer.getMapView().getHeight();
	    
	    //Calculate an edge point of the screen.
	    GeoPoint edgePoint = this.mapNotesViewer.getMapView().getProjection().fromPixels(screenCoordsCenter.x, screenCoordsCenter.y + height/2);
	    			
	    //Translate to screen coords
	    Point screenCoordsEdge = new Point();
	    this.mapNotesViewer.getMapView().getProjection().toPixels(edgePoint, screenCoordsEdge);
				
	    //Diff grades
	    double difGrades = Math.abs(mapCenter.getLatitudeE6() - edgePoint.getLatitudeE6());
	    difGrades /= 1E6;
	    
	    //Diff pixels
	    double difPixels = Math.abs(screenCoordsCenter.y - screenCoordsEdge.y);
	    	
	    //Return grades per pixel	
	    return (difGrades/difPixels);
	    		
	}

	/**
	 *    
	 * @param gr The group to be painted
	 * @param canvas The canvas where paint
	 * @param mapView The map controller.
	 */
    private void drawMessageWindow(Group gr, Canvas canvas, MapView mapView) {
    	
    	//If we have something to paint
    	if ( gr != null) {
 
			//  First determine the screen coordinates of the Location
			Point destinationOffset = new Point();
			mapView.getProjection().toPixels(gr.getPosition().getGeoPoint(), destinationOffset);

			//Determine the size of the message
		 	float nickWindowWidth = textPaint.measureText(gr.getName());
		 	float nickWindowHeight = textPaint.descent()-textPaint.ascent();	
				
		 	//Add some space to the note
		 	nickWindowWidth += 4;
		 	
		 	//Create the rectangle to the message
		 	RectF nickWindowRect;
		 	float nickWindowOffsetX;
		 	float nickWindowOffsetY;
		 	nickWindowRect = new RectF(0,0,nickWindowWidth,nickWindowHeight);	
		 	
		 	//Calculate the offset to paint the window
		 	nickWindowOffsetX = destinationOffset.x-nickWindowWidth/2;
		 	
		 	//If the group is only one note
		 	if(!gr.isGroup())
		 	{
		 		//If the note has photo we take the photoIcon heights or the noteIcon height.
				if(gr.hasPhoto())
					nickWindowOffsetY = destinationOffset.y-nickWindowHeight-photoIcon.getHeight();
				else
					nickWindowOffsetY = destinationOffset.y-nickWindowHeight-noteIcon.getHeight();
		 	}
		 	else
		 	{
		 		//If the group is a real group of notes 
				nickWindowOffsetY = destinationOffset.y-nickWindowHeight-iconGroup.getHeight();
		 	}
		 	
		 	//Establish the offset to the rectangle
			nickWindowRect.offset(nickWindowOffsetX,nickWindowOffsetY);

			//  Draw inner nick window with round borders
			canvas.drawRoundRect(nickWindowRect, 5, 5, getInnerNickPaint());
	
			//  Draw the nick		
			int textOffsetY = 10;
			int textOffsetX = 2;
			
			//Draw the text
			canvas.drawText(gr.getName(),nickWindowOffsetX + textOffsetX,nickWindowOffsetY+textOffsetY,getTextNickPaint());

			//If we want to draw the messages
			if(showMessages)
			{
				
				//  First determine the measures of the text
			 	float messageWindowWidth = textPaint.measureText(gr.getMessage());
			 	float messageWindowHeight = textPaint.descent()-textPaint.ascent();	
					
			 	//Add a little offset
			 	messageWindowWidth += 4;
			 	
			 	//Create the rectangle
				RectF messageWindowRect = new RectF(0,0,messageWindowWidth,messageWindowHeight);				
				float messageWindowOffsetX = destinationOffset.x-messageWindowWidth/2;
				float messageWindowOffsetY = destinationOffset.y+2;
				messageWindowRect.offset(messageWindowOffsetX,messageWindowOffsetY);

				//  Draw inner nick window with round borders.
				canvas.drawRoundRect(messageWindowRect, 5, 5, getInnerMessagePaint());
				
				//  Draw the message	
				canvas.drawText(gr.getMessage(),messageWindowOffsetX + textOffsetX,messageWindowOffsetY+textOffsetY,getTextMessagePaint());				
			}
    	}
    }
    
    public void setSelectedUser(KUserInfo us)
    {
    	this.selectedUser = us;
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
	
	
	//Listeners
	
	private List<NoteSelectedListener> _listeners = new ArrayList<NoteSelectedListener>();
	
	/**
	 * Add Event listener to know when the user select a note(s)
	 * @param listener The NoteSelectedListener
	 */
	public synchronized void addEventListener(NoteSelectedListener listener)	{
		    _listeners.add(listener);
	}
		  
	/**
	 * Remove a Listener
	 * @param listener Listener to be removed
	 */
	public synchronized void removeEventListener(NoteSelectedListener listener)	{
		    _listeners.remove(listener);
		  
	}

	/**
	 * Fire an event when the user select a note(s)
	 * @param gr Selected group of note(s)
	 */
	private synchronized void fireEvent(Group gr)	{
		//Create the event
		NoteSelectedEvent event = new NoteSelectedEvent(this);
		    
		//Notice the event to the listeners
		Iterator<NoteSelectedListener> i = _listeners.iterator();    
		while(i.hasNext())	{
		      ((NoteSelectedListener) i.next()).handleNoteSelectedEvent(event, gr);
		}  
	}
}

