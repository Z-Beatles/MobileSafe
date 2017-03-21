package cn.waynechu.mobilesafe.chapter04;

import java.util.ArrayList;
import java.util.List;

import cn.waynechu.mobilesafe.R;
import cn.waynechu.mobilesafe.chapter04.adapter.AppManagerAdapter;
import cn.waynechu.mobilesafe.chapter04.entity.AppInfo;
import cn.waynechu.mobilesafe.chapter04.utils.AppInfoParser;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AppManagerActivity extends Activity implements OnClickListener {
	private TextView mPhoneMemoryTV;
	private UninstallRecesiver receiver;
	private TextView mSDMemoryTV;
	private TextView mAppNumTV;
	private ListView mListView;
	private AppManagerAdapter adapter;
	private List<AppInfo> appInfos;
	private List<AppInfo> userAppInfos = new ArrayList<AppInfo>();
	private List<AppInfo> systemAppInfos = new ArrayList<AppInfo>();

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 10:
				adapter = new AppManagerAdapter(userAppInfos, systemAppInfos,
						AppManagerActivity.this);
				mListView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				break;
			case 15:
				adapter.notifyDataSetChanged();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_app_manager);
		receiver = new UninstallRecesiver();
		IntentFilter intentFilter = new IntentFilter(
				Intent.ACTION_PACKAGE_REMOVED);
		intentFilter.addDataScheme("package");
		registerReceiver(receiver, intentFilter);
		initView();
	}

	/** ��ʼ���ؼ� */
	private void initView() {
		findViewById(R.id.rl_titlebar).setBackgroundColor(
				getResources().getColor(R.color.bright_yellow));
		ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
		mLeftImgv.setOnClickListener(this);
		mLeftImgv.setImageResource(R.drawable.back);
		mPhoneMemoryTV = (TextView) findViewById(R.id.tv_phonememory_appmanager);
		mSDMemoryTV = (TextView) findViewById(R.id.tv_sdmemory_appmanager);
		mAppNumTV = (TextView) findViewById(R.id.tv_appnumber);
		mListView = (ListView) findViewById(R.id.lv_appmanager);

		getMemoryFromPhone();
		initData();
		initListener();
	}

	private void initListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				if (adapter != null) {
					new Thread() {
						public void run() {
							AppInfo mappInfo = (AppInfo) adapter
									.getItem(position);
							boolean flag = mappInfo.isSelected;
							for (AppInfo appInfo : userAppInfos) {
								appInfo.isSelected = false;
							}
							for (AppInfo appInfo : systemAppInfos) {
								appInfo.isSelected = false;
							}
							if (mappInfo != null) {
								if (flag) {
									mappInfo.isSelected = false;
								} else {
									mappInfo.isSelected = true;
								}
								mHandler.sendEmptyMessage(15);
							}
						}
					}.start();
				}
			}
		});
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int arg2, int arg3) {
				if (firstVisibleItem >= userAppInfos.size() + 1) {
					mAppNumTV.setText("ϵͳ����" + systemAppInfos.size() + "��");
				} else {
					mAppNumTV.setTag("�û�����" + userAppInfos.size() + "��");
				}
			}
		});
	}

	public void initData() {
		appInfos = new ArrayList<AppInfo>();
		new Thread() {
			public void run() {
				appInfos.clear();
				userAppInfos.clear();
				systemAppInfos.clear();
				appInfos.addAll(AppInfoParser
						.getAppInfos(AppManagerActivity.this));
				for (AppInfo appInfo : appInfos) {
					if (appInfo.isUserApp) {
						userAppInfos.add(appInfo);
					} else {
						systemAppInfos.add(appInfo);
					}
				}
				mHandler.sendEmptyMessage(10);
			}
		}.start();
	}

	private void getMemoryFromPhone() {
		long avail_sd = Environment.getExternalStorageDirectory()
				.getFreeSpace();
		long avail_rom = Environment.getDataDirectory().getFreeSpace();

		String str_avail_sd = Formatter.formatFileSize(this, avail_sd);
		String str_avail_rom = Formatter.formatFileSize(this, avail_rom);
		mPhoneMemoryTV.setText("ʣ���ֻ��ڴ棺" + str_avail_rom);
		mSDMemoryTV.setText("ʣ��SD���ڴ棺" + str_avail_sd);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.imgv_leftbtn:
			finish();
			break;
		}
	}

	/**
	 * ����Ӧ�ó���ж�صĹ㲥
	 * 
	 * @author waynechu
	 * 
	 */
	class UninstallRecesiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			initData();
		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		receiver = null;
		super.onDestroy();
	}

}
