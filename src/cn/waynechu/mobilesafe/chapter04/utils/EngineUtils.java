package cn.waynechu.mobilesafe.chapter04.utils;

import cn.waynechu.mobilesafe.chapter04.entity.AppInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;
import com.stericson.RootTools.RootTools;

/**
 * 业务工具类
 * 
 * @author waynechu
 * 
 */
public class EngineUtils {
	/**
	 * 分享应用
	 * 
	 * @param context
	 * @param appinfo
	 */
	public static void shareApplication(Context context,AppInfo appInfo) {
		Intent intent = new Intent("android.intent.action.SEND");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT,
				"推荐您使用一款软件，名称叫：" + appInfo.appName
						+ "下载路径：https://www.topblog.top/download/mobilesafe");
		context.startActivity(intent);
	}

	/**
	 * 开启应用程序
	 * 
	 * @param context
	 * @param appInfo
	 */
	public static void startApplication(Context context, AppInfo appInfo) {
		PackageManager pm = context.getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage(appInfo.packageName);
		if (intent != null) {
			context.startActivity(intent);
		} else {
			Toast.makeText(context, "该应用无启动界面", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 开启应用设置页面
	 * 
	 * @param context
	 * @param appInfo
	 */
	public static void settingAppDetail(Context context, AppInfo appInfo) {
		Intent intent = new Intent();
		intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setData(Uri.parse("package:" + appInfo.packageName));
		context.startActivity(intent);
	}

	/**
	 * 卸载应用，需在Build Path中添加RootTools工具包
	 * 
	 * @param context
	 * @param appInfo
	 */
	public static void uninstallApplication(Context context, AppInfo appInfo) {
		if (appInfo.isUserApp) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DELETE);
			intent.setData(Uri.parse("package:" + appInfo.packageName));
			context.startActivity(intent);
		} else {
			// 系统需要获取root权限，利用linux命令删除文件
			if (!RootTools.isRootAvailable()) {
				Toast.makeText(context, "卸载系统应用，需要获取root权限！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			try {
				if (!RootTools.isAccessGiven()) {
					Toast.makeText(context, "请授予手机安全卫士root权限！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				RootTools.sendShell("mount -o remount ,rw /system", 3000);
				RootTools.sendShell("rm -r/" + appInfo.apkPath, 3000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
