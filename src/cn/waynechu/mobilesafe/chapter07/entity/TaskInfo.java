package cn.waynechu.mobilesafe.chapter07.entity;

import android.graphics.drawable.Drawable;

/** �������е�App����Ϣʵ���� */
public class TaskInfo {
	// APP����
	public String appName;
	// ����ͼ��
	public Drawable appIcon;
	// Ӧ�ð���
	public String packageName;
	// ռ���ڴ�
	public long appMemory;
	// ���ڱ��Ӧ���Ƿ�ѡ��
	public boolean isChecked;
	// �û�����(true)
	public boolean isUserApp;
	
	@Override
	public String toString() {
		return "TaskInfo [appName=" + appName + ", appIcon=" + appIcon
				+ ", packageName=" + packageName + ", appMemory=" + appMemory
				+ ", isChecked=" + isChecked + ", isUserApp=" + isUserApp + "]";
	}
	
}
