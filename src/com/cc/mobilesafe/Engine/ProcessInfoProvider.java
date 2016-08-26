package com.cc.mobilesafe.Engine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.BasicEofSensorWatcher;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.Bean.ProcessInfoBean;
import com.cc.mobilesafe.Utils.LogUtils;

import android.R.string;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.MutableContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

public class ProcessInfoProvider {
	private static final String TAG = "ProcessInfoProvider";
	private static ActivityManager systemService;

	/**
	 * 赶回进程总数
	 * 
	 * @param context
	 * @return
	 */
	public static int getProcessCout(Context context) {
		int size = 0;
		try {
			systemService = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> runningAppProcesses = systemService.getRunningAppProcesses();
			size = runningAppProcesses.size();
		} catch (Exception e) {

			e.printStackTrace();
			LogUtils.i(TAG, "getProcessCout fail!!!!" + size);
		}
		return size;

	}

	/**
	 * 返回可用的内存 返回单位为 bytes
	 * 
	 * @param context
	 * @return
	 */
	public static long getRestMemory(Context context) {
		long size = 0;
		try {
			systemService = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			MemoryInfo memoryInfo = new MemoryInfo();
			systemService.getMemoryInfo(memoryInfo);

			size = memoryInfo.availMem;
		} catch (Exception e) {
			//
			LogUtils.i(TAG, "getRestMemory fail!!!!" + size);
		}
		return size;
	}

	/**
	 * 获取总内存 返回 单位为bytes long类型的
	 * 
	 * @param context
	 * @return
	 */
	public static long getTotalMemory(Context context) {
		long size = 0;
		// BufferedReader bufferedReader = null;
		// FileReader fileReader = null;

		try {
			systemService = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			MemoryInfo memoryInfo = new MemoryInfo();
			systemService.getMemoryInfo(memoryInfo);
			// 低版本 不能用这种方式 api16以上才能使用
			size = memoryInfo.totalMem;
			//
			// fileReader = new FileReader("pro/meminfo");
			//
			// bufferedReader = new BufferedReader(fileReader);
			// String readLine = bufferedReader.readLine();
			// char[] charArray = readLine.toCharArray();
			//
			// StringBuffer stringBuffer = new StringBuffer();
			// for (char c : charArray) {
			// if (c >= '0' && c <= '9') {
			// stringBuffer.append(c);
			// }
			// }
			// size = Long.parseLong(stringBuffer.toString()) * 1024;
			// } catch (Exception e) {
			//
			// LogUtils.i(TAG, "getTotalMemory fail!!!!" + size);
			// } finally {
			// if (fileReader != null) {
			// try {
			// fileReader.close();
			// } catch (IOException e) {
			//
			// e.printStackTrace();
			// }
			// }
			// if (bufferedReader != null) {
			// try {
			// bufferedReader.close();
			// } catch (IOException e) {
			//
			// e.printStackTrace();
			// }
			// }

		} catch (Exception e) {
			LogUtils.i(TAG, "getTotalMemory fail!!!!" + size);
		}
		return size;
	}

	/**
	 * 获取所有进程信息
	 * 
	 * @param context
	 * @return
	 */
	public static List<ProcessInfoBean> getAllProcessInfo(Context context) {
		List<ProcessInfoBean> list = new ArrayList<ProcessInfoBean>();
		try {
			systemService = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> runningAppProcesses = systemService.getRunningAppProcesses();
			PackageManager packageManager = context.getPackageManager();
			ProcessInfoBean bean = null;

			for (RunningAppProcessInfo temp : runningAppProcesses) {
				bean = new ProcessInfoBean();
				bean.packageName = temp.processName;
				// 获取占用的内存大小
				android.os.Debug.MemoryInfo[] processMemoryInfo = systemService
						.getProcessMemoryInfo(new int[] { temp.pid });

				android.os.Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
				bean.useMemorySize = (long) (memoryInfo.getTotalPrivateDirty() * 1024);

				try {
					ApplicationInfo applicationInfo = packageManager.getApplicationInfo(temp.processName, 0);
					bean.name = applicationInfo.loadLabel(packageManager).toString();
					bean.icon = applicationInfo.loadIcon(packageManager);

					if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
						bean.isSystemProcess = true;
					} else {
						bean.isSystemProcess = false;
					}
				} catch (NameNotFoundException e) {

					LogUtils.i(TAG, "NameNotFoundException  NameNotFoundException");
					bean.name = temp.processName;
					bean.icon = context.getResources().getDrawable(R.drawable.ic_launcher);
					bean.isSystemProcess = true;

					e.printStackTrace();
				}

				list.add(bean);
			}
		} catch (Exception e) {

			LogUtils.i(TAG, "getProcessInfo  fail!!!!!");
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 获取分类进程 true 代表系统进程 否则为 用户进程
	 * 
	 * @param context
	 * @param isSystem
	 *            是否是系统进程
	 * @return
	 */
	public static List<ProcessInfoBean> getClassProcessInfo(Context context, boolean isSystem) {
		List<ProcessInfoBean> list = new ArrayList<ProcessInfoBean>();
		List<ProcessInfoBean> allProcessInfo = getAllProcessInfo(context);
		for (ProcessInfoBean processInfoBean : allProcessInfo) {
			if (processInfoBean.isSystemProcess & isSystem) {
				list.add(processInfoBean);
			}
			if ((!processInfoBean.isSystemProcess) & (!isSystem)) {
				list.add(processInfoBean);
			}
		}
		return list;
	}

	/**
	 * 杀死指定包名的进程
	 * 
	 * @param context
	 * @param bean
	 *            ProcessInfoBean 实体
	 */
	public static void killProcess(Context context, ProcessInfoBean bean) {
		// TODO 自动生成的方法存根
		try {
			systemService = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			systemService.killBackgroundProcesses(bean.packageName);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			LogUtils.i(TAG, "killProcess fail!!!!!!!");
			e.printStackTrace();
		}
	}

}
