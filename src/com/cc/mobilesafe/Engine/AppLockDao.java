package com.cc.mobilesafe.Engine;

import java.util.ArrayList;
import java.util.List;

import com.cc.mobilesafe.Utils.LogUtils;
import com.cc.mobilesafe.db.AppLockOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class AppLockDao {
	/**
	 * 数据库表名称呢个
	 */
	private static final String DB_NAME = "applock";

	private static final String TAG = "AppLockDao";

	private static final String DB_CHANGE = "content://applock/change";

	private Context context;
	private SQLiteOpenHelper appLockOpenHelper;

	private AppLockDao(Context context) {
		this.context = context;
		appLockOpenHelper = new AppLockOpenHelper(context);
	}

	private static AppLockDao appLockDao = null;

	public static AppLockDao getInstance(Context context) {
		if (appLockDao == null) {
			appLockDao = new AppLockDao(context);
		}
		return appLockDao;
	}

	/**
	 * 增加指定报名的数据
	 * 
	 * @param packagename
	 */
	public void insert(String packagename) {
		try {
			SQLiteDatabase db = appLockOpenHelper.getReadableDatabase();
			ContentValues values = new ContentValues();
			values.put("packagename", packagename);
			long insert = db.insert(DB_NAME, null, values);
			LogUtils.i(TAG, insert + "insert");
			db.close();

			context.getContentResolver().notifyChange(Uri.parse(DB_CHANGE), null);
		} catch (Exception e) {

			LogUtils.i(TAG, "insert fail");
			e.printStackTrace();
		}
	}

	/**
	 * 删除 指定报名的数据
	 * 
	 * @param packagename
	 */
	public void delete(String packagename) {
		try {
			SQLiteDatabase db = appLockOpenHelper.getReadableDatabase();
			int delete = db.delete(DB_NAME, "packagename=?", new String[] { packagename });
			LogUtils.i(TAG, delete + "delete");
			db.close();

			context.getContentResolver().notifyChange(Uri.parse(DB_CHANGE), null);
		} catch (Exception e) {

			LogUtils.i(TAG, "delete fail");
			e.printStackTrace();
		}
	}

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	public List<String> queryAll() {
		List<String> list = new ArrayList<String>();
		try {
			SQLiteDatabase db = appLockOpenHelper.getReadableDatabase();
			Cursor query = db.query(DB_NAME, null, null, null, null, null, null);
			while (query.moveToNext()) {

				list.add(query.getString(query.getColumnIndex("packagename")));
			}
			LogUtils.i(TAG, query.getCount() + "queryAll");
			query.close();
			db.close();
		} catch (Exception e) {

			LogUtils.i(TAG, "queryAll fail");
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 查询指定报名的应用
	 * 
	 * @param packageName
	 * @return
	 */
	public List<String> query(String packageName) {
		List<String> list = new ArrayList<String>();
		try {
			SQLiteDatabase db = appLockOpenHelper.getReadableDatabase();
			Cursor query = db.query(DB_NAME, null, "packagename=?", new String[] { packageName }, null, null, null);
			while (query.moveToNext()) {
				;
				list.add(query.getString(query.getColumnIndex("packagename")));
			}
			query.close();
			db.close();
		} catch (Exception e) {

			LogUtils.i(TAG, "queryAll fail");
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 删除所有
	 */
	public void deleteAll() {
		try {
			SQLiteDatabase db = appLockOpenHelper.getReadableDatabase();
			db.delete(DB_NAME, null, null);
			db.close();

			context.getContentResolver().notifyChange(Uri.parse(DB_CHANGE), null);
		} catch (Exception e) {

			LogUtils.i(TAG, "deleteAll fail");
			e.printStackTrace();
		}
	}

	/**
	 * 查询所有条目
	 * 
	 * @return
	 */
	public int allSize() {
		return queryAll().size();
	}

}
