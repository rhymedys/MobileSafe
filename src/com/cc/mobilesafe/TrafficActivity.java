package com.cc.mobilesafe;

import android.app.Activity;
import android.content.Context;
import android.net.TrafficStats;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class TrafficActivity extends Activity {

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traffic);
		this.context = this;

		// 接受 2 3 4 g download流量
		long mobileRxBytes = TrafficStats.getMobileRxBytes();

		// 2 3 4 g total 流量
		long mobileTxBytes = TrafficStats.getMobileTxBytes();

		// download流量总和
		long totalRxBytes = TrafficStats.getTotalRxBytes();

		// 总流量
		long totalTxBytes = TrafficStats.getTotalTxBytes();
		
		
	}
}
