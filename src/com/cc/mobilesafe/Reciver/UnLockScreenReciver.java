package com.cc.mobilesafe.Reciver;

import com.cc.mobilesafe.Engine.ProcessInfoProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UnLockScreenReciver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ProcessInfoProvider.killALLProcess(context);
	}

}
