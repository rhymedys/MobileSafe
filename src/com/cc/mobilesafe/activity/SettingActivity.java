package com.cc.mobilesafe.activity;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.R.menu;
import com.cc.mobilesafe.utils.ConstantValue;
import com.cc.mobilesafe.utils.SpUtils;
import com.cc.mobilesafe.view.SettingItemView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {

	private Context context;
	private SettingItemView siv_Update;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		this.context=this;
		
		initUPdate();
	}

	/**
	 * 初始话 更新 设置
	 */
	private void initUPdate() {
		
		 siv_Update  =(SettingItemView) findViewById(R.id.siv_Update);
		 boolean saveResult = SpUtils.getBoolean(context, ConstantValue.OPENUPDATE, false);
		 siv_Update.setCheck(saveResult);
		 siv_Update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				boolean check = siv_Update.isCheck();
				siv_Update.setCheck(!check);
				SpUtils.putBoolean(context, ConstantValue.OPENUPDATE, !check);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
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
