package com.cc.mobilesafe.Adapter;

import java.util.ArrayList;

import com.cc.mobilesafe.Bean.BlackNumberInfoBean;
import com.cc.mobilesafe.db.Dao.BlackNumberDao;
import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.id;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class BlackNumAdapter extends BaseAdapter implements ListAdapter {

	protected static final int REFRESH_ADAPTER = 1;
	private Context context;
	private ArrayList<BlackNumberInfoBean> list;
	private String[] type = new String[] { "只拦截短信", "只拦截电话", "拦截所有" };
	private Handler handler;

	static class BlackNumberListViewHolder {

		TextView tv_phone;
		TextView tv_mode;
		ImageView iv_delete;
	}

	public BlackNumAdapter(Context context, ArrayList<BlackNumberInfoBean> list, Handler handler) {
		this.context = context;
		this.list = list;
		this.handler = handler;
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		BlackNumberListViewHolder holder = null;
		if (convertView != null) {
			holder = (BlackNumberListViewHolder) convertView.getTag();
		} else {
			convertView = View.inflate(context, R.layout.listview_blacknumber_item, null);
			holder = new BlackNumberListViewHolder();
			holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
			holder.tv_mode = (TextView) convertView.findViewById(R.id.tv_mode);
			holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
			convertView.setTag(holder);
		}
		// TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
		// TextView tv_mode = (TextView) view.findViewById(R.id.tv_mode);
		holder.iv_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根

				BlackNumberDao blackNumberDao = BlackNumberDao.getInstance(context);
				blackNumberDao.delete(list.get(position).phone);

				list.remove(position);

				handler.sendEmptyMessage(REFRESH_ADAPTER);

			}
		});

		String mode = type[list.get(position).mode];
		holder.tv_mode.setText(mode);

		holder.tv_phone.setText(list.get(position).phone);

		return convertView;
	}
	
	

}
