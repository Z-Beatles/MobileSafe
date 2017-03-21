package cn.waynechu.mobilesafe.chapter04.entity;

import android.graphics.drawable.Drawable;

/**
 * 应用程序信息实体类
 * 
 * @author hp
 * 
 */
public class AppInfo {
	/** 应用程序包名 */
	public String packageName;
	/** 应用程序图标 */
	public Drawable icon;
	/** 应用程序图标 */
	public String appName;
	/** 应用程序路径 */
	public String apkPath;
	/** 应用程序大小 */
	public long appSize;
	/** 是否是手机内存 */
	public boolean isInRoom;
	/** 是否是用户应用 */
	public boolean isUserApp;
	/** 是否选中，默认都为false */
	public boolean isSelected = false;

	/** 拿到App位置字符串 */
	public String getAppLocation(boolean isInRoom) {
		if (isInRoom) {
			return "手机内存";
		} else {
			return "外部存储";
		}
	}
}
