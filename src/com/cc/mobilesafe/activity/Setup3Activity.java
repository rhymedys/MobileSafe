package com.cc.mobilesafe.activity;

import java.lang.annotation.Annotation;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.R.menu;
import com.cc.mobilesafe.utils.ConstantValue;
import com.cc.mobilesafe.utils.SpUtils;
import com.cc.mobilesafe.utils.ToastUtil;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class Setup3Activity extends Activity {

	private Context context;
	private EditText et_safenum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		this.context = this;

		initUI();
	}

	/**
	 * 初始化ui布局
	 */
	private void initUI() {
		// TODO 自动生成的方法存根
		et_safenum = (EditText) findViewById(R.id.et_safenum);
		String temp = SpUtils.getString(context, ConstantValue.SAFE_CONTACT_NUM, "");
		if (!TextUtils.isEmpty(temp)) {
			et_safenum.setText(temp);
		}

		findViewById(R.id.btn_SetupNext).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				String num = et_safenum.getText().toString().trim();
				if (!TextUtils.isEmpty(num)) {
					SpUtils.putString(context, ConstantValue.SAFE_CONTACT_NUM, num);
					startActivity(new Intent(context, Setup4Activity.class));
					finish();
				}else{
					ToastUtil.show(context, "请输入安全号码");
				}

			}
		});

		findViewById(R.id.btn_SetupBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				startActivity(new Intent(context, Setup2Activity.class));
				finish();
			}
		});

		findViewById(R.id.btn_selectcontact).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根

				Intent intent = new Intent(context, ContactListActivity.class);
				startActivityForResult(intent, 0);

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 返回到当前页面后处理接收的结果
		switch (requestCode) {
		case 0:
			if (data != null) {
				String phoneNum = data.getStringExtra("phoneNum");
				phoneNum = phoneNum.replace("-", "").replace(" ", "").trim();
				et_safenum.setText(phoneNum);
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setup3, menu);
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
