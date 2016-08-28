package com.cc.mobilesafe.Service;

import java.util.List;

import com.cc.mobilesafe.Activity.EnterPsdActivity;
import com.cc.mobilesafe.Engine.AppLockDao;
import com.cc.mobilesafe.Utils.LogUtils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.AppTask;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.MediaBrowserCompat.MediaItem.Flags;

public class AppLockService extends Service {

	private static final String TAG = "AppLockService";
	private static final String DB_CHANGE = "content://applock/change";
	private boolean isWatch;
	private ActivityManager activityManager;
	private AppLockDao db;
	private String skipPackageName;
	private AppLockReciver appLockReciver;
	private List<String> queryAll;
	private AppLockDatabaseObserver appLockDatabaseObserver;

	@Override
	public void onCreate() {
		LogUtils.i(TAG, "AppLockService success");

		isWatch = true;
		watch();

		IntentFilter filter = new IntentFilter("android.appwidget.action.SKIP");
		appLockReciver = new AppLockReciver();
		registerReceiver(appLockReciver, filter);

		// 注册一个内容观察者
		appLockDatabaseObserver = new AppLockDatabaseObserver(new Handler());
		getContentResolver().registerContentObserver(Uri.parse(DB_CHANGE), true, appLockDatabaseObserver);
		super.onCreate();
	}

	/**
	 * 监察
	 */
	private void watch() {

		db = AppLockDao.getInstance(getApplicationContext());
		new Thread(new Runnable() {

			@SuppressLint("NewApi")
			public void run() {
				queryAll = db.queryAll();
				LogUtils.i(TAG, queryAll.size() + "");
				while (isWatch) {
					activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
					List<RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
					String packageName = runningTasks.get(0).topActivity.getPackageName();
					if (queryAll.contains(packageName)) {
						if (!packageName.equals(skipPackageName)) {
							Intent intent = new Intent(getApplicationContext(), EnterPsdActivity.class);

							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

							intent.putExtra("packageName", packageName);
							startActivity(intent);
						}
					}

					// LogUtils.i(TAG, "enter watch");
				}

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onDestroy() {
		isWatch = false;
		if (appLockReciver != null) {
			unregisterReceiver(appLockReciver);
		}
		if (appLockDatabaseObserver != null) {
			getContentResolver().unregisterContentObserver(appLockDatabaseObserver);
		}

		super.onDestroy();
	}

	class AppLockReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			skipPackageName = intent.getExtras().getString("packageName");
		}

	}

	class AppLockDatabaseObserver extends ContentObserver {

		public AppLockDatabaseObserver(Handler handler) {
			super(handler);
			// TODO 自动生成的构造函数存根
		}

		@Override
		public void onChange(boolean selfChange) {
			// TODO 自动生成的方法存根
			new Thread(new Runnable() {
				public void run() {
					queryAll = db.queryAll();
				}
			}).start();
			super.onChange(selfChange);
		}

	}

}
