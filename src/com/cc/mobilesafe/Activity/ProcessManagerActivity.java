package com.cc.mobilesafe.Activity;

import java.util.ArrayList;
import java.util.List;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.Adapter.ProcessInfoAdapter;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.R.menu;
import com.cc.mobilesafe.Bean.ProcessInfoBean;
import com.cc.mobilesafe.Engine.ProcessInfoProvider;
import com.cc.mobilesafe.Utils.ToastUtil;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ProcessManagerActivity extends Activity implements OnClickListener {

	private Context context;
	private TextView tv_memory;
	private TextView tv_process_count;
	private Button btn_clear;
	private ListView lv_process;
	private ProcessInfoAdapter processAdapter;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);

			processAdapter = new ProcessInfoAdapter(context, userProcessInfo, systemProcessInfo);
			lv_process.setAdapter(processAdapter);

			if (tv_title != null && userProcessInfo != null) {
				tv_title.setText("用户进程(" + userProcessInfo.size() + ")");
			}

		}

	};
	private List<ProcessInfoBean> systemProcessInfo;
	private List<ProcessInfoBean> userProcessInfo;
	private TextView tv_title;
	private long restMemory;
	private long totalMemory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_manager);
		this.context = this;

		initUi();
		initTitleInfoData();
		initProcessListData();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (processAdapter != null) {
			processAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 初始化list列表
	 */
	private void initProcessListData() {

		getData();
		lv_process.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// 一定要写这一句 if
				if (userProcessInfo != null && systemProcessInfo != null) {

					if (firstVisibleItem < userProcessInfo.size() + 1) {
						tv_title.setText("用户进程(" + userProcessInfo.size() + ")");
					} else {
						tv_title.setText("当前系统(" + systemProcessInfo.size() + ")");
					}
				}
			}
		});

		lv_process.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				ProcessInfoBean itemAtPosition = (ProcessInfoBean) parent.getItemAtPosition(position);
				if (itemAtPosition != null && !itemAtPosition.packageName.equals(context.getPackageName())) {
					itemAtPosition.isCheck = !itemAtPosition.isCheck;
					CheckBox cb_process = (CheckBox) view.findViewById(R.id.cb_process);
					cb_process.setChecked(itemAtPosition.isCheck);
				}
			}
		});

	}

	private void getData() {

		new Thread(new Runnable() {
			public void run() {
				systemProcessInfo = ProcessInfoProvider.getClassProcessInfo(context, true);
				userProcessInfo = ProcessInfoProvider.getClassProcessInfo(context, false);
				handler.sendEmptyMessage(0);
			}
		}).start();
	}

	/**
	 * 初始化抬头信息
	 */
	private void initTitleInfoData() {

		tv_process_count.setText("当前进程总数" + ProcessInfoProvider.getProcessCout(context));
		totalMemory = ProcessInfoProvider.getTotalMemory(context);
		restMemory = ProcessInfoProvider.getRestMemory(context);

		String strTotalMemory = Formatter.formatFileSize(context, totalMemory);
		String strRestMemory = Formatter.formatFileSize(context, restMemory);

		tv_memory.setText("当前可用内存:" + strRestMemory + "/" + strTotalMemory);

	}

	private void initUi() {
		tv_memory = (TextView) findViewById(R.id.tv_memory);
		tv_process_count = (TextView) findViewById(R.id.tv_process_count);
		tv_title = (TextView) findViewById(R.id.tv_title);

		findViewById(R.id.btn_clear).setOnClickListener(this);
		findViewById(R.id.btn_all).setOnClickListener(this);
		findViewById(R.id.btn_inall).setOnClickListener(this);
		findViewById(R.id.btn_setting).setOnClickListener(this);

		lv_process = (ListView) findViewById(R.id.lv_process);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_all:
			selectAll();
			break;
		case R.id.btn_inall:
			selectInAll();
			break;
		case R.id.btn_clear:
			clearSelect();
			break;
		case R.id.btn_setting:
			setting();
			break;

		default:
			break;
		}
	}

	private void setting() {

		startActivityForResult(new Intent(context, ProcessSettingActivity.class), 0);
	}

	/**
	 * 清理选中内容
	 */
	private void clearSelect() {

		List<ProcessInfoBean> killProcessList = new ArrayList<ProcessInfoBean>();
		for (ProcessInfoBean bean : userProcessInfo) {
			if (bean.packageName.equals(context.getPackageName())) {
				continue;
			} else {
				if (bean.isCheck) {
					killProcessList.add(bean);
				}
			}
		}

		for (ProcessInfoBean bean : systemProcessInfo) {
			if (bean.isCheck) {
				killProcessList.add(bean);
			}
		}

		for (ProcessInfoBean bean : killProcessList) {
			if (bean.isSystemProcess) {
				systemProcessInfo.remove(bean);
			} else {
				userProcessInfo.remove(bean);
			}

			ProcessInfoProvider.killProcess(context, bean);
		}

		if (processAdapter != null) {
			processAdapter.notifyDataSetChanged();
		}

		// 释放多少空间
		Long currentRestMemory = ProcessInfoProvider.getRestMemory(context);
		String releaseMemory = Formatter.formatFileSize(context, currentRestMemory - restMemory);
		ToastUtil.show(context, "杀死了" + killProcessList.size() + "个进程,释放了" + releaseMemory + "空间");

		// 更新状态
		initTitleInfoData();

	}

	/**
	 * 反选
	 */
	private void selectInAll() {

		for (ProcessInfoBean bean : userProcessInfo) {
			if (bean.packageName.equals(context.getPackageName())) {
				continue;
			} else {
				bean.isCheck = !bean.isCheck;
			}
		}
		for (ProcessInfoBean bean : systemProcessInfo) {
			bean.isCheck = !bean.isCheck;
		}

		if (processAdapter != null) {
			processAdapter.notifyDataSetChanged();
		}

		tv_process_count.setText("用户进程(" + userProcessInfo.size() + ")");
	}

	/**
	 * 全选
	 * 
	 */
	private void selectAll() {

		for (ProcessInfoBean bean : userProcessInfo) {
			if (bean.packageName.equals(context.getPackageName())) {
				continue;
			} else {
				bean.isCheck = true;
			}
		}
		for (ProcessInfoBean bean : systemProcessInfo) {
			bean.isCheck = true;
		}

		if (processAdapter != null) {
			processAdapter.notifyDataSetChanged();
		}
	}

}
