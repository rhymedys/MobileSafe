package com.cc.mobilesafe.activity;

import org.apache.http.conn.BasicEofSensorWatcher;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.R.menu;
import com.cc.mobilesafe.Utils.ConstantValue;
import com.cc.mobilesafe.Utils.SpUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class ToastLocationActivity extends Activity {

	private Context context;
	private ImageView iv_drag;
	private Button btn_top;
	private Button btn_buttom;
	private int screenWidth;
	private int screenHeight;

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
		
		WindowManager windowManager=(WindowManager) getSystemService(WINDOW_SERVICE);
		 screenWidth = windowManager.getDefaultDisplay().getWidth();
		screenHeight = windowManager.getDefaultDisplay().getHeight();
		
	
		//回显
		int locationX = SpUtils.getInt(context, ConstantValue.LOCATION_X, 0);
		int locationY = SpUtils.getInt(context, ConstantValue.LOCATION_Y, 0);
		
		LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);;
		layoutParams.leftMargin=locationX;
		layoutParams.topMargin=locationY;
		
		iv_drag.setLayoutParams(layoutParams);
		
		
		
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
					
					//容错处理
					//22为上方通知栏的高度  大概的
					if (iv_drag.getLeft()<0||iv_drag.getRight()>screenWidth||iv_drag.getTop()<0||iv_drag.getBottom()>screenHeight-22) {
						break;
					}

					// 设置移动后离中心点的距离
					iv_drag.layout(left, top, right, bottom);

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
				return true;
			}
		});
	}

}
