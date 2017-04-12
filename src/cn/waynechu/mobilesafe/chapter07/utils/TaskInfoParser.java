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
 * ������Ϣ�Ľ�����
 * 
 * @author waynechu
 * 
 */
public class TaskInfoParser {
	public static List<TaskInfo> getRunningTaskInfos(Context context) {
		// ��ȡ���̹�������Ȼ��ͨ�����̹�������ȡ�������еĳ���
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
		// TODO GUG:getRunningAppProcesses()����ֻ���ص�ǰӦ�ñ���
		
		// ��ȡ��������
		PackageManager pm = context.getPackageManager();
		// �ü������ڴ�����̶���
		List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		// ���������������еĳ���
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
					// ϵͳ����
					taskInfo.isUserApp = false;
				} else {
					// �û�����
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
			 * System.out.println("���̸�����" + processInfos.size());
			 * System.out.println("������Ϣ��" + taskInfo.toString());
			 */
		}
		return taskInfos;
	}
}
