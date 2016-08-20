package com.cc.mobilesafe.Utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.nfc.tech.IsoDep;

public class ServiceUtils {
	/**
	 * 检查 服务是否运行状态
	 * @param ServiceName 服务名称
	 * @return true 则正在运行
	 */
	public static boolean isRunning(Context context,String ServiceName){
		boolean result=false;
		ActivityManager activityManager=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = activityManager.getRunningServices(1000);
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			if (runningServiceInfo.service.getClassName().equals(ServiceName)) {
				result=true;
			}
		}
		return result;
	}
}
