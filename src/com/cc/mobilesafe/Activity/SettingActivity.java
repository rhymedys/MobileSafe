package com.cc.mobilesafe.Activity;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.R.menu;
import com.cc.mobilesafe.Service.AddressService;
import com.cc.mobilesafe.Utils.ConstantValue;
import com.cc.mobilesafe.Utils.LogUtils;
import com.cc.mobilesafe.Utils.ServiceUtils;
import com.cc.mobilesafe.Utils.SpUtils;
import com.cc.mobilesafe.View.SettingClickView;
import com.cc.mobilesafe.View.SettingItemView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity {

	private static final String ADDRESS_SERVICE = "com.cc.mobilesafe.Service.AddressService";
	protected static final String TAG = "SettingActivity";
	private Context context;
	private SettingItemView siv_Update;
	private SettingItemView siv_locationset;
	private SettingClickView scv_set_location_style;
	private String[] styles;
	private int intStyle;
	private SettingClickView scv_set_toast_location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		this.context = this;

		initUpdate();
		initSettingShowLocation();
		initSettingShowLocationStyle();
		initSettingShowLocationArea();
		
	}

	/**
	 * 设置来电显示的位置
	 */
	private void initSettingShowLocationArea() {
		// TODO 自动生成的方法存根
		scv_set_toast_location = (SettingClickView) findViewById(R.id.scv_set_toast_location);
		scv_set_toast_location.setTitle("归属地提示框位置");
		scv_set_toast_location.setDescription("归属地提示框位置");
		scv_set_toast_location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
					startActivity(new Intent(context, ToastLocationActivity.class));
			}
		});
	}

	/**
	 * 设置来电显示的的样式
	 */
	private void initSettingShowLocationStyle() {
		styles = new String[] { "透明", "橙色", "蓝色", "灰色", "绿色" };
		scv_set_location_style = (SettingClickView) findViewById(R.id.scv_set_toast_style);
		scv_set_location_style.setTitle("设置归属地显示风格");
		intStyle = SpUtils.getInt(context, ConstantValue.TOAST_STYLE, 0);
		scv_set_location_style.setDescription(styles[intStyle]);
		scv_set_location_style.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				LogUtils.i(TAG, "scv_set_is_location_style.onClick");
				
				//重新读取 intStyle
				intStyle = SpUtils.getInt(context, ConstantValue.TOAST_STYLE, 0);
				
				
				showToastStyleDialog();

			}
		});
	}

	protected void showToastStyleDialog() {
		// TODO 自动生成的方法存根
		Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("请选择归属地样式");
		builder.setSingleChoiceItems(styles, intStyle, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO 自动生成的方法存根

				SpUtils.putInt(context, ConstantValue.TOAST_STYLE, which);
				scv_set_location_style.setDescription(styles[which]);
				dialog.dismiss();
			}

		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO 自动生成的方法存根
				dialog.dismiss();
			}

		});
		builder.show();
	}

	/**
	 * 设置是否开启来电显示功能
	 */
	private void initSettingShowLocation() {
		//
		siv_locationset = (SettingItemView) findViewById(R.id.siv_set_is_toast);

		boolean isRunning = ServiceUtils.isRunning(context, ADDRESS_SERVICE);
		siv_locationset.setCheck(isRunning);

		siv_locationset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//
				boolean check = siv_locationset.isCheck();
				siv_locationset.setCheck(!check);
				isOpenSettingLocationShowService(!check);
			}
		});

	}

	/**
	 * 是否开启来电位置功能的服务     这是一个被调用的服务 this is service
	 * 
	 * @param is_setting_show_location
	 */
	protected void isOpenSettingLocationShowService(boolean is_setting_show_location) {
		//
		if (is_setting_show_location) {
			Intent service = new Intent(context, AddressService.class);
			startService(service);
		} else {
			stopService(new Intent(context, AddressService.class));
		}
	}

	/**
	 * 初始话 更新 设置
	 */
	private void initUpdate() {

		siv_Update = (SettingItemView) findViewById(R.id.siv_Update);
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

}
