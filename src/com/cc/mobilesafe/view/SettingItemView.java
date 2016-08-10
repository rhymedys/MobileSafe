package com.cc.mobilesafe.view;

import com.cc.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout {

	public static final int CustomizeStyle = 0x7f010004;
	private CheckBox cb_box;
	private TextView tv_des;

	public SettingItemView(Context context) {
		this(context, null);
		// TODO 自动生成的构造函数存根
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO 自动生成的构造函数存根
	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);

		// TODO 自动生成的构造函数存根
	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO 自动生成的构造函数存根

		View.inflate(context, R.layout.setting_item_view, this);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		tv_des = (TextView) findViewById(R.id.tv_des);
		cb_box = (CheckBox) findViewById(R.id.cb_box);
	}

	/**
	 * 判断checkbox是否选择状态
	 * 
	 * @return
	 */
	public boolean isCheck() {
		return cb_box.isChecked();
	}

	/**
	 * @param isCkeck
	 *            是否作为开启的变量
	 */
	public void setCheck(boolean isCkeck) {
		cb_box.setChecked(isCkeck);
		if (isCkeck) {
			tv_des.setText("自动更新已开启");
		}else {
			tv_des.setText("自动更新已关闭");
		}
	}
	
	
	
	
	
	

}
