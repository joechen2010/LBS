package com.fflandroid;


import java.util.EventObject;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.events.NoteSelectedListener;
import com.fflandroid.R;
import com.google.android.maps.MapActivity;
import com.gps.GPSListener;
import com.map.MapViewer;
import com.model.Group;
import com.web_services.ToServer;


/**
 * This Activity is used to show Maps
 * @author Juan Javier García, Aitor Martin, Andreas Kapouranis
 *
 */
public class Map extends MapActivity implements OnClickListener{
	
	//Local variables

    GPSListener locationListener;
    LocationManager locationManager;
    
    Group selectedGroup;
    MapViewer mlv;

    //This variable is used to know if the map has the users view or the notes view
   // boolean usersView = true;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);
        
        //Activate the GPS
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); 
        locationListener = new GPSListener(this); 
        
        //Requests updates each 200 meters
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 200,locationListener);
   
        mlv = (MapViewer) findViewById(R.id.map_view);
        
    	// Register for events
        addEvents(); 
    }
   
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	new MenuInflater(getApplication())
    	.inflate(R.layout.menu, menu);
    	return(super.onCreateOptionsMenu(menu));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent newIntent;
    	
    	// Handle item selection
        switch (item.getItemId()) {
        
        	//If the user wants to create a comment
	        case R.id.create_comment:
	    		newIntent = new Intent(this, CreateComment.class);
				this.startActivityForResult(newIntent, 0);
				
	            return true;
	        	
	        // If the user wants to take a photo
	        case R.id.take_photo:
				newIntent = new Intent(this, CameraAndroid.class);
				this.startActivityForResult(newIntent, 0);
				
	            return true;
	            
	        // If the user wants to find friends
	        case R.id.find_friend:
				newIntent = new Intent(this, FindFriend.class);
				this.startActivityForResult(newIntent, 0);

				return true;

			//If the user wants to see his friends requests
	        case R.id.friend_requests:
	        	
			try {
				//If the user has friend's requests.
				if(ToServer.getRequests().size() >0)
	        	{
	        	
	        		newIntent = new Intent(this, FriendRequest.class);
	        		this.startActivityForResult(newIntent, 0);
	        	}
				else
				{
					Toast.makeText(getApplicationContext(), "You do not have any friend's request.", Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "Fail while trying to access to friend's request: " + e.getMessage(), Toast.LENGTH_LONG).show();

			}
				
	            return true;
	        		        	
	        //If the user wants to modify his personal data
	        case R.id.modify_personal:
	        	
				newIntent = new Intent(this, ModifyUser.class);
				this.startActivityForResult(newIntent, 0);
	            return true;
	            
	        //If the user wants to exit
	        case R.id.exit:
	        	
	            ToServer.logout();
	        	this.finish();
	            
	            return true;
	        default:
	        	return super.onOptionsItemSelected(item);
        }
    }
    
    //If the user press the back button and we are in the notes view we have to return to users view
    //Otherwise if we are in users view we have to return to login Activity.
    @Override
    public void onBackPressed()
    {	
    	//Return tu login activity
    	if(mlv.getUsersView())
    		this.finish();
    	else
    	{
    		//Return to users view
    		mlv.activateUsersView();  		
    	}
    }
   
   
	private void addEvents() {
		
		//Add a events to know when  the user selects a note
	    mlv.getMapNotesOverlay().addEventListener(new NoteSelectedListener()
        {
	        	
			@Override
			public void handleNoteSelectedEvent(EventObject e, Group gr) {
				//We have to go to gallery because the user has select a (groups) note
				selectedGroup = gr;
				onClick(mlv);
			}
        	
        });				
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		
		return false;
	}

	@Override
	public void onClick(View v) {
		//If the user has selected a group
		if(selectedGroup != null)
		{
			//It was clicked a user, we show his notes
			Intent newIntent = new Intent(v.getContext(), PhotoGallery.class);
			PhotoGallery.selectedGroup = selectedGroup;
			selectedGroup = null;
			this.startActivityForResult(newIntent, 0);
	
		}
	}	
	
}
