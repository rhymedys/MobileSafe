package com.cc.mobilesafe.Activity;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.R.menu;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class RocketBackgroudActivity extends Activity {

	private Context context;
	private ImageView iv_desktop_smoke_m;
	private ImageView iv_desktop_smoke_t;
	
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO 自动生成的方法存根
			finish();
			super.handleMessage(msg);
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rocket_backgroud);
		this.context = this;
		
		iv_desktop_smoke_t = (ImageView) findViewById(R.id.iv_desktop_smoke_t);
		iv_desktop_smoke_m = (ImageView) findViewById(R.id.iv_desktop_smoke_m);
		
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(500);
		iv_desktop_smoke_m.startAnimation(alphaAnimation);
		iv_desktop_smoke_t.startAnimation(alphaAnimation);
		
		handler.sendEmptyMessageDelayed(0, 1000);
	}

}
