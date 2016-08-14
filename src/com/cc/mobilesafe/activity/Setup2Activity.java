package com.cc.mobilesafe.activity;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.R.menu;
import com.cc.mobilesafe.utils.ConstantValue;
import com.cc.mobilesafe.utils.SpUtils;
import com.cc.mobilesafe.utils.ToastUtil;
import com.cc.mobilesafe.view.SettingItemView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

public class Setup2Activity extends BaseSetupActivity {

	private Context context;
	private SettingItemView siv_sim_bound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		this.context = this;

		initUI();

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// TODO 自动生成的方法存根
		String simnum = SpUtils.getString(context, ConstantValue.SIMNUM, "");
		if (TextUtils.isEmpty(simnum)) {
			siv_sim_bound.setCheck(false);
		} else {
			siv_sim_bound.setCheck(true);
		}
		
		findViewById(R.id.btn_SetupBack).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				showPrePage();
			}
		});
		findViewById(R.id.btn_SetupNext).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				showNextPage();
			}
		});
	}

	/**
	 * 初始化ui布局
	 */
	private void initUI() {
		// TODO 自动生成的方法存根

		siv_sim_bound = (SettingItemView) findViewById(R.id.siv_sim_bound);
		initData();
		siv_sim_bound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				boolean check = siv_sim_bound.isCheck();
				siv_sim_bound.setCheck(!check);
				if (!check) {
					TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					String simSerialNumber = telephonyManager.getSimSerialNumber();
					if (simSerialNumber != null) {
						SpUtils.putString(context, ConstantValue.SIMNUM, simSerialNumber);
					}
				} else {
					SpUtils.remove(context, ConstantValue.SIMNUM);
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setup2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void showPrePage() {
		// TODO 自动生成的方法存根
		startActivity(new Intent(context, SetupActivity.class));
		overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_in_out_anim);
		finish();
	}

	@Override
	protected void showNextPage() {
		// TODO 自动生成的方法存根
		if (!TextUtils.isEmpty(SpUtils.getString(context, ConstantValue.SIMNUM, ""))) {
			startActivity(new Intent(context, Setup3Activity.class));
			overridePendingTransition(R.anim.next_in_anim, R.anim.next_in_out_anim);
			finish();
		} else {
			ToastUtil.show(context, "请绑定sim卡后继续下一页操作");
		}
	}
}
