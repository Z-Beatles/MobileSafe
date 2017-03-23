package cn.waynechu.mobilesafe.chapter04.entity;

import android.graphics.drawable.Drawable;

/**
 * Ӧ�ó�����Ϣʵ����
 * 
 * @author waynechu
 * 
 */
public class AppInfo {
	/** Ӧ�ó������ */
	public String packageName;
	/** Ӧ�ó���ͼ�� */
	public Drawable icon;
	/** Ӧ�ó���ͼ�� */
	public String appName;
	/** Ӧ�ó���·�� */
	public String apkPath;
	/** Ӧ�ó����С */
	public long appSize;
	/** �Ƿ����ֻ��ڴ� */
	public boolean isInRoom;
	/** �Ƿ����û�Ӧ�� */
	public boolean isUserApp;
	/** �Ƿ�ѡ�У�Ĭ�϶�Ϊfalse */
	public boolean isSelected = false;

	/** �õ�Appλ���ַ��� */
	public String getAppLocation(boolean isInRoom) {
		if (isInRoom) {
			return "�ֻ��ڴ�";
		} else {
			return "�ⲿ�洢";
		}
	}
}
