package com.cc.mobilesafe.db.Dao;

import java.util.ArrayList;

import com.cc.mobilesafe.Bean.BlackNumberInfoBean;
import com.cc.mobilesafe.db.BlackNumberOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumberDao {
	private static final String DB_NAME = "blacknumber";
	// 单例画BlackNumberDao

	private Context context;
	private SQLiteOpenHelper blackNumberOpenHelper;

	private BlackNumberDao(Context context) {
		this.context = context;
		blackNumberOpenHelper = new BlackNumberOpenHelper(context);
	}

	private static BlackNumberDao blackNumberDao = null;

	public static BlackNumberDao getInstance(Context context) {
		if (blackNumberDao == null) {
			blackNumberDao = new BlackNumberDao(context);
		}
		return blackNumberDao;
	}

	/**
	 *
	 * @param phone
	 *            拦截的电话号码
	 * @param mode
	 *            拦截类型（1.短信 2.电话3.拦截所有）
	 */
	public void insert(String phone, String mode) {
		SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("phone", phone);
		values.put("mode", mode);
		db.insert(DB_NAME, null, values);
		db.close();

	}

	/**
	 * 
	 * @param phone
	 *            根据phone 删除数据
	 */
	public void delete(String phone) {
		SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
		db.delete(DB_NAME, "phone=?", new String[] { phone });
		db.close();
	}

	/**
	 * @param phone
	 *            更新拦截模式的电话号码
	 * @param mode
	 *            要更新为的模式
	 */
	public void update(String phone, String mode) {
		SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", mode);
		db.update(DB_NAME, values, "phone=?", new String[] { phone });
		db.close();
	}
	
	
	/**
	 * 查询全部
	 *  ArrayList<BlackNumberInfoBean> 类型
	 */
	public ArrayList<BlackNumberInfoBean> queryAll()
	{
		SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
		Cursor query = db.query(DB_NAME, new String[]{"phone","mode"},null, null, null, null, "_id desc");
		ArrayList<BlackNumberInfoBean> list = new ArrayList<BlackNumberInfoBean>();
		while(query.moveToNext()){
			BlackNumberInfoBean bean = new BlackNumberInfoBean();
			bean.phone=query.getString(query.getColumnIndex("phone"));
			bean.mode=query.getString(query.getColumnIndex("mode"));
			list.add(bean);
		}
		query.close();
		db.close();
		return list;
	}

}
