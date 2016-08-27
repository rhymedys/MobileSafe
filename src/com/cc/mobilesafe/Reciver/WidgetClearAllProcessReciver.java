package com.cc.mobilesafe.Reciver;

import com.cc.mobilesafe.Engine.ProcessInfoProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class WidgetClearAllProcessReciver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自动生成的方法存根
		ProcessInfoProvider.killALLProcess(context);
	}

}
