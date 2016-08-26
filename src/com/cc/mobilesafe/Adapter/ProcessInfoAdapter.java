package com.cc.mobilesafe.Adapter;

import java.util.List;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.Bean.ProcessInfoBean;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ProcessInfoAdapter extends BaseAdapter implements ListAdapter {

	private Context context;
	private List<ProcessInfoBean> userProcessInfo;
	private List<ProcessInfoBean> systemProcessInfo;

	static class ProcessViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_memoryinfo;
		CheckBox cb_process;

	}

	static class TitleViewHolder {
		TextView list_process_item_title;
	}

	public ProcessInfoAdapter(Context context, List<ProcessInfoBean> userProcessInfo,
			List<ProcessInfoBean> systemProcessInfo) {
		// TODO 自动生成的构造函数存根
		this.context = context;
		this.userProcessInfo = userProcessInfo;
		this.systemProcessInfo = systemProcessInfo;
	}

	@Override
	public int getViewTypeCount() {
		// TODO 自动生成的方法存根
		return super.getViewTypeCount() + 1;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO 自动生成的方法存根
		if (position == 0 || position == userProcessInfo.size() + 1) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return userProcessInfo.size() + systemProcessInfo.size() + 2;
	}

	/*
	 * 如果 position == 0 || position == userProcessInfo.size() + 1 返回null 否则返回一个
	 * bean 实体
	 */
	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		if (position == 0 || position == userProcessInfo.size() + 1) {
			return null;
		} else {
			if (position <= userProcessInfo.size() + 1) {
				return userProcessInfo.get(position - 1);
			} else {
				return systemProcessInfo.get(position - userProcessInfo.size() - 2);
			}
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		int itemViewType = getItemViewType(position);
		ProcessViewHolder processViewHolder = new ProcessViewHolder();
		TitleViewHolder titleViewHolder = new TitleViewHolder();
		if (itemViewType == 0) {
			if (convertView != null) {
				titleViewHolder = (TitleViewHolder) convertView.getTag();
			} else {
				convertView = View.inflate(context, R.layout.listview_app_item_title, null);
				titleViewHolder.list_process_item_title = (TextView) convertView.findViewById(R.id.list_app_item_title);

				convertView.setTag(titleViewHolder);
			}
			if (position == 0) {
				titleViewHolder.list_process_item_title.setText("用户进程：" + userProcessInfo.size());

			} else {
				titleViewHolder.list_process_item_title.setText("系统进程：" + systemProcessInfo.size());
			}
		} else {
			if (convertView != null) {
				processViewHolder = (ProcessViewHolder) convertView.getTag();
			} else {
				convertView = View.inflate(context, R.layout.listview_process_item, null);
				processViewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				processViewHolder.tv_memoryinfo = (TextView) convertView.findViewById(R.id.tv_memoryinfo);
				processViewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
				processViewHolder.cb_process = (CheckBox) convertView.findViewById(R.id.cb_process);

				convertView.setTag(processViewHolder);
			}

			ProcessInfoBean item = (ProcessInfoBean) getItem(position);
			processViewHolder.tv_name.setText(item.name);
			String formatFileSize = Formatter.formatFileSize(context, item.useMemorySize);
			processViewHolder.tv_memoryinfo.setText("内存占用:" + formatFileSize);
			processViewHolder.iv_icon.setBackgroundDrawable(item.icon);
			if (item.packageName.equals(context.getPackageName())) {
				processViewHolder.cb_process.setVisibility(View.GONE);
			} else {
				processViewHolder.cb_process.setVisibility(View.VISIBLE);
			}
			processViewHolder.cb_process.setChecked(item.isCheck);
		}

		return convertView;
	}

}
