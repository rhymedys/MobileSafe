package com.cc.mobilesafe.Adapter;

import java.util.List;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.Bean.AppInfoBean;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class LockorUnlockListAdapter extends BaseAdapter implements ListAdapter {

	private Context context;
	private List<AppInfoBean> list;
	private boolean isLock;

	static class ViewHolder {
		ImageView iv_icon;
		ImageView iv_lock;
		TextView tv_title;
	}

	/**
	 * 
	 * @param context
	 * @param list
	 *            传入的list
	 * @param isLock
	 *            用于标志该列表 是否为加锁的程序列表 true为加锁 false为未加锁
	 */
	public LockorUnlockListAdapter(Context context, List<AppInfoBean> list, boolean isLock) {
		// TODO 自动生成的构造函数存根
		this.context = context;
		this.list = list;
		this.isLock = isLock;
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return list.size();
	}

	@Override
	public AppInfoBean getItem(int position) {
		// TODO 自动生成的方法存根
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.listview_islock_item, null);
			holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
			holder.iv_lock = (ImageView) convertView.findViewById(R.id.iv_lock);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);

			convertView.setTag(holder);
		}
		AppInfoBean bean = getItem(position);
		holder.iv_icon.setBackground(bean.icon);
		holder.tv_title.setText(bean.name);
		if (isLock) {
			holder.iv_lock.setBackgroundResource(R.drawable.lock);
		} else {
			holder.iv_lock.setBackgroundResource(R.drawable.unlock);
		}
		return convertView;
	}

}
