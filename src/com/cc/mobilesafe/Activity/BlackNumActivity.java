package com.cc.mobilesafe.Activity;

import java.util.ArrayList;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.Adapter.BlackNumAdapter;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.Bean.BlackNumberInfoBean;
import com.cc.mobilesafe.db.Dao.BlackNumberDao;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

public class BlackNumActivity extends Activity {

	private Context context;
	private ListView lv_blacknumber;
	private Button btn_add;
	private BlackNumberDao blackNumberDao;
	private ArrayList<BlackNumberInfoBean> listAll;
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO 自动生成的方法存根
			lv_blacknumber.setAdapter(new BlackNumAdapter(context,listAll));
			super.handleMessage(msg);
		}
		
	};

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_black_num);
		this.context = this;

		initUI();
		initData();
	}

	private void initData() {
		// TODO 自动生成的方法存根
		new Thread(new Runnable() {

			public void run() {
				blackNumberDao = BlackNumberDao.getInstance(context);
				listAll = blackNumberDao.queryAll();
				handler.sendEmptyMessage(0);
			}
		}).start();

	}

	private void initUI() {
		lv_blacknumber = (ListView) findViewById(R.id.lv_blacknumber);
		btn_add = (Button) findViewById(R.id.btn_add);
	}

}
