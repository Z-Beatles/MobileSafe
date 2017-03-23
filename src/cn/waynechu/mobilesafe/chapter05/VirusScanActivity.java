package cn.waynechu.mobilesafe.chapter05;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import cn.waynechu.mobilesafe.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class VirusScanActivity extends Activity implements OnClickListener {
	private TextView mLastTimTV;
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

	private void copyDB(final String dbname) {
		new Thread() {
			public void run() {
				try {
					File file = new File(getFilesDir(), dbname);
					if (file.exists() && file.length() > 0) {
						Log.i("VirusScanActivity", "数据库已经存在！");
						return;
					}
					InputStream is = getAssets().open(dbname);
					FileOutputStream fos = openFileOutput(dbname, MODE_PRIVATE);
					byte[] buffer = new byte[1024];

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}.start();

	}

	private void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
