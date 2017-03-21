package cn.waynechu.mobilesafe.chapter03;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.waynechu.mobilesafe.R;
import cn.waynechu.mobilesafe.chapter02.ContactSelectActivity;
import cn.waynechu.mobilesafe.chapter03.db.dao.BlackNumberDao;
import cn.waynechu.mobilesafe.chapter03.entity.BlackContactInfo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * �ֶ����뷽ʽ��Ӻ���������
 * 
 * @author waynechu
 * 
 */
public class AddBlackNumberActivity extends Activity implements OnClickListener {

	private CheckBox mSmsCB;
	private CheckBox mTelCB;
	private EditText mNumET;
	private EditText mNameET;
	private BlackNumberDao dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_blacknumber);
		dao = new BlackNumberDao(this);
		initView();
	}

	private void initView() {
		findViewById(R.id.rl_titlebar).setBackgroundColor(
				getResources().getColor(R.color.bright_purple));
		((TextView) findViewById(R.id.tv_title)).setText("��Ӻ�����");
		ImageView mLeftImagv = (ImageView) findViewById(R.id.imgv_leftbtn);
		mLeftImagv.setOnClickListener(this);
		mLeftImagv.setImageResource(R.drawable.back);
		mSmsCB = (CheckBox) findViewById(R.id.cb_blacknumber_sms);
		mTelCB = (CheckBox) findViewById(R.id.cb_blacknumber_tel);
		mNumET = (EditText) findViewById(R.id.et_blacknumber);
		mNameET = (EditText) findViewById(R.id.et_blackname);
		findViewById(R.id.add_blacknum_btn).setOnClickListener(this);
		findViewById(R.id.add_fromcontact_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgv_leftbtn:
			finish();
			break;
		case R.id.add_blacknum_btn:
			String number = mNumET.getText().toString().trim();
			String name = mNameET.getText().toString().trim();
			if (TextUtils.isEmpty(number) || TextUtils.isEmpty(name)) {
				Toast.makeText(this, "�绰������ֻ��Ų���Ϊ�գ�", Toast.LENGTH_SHORT)
						.show();
				return;
			} else {
				BlackContactInfo blackContactInfo = new BlackContactInfo();
				blackContactInfo.phoneNumber = number;
				blackContactInfo.contactName = name;
				if (mSmsCB.isChecked() & mTelCB.isChecked()) {
					blackContactInfo.mode = 3;
				} else if (mSmsCB.isChecked() & !mTelCB.isChecked()) {
					blackContactInfo.mode = 2;
				} else if (!mSmsCB.isChecked() & mTelCB.isChecked()) {
					blackContactInfo.mode = 1;
				} else {
					Toast.makeText(this, "��ѡ������ģʽ��", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!dao.isNumberExist(blackContactInfo.phoneNumber)) {
					dao.add(blackContactInfo);
				} else {
					Toast.makeText(this, "�ú����Ѿ��������������", Toast.LENGTH_SHORT).show();
				}
				finish();
			}
			break;
		case R.id.add_fromcontact_btn:
			startActivityForResult(
			        //���õڶ��µ���ϵ��ѡ�����
			        
					new Intent(this, ContactSelectActivity.class),
					Toast.LENGTH_SHORT);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			String phone = data.getStringExtra("phone");
			String name = data.getStringExtra("name");
			// ƥ�����з������ַ���ʹ��replaceAll()����ȥ��
			String regEx = "[^0-9]";
			Pattern pattern = Pattern.compile(regEx);
			Matcher m = pattern.matcher(phone);
			String safePhone = m.replaceAll("");
			mNumET.setText(safePhone);
			mNameET.setText(name);
		}
	}

}
