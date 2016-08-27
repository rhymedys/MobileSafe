package com.cc.mobilesafe.Engine;

import java.util.ArrayList;

import com.cc.mobilesafe.Bean.BlackNumberInfoBean;
import com.cc.mobilesafe.db.BlackNumberOpenHelper;

import android.R.integer;
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
	 *            拦截类型（0.短信 1.电话2.拦截所有）
	 */
	public void insert(String phone, int mode) {
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
	public void update(String phone, int mode) {
		SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", mode);
		db.update(DB_NAME, values, "phone=?", new String[] { phone });
		db.close();
	}

	/**
	 * 查询全部 ArrayList<BlackNumberInfoBean> 类型
	 */
	public ArrayList<BlackNumberInfoBean> queryAll() {
		SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
		Cursor query = db.query(DB_NAME, new String[] { "phone", "mode" }, null, null, null, null, "_id desc");
		ArrayList<BlackNumberInfoBean> list = new ArrayList<BlackNumberInfoBean>();
		while (query.moveToNext()) {
			BlackNumberInfoBean bean = new BlackNumberInfoBean();
			bean.phone = query.getString(query.getColumnIndex("phone"));
			bean.mode = query.getInt(query.getColumnIndex("mode"));
			list.add(bean);
		}
		query.close();
		db.close();
		return list;
	}

	/**
	 * 查询指定位置开始的20条数据
	 * 
	 * @param index
	 *            开始查询的索引
	 * @return
	 */
	public ArrayList<BlackNumberInfoBean> query(int index) {

		SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
		ArrayList<BlackNumberInfoBean> list = new ArrayList<BlackNumberInfoBean>();
		Cursor rawQuery = db.rawQuery("select * from blacknumber order by _id desc limit ?,20",
				new String[] { String.valueOf(index) });
		while (rawQuery.moveToNext()) {
			BlackNumberInfoBean bean = new BlackNumberInfoBean();
			bean.phone = rawQuery.getString(rawQuery.getColumnIndex("phone"));
			bean.mode = rawQuery.getInt(rawQuery.getColumnIndex("mode"));
			list.add(bean);

		}
		db.close();
		return list;
	}

	/**
	 * 获取数据库里面数据数量
	 * @return  数量
	 */
	public int getCount() {

		int count = 0;
		SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
		ArrayList<BlackNumberInfoBean> list = new ArrayList<BlackNumberInfoBean>();
		Cursor rawQuery = db.rawQuery("select count( *)  from blacknumber", null);
		if (rawQuery.moveToNext()) {
			count = rawQuery.getInt(0);
		}
		rawQuery.close();
		db.close();
		return count;

	}
	
	
	/**
	 * 根据号码查询 mode 的值
	 * @param phone
	 * @return mode类型 0sms 1phone 2all   返回-1代表没有结果
	 */
	public int queryMode(String phone){
		int result=-1;
		SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
		Cursor query = db.query(DB_NAME, new String[]{"mode"}, "phone=?", new String[]{phone}, null, null, null);
		if (query.moveToNext()) {
			result=query.getInt(query.getColumnIndex("mode"));
		}
		query.close();
		db.close();
		return result;
		
	}

}
