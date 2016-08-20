package com.cc.mobilesafe.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cc.mobilesafe.R;
import com.cc.mobilesafe.R.id;
import com.cc.mobilesafe.R.layout;
import com.cc.mobilesafe.R.menu;
import com.cc.mobilesafe.adapter.MyContactlistAdapter;
import com.cc.mobilesafe.utils.ToastUtil;

import android.R.string;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ContactListActivity extends Activity {

	private Context context;
	private ListView lv_contactslist;
	private List<HashMap<String, String>> contactlist = new ArrayList<HashMap<String, String>>();

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO 自动生成的方法存根
			lv_contactslist.setAdapter(new MyContactlistAdapter(context, contactlist));
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		this.context = this;

		initUI();
		initData();
	}

	/**
	 * 获取系统联系人数据方法
	 */
	private void initData() {
		// TODO 自动生成的方法存根
		new Thread(new Runnable() {
			public void run() {

				ContentResolver resolver = getContentResolver();

				Cursor cursor_contact_id = resolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
						new String[] { "contact_id" }, null, null, null);
				contactlist.clear();
				while (cursor_contact_id.moveToNext()) {

					String strID = cursor_contact_id.getString(0);
					Cursor indexCursor = resolver.query(Uri.parse("content://com.android.contacts/data"),
							new String[] { "data1", "mimetype" }, "raw_contact_id=?", new String[] { strID }, null);
					HashMap<String, String> hashMap = new HashMap<String, String>();
					while (indexCursor.moveToNext()) {
						String strData1 = indexCursor.getString(0);
						String strMimetype = indexCursor.getString(1);
						// 区分数据类型 填充数据
						if (!TextUtils.isEmpty(strData1)) {
							if (strMimetype.equals("vnd.android.cursor.item/phone_v2")) {

								hashMap.put("phoneNum", strData1);

							} else if (strMimetype.equals("vnd.android.cursor.item/name")) {
								hashMap.put("phoneName", strData1);
							}
						}

					}
					indexCursor.close();
					contactlist.add(hashMap);

				}
				// 发送空消息告诉主线程 可以使用填充号的list
				handler.sendEmptyMessage(0);
			}
		}).start();
	}

	/**
	 * 初始化 ui 布局
	 */
	private void initUI() {
		lv_contactslist = (ListView) findViewById(R.id.lv_contactslist);
		lv_contactslist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				HashMap<String, String> itemAtPosition = (HashMap<String, String>) parent.getItemAtPosition(position);
				String num = itemAtPosition.get("phoneNum");
				if (!TextUtils.isEmpty(num)) {
					Intent intent = new Intent();
					intent.putExtra("phoneNum", num);
					setResult(0, intent);
					finish();
				}
				else{
					ToastUtil.show(context, "此用户号码为空");
				}
			}
		});
	}
}
