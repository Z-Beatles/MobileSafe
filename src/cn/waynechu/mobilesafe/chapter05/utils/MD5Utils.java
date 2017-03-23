package cn.waynechu.mobilesafe.chapter05.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * ��ȡ�ļ���md5ֵ
 * 
 * @author waynechu
 */
public class MD5Utils {
	/**
	 * getFileMd5��������һ��Ӧ�õ�ȫ��·������󷵻ظ�Ӧ�õ�md5ֵ
	 * 
	 * @param pathӦ�õ�ȫ��·��
	 * @return md5ֵ
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
