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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ExpandableListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ExpandableListAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.ExpandableListView.OnChildClickListener;

public class ContactsList extends ExpandableListActivity implements OnChildClickListener {

    private ExpandableListAdapter listAdapter;
    private final String GROUP_NAME = "contactName";
    private final String CHILD_NAME = "contactEmail";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create a uri to access contacts
        Uri mContacts = Contacts.ContactMethods.CONTENT_URI;

        // select the columns that we wish to get in query
        String[] projection = new String[] { Contacts.ContactMethods.PERSON_ID, Contacts.ContactMethods.DISPLAY_NAME,
                Contacts.ContactMethods.KIND, Contacts.ContactMethods.DATA };

        // create a cursor which holds query results (like ResultSet in java)
        Cursor c = this.managedQuery(mContacts, projection, null, null, Contacts.ContactMethods.DISPLAY_NAME + " ASC");

        List<Map<String, String>> groups = new ArrayList<Map<String, String>>();
        List<List<Map<String, String>>> children = new ArrayList<List<Map<String, String>>>();

        List<Map<String, String>> childrenForAGroup = new ArrayList<Map<String, String>>();

        // move to the first row of the results table
        c.moveToFirst();
        int previousId = -1;

        do {
            int personId = c.getInt(c.getColumnIndex(Contacts.ContactMethods.PERSON_ID));

            // if this person is already added in list, do not add again but
            // just add it's information
            if (personId != previousId) {
                String personName = c.getString(c.getColumnIndex(Contacts.ContactMethods.DISPLAY_NAME));

                Map<String, String> groupMap = new HashMap<String, String>();
                groupMap.put(GROUP_NAME, personName);
                groups.add(groupMap);

                if (previousId != -1) {
                    children.add(childrenForAGroup);
                    childrenForAGroup = new ArrayList<Map<String, String>>();
                }
            }

            int dataKind = c.getInt(c.getColumnIndex(Contacts.ContactMethods.KIND));

            // Pick only data with id=1 which represents email address
            if (dataKind == 1) {
                String personEmail = c.getString(c.getColumnIndex(Contacts.ContactMethods.DATA));

                Map<String, String> curChildMap = new HashMap<String, String>();
                curChildMap.put(CHILD_NAME, personEmail);
                childrenForAGroup.add(curChildMap);
            }

            previousId = personId;

        } while (c.moveToNext());

        // add the last set of children to the children's list
        children.add(childrenForAGroup);

        // find out the indexes of empty children lists and remove them and
        // their respective groups
        List indexes = this.findEmptyChildrenLists(children);
        for (int i = 0; i < indexes.size(); i++) {
            groups.remove(Integer.parseInt(indexes.get(i).toString()));
            children.remove(Integer.parseInt(indexes.get(i).toString()));
        }

        // Set up list adapter
        listAdapter = new SimpleExpandableListAdapter(
                this,
                groups,
                android.R.layout.simple_expandable_list_item_1,
                new String[] { GROUP_NAME },
                new int[] { android.R.id.text1 },
                children,
                android.R.layout.simple_list_item_multiple_choice,
                new String[] { CHILD_NAME },
                new int[] { android.R.id.text1 }
        );
        setListAdapter(listAdapter);
    }

    private List findEmptyChildrenLists(List<?> list) {
        List indexes = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            if (((List<?>) list.get(i)).isEmpty()) {
                indexes.add(i);
            }
        }
        return indexes;
    }

    private void removeEmptyGroupsAndChildren(List indexes, List groups, List children) {
        for (int i = 0; i < indexes.size(); i++) {
            groups.remove(indexes.get(i));
            children.remove(indexes.get(i));
        }
    }

    @Override
    public boolean onChildClick(android.widget.ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//        Intent data = new Intent();
//        data.putExtra("email", ((TextView) v).getText().toString());
//        setResult(Activity.RESULT_OK, data);
//        finish();
//        return true;
        
        CheckedTextView tempView = (CheckedTextView) v.findViewById(android.R.id.text1);
        tempView.setChecked(!tempView.isChecked());
        return super.onChildClick(parent, v, groupPosition, childPosition, id);
    }
}
