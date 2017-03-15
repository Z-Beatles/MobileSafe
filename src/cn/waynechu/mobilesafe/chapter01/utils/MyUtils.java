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
     * 获取应用版本号
     * 
     * @param context
     * @return versionName
     */
    public static String getVersion(Context context) {
        // PackageManager 可以获取清单文件中的所有信息
        PackageManager manager = context.getPackageManager();
        try {
            // getPackageName()获取到当前程序的包名
            PackageInfo packageInfo = manager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 安装新版本
     * 
     * @param activity
     */
    public static void installApk(Activity activity) {
        /*
         * String android.intent.action.VIEW 用于显示用户的数据。
         * 比较通用，会根据用户的数据类型打开相应的Activity。 比如
         * tel:15500000000打开拨号程序，http://www.waynechu.cn 则会打开浏览器等。
         */
        Intent intent = new Intent("android.intent.action.VIEW");
        // 添加默认分类
        intent.addCategory("android.intent.category.DEFAULT");
        // 设置数据和类型
        intent.setDataAndType(
                Uri.fromFile(new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/mobilesafe.apk")),
                "application/vnd.android.package-archive");
        activity.startActivityForResult(intent, 0);
    }
}
