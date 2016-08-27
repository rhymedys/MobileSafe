package com.cc.mobilesafe.Reciver;

import com.cc.mobilesafe.Service.UpdateWidgetService;
import com.cc.mobilesafe.Utils.LogUtils;
import com.cc.mobilesafe.Utils.ServiceUtils;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.UpdateAppearance;

public class CCMobileSafeWidgetProvider extends AppWidgetProvider {
	private static final String TAG = "CCMobileSafeWidgetProvider";
	private static final String SERVICENAME = "com.cc.mobilesafe.Service.UpdateWidgetService";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 每次接收到广播的时候

		super.onReceive(context, intent);
		LogUtils.i(TAG, "onReceive");
	}

	@Override
	public void onEnabled(Context context) {
		// 第一次添加的时候
		context.startService(new Intent(context, UpdateWidgetService.class));

		LogUtils.i(TAG, "onEnabled");
		super.onEnabled(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		// 发生更新 添加时
		context.startService(new Intent(context, UpdateWidgetService.class));
		LogUtils.i(TAG, "onUpdate");
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {
		// 部件长宽高 发生改变时发生 改变
		context.startService(new Intent(context, UpdateWidgetService.class));
		LogUtils.i(TAG, "onAppWidgetOptionsChanged");
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
	}

	@Override
	public void onDisabled(Context context) {
		// 最终消失 删除最后一个控件
		context.stopService(new Intent(context, UpdateWidgetService.class));
		LogUtils.i(TAG, "onDisabled");
		super.onDisabled(context);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// 删除的时候 删除一个控件的时候
		LogUtils.i(TAG, "onDeleted");
		super.onDeleted(context, appWidgetIds);
	}

}
