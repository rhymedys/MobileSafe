package com.cc.mobilesafe.Service;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.Activity.RocketBackgroudActivity;
import com.cc.mobilesafe.Utils.ConstantValue;
import com.cc.mobilesafe.Utils.SpUtils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;

public class RocketService extends Service {

	private int screenHeight;
	private int screenWidth;
	private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
	private WindowManager windowManager;
	private View viewRocket;
	private ImageView iv_rocket;
	private WindowManager.LayoutParams params;
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			int height=(Integer) msg.obj;
			params.y=height;
			windowManager.updateViewLayout(viewRocket, params);
			super.handleMessage(msg);
		}
	
	};


		@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

	@Override
	public void onCreate() {
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		screenWidth = windowManager.getDefaultDisplay().getWidth();
		screenHeight = windowManager.getDefaultDisplay().getHeight();

		showRocket();
		super.onCreate();
	}

	/**
	 * 自定义展示小火箭
	 */
	private void showRocket() {
		params = mParams;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		params.setTitle("Toast");
		params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		// 指定默认初始化的位置
		params.gravity = Gravity.LEFT + Gravity.TOP;

		viewRocket = View.inflate(getApplicationContext(), R.layout.rocket_view, null);
		iv_rocket = (ImageView) viewRocket.findViewById(R.id.iv_rocket);
		AnimationDrawable animationDrawable = (AnimationDrawable) iv_rocket.getBackground();
		animationDrawable.start();

		// 读取初始化默认位置
		params.x = SpUtils.getInt(getApplicationContext(), ConstantValue.ROCKET_LOCATION_X, 0);
		params.y = SpUtils.getInt(getApplicationContext(), ConstantValue.ROCKET_LOCATION_Y, 0);

		windowManager.addView(viewRocket, params);

		viewRocket.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
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
					windowManager.updateViewLayout(viewRocket, params);

					// 容错处理
					// 22为上方通知栏的高度 大概的
					if (params.x < 0) {
						params.x = 0;
					}
					if (params.x + viewRocket.getWidth() > screenWidth) {
						params.x = screenWidth - viewRocket.getWidth();
					}
					if (params.y < 0) {
						params.y = 0;
					}
					if (params.y + viewRocket.getHeight() > screenHeight - 22) {
						params.y = screenHeight - viewRocket.getHeight() - 22;
					}

					// 重置坐标
					startX = (int) event.getRawX();
					startY = (int) event.getY();

					break;
				case MotionEvent.ACTION_UP:
					int temp = screenHeight - iv_rocket.getHeight();
					if (params.y + iv_rocket.getHeight() >= temp) {
						sendRocket(params);
						Intent intent = new Intent(getApplicationContext(), RocketBackgroudActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						
						startActivity(intent);
					}
					SpUtils.putInt(getApplicationContext(), ConstantValue.ROCKET_LOCATION_X, params.x);
					SpUtils.putInt(getApplicationContext(), ConstantValue.ROCKET_LOCATION_Y, params.y);
					break;

				default:
					break;
				}

				return true;
			}
		});

	}

	/**
	 * 发射火箭
	 */
	protected void sendRocket(final WindowManager.LayoutParams params) {
		
		new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < 11; i++) {
					int height = params.x - i * 35;
					try {
						Thread.sleep(50);
						
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					Message msg = Message.obtain();
					msg.obj=height;
					handler.sendMessage(msg);
				}
			}
		}).start();
	}

	@Override
	public void onDestroy() {
		
		if (windowManager != null && viewRocket != null) {
			windowManager.removeView(viewRocket);
		}
		super.onDestroy();
	}

}
