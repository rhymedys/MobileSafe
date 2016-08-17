package com.cc.mobilesafe.Reciver;

import com.cc.mobilesafe.utils.ConstantValue;
import com.cc.mobilesafe.utils.SpUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class BootReciver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自动生成的方法存根
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		String simSerialNumber = telephonyManager.getSimSerialNumber();
		String simnum = SpUtils.getString(context, ConstantValue.SIMNUM, "");
		if (!simnum.equals(simSerialNumber)) {
			SmsManager smsManager = SmsManager.getDefault();
			String text="sim change!!";	
			String safe_contact_num = SpUtils.getString(context, ConstantValue.SAFE_CONTACT_NUM, "");
			if(!TextUtils.isEmpty(safe_contact_num)){
				smsManager.sendTextMessage(safe_contact_num, null, text, null, null);
			}
				
			
		}
	}

}
