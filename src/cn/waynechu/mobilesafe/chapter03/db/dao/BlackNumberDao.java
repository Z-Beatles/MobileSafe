package cn.waynechu.mobilesafe.chapter03.db.dao;

import java.util.ArrayList;
import java.util.List;
import cn.waynechu.mobilesafe.chapter03.db.BlackNumberOpenHelper;
import cn.waynechu.mobilesafe.chapter03.entity.BlackContactInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

/**
 * DAO(Data Access Object) -- ���ݿ���ʶ���
 * 
 * @author waynechu
 * 
 */
public class BlackNumberDao {
	private BlackNumberOpenHelper blackNumberOpenHelper;

	public BlackNumberDao(Context context) {
		super();
		blackNumberOpenHelper = new BlackNumberOpenHelper(context);
	}

	/**
	 * �������
	 * 
	 * @param blackContactInfo
	 * @return
	 */
	public boolean add(BlackContactInfo blackContactInfo) {
		// ����һ��SQLite���ݿ�
		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		// ContentValues��HashTable���ƶ���һ�ִ�����ƣ�������������ContentValuesֻ�ܴ�������������ͣ���HashTableȴ���Դ������
		// ����ɹ�����������¼��ID�����򷵻�-1
		ContentValues values = new ContentValues();
		if (blackContactInfo.phoneNumber.startsWith("+86")) {
			// substring()������ȡ��+86����ַ���
			blackContactInfo.phoneNumber = blackContactInfo.phoneNumber
					.substring(3, blackContactInfo.phoneNumber.length());
		}
		values.put("number", blackContactInfo.phoneNumber);
		values.put("name", blackContactInfo.contactName);
		values.put("mode", blackContactInfo.mode);
		long rowid = db.insert("blacknumber", null, values);
		if (rowid == -1) {
			return false;// ���ݲ��벻�ɹ�
		} else {
			return true;
		}
	}

	/**
	 * ɾ������
	 * 
	 * @param blackContactInfo
	 * @return ɾ��ʧ�ܷ��� 0
	 */
	public boolean delete(BlackContactInfo blackContactInfo) {
		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		int rownumber = db.delete("blacknumber", "number=?",
				new String[] { blackContactInfo.phoneNumber });
		if (rownumber == 0) {
			// ɾ��ʧ�ܷ��� 0
			return false;
		} else {
			return true;
		}
	}

	/**
	 * ��ȡ���ݿ������Ŀ����
	 * 
	 * @return
	 */
	public int getTotalNumber() {
		SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from blacknumber", null);
		cursor.moveToNext();
		int count = cursor.getInt(0);
		cursor.close();
		db.close();
		return count;
	}

	/**
	 * ��ҳ��ѯ���ݿ�ļ�¼
	 * 
	 * @param pagenumber-ҳ�룬��0��ʼ
	 * @param pagesize-ҳ���С
	 * @return mBlackContactInfos(ArrayList<BlackContactInfo>����)
	 */
	public List<BlackContactInfo> getPageBlackNumber(int pagenumber,
			int pagesize) {
		SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
		/*
		 * rawQuery()�ĵڶ�������Ϊռλ�������û�У���������Ϊnull 
		 * limit 6 offset 12(2*6) --- ��ʾ�ӵ�12����ʼ��ȡ6�� 
		 * Ҳ���Ա�ʾΪ limit 12,6
		 */
		Cursor cursor = db.rawQuery(
				"select number,mode,name from blacknumber limit ? offset ?",
				new String[] { String.valueOf(pagesize),
						String.valueOf(pagesize * pagenumber) });
		/*
		 * List��һ���ӿڣ�ArrayList��Vector��LinkedListʵ������ӿ� ��̬�����������ָ��������
		 */
		List<BlackContactInfo> mBlackContactInfos = new ArrayList<BlackContactInfo>();
		while (cursor.moveToNext()) {
			BlackContactInfo info = new BlackContactInfo();
			info.phoneNumber = cursor.getString(0);
			info.mode = cursor.getInt(1);
			info.contactName = cursor.getString(2);
			mBlackContactInfos.add(info);
		}
		cursor.close();
		db.close();
		SystemClock.sleep(30);
		return mBlackContactInfos;
	}

	/**
	 * �жϺ��������Ƿ���ڸú���
	 * 
	 * @param number
	 * @return
	 */
	public boolean isNumberExist(String number) {
		SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("blacknumber", null, "number=?",
				new String[] { number }, null, null, null);
		if (cursor.moveToNext()) {
			cursor.close();
			db.close();
			return true;
		}
		cursor.close();
		db.close();
		return false;
	}

	/**
	 * ���ݺ����ѯ��������Ϣ
	 * 
	 * @param number
	 * @return
	 */
	public int getBlackContactMode(String number) {
		SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("blacknumber", new String[] { "mode" },
				"number=?", new String[] { number }, null, null, null);
		int mode = 0;
		if (cursor.moveToNext()) {
			mode = cursor.getInt(cursor.getColumnIndex("mode"));
		}
		cursor.close();
		db.close();
		return mode;
	}
}
