package com.cc.mobilesafe.activity;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.R.menu;
import com.cc.mobilesafe.utils.ConstantValue;
import com.cc.mobilesafe.utils.SpUtils;
import com.cc.mobilesafe.utils.ToastUtil;

import android.app.Activity;
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

public class Setup4Activity extends Activity {

	private Context context;
	private CheckBox cb_box;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		this.context = this;

		initUI();
	}

	/**
	 * 初始化ui布局
	 */
	private void initUI() {
		// 
		boolean open_safe_security = SpUtils.getBoolean(context, ConstantValue.OPEN_SAFE_SECURITY, false);
		cb_box = (CheckBox) findViewById(R.id.cb_box);
		// cb设置初始化状态
		cb_box.setChecked(open_safe_security);
		if (open_safe_security) {
			cb_box.setText("安全设置已开启");
		} else {
			cb_box.setText("安全设置已关闭");
		}
		// 设置点击状态
		cb_box.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// 

				if (isChecked) {
					cb_box.setText("安全设置已开启");
				} else {
					cb_box.setText("安全设置已关闭");
				}
				SpUtils.putBoolean(context, ConstantValue.OPEN_SAFE_SECURITY, isChecked);
			}
		});

		findViewById(R.id.btn_SetupBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 
				startActivity(new Intent(context, Setup3Activity.class));
				overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_in_out_anim);
				finish();
			}
		});

		findViewById(R.id.btn_SetupNext).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 
				boolean temp = SpUtils.getBoolean(context, ConstantValue.OPEN_SAFE_SECURITY, false);
				if (temp) {
					SpUtils.putBoolean(context, ConstantValue.SETUP_OVER, true);
					
					startActivity(new Intent(context, SetupOverActivity.class));
					overridePendingTransition(R.anim.next_in_anim, R.anim.next_in_out_anim);
					finish();
				} else {
					ToastUtil.show(context, "安全设置未开启");
				}

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setup4, menu);
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
}
