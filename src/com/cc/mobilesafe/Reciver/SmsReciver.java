package com.cc.mobilesafe.Reciver;

import java.io.IOException;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.utils.ConstantValue;
import com.cc.mobilesafe.utils.SpUtils;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

/**
 * @author Rhymedys
 *接收监控短信的reciver
 */
public class SmsReciver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自动生成的方法存根
		boolean open_safe_security = SpUtils.getBoolean(context, ConstantValue.OPEN_SAFE_SECURITY, false);
		if (open_safe_security) {
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
				
				
			}
		}

	}

}
