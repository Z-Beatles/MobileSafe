package cn.waynechu.mobilesafe.chapter05.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * ���ĳ��md5�Ƿ��ǲ���
 * 
 * @author waynechu
 * 
 */
public class AntiVirusDao {
	public static String checkVirus(String md5) {
		String desc = null;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(
				"/data/data/cn.waynechu.mobilesafe/files/antivirus.db", null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select desc from datable where md5=?",
				new String[] { md5 });
		if (cursor.moveToNext()) {
			desc = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return desc;
	}

}
