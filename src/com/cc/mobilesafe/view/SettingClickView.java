package com.cc.mobilesafe.view;

import com.cc.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingClickView extends RelativeLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.cc.mobilesafe";
	private TextView tv_des;
	private TextView tv_title;

	public SettingClickView(Context context) {
		this(context, null);
		// TODO 自动生成的构造函数存根
	}

	public SettingClickView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO 自动生成的构造函数存根
	}

	public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);

		// TODO 自动生成的构造函数存根
	}

	public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO 自动生成的构造函数存根

		View.inflate(context, R.layout.setting_click_view, this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_des = (TextView) findViewById(R.id.tv_des);

	}

	public void setTitle(String title) {
		tv_title.setText(title);
	}

	public void setDescription(String Description) {
		tv_des.setText(Description);
	}

}
