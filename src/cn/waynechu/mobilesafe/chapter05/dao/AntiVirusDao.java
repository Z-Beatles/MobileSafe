package cn.waynechu.mobilesafe.chapter05.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 检查某个md5是否是病毒
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
		Cursor cursor = db.rawQuery("selece desc from database where md5=?",
				new String[] { md5 });
		if (cursor.moveToNext()) {
			desc = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return desc;
	}

}
