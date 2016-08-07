package com.cc.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	public static boolean DEBUG = true;
	public static void show(Context context ,String text){
		if (DEBUG) {
			Toast.makeText(context, text, 0).show();
		}

	}
	
}
