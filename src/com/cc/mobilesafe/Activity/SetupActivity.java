package com.cc.mobilesafe.Activity;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.R.menu;

import android.app.Activity;
import android.app.usage.UsageEvents.Event;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus.Listener;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;

public class SetupActivity extends BaseSetupActivity {

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup);
		this.context = this;

		initUI();

	}

	/**
	 * 初始化ui布局
	 */
	private void initUI() {
		// TODO 自动生成的方法存根
			findViewById(R.id.btn_SetupNext).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 自动生成的方法存根
					showNextPage();
				}
			});
	}
	
	@Override
	protected void showPrePage() {
		// TODO 自动生成的方法存根
		
	}
	
	@Override
	protected void showNextPage() {
		// TODO 自动生成的方法存根
		startActivity(new Intent(context, Setup2Activity.class));
		overridePendingTransition(R.anim.next_in_anim, R.anim.next_in_out_anim);
		finish();
	}



}
