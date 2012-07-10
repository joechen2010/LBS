package cn.edu.nju.software.gof.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class WelcomeActivity extends Activity{
	private final int SPLASH_DISPLAY_LENGHT = 500; //延迟0.5秒 
	  
    @Override 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        //getWindow().requestFeature(Window.FEATURE_PROGRESS); //去标题栏 
        requestWindowFeature(Window.FEATURE_NO_TITLE); //隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        setContentView(R.layout.splash); 
        new Handler().postDelayed(new Runnable(){ 
  
         @Override 
         public void run() { 
             Intent mainIntent = new Intent(getApplicationContext(), LoginActivity.class); 
             startActivity(mainIntent); 
             finish();
         } 
            
        }, SPLASH_DISPLAY_LENGHT); 
    } 
}
