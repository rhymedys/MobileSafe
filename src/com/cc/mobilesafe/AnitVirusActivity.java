package com.cc.mobilesafe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.cc.mobilesafe.Bean.VirusBean;
import com.cc.mobilesafe.Engine.VirusDao;
import com.cc.mobilesafe.Utils.LogUtils;
import com.cc.mobilesafe.Utils.Md5Util;
import com.cc.mobilesafe.Utils.ToastUtil;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AnitVirusActivity extends Activity {

	private static final String TAG = "AnitVirusActivity";
	protected static final int UPDATE = 0;
	protected static final int SCANING = 100;
	protected static final int FINISH_SCANING = 101;
	private Context context;
	private ImageView iv_scan;
	private TextView tv_state;
	private ProgressBar pb_progress;
	private LinearLayout ll_add_text;
	private Animation scanAnimation;
	private PackageManager packageManager;
	private List<PackageInfo> installedPackagesSignature = null;
	private Signature[] signatures;
	private List<String> virusList = null;
	private List<VirusBean> virusScanInfoList = null;
	private List<VirusBean> allnfoList = null;
	private VirusBean virusBean = null;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SCANING:
				virusBean = (VirusBean) msg.obj;
				tv_state.setText("正在扫描" + virusBean.name);

				TextView textView = new TextView(context);
				textView.setSingleLine(true);
				if (virusBean.isVirus) {
					textView.setTextColor(Color.RED);
					textView.setText("发现病毒：" + virusBean.name);
				} else {
					textView.setTextColor(Color.BLACK);
					textView.setText("扫描安全：" + virusBean.name);
				}

				ll_add_text.addView(textView, 0);

				break;
			case FINISH_SCANING:
				iv_scan.clearAnimation();
				tv_state.setText("扫描完成");

				if (virusScanInfoList.size() > 0) {
					unInstallVirus();
				}

				break;

			default:
				break;
			}
			super.handleMessage(msg);

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anit_virus);
		this.context = this;

		initUI();
		initAnimation();
		checkVirus();

	}

	private void checkVirus() {

		new Thread(new Runnable() {
			public void run() {
				virusScanInfoList = new ArrayList<VirusBean>();
				allnfoList = new ArrayList<VirusBean>();
				virusList = VirusDao.getList();
				if (virusList.size() <= 0) {
					ToastUtil.show(context, "病毒特征库错误");
					return;
				}

				packageManager = getPackageManager();

				installedPackagesSignature = packageManager
						.getInstalledPackages(PackageManager.GET_SIGNATURES + PackageManager.GET_UNINSTALLED_PACKAGES);
				VirusBean bean = null;

				pb_progress.setMax(installedPackagesSignature.size());
				int index = 0;
				for (PackageInfo applicationInfo : installedPackagesSignature) {
					bean = new VirusBean();

					signatures = applicationInfo.signatures;
					Signature signature = signatures[0];
					LogUtils.i(TAG, signature + "");
					String strSignature = String.valueOf(signature);
					LogUtils.i(TAG, strSignature);
					strSignature = Md5Util.encoder(strSignature);
					bean.packageName = applicationInfo.packageName;

					String tempName = applicationInfo.applicationInfo.loadLabel(packageManager).toString();
					if (TextUtils.isEmpty(tempName)) {
						bean.name = applicationInfo.packageName;
					} else {
						bean.name = tempName;
					}

					if (virusList.contains(strSignature)) {
						bean.isVirus = true;
						virusScanInfoList.add(bean);
					} else {
						bean.isVirus = false;
					}

					allnfoList.add(bean);
					index++;
					pb_progress.setProgress(index);

					try {
						Thread.sleep(50 + new Random().nextInt(100));
					} catch (InterruptedException e) {
						LogUtils.i(TAG, "sleep FAIL!!!");
						e.printStackTrace();
					}

					Message msg = Message.obtain();
					msg.what = SCANING;
					msg.obj = bean;
					handler.sendMessage(msg);
				}

				handler.sendEmptyMessage(FINISH_SCANING);

			}
		}).start();
	}

	private void initAnimation() {
		scanAnimation = AnimationUtils.loadAnimation(context, R.anim.anitvirus_rotate);
		iv_scan.startAnimation(scanAnimation);
	}

	private void initUI() {
		iv_scan = (ImageView) findViewById(R.id.iv_scan);
		tv_state = (TextView) findViewById(R.id.tv_state);
		pb_progress = (ProgressBar) findViewById(R.id.pb_progress);
		ll_add_text = (LinearLayout) findViewById(R.id.ll_add_text);
	}

	private void unInstallVirus() {

		for (VirusBean tempBean : virusScanInfoList) {
			String packageName = tempBean.packageName;
			try {
				Intent intentUninstall = new Intent();
				intentUninstall.addCategory("android.intent.category.DEFAULT");
				intentUninstall.setAction("android.intent.action.DELETE");
				intentUninstall.setData(Uri.parse("package:" + packageName));
				startActivity(intentUninstall);
			} catch (Exception e) {
				// TODO: handle exception
				ToastUtil.show(context, tempBean.name + "卸载失败");
			}
		}
	}

}
