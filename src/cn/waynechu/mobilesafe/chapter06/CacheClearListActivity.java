package cn.waynechu.mobilesafe.chapter06;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.waynechu.mobilesafe.R;
import cn.waynechu.mobilesafe.chapter06.adapter.CacheCleanAdapter;
import cn.waynechu.mobilesafe.chapter06.entity.CacheInfo;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.IPackageStatsObserver.Stub;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 缓存扫描逻辑
 * 
 * @author waynechu
 */
public class CacheClearListActivity extends Activity implements OnClickListener {
	protected static final int SCANNING = 100;
	protected static final int FINISH = 101;
	private PackageManager pm;
	/** 建议清理 */
	private TextView mRecommendTV;
	/** 可清理 */
	private TextView mCanCleanTV;
	private ListView mCacheLV;
	private Button mCacheBtn;
	private AnimationDrawable animation;
	private CacheCleanAdapter adapter;
	public long cacheMemory;
	private Thread thread;
	private List<CacheInfo> mCacheInfos = new ArrayList<CacheInfo>();
	private List<CacheInfo> cacheInfos = new ArrayList<CacheInfo>();
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SCANNING:
				PackageInfo info = (PackageInfo) msg.obj;
				mRecommendTV.setText("正在扫描：  " + info.packageName);
				mCanCleanTV.setText("已扫描缓存：  "
						+ Formatter.formatFileSize(CacheClearListActivity.this,
								cacheMemory));
				mCacheInfos.clear();
				mCacheInfos.addAll(cacheInfos);

				adapter.notifyDataSetChanged();
				mCacheLV.setSelection(mCacheInfos.size());
				break;
			case FINISH:
				animation.stop();
				if (cacheMemory > 0) {
					mCacheBtn.setEnabled(true);
				} else {
					mCacheBtn.setEnabled(false);
					Toast.makeText(CacheClearListActivity.this, "您的手机洁净如新",
							Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cacheclearlist);
		pm = getPackageManager();
		initView();
	}

	/** 初始化控件 */
	private void initView() {
		findViewById(R.id.rl_titlebar).setBackgroundColor(
				getResources().getColor(R.color.rose_red));
		ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
		mLeftImgv.setOnClickListener(this);
		mLeftImgv.setImageResource(R.drawable.back);
		((TextView) findViewById(R.id.tv_title)).setText("缓存扫描");
		mRecommendTV = (TextView) findViewById(R.id.tv_recommend_clean);
		mCanCleanTV = (TextView) findViewById(R.id.tv_can_clean);
		mCacheLV = (ListView) findViewById(R.id.lv_scancache);
		mCacheBtn = (Button) findViewById(R.id.btn_cleanall);
		mCacheBtn.setOnClickListener(this);
		animation = (AnimationDrawable) findViewById(R.id.imgv_broom)
				.getBackground();
		// setOneShot()方法表示动画效果是否只执行一次
		animation.setOneShot(false);
		animation.start();
		adapter = new CacheCleanAdapter(this, mCacheInfos);
		mCacheLV.setAdapter(adapter);
		fillData();
	}

	/** 填充数据 */
	private void fillData() {
		thread = new Thread() {
			public void run() {
				// 遍历手机内所有应用程序
				cacheInfos.clear();
				List<PackageInfo> infos = pm.getInstalledPackages(0);
				for (PackageInfo info : infos) {
					getCacheSize(info);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Message msg = Message.obtain();
					msg.obj = info;
					msg.what = SCANNING;
					handler.sendMessage(msg);
				}
				Message msg = Message.obtain();
				msg.what = FINISH;
				handler.sendMessage(msg);
			}
		};
		thread.start();
	}

	/**
	 * 获取某个包名对应的应用程序的缓存大小
	 * 
	 * @param info包信息
	 */
	private void getCacheSize(PackageInfo info) {
		try {
			/*
			 * 由于getPackageSizeInfo(String packageName,IPackageStatsObserver
			 * observer)方法 是隐藏的，所以要通过反射来获取该方法，packageName包名，observer远程服务的AIDL接口
			 */
			Method method = PackageManager.class.getDeclaredMethod(
					"getPackageSizeInfo", String.class,
					IPackageStatsObserver.class);
			method.invoke(pm, info.packageName, new MyPackObserver(info));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 该类是一个AIDL的实现类，需要实现该类中的onGetStatsCompleted方法，然后通过pStats.cacheSize()获取到缓存信息
	 * 
	 * @author waynechu
	 * 
	 */
	private class MyPackObserver extends Stub {
		private PackageInfo info;

		public MyPackObserver(PackageInfo info) {
			this.info = info;
		}

		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			long cachesize = pStats.cacheSize;
			if (cachesize >= 0) {
				CacheInfo cacheInfo = new CacheInfo();
				cacheInfo.cacheSiza = cachesize;
				cacheInfo.packagename = info.packageName;
				cacheInfo.appName = info.applicationInfo.loadLabel(pm)
						.toString();
				cacheInfo.appIcon = info.applicationInfo.loadIcon(pm);
				cacheInfos.add(cacheInfo);
				cacheMemory += cachesize;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		animation.stop();
		if (thread != null) {
			thread.interrupt();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgv_leftbtn:
			finish();
			break;
		case R.id.btn_cleanall:
			if (cacheMemory > 0) {
				// 跳转到清理缓存界面
				Intent intent = new Intent(this, CleanCacheActivity.class);
				// 传递将要清理的垃圾大小
				intent.putExtra("cacheMemory", cacheMemory);
				startActivity(intent);
				finish();
			}
			break;
		}
	}
}
