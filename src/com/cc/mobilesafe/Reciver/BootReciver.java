package com.cc.mobilesafe.Reciver;

import com.cc.mobilesafe.Utils.ConstantValue;
import com.cc.mobilesafe.Utils.SpUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class BootReciver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自动生成的方法存根

		if (SpUtils.getBoolean(context, ConstantValue.OPEN_SAFE_SECURITY, false)) {
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);

			String simSerialNumber = telephonyManager.getSimSerialNumber();
			// SIM卡 发生改变时发生短信
			String simnum = SpUtils.getString(context, ConstantValue.SIMNUM, "");
			if (!simnum.equals(simSerialNumber)) {
				SmsManager smsManager = SmsManager.getDefault();
				String text = "sim change!!";
				String safe_contact_num = SpUtils.getString(context, ConstantValue.SAFE_CONTACT_NUM, "");
				if (!TextUtils.isEmpty(safe_contact_num)) {
					smsManager.sendTextMessage(safe_contact_num, null, text, null, null);
				}

			}

		}

	}

}
