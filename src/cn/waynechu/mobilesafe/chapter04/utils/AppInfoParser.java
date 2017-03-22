package cn.waynechu.mobilesafe.chapter04.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import cn.waynechu.mobilesafe.chapter04.entity.AppInfo;

/**
 * 获取手机里的所有应用程序的信息
 * 
 * @author waynechu
 * 
 */
public class AppInfoParser {
	public static List<AppInfo> getAppInfos(Context context) {
		// 得到包管理器
		PackageManager pm = context.getPackageManager();
		// 获取到已安装的应用程序的包信息
		List<PackageInfo> packInfos = pm.getInstalledPackages(0);
		// 用于储存获取到的应用程序信息
		List<AppInfo> appinfos = new ArrayList<AppInfo>();
		for (PackageInfo packInfo : packInfos) {
			AppInfo appinfo = new AppInfo();
			String packname = packInfo.packageName;
			appinfo.packageName = packname;
			Drawable icon = packInfo.applicationInfo.loadIcon(pm);
			appinfo.icon = icon;
			String appname = packInfo.applicationInfo.loadLabel(pm).toString();
			appinfo.appName = appname;
			// 应用程序apk包的路径
			String apkpath = packInfo.applicationInfo.sourceDir;
			appinfo.apkPath = apkpath;
			File file = new File(apkpath);
			long appSize = file.length();
			appinfo.appSize = appSize;
			// 应用程序安装的位置
			int flags = packInfo.applicationInfo.flags;
			if ((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags) != 0) {
				// 外部储存
				appinfo.isInRoom = false;
			} else {
				// 手机内存
				appinfo.isInRoom = true;
			}
			// 是否是系统应用
			if ((ApplicationInfo.FLAG_SYSTEM & flags) != 0) {
				// 系统应用
				appinfo.isUserApp = false;
			} else {
				// 用户应用
				appinfo.isUserApp = true;
			}
			appinfos.add(appinfo);
			appinfo = null;
		}
		return appinfos;
	}
}
