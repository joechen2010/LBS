package cn.edu.nju.software.gof.intent;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

public class CommonIntents {
	
	public static final int NETWORK_SETTING = 0;
	

	public static void goToNetworkSetting(Context context) {
		Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
		context.startActivity(intent);
	}
}
