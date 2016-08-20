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
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class Setup3Activity extends BaseSetupActivity {

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

		findViewById(R.id.btn_selectcontact).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根

				Intent intent = new Intent(context, ContactListActivity.class);
				startActivityForResult(intent, 0);

			}
		});
		
		findViewById(R.id.btn_SetupNext).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				showNextPage();
			}
		});
		
		findViewById(R.id.btn_SetupBack).setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				showPrePage();
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
	protected void showPrePage() {

		startActivity(new Intent(context, Setup2Activity.class));
		overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_in_out_anim);
		finish();
	}


	@Override
	protected void showNextPage() {

		String num = et_safenum.getText().toString().trim();
		if (!TextUtils.isEmpty(num)) {
			SpUtils.putString(context, ConstantValue.SAFE_CONTACT_NUM, num);
			startActivity(new Intent(context, Setup4Activity.class));
			overridePendingTransition(R.anim.next_in_anim, R.anim.next_in_out_anim);
			finish();
		}else{
			ToastUtil.show(context, "请输入安全号码");
		}
	}
}
