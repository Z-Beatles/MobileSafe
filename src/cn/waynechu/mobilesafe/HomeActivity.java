package cn.waynechu.mobilesafe;

import cn.waynechu.mobilesafe.R;
import cn.waynechu.mobilesafe.chapter01.adapter.HomeAdapter;
import cn.waynechu.mobilesafe.chapter02.LostFindActivity;
import cn.waynechu.mobilesafe.chapter02.dialog.InterPasswordDialog;
import cn.waynechu.mobilesafe.chapter02.dialog.InterPasswordDialog.MyCallBack;
import cn.waynechu.mobilesafe.chapter02.dialog.SetUpPasswordDialog;
import cn.waynechu.mobilesafe.chapter02.receiver.MyDeviceAdminReceiver;
import cn.waynechu.mobilesafe.chapter02.utils.MD5Utils;
import cn.waynechu.mobilesafe.chapter03.SecurityPhoneActivity;
import cn.waynechu.mobilesafe.chapter04.AppManagerActivity;
import cn.waynechu.mobilesafe.chapter05.VirusScanActivity;
import cn.waynechu.mobilesafe.chapter06.CacheClearListActivity;
import cn.waynechu.mobilesafe.chapter07.ProcessManagerActivity;
import cn.waynechu.mobilesafe.chapter08.TrafficMonitoringActivity;
import cn.waynechu.mobilesafe.chapter09.AdvancedToolsActivity;
import cn.waynechu.mobilesafe.chapter10.SettingsActivity;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

/**
 * ����ѡ�����
 * 
 * @author Wayne Chu
 * 
 */
public class HomeActivity extends Activity {
	// ����GridView
	private GridView gv_home;
	// �����ֻ���������� sp
	private SharedPreferences msharedPreferences;
	// �豸����Ա
	private DevicePolicyManager policyManager;
	// ����Ȩ��
	private ComponentName componentName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ��ʼ������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_home);
		msharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		// ��ʼ��GridView,����Adapter�������
		gv_home = (GridView) findViewById(R.id.gv_home);
		gv_home.setAdapter(new HomeAdapter(HomeActivity.this));
		// ������Ŀ����¼�
		gv_home.setOnItemClickListener(new OnItemClickListener() {
			// parent����gridView��view����ÿ����Ŀ��view����position����ÿ����Ŀ��λ��
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:// �ֻ�����
					if (isSetUpPassword()) {
						showInterPswdDialog();
					} else {
						showSetUpPswdDialog();
					}
					break;
				case 1:// ͨѶ��ʿ
					startActivity(SecurityPhoneActivity.class);
					break;
				case 2:// ����ܼ�
					startActivity(AppManagerActivity.class);
					break;
				case 3:// ������ɱ
					startActivity(VirusScanActivity.class);
					break;
				case 4:// ��������
					startActivity(CacheClearListActivity.class);
					break;
				case 5:// ���̹���
					startActivity(ProcessManagerActivity.class);
					break;
				case 6:// ����ͳ��
					startActivity(TrafficMonitoringActivity.class);
					break;
				case 7:// �߼�����
					startActivity(AdvancedToolsActivity.class);
					break;
				case 8:// ��������
					startActivity(SettingsActivity.class);
					break;
				default:
					break;
				}
			}
		});
		// ��ȡ�豸����Ա
		policyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		// ����Ȩ��
		componentName = new ComponentName(this, MyDeviceAdminReceiver.class);
		// �ж�����Ȩ��
		boolean active = policyManager.isAdminActive(componentName);
		if (!active) {
			Intent intent = new Intent(
					DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
					componentName);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
					"�ֻ���ʿ��������Զ���������������������Լ�Զ��������ݵĹ�����Ҫ������豸���������������Ϲ��ܽ��޷�����ʹ�ã�");
			startActivity(intent);
		}
	}

	/**
	 * ������������Ի���
	 */
	private void showSetUpPswdDialog() {
		final SetUpPasswordDialog setUpPasswordDialog = new SetUpPasswordDialog(
				HomeActivity.this);
		setUpPasswordDialog.setCallBack(new SetUpPasswordDialog.MyCallBack() {
			@Override
			public void ok() {
				String firstPwsd = setUpPasswordDialog.mFirstPWDET.getText()
						.toString().trim();
				String affirmPwsd = setUpPasswordDialog.mAffirmET.getText()
						.toString().trim();
				if (!TextUtils.isEmpty(firstPwsd)
						&& !TextUtils.isEmpty(affirmPwsd)) {
					if (firstPwsd.equals(affirmPwsd)) {
						savePswd(affirmPwsd);
						setUpPasswordDialog.dismiss();
						showInterPswdDialog();
					} else {
						Toast.makeText(HomeActivity.this, "�������벻һ�£�",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(HomeActivity.this, "���벻��Ϊ�գ�",
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void cancle() {
				setUpPasswordDialog.dismiss();
			}
		});
		setUpPasswordDialog.setCancelable(true);
		setUpPasswordDialog.show();

	}

	/**
	 * ��������
	 * 
	 * @param affirmPwsd
	 */
	private void savePswd(String affirmPwsd) {
		Editor edit = msharedPreferences.edit();
		// MD5���ܱ����û���˽
		edit.putString("PhoneAntiTheftPWD", MD5Utils.encode(affirmPwsd));
		edit.commit();
	}

	/**
	 * ������������Ի���
	 */
	private void showInterPswdDialog() {
		final String password = getPassword();
		final InterPasswordDialog mInterPasswordDialog = new InterPasswordDialog(
				HomeActivity.this);
		mInterPasswordDialog.setCallBack(new MyCallBack() {
			@Override
			public void confirm() {
				if (TextUtils.isEmpty(mInterPasswordDialog.getPassword())) {
					Toast.makeText(HomeActivity.this, "���벻��Ϊ��",
							Toast.LENGTH_SHORT).show();
				} else if (password.equals(MD5Utils.encode(mInterPasswordDialog
						.getPassword()))) {
					mInterPasswordDialog.dismiss();
					startActivity(LostFindActivity.class);
				} else {
					mInterPasswordDialog.dismiss();
					Toast.makeText(HomeActivity.this, "�����������������룡",
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void cancle() {
				mInterPasswordDialog.dismiss();
			}
		});
		mInterPasswordDialog.setCancelable(true);
		// �öԻ�����ʾ
		mInterPasswordDialog.show();
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ��sp�д��������
	 */
	private String getPassword() {
		String password = msharedPreferences.getString("PhoneAntiTheftPWD",
				null);
		if (TextUtils.isEmpty(password)) {
			return "";
		}
		return password;
	}

	/**
	 * �ж��û��Ƿ����ù��ֻ���������
	 * 
	 * @return sp��PhoneAntiTheftPWD�����򷵻�true
	 */
	protected boolean isSetUpPassword() {
		String password = msharedPreferences.getString("PhoneAntiTheftPWD",
				null);
		if (TextUtils.isEmpty(password)) {
			return false;
		}
		return true;
	}

	/**
	 * �����µ�Activity���ر��Լ�
	 * 
	 * @param cls
	 */
	public void startActivity(Class<?> cls) {
		Intent intent = new Intent(HomeActivity.this, cls);
		startActivity(intent);
	}

	/**
	 * ����ʵ�ְ����η��ؼ��˳�����
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		long mExitTime = System.currentTimeMillis();
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) < 2000) {
				System.exit(0);
			} else {
				Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
