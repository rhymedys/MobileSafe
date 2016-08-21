package com.cc.mobilesafe.Service;

import com.cc.mobilesafe.Reciver.BlackNumberReciver;
import com.cc.mobilesafe.Utils.LogUtils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class BlackNumberService extends Service {

	private static final String TAG = "BlackNumberService";
	private BlackNumberReciver blackNumberReciver;



	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void onCreate() {
		
		IntentFilter filter=new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(1000);
		blackNumberReciver = new BlackNumberReciver();
		registerReceiver(blackNumberReciver, filter);
		super.onCreate();
	
	}
	
	

	@Override
	public void onDestroy() {
		// TODO 自动生成的方法存根
		if (blackNumberReciver!=null) {
			unregisterReceiver(blackNumberReciver);
		}
		LogUtils.i(TAG, "onDestroy+++++++++++");
		super.onDestroy();
	}

}
