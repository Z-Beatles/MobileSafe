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
 * DAO(Data Access Object) -- 数据库访问对象
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
	 * 添加数据
	 * 
	 * @param blackContactInfo
	 * @return
	 */
	public boolean add(BlackContactInfo blackContactInfo) {
		// 创建一个SQLite数据库
		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		// ContentValues和HashTable类似都是一种储存机制，最大的区别在于ContentValues只能储存基本数据类型，而HashTable却可以储存对象。
		// 插入成功返回这条记录的ID，否则返回-1
		ContentValues values = new ContentValues();
		if (blackContactInfo.phoneNumber.startsWith("+86")) {
			// 截取+86后的字符串
			blackContactInfo.phoneNumber = blackContactInfo.phoneNumber
					.substring(3, blackContactInfo.phoneNumber.length());
		}
		values.put("number", blackContactInfo.phoneNumber);
		values.put("name", blackContactInfo.contactName);
		values.put("mode", blackContactInfo.mode);
		long rowid = db.insert("blacknumber", null, values);
		if (rowid == -1) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 删除数据
	 * 
	 * @param blackContactInfo
	 * @return
	 */
	public boolean delete(BlackContactInfo blackContactInfo) {
		SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
		int rownumber = db.delete("blacknumber", "number=?",
				new String[] { blackContactInfo.phoneNumber });
		if (rownumber == 0) {
			// 删除失败返回 0
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获取数据库的总条目个数
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
	 * 分页查询数据库的记录
	 * 
	 * @param pagenumber 页码，从0开始
	 * @param pagesize 页面大小
	 * @return mBlackContactInfos(ArrayList<BlackContactInfo>类型)
	 */
	public List<BlackContactInfo> getPageBlackNumber(int pagenumber,
			int pagesize) {
		SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
		/*
		 * rawQuery()的第二个参数为占位符，如果没有，可以设置为null
		 * limit 6 offset 12(2*6) ------  表示从第12条开始读取6条
		 * 也可以表示为 limit 12,6
		 */
		Cursor cursor = db.rawQuery(
				"select number,mode,name from blacknumber limit ? offset ?",
				new String[] { String.valueOf(pagesize),
						String.valueOf(pagesize * pagenumber) });
		/*
		 * List是一个接口，ArrayList、Vector、LinkedList实现这个接口 多态，父类的引用指向了子类
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
}
