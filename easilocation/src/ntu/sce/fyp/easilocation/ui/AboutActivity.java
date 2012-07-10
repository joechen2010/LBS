package ntu.sce.fyp.easilocation.ui;

import ntu.sce.fyp.easilocation.R;
import ntu.sce.fyp.easilocation.util.UIUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		((TextView)findViewById(R.id.title_text)).setText("About");
	}
	
	public void onHomeClick(View v) {
		UIUtils.goHome(this);
	}
	
	public void onSearchClick(View v) {
		UIUtils.goSearch(this);
	}
}
