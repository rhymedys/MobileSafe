package com.cc.mobilesafe.Engine;

import java.util.ArrayList;
import java.util.List;

import com.cc.mobilesafe.Bean.AppInfoBean;
import com.cc.mobilesafe.Utils.LogUtils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.GetChars;

public class AppInfoProcider {
	private static final String TAG = "AppInfoProcider";
	private static ArrayList<AppInfoBean> appList;

	/**
	 * 返回当前手机所有应用的相关信息(名称，包名，图标，(内存，sd卡，)(系统，用户))
	 */
	public static ArrayList<AppInfoBean> getAppInfoList(Context context) {
		appList = new ArrayList<AppInfoBean>();
		PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
		LogUtils.i(TAG, installedPackages.size() + "");
		AppInfoBean appInfoBean = null;
		for (PackageInfo packageInfo : installedPackages) {
			appInfoBean = new AppInfoBean();
			appInfoBean.packageName = packageInfo.packageName;
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			appInfoBean.name = applicationInfo.loadLabel(packageManager).toString();
			appInfoBean.icon = applicationInfo.loadIcon(packageManager);
			// 判断是否为系统应用
			if ((applicationInfo.flags & applicationInfo.FLAG_SYSTEM) == applicationInfo.FLAG_SYSTEM) {
				appInfoBean.isSystemApp = true;
			} else {
				appInfoBean.isSystemApp = false;
			}
			if ((applicationInfo.flags
					& applicationInfo.FLAG_EXTERNAL_STORAGE) == applicationInfo.FLAG_EXTERNAL_STORAGE) {
				appInfoBean.isSDCard = true;
			} else {
				appInfoBean.isSDCard = false;
			}
			appList.add(appInfoBean);
		}
		return appList;
	}

	/**
	 * 分类获取 系统应用 或者 用户应用
	 * @param context
	 * @param isSystemApp true返回的是系统应用，false 返回的是 用户应用
	 * @return  true返回的是系统应用，false 返回的是 用户应用
	 */
	public static List<AppInfoBean> getClassApp(Context context, boolean isSystemApp) {
		ArrayList<AppInfoBean> allAppInfoList = getAppInfoList(context);
		ArrayList<AppInfoBean> classAppList = new ArrayList<AppInfoBean>();
		for (AppInfoBean bean : allAppInfoList) {
			if (isSystemApp & bean.isSystemApp) {
				// 系统应用
				classAppList.add(bean);
			}
			if (!isSystemApp &! bean.isSystemApp){
				//  用户应用
				classAppList.add(bean);
			}

		}
		return classAppList;
	}
}
