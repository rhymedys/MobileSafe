package com.cc.mobilesafe.Activity;

import java.util.ArrayList;
import java.util.List;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.Adapter.LockorUnlockListAdapter;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.R.menu;
import com.cc.mobilesafe.Bean.AppInfoBean;
import com.cc.mobilesafe.Engine.AppInfoProcider;
import com.cc.mobilesafe.Engine.AppLockDao;
import com.cc.mobilesafe.Utils.LogUtils;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AppLockActivity extends Activity {

	protected static final String TAG = "AppLockActivity";
	protected static final int UPDATE_ADAPTER = 1;
	private Context context;
	private LinearLayout ll_Lock;
	private LinearLayout ll_Unlock;
	private TextView tv_LockTitle;
	private TextView tv_UnLockTitle;
	private ListView lv_LockList;
	private ListView lv_UnLockList;
	private Button btn_Lock;
	private Button btn_UnLock;
	private List<AppInfoBean> appInfoList;
	private List<AppInfoBean> lockList;
	private List<AppInfoBean> unLockList;
	private AppLockDao db;
	private LockorUnlockListAdapter unLockListAdapter;
	private LockorUnlockListAdapter lockListAdapter;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				if (lockList != null && unLockList != null) {

					lockListAdapter = new LockorUnlockListAdapter(context, lockList, true);
					lv_LockList.setAdapter(lockListAdapter);

					unLockListAdapter = new LockorUnlockListAdapter(context, unLockList, false);
					lv_UnLockList.setAdapter(unLockListAdapter);

					tv_LockTitle.setText("已加锁应用：" + lockList.size());
					tv_UnLockTitle.setText("未加锁应用：" + unLockList.size());
				}
				break;
			case UPDATE_ADAPTER:
				if (lockListAdapter != null && unLockListAdapter != null) {
					LogUtils.i(TAG, "UPDATE_ADAPTER");
					lockListAdapter.notifyDataSetChanged();
					unLockListAdapter.notifyDataSetChanged();

					tv_LockTitle.setText("已加锁应用：" + lockList.size());
					tv_UnLockTitle.setText("未加锁应用：" + unLockList.size());
				}
				break;

			default:
				break;
			}
		}

	};
	private Animation loadAnimation;

	/**
	 * 标志 该操作是发生在 lock列表 还是unlock列表
	 */
	private boolean isLock = false;
	private AdapterView<?> listParent;
	private View listView;
	private int listPositon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_lock);
		this.context = this;

		initUI();
		initData();

	}

	/**
	 * 初始化 所有数据 区分已加锁 和未加锁应用
	 */
	private void initData() {
		lockList = new ArrayList<AppInfoBean>();
		unLockList = new ArrayList<AppInfoBean>();

		new Thread(new Runnable() {
			public void run() {
				appInfoList = AppInfoProcider.getAppInfoList(context);
				db = AppLockDao.getInstance(context);
				List<String> queryAllLock = db.queryAll();
				if (queryAllLock.size() != 0) {
					for (AppInfoBean bean : appInfoList) {
						if (queryAllLock.contains(bean.packageName)) {
							lockList.add(bean);
						} else {
							unLockList.add(bean);
						}

					}
				} else {
					unLockList = appInfoList;

				}
				LogUtils.i(TAG, "Thread:" + queryAllLock.size() + " " + lockList.size() + " " + unLockList.size());

				handler.sendEmptyMessage(0);
			}
		}).start();

	}

	/**
	 * 初始化 所有ui结构
	 */
	private void initUI() {
		loadAnimation = AnimationUtils.loadAnimation(context, R.anim.applock_out_anim);

		ll_Lock = (LinearLayout) findViewById(R.id.ll_lock);
		ll_Unlock = (LinearLayout) findViewById(R.id.ll_unlock);

		tv_LockTitle = (TextView) findViewById(R.id.tv_lockTitle);
		tv_UnLockTitle = (TextView) findViewById(R.id.tv_unTitle);

		lv_LockList = (ListView) findViewById(R.id.lv_lockApplist);
		lv_UnLockList = (ListView) findViewById(R.id.lv_unApplist);

		btn_Lock = (Button) findViewById(R.id.btn_lock);
		btn_UnLock = (Button) findViewById(R.id.btn_unlock);

		btn_Lock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ll_Lock.setVisibility(View.VISIBLE);
				ll_Unlock.setVisibility(View.GONE);

				btn_Lock.setBackgroundResource(R.drawable.tab_left_pressed);
				btn_UnLock.setBackgroundResource(R.drawable.tab_right_default);

				btn_Lock.setClickable(false);
				btn_UnLock.setClickable(true);

			}
		});

		btn_UnLock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				ll_Lock.setVisibility(View.GONE);
				ll_Unlock.setVisibility(View.VISIBLE);

				btn_Lock.setBackgroundResource(R.drawable.tab_right_default);
				btn_UnLock.setBackgroundResource(R.drawable.tab_right_pressed);

				btn_Lock.setClickable(true);
				btn_UnLock.setClickable(false);
			}
		});

		lv_LockList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
				listParent = parent;
				listView = view;
				listPositon = position;
				isLock = true;
				ImageView iv_lock = (ImageView) view.findViewById(R.id.iv_lock);
				iv_lock.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {

						view.startAnimation(loadAnimation);
					}
				});

			}
		});

		lv_UnLockList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
				listParent = parent;
				listView = view;
				listPositon = position;
				isLock = false;
				ImageView iv_lock = (ImageView) view.findViewById(R.id.iv_lock);
				iv_lock.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						view.startAnimation(loadAnimation);
					}
				});

			}
		});

		loadAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				
				if (isLock) {
					AppInfoBean bean = (AppInfoBean) listParent.getItemAtPosition(listPositon);
					unLockList.add(bean);
					lockList.remove(listPositon);
					db.delete(bean.packageName);
					handler.sendEmptyMessage(UPDATE_ADAPTER);
				} else {
					AppInfoBean bean = (AppInfoBean) listParent.getItemAtPosition(listPositon);
					lockList.add(bean);
					unLockList.remove(listPositon);
					db.insert(bean.packageName);
					handler.sendEmptyMessage(UPDATE_ADAPTER);
				}
			}
		});

	}

}
