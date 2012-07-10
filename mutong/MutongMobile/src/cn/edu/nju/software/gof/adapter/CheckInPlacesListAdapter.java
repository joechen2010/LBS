package cn.edu.nju.software.gof.adapter;

import java.util.List;

import cn.edu.nju.software.gof.activity.R;
import cn.edu.nju.software.gof.beans.CheckInInformationBean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CheckInPlacesListAdapter extends BaseAdapter{
	private Context context = null;
	private List<CheckInInformationBean> objects;
	
	public CheckInPlacesListAdapter(Context context, List<CheckInInformationBean> objects){
		this.context = context;
		this.objects = objects;
	}
	
	@Override
	public View getView(int position, View convertView,ViewGroup parent){
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.checkin_item, parent, false);
		}
		CheckInInformationBean bean = objects.get(position);
		//
		TextView place_no = (TextView) convertView.findViewById(R.id.number);
		place_no.setText(Integer.toString(position+1));
		//
		TextView place_name = (TextView) convertView.findViewById(R.id.place_name);
		place_name.setText(bean.getPlaceName());
		//
		TextView reach_time = (TextView) convertView.findViewById(R.id.reach_time);
		reach_time.setText(Integer.toString(bean.getMyCheckInTimes()));
		return convertView;
	}

	@Override
	public int getCount() {
		return this.objects.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
