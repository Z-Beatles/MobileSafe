package cn.waynechu.mobilesafe.chapter03.entity;

public class BlackContactInfo {
	public String phoneNumber;
	public String contactName;
	public int mode;
	public String getModeString(int mode){
		switch(mode){
		case 1:
			return "�绰����";
		case 2:
			return "��������";
		case 3:
			return "�绰����������";
		}
		return "";
	}
}
