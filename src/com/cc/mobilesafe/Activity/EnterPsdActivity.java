package com.cc.mobilesafe.Activity;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.R.menu;
import com.cc.mobilesafe.Engine.AppInfoProcider;
import com.cc.mobilesafe.Utils.LogUtils;
import com.cc.mobilesafe.Utils.ToastUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EnterPsdActivity extends Activity {

	private static final String TAG = "EnterPsdActivity";
	private Context context;
	private String packageName;
	private TextView tv_name;
	private ImageView iv_icon;
	private EditText et_PWD;
	private Button btn_ok;

	private String pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		LogUtils.i(TAG, TAG + "success");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_psd);
		this.context = this;
		packageName = getIntent().getStringExtra("packageName");

		initUI();
		initData();
	}

	private void initData() {
		// TODO 自动生成的方法存根
		PackageManager packageManager = getPackageManager();
		try {
			iv_icon.setBackground(packageManager.getApplicationIcon(packageName));
			String installerPackageName = packageManager.getInstallerPackageName(packageName);
			tv_name.setText(installerPackageName);
		} catch (NameNotFoundException e) {
			// TODO 自动生成的 catch 块
			LogUtils.i(TAG, "initData fail!!");
			e.printStackTrace();
		}

		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pwd = et_PWD.getText().toString().trim();
				if (TextUtils.isEmpty(pwd)) {
					ToastUtil.show(context, "密码不能为空");
				} else {
					if (pwd.equals("123")) {
						Intent intent = new Intent("android.appwidget.action.SKIP");
						intent.putExtra("packageName", packageName);
						sendBroadcast(intent);
						finish();
					} else {
						ToastUtil.show(context, "密码错误");
					}
				}
			}
		});
	}

	private void initUI() {
		tv_name = (TextView) findViewById(R.id.tv_name);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);
		et_PWD = (EditText) findViewById(R.id.et_PWD);
		btn_ok = (Button) findViewById(R.id.btn_ok);
	}

	@Override
	public void onBackPressed() {
		// 跳转到主界面
		// Intent intent = new Intent(Intent.ACTION_MAIN);
		// intent.addCategory(Intent.CATEGORY_HOME);
		// startActivity(intent);
		// super.onBackPressed();
		super.onAttachedToWindow();
	}
}
