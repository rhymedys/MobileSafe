package com.cc.mobilesafe.Activity;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Text;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.Adapter.appInfoListAdapter;
import com.cc.mobilesafe.Bean.AppInfoBean;
import com.cc.mobilesafe.Engine.AppInfoProcider;
import com.cc.mobilesafe.Utils.LogUtils;
import com.cc.mobilesafe.Utils.ToastUtil;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class AppManagerActivity extends Activity implements OnClickListener {

	private static final String TAG = "AppManagerActivity";
	private Context context;
	private TextView tv_memory;
	private TextView tv_sdmemory;
	private ListView lv_applist;
	private List<List<AppInfoBean>> allAppInfoList;
	private appInfoListAdapter appInfoListAdapter;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			appInfoListAdapter = new appInfoListAdapter(context, allAppInfoList);
			lv_applist.setAdapter(appInfoListAdapter);
			if ((tv_des != null) && userAppInfoList != null) {
				tv_des.setText("用户应用(" + userAppInfoList.size() + ")");
			}
		}

	};
	private List<AppInfoBean> userAppInfoList;
	private List<AppInfoBean> systemAppInfoList;
	private TextView tv_des;
	private AppInfoBean appInfoBean;
	private TextView tv_uninstall;
	private TextView tv_open;
	private TextView tv_share;
	private PopupWindow popupWindow;
	private boolean booleanIntentUninstall = false;
	private int currentPosition = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);
		this.context = this;

		initAllMemory();
		initAppList();
	}

	@Override
	protected void onResume() {
		if (booleanIntentUninstall) {
			userAppInfoList.remove(currentPosition);
			appInfoListAdapter.notifyDataSetChanged();
			booleanIntentUninstall = !booleanIntentUninstall;
		}
		super.onResume();

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

		lv_applist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if (position == 0 || position == userAppInfoList.size() + 1) {
					return;
				} else {

					if (position < userAppInfoList.size() + 1) {
						appInfoBean = userAppInfoList.get(position - 1);
						currentPosition = position - 1;
					} else {
						appInfoBean = systemAppInfoList.get(position - userAppInfoList.size() - 2);
					}
					showPopwindow(view);

				}

			}

		});
	}

	/**
	 * 显示点击窗口
	 */
	protected void showPopwindow(View view) {

		View contentView = View.inflate(context, R.layout.popwindow_applist, null);
		tv_uninstall = (TextView) contentView.findViewById(R.id.tv_uninstall);
		tv_open = (TextView) contentView.findViewById(R.id.tv_open);
		tv_share = (TextView) contentView.findViewById(R.id.tv_share);

		tv_uninstall.setOnClickListener(this);
		tv_open.setOnClickListener(this);
		tv_share.setOnClickListener(this);

		AnimationSet animationSet = new AnimationSet(true);
		Animation alphaAnimation = AnimationUtils.loadAnimation(context, R.anim.applist_popwindow_alpha_animation);
		Animation scaleAnimation = AnimationUtils.loadAnimation(context, R.anim.applist_popwindow_scale_animation);
		animationSet.addAnimation(alphaAnimation);
		animationSet.addAnimation(scaleAnimation);
		contentView.startAnimation(animationSet);

		popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, true);
		popupWindow.setBackgroundDrawable(new ColorDrawable());
		popupWindow.showAsDropDown(view, 100, -view.getHeight());

		popupWindow.setOutsideTouchable(true);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_uninstall:
			if (appInfoBean.isSystemApp) {
				ToastUtil.show(context, "此应用为系统应用，不能卸载");
				return;
			} else {
				try {
					Intent intentUninstall = new Intent();
					intentUninstall.addCategory("android.intent.category.DEFAULT");
					intentUninstall.setAction("android.intent.action.DELETE");
					intentUninstall.setData(Uri.parse("package:" + appInfoBean.packageName));
					booleanIntentUninstall = true;
					startActivity(intentUninstall);
				} catch (Exception e) {
					LogUtils.i(TAG, "tv_uninstall fail!!!!!!!!!!!!!!!!!!!!!!!!!");
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			popupWindow.dismiss();
			break;
		case R.id.tv_open:
			PackageManager packageManager = getPackageManager();
			try {
				Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage(appInfoBean.packageName);
				if (launchIntentForPackage != null) {
					startActivity(launchIntentForPackage);
				} else {
					ToastUtil.show(context, "该应用不能被开启");
				}
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				LogUtils.i(TAG, "tv_open fail!!!!!!!!!!!!!");
				e.printStackTrace();
			}
			popupWindow.dismiss();
			break;
		case R.id.tv_share:
			try {
				Intent intentSms = new Intent(Intent.ACTION_SEND);
				intentSms.putExtra(Intent.EXTRA_TEXT, "new app to you" + appInfoBean.name);
				intentSms.setType("text/plain");
				startActivity(intentSms);
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				LogUtils.i(TAG, "tv_share fail!!!!!!!!!!");
				e.printStackTrace();
			}
			popupWindow.dismiss();
			break;

		default:
			break;
		}
	}

}
