package com.fflandroid;

import com.fflandroid.R;
import com.gps.GPSListener;
import com.web_services.ToServer;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * CreateComment is the activity to allow the user store a comment in the database 
 * @author Juan Javier García, Aitor Martin, Andreas Kapouranis
 *
 */
public class CreateComment extends Activity implements OnClickListener{

	
	//Buttons and text
	private Button bSave;
	private Button bCancel;
	private EditText text;
	
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_comment);
		
		//Fit the buttons to variables.
		bSave = (Button) findViewById(R.id.addcomment_ok);
        bSave.setOnClickListener(this);     
        bCancel = (Button) findViewById(R.id.addcomment_cancel);
        bCancel.setOnClickListener(this);  			
	
	}
	
	@Override
	public void onClick(View v) {
		Button sourceButton=(Button)v;

		//If the user press save
		if(sourceButton==this.bSave)
		{	
		     //Read the comment
			 text = (EditText)findViewById(R.id.addcomment_comment);	 
			 String textString = text.getText().toString();
			 
			 //If the user did not enter a note
			 if(textString.length() == 0)
				 Toast.makeText(getApplicationContext(), "You must enter a note.", Toast.LENGTH_LONG).show();
			 else
			 {
				 try {
					 
					 //Activate the gps
					 LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); 
					 GPSListener locationListener = new GPSListener(this); 
					 locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,locationListener);
				     
					 boolean success = false;
					 
					 //Store the note in the database
					 //If the GPS has detected new position
					 if(locationListener.hasPosition())
					 {
				
						 success = ToServer.setNote(locationListener.getCurrentLatitude(), locationListener.getCurrentLongitude(), textString, null);


					 }
					 //If the GPS does not detected new position we use the last user's postion.
					 else
					 {
						 if(ToServer.myUser().getPosition()!= null)
						 {
							 success = ToServer.setNote(ToServer.myUser().getPosition().getLatitudeFloat(),ToServer.myUser().getPosition().getLongitudeFloat(), textString, null);

						 }
						 else
							 success = false;
					 }
					 
					 //Store the note
					 	
					 //If the note has been saved successfully.
					 if(success)
					 {
						 //Show a message 
						 Toast.makeText(getApplicationContext(), "The note has been saved.", Toast.LENGTH_LONG).show();
						 this.finish();
					 }
					 else
					 {
						 Toast.makeText(getApplicationContext(), "Failed while trying to save the note ", Toast.LENGTH_LONG).show();
					 }
						 
				 } catch (Exception e) {
					 Toast.makeText(getApplicationContext(), "Failed while trying to save the note: " + e.getMessage(), Toast.LENGTH_LONG).show();

				}
			 }
		}
		
		//If the user press the button cancel we leave this application.
		else if(sourceButton==this.bCancel)
		{
			this.finish();
		}
		
	}
	   

}
