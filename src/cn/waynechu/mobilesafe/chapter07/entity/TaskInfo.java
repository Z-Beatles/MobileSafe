package cn.waynechu.mobilesafe.chapter07.entity;

import android.graphics.drawable.Drawable;

/** 正在运行的App的信息实体类 */
public class TaskInfo {
	// APP名称
	public String appName;
	// 进程图标
	public Drawable appIcon;
	// 应用包名
	public String packageName;
	// 占用内存
	public long appMemory;
	// 用于标记应用是否选中
	public boolean isChecked;
	// 用户进程(true)
	public boolean isUserApp;
	
	@Override
	public String toString() {
		return "TaskInfo [appName=" + appName + ", appIcon=" + appIcon
				+ ", packageName=" + packageName + ", appMemory=" + appMemory
				+ ", isChecked=" + isChecked + ", isUserApp=" + isUserApp + "]";
	}
	
}
