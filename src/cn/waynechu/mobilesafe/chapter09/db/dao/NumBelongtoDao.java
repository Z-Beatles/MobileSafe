package cn.waynechu.mobilesafe.chapter09.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/** 查询号码归属地的数据库逻辑类 */
public class NumBelongtoDao {

	/**
	 * 返回电话号码归属地
	 * 
	 * @param phonenumber
	 * @return location
	 */
	public static String getLocation(String phonenumber) {

		String location = phonenumber;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(
				"/data/data/cn.waynechu.mobilesafe/files/address.db", null,
				SQLiteDatabase.OPEN_READONLY);
		// 正则匹配以1开头，第二位为34578，第三位为1-9并且为终结字符
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
			// 判断电话号码的长度
			case 3:
				if ("110".equals(phonenumber)) {
					location = "匪警";
				} else if ("120".equals(phonenumber)) {
					location = "急救";
				} else {
					location = "报警号码";
				}
				break;
			case 4:
				location = "模拟器";
				break;
			case 5:
				location = "客服电话";
				break;
			case 7:
				location = "本地电话";
				break;
			case 8:
				location = "本地电话";
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
