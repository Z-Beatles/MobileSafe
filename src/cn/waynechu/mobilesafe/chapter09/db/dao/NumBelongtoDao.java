package cn.waynechu.mobilesafe.chapter09.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/** ��ѯ��������ص����ݿ��߼��� */
public class NumBelongtoDao {

	/**
	 * ���ص绰���������
	 * 
	 * @param phonenumber
	 * @return location
	 */
	public static String getLocation(String phonenumber) {

		String location = phonenumber;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(
				"/data/data/cn.waynechu.mobilesafe/files/address.db", null,
				SQLiteDatabase.OPEN_READONLY);
		// ����ƥ����1��ͷ���ڶ�λΪ34578������λΪ1-9����Ϊ�ս��ַ�
		if (phonenumber.matches("^1[34578]\\d{9}$")) {
			Cursor cursor = db
					.rawQuery(
							"select location from data2 where id=(select outkey from data1 where id=?)",
							new String[] { phonenumber.substring(0, 7) });
			if (cursor.moveToNext()) {
				location = cursor.getString(0);
			}
			cursor.close();
		} else {
			switch (phonenumber.length()) {
			// �жϵ绰����ĳ���
			case 3:
				if ("110".equals(phonenumber)) {
					location = "�˾�";
				} else if ("120".equals(phonenumber)) {
					location = "����";
				} else {
					location = "��������";
				}
				break;
			case 4:
				location = "ģ����";
				break;
			case 5:
				location = "�ͷ��绰";
				break;
			case 7:
				location = "���ص绰";
				break;
			case 8:
				location = "���ص绰";
				break;
			default:
				if (location.length() >= 9 && location.startsWith("0")) {
					String address = null;
					Cursor cursor = db.rawQuery(
							"select location from data2 where area = ?",
							new String[] { location.substring(1, 3) });
					if (cursor.moveToNext()) {
						String str = cursor.getString(0);
						address = str.substring(0, str.length() - 2);
					}
					cursor.close();
					cursor = db.rawQuery(
							"select location from data2 where area = ?",
							new String[] { location.substring(1, 4) });
					if (cursor.moveToNext()) {
						String str = cursor.getString(0);
						address = str.substring(0, str.length() - 2);
					}
					cursor.close();
					if (!TextUtils.isEmpty(address)) {
						location = address;
					}
				}
				break;
			}
		}
		db.close();
		return location;
	}
}
