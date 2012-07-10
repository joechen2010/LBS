/* Copyright (C) 2009  Axel Müller <axel.mueller@avanux.de> 
 * 
 * This file is part of LiveTracker.
 * 
 * LiveTracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * LiveTracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with LiveTracker.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.avanux.android.livetracker2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

/**
 * 
 * - check isStartable(Activity) before starting this activity : in forum fragen, ob es dafür standard-Lösung gibt
 * - filters out contacts having no email address 
 * 
 * @author axel
 *
 */
public class InviteContactsByEmailActivity extends ExpandableListActivity implements OnChildClickListener {

    private final static String TAG = "LiveTracker:InviteContactsByEmailActivity";
    
    private final String CONTACT_NAME = "contactName";
    private final String EMAIL_ADDRESS = "contactEmail";
    private final String EMAIL_ADDRESS_TYPE = "contactEmailType";

    public static final int MENU_ITEM_ID_SEND_INVITATION = Menu.FIRST;
    
    public static final String EXTRA_TRACKING_ID = "TRACKING_ID";
    
    private Collection<String> selectedEmailAddresses = new HashSet<String>();
    
    private String trackingID;

    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.trackingID = getIntent().getStringExtra(EXTRA_TRACKING_ID);
        
        List<Map<String, String>> allContactNamesWithDisplayName = new ArrayList<Map<String, String>>();
        List<List<Map<String, String>>> children = new ArrayList<List<Map<String, String>>>();
        List<Map<String, String>> emailAddressesOfContact = new ArrayList<Map<String, String>>();

        CharSequence[] emailAddressTypes = getResources().getTextArray(android.R.array.emailAddressTypes);

//        Set<Integer> types = new HashSet<Integer>();
        
        int previousPersonId = -1;
        Cursor cursor = getContactCursor(this);
        do {
            int personId = cursor.getInt(cursor.getColumnIndex(Contacts.ContactMethods.PERSON_ID));

            // if this person is already added in list, do not add again but just add it's information
            if (personId != previousPersonId) {
                Map<String, String> contactNamesWithDisplayName = new HashMap<String, String>();
                contactNamesWithDisplayName.put(CONTACT_NAME, cursor.getString(cursor.getColumnIndex(Contacts.ContactMethods.DISPLAY_NAME)));
                allContactNamesWithDisplayName.add(contactNamesWithDisplayName);

                if (previousPersonId != -1) {
                    children.add(emailAddressesOfContact);
                    emailAddressesOfContact = new ArrayList<Map<String, String>>();
                }
            }

            
            // kindOfContactMethod: 1 represents email address (no constant found yet where this value is defined)
            int kindOfContactMethod = cursor.getInt(cursor.getColumnIndex(Contacts.ContactMethods.KIND));
            if (kindOfContactMethod == 1) {
                Map<String, String> emailAddressWithAttributes = new HashMap<String, String>();
                emailAddressWithAttributes.put(EMAIL_ADDRESS, cursor.getString(cursor.getColumnIndex(Contacts.ContactMethods.DATA)));
                
                /**
                 * Unfortunately values of getColumnIndex(Contacts.ContactMethods.TYPE) and android.R.array.emailAddressTypes don't really match
                 * and have to be mapped therefore:
                 * 
                 * Value   emailAddressTypes   getColumnIndex(Contacts.ContactMethods.TYPE)
                 *   0       HOME                CUSTOM
                 *   1       WORK                HOME
                 *   2       OTHER               WORK
                 *   3       CUSTOM              OTHER
                 */
                int typeOfContactMethod = cursor.getInt(cursor.getColumnIndex(Contacts.ContactMethods.TYPE)) - 1;
                if(typeOfContactMethod == -1) {
                	typeOfContactMethod = 3;
                }
//                types.add(typeOfContactMethod);                
                
                if(typeOfContactMethod >= 0 && typeOfContactMethod < emailAddressTypes.length) {
                    emailAddressWithAttributes.put(EMAIL_ADDRESS_TYPE, emailAddressTypes[typeOfContactMethod].toString());
                }
                else {
                    emailAddressWithAttributes.put(EMAIL_ADDRESS_TYPE, emailAddressTypes[2].toString());
                }
                
                emailAddressesOfContact.add(emailAddressWithAttributes);
            }

            previousPersonId = personId;

        } while (cursor.moveToNext());

