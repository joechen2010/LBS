package com.fflandroid;


import com.fflandroid.R;
import com.model.KUserInfo;
import com.web_services.ToServer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * This Activity is used to allow the users register into the system.
 * @author Juan Javier García, Aitor Martin, Andreas Kapouranis
 *
 */
public class Register extends Activity implements OnClickListener{
	
	//Buttons
	private Button bSave;
	private Button bCancel;
		
	private EditText et_name, et_surname, et_address, et_email, et_country,et_nick,
	et_password, et_phone;
	@Override
	    
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        
        //Fit the interface's buttons and EditTexts to local variables
        bSave= (Button) findViewById(R.id.register_bSave);
        bSave.setOnClickListener(this);  	  
        
        bCancel= (Button) findViewById(R.id.register_bCancel);
        bCancel.setOnClickListener(this);  	    
        
        
        et_name = (EditText)findViewById(R.id.register_tName);
        et_surname = (EditText)findViewById(R.id.register_tSurname);
        et_address = (EditText)findViewById(R.id.register_tAddress);
        et_country = (EditText)findViewById(R.id.register_tCountry);
        et_email = (EditText)findViewById(R.id.register_tEmail);
        et_nick = (EditText)findViewById(R.id.register_tNick);
        et_password = (EditText)findViewById(R.id.register_tPassword);
        et_phone = (EditText)findViewById(R.id.register_tPhone);
        
	}

	@Override
	public void onClick(View v) {
		Button sourceButton=(Button)v;
		
		//If the user press the button save
		if(sourceButton==this.bSave)
		{
			String name, surname, address, email, country, nick,
			password, phone;	
		
			//Read the values
			name = et_name.getText().toString();
			surname = et_surname.getText().toString();
			address = et_address.getText().toString();
			country = et_country.getText().toString();
			email = et_email.getText().toString();
			nick = et_nick.getText().toString();
			password = et_password.getText().toString();
			phone = et_phone.getText().toString();
		 
			
			//Check if all the attributes has a value
			if(name.length() == 0)
			 Toast.makeText(getApplicationContext(), "You must enter the name.", Toast.LENGTH_LONG).show();
			else if(surname.length() == 0)
				 Toast.makeText(getApplicationContext(), "You must enter the surname .", Toast.LENGTH_LONG).show();
			else if(address.length() == 0)
				 Toast.makeText(getApplicationContext(), "You must enter the address.", Toast.LENGTH_LONG).show();
			else if(country.length() == 0)
				 Toast.makeText(getApplicationContext(), "You must enter the country .", Toast.LENGTH_LONG).show();
			else if(email.length() == 0)
				 Toast.makeText(getApplicationContext(), "You must enter the email.", Toast.LENGTH_LONG).show();
			else if(nick.length() == 0 || nick.length()<3)
				 Toast.makeText(getApplicationContext(), "You must enter the nick greater than 3 characters .", Toast.LENGTH_LONG).show();
			else if(password.length() == 0 || password.length() < 4 )
				 Toast.makeText(getApplicationContext(), "You must enter a password greater than 4 characters.", Toast.LENGTH_LONG).show();
			else if(phone.length() == 0)
				 Toast.makeText(getApplicationContext(), "You must enter the phone number.", Toast.LENGTH_LONG).show();
			else if(phone.length() > 9)
				 Toast.makeText(getApplicationContext(), "The phone number is incorrect.", Toast.LENGTH_LONG).show();
			
			else
			{
				
				//Sets the values
				KUserInfo us = new KUserInfo();
				us.setNick(nick);
				us.setName(name);
				us.setSurname(surname);
				us.setEmail(email);
				us.setPhone(Integer.parseInt(phone));
				us.setCountry(country);
				us.setAddress(address);
				us.setAdministrator(false);
				
				try {
					//If the user already exits in the database
					if(ToServer.exists(nick))
					{
						Toast.makeText(getApplicationContext(), "The nick already exist. You have to change.", Toast.LENGTH_LONG).show();
					}
					else
					{
						//Save the user
						boolean succesfully = ToServer.newUser(us, password);
						
						//If the user has been saved in the database
						if(succesfully)
						{
							Toast.makeText(getApplicationContext(), "The user has been saved.", Toast.LENGTH_LONG).show();
							this.finish();
						}
						else
							Toast.makeText(getApplicationContext(), "Error, the user has not been saved.", Toast.LENGTH_LONG).show();
						
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Failed while trying to save the user." + e.getMessage(), Toast.LENGTH_LONG).show();
				
					e.printStackTrace();
				}
	
			}
		}
		//If the user press the cancel button
		else if(sourceButton==this.bCancel)
		{
			this.finish();
		}			
	}
}
