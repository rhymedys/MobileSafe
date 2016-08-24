package com.cc.mobilesafe.Engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.cc.mobilesafe.Interface.SmsBackupProgressInterface;
import com.cc.mobilesafe.Utils.LogUtils;
import com.cc.mobilesafe.Utils.ToastUtil;

import android.R.integer;
import android.R.xml;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;
import android.widget.ProgressBar;

public class SmsBackup {
	private static final Uri SMSURI = Uri.parse("content://sms/");
	private static final String TAG = "SmsBackup";
	private static final int EMPTY_SMS = 100;
	private static ContentResolver contentResolver;
	private static int index = 0;

	private static Cursor query;

	public static void backup(Context context, String saveSmsPath, SmsBackupProgressInterface callBack, Handler handler) {
		LogUtils.i(TAG, SMSURI + "");
		FileOutputStream fileOutputStream = null;
		try {
			contentResolver = context.getContentResolver();
			query = contentResolver.query(SMSURI, new String[] { "address", "date", "type", "body" }, null, null, null);
			if (query.getCount() == 0 || callBack == null) {
				handler.sendEmptyMessage(EMPTY_SMS);
				return;
			}

			File file = new File(saveSmsPath);
			fileOutputStream = new FileOutputStream(file);

			XmlSerializer newSerializer = Xml.newSerializer();
			newSerializer.setOutput(fileOutputStream, "utf-8");
			newSerializer.startDocument("utf-8", true);
			newSerializer.startTag(null, "smss");

//			progressDialog.setMax(query.getCount());
			callBack.setMax(query.getCount());

			while (query.moveToNext()) {
				newSerializer.startTag(null, "sms");

				newSerializer.startTag(null, "address");
				newSerializer.text(query.getString(query.getColumnIndex("address")));
				newSerializer.endTag(null, "address");

				newSerializer.startTag(null, "date");
				newSerializer.text(query.getString(query.getColumnIndex("date")));
				newSerializer.endTag(null, "date");

				newSerializer.startTag(null, "type");
				newSerializer.text(query.getString(query.getColumnIndex("type")));
				newSerializer.endTag(null, "type");

				newSerializer.startTag(null, "body");
				newSerializer.text(query.getString(query.getColumnIndex("body")));
				newSerializer.endTag(null, "body");

				newSerializer.endTag(null, "sms");

				index++;
				// 能够让用户看到 变化过程
				Thread.sleep(500);
				LogUtils.i(TAG, index + "");

//				progressDialog.setProgress(index);
				callBack.setProgress(index);
			}
			newSerializer.endTag(null, "smss");
			newSerializer.endDocument();

		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.i(TAG, "file创建错误");
		} finally {
			try {
				if (query != null) {
					query.close();
				}
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (IOException e) {
				LogUtils.i(TAG, "关闭流错误");
				e.printStackTrace();
			}
		}

	}
	
	
}
