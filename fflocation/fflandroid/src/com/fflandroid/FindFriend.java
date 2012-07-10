package com.fflandroid;

import java.util.List;

import com.fflandroid.R;
import com.model.KUserInfo;
import com.web_services.ToServer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * This Activity is used to search users in the database
 * @author Juan Javier García, Aitor Martin, Andreas Kapouranis
 *
 */
public class FindFriend extends Activity implements OnClickListener{
	
	//Interface's buttons
	private Button bSearch;
	private Button bCancel;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find);
        
        //Fit the interface's buttons with local variables
        bSearch = (Button) findViewById(R.id.find_bSearch);
        bSearch.setOnClickListener(this);     

        bCancel = (Button) findViewById(R.id.find_bCancel);
        bCancel.setOnClickListener(this); 
        
    }
    
  
    
	@Override
	public void onClick(View v) {
		Button sourceButton=(Button)v;
		
		//If the user press the button search
		if(sourceButton==bSearch)
		{		
			//Read the TextView
			EditText textName = (EditText)findViewById(R.id.find_tName);
			EditText textCity = (EditText)findViewById(R.id.find_tCity);
			EditText textNick = (EditText)findViewById(R.id.find_tNick);
			EditText textSurname = (EditText)findViewById(R.id.find_tSurname);
			
			//Translate into Strings
			String name = textName.getText().toString();
			String city = textCity.getText().toString();
			String nick = textNick.getText().toString();
			String surname = textSurname.getText().toString();
		 	
			try {
				//Read the users founds.
				List<KUserInfo> listUsers = ToServer.searchFriend(nick, name, surname, city);
				
				
				//Prepare to send the ids and nicks to ListFriends
				String[] listItems = new String[listUsers.size()];
				int [] listIds = new int[listUsers.size()];
				
				for(int i=0; i< listUsers.size(); i++)
				{
					listItems[i] = listUsers.get(i).getNick();
					listIds[i] = listUsers.get(i).getId();
				}
				
				if(listUsers.size()>0)
				{
				
					//Start the new activity
					Intent newIntent = new Intent(this, ListFriends.class);
					newIntent.putExtra("items", listItems);	
					newIntent.putExtra("ids", listIds);
			
					this.startActivityForResult(newIntent, 0);	
				}
				else
				{
					Toast.makeText(getApplicationContext(), "No matches found for the search.", Toast.LENGTH_LONG).show();
					
				}
			} catch (Exception e) {
			
				Toast.makeText(getApplicationContext(), "Failed while trying to find friends." + e.getMessage(), Toast.LENGTH_LONG).show();
				
			}
			
	
		}
		//If the user press the button cancel
		else if(sourceButton==bCancel)
		{
			this.finish();
		}
	}
     
}