        // FIXME
//        StringBuilder text = new StringBuilder("types=");
//        for (Integer type : types) {
//			text.append(type + " ");
//		}        
//        text.append("\n");
//        for (int i = 0; i < emailAddressTypes.length; i++) {
//			text.append(i + "=" + emailAddressTypes[i] + " ");
//		}
//    	Toast.makeText(InviteContactsByEmailActivity.this, text, Toast.LENGTH_LONG).show();        
        
        
        // add the last set of children to the children's list
        children.add(emailAddressesOfContact);

        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                this,
                allContactNamesWithDisplayName,
                android.R.layout.simple_expandable_list_item_1,
                new String[] { CONTACT_NAME },
                new int[] { android.R.id.text1 },
                children,
                R.layout.email_list_item,
                new String[] { EMAIL_ADDRESS_TYPE, EMAIL_ADDRESS },
                new int[] { android.R.id.text1, android.R.id.text2 }
        );
        setListAdapter(adapter);
    }

    @Override
    public boolean onChildClick(android.widget.ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        // update child view
        CheckedTextView tempView = (CheckedTextView) v.findViewById(android.R.id.text2);
        tempView.setChecked(!tempView.isChecked());
        
        // update list of selected email addresses
        String emailAddress = ((TextView) tempView).getText().toString();
        if(tempView.isChecked()) {
            this.selectedEmailAddresses.add(emailAddress);
        }
        else {
            this.selectedEmailAddresses.remove(emailAddress);
        }
        
        return super.onChildClick(parent, v, groupPosition, childPosition, id);
    }

    private static Cursor getContactCursor(Activity activity) {
        // the columns the query should return
        String[] returnColumns = new String[] {
                Contacts.ContactMethods.PERSON_ID,
                Contacts.ContactMethods.DISPLAY_NAME,
                Contacts.ContactMethods.KIND,
                Contacts.ContactMethods.DATA,
                Contacts.ContactMethods.TYPE
                };

        // select returnColumns of all contacts and sort by display name
        Cursor cursor = activity.managedQuery(Contacts.ContactMethods.CONTENT_URI, returnColumns, null, null, Contacts.ContactMethods.DISPLAY_NAME + " ASC");
        // move to the first row of the results table
        cursor.moveToFirst();
        return cursor;
    }
    
    public static boolean isStartable(Activity activity) {
        Cursor cursor = getContactCursor(activity);
        return cursor.getCount() > 0;
    }

    
    // ~ Options Menu ---------------------------------------------------------
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(MENU_ITEM_ID_SEND_INVITATION, MENU_ITEM_ID_SEND_INVITATION, 0, R.string.menu_send_invitation)
            .setIcon(R.drawable.ic_menu_send);
        return result;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(this.selectedEmailAddresses.size() > 0) {
            menu.setGroupEnabled(MENU_ITEM_ID_SEND_INVITATION, true);
        }
        else {
            menu.setGroupEnabled(MENU_ITEM_ID_SEND_INVITATION, false);
        }
        
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_ITEM_ID_SEND_INVITATION:
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, (String[]) this.selectedEmailAddresses.toArray(new String[] {}));
            intent.putExtra(Intent.EXTRA_SUBJECT, getText(R.string.mail_subject));
            intent.putExtra(Intent.EXTRA_TEXT, getText(R.string.mail_body) + " " + Configuration.getServerBaseUrl() + "/?trackingID=" + this.trackingID);
            
            /* The "MessageCompose" activity of the default "Email" application
             * uses the following intent filters for action SEND:
             * 
             *  text/plain, image/*, video/*
             * 
             * The "MessageCompose" activity of the K9 email application
             * uses the following intent filters for action SEND:
             *  
             *  text/*, image/*, message/*
             * 
             * This leaves "text/plain" as the only choice in order to be able to use both
             * email applications. Unfortunately the "Messaging" (SMS) application is also
             * registered for "text/plain" :-(
             * Hopefully the default "Email" application will also support something like
             * "message/rfc822" in the future. 
             */
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent, "Send invitations"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
}
