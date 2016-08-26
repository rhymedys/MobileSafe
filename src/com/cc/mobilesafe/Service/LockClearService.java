package com.cc.mobilesafe.Service;

import com.cc.mobilesafe.Reciver.UnLockScreenReciver;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class LockClearService extends Service {

	private UnLockScreenReciver unLockScreenReciver;

	@Override
	public void onCreate() {
		// TODO 自动生成的方法存根
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		unLockScreenReciver = new UnLockScreenReciver();
		registerReceiver(unLockScreenReciver, intentFilter);
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO 自动生成的方法存根
		if (unLockScreenReciver != null) {
			unregisterReceiver(unLockScreenReciver);
		}
		super.onDestroy();
	}

}
