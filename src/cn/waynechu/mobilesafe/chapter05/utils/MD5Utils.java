package cn.waynechu.mobilesafe.chapter05.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * 获取文件的md5值
 * 
 * @author waynechu
 */
public class MD5Utils {
	/**
	 * getFileMd5方法接受一个应用的全名路径，最后返回该应用的md5值
	 * 
	 * @param path应用的全名路径
	 * @return md5值
	 */
	public static String getFileMd5(String path) {
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			File file = new File(path);
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = fis.read(buffer)) != -1) {
				digest.update(buffer, 0, len);
			}
			byte[] result = digest.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : result) {
				int number = b & 0xff;
				String hex = Integer.toHexString(number);
				if (hex.length() == 1) {
					sb.append("0" + hex);
				} else {
					sb.append(hex);
				}
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
