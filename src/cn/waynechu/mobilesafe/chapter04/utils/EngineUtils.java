package cn.waynechu.mobilesafe.chapter04.utils;

import cn.waynechu.mobilesafe.chapter04.entity.AppInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;
import com.stericson.RootTools.RootTools;

/**
 * ҵ�񹤾���
 * 
 * @author waynechu
 * 
 */
public class EngineUtils {
	/**
	 * ����Ӧ��
	 * 
	 * @param context
	 * @param appinfo
	 */
	public static void shareApplication(Context context,AppInfo appInfo) {
		Intent intent = new Intent("android.intent.action.SEND");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT,
				"�Ƽ���ʹ��һ����������ƽУ�" + appInfo.appName
						+ "����·����https://www.topblog.top/download/mobilesafe");
		context.startActivity(intent);
	}

	/**
	 * ����Ӧ�ó���
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
			Toast.makeText(context, "��Ӧ������������", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * ����Ӧ������ҳ��
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
	 * ж��Ӧ�ã�����Build Path�����RootTools���߰�
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
			// ϵͳ��Ҫ��ȡrootȨ�ޣ�����linux����ɾ���ļ�
			if (!RootTools.isRootAvailable()) {
				Toast.makeText(context, "ж��ϵͳӦ�ã���Ҫ��ȡrootȨ�ޣ�",
						Toast.LENGTH_SHORT).show();
				return;
			}
			try {
				if (!RootTools.isAccessGiven()) {
					Toast.makeText(context, "�������ֻ���ȫ��ʿrootȨ�ޣ�",
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
