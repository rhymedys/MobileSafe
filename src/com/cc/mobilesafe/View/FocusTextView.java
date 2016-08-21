package com.cc.mobilesafe.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author Rhymedys
 *能够获取焦点的自定义textview
 */
public class FocusTextView extends TextView {


	public FocusTextView(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
	}
	
	public FocusTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
	}

	public FocusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO 自动生成的构造函数存根
	}

	@Override
	public boolean isFocused() {
		// TODO 自动生成的方法存根
		return true;
	}
}
