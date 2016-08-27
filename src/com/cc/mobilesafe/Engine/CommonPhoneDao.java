package com.cc.mobilesafe.Engine;

import com.cc.mobilesafe.R.id;

import java.util.ArrayList;
import java.util.List;

import com.cc.mobilesafe.Bean.CommonPhoneGroupBean;
import com.cc.mobilesafe.Bean.CommonPhoneChildBean;
import com.cc.mobilesafe.Bean.CommonPhoneGroupCombineChild;
import com.cc.mobilesafe.Utils.LogUtils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class CommonPhoneDao {
	private static final String TAG = "CommonPhoneDao !!!!!!!";
	public static String path = "data/data/com.cc.mobilesafe/files/commonnum.db";
	private SQLiteDatabase openDatabase;

	/**
	 * 查询分类的组 返回所有分类集合
	 * 
	 * @return
	 */
	public List<CommonPhoneGroupBean> getGroup() {
		List<CommonPhoneGroupBean> arrayList = new ArrayList<CommonPhoneGroupBean>();
		openDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		Cursor query = openDatabase.query("classlist", new String[] { "name", "idx" }, null, null, null, null, null);
		CommonPhoneGroupBean bean = null;
		while (query.moveToNext()) {
			bean = new CommonPhoneGroupBean();
			bean.name = query.getString(query.getColumnIndex("name"));
			bean.idx = query.getInt(query.getColumnIndex("idx"));
			arrayList.add(bean);
		}
		LogUtils.i(TAG, query.getCount()+"");
		query.close();
		openDatabase.close();
		return arrayList;
	}

	/**
	 * 查询每一个分类中明细内容
	 * 
	 * @param index
	 *            传入一个组索引
	 */
	public List<CommonPhoneChildBean> getGroupChind(int index) {
		List<CommonPhoneChildBean> arrayList = new ArrayList<CommonPhoneChildBean>();
		openDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		Cursor query = openDatabase.query("table" + index, new String[] { "_id", "number", "name" }, null, null, null,
				null, null);
		CommonPhoneChildBean bean = null;
		while (query.moveToNext()) {
			bean = new CommonPhoneChildBean();
			bean._id = query.getInt(query.getColumnIndex("_id"));
			bean.name = query.getString(query.getColumnIndex("name"));
			bean.number = query.getString(query.getColumnIndex("number"));
			arrayList.add(bean);
		}
		LogUtils.i(TAG, query.getCount()+"");
		query.close();
		openDatabase.close();

		return arrayList;
	}

	/**
	 * 同时获取组和孩子一起的集合
	 * 
	 * @return
	 */
	public List<CommonPhoneGroupCombineChild> getAll() {
		List<CommonPhoneGroupCombineChild> arrayList = new ArrayList<CommonPhoneGroupCombineChild>();
		openDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
		Cursor query = openDatabase.query("classlist", new String[] { "name", "idx" }, null, null, null, null, null);
		CommonPhoneGroupCombineChild bean = null;
		while (query.moveToNext()) {
			bean = new CommonPhoneGroupCombineChild();
			bean.name = query.getString(query.getColumnIndex("name"));
			int idx = query.getInt(query.getColumnIndex("idx"));
			bean.idx = idx;
			bean.child = getGroupChind(idx);
			arrayList.add(bean);
		}
		LogUtils.i(TAG, query.getCount()+"");
		query.close();
		openDatabase.close();
		return arrayList;
	}

}
