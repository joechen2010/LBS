package com.map;


import java.util.EventObject;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.events.FriendSelectedListener;
import com.google.android.maps.MapView;
import com.model.KUserInfo;

/**
 * MapViewer is an Android View that displays a map.
 *
 */
public class MapViewer extends LinearLayout {

	private MapUsersOverlay overlay_users;
	private MapNotesOverlay overlay_notes;
	private static MapViewer viewer;
	private KUserInfo selectedUser;
    private MapView mapView;
    
	public MapViewer(Context context, AttributeSet attrs) throws Exception {
		super(context, attrs);
		init();
	}

	public MapViewer(Context context) throws Exception {
		super(context);
		init();
	}

	
	public void init() throws Exception {		

		viewer = this;		
		setOrientation(VERTICAL);
		setLayoutParams(new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.FILL_PARENT));

		//The key for maps
		mapView = new MapView(getContext(),"0bDHzVGT2mazIcJxDdHBnTQ5cSjb4fM9UzNofBQ");
		
		//Set enabled
		mapView.setEnabled(true);
		
		//Set clickable
		mapView.setClickable(true);
		
		//Set zoom controls
		mapView.setBuiltInZoomControls(true);
		
		//Set zoom level
    	mapView.getController().setZoom(3);		
		
    	//Add mapView
		addView(mapView);
		
		//Overlay users and overlay notes
		overlay_users = new MapUsersOverlay(this, getContext());
		overlay_notes = new MapNotesOverlay(this);
		
		//First add the users overlay
		mapView.getOverlays().add(overlay_users);
		
    	//Register for events
    	addEvents();
        
	}
	
    /**
     * Activate the users view and remove the map notes overlay
     */
	public void activateUsersView()
	{
		selectedUser = null;
		if(mapView.getOverlays().size()>0)
			mapView.getOverlays().remove(0);
		
		//Add overlays
		mapView.getOverlays().add(overlay_users);	
		overlay_users.init(viewer);
		
	}

	public MapView getMapView() {
		return mapView;
	}
	
	public KUserInfo getSelectedUser()
	{
		return selectedUser;
	}

	/**
	* Register for events 
	*/
	private void addEvents()
	{

    	// Register for events
        overlay_users.addEventListener(new FriendSelectedListener()
        {
        	
    		@Override
    		public void handleFriendSelectedEvent(EventObject e, KUserInfo us) {
    			
    			//Change for map overlay notes
    			selectedUser = us;	
    			mapView.getOverlays().remove(0);
    			
    			overlay_notes.setSelectedUser(us);

    			mapView.getOverlays().add(overlay_notes);
    			
    		}
        	
        });
        		
	}
	public MapNotesOverlay getMapNotesOverlay()
	{
		return overlay_notes;
	}
	public MapUsersOverlay getMapLocationOverlay()
	{
		return overlay_users;
	}
	public boolean getUsersView()
	{
		return (selectedUser==null);
	}
	
}

