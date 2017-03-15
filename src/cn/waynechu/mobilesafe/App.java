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
 * 自定义的一个Application类,检查SIM卡是否发生变化
 * 
 * @author waynechu
 * 
 */
// 当Android程序启动时系统会创建一个Application对象，用来存储系统的一些信息
public class App extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// 在application的onCreate中初始化xutil
		x.Ext.init(this);
		// 是否输出debug日志, 开启debug会影响性能.
		x.Ext.setDebug(BuildConfig.DEBUG);
		correctSIM();
	}

	public void correctSIM() {
		// TODO Auto-generated method stub
		// 检查SIM卡是否发生变化
		SharedPreferences sp = getSharedPreferences("config",
				Context.MODE_PRIVATE);
		// 获取防盗保护的状态
		boolean protecting = sp.getBoolean("protecting", true);
		if (protecting) {
			// 得到绑定的SIM卡串号
			String bindsim = sp.getString("sim", "");
			// 得到手机现在的SIM卡串号
			TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String realsim = tm.getSimSerialNumber();
			if (bindsim.equals(realsim)) {
				Log.i("", "sim卡未发生变化，还是您的手机！");
			} else {
				Log.i("", "sim卡发生了变化！");
				String safenumber = sp.getString("safephone", "");
				if (!TextUtils.isEmpty(safenumber)) {
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(safenumber, null,
							"您亲友的手机SIM卡已被更换！", null, null);
				}
			}
		}
	}

}
