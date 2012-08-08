package cn.edu.nju.software.gof.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import cn.edu.nju.software.gof.activity.MyApplication;
import cn.edu.nju.software.gof.beans.User;
import cn.edu.nju.software.gof.gps.GpsDetector;

public class GPSService extends Service{
	
	public static final String ACTION = "android.intent.action.GPS_SERVICE"; 
	
	  @Override
	  public void onCreate(){
	  }
	  
	  @Override
	  public void onDestroy(){
	   // TODO Auto-generated method stub
	   super.onDestroy();
	  }


	  @Override
	  public boolean onUnbind(Intent intent){
	   // TODO Auto-generated method stub
	   super.onUnbind(intent);
	   return true;
	  }

	  @Override
	  public IBinder onBind(Intent intent){
	   // TODO Auto-generated method stub
	   return null;
	  }
	  
	  @Override
	  public void onStart(Intent intent,int startId){
	      super.onStart(intent, startId);
	      GpsDetector.getInstance(this.getApplicationContext()).detect();
	      /*Intent mintent = new Intent(this,AlarmReceiver.class);
	      mintent.setAction(ACTION);
	      PendingIntent pIntent = PendingIntent.getBroadcast(this,0,mintent,0);
	      long startTime = SystemClock.elapsedRealtime();//开始时间 
	      AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
	      //每10秒发送一个广播，时间到了将发送pIntent这个广播，在alarmReceiver中接受广播
	      alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,startTime,10*1000,pIntent);*/
	   }
}