package cn.edu.nju.software.gof.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class GPSReceiver extends BroadcastReceiver{
	
	public static final String ACTION = "android.intent.action.BOOT_COMPLETED"; 
	
    @Override
    public void onReceive(Context context,Intent intent){
         intent = new Intent(context,GPSService.class);
         context.startService(intent);
    } 
}
