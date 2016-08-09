package com.cc.mobilesafe.activity;

import java.lang.annotation.Annotation;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.drawable;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.R.menu;
import com.cc.mobilesafe.adapter.HomeGridViewAdapter;
import com.lidroid.xutils.view.annotation.event.OnChildClick;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class HomeActivity extends Activity implements OnItemClickListener {

	private Context context;
	private GridView gv_HomeView;
	private String[] titleStrs;
	private int [] picInts;
	private FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		this.context = this;

		initUI();
		initData();
		gv_HomeView.setOnItemClickListener(this);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO 自动生成的方法存根

		switch (position) {
		case 0:
			
			break;
		case 1:
			
			break;
		case 2:
			
			break;
		case 3:
			
			break;
		case 4:
			
			break;
		case 5:
			
			break;
		case 6:
			
			break;
		case 7:
			
			break;
		case 8:
			startActivity(new Intent(context, SettingActivity.class));
			break;
			

		default:
			break;
		}

	}
}
