package ntu.sce.fyp.easilocation.ui;

import ntu.sce.fyp.easilocation.R;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Contacts;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendsListActivity extends ListActivity {

	private static final Uri mContactsUri = ContactsContract.Contacts.CONTENT_URI;
	private static final String mSelection = "HAS_PHONE_NUMBER = 1";
	private static final String[] mProjection = { Contacts._ID, Contacts.DISPLAY_NAME, Contacts.HAS_PHONE_NUMBER, Contacts.LOOKUP_KEY };
	private static final String mOrder = Contacts.DISPLAY_NAME + " ASC";
	private static final String[] mSelectionArgs = new String[]{};
	
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_friends_list);
		
		/** retrieve contacts which have phone numbers and display them*/
		Cursor c = managedQuery(mContactsUri, mProjection, mSelection, mSelectionArgs, mOrder);
		FriendListAdapter mAdapter = new FriendListAdapter(getApplicationContext(), c);
		setListAdapter(mAdapter);
		
	}
	
	/** {@inheritDoc} */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	Toast.makeText(this, "Contact clicked", Toast.LENGTH_SHORT).show();
    }
	
	/** 
     * Cursor adapter for list view activity, cursor will 
     * traverse through contact tables to get information 
     */
    private class FriendListAdapter extends CursorAdapter {
    	private final Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    	
    	/**
    	 * {@link CursorAdapter} that renders a {@link TracksQuery}.
    	 */
    	public FriendListAdapter(Context context, Cursor cursor) {
    		super(context, cursor);
    	}

    	/** {@inheritDoc} */
    	@Override
    	public View newView(Context context, Cursor cursor, ViewGroup parent) {
    		return getLayoutInflater().inflate(R.layout.list_item_friend, parent, false);
    	}

    	/** {@inheritDoc} */
    	@Override
    	public void bindView(View view, Context context, Cursor cursor) {
    		// obtain references to text views
    		final TextView name = (TextView) view.findViewById(android.R.id.text1);
    		final TextView phone = (TextView) view.findViewById(android.R.id.text2);
    		
    		String contactId = cursor.getString(cursor.getColumnIndex(Contacts._ID));
    		String contactName = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME));
    		
    		/** append contact name to list item view */
    		name.setText(contactName);
    		/** append contact phones to list item view */
			Cursor phoneCur = getContentResolver().query(
					phoneUri, null, 
					CommonDataKinds.Phone.CONTACT_ID + " = ?", 
					new String[] { contactId }, 
					null);
			StringBuilder sb = new StringBuilder();
			// handle if there are many phone numbers
			while (phoneCur.moveToNext()) {
				String phoneNum = phoneCur.getString(phoneCur.getColumnIndex(CommonDataKinds.Phone.NUMBER));
				sb.append(phoneNum + " ");
			}
			// release all resources of phone cursor and make it completely invalid
			phoneCur.close();
			phone.setText(sb.toString());
    	}
    }
}
