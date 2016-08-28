package com.cc.mobilesafe.View;

import com.cc.mobilesafe.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingItemView extends RelativeLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.cc.mobilesafe";
	private CheckBox cb_box;
	private TextView tv_des;
	private String mDesoff;
	private String mDeson;
	private String mDestitle;

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
		// attrs为属性集
		

		View.inflate(context, R.layout.setting_item_view, this);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		tv_des = (TextView) findViewById(R.id.tv_des);
		cb_box = (CheckBox) findViewById(R.id.cb_box);
		
		
		initAttrs(attrs);
		tv_title.setText(mDestitle);
	}

	/**
	 * @param attrs构造方法中维护好的属性集合
	 * 返回属性集合中自定义的属性值
	 */
	private void initAttrs(AttributeSet attrs) {
		mDestitle = attrs.getAttributeValue(NAMESPACE, "destitle");
		mDesoff = attrs.getAttributeValue(NAMESPACE, "desoff");
		mDeson = attrs.getAttributeValue(NAMESPACE, "deson");
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
	 *            作为是否开启的变量
	 */
	public void setCheck(boolean isCkeck) {
		cb_box.setChecked(isCkeck);
		if (isCkeck) {
			tv_des.setText(mDeson);
		}else {
			tv_des.setText(mDesoff);
		}
	}
	
	
	
	
	
	

}
