package com.cc.mobilesafe.Engine;

import java.util.ArrayList;
import java.util.List;

import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.Utils.LogUtils;

import android.app.DownloadManager.Request;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class VirusDao {
	private static final String TAG = "VirusDao";
	private static final String TABLE = "datable";
	public static String path = "data/data/com.cc.mobilesafe/files/antivirus.db";
	private static SQLiteDatabase openDatabase;
	private static Cursor query;
	private static List<String> list;

	/**
	 * 获取病毒库中的所有恶意软件的 md5值，返回一个list
	 * 
	 * @return
	 */
	public static List<String> getList() {
		list = new ArrayList<String>();
		try {
			openDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
			query = openDatabase.query(TABLE, new String[] { "md5" }, null, null, null, null, null);
			while (query.moveToNext()) {
				list.add(query.getString(0));
				Log.i(TAG, query.getString(0));
			}
		} catch (SQLiteException e) {
			// TODO: handle exception
			LogUtils.i(TAG, "openDatabase fail!");
		} catch (Exception e) {
			// TODO: handle exception
			LogUtils.i(TAG, "getlist fail!");
		} finally {
			if (query != null) {
				query.close();
			}
			if (openDatabase != null) {
				openDatabase.close();
			}
		}

		// LogUtils.i(TAG, list.size()+ "");
		return list;
	}

}
