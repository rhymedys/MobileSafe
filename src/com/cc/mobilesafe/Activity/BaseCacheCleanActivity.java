package com.cc.mobilesafe.Activity;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.R.menu;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost.TabSpec;

public class BaseCacheCleanActivity extends TabActivity {

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_cache_clean);
		this.context = this;

		TabSpec tab1 = getTabHost().newTabSpec("clear_cache").setIndicator("缓存清理");
		TabSpec tab2 = getTabHost().newTabSpec("sd_cache_clear").setIndicator("sd卡清理");

		tab1.setContent(new Intent(context, ClearCacheActivity.class));
		tab2.setContent(new Intent(context, ClearSDCacheActivity.class));

		getTabHost().addTab(tab1);
		getTabHost().addTab(tab2);

	}

}
