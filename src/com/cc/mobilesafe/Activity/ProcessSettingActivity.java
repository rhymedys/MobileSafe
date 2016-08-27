package com.cc.mobilesafe.Activity;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.R.menu;
import com.cc.mobilesafe.Service.LockClearService;
import com.cc.mobilesafe.Utils.ConstantValue;
import com.cc.mobilesafe.Utils.ServiceUtils;
import com.cc.mobilesafe.Utils.SpUtils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ProcessSettingActivity extends Activity {

	private static final String LOCK_CLEARSERVICE = "com.cc.mobilesafe.Service.LockClearService";
	private Context context;
	private CheckBox cb_IsDisplay;
	private CheckBox cb_lockclear;
	private Intent service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_setting);
		this.context = this;

		initDisplaySystemProcess();
		initLockClear();
	}

	/**
	 * 锁屏清理
	 */
	private void initLockClear() {
		cb_lockclear = (CheckBox) findViewById(R.id.cb_lockclear);
		boolean running = ServiceUtils.isRunning(context, LOCK_CLEARSERVICE);
		cb_lockclear.setChecked(running);
		if (running) {
			cb_lockclear.setText("锁屏清理已开启");

		} else {
			cb_lockclear.setText("锁屏清理已关闭");

		}
		cb_lockclear.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked) {
					cb_lockclear.setText("锁屏清理已开启");
					service = new Intent(context, LockClearService.class);
					startService(service);

				} else {
					cb_lockclear.setText("锁屏清理已关闭");
					if (service != null) {
						stopService(service);
					}
				}

			}
		});
	}

	/**
	 * 是否显示系统进程
	 */
	private void initDisplaySystemProcess() {

		cb_IsDisplay = (CheckBox) findViewById(R.id.cb_display_systemprocess);
		boolean is_display_systemprocess = SpUtils.getBoolean(context, ConstantValue.IS_DISPLAY_SYSTEMPROCESS, false);
		cb_IsDisplay.setChecked(is_display_systemprocess);
		if (is_display_systemprocess) {
			cb_IsDisplay.setText("显示系统进程");

		} else {
			cb_IsDisplay.setText("隐藏系统进程");

		}
		cb_IsDisplay.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked) {
					cb_IsDisplay.setText("显示系统进程");

				} else {
					cb_IsDisplay.setText("隐藏系统进程");

				}
				SpUtils.putBoolean(context, ConstantValue.IS_DISPLAY_SYSTEMPROCESS, isChecked);
			}
		});

	}

}
