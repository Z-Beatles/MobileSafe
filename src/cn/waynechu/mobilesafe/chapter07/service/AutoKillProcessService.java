package cn.waynechu.mobilesafe.chapter07.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class AutoKillProcessService extends Service {

	private ScreenLockReceiver receiver;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		receiver = new ScreenLockReceiver();
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(receiver);
		receiver = null;
		super.onDestroy();
	}

	class ScreenLockReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			for (RunningAppProcessInfo info : am.getRunningAppProcesses()) {
				String packname = info.processName;
				am.killBackgroundProcesses(packname);
			}
		}
	}
}
