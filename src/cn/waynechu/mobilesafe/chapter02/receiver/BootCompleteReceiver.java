package cn.waynechu.mobilesafe.chapter02.receiver;

import cn.waynechu.mobilesafe.App;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 监听开机启动的广播接收者，主要用于检查SIM是否被更换，如果被更换则发送短信给绑定的安全号码
 * 
 * @author waynechu
 * 
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent arg1) {
        ((App) context.getApplicationContext()).correctSIM();// 初始化
    }
}
