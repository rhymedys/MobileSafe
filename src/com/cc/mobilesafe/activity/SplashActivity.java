package com.cc.mobilesafe.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.Utils.ConstantValue;
import com.cc.mobilesafe.Utils.LogUtils;
import com.cc.mobilesafe.Utils.SpUtils;
import com.cc.mobilesafe.Utils.StreamUtil;
import com.cc.mobilesafe.Utils.ToastUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Rhymedys
 *
 */
/**
 * @author Rhymedys
 *
 */
public class SplashActivity extends Activity {

	protected static final String TAG = "SplashActivity";
	/**
	 * 各类状态码
	 */
	protected static final int UPDATE_VERSION = 100;
	protected static final int ENTER_HOME = 101;
	protected static final int URL_ERROR = 102;
	protected static final int IO_ERROR = 103;
	protected static final int JSON_ERROR = 104;

	private Context context;
	private TextView tv_version_name;
	private String versionName;
	private int mLocalVersionCode;
	private String mDownloadUrl;
	private String versionDes;
	private RelativeLayout rl_root;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case UPDATE_VERSION:
				showUpdataDialog(msg);
				break;
			case ENTER_HOME:
				enterHome();
				break;
			case URL_ERROR:
				ToastUtil.show(context, "url 异常");
				enterHome();
				break;
			case IO_ERROR:
				ToastUtil.show(context, "读取 异常");
				enterHome();
				break;
			case JSON_ERROR:
				ToastUtil.show(context, "json 解析异常");
				enterHome();
				break;

			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		this.context = this;

		initUI();
		initData();
		initAnimation();
		initDB();

	}

	/**
	 * 初始化数据库
	 */
	private void initDB() {
		// TODO 自动生成的方法存根
		initAddressDB("address.db");
	}

	/**
	 * 初始化 address 数据库
	 * 
	 * @param dbName
	 */
	private void initAddressDB(String dbName) {
		// TODO 自动生成的方法存根
		File filesDir = getFilesDir();
		File file = new File(filesDir, dbName);

		if (file.exists()) {
			return;
		} else {
			InputStream inputStream = null;
			FileOutputStream fileOutputStream = null;
			try {

				inputStream = getAssets().open(dbName);
				fileOutputStream = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = -1;
				while ((len = inputStream.read(buffer)) != -1) {
					fileOutputStream.write(buffer, 0, len);
					fileOutputStream.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fileOutputStream != null && inputStream != null) {
					try {
						fileOutputStream.close();
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

			}
		}
	}

	/**
	 * 初始化淡入动画
	 */
	private void initAnimation() {

		Animation animation = AnimationUtils.loadAnimation(context, R.anim.splash_alpha_animation);

		rl_root = (RelativeLayout) findViewById(R.id.rl_root);
		rl_root.startAnimation(animation);

	}

	/**
	 * 弹出更新对话框
	 */
	protected void showUpdataDialog(Message msg) {

		Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("更新提示");
		builder.setIcon(R.drawable.ic_launcher);

		Bundle data = msg.getData();
		versionDes = data.getString("versionDes");
		mDownloadUrl = data.getString("downloadUrl");

		builder.setMessage(versionDes);
		builder.setPositiveButton("立马下载", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				downloadAPK();

				dialog.dismiss();
			}
		});

		builder.setNegativeButton("稍后更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				enterHome();
				dialog.dismiss();
			}
		});

		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {

				enterHome();
				dialog.dismiss();
			}
		});

		builder.show();
	}

	protected void downloadAPK() {
		//
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
					+ "mobilesafe.apk";
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {

				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					//
					File file = arg0.result;
					Toast.makeText(context, "下载成功", Toast.LENGTH_SHORT).show();
					installAPK(file);

					LogUtils.i(TAG, "onSuccess");
				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					//
					Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();

					LogUtils.i(TAG, "onFailure");
				}

				@Override
				public void onStart() {
					//
					Toast.makeText(context, "开始下载...", Toast.LENGTH_SHORT).show();
					super.onStart();

					LogUtils.i(TAG, "onStart");
				}

				@Override
				public void onLoading(long total, long current, boolean isUploading) {
					//
					super.onLoading(total, current, isUploading);

					LogUtils.i(TAG, String.valueOf(total));
					LogUtils.i(TAG, String.valueOf(current));
				}
			});
		}
	}

	/**
	 * 安装APK
	 */
	protected void installAPK(File file) {
		//
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		startActivityForResult(intent, 0);
	}

	/**
	 * 进入主页
	 */
	protected void enterHome() {

		startActivity(new Intent(context, HomeActivity.class));
		finish();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		tv_version_name.setText(getVersionName());
		mLocalVersionCode = getVersionCode();
		if (SpUtils.getBoolean(context, ConstantValue.OPENUPDATE, false)) {
			checkVersion();
		} else {
			Message msg = Message.obtain();
			msg.what = ENTER_HOME;
			handler.sendMessageDelayed(msg, 4000);

		}

	}

	/**
	 * 检测版本号
	 * 
	 * json格式 { versionName:"2.0", versionDes:"2.0版本来了", versionCode:"2",
	 * downloadUrl:"http:www.baidu.com" }
	 * 
	 */
	private void checkVersion() {

		new Thread(new Runnable() {
			public void run() {
				long startTime = System.currentTimeMillis();
				String strUrl = "http://192.168.3.101:8080/update.json";
				Message msg = Message.obtain();
				try {
					URL url = new URL(strUrl);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(2000);
					conn.setReadTimeout(2000);
					conn.setRequestMethod("GET");

					if (conn.getResponseCode() == 200) {
						InputStream inputStream = conn.getInputStream();
						String streamToString = StreamUtil.streamToString(inputStream);
						LogUtils.i(TAG, streamToString);
						JSONObject jsonObject = new JSONObject(streamToString);
						int versionCode = jsonObject.getInt("versionCode");

						if (versionCode != mLocalVersionCode) {

							Bundle data = new Bundle();
							data.putString("versionDes", jsonObject.getString("versionDes"));
							data.putString("versionName", jsonObject.getString("versionName"));
							data.putString("downloadUrl", jsonObject.getString("downloadUrl"));
							data.putInt("versionCode", versionCode);

							msg.what = UPDATE_VERSION;
							msg.setData(data);

						} else {
							msg.what = ENTER_HOME;
						}

					}
				} catch (MalformedURLException e) {

					e.printStackTrace();
					msg.what = URL_ERROR;
				} catch (JSONException e) {

					e.printStackTrace();
					msg.what = JSON_ERROR;
				} catch (IOException e) {

					e.printStackTrace();
					msg.what = IO_ERROR;
				} finally {
					long endTime = System.currentTimeMillis();
					if (endTime - startTime < 4000) {
						SystemClock.sleep(4000 - (endTime - startTime));
					}
					handler.sendMessage(msg);
				}
			}
		}).start();

	}

	/**
	 * 返回版本号
	 * 
	 * @return ！0代表成功
	 */
	private int getVersionCode() {

		PackageManager pm = getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (Exception e) {

			e.printStackTrace();

		}
		return 0;
	}

	/**
	 * 获取版本名称 @ return 应用版本名称
	 */
	private String getVersionName() {

		PackageManager pm = getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionName;
		} catch (Exception e) {

			e.printStackTrace();

		}
		return null;
	}

	/**
	 * 初始化 ui
	 */
	private void initUI() {

		tv_version_name = (TextView) findViewById(R.id.tv_version_name);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		enterHome();
		super.onActivityResult(requestCode, resultCode, data);
	}
}
