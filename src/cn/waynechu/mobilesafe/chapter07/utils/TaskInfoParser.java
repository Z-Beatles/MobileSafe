package cn.waynechu.mobilesafe.chapter07.utils;

import java.util.ArrayList;
import java.util.List;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;
import cn.waynechu.mobilesafe.R;
import cn.waynechu.mobilesafe.chapter07.entity.TaskInfo;

/**
 * 进程信息的解析器
 * 
 * @author waynechu
 * 
 */
public class TaskInfoParser {
	public static List<TaskInfo> getRunningTaskInfos(Context context) {
		// 获取进程管理器，然后通过进程管理器获取正在运行的程序
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
		// TODO GUG:getRunningAppProcesses()方法只返回当前应用本身
		
		// 获取包管理器
		PackageManager pm = context.getPackageManager();
		// 该集合用于储存进程对象
		List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		// 遍历所有正在运行的程序
		for (RunningAppProcessInfo processInfo : processInfos) {
			String packname = processInfo.processName;
			TaskInfo taskInfo = new TaskInfo();
			taskInfo.packageName = packname;
			MemoryInfo[] memoryInfos = am
					.getProcessMemoryInfo(new int[] { processInfo.pid });
			long memsize = memoryInfos[0].getTotalPrivateDirty() * 1024;
			taskInfo.appMemory = memsize;
			try {
				PackageInfo packInfo = pm.getPackageInfo(packname, 0);
				Drawable icon = packInfo.applicationInfo.loadIcon(pm);
				taskInfo.appIcon = icon;
				String appname = packInfo.applicationInfo.loadLabel(pm)
						.toString();
				taskInfo.appName = appname;
				if ((ApplicationInfo.FLAG_SYSTEM & packInfo.applicationInfo.flags) != 0) {
					// 系统进程
					taskInfo.isUserApp = false;
				} else {
					// 用户进程
					taskInfo.isUserApp = true;
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				taskInfo.appName = packname;
				taskInfo.appIcon = context.getResources().getDrawable(
						R.drawable.ic_default);
			}
			taskInfos.add(taskInfo);
			/*
			 * System.out.println("进程个数：" + processInfos.size());
			 * System.out.println("进程信息：" + taskInfo.toString());
			 */
		}
		return taskInfos;
	}
}
