package cn.waynechu.mobilesafe.chapter04.utils;

import android.content.Context;

/**
 * 用于dip和px之间转换的工具类
 * 在xUtil框架里也有实现该方法
 * 
 * @author waynechu
 * 
 */
public class DensityUtil {
	/**
	 * dip转换为像素px
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
	 * 像素px转换为dip
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
