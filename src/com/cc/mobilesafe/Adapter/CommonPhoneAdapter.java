package com.cc.mobilesafe.Adapter;

import java.util.List;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.Bean.CommonPhoneChildBean;
import com.cc.mobilesafe.Bean.CommonPhoneGroupBean;
import com.cc.mobilesafe.Bean.CommonPhoneGroupCombineChild;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class CommonPhoneAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<CommonPhoneGroupCombineChild> list;

	static class GroupViewHolder {
		TextView tv_groupname;
	}

	static class ChildViewHolder {
		TextView tv_name;
		TextView tv_phone;
	}

	public CommonPhoneAdapter(Context context, List<CommonPhoneGroupCombineChild> list) {
		// TODO 自动生成的构造函数存根
		this.context = context;
		this.list = list;
	}

	@Override
	public boolean hasStableIds() {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public CommonPhoneChildBean getChild(int groupPosition, int childPosition) {
		// TODO 自动生成的方法存根
		return list.get(groupPosition).child.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO 自动生成的方法存根
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		// TODO 自动生成的方法存根
		ChildViewHolder holder = new ChildViewHolder();
		if (convertView != null) {
			holder = (ChildViewHolder) convertView.getTag();
		} else {
			convertView = View.inflate(context, R.layout.elv_commonphonechild, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);

			convertView.setTag(holder);
		}
		holder.tv_name.setText(getChild(groupPosition, childPosition).name);
		holder.tv_phone.setText("电话号码：" + getChild(groupPosition, childPosition).number);

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO 自动生成的方法存根
		return list.get(groupPosition).child.size();
	}

	@Override
	public CommonPhoneGroupCombineChild getGroup(int groupPosition) {
		// TODO 自动生成的方法存根
		return list.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO 自动生成的方法存根
		return list.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO 自动生成的方法存根
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		GroupViewHolder holder = new GroupViewHolder();
		if (convertView != null) {
			holder = (GroupViewHolder) convertView.getTag();
		} else {
			convertView = View.inflate(context, R.layout.elv_commonphonegroup, null);
			holder.tv_groupname = (TextView) convertView.findViewById(R.id.tv_groupname);
			convertView.setTag(holder);
		}
		holder.tv_groupname.setText(getGroup(groupPosition).name);

		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO 自动生成的方法存根
		return true;
	}

}
