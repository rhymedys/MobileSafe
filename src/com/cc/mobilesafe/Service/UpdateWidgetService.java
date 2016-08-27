package com.cc.mobilesafe.Service;

import java.util.Timer;
import java.util.TimerTask;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.Activity.HomeActivity;
import com.cc.mobilesafe.Engine.ProcessInfoProvider;

import com.cc.mobilesafe.Reciver.CCMobileSafeWidgetProvider;
import com.cc.mobilesafe.Utils.LogUtils;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {

	private static final String TAG = "UpdateWidgetService !!!!";
	private Timer timer;
	private WidgetTimerReciver widgetTimerReciver;

	@Override
	public void onCreate() {

		startTimer();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		widgetTimerReciver = new WidgetTimerReciver();
		registerReceiver(widgetTimerReciver, filter);
		super.onCreate();
	}

	/**
	 * 定时器
	 */
	private void startTimer() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {

				updateWidget();
			}

		}, 0, 3000);
	}

	/**
	 * 更新widget
	 */
	private void updateWidget() {

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.process_widget);
		remoteViews.setTextViewText(R.id.process_count, "进程总数：" + ProcessInfoProvider.getProcessCout(this));
		String formatFileSize = Formatter.formatFileSize(this, ProcessInfoProvider.getRestMemory(this));
		LogUtils.i(TAG, formatFileSize);
		remoteViews.setTextViewText(R.id.process_memory, "可用内存：" + formatFileSize);

		Intent intent = new Intent("android.appwidget.action.WIDGET_CLEAR_ALLPROCESS");
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);

		/*
		 * Intent intent = new Intent(getApplicationContext(),
		 * HomeActivity.class); PendingIntent pendingIntent =
		 * PendingIntent.getActivity(this, 0, intent,
		 * PendingIntent.FLAG_CANCEL_CURRENT);
		 * remoteViews.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
		 */
		ComponentName component = new ComponentName(this, CCMobileSafeWidgetProvider.class);
		appWidgetManager.updateAppWidget(component, remoteViews);
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onDestroy() {

		if (widgetTimerReciver != null) {
			unregisterReceiver(widgetTimerReciver);
		}
		timer.cancel();
		timer=null;
		LogUtils.i(TAG, "onDestroy");
		super.onDestroy();
	}

	class WidgetTimerReciver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			String action = intent.getAction();
			if (action.equals(Intent.ACTION_SCREEN_OFF)) {
				if (timer != null) {
					timer.cancel();
					timer=null;
				}
			}
			if (action.equals(Intent.ACTION_SCREEN_ON)) {
				startTimer();
			}
		}

	}

}
