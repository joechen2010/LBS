package cn.edu.nju.software.gof.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PlaceOfferActivity extends Activity {
	private TextView text;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.place_offer);
		
		refresh();
	}

	private void refresh() {
		// TODO Auto-generated method stub
		text = (TextView)findViewById(R.id.offer_name);
		text.setText("“别咬我”酒楼吃“一”送“一”");
		
		text = (TextView)findViewById(R.id.offer_content);
		text.setText("“牧童”优惠信息：凭此信息可至“别咬我”酒楼享受吃“一”送“一”的特大优惠活动，好机会不容错过，赶快行动吧！");
		
		text = (TextView)findViewById(R.id.offer_time);
		text.setText("有效期：2010年10月13日至2010年10月23日");
	}
}
