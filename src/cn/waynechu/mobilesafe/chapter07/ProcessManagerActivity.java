package cn.waynechu.mobilesafe.chapter07;

import java.util.ArrayList;
import java.util.List;
import cn.waynechu.mobilesafe.R;
import cn.waynechu.mobilesafe.chapter07.adapter.ProcessManagerAdapter;
import cn.waynechu.mobilesafe.chapter07.entity.TaskInfo;
import cn.waynechu.mobilesafe.chapter07.utils.SystemInfoUtils;
import cn.waynechu.mobilesafe.chapter07.utils.TaskInfoParser;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

/**
 * 进程管理逻辑
 * 
 * @author waynechu
 * 
 */
public class ProcessManagerActivity extends Activity implements OnClickListener {

	// 运行中的程序数量
	private TextView mRunProcessNum;
	// 可用/总内存
	private TextView mMemoryTV;
	// 固定显示进程的个数
	private TextView mProcessNumTV;
	private int runningPocessCount;
	private long totalMem;
	private ListView mListView;
	private List<TaskInfo> runningTaskInfos;
	/** 用户进程信息集合 */
	private List<TaskInfo> userTaskInfos = new ArrayList<TaskInfo>();
	/** 系统进程信息集合 */
	private List<TaskInfo> sysTaskInfo = new ArrayList<TaskInfo>();
	protected ProcessManagerAdapter adapter;
	private ActivityManager manager;
	private int runningProcessCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_processmanager);
		initView();
		fillData();
	}

	@Override
	protected void onResume() {
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
		super.onResume();

	}

	private void initView() {
		findViewById(R.id.rl_titlebar).setBackgroundColor(
				getResources().getColor(R.color.bright_green));
		ImageView mLeftImagv = (ImageView) findViewById(R.id.imgv_leftbtn);
		mLeftImagv.setImageResource(R.drawable.back);
		mLeftImagv.setOnClickListener(this);
		ImageView mRightImgv = (ImageView) findViewById(R.id.imgv_rightbtn);
		mRightImgv.setImageResource(R.drawable.processmanager_setting_icon);
		mRightImgv.setOnClickListener(this);
		((TextView) findViewById(R.id.tv_title)).setText("进程管理");
		mRunProcessNum = (TextView) findViewById(R.id.tv_runningprocess_num);
		mMemoryTV = (TextView) findViewById(R.id.tv_memory_processmanager);
		mProcessNumTV = (TextView) findViewById(R.id.tv_user_runningprocess);
		// 通过系统信息工具类获取到进程个数，可用内存大小以及总内存大小
		runningPocessCount = SystemInfoUtils
				.getRunningPocessCount(ProcessManagerActivity.this);
		mRunProcessNum.setText("运行中的进程： " + runningPocessCount + "个");
		long totalAvailMem = SystemInfoUtils.getAvailMem(this);
		totalMem = SystemInfoUtils.getTotalMem();
		// formatFileSize()方法可以将long型转化为表示文件大小的KB、MB、GB等
		mMemoryTV.setText("可用/总内存："
				+ Formatter.formatFileSize(this, totalAvailMem) + "/"
				+ Formatter.formatFileSize(this, totalMem));
		mListView = (ListView) findViewById(R.id.lv_runningapps);
		initListener();
	}

	private void initListener() {
		findViewById(R.id.btn_selectall).setOnClickListener(this);
		findViewById(R.id.btn_select_inverse).setOnClickListener(this);
		findViewById(R.id.btn_cleanprocess).setOnClickListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object object = mListView.getItemAtPosition(position);
				if (object != null & object instanceof TaskInfo) {
					TaskInfo info = (TaskInfo) object;
					if (info.packageName.equals(getPackageName())) {
						// 当前点击的条目是本应用程序
						System.out.println("你点击了" + info.packageName + "应用！");
						return;
					}
					info.isChecked = !info.isChecked;
					adapter.notifyDataSetChanged();
				}
			}
		});
		// 滑动屏幕时动态显示系统进程或者用户进程顶栏
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int arg2, int arg3) {
				if (firstVisibleItem >= userTaskInfos.size() + 1) {
					mProcessNumTV.setText("系统进程：" + sysTaskInfo.size() + "个");
				} else {
					mProcessNumTV.setText("用户进程：" + userTaskInfos.size() + "个");
				}
			}
		});
	}

	private void fillData() {
		userTaskInfos.clear();
		sysTaskInfo.clear();
		new Thread() {
			public void run() {
				runningTaskInfos = TaskInfoParser
						.getRunningTaskInfos(getApplicationContext());
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						for (TaskInfo taskInfo : runningTaskInfos) {
							if (taskInfo.isUserApp) {
								userTaskInfos.add(taskInfo);
							} else {
								sysTaskInfo.add(taskInfo);
							}
						}
						if (adapter == null) {
							adapter = new ProcessManagerAdapter(
									getApplicationContext(), userTaskInfos,
									sysTaskInfo);
							mListView.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
						if (userTaskInfos.size() > 0) {
							mProcessNumTV.setText("用户进程："
									+ userTaskInfos.size() + "个");
						} else {
							mProcessNumTV.setText("系统进程：" + sysTaskInfo.size()
									+ "个");
						}
					}
				});
			}
		}.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgv_leftbtn:
			finish();
			break;
		case R.id.imgv_rightbtn:
			startActivity(new Intent(this, ProcessManagerSettingActivity.class));
			break;
		case R.id.btn_selectall:
			selectAll();
			break;
		case R.id.btn_select_inverse:
			inverse();
			break;
		case R.id.btn_cleanprocess:
			cleanProcess();
			break;
		}
	}

	/** 全选 */
	private void selectAll() {
		for (TaskInfo taskInfo : userTaskInfos) {
			if (taskInfo.packageName.equals(getPackageName())) {
				continue;
			}
			taskInfo.isChecked = true;
		}
		for (TaskInfo taskInfo : sysTaskInfo) {
			taskInfo.isChecked = true;
		}
		adapter.notifyDataSetChanged();
	}

	/** 反选 */
	private void inverse() {
		for (TaskInfo taskInfo : userTaskInfos) {
			if (taskInfo.packageName.equals(getPackageName())) {
				continue;
			}
			boolean checked = taskInfo.isChecked;
			taskInfo.isChecked = !checked;
		}
		for (TaskInfo taskInfo : sysTaskInfo) {
			boolean checked = taskInfo.isChecked;
			taskInfo.isChecked = !checked;
		}
		adapter.notifyDataSetChanged();
	}

	/** 清理进程 */
	private void cleanProcess() {
		manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		int count = 0;
		long saveMemory = 0;
		List<TaskInfo> killedtaskInfos = new ArrayList<TaskInfo>();
		for (TaskInfo info : userTaskInfos) {
			if (info.isChecked) {
				count++;
				saveMemory += info.appMemory;
				manager.killBackgroundProcesses(info.packageName);
				killedtaskInfos.add(info);
			}
		}
		for (TaskInfo info : sysTaskInfo) {
			if (info.isChecked) {
				count++;
				saveMemory += info.appMemory;
				manager.killBackgroundProcesses(info.packageName);
				killedtaskInfos.add(info);
			}
		}
		for (TaskInfo info : killedtaskInfos) {
			if (info.isUserApp) {
				userTaskInfos.remove(info);
			} else {
				sysTaskInfo.remove(info);
			}
		}
		runningProcessCount -= count;
		mRunProcessNum.setText("运行中的进程：" + runningProcessCount + "个");
		mMemoryTV.setText("可用/总内存："
				+ Formatter.formatFileSize(this,
						SystemInfoUtils.getAvailMem(this)) + "/"
				+ Formatter.formatFileSize(this, totalMem));
		Toast.makeText(
				this,
				"清理了" + count + "个进程，释放了"
						+ Formatter.formatFileSize(this, saveMemory) + "内存",
				Toast.LENGTH_SHORT).show();
		mProcessNumTV.setText("用户进程：" + userTaskInfos.size() + "个");
		adapter.notifyDataSetChanged();
	}
}
