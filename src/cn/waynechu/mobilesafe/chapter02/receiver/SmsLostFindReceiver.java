package cn.waynechu.mobilesafe.chapter02.receiver;

import cn.waynechu.mobilesafe.R;
import cn.waynechu.mobilesafe.chapter02.service.GPSLocationService;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

public class SmsLostFindReceiver extends BroadcastReceiver {
	private static final String TAG = SmsLostFindReceiver.class.getSimpleName();
	private SharedPreferences sharedPreferences;

	@Override
	public void onReceive(Context context, Intent intent) {
		sharedPreferences = context.getSharedPreferences("config",
				Activity.MODE_PRIVATE);
		boolean protecting = sharedPreferences.getBoolean("protecting", true);
		// 防盗保护开启
		if (protecting) {
			// 获取超级管理员
			DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context
					.getSystemService(Context.DEVICE_POLICY_SERVICE);
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				String sender = smsMessage.getOriginatingAddress();
				// 截取电话号码
				if (sender.startsWith("+86")) {
					sender = sender.substring(3, sender.length());
				}
				String body = smsMessage.getMessageBody();
				String safephone = sharedPreferences.getString("safephone",
						null);
				// 如果该短信是安全号码发送的
				if (!TextUtils.isEmpty(safephone) & sender.equals(safephone)) {
					if ("#*location*#".equals(body)) {
						Log.i(TAG, "返回位置信息");
						// 获取位置 在GPSLocationService服务中实现
						Intent service = new Intent(context,
								GPSLocationService.class);
						context.startService(service);
						abortBroadcast();
					} else if ("#*alarm*#".equals(body)) {
						Log.i(TAG, "播放报警音乐");
						MediaPlayer player = MediaPlayer.create(context,
								R.raw.alarm);
						// 设置声音大小
						player.setVolume(1.0f, 1.0f);
						player.start();
						abortBroadcast();
					} else if ("#*wipedate*#".equals(body)) {
						Log.i(TAG, "远程清除数据");
						devicePolicyManager
								.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
						abortBroadcast();
					} else if ("#*lockscreen*#".equals(body)) {
						Log.i("TAG", "远程锁屏");
						devicePolicyManager.resetPassword("123456", 0);
						devicePolicyManager.lockNow();
						abortBroadcast();
					}
				}
			}
		}

	}

}
