package cn.waynechu.mobilesafe.chapter03.receiver;

import cn.waynechu.mobilesafe.chapter03.db.dao.BlackNumberDao;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * �㲥�������������ض���
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
			// ���������عر�
			Log.i("���ض���", "���������عر�");
			return;
		}
		Log.i("���ض���", "�����������Ѿ���");
		// ����Ǻ�����������ֹ�㲥
		BlackNumberDao dao = new BlackNumberDao(context);
		// ���ڻ�ȡ���յ�����Ϣ
		Object[] objs = (Object[]) intent.getExtras().get("pdus");
		for (Object obj : objs) {
			SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
			String sender = smsMessage.getOriginatingAddress();
			//String body = smsMessage.getMessageBody();
			if (sender.startsWith("+86")) {
				sender = sender.substring(3, sender.length());
			}
			int mode = dao.getBlackContactMode(sender);
			// ����Ƕ������ػ�����ȫ�����أ������ظö���
			if (mode == 2 || mode == 3) {
				abortBroadcast();
			}
		}
	}

}
