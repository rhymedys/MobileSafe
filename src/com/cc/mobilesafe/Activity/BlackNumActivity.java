package com.cc.mobilesafe.Activity;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.Adapter.BlackNumAdapter;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.Bean.BlackNumberInfoBean;
import com.cc.mobilesafe.Utils.LogUtils;
import com.cc.mobilesafe.Utils.ToastUtil;
import com.cc.mobilesafe.db.Dao.BlackNumberDao;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class BlackNumActivity extends Activity {

	/**
	 * 创建一个ADAPTER标志
	 */
	protected static final int NEW_ADAPTER = 0;
	/**
	 * 刷新ADAPTER标志
	 */
	protected static final int REFRESH_ADAPTER = 1;
	/**
	 * 获取数据库数据数量的标志
	 */
	protected static final int LOAD_DATA_COUNT = 2;
	protected static final String TAG = null;

	private Context context;
	private ListView lv_blacknumber;
	private Button btn_add;
	private BlackNumberDao blackNumberDao;
	private int mode = 0;
	private BlackNumAdapter blackNumAdapter;
	private ArrayList<BlackNumberInfoBean> listAll;
	private int intDataCount;

	private EditText et_phone;
	private RadioGroup rg_mode;
	private ImageView iv_delete;
	/**
	 * 防止重复加载的变量 正在加载 为true 加载完毕为false
	 */
	private boolean isLoad = false;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case NEW_ADAPTER:
				blackNumAdapter = new BlackNumAdapter(context, listAll, handler);
				lv_blacknumber.setAdapter(blackNumAdapter);

				break;
			case REFRESH_ADAPTER:
				if (blackNumAdapter != null) {
					blackNumAdapter.notifyDataSetChanged();
					LogUtils.i(TAG, "REFRESH_ADAPTER="+REFRESH_ADAPTER);
				}
				break;
			case LOAD_DATA_COUNT:
				if (blackNumberDao != null) {
					intDataCount = blackNumberDao.getCount();
					LogUtils.i(TAG, "LOAD_DATA_COUNT="+intDataCount);
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
		setContentView(R.layout.activity_black_num);
		this.context = this;

		initUI();
		initData();
	}

	/**
	 * 初始化 初次加载数据
	 */
	private void initData() {
		// 第一次加载数据从位置0开始
		new Thread(new Runnable() {
			public void run() {
				blackNumberDao = BlackNumberDao.getInstance(context);

				listAll = blackNumberDao.query(0);
				handler.sendEmptyMessage(0);
				handler.sendEmptyMessage(LOAD_DATA_COUNT);
			}
		}).start();

	}

	/**
	 * 
	 */
	private void initUI() {
		lv_blacknumber = (ListView) findViewById(R.id.lv_blacknumber);
		btn_add = (Button) findViewById(R.id.btn_add);

		btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				showAddDialog();
			}
		});

		// 监听list的滑动状态
		lv_blacknumber.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO 自动生成的方法存根
				if (listAll != null) {
					if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
							&& (lv_blacknumber.getLastVisiblePosition() >= listAll.size() - 1) && !isLoad) {
						if (listAll.size() < intDataCount) {
							new Thread(new Runnable() {
								public void run() {
									blackNumberDao = BlackNumberDao.getInstance(context);
									ArrayList<BlackNumberInfoBean> moreList = blackNumberDao.query(listAll.size());
									listAll.addAll(moreList);
									handler.sendEmptyMessage(REFRESH_ADAPTER);
								}
							}).start();
						}else {
							
						}

					}
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO 自动生成的方法存根

			}
		});

	}

	/**
	 * 显示添加对话框
	 */
	protected void showAddDialog() {
		// TODO 自动生成的方法存根
		Builder builder = new AlertDialog.Builder(context);
		final AlertDialog dialog = builder.create();

		View view = View.inflate(context, R.layout.dialog_add_blacknumber, null);
		et_phone = (EditText) view.findViewById(R.id.et_phone);
		rg_mode = (RadioGroup) view.findViewById(R.id.rg_mode);

		rg_mode.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO 自动生成的方法存根
				switch (checkedId) {
				case R.id.rb_all:
					mode = 2;
					break;
				case R.id.rb_phone:
					mode = 1;
					break;
				case R.id.rb_sms:
					mode = 0;
					break;

				default:
					break;
				}
			}
		});

		view.findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				String phone = et_phone.getText().toString().trim();
				if (!TextUtils.isEmpty(phone)) {
					blackNumberDao.insert(phone, mode);
					BlackNumberInfoBean bean = new BlackNumberInfoBean();
					bean.mode = mode;
					bean.phone = phone;
					listAll.add(0, bean);

					handler.sendEmptyMessage(REFRESH_ADAPTER);
					handler.sendEmptyMessage(LOAD_DATA_COUNT);
					dialog.dismiss();
				} else {
					ToastUtil.show(context, "请输入正确的电话号码");
				}

			}
		});
		view.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				dialog.dismiss();
			}
		});

		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

}
