package cn.edu.nju.software.gof.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class CustomizeInformationActivity extends Activity {
	private ListView list;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.place_list_main);
		this.setTitle("美食");
		
		refresh();
	}

	private void refresh() {
		// TODO Auto-generated method stub
		list = (ListView)findViewById(R.id.MyListView);
		
		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();   
	    for(int i = 0; i < 15; i++) {   
	        HashMap<String, String> map = new HashMap<String, String>();   
	        map.put("ItemTitle", "This is Title.....");   
	        map.put("ItemText", "This is text.....");   
	        mylist.add(map);   
	    }   
	    //生成适配器，数组===》ListItem   
	    SimpleAdapter mSchedule = new SimpleAdapter(this,
	    		mylist,//数据来源    
	    		R.layout.place_list_item,//ListItem的XML实现   
	                                                
	    		//动态数组与ListItem对应的子项           
	    		new String[] {"ItemTitle", "ItemText"},    
	                                                
	    		//ListItem的XML文件里面的两个TextView ID  
	    		new int[] {R.id.place_item_title,R.id.place_item_address});   
	    //添加并且显示   
	    list.setAdapter(mSchedule);
	    
	    registerEventHandler(list);
	}
	
	private void registerEventHandler(ListView lv) {
		// TODO Auto-generated method stub
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				int itemId = arg2 + 1;
				goToDetail(itemId);
			}
		});
	}
	
	private void goToDetail(int id) {
		Bundle bundle = new Bundle();
		bundle.putInt("number", 0);
		
		Intent intent = new Intent();
		intent.putExtras(bundle);
		intent.setClass(getApplicationContext(), 
				PlaceInformationActivity.class);
		this.startActivity(intent);
		CustomizeInformationActivity.this.finish();
	}
}
