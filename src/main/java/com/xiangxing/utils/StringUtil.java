package com.xiangxing.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 
	 * 判断是否为空
	 * 
	 * @param String
	 * @return boolean true表示String不为空，false表示String为空
	 * @throws 无
	 */
	public static boolean isNotEmpty(String str) {
		if (str == null || str.equals("") || str.equals("null") || str.equals("undefined")) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 判断是否为空
	 * 
	 * @param String
	 * @return boolean true表示String为空，false表示String不为空
	 * @throws 无
	 */
	public static boolean isEmpty(String str) {
		if (str == null || str.equals("") || str.equals("null") || str.equals("undefined")) {
			return true;
		}
		return false;
	}

	/**
	 * 转换字节数组为16进制字串
	 * 
	 * @param b
	 *            字节数组
	 * @return 16进制字串
	 */

	public static String byteArray2HexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byte2HexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byte2HexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String transformSolrMetacharactor(String input) {
		StringBuffer sb = new StringBuffer();
		String regex = "[+\\-&|!(){}\\[\\]^\"~*?:(\\)]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		while (matcher.find()) {
			matcher.appendReplacement(sb, "\\\\" + matcher.group());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 校验手机
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	// public static boolean isMobileNO(String mobiles) {
	// boolean flag = false;
	// try {
	// Pattern p =
	// Pattern.compile("^((13[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
	// Matcher m = p.matcher(mobiles);
	// flag = m.matches();
	// } catch (Exception e) {
	// flag = false;
	// }
	// return flag;
	// }
}
