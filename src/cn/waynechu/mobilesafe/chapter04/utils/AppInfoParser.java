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
 * ��ȡ�ֻ��������Ӧ�ó������Ϣ
 * 
 * @author waynechu
 * 
 */
public class AppInfoParser {
	public static List<AppInfo> getAppInfos(Context context) {
		// �õ���������
		PackageManager pm = context.getPackageManager();
		// ��ȡ���Ѱ�װ��Ӧ�ó���İ���Ϣ
		List<PackageInfo> packInfos = pm.getInstalledPackages(0);
		// ���ڴ����ȡ����Ӧ�ó�����Ϣ
		List<AppInfo> appinfos = new ArrayList<AppInfo>();
		for (PackageInfo packInfo : packInfos) {
			AppInfo appinfo = new AppInfo();
			String packname = packInfo.packageName;
			appinfo.packageName = packname;
			Drawable icon = packInfo.applicationInfo.loadIcon(pm);
			appinfo.icon = icon;
			String appname = packInfo.applicationInfo.loadLabel(pm).toString();
			appinfo.appName = appname;
			// Ӧ�ó���apk����·��
			String apkpath = packInfo.applicationInfo.sourceDir;
			appinfo.apkPath = apkpath;
			File file = new File(apkpath);
			long appSize = file.length();
			appinfo.appSize = appSize;
			// Ӧ�ó���װ��λ��
			int flags = packInfo.applicationInfo.flags;
			if ((ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags) != 0) {
				// �ⲿ����
				appinfo.isInRoom = false;
			} else {
				// �ֻ��ڴ�
				appinfo.isInRoom = true;
			}
			// �Ƿ���ϵͳӦ��
			if ((ApplicationInfo.FLAG_SYSTEM & flags) != 0) {
				// ϵͳӦ��
				appinfo.isUserApp = false;
			} else {
				// �û�Ӧ��
				appinfo.isUserApp = true;
			}
			appinfos.add(appinfo);
			appinfo = null;
		}
		return appinfos;
	}
}
