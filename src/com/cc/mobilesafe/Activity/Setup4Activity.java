package com.cc.mobilesafe.Activity;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.R.menu;
import com.cc.mobilesafe.Utils.ConstantValue;
import com.cc.mobilesafe.Utils.SpUtils;
import com.cc.mobilesafe.Utils.ToastUtil;

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

public class Setup4Activity extends BaseSetupActivity {

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
	}


	@Override
	protected void showPrePage() {
		// TODO 自动生成的方法存根
		startActivity(new Intent(context, Setup3Activity.class));
		overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_in_out_anim);
		finish();
	}

	@Override
	protected void showNextPage() {
		// TODO 自动生成的方法存根
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
}
