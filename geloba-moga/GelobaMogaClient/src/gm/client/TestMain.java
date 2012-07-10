package gm.client;


import java.io.IOException;
import java.util.HashSet;

import org.apache.http.client.ClientProtocolException;

import com.google.android.maps.MapActivity;

import gm.client.R;
import gm.client.communication.Communicator;
import gm.client.communication.GeoManager;
import gm.client.communication.PollingEvent;
import gm.client.communication.PollingListener;
import gm.client.communication.UserManager;
import gm.client.controls.GoogleMapControl;
import gm.client.exception.HTTPClientException;
import gm.client.geo.GPS;
import gm.server.persistence.User;
import gm.shared.actions.GetNearMapablesAction;
import gm.shared.actions.GetUserAction;
import gm.shared.actions.UpdatePositionAction;
import gm.shared.mapable.Mapable;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TestMain extends MapActivity {
	/**
	 * TAG the current Activity
	 * used by logging
	 */
	private static final String TAG = "TestMain"; // private final String TAG = getClass().getSimpleName();
	
	public static String nl = System.getProperty("line.separator");
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        GPS.init(this);
        
        TextView status = (TextView)findViewById(R.id.StatusView);
    																  status.setText("Getting Communicator... " +nl);
														        				Log.v(TAG, "init Communicator " +nl);
        Communicator com = Communicator.get();
        																			      status.append("done!" +nl);

        																			     
       try {
		User me = Communicator.get().getUserManager().getMe();
		me.getName();
		me.getUserId();
		me.getLatitude();
		me.getLongitude();
		
	} catch (ClientProtocolException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IllegalStateException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (ClassNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (HTTPClientException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
        																			      
        //com.setEndpoint("http://10.0.2.2:8080/GelobaMoga-Server"); 
        com.setEndpoint("http://192.168.178.24:8080/GelobaMoga-Server"); 
														        				Log.v(TAG, "check if logged in" +nl);
														        		 status.append("Check if logged in... " +nl);
        if (!com.getUserManager().isLoggedIn()) {							        	
															        					   status.append("NOT!" +nl);
												        					   	   status.append("Do login... " +nl);
																        					    Log.v(TAG, "log in");
        	try {
        		com.getUserManager().login("asdf", "asdf");
    																		 status.append("Check if logged in... ");
		        if (com.getUserManager().isLoggedIn()) {
		        					 status.append("Logged in as " +com.getUserManager().getMe().getName() +"!" +nl);	     
		        	//doSomething();
		        	
		        				
		        				
		        	GoogleMapControl map = new GoogleMapControl(this, "03k3rlwA5BMPvWkw1ymt5f0VAvNtG2549HqEOVA");
		        	
		        	map.setCenter(GPS.getLat(), GPS.getLon());
		        	map.setZoomLevel(5);
		        	map.setType(GoogleMapControl.TYPE_SAT);
		        	
		        	map.display(
		        			GeoManager.get().getNearUser(
		        					UserManager.get().getMe(),
		        					5,
		        					5)
		        			);
		        	setContentView(map.getView());
		        	
		        	
		        } 														  else status.append("Login failed... " +nl);
		        
			} catch (Exception e) {
				e.printStackTrace();
																	status.append("EXCEPTION: " +e.getMessage() +nl);
			} 
        }
																					  Log.d(TAG, ">> program done!");
    }
    
    private void doSomething() throws ClientProtocolException, IllegalStateException, IOException, ClassNotFoundException, HTTPClientException {
    	// Set my current position on the server:
    	UpdatePositionAction myPos = new UpdatePositionAction();
    	
    	myPos.setLatitude(GPS.getLat());
    	myPos.setLongitude(GPS.getLon());
    	
    	Communicator.get().applyAction(myPos);
    	
    	// Get the "me" object:
    	User me = ((GetUserAction) 
    							  Communicator.get().applyAction(
    																new GetUserAction()
    															)
    				).getUser();
    	
    	// Get objects around me:
    	HashSet<Mapable> nearMe = ((GetNearMapablesAction) 
    												Communicator.get().applyAction(
    																new GetNearMapablesAction(
																				me,5,5
																	)
    												)
    								).getResult();
    	
    	/**
    	 * Erstelle einen GoogleMapControler
    	 * Setze diverse Einstellungen
    	 * Übergebe Menge von Mapables
    	 * Setze ContentView auf view von GoogleMapControler
    	 * 
    	 * Die Menge an Mapables wird auf der GoogleMap dargestellt
    	 * 
    	 * GoogleMapControler map = new GoogleMapControler();
    	 * map.setType(GoogleMapControler.TYPE_SAT);
    	 * map.setZoomLevel(5); 
    	 * map.setCenter(gps.getLat(), gps.getLon());
    	 * map.display(nearMe);
    	 * 
    	 * setContentView(map.getView());
    	 */
    	
    	/**
    	 * Erstelle einen GoogleMapControler
    	 * Setze diverse Einstellungen
    	 * Setze Einstellungen für automatischen Modus
    	 * Setze ContentView auf automatischen view von GoogleMapControler
    	 * 
    	 * Das GoogleMapControler holt sich im gegebenen Zeitinterval
    	 * alle Mapables im gegebenen Radius von der aktuellen Position
    	 * und stellt diese dar
    	 * (benötigt Communicator, GPS, Actions)
    	 * 
    	 * GoogleMapControler map2 = new GoogleMapControler();
    	 * map2.setType(GoogleMapControler.TYPE_NORMAL);
    	 * map2.setZoomLevel(10);
    	 * map.setCenter(gps.getLan(), gps.getLon());
    	 * 
    	 * map.setRadius(500); // m
    	 * map.setTimer(5000); // ms
    	 * 
    	 * setContentView(map.getAutomaticView());
    	 * 
    	 */
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
    
}