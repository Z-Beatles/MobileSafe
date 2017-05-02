package cn.waynechu.mobilesafe.chapter08.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cn.waynechu.mobilesafe.chapter08.service.TrafficMonitoringService;
import cn.waynechu.mobilesafe.chapter08.utils.SystemInfoUtils;

/** ���������Ĺ㲥���࣬�������ݿ⣬�������� */
public class BootCompleteReciever extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// �����㲥�������ж�������ط����Ƿ���
		if (!SystemInfoUtils
				.isServiceRunning(context,
						"cn.waynechu.mobilesafe.chapter08.service.TrafficMonitoringService")) {
			context.startService(new Intent(context,
					TrafficMonitoringService.class));
		}
	}
}
