package cn.waynechu.mobilesafe.chapter01.utils;

import java.io.File;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;

public class MyUtils {
    /**
     * ��ȡӦ�ð汾��
     * 
     * @param context
     * @return versionName
     */
    public static String getVersion(Context context) {
        // PackageManager ���Ի�ȡ�嵥�ļ��е�������Ϣ
        PackageManager manager = context.getPackageManager();
        try {
            // getPackageName()��ȡ����ǰ����İ���
            PackageInfo packageInfo = manager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * ��װ�°汾
     * 
     * @param activity
     */
    public static void installApk(Activity activity) {
        /*
         * String android.intent.action.VIEW ������ʾ�û������ݡ�
         * �Ƚ�ͨ�ã�������û����������ʹ���Ӧ��Activity�� ����
         * tel:15500000000�򿪲��ų���http://www.waynechu.cn ����������ȡ�
         */
        Intent intent = new Intent("android.intent.action.VIEW");
        // ���Ĭ�Ϸ���
        intent.addCategory("android.intent.category.DEFAULT");
        // �������ݺ�����
        intent.setDataAndType(
                Uri.fromFile(new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/mobilesafe.apk")),
                "application/vnd.android.package-archive");
        activity.startActivityForResult(intent, 0);
    }
}
