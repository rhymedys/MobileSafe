package com.cc.mobilesafe.Activity;

import java.io.File;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.R.menu;
import com.cc.mobilesafe.Engine.SmsBackup;
import com.cc.mobilesafe.Utils.ToastUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Telephony.Sms;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class AToolActivity extends Activity {

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO 自动生成的方法存根
			super.handleMessage(msg);
			switch (msg.what) {
			case EMPTY_SMS:
				ToastUtil.show(context, "通讯录中没有短信");
				break;

			default:
				break;
			}
		}
	};
	private Context context;
	/**
	 * 短信为空
	 */
	private static final int EMPTY_SMS = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atool);
		this.context = this;

		// 归属地查询方法
		initPhoneAddress();
		// 短信备份功能
		initSmsBackup();
	}

	/**
	 * 短信备份功能
	 */
	private void initSmsBackup() {

		findViewById(R.id.tv_sms_backup).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				findViewById(R.id.tv_sms_backup).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						showSmsBackupDialog();

					}

				});
				;

			}
		});
	}

	/**
	 * 查询号码归属地功能
	 */
	private void initPhoneAddress() {

		findViewById(R.id.tv_query_phone_address).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(new Intent(context, QueryAddressActivity.class));
			}
		});
	}

	/**
	 * 展示SMS备份过程中的进度条
	 */
	private void showSmsBackupDialog() {
		// TODO 自动生成的方法存根
		final ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("短信正在备份中");
		progressDialog.setIcon(R.drawable.ic_launcher);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

		// 获取系统短信
		new Thread(new Runnable() {

			public void run() {
				String smsSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.pathSeparator
						+ "smsBackup.xml";
				if (progressDialog != null) {
					SmsBackup.backup(context, smsSavePath, progressDialog, handler);
				}

			}
		}).start();
	}

}
