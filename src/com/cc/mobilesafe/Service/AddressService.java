package com.cc.mobilesafe.Service;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.Activity.QueryAddressActivity;
import com.cc.mobilesafe.Engine.AddressDao;
import com.cc.mobilesafe.Utils.ConstantValue;
import com.cc.mobilesafe.Utils.LogUtils;
import com.cc.mobilesafe.Utils.SpUtils;
import com.cc.mobilesafe.Utils.ToastUtil;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

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
	private int screenWidth;
	private int screenHeight;
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
		screenHeight = windowManager.getDefaultDisplay().getHeight();
		screenWidth = windowManager.getDefaultDisplay().getWidth();

		toastBackgrouds = new int[] { R.drawable.call_locate_white, R.drawable.call_locate_orange,
				R.drawable.call_locate_blue, R.drawable.call_locate_gray, R.drawable.call_locate_green };

		LogUtils.i(TAG, "已开启");
		super.onCreate();
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
		// 指定默认初始化的位置
		params.gravity = Gravity.LEFT + Gravity.TOP;

		viewToast = View.inflate(this, R.layout.toast_view, null);
		tv_toast = (TextView) viewToast.findViewById(R.id.tv_toast);

		viewToast.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO 自动生成的方法存根
				int startY = 0;
				int startX = 0;
				int moveY = 0;
				int moveX = 0;
				int disX = 0;
				int disY = 0;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 距离原点的距离
					startY = (int) event.getRawY();
					startX = (int) event.getRawX();
					break;
				case MotionEvent.ACTION_MOVE:
					moveX = (int) event.getRawX();
					moveY = (int) event.getRawY();

					// 移动的距离
					disX = moveX - startX;
					disY = moveY - startY;

					// 获取原来距离中心点的距离且更新
					params.x = params.x + disX;
					params.y = params.y + disY;
					windowManager.updateViewLayout(tv_toast, params);

					
					// 容错处理
					// 22为上方通知栏的高度 大概的
					if (params.x < 0 ) {
						params.x=0;
					}
					if ( params.x+tv_toast.getWidth() > screenWidth ) {
						params.x=screenWidth-tv_toast.getWidth();
					}
					if ( params.y < 0 ) {
						params.y=0;
					}
					if ( params.y+tv_toast.getHeight() > screenHeight-22) {
						params.y=screenHeight-tv_toast.getHeight()-22;
					}


					// 重置坐标
					startX = (int) event.getRawX();
					startY = (int) event.getY();

					break;
				case MotionEvent.ACTION_UP:
					SpUtils.putInt(getApplicationContext(), ConstantValue.TOAST_LOCATION_X, params.x);
					SpUtils.putInt(getApplicationContext(), ConstantValue.TOAST_LOCATION_Y, params.y);
					break;

				default:
					break;
				}

				// 既要响应点击事件，又要执行拖拽 需返回 false
				return true;
			}
		});

		// 设置位置 params.x为左上角x的位置
		int location_x = SpUtils.getInt(getApplicationContext(), ConstantValue.TOAST_LOCATION_X, 0);
		int location_y = SpUtils.getInt(getApplicationContext(), ConstantValue.TOAST_LOCATION_Y, 0);
		params.x = location_x;
		params.y = location_y;

		// 设置风格
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
