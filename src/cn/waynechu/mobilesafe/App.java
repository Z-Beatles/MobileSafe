package cn.waynechu.mobilesafe;

import org.xutils.x;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * �Զ����һ��Application��,���SIM���Ƿ����仯
 * 
 * @author waynechu
 * 
 */
// ��Android��������ʱϵͳ�ᴴ��һ��Application���������洢ϵͳ��һЩ��Ϣ
public class App extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// ��application��onCreate�г�ʼ��xutil
		x.Ext.init(this);
		// �Ƿ����debug��־, ����debug��Ӱ������.
		x.Ext.setDebug(BuildConfig.DEBUG);
		correctSIM();
	}

	public void correctSIM() {
		// TODO Auto-generated method stub
		// ���SIM���Ƿ����仯
		SharedPreferences sp = getSharedPreferences("config",
				Context.MODE_PRIVATE);
		// ��ȡ����������״̬
		boolean protecting = sp.getBoolean("protecting", true);
		if (protecting) {
			// �õ��󶨵�SIM������
			String bindsim = sp.getString("sim", "");
			// �õ��ֻ����ڵ�SIM������
			TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String realsim = tm.getSimSerialNumber();
			if (bindsim.equals(realsim)) {
				Log.i("", "sim��δ�����仯�����������ֻ���");
			} else {
				Log.i("", "sim�������˱仯��");
				String safenumber = sp.getString("safephone", "");
				if (!TextUtils.isEmpty(safenumber)) {
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(safenumber, null,
							"�����ѵ��ֻ�SIM���ѱ�������", null, null);
				}
			}
		}
	}

}
