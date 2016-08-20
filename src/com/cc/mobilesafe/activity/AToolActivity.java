package com.cc.mobilesafe.activity;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.R.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class AToolActivity extends Activity {

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atool);
		this.context = this;

		//归属地查询方法
		initPhoneAddress();
	}

	private void initPhoneAddress() {
		// TODO 自动生成的方法存根
		findViewById(R.id.tv_query_phone_address).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				startActivity(new Intent(context, QueryAddressActivity.class));
			}
		});
	}



}
