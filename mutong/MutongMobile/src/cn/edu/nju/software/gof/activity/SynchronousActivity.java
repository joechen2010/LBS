package cn.edu.nju.software.gof.activity;

import cn.edu.nju.software.gof.requests.SynchronizationUtilities;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SynchronousActivity extends ListActivity {
	
	private final int SINA_ID = 0;
	private final int RENREN_ID = 1;
	private final int TWITTER_ID = 2;
	private EditText usernameEdit;
	private EditText passwordEdit;
	String abc;
	
	private final int ERROR_DIALOG = 10;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.synchronous);
		registerEventHandler();
	}

	private void registerEventHandler() {
		// TODO Auto-generated method stub
		ListView listView = getListView();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long ID) {
				// TODO Auto-generated method stub
				MyApplication application = (MyApplication) getApplication();
				final String sessionID = (String) application.getData("sessionID");
				String website;
				Uri uri;
				Intent it;
				
				switch(position) {
				case SINA_ID:
					website = SynchronizationUtilities.getOauthAddress(sessionID, "SINA");
					uri = Uri.parse(website);
					it = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(it);
					break;
				case RENREN_ID:
					showMyDialog(sessionID, "RENREN");
					break;
				case TWITTER_ID:
					website = SynchronizationUtilities.getOauthAddress(sessionID, "TWITTER");
					uri = Uri.parse(website);
					it = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(it);
					break;
				}
			}
		});
	}
	
	private void showMyDialog(final String sessionID, final String provider) {
		// TODO Auto-generated method stub
		LayoutInflater factory = LayoutInflater.from(SynchronousActivity.this);
		//得到自定义对话框
        final View DialogView = factory.inflate(R.layout.sync_renren, null);
        //创建对话框
        Dialog dlg = new AlertDialog.Builder(SynchronousActivity.this)
        .setTitle("人人同步设置")
        .setView(DialogView)//设置自定义对话框的样式
        .setPositiveButton("确定", //设置"确定"按钮
        new DialogInterface.OnClickListener() //设置事件监听
        {
            public void onClick(DialogInterface dialog, int whichButton) 
            {
            	//输入完成后，点击“确定”开始同步
            	usernameEdit = (EditText)DialogView.findViewById(R.id.renren_user_name_edit);
            	passwordEdit = (EditText)DialogView.findViewById(R.id.renren_password_edit);
            	
            	String userName = usernameEdit.getText().toString();
            	String password = passwordEdit.getText().toString();
            	
            	SynchronizationUtilities.getOauthAddressA(sessionID, provider, userName, password);  	
            }
        }).setNegativeButton("取消", 
        		new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						return;
					}
				}).create();//创建
        dlg.show();//显示
	}
}
