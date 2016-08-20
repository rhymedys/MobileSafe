package com.cc.mobilesafe.activity;

import org.apache.http.conn.BasicEofSensorWatcher;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.R.menu;
import com.cc.mobilesafe.Utils.ConstantValue;
import com.cc.mobilesafe.Utils.LogUtils;
import com.cc.mobilesafe.Utils.SpUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class ToastLocationActivity extends Activity {

	protected static final String TAG = "ToastLocationActivity";
	private Context context;
	private ImageView iv_drag;
	private Button btn_top;
	private Button btn_buttom;
	private int screenWidth;
	private int screenHeight;
	private long[] mHits = new long[2];// 2为双击次数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_toast_location);
		this.context = this;

		intiUI();

	}

	private void intiUI() {
		iv_drag = (ImageView) findViewById(R.id.iv_drag);
		btn_top = (Button) findViewById(R.id.btn_top);
		btn_buttom = (Button) findViewById(R.id.btn_buttom);

		WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		screenWidth = windowManager.getDefaultDisplay().getWidth();
		screenHeight = windowManager.getDefaultDisplay().getHeight();

		// 回显
		int locationX = SpUtils.getInt(context, ConstantValue.LOCATION_X, 0);
		int locationY = SpUtils.getInt(context, ConstantValue.LOCATION_Y, 0);

		LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.leftMargin = locationX;
		layoutParams.topMargin = locationY;

		iv_drag.setLayoutParams(layoutParams);

		btnVisible(locationY);
		
		iv_drag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// 双击事件主要代码
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();
				// 双击事件的时间间隔500ms
				if (mHits[mHits.length - 1] - mHits[0] < 500) {
					LogUtils.i(TAG, "双击居中");
					int left =screenWidth/2-iv_drag.getWidth()/2;
					int right =screenWidth/2+iv_drag.getWidth()/2;
					int top=screenHeight/2-iv_drag.getHeight()/2;
					int bottom=screenHeight/2+iv_drag.getHeight()/2;
					
					iv_drag.layout(left, top, right, bottom);
					
					btnVisible(iv_drag.getHeight());
					
				}

			}

		});
		

		iv_drag.setOnTouchListener(new OnTouchListener() {

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

					// 获取原来距离中心点的距离
					int left = iv_drag.getLeft() + disX;
					int top = iv_drag.getTop() + disY;
					int right = iv_drag.getRight() + disX;
					int bottom = iv_drag.getBottom() + disY;

					// 容错处理
					// 22为上方通知栏的高度 大概的
					if (left < 0 || right > screenWidth || top < 0 || bottom > screenHeight - 22) {
						return true;
					}

					// 设置移动后离中心点的距离
					iv_drag.layout(left, top, right, bottom);

					btnVisible(top);
					// 重置坐标
					startX = (int) event.getRawX();
					startY = (int) event.getY();
					
					break;
				case MotionEvent.ACTION_UP:
					SpUtils.putInt(context, ConstantValue.LOCATION_X, iv_drag.getLeft());
					SpUtils.putInt(context, ConstantValue.LOCATION_Y, iv_drag.getTop());

					break;

				default:
					break;
				}
				
				
				//既要响应点击事件，又要执行拖拽 需返回 false
				return false;
			}
		});


	
	}
	
	
	public void btnVisible(int top) {
		if (top > screenHeight / 2) {
			btn_buttom.setVisibility(View.INVISIBLE);
			btn_top.setVisibility(View.VISIBLE);
		} else {
			btn_top.setVisibility(View.INVISIBLE);
			btn_buttom.setVisibility(View.VISIBLE);

		}
	}

}
