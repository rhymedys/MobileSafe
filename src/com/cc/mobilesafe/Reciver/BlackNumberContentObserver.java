package com.cc.mobilesafe.Reciver;

import com.cc.mobilesafe.Utils.LogUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

public class BlackNumberContentObserver extends ContentObserver {

	private static final String TAG = "BlackNumberContentObserver";

	private String phone;

	private Uri uri;
	private Handler handler;

	private Context context;

	public BlackNumberContentObserver(Handler handler, Context context, Uri uri, String phone) {
		// super(handler)要放在第一行
		super(handler);
		this.phone = phone;
		this.context = context;
		this.uri = uri;
		this.handler = handler;

	}

	@Override
	public void onChange(boolean selfChange) {
		int delete = context.getContentResolver().delete(uri, "number=?", new String[] { phone });
		LogUtils.i(TAG, delete+"");
		super.onChange(selfChange);
	}

}
