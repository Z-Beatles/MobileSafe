package cn.waynechu.mobilesafe.chapter02.receiver;

import cn.waynechu.mobilesafe.App;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * �������������Ĺ㲥�����ߣ���Ҫ���ڼ��SIM�Ƿ񱻸�����������������Ͷ��Ÿ��󶨵İ�ȫ����
 * 
 * @author waynechu
 * 
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent arg1) {
        ((App) context.getApplicationContext()).correctSIM();// ��ʼ��
    }
}
