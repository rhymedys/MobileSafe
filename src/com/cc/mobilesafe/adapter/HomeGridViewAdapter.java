package com.cc.mobilesafe.adapter;

import com.cc.mobilesafe.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeGridViewAdapter extends BaseAdapter {

	private Context context;
	private String[] titleStr;
	private int[] picInt;
	private ImageView iv_icon;
	private TextView tv_title;

	public HomeGridViewAdapter(Context context,String []titleStr ,int[] picInt){
		this.context=context;
		this.titleStr=titleStr;
		this.picInt=picInt;
	}
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return titleStr.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return titleStr[position];
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
		if (convertView!=null) {
			view=convertView;
		}else{
			view=View.inflate(context, R.layout.gridview_item, null);
		}
		
		
		iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		
		tv_title.setText(titleStr[position]);
		iv_icon.setBackgroundResource(picInt[position]);
		
		return view;
	}

}
