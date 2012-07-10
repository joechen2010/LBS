package cn.edu.nju.software.lcy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Mutong extends Activity {
    /** Called when the activity is first created. */
    
	private TextView bottommenu;
    private Spinner spinner;
    private String[] states = {"在线", "隐身"};
    
    Button loginButton, registerButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        this.spinner = (Spinner) findViewById(R.id.stateSpinner);
        
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,
        		android.R.layout.simple_spinner_item, states);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        spinner.setAdapter(aa);
        
        this.loginButton = (Button)findViewById(R.id.loginButton);
        this.registerButton = (Button)findViewById(R.id.registerButton);
        
        loginButton.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v) {
        		Intent intent = new Intent();
        		//锟狡讹拷intent要锟斤拷锟斤拷锟斤拷锟斤拷注锟斤拷页锟斤拷
        		intent.setClass(Mutong.this, MainApplication.class);
        	    finish();
        		startActivity(intent);
        		//Mutong.this.finish();
        	}
        });
        
        registerButton.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v) {
        		Intent intent = new Intent();
        		//锟狡讹拷intent要锟斤拷锟斤拷锟斤拷锟斤拷注锟斤拷页锟斤拷
        		intent.setClass(Mutong.this, Register.class);
        	    finish();
        		startActivity(intent);
        		//Mutong.this.finish();
        	}
        });
        
        this.bottommenu = (TextView)findViewById(R.id.bottommenu);
        //定义左下角按钮（实际上是TextView）事件
        bottommenu.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bottommenu.setBackgroundResource(R.drawable.change);
				openOptionsMenu();
			}
        });
    }
    
    //锟斤拷锟斤拷menu
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	//锟斤拷锟斤拷menu锟斤拷锟斤拷为res/menu/loginMenu.xml
    	inflater.inflate(R.menu.loginmenu, menu);
    	return true;
    }
    
    //锟斤拷锟斤拷说锟斤拷录锟�    
    public boolean onOptionsItemSelected(MenuItem item) {
    	int item_id = item.getItemId();
    	
    	switch (item_id) {
    	case R.id.deleteAccount:
    		newWindow();
    		break;
    	
    	case R.id.exit:
    		Mutong.this.finish();
    		break;
    	}
    	return true;
    }
    public void  onOptionsMenuClosed  (Menu menu){
    	bottommenu.setBackgroundResource(R.drawable.home);
    }
    
    //寮瑰嚭娓呴櫎璧勬枡鐨勭‘璁ゅ璇濇
	private void newWindow() {
		LayoutInflater factory = LayoutInflater.from(Mutong.this);
		final View dialogView = factory.inflate(R.layout.deleteaccountdialog, null);
		AlertDialog dlg = new AlertDialog.Builder(Mutong.this).setTitle("清除账号")
		.setView(dialogView) 
		.setPositiveButton("清  除", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		})
		.setNegativeButton("取  消", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		}).create();
		dlg.show();
	}
}