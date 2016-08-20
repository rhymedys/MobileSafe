package com.cc.mobilesafe.Service;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.Utils.ConstantValue;
import com.cc.mobilesafe.Utils.LogUtils;
import com.cc.mobilesafe.Utils.SpUtils;
import com.cc.mobilesafe.Utils.ToastUtil;
import com.cc.mobilesafe.activity.QueryAddressActivity;
import com.cc.mobilesafe.engine.AddressDao;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class AddressService extends Service {

	private static final String TAG = "AddressService";
	private PhoneStateListener myPhoneStateListener;
	private TelephonyManager telephonyManager;
	private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
	private View viewToast;
	private String addressResult;
	private WindowManager windowManager;
	private TextView tv_toast;
	private int[] toastBackgrouds;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			//
			if ((!TextUtils.isEmpty(addressResult)) && tv_toast != null) {
				tv_toast.setText(addressResult);
			}

			super.handleMessage(msg);
		}

	};

	@Override
	public void onCreate() {
		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		myPhoneStateListener = new MyPhoneStateListener();
		telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		LogUtils.i(TAG, "已开启");
		super.onCreate();

		toastBackgrouds = new int[] { R.drawable.call_locate_white, R.drawable.call_locate_orange,
				R.drawable.call_locate_blue, R.drawable.call_locate_gray, R.drawable.call_locate_green };
	}

	@Override
	public IBinder onBind(Intent intent) {
		//
		return null;
	}

	@Override
	public void onDestroy() {
		//
		if (myPhoneStateListener != null && telephonyManager != null) {
			telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE);
		}
		LogUtils.i(TAG, "正在关闭");
		super.onDestroy();
	}

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
				if (viewToast != null && windowManager != null) {
					windowManager.removeView(viewToast);
				}
				break;
			// 来电状态
			case TelephonyManager.CALL_STATE_RINGING:
				QueryAddress(incomingNumber);
				showToast(incomingNumber);
				break;

			default:
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}

	}

	/**
	 * 自定义显示Toast
	 */
	public void showToast(String incomingNumber) {
		// 自定义Toast

		final WindowManager.LayoutParams params = mParams;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		params.setTitle("Toast");
		params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		params.gravity = Gravity.LEFT + Gravity.TOP;

		viewToast = View.inflate(this, R.layout.toast_view, null);
		tv_toast = (TextView) viewToast.findViewById(R.id.tv_toast);

		int intStyle = SpUtils.getInt(getApplicationContext(), ConstantValue.TOAST_STYLE, 0);
		tv_toast.setBackgroundResource(toastBackgrouds[intStyle]);
		windowManager.addView(viewToast, params);

	}

	/**
	 * 查询归属地
	 * 
	 * @param incomingNumber
	 */
	public void QueryAddress(final String incomingNumber) {
		//
		new Thread(new Runnable() {

			public void run() {
				addressResult = AddressDao.getAddress(incomingNumber);
				handler.sendEmptyMessage(0);
			}
		}).start();
	}

}
