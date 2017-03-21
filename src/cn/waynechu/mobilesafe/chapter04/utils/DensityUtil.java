package cn.waynechu.mobilesafe.chapter04.utils;

import android.content.Context;

/**
 * ����dp��px֮���ת���Ĺ�����
 * 
 * @author waynechu
 * 
 */
public class DensityUtil {
	/**
	 * dipת��Ϊ����px
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		try {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int) (dpValue * scale + 0.5f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (int) dpValue;
	}

	/**
	 * ����pxת��Ϊdip
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		try {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int) (pxValue / scale + 0.5f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (int) pxValue;
	}
}
