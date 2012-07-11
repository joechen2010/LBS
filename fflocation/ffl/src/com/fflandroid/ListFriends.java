package com.fflandroid;


import java.util.ArrayList;
import java.util.List;

import com.fflandroid.R;
import com.web_services.ToServer;

import android.app.ListActivity;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * This Activity is used to show the result of the friend's search to the user
 * @author Juan Javier García, Aitor Martin, Andreas Kapouranis
 *
 */
public class ListFriends extends ListActivity implements OnClickListener{


	
	//Private variables
	private String [] listItems;
	private int [] listIds;
	
	//Buttons
	private Button bAddFriend;
	private Button bCancel;
	
	List<Boolean> listToAdd;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		//Get the information of the friends
		Bundle b = getIntent().getExtras();
		listItems =  (String[]) b.get("items");
		listIds=  (int[]) b.get("ids");
		
	
		
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked,listItems));
		listToAdd = new ArrayList<Boolean>();
		for(int i=0; i< getListAdapter().getCount(); i++)
			listToAdd.add(false);
		
		
		
		
		//Fit the interface's buttons to local variables
        bAddFriend = (Button) findViewById(R.id.list_bAdd);
        bAddFriend.setOnClickListener(this);     

        bCancel = (Button) findViewById(R.id.list_bCancel);
        bCancel.setOnClickListener(this); 		
	}
	
	
	@Override
	protected void onListItemClick( ListView l, View v, int position, long id)
	{
		//Check/Uncheck friends 
		CheckedTextView textView = (CheckedTextView)v;
		textView.setChecked(!textView.isChecked());
		
		listToAdd.set(position, textView.isChecked());
		
		 super.onListItemClick(l, v, position, id);
		
		
	}



	@Override
	public void onClick(View v) {
		Button sourceButton=(Button)v;
		boolean fail = false;

		//If the user press the button Add friend
		if(sourceButton==bAddFriend)
		{
			
			//For each friend
		 	for(int i=0; i<this.listToAdd.size();i++)
		 	{
		 	
		 		
		 		//If is checked
				if(listToAdd.get(i) == true)
				{
			
					try {		
						//Save the friend request
						if(!ToServer.askFriend(listIds[i]))
						{
							fail = true;
							Toast.makeText(getApplicationContext(), "Failed while trying to save friend request: " + listItems[i], Toast.LENGTH_LONG).show();
						}
						
					} catch (Exception e) {
						 Toast.makeText(getApplicationContext(), "Failed while trying to save friend request" + listItems[i], Toast.LENGTH_LONG).show();
						 fail = true;
						 e.printStackTrace();
						 
					}
				}
		 	}
		 	if(!fail)
		 	{
				 Toast.makeText(getApplicationContext(), "Your friend requests has been updated sucesfully.", Toast.LENGTH_LONG).show();

		 	}
	
		 	
		 	//Finish
		 	this.finish();
		}
		//If the user press the button cancel
		else if(sourceButton==bCancel)
		{
			this.finish();
			
		}		
		
	}

	
}


