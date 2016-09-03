package com.cc.mobilesafe.global;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import com.cc.mobilesafe.Utils.LogUtils;
import com.lidroid.xutils.HttpUtils;

import android.app.Application;
import android.os.Environment;

public class MyApplication extends Application {

	protected static final String TAG = "MyApplication";

	@Override
	public void onCreate() {
		// TODO 自动生成的方法存根
		super.onCreate();

		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread thread, Throwable ex) {
				// TODO 自动生成的方法存根
				ex.printStackTrace();
				LogUtils.i(TAG, "catch a uncaughtException ");

				String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.pathSeparator
						+ "collectError.log";
				try {
					PrintWriter printWriter = new PrintWriter(path);
					ex.printStackTrace(printWriter);
					printWriter.close();
				} catch (FileNotFoundException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				System.exit(0);
			}
		});
	}

}
