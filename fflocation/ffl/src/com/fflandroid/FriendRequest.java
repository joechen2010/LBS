package com.fflandroid;

import java.util.ArrayList;
import java.util.List;

import com.fflandroid.R;
import com.model.KUserInfo;
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
 * This Activity is used to show the user the requests for friend that he has.
 * @author Juan Javier García, Aitor Martin, Andreas Kapouranis
 *
 */
public class FriendRequest extends ListActivity implements OnClickListener{

	//Local variables
	List<KUserInfo> listUsers;
	private Button bAddFriend;
	private Button bCancel;
	private List<Boolean> listToAdd;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_request);

		try {
			//Get the friends requests.
			listUsers = ToServer.getRequests();
			String[] listItems = new String[listUsers.size()];
			listToAdd = new ArrayList<Boolean>();
			
			for(int i=0; i< listUsers.size(); i++)
			{
				listItems[i] = listUsers.get(i).getNick();
				listToAdd.add(false);
			}
			this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked,listItems));
							
		} catch (Exception e) {
			 Toast.makeText(getApplicationContext(), "Failed looking for friend requests: " + e.getMessage(), Toast.LENGTH_LONG).show();
			 this.finish();
		}		
		

		//Fit the interface's buttons to a local variables
        bAddFriend = (Button) findViewById(R.id.listrequest_bAdd);
        bAddFriend.setOnClickListener(this);     

        bCancel = (Button) findViewById(R.id.listrequest_bCancel);
        bCancel.setOnClickListener(this); 
       
	}

	@Override
	protected void onListItemClick( ListView l, View v, int position, long id)
	{
		//Set checked/unchecked the selected item
		CheckedTextView textView = (CheckedTextView)v;
		textView.setChecked(!textView.isChecked());
		listToAdd.set(position, textView.isChecked());
		
		super.onListItemClick(l, v, position, id);
	}


	@Override
	public void onClick(View v) {
		Button sourceButton=(Button)v;
		boolean fail = false;
	
		//If the user press the button add friends
		if(sourceButton==bAddFriend)
		{
			//For all the items
			for(int i=0; i<this.getListView().getCount(); i++)
			{
				//If the item is checked
				if(listToAdd.get(i) == true)
				{
					try {
						//Ask for friend
						if(!ToServer.askFriend(listUsers.get(i).getId()))
							fail = true;
						
					} catch (Exception e) {
						fail = true;
					}
				}
			}
			
			if(!fail)
			{
				Toast.makeText(getApplicationContext(), "All the friends have been added sucesfully.", Toast.LENGTH_LONG).show();
			}
			else
				Toast.makeText(getApplicationContext(), "Failed while trying to save friends ", Toast.LENGTH_LONG).show();

			//Finish
			this.finish();
	
		}
		//If the user press the button finish
		else if(sourceButton==bCancel)
		{
			this.finish();			
		}		
		
	}
}
