package com.cc.mobilesafe.Activity;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.R.menu;
import com.cc.mobilesafe.Bean.CacheInfoBean;
import com.cc.mobilesafe.Utils.LogUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.IPackageStatsObserver.Stub;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v4.widget.TextViewCompat;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ClearCacheActivity extends Activity {

	protected static final String TAG = "ClearCacheActivity";
	protected static final int UPDATE_CACHE = 101;
	protected static final int CHECK_PROGRESSING = 102;
	protected static final int CHECK_FINISH = 103;
	protected static final int CLEAR_ALL_FINISH = 104;
	protected static final int CLEAR_ITEM_CACHE_FINISH = 105;
	private Context context;
	private Button btn_clear;
	private LinearLayout ll_list;
	private TextView tv_state;
	private ProgressBar pb_progress;
	private PackageManager packageManager;
	private List<PackageInfo> installedPackages;
	private String packageName;
	private Stub mStatsObserver;
	private ImageView iv_icon;
	protected TextView tv_name;
	protected TextView tv_memoryinfo;
	protected ImageView iv_delete;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case UPDATE_CACHE:
				final CacheInfoBean packageInfoBean = (CacheInfoBean) msg.obj;

				View listItem = View.inflate(context, R.layout.linearlayout_cache_item, null);
				iv_icon = (ImageView) listItem.findViewById(R.id.iv_icon);
				tv_name = (TextView) listItem.findViewById(R.id.tv_name);
				tv_memoryinfo = (TextView) listItem.findViewById(R.id.tv_memoryinfo);
				iv_delete = (ImageView) listItem.findViewById(R.id.iv_delete);

				iv_icon.setBackgroundDrawable(packageInfoBean.icon);
				tv_name.setText(packageInfoBean.name);
				String formatFileSize = Formatter.formatFileSize(context, packageInfoBean.cacheSize);
				tv_memoryinfo.setText("缓存大小：" + formatFileSize);

				iv_delete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						clearItemCache(packageInfoBean.packageName);
					}

				});

				ll_list.addView(listItem);

				break;
			case CHECK_PROGRESSING:
				String appName = (String) msg.obj;
				tv_state.setText("正在扫描：" + appName);
				break;
			case CHECK_FINISH:
				tv_state.setText("扫描完成");
				btn_clear.setClickable(true);
				break;
			case CLEAR_ALL_FINISH:
				ll_list.removeAllViews();
				tv_state.setText("清理完成");
				break;
			case CLEAR_ITEM_CACHE_FINISH:

				break;

			default:
				break;
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clear_cache);
		this.context = this;

		initUI();
		initData();

	}

	private void initData() {
		packageManager = getPackageManager();
		new Thread(new Runnable() {

			public void run() {
				int index = 0;
				btn_clear.setClickable(false);
				installedPackages = packageManager.getInstalledPackages(0);
				pb_progress.setMax(installedPackages.size());
				for (PackageInfo temp : installedPackages) {

					packageName = temp.packageName;
					getPackageCache(packageName);

					index++;
					try {
						Thread.sleep(new Random().nextInt(100));
					} catch (InterruptedException e) {

						e.printStackTrace();
					}
					pb_progress.setProgress(index);

					Message msg = Message.obtain();
					msg.what = CHECK_PROGRESSING;
					msg.obj = temp.applicationInfo.loadLabel(packageManager).toString();
					handler.sendMessage(msg);
				}

				handler.sendEmptyMessage(CHECK_FINISH);
			}
		}).start();

	}

	protected void getPackageCache(final String packageName) {

		mStatsObserver = new IPackageStatsObserver.Stub() {

			@Override
			public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {

				long cacheSize = pStats.cacheSize;
				if (cacheSize > 0) {
					try {
						CacheInfoBean bean = new CacheInfoBean();
						bean.packageName = pStats.packageName;
						bean.cacheSize = cacheSize;
						bean.name = packageManager.getApplicationInfo(pStats.packageName, 0).loadLabel(packageManager)
								.toString();
						bean.icon = packageManager.getApplicationInfo(pStats.packageName, 0).loadIcon(packageManager);

						Message msg = Message.obtain();
						msg.what = UPDATE_CACHE;
						msg.obj = bean;
						handler.sendMessage(msg);
					} catch (NameNotFoundException e) {

						LogUtils.i(TAG, "load name or icon fail!!");
						e.printStackTrace();
					}

				}
			}
		};

		// 反射
		//
		// mPm.getPackageSizeInfo("com.android.browser", mStatsObserver);

		try {
			Class<?> clazz = Class.forName("android.content.pm.PackageManager");

			Method method = clazz.getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);

			method.invoke(packageManager, packageName, mStatsObserver);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	private void initUI() {
		btn_clear = (Button) findViewById(R.id.btn_clear);
		ll_list = (LinearLayout) findViewById(R.id.ll_list);
		tv_state = (TextView) findViewById(R.id.tv_state);
		pb_progress = (ProgressBar) findViewById(R.id.pb_progress);

		btn_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clearAll();

			}
		});

	}

	protected void clearAll() {
		try {
			Class<?> clazz = Class.forName("android.content.pm.PackageManager");

			Method method = clazz.getMethod("freeStorageAndNotify", long.class, IPackageDataObserver.class);

			method.invoke(packageManager, Long.MAX_VALUE, new IPackageDataObserver.Stub() {

				@Override
				public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
					// TODO 自动生成的方法存根
					Message msg = Message.obtain();
					msg.what = CLEAR_ALL_FINISH;
					handler.sendMessage(msg);
				}
			});

		} catch (Exception e) {

			LogUtils.i(TAG, "clearAll fail!!");
			e.printStackTrace();
		}

	}

	private void clearItemCache(String packageName) {
		// TODO 自动生成的方法存根
		// try {
		// Class<?> clazz = Class.forName("android.content.pm.PackageManager");
		//
		// Method method = clazz.getMethod("deleteApplicationCacheFiles",
		// String.class, IPackageDataObserver.class);
		//
		// method.invoke(packageManager, packageName, new
		// IPackageDataObserver.Stub() {
		//
		// @Override
		// public void onRemoveCompleted(String packageName, boolean succeeded)
		// throws RemoteException {
		// // TODO 自动生成的方法存根
		// handler.sendEmptyMessage(CLEAR_ITEM_CACHE_FINISH);
		// }
		// });
		//
		// } catch (Exception e) {
		//
		// LogUtils.i(TAG, "clearItemCache fail!!");
		// e.printStackTrace();
		// }
		Intent intent = new Intent();
		intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
		intent.setData(Uri.parse("package:" + packageName));
		startActivity(intent);

	}
}
