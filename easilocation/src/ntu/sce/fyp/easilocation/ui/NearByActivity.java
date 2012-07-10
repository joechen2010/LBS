package ntu.sce.fyp.easilocation.ui;

import ntu.sce.fyp.easilocation.R;
import ntu.sce.fyp.easilocation.util.UIUtils;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Browser;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class NearByActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nearby);
		
		((TextView) findViewById(R.id.title_text)).setText("Near by places");

//		String[] projection = new String[] { Browser.BookmarkColumns._ID, Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL };
//		String[] displayFields = new String[] { Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL };
//		int[] displayViews = new int[] { android.R.id.text1, android.R.id.text2 };
//
//		Cursor cur = managedQuery(android.provider.Browser.BOOKMARKS_URI, projection, null, null, null);
//		setListAdapter(new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cur, displayFields, displayViews));
	}
	
	/** Handle "home" title-bar action. */
    public void onHomeClick(View v) {
        UIUtils.goHome(this);
    }
    
    /** Handle "search" title-bar action. */
    public void onSearchClick(View v) {
    	UIUtils.goSearch(this);
    }

    /** Handle "refresh" title-bar action. */
    public void onRefreshClick(View v) {
    }
    
}
