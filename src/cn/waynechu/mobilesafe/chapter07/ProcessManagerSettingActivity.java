package cn.waynechu.mobilesafe.chapter07;

import cn.waynechu.mobilesafe.R;
import cn.waynechu.mobilesafe.chapter07.service.AutoKillProcessService;
import cn.waynechu.mobilesafe.chapter07.utils.SystemInfoUtils;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * ���ý����߼�
 * 
 * @author waynechu
 * 
 */
public class ProcessManagerSettingActivity extends Activity implements
		OnClickListener, OnCheckedChangeListener {

	private SharedPreferences mSP;
	private ToggleButton mShowSysAppsTgb;
	private ToggleButton mKillProcessTgb;
	private boolean runing;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_processmanagersetting);
		mSP = getSharedPreferences("config", MODE_PRIVATE);
		initView();
	}

	private void initView() {
		findViewById(R.id.rl_titlebar).setBackgroundColor(
				getResources().getColor(R.color.bright_green));
		ImageView mLeftImgv = (ImageView) findViewById(R.id.imgv_leftbtn);
		mLeftImgv.setOnClickListener(this);
		mLeftImgv.setImageResource(R.drawable.back);
		((TextView) findViewById(R.id.tv_title)).setText("���̹�������");
		mShowSysAppsTgb = (ToggleButton) findViewById(R.id.tgb_showsys_process);
		mKillProcessTgb = (ToggleButton) findViewById(R.id.tgb_killprocess_lockscreen);
		mShowSysAppsTgb.setChecked(mSP.getBoolean("showSystemProcess", true));
		runing = SystemInfoUtils.isServiceRunning(this, "cn.waynechu.mobilesafe.chapter07.service.AutoKillProcessService");
		mKillProcessTgb.setChecked(runing);
		initListener();
	}

	/** ��ʼ�������� */
	private void initListener() {
		mKillProcessTgb.setOnCheckedChangeListener(this);
		mShowSysAppsTgb.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch(buttonView.getId()){
		case R.id.tgb_showsys_process:
			saveStatus("showSystemProcess",isChecked);
			break;
		case R.id.tgb_killprocess_lockscreen:
			Intent service = new Intent(this,AutoKillProcessService.class);
			if(isChecked){
				// ��������
				startService(service);
			}else{
				// �رշ���
				stopService(service);
			}
			break;
		}
	}

	private void saveStatus(String string, boolean isChecked) {
		Editor edit = mSP.edit();
		edit.putBoolean(string, isChecked);
		edit.commit();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.imgv_leftbtn:
			finish();
			break;
		}
	}
}
