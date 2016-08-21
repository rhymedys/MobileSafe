package com.cc.mobilesafe.Adapter;

import java.util.HashMap;
import java.util.List;

import com.cc.mobilesafe.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author Rhymedys
 * 防盗功能中的 contactlist adapter
 *
 */
public class MyContactlistAdapter extends BaseAdapter {

	private Context context;
	private List<HashMap<String, String>> contactlist;

	public MyContactlistAdapter(Context context, List<HashMap<String, String>> contactlist) {
		// TODO 自动生成的构造函数存根
		this.context = context;
		this.contactlist = contactlist;
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return contactlist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return contactlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		View view;
		if (convertView != null) {
			view = convertView;
		} else {
			view = View.inflate(context, R.layout.listview_contacts, null);
		}

		TextView tv_Name = (TextView) view.findViewById(R.id.tv_Name);
		TextView tv_Num = (TextView) view.findViewById(R.id.tv_Oversetup_Num);
		tv_Name.setText(contactlist.get(position).get("phoneName"));
		tv_Num.setText(contactlist.get(position).get("phoneNum"));
		
		
		return view;
	}

}
