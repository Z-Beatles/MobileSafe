package cn.waynechu.mobilesafe.chapter05;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import cn.waynechu.mobilesafe.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 病毒查杀逻辑
 * 
 * @author waynechu
 * 
 */
public class VirusScanActivity extends Activity implements OnClickListener {
	private TextView mLastTimeTV;
	private SharedPreferences mSP;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_virusscan);
		mSP = getSharedPreferences("config", MODE_PRIVATE);
		copyDB("antivirus.db");
		initView();
	}

	@Override
	protected void onResume() {
		String string = mSP.getString("lastVirusScan", "您还没有查杀病毒！");
		mLastTimeTV.setText(string);
		super.onResume();
	}

	/**
	 * 复制病毒数据库
	 * 
	 * @param string
	 */
	private void copyDB(final String dbname) {
		new Thread() {
			public void run() {
				try {
					// getFilesDir()方法用于获取/data/data/<package>/files目录，一般放一些长时间保存的数据
					File file = new File(getFilesDir(), dbname);
					if (file.exists() && file.length() > 0) {
						Log.i("VirusScanActivity", "数据库已存在！");
						return;
					}
					// assets文件夹里面的文件都是保持原始的文件格式，需要用AssetManager以字节流的形式读取文件。
					InputStream is = getAssets().open(dbname);
					FileOutputStream fos = openFileOutput(dbname, MODE_PRIVATE);
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						// write(byte[] buffer, int byteOffset, int byteCount)
						fos.write(buffer, 0, len);
					}
					is.close();
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		findViewById(R.id.rl_titlebar).setBackgroundColor(
				getResources().getColor(R.color.light_blue));
		ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
		((TextView) findViewById(R.id.tv_title)).setText("病毒查杀");
		mLeftImgv.setOnClickListener(this);
		mLeftImgv.setImageResource(R.drawable.back);
		mLastTimeTV = (TextView) findViewById(R.id.tv_lastscantime);
		findViewById(R.id.rl_allscanvirus).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgv_leftbtn:
			finish();
			break;
		case R.id.rl_allscanvirus:
			startActivity(new Intent(this, VirusScanSpeedActivity.class));
			break;
		}
	}
}
