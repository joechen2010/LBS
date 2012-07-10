package cn.edu.nju.software.gof.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.nju.software.gof.activity.R;
import cn.edu.nju.software.gof.viewbeans.FriendRequestInfo;

public class FriendRequestAdapter extends BaseAdapter {
	private List<FriendRequestInfo> results = null;
	private Context context;

	public FriendRequestAdapter(Context context, List<FriendRequestInfo> friends) {
		this.context = context;
		this.results = friends;
	}

	@Override
	public int getCount() {
		return results.size();
	}

	@Override
	public Object getItem(int position) {
		return results.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.friend_search_request_item,
					parent, false);
		}
		FriendRequestInfo info = results.get(position);
		//
		ImageView avatar = (ImageView) convertView
				.findViewById(R.id.friend_avatar);
		if (info.getAvatar() != null) {
			avatar.setImageDrawable(info.getAvatar());
		} else {
			avatar.setImageResource(R.drawable.lou);
		}
		//
		TextView realName = (TextView) convertView
				.findViewById(R.id.friend_user_name);
		realName.setText(info.getFriendUserName());
		//
		TextView location = (TextView) convertView
				.findViewById(R.id.friend_real_name);
		location.setText(info.getFriendRealName());
		return convertView;
	}
}
