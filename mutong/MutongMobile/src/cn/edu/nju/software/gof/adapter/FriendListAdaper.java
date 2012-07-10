package cn.edu.nju.software.gof.adapter;

import java.util.List;

import cn.edu.nju.software.gof.activity.R;
import cn.edu.nju.software.gof.beans.BreifPlaceInformationBean;
import cn.edu.nju.software.gof.beans.FriendNearbyInformationBean;
import cn.edu.nju.software.gof.viewbeans.FriendInfo;
import cn.edu.nju.software.gof.viewbeans.NearbyFriendInfo;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendListAdaper extends BaseAdapter {

	private Context context = null;
	private List<NearbyFriendInfo> objects;
	
	public FriendListAdaper(Context context, List<NearbyFriendInfo> objects){
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
			convertView = inflater.inflate(R.layout.nearby_friend_list_item, parent, false);
		}
		NearbyFriendInfo bean = objects.get(position);
		//
		ImageView avatorView = (ImageView) convertView.findViewById(R.id.friend_nearby_item);
		if(bean.getAvatar() != null){
			avatorView.setImageDrawable(bean.getAvatar());
		}else {
			avatorView.setImageResource(R.drawable.lou);
		}
		TextView friend_name = (TextView) convertView.findViewById(R.id.nearby_friend_name);
		friend_name.setText(bean.getFriendName());
		//
		TextView friend_time = (TextView) convertView.findViewById(R.id.nearby_friend_time);
		friend_time.setText(bean.getTime());
		//
		return convertView;
	}
}
