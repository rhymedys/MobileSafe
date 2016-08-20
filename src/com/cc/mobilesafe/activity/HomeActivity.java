package com.cc.mobilesafe.activity;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.drawable;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.R.menu;
import com.cc.mobilesafe.Utils.ConstantValue;
import com.cc.mobilesafe.Utils.Md5Util;
import com.cc.mobilesafe.Utils.SpUtils;
import com.cc.mobilesafe.Utils.ToastUtil;
import com.cc.mobilesafe.adapter.HomeGridViewAdapter;
import com.lidroid.xutils.view.annotation.event.OnChildClick;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity implements OnItemClickListener {

	private Context context;
	private GridView gv_HomeView;
	private String[] titleStrs;
	private int[] picInts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		this.context = this;

		initUI();
		initData();
		gv_HomeView.setOnItemClickListener(this);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		titleStrs = new String[] { "手机防盗", "通信卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };
		picInts = new int[] { R.drawable.home_safe, R.drawable.home_callmsgsafe, R.drawable.home_apps,
				R.drawable.home_taskmanager, R.drawable.home_netmanager, R.drawable.home_trojan,
				R.drawable.home_sysoptimize, R.drawable.home_tools, R.drawable.home_settings

		};
		gv_HomeView.setAdapter(new HomeGridViewAdapter(context, titleStrs, picInts));
	}

	/**
	 * 初始化HomeUI
	 */
	private void initUI() {

		gv_HomeView = (GridView) findViewById(R.id.gv_HomeView);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		switch (position) {
		case 0:
			showDialog();
			break;
		case 1:

			break;
		case 2:

			break;
		case 3:

			break;
		case 4:

			break;
		case 5:

			break;
		case 6:

			break;
		case 7:
			startActivity(new Intent(context,AToolActivity.class));
			break;
		case 8:
			startActivity(new Intent(context, SettingActivity.class));
			break;

		default:
			break;
		}

	}

	/**
	 * 防盗功能的对话框
	 */
	private void showDialog() {

		String pwd = SpUtils.getString(context, ConstantValue.MOBILE_SAFE_PWD, "");
		if (TextUtils.isEmpty(pwd)) {
			showSetPwdDialog();
		} else {
			showConfirmPwdDialog();
		}

	}

	/**
	 * 确认密码对话框
	 */
	private void showConfirmPwdDialog() {

		Builder builder = new AlertDialog.Builder(context);
		final AlertDialog dialog = builder.create();

		View view = View.inflate(context, R.layout.dialog_confirm_pwd, null);
		final EditText et_PWD = (EditText) view.findViewById(R.id.et_PWD);

		view.findViewById(R.id.btn_Con_PWD_OK).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String pwd = Md5Util.encoder(et_PWD.getText().toString());
				if (!TextUtils.isEmpty(pwd)) {
					String result = SpUtils.getString(context, ConstantValue.MOBILE_SAFE_PWD, "");
					if (pwd.equals(result)) {
						startActivity(new Intent(context, SetupOverActivity.class));
						dialog.dismiss();
					} else {
						ToastUtil.show(context, "密码错误");
					}
				} else {
					ToastUtil.show(context, "密码为空");
				}
			}
		});
		view.findViewById(R.id.btn_Con_PWD_Cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();
			}
		});
		dialog.setView(view);
		dialog.show();

	}

	/**
	 * 初次设置密码对话框
	 */
	private void showSetPwdDialog() {

		Builder builder = new AlertDialog.Builder(context);
		final AlertDialog dialog = builder.create();

		View view = View.inflate(context, R.layout.dialog_set_pwd, null);
		final TextView et_PWD = (TextView) view.findViewById(R.id.et_PWD);
		final TextView et_RPWD = (TextView) view.findViewById(R.id.et_RPWD);
		view.findViewById(R.id.btn_Set_PWD_Cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();
			}
		});
		view.findViewById(R.id.btn_Set_PWD_OK).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String pwd = Md5Util.encoder(et_PWD.getText().toString());
				String rpwd = Md5Util.encoder(et_RPWD.getText().toString());
				if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(rpwd)) {
					if (pwd.equals(rpwd)) {
						SpUtils.putString(context, ConstantValue.MOBILE_SAFE_PWD, pwd);
						Intent intent = new Intent(context, SetupOverActivity.class);
						startActivity(intent);
						dialog.dismiss();
					} else {
						ToastUtil.show(context, "两次密码不一致");
					}
				} else {
					ToastUtil.show(context, "密码输入有误");
				}

			}
		});
		dialog.setView(view);
		dialog.show();

	}
}
