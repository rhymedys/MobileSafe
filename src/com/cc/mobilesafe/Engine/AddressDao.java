package com.cc.mobilesafe.Engine;

import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.Utils.LogUtils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class AddressDao {
	private static final String TAG = "ADDRESSDAO";
	public static String path = "data/data/com.cc.mobilesafe/files/address.db";

	/**
	 * 查询电话号码
	 * 
	 * @param phone
	 */
	public static String getAddress(String phone) {
		String regularExpression = "^1[3-8]\\d{9}";
		boolean matches = phone.matches(regularExpression);
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		String strLocation = "未知号码";
		if (matches) {
			Cursor queryOutKey = db.query("data1", new String[] { "outkey" }, "id=?",
					new String[] { phone.substring(0, 7) }, null, null, null);
			if (queryOutKey.moveToNext()) {
				String strOutKey = queryOutKey.getString(queryOutKey.getColumnIndex("outkey"));
				Cursor queryLocation = db.query("data2", new String[] { "location" }, "id=?",
						new String[] { strOutKey }, null, null, null);
				if (queryLocation.moveToNext()) {

					strLocation = queryLocation.getString(queryLocation.getColumnIndex("location"));
					LogUtils.i(TAG, strLocation);
				}
			}
		} else {
			Cursor queryLocation;
			switch (phone.length()) {
			case 3:
				strLocation = "此电话号码为特殊行为电话号码";
				break;
			case 5:

				strLocation = "服务电话号码";

				break;
			case 7:

				strLocation = "本地号码";
				break;
			case 11:

				queryLocation = db.query("data2", new String[] { "location" }, "area=?",
						new String[] { phone.substring(1, 3) }, null, null, null);
				if (queryLocation.moveToNext()) {
					strLocation = queryLocation.getString(queryLocation.getColumnIndex("location"));
				}
				break;
			case 12:
				queryLocation = db.query("data2", new String[] { "location" }, "area=?",
						new String[] { phone.substring(1, 4) }, null, null, null);
				if (queryLocation.moveToNext()) {
					strLocation = queryLocation.getString(queryLocation.getColumnIndex("location"));
				}
				break;
			default:
				break;
			}

		}
		LogUtils.i(TAG, strLocation);
		return strLocation;
	}

}
