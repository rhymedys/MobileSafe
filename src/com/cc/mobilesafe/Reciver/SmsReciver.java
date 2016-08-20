package com.cc.mobilesafe.Reciver;

import java.io.IOException;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.Service.LocationService;
import com.cc.mobilesafe.utils.ConstantValue;
import com.cc.mobilesafe.utils.Md5Util;
import com.cc.mobilesafe.utils.SpUtils;
import com.cc.mobilesafe.utils.ToastUtil;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

/**
 * @author Rhymedys 接收监控短信的reciver
 */
public class SmsReciver extends BroadcastReceiver {

	private ComponentName mDeviceAdminComponent;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自动生成的方法存根
		boolean open_safe_security = SpUtils.getBoolean(context, ConstantValue.OPEN_SAFE_SECURITY, false);
		if (open_safe_security) {
			DevicePolicyManager mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
			Object[] objects = (Object[]) intent.getExtras().get("pdus");
			for (Object object : objects) {
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);
				String originatingAddress = sms.getOriginatingAddress();
				String message = sms.getMessageBody();
				if (message.contentEquals("#*alarm*#")) {
					MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
					try {
						mediaPlayer.prepare();
						mediaPlayer.setLooping(true);
						mediaPlayer.start();
					} catch (Exception e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
				if (message.contentEquals("#*location*#")) {
					Intent service = new Intent(context, LocationService.class);
					context.startService(service);
				}
				if (message.contentEquals("#*wipedata*#")) {

					// 激活设备管理器
					MyDeviceAdmin(context, DeviceAdminReciver.class);
					if (mDPM.isAdminActive(mDeviceAdminComponent)) {
						mDPM.wipeData(0);
					} else {
						ToastUtil.show(context, "请先激活设备管理器");
					}
				}

				if (message.contentEquals("#*lockscreen*#")) {
					// 激活设备管理器
					MyDeviceAdmin(context, DeviceAdminReciver.class);
					if (mDPM.isAdminActive(mDeviceAdminComponent)) {
						mDPM.lockNow();
						mDPM.resetPassword("0000", 0);
					} else {
						ToastUtil.show(context, "请先激活设备管理器");
					}
				}

			}
		}

	}

	/**
	 * 激活设备管理器
	 * @param context 上下文
	 * @param cls  一个继承DeviceAdminReceiver的类
	 */
	public void MyDeviceAdmin(Context context, Class<?> cls) {
		mDeviceAdminComponent = new ComponentName(context, cls);
		
		//代码激活设备管理器
		Intent intentDevice = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intentDevice.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminComponent);
		intentDevice.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "设备管理器");
		context.startActivity(intentDevice);
	}

}
