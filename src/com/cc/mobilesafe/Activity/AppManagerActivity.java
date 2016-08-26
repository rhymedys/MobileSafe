package com.cc.mobilesafe.Activity;

import java.util.ArrayList;
import java.util.List;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.Adapter.appInfoListAdapter;
import com.cc.mobilesafe.Bean.AppInfoBean;
import com.cc.mobilesafe.Engine.AppInfoProcider;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

public class AppManagerActivity extends Activity {

	private Context context;
	private TextView tv_memory;
	private TextView tv_sdmemory;
	private ListView lv_applist;
	private List<List<AppInfoBean>> allAppInfoList;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			lv_applist.setAdapter(new appInfoListAdapter(context, allAppInfoList));
			if ((tv_des != null) && userAppInfoList != null) {
				tv_des.setText("用户应用(" + userAppInfoList.size() + ")");
			}
		}

	};
	private List<AppInfoBean> userAppInfoList;
	private List<AppInfoBean> systemAppInfoList;
	private TextView tv_des;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);
		this.context = this;

		initAllMemory();
		initAppList();
	}

	/**
	 * 初始化应用列表
	 */
	private void initAppList() {
		lv_applist = (ListView) findViewById(R.id.lv_applist);
		tv_des = (TextView) findViewById(R.id.tv_des);

		new Thread(new Runnable() {

			public void run() {

				systemAppInfoList = AppInfoProcider.getClassApp(context, true);
				userAppInfoList = AppInfoProcider.getClassApp(context, false);

				allAppInfoList = new ArrayList<List<AppInfoBean>>();
				allAppInfoList.add(userAppInfoList);
				allAppInfoList.add(systemAppInfoList);

				handler.sendEmptyMessage(0);

			}
		}).start();

		lv_applist.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				if (userAppInfoList != null && systemAppInfoList != null) {
					if (firstVisibleItem >= userAppInfoList.size() + 1) {
						tv_des.setText("系统应用(" + systemAppInfoList.size() + ")");
					} else {
						tv_des.setText("用户应用(" + userAppInfoList.size() + ")");
					}
				}
			}
		});
	}

	/**
	 * 初始化应用列表 存储信息
	 */
	private void initAllMemory() {
		tv_memory = (TextView) findViewById(R.id.tv_memory);
		tv_sdmemory = (TextView) findViewById(R.id.tv_sdmemory);

		String dataDirectoryPath = Environment.getDataDirectory().getAbsolutePath();
		String externalStorageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();

		long dataDirectoryAvaliable = getAvaliable(dataDirectoryPath);
		long externalStorageAvaliable = getAvaliable(externalStorageDirectoryPath);

		tv_memory.setText("内置存储：" + Formatter.formatFileSize(context, dataDirectoryAvaliable));
		tv_sdmemory.setText("SD存储：" + Formatter.formatFileSize(context, externalStorageAvaliable));

	}

	/**
	 * 获取可用容量大小
	 * 
	 * @param dataDirectoryPath
	 *            返回值为byte=8bit int 最多 2G
	 */

	private long getAvaliable(String dataDirectoryPath) {
		// 可用大小类
		StatFs statFs = new StatFs(dataDirectoryPath);
		long totalBlockSize = statFs.getAvailableBlocks() * statFs.getBlockSize();

		return totalBlockSize;
	}

}
