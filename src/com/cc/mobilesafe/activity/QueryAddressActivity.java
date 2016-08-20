package com.cc.mobilesafe.activity;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.Utils.ToastUtil;
import com.cc.mobilesafe.engine.AddressDao;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.TextView;

public class QueryAddressActivity extends Activity {

	private Context context;
	private EditText et_phone;
	private TextView tv_phoneaddress;
	private String address;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO 自动生成的方法存根
			tv_phoneaddress.setText(address);
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_address);
		this.context = this;

		initUI();
	}
	
	
	private void initUI() {
		et_phone = (EditText) findViewById(R.id.et_phone);
		tv_phoneaddress = (TextView) findViewById(R.id.tv_phoneaddress);
		findViewById(R.id.btn_query).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				String phone = et_phone.getText().toString().trim();
				if (!TextUtils.isEmpty(phone)) {
					query(phone);

				} else {
					Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.shake);
					et_phone.startAnimation(loadAnimation);		
					ToastUtil.show(context, "请输入正确的电话号码");
				}
			}
		});

		et_phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根

				String phone = et_phone.getText().toString().trim();
				query(phone);
			}
		});
	}

	protected void query(final String phone) {
		// TODO 自动生成的方法存根
		new Thread(new Runnable() {
			public void run() {
				address = AddressDao.getAddress(phone);
				handler.sendEmptyMessage(0);
			}
		}).start();
	}

}
