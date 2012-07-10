package cn.edu.nju.software.lcy;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

import com.google.android.maps.MapActivity;

public class MainApplication extends MapActivity {
	
	private TabHost tabs;
	private ListView list;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maintab);
        
        initialTab();
	}

	private void initialTab() {
		// TODO Auto-generated method stub
		this.tabs = (TabHost)findViewById(R.id.tabhost);
		tabs.setup();
		TabHost.TabSpec spec = tabs.newTabSpec("tag1");
		spec.setContent(R.id.tag1);
		spec.setIndicator("��ͼ", getResources().getDrawable(R.drawable.icon));
		tabs.addTab(spec);
		spec = tabs.newTabSpec("tag2");
		spec.setContent(R.id.tag2);
		spec.setIndicator("����", getResources().getDrawable(R.drawable.icon));
		tabs.addTab(spec);
		spec = tabs.newTabSpec("tag3");
		spec.setContent(R.id.tag3);
		spec.setIndicator("�ص�", getResources().getDrawable(R.drawable.icon));
		tabs.addTab(spec);
		spec = tabs.newTabSpec("tag4");
		spec.setContent(R.id.tag4);
		spec.setIndicator("ǩ��", getResources().getDrawable(R.drawable.icon));
		tabs.addTab(spec);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
    	MenuInflater inflater = getMenuInflater();
    	//����menu����Ϊres/menu/loginMenu.xml
    	//According to different tabs, set up different menu
    	switch (tabs.getCurrentTab()) {
    	case 0:
    		inflater.inflate(R.menu.mapmenu, menu);
    		break;
    	case 1:
    		inflater.inflate(R.menu.friendmenu, menu);
    		freshFriendView();
    		break;
    	case 2:
    		inflater.inflate(R.menu.placemenu, menu);
    		break;
    	case 3:
    		inflater.inflate(R.menu.checkoutmenu, menu);
    		break;
    	}
    	return super.onCreateOptionsMenu(menu);
    }
	private void freshFriendView() {
		// TODO Auto-generated method stub
		//��XML�е�ListView����ΪItem������
		
		//��ɶ�̬���飬����ת�����
		
	}

	public boolean onOptionsItemSelected(MenuItem item) {
    	int item_id = item.getItemId();
    	
    	switch (item_id) {
    	case R.id.refreshmap:
    		this.tabs.setCurrentTab(0);
    		break;
    	
    	case R.id.viewfriends:
    		this.tabs.setCurrentTab(1);
    		break;
    		
    	case R.id.viewplace:
    		this.tabs.setCurrentTab(2);
    		break;
    	
    	case R.id.newplace:
    		this.tabs.setCurrentTab(0);
    		break;
    		
    	case R.id.mainexit:
    		this.finish();
    		break;
    		
    	case R.id.more:
    		break;
    	}
    	return true;
    }
}
