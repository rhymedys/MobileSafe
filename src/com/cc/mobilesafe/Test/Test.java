package com.cc.mobilesafe.Test;

import java.util.Random;

import com.cc.mobilesafe.db.Dao.BlackNumberDao;

import android.test.AndroidTestCase;

/**
 * @author Rhymedys
 *此类为junit测试
 */
public class Test extends AndroidTestCase {
	public void insert(){
		BlackNumberDao instance = BlackNumberDao.getInstance(getContext());
//		instance.insert("100", "1");
//		instance.update("100", "2");
//		instance.delete("100");
//		instance.queryAll();
//		for(int i=0;i<100;i++){	
//				instance.insert("183000"+i, new Random().nextInt(3));		
//			
//		}
		instance.query(0);
	}
}
