package com.cc.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SpUtils {
	private static SharedPreferences sp;

	/**
	 * 写入标识
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putBoolean(Context context, String key, boolean value) {
		if (sp==null) {
			sp = context.getSharedPreferences(ConstantValue.IPCONFIG, context.MODE_PRIVATE);
		}
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
		
	}
	
	/**
	 * 读取标识
	 * @param context 上下文
	 * @param key  键值
	 * @param defValue  存储节点没有的时候 默认值
	 * @return  返回状态，如果没有 默认为defValue
	 */
	public static boolean getBoolean(Context context, String key, boolean defValue) {
		if (sp==null) {
			sp = context.getSharedPreferences(ConstantValue.IPCONFIG, context.MODE_PRIVATE);
		}
		return sp.getBoolean(key, defValue);
	
		
	}
}
