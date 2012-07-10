package org.triplemaps.utils;

import android.content.Context;
import android.widget.Toast;

public class Utilities 
{	
	public static void showToast(CharSequence message, Context appContext)
    {
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(appContext, message, duration);
		toast.show();
    }
}
