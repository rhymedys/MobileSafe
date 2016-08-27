package com.cc.mobilesafe.Activity;

import java.util.List;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.Adapter.CommonPhoneAdapter;
import com.cc.mobilesafe.Bean.CommonPhoneChildBean;
import com.cc.mobilesafe.Bean.CommonPhoneGroupCombineChild;
import com.cc.mobilesafe.Engine.CommonPhoneDao;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ListAdapter;

public class CommonPhoneActivity extends Activity {

	private Context context;
	private ExpandableListView elv_commonphone;
	private CommonPhoneDao commonPhoneDao;
	private List<CommonPhoneGroupCombineChild> all;
	private CommonPhoneAdapter commonPhoneAdapter;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			if (all != null && elv_commonphone != null) {
				commonPhoneAdapter = new CommonPhoneAdapter(context, all);
				elv_commonphone.setAdapter(commonPhoneAdapter);
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_phone);
		this.context = this;

		initUI();
		initData();

	}

	/**
	 * 准备数据 并且填充
	 */
	private void initData() {
		commonPhoneDao = new CommonPhoneDao();
		new Thread(new Runnable() {
			public void run() {
				all = commonPhoneDao.getAll();
				handler.sendEmptyMessage(0);
			}
		}).start();

		elv_commonphone.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition,
					long id) {

				if (commonPhoneAdapter != null) {
					CommonPhoneChildBean child = commonPhoneAdapter.getChild(groupPosition, childPosition);
					startCall(child.number);
				}
				return false;
			}

		});

	}

	/**
	 * 打电话
	 */
	private void startCall(String phone) {

		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:" + phone));

		startActivity(intent);

	}

	private void initUI() {
		elv_commonphone = (ExpandableListView) findViewById(R.id.elv_commonphone);

	}

}
