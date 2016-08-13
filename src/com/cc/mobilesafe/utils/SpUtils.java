package com.cc.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SpUtils {
	private static SharedPreferences sp;
	/**
	 * 配置文件的名称
	 */
	public static final String CONFIG = "config";

	/**
	 * 写入存储标识
	 * @param context 上下文
	 * @param key 键值
	 * @param value  值
	 */
	public static void putBoolean(Context context, String key, boolean value) {
		if (sp==null) {
			sp = context.getSharedPreferences(CONFIG, context.MODE_PRIVATE);
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
			sp = context.getSharedPreferences(CONFIG, context.MODE_PRIVATE);
		}
		return sp.getBoolean(key, defValue);
	
		
	}
	/**
	 * 写入存储标识
	 * @param context 上下文
	 * @param key 键值
	 * @param value  存储String的值
	 */
	public static void putString(Context context, String key, String value) {
		if (sp==null) {
			sp = context.getSharedPreferences(CONFIG, context.MODE_PRIVATE);
		}
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
		
	}
	
	/**
	 * 读取标识
	 * @param context 上下文
	 * @param key  键值
	 * @param defValue  存储节点没有的时候 默认值
	 * @return  返回状态，如果没有 默认为defValue
	 */
	public static String getString(Context context, String key, String defValue) {
		if (sp==null) {
			sp = context.getSharedPreferences(CONFIG, context.MODE_PRIVATE);
		}
		return sp.getString(key, defValue);
	
		
	}

	/**
	 * @param context 上下文
	 * @param key 要删除的key键值
	 */
	public static void remove(Context context, String key) {
		// TODO 自动生成的方法存根
		if (sp==null) {
			sp=context.getSharedPreferences(CONFIG, context.MODE_PRIVATE);
		}
		Editor edit = sp.edit();
		edit.remove(key);
		edit.commit();
	}
	
	

}
