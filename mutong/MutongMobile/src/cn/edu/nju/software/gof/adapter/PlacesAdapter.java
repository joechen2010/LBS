package cn.edu.nju.software.gof.adapter;

import java.util.List;

import cn.edu.nju.software.gof.activity.R;
import cn.edu.nju.software.gof.beans.BreifPlaceInformationBean;
import cn.edu.nju.software.gof.viewbeans.NearbyPlaceInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlacesAdapter extends BaseAdapter {

	private Context context = null;
	private List<NearbyPlaceInfo> objects;
	
	public PlacesAdapter(Context context, List<NearbyPlaceInfo> objects){
		this.context = context;
		this.objects = objects;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return objects.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.place_list_item, parent, false);
		}
		NearbyPlaceInfo bean = objects.get(position);
		//
		ImageView avatorView = (ImageView) convertView.findViewById(R.id.place_nearby_item);
		if(bean.getAvatar() != null){
			avatorView.setImageDrawable(bean.getAvatar());
		} else {
			avatorView.setImageResource(R.drawable.lou);
		}
		TextView place_name = (TextView) convertView.findViewById(R.id.place_item_title);
		place_name.setText(bean.getPlaceName());
		//
		TextView place_address = (TextView) convertView.findViewById(R.id.place_item_address);
		place_address.setText(bean.getCurrentMoney());
		//
		return convertView;
	}

}
