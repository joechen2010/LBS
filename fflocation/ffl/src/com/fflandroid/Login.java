package com.fflandroid;



import com.fflandroid.R;
import com.web_services.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * This Activity is used to allow the user to login in the system.
 * @author Juan Javier García, Aitor Martin, Andreas Kapouranis
 *
 */
public class Login extends Activity implements OnClickListener{
    
	//local variables
	private Button bRegister;
	private Button bLogin;
	private EditText nick;
	private EditText password;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Inflate the interface
        setContentView(R.layout.login);
        
        //Fit the interface's button to local variables
        bRegister = (Button) findViewById(R.id.bRegister);
        bRegister.setOnClickListener(this);
        bLogin = (Button) findViewById(R.id.bLogin);
        bLogin.setOnClickListener(this);  
        
        String ip = getString(R.string.IP);
		//Send the ip to the server
		ToServer.setIP(ip);
    }

	@Override
	public void onClick(View v) {
		Button sourceButton=(Button)v;
		
		//If the user click the register button.
		if(sourceButton==this.bRegister)
		{
			//Start the register activity
			Intent nuevoIntent = new Intent(v.getContext(), Register.class);
    		this.startActivityForResult(nuevoIntent, 0);
		}
		//If the user click the login button
		if(sourceButton==this.bLogin)
		{
			//Get the nick and password
			nick = (EditText)findViewById(R.id.tNick);
			password = (EditText)findViewById(R.id.tPassword);

			 String nk, pw;

			 nk = nick.getText().toString();
			
			 
			 //If the user has not entered the nick.
			 if(nk.length() == 0)
					Toast.makeText(getApplicationContext(), "You must enter a nick", Toast.LENGTH_LONG).show();
			 else
			 {
				 pw = password.getText().toString();
			
				 //If the user has not entered the password
				 if(pw.length() == 0)
				 {
					 Toast.makeText(getApplicationContext(), "You must enter a password", Toast.LENGTH_LONG).show();
				 }
				 else
				 {
					 try {
						 ToServer.login(nk, pw);
						
						 //If the User/Password are correct
						 if(ToServer.logged() == true )
						 {	
							Toast.makeText(getApplicationContext(), "The user has been identified correctly", Toast.LENGTH_LONG).show();
							
							//The identification is correct. We show the map. 
					
							Intent nuevoIntent = new Intent(v.getContext(), Map.class);
							this.startActivityForResult(nuevoIntent, 0);
						
						 }
						 else
						 {
								Toast.makeText(getApplicationContext(), "The user has not been identified correctly. Please re-enter your nick/password.", Toast.LENGTH_LONG).show();
						 }
					} catch (Exception e) {
						
						
						//If we can not connect we show an alert asking for the new ip.
						final AlertDialog.Builder alert = new AlertDialog.Builder(this);
						final EditText input = new EditText(this);
												
						alert.setMessage("The current server's ip is incorrect, please enter the current server's ip");
						alert.setView(input);
						alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								String ip = input.getText().toString().trim();
								Toast.makeText(getApplicationContext(), "The current ip is " + ip,
										Toast.LENGTH_SHORT).show();
								
								ToServer.setIP(ip);
								
						
							}
						});

						alert.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int whichButton) {
										dialog.cancel();
									}
								});
						alert.show();						
					}
			 
			
				 }//else
			 
			 }//if		
			

		}//if
	}//onclick
}