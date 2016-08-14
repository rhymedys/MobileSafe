package com.cc.mobilesafe.activity;

import com.cc.mobilesafe.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class BaseSetupActivity extends Activity {
	protected abstract void showPrePage();

	protected abstract void showNextPage();

	private GestureDetector gestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				// <0上一页

				if (e1.getX() - e2.getX() < 0) {
					showPrePage();
				}
				// 下一页
				if (e1.getX() - e2.getX() > 0) {
					showNextPage();
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});

		try {
			findViewById(R.id.btn_SetupNext).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					showNextPage();
				}
			});

			findViewById(R.id.btn_SetupBack).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					showPrePage();
				}
			});
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO 自动生成的方法存根
		gestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

}
