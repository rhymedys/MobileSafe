package com.cc.mobilesafe;

import com.cc.mobilesafe.adapter.HomeGridViewAdapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

public class HomeActivity extends Activity {

	private Context context;
	private GridView gv_HomeView;
	private String[] titleStrs;
	private int [] picInts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		this.context = this;

		initUI();
		initData();

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		titleStrs = new String[]{"手机防盗","通信卫士","软件管理","进程管理","流量统计","手机杀毒","缓存清理","高级工具","设置中心"};
		picInts = new int[]{R.drawable.home_safe,R.drawable.home_callmsgsafe,R.drawable.home_apps,
				R.drawable.home_taskmanager,R.drawable.home_netmanager,R.drawable.home_trojan,
				R.drawable.home_sysoptimize,R.drawable.home_tools,R.drawable.home_settings
				
		};
		gv_HomeView.setAdapter(new HomeGridViewAdapter(context,titleStrs,picInts));
	}

	/**
	 * 初始化HomeUI
	 */
	private void initUI() {
		// TODO 自动生成的方法存根
		gv_HomeView = (GridView) findViewById(R.id.gv_HomeView);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
