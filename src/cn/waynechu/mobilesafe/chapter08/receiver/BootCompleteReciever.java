package cn.waynechu.mobilesafe.chapter08.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cn.waynechu.mobilesafe.chapter08.service.TrafficMonitoringService;
import cn.waynechu.mobilesafe.chapter08.utils.SystemInfoUtils;

/** 监听开机的广播该类，更新数据库，开启服务 */
public class BootCompleteReciever extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// 开机广播，用于判断流量监控服务是否开启
		if (!SystemInfoUtils
				.isServiceRunning(context,
						"cn.waynechu.mobilesafe.chapter08.service.TrafficMonitoringService")) {
			context.startService(new Intent(context,
					TrafficMonitoringService.class));
		}
	}
}
