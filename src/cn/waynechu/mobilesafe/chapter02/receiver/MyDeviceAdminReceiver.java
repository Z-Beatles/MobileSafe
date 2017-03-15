package cn.waynechu.mobilesafe.chapter02.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;

public class MyDeviceAdminReceiver extends DeviceAdminReceiver {
    // 定义超级管理员的广播接收者，只有超级管理员才能远程清除数据和远程锁屏以及更改锁屏密码
    @Override
    public void onReceive(Context context, Intent intent) {
    }
}
