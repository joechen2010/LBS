package cn.edu.nju.software.gof.adapter;

import java.util.List;

import cn.edu.nju.software.gof.activity.R;
import cn.edu.nju.software.gof.beans.CheckInInformationBean;
import cn.edu.nju.software.gof.beans.CommentBean;
import cn.edu.nju.software.gof.viewbeans.CommentWithAvatarBean;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentsAdapter extends BaseAdapter {
	
	private Context context = null;
	private List<CommentWithAvatarBean> objects;
	
	public CommentsAdapter(Context context, List<CommentWithAvatarBean> objects){
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
			convertView = inflater.inflate(R.layout.comment_list_item, parent, false);
		}
		CommentWithAvatarBean bean = objects.get(position);
		//
		ImageView avatorView = (ImageView) convertView.findViewById(R.id.friend_comment_item);
		if(bean.getAvatar() != null){
			avatorView.setImageDrawable(bean.getAvatar());
		}else {
			avatorView.setImageResource(R.drawable.lou);
		}
		TextView place_no = (TextView) convertView.findViewById(R.id.comment_owner_name);
		place_no.setText(bean.getOwnerName());
		//
		TextView place_name = (TextView) convertView.findViewById(R.id.comment_content);
		place_name.setText(bean.getContent());
		//
		TextView reach_time = (TextView) convertView.findViewById(R.id.comment_time);
		reach_time.setText(bean.getTime());
		return convertView;
	}

}
