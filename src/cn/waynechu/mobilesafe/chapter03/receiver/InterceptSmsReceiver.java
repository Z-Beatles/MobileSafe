package cn.waynechu.mobilesafe.chapter03.receiver;

import cn.waynechu.mobilesafe.chapter03.db.dao.BlackNumberDao;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * 广播接收者用于拦截短信
 * 
 * @author Administrator
 * 
 */
public class InterceptSmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		SharedPreferences mSharedPreferences = context.getSharedPreferences(
				"config", Context.MODE_PRIVATE);
		boolean BlackNumStatus = mSharedPreferences.getBoolean(
				"BlackNumStatus", true);
		if (!BlackNumStatus) {
			// 黑名单拦截关闭
			Log.i("拦截短信", "黑名单拦截关闭");
			return;
		}
		Log.i("拦截短信", "黑名单拦截已经打开");
		// 如果是黑名单，则终止广播
		BlackNumberDao dao = new BlackNumberDao(context);
		// 用于获取接收到的信息
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		for (Object obj : objs) {
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
			String sender = smsMessage.getOriginatingAddress();
			//String body = smsMessage.getMessageBody();
			if (sender.startsWith("+86")) {
				sender = sender.substring(3, sender.length());
			}
			int mode = dao.getBlackContactMode(sender);
			// 如果是短信拦截或者是全部拦截，则拦截该短信
			if (mode == 2 || mode == 3) {
				abortBroadcast();
			}
		}
	}

}
