package com.cc.mobilesafe.Adapter;

import java.util.ArrayList;
import java.util.List;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.Bean.AppInfoBean;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class appInfoListAdapter extends BaseAdapter implements ListAdapter {

	private Context context;
	private ViewHolderListItem viewHolderListItem = null;
	private List<AppInfoBean> userAppInfoList;
	private List<AppInfoBean> systemAppInfoList;
	private ViewHolderListTitle viewHolderListTitle;

	static class ViewHolderListItem {

		ImageView iv_icon;
		TextView tv_name;
		TextView tv_path;

	}

	static class ViewHolderListTitle {

		TextView list_app_item_title;

	}

	public appInfoListAdapter(Context context, List<List<AppInfoBean>> allAppInfoList) {
		this.context = context;
		userAppInfoList = allAppInfoList.get(0);
		systemAppInfoList = allAppInfoList.get(1);

	}

	@Override
	public int getViewTypeCount() {
		// 获取数据适配器中条目类型的总数，修改成两种（纯文本，图片+文字）
		return super.getViewTypeCount() + 1;

	}

	/*
	 * 指定索引指向条目类型。条目类型状态 返回 0表示 该条目类型为纯文本 返回1表示该类型为 图片+文字
	 */
	@Override
	public int getItemViewType(int position) {

		if (position == 0 || position == userAppInfoList.size() + 1) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int getCount() {

		return userAppInfoList.size() + systemAppInfoList.size() + 2;
	}

	@Override
	public Object getItem(int position) {

		if (position == 0 || position == userAppInfoList.size() + 1) {
			return null;
		} else {
			if (position < userAppInfoList.size() + 1) {
				return userAppInfoList.get(position - 1);
			} else {
				return systemAppInfoList.get(position - userAppInfoList.size() - 2);
			}
		}
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int itemViewType = getItemViewType(position);
		if (itemViewType == 0) {
			viewHolderListTitle = new ViewHolderListTitle();
			if (convertView != null) {
				viewHolderListTitle = (ViewHolderListTitle) convertView.getTag();
			} else {
				convertView = View.inflate(context, R.layout.listview_app_item_title, null);
				viewHolderListTitle.list_app_item_title = (TextView) convertView.findViewById(R.id.list_app_item_title);
				convertView.setTag(viewHolderListTitle);
			}
			if (position == 0) {
				viewHolderListTitle.list_app_item_title.setText("用户应用(" + userAppInfoList.size() + ")");

			} else {
				viewHolderListTitle.list_app_item_title.setText("系统应用(" + systemAppInfoList.size() + ")");
			}
		} else {
			viewHolderListItem = new ViewHolderListItem();
			if (convertView != null) {
				viewHolderListItem = (ViewHolderListItem) convertView.getTag();
			} else {
				convertView = View.inflate(context, R.layout.listview_app_item, null);
				viewHolderListItem.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
				viewHolderListItem.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				viewHolderListItem.tv_path = (TextView) convertView.findViewById(R.id.tv_path);

				convertView.setTag(viewHolderListItem);
			}
			AppInfoBean appInfoBean = (AppInfoBean) getItem(position);
			viewHolderListItem.iv_icon.setBackground(appInfoBean.icon);
			viewHolderListItem.tv_name.setText(appInfoBean.name);
			if (appInfoBean.isSystemApp) {
				viewHolderListItem.tv_path.setText("系统应用");

			} else {
				viewHolderListItem.tv_path.setText("用户应用");
			}

		}

		return convertView;

	}

}
