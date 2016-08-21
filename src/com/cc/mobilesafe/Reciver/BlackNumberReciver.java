package com.cc.mobilesafe.Reciver;

import com.cc.mobilesafe.db.Dao.BlackNumberDao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class BlackNumberReciver extends BroadcastReceiver {

	private static final int BLOCK_SMS = 0;
	private static final int BLOCK_PHONE = 1;
	private static final int BLOCK_ALL = 2;
	private BlackNumberDao mDao;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自动生成的方法存根
		mDao = BlackNumberDao.getInstance(context);
		Object[] objects = (Object[]) intent.getExtras().get("pdus");
		if (objects.length > 0) {
			for (Object object : objects) {
				SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);
				String originatingAddress = sms.getOriginatingAddress();
				int queryMode = mDao.queryMode(originatingAddress);
				if (queryMode==BLOCK_SMS&&queryMode==BLOCK_ALL) {
					//中断广播
					abortBroadcast();
				}
			}
		}

	}

}
