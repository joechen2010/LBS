package cn.edu.nju.software.gof.adapter;

import java.util.List;

import cn.edu.nju.software.gof.activity.R;
import cn.edu.nju.software.gof.viewbeans.FriendInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context = null;
	private List<String> groupTitles = null;
	private List<List<FriendInfo>> childDatas = null;

	public FriendExpandableListAdapter(Context context,
			List<String> groupTitles, List<List<FriendInfo>> childDatas) {
		super();
		this.context = context;
		this.groupTitles = groupTitles;
		this.childDatas = childDatas;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childDatas.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.friend_item, parent, false);
		}
		FriendInfo friendInfo = (FriendInfo) getChild(groupPosition,
				childPosition);
		//
		ImageView avatar = (ImageView) convertView
				.findViewById(R.id.friend_avatar);
		if (friendInfo.getAvatar() != null) {
			avatar.setImageDrawable(friendInfo.getAvatar());
		}else {
			avatar.setImageResource(R.drawable.lou);
		}
		//
		TextView realName = (TextView) convertView
				.findViewById(R.id.friend_real_name);
		realName.setText(friendInfo.getFriendRealName());
		//
		TextView location = (TextView) convertView
				.findViewById(R.id.friend_location);
		location.setText(friendInfo.getLastPersonalLocation());
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childDatas.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupTitles.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groupTitles.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater
					.inflate(R.layout.friend_group, parent, false);
		}
		String title = (String) getGroup(groupPosition);
		TextView text = (TextView) convertView
				.findViewById(R.id.friend_group_title);
		text.setText(title);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
