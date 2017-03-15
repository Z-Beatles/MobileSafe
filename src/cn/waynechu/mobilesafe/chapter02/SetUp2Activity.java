package cn.waynechu.mobilesafe.chapter02;

import cn.waynechu.mobilesafe.R;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

public class SetUp2Activity extends BaseSetUpActivity implements
		OnClickListener {
	private TelephonyManager mTelephonyManager;
	private Button mBindSIMBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		initView();
	}

	private void initView() {
		// 设置第二个小圆点的颜色
		((RadioButton) findViewById(R.id.rb_second)).setChecked(true);
		mBindSIMBtn = (Button) findViewById(R.id.btn_bind_sim);
		mBindSIMBtn.setOnClickListener(this);
		if (isBind()) {
			mBindSIMBtn.setEnabled(false);
		} else {
			mBindSIMBtn.setEnabled(true);
		}
	}

	private boolean isBind() {
		String simString = sp.getString("sim", null);
		if (TextUtils.isEmpty(simString)) {
			return false;
		}
		return true;
	}

	@Override
	public void showPre() {
		startActivityAndFinishSelf(SetUp1Activity.class);
	}

	@Override
	public void showNext() {
//		// 测试机无法绑定成功，先取消帮卡功能
//		if (!isBind()) {
//			Toast.makeText(this, "您还没有绑定SIM卡！", Toast.LENGTH_SHORT).show();
//			return;
//		}
		startActivityAndFinishSelf(SetUp3Activity.class);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_bind_sim:
			bindSIM();
			break;
		}
	}

	/**
	 * 判断是否绑定SIM卡，如果没有则将手机的SIM卡串号存入SharePreferences对象中
	 */
	private void bindSIM() {
		if (!isBind()) {
			String simSerialNumber = mTelephonyManager.getSimSerialNumber();
			Editor edit = sp.edit();
			edit.putString("sim", simSerialNumber);
			edit.commit();
			Toast.makeText(this, "SIM卡绑定成功！", Toast.LENGTH_SHORT).show();
			mBindSIMBtn.setEnabled(false);
		} else {
			Toast.makeText(this, "SIM卡已经绑定！", Toast.LENGTH_SHORT).show();
			mBindSIMBtn.setEnabled(false);
		}
	}
}