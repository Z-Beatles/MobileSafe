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
 * ������ɱ�߼�
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
		String string = mSP.getString("lastVirusScan", "����û�в�ɱ������");
		mLastTimeTV.setText(string);
		super.onResume();
	}

	/**
	 * ���Ʋ������ݿ�
	 * 
	 * @param string
	 */
	private void copyDB(final String dbname) {
		new Thread() {
			public void run() {
				try {
					// getFilesDir()�������ڻ�ȡ/data/data/<package>/filesĿ¼��һ���һЩ��ʱ�䱣�������
					File file = new File(getFilesDir(), dbname);
					if (file.exists() && file.length() > 0) {
						Log.i("VirusScanActivity", "���ݿ��Ѵ��ڣ�");
						return;
					}
					// assets�ļ���������ļ����Ǳ���ԭʼ���ļ���ʽ����Ҫ��AssetManager���ֽ�������ʽ��ȡ�ļ���
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
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		findViewById(R.id.rl_titlebar).setBackgroundColor(
				getResources().getColor(R.color.light_blue));
		ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
		((TextView) findViewById(R.id.tv_title)).setText("������ɱ");
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
