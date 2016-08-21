package com.cc.mobilesafe.Activity;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.R.menu;
import com.cc.mobilesafe.Utils.ConstantValue;
import com.cc.mobilesafe.Utils.LogUtils;
import com.cc.mobilesafe.Utils.SpUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SetupOverActivity extends Activity {

	protected static final String TAG = "SetupOverActivity";
	private Context context;
	private TextView tv_Num;
	private TextView tv_Reset_Setup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = this;

		boolean setup_over = SpUtils.getBoolean(context, ConstantValue.SETUP_OVER, false);
		if (setup_over) {
			setContentView(R.layout.activity_setup_over);
		} else {
			startActivity(new Intent(context, SetupActivity.class));
			finish();
		}

		initUI();
		initData();
	}

	private void initData() {
		// TODO 自动生成的方法存根
		String safe_contact_num = SpUtils.getString(context, ConstantValue.SAFE_CONTACT_NUM, "");
		if (!TextUtils.isEmpty(safe_contact_num)) {
			tv_Num.setText(safe_contact_num);
		}
	}

	private void initUI() {
		tv_Num = (TextView) findViewById(R.id.tv_Oversetup_Num);
		tv_Reset_Setup = (TextView) findViewById(R.id.tv_Reset_Setup);
		
		tv_Reset_Setup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				startActivity(new Intent(context, SetupActivity.class));
				finish();
			}
		});

	}

}
