package ntu.sce.fyp.easilocation.util;

import ntu.sce.fyp.easilocation.ui.HomeActivity;
import ntu.sce.fyp.easilocation.ui.MapDetailActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class UIUtils extends Activity {

	public static void goHome(Context context) {
		final Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
	}

	public static void goSearch(Activity activity) {
		activity.startSearch(null, false, Bundle.EMPTY, false);
	}
	
	public static void goCurrentLocation(Context context) {
		Intent temp = new Intent(context, MapDetailActivity.class);
		context.startActivity(temp);
	}
	
}
