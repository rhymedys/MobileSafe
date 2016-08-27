package com.cc.mobilesafe.Service;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.cc.mobilesafe.Engine.BlackNumberDao;
import com.cc.mobilesafe.Reciver.BlackNumberContentObserver;
import com.cc.mobilesafe.Reciver.BlackNumberReciver;
import com.cc.mobilesafe.Service.AddressService.MyPhoneStateListener;
import com.cc.mobilesafe.Utils.LogUtils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

public class BlackNumberService extends Service {

	private static final String TAG = "BlackNumberService++++++++++++++";

	private BlackNumberReciver blackNumberReciver;
	private TelephonyManager telephonyManager;
	private MyPhoneStateListener myPhoneStateListener;

	private BlackNumberDao mDao;

	private ContentResolver contentResolver;

	private Uri uri;

	private BlackNumberContentObserver blackNumberContentObserver;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {

		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(1000);
		blackNumberReciver = new BlackNumberReciver();
		registerReceiver(blackNumberReciver, filter);

		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		myPhoneStateListener = new MyPhoneStateListener();
		telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

		mDao = BlackNumberDao.getInstance(getApplicationContext());

		super.onCreate();

	}

	/**
	 * @author Rhymedys 继承监听电话状态改变
	 */
	class MyPhoneStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			//
			switch (state) {
			// 摘机状态
			case TelephonyManager.CALL_STATE_OFFHOOK:

				break;
			// 空闲状态
			case TelephonyManager.CALL_STATE_IDLE:

				break;
			// 来电状态
			case TelephonyManager.CALL_STATE_RINGING:
				endCall(incomingNumber);
				break;

			default:
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}

	}

	/**
	 * 挂断电话
	 */
	public void endCall(String phone) {
		if (mDao == null || TextUtils.isEmpty(phone)) {
			return;
		}
		int queryMode = mDao.queryMode(phone);
		if (queryMode == 1 || queryMode == 2) {
			// AIDL注意包名
			// ITelephony.Stub.asInterface(Context.getService(Context.TELEPHONY_SERVICE));
			// import android.os.ServiceManager;
			LogUtils.i(TAG, queryMode + "");
			try {
				Class<?> clazz = Class.forName("android.os.ServiceManager");
				// 方法名，方法中形参的的类型
				Method method = clazz.getMethod("getService", String.class);
				// 反射调用此方法
				IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
				ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
				iTelephony.endCall();
			} catch (Exception e) {
				LogUtils.i(TAG, " endCall try error ");
				e.printStackTrace();
			}
		}

		uri = Uri.parse("content://call_log/calls");
		blackNumberContentObserver = new BlackNumberContentObserver(new Handler(), getApplicationContext(), uri, phone);
		getContentResolver().registerContentObserver(uri, true, blackNumberContentObserver);

	}

	@Override
	public void onDestroy() {

		if (blackNumberReciver != null) {
			unregisterReceiver(blackNumberReciver);
		}
		if (blackNumberContentObserver != null) {
			getContentResolver().unregisterContentObserver(blackNumberContentObserver);
		}
		if (myPhoneStateListener != null) {

			telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
		}

		LogUtils.i(TAG, "onDestroy+++++++++++");
		super.onDestroy();
	}

}
