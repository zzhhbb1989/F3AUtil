package f3a.util.data;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 数据单位转换工具类
 * @author Bob Ackles
 *
 */
public class DataUtil {
	
	private static final long TIME_LIMIT = 24*60*60*1000;
	
	public static final String B = "B";
	public static final String KB = "KB";
	public static final String MB = "MB";
	public static final String GB = "GB";
	public static final String TB = "TB";
	
	private static final String[] UNIT = {
		B, KB, MB, GB, TB
	};
	
	public enum UNITS {
		B, KB, MB, GB, TB
	}
	
	public static final String RMB = "￥";
	public static final String SQUARE_METER = "m²";
	
	/**
	 * 获得数据的最大单位表示，如：
	 * <p>93B -> 93B
	 * <p>4*1024B -> 4KB
	 * <p>294*1024*1024B -> 294MB
	 * @param bytes 源数据，单位为byte
	 * @return
	 */
	public static String getMaxUnit(long bytes) {
		return getMaxUnit(bytes, 2);
	}
	
	/**
	 * 获得数据的最大单位表示，如：1024*1024B -> 1MB
	 * @param bytes 源数据，单位为byte
	 * @param scale 保留的小数位数
	 * @return
	 */
	public static String getMaxUnit(long bytes, int scale) {
		return getMaxUnit(bytes, UNITS.B, scale);
	}
	
	/**
	 * 获得数据的最大单位表示，如：1024*1024KB -> 1GB
	 * @param data 源数据
	 * @param initUnit 源数据的单位
	 * @param scale 保留的小数位数
	 * @return
	 */
	public static String getMaxUnit(long data, UNITS initUnit, int scale) {
		int i = 0;
		if (initUnit == UNITS.B) {
			i = 0;
		} else if (initUnit == UNITS.KB) {
			i = 1;
		} else if (initUnit == UNITS.MB) {
			i = 2;
		} else if (initUnit == UNITS.GB) {
			i = 3;
		} else {
			i = 4;
		}
		double tmp = data;
		while (tmp >= 1024 && i < UNIT.length - 1) {
			i++;
			tmp /= 1024;
		}
		if (i > 0) {
			tmp = scaleDouble(tmp, scale);
		}
		return tmp + UNIT[i];
	}
	
	/**
	 * 将数据单位转为byte
	 * @param data
	 * @param units
	 * @return
	 */
	public static long getDataInBytes(long data, UNITS units) {
		int i = 0;
		if (units == UNITS.B) {
			i = 0;
		} else if (units == UNITS.KB) {
			i = 1;
		} else if (units == UNITS.MB) {
			i = 2;
		} else if (units == UNITS.GB) {
			i = 3;
		} else {
			i = 4;
		}
		long result = data;
		while (i > 0) {
			result *= 1024;
		}
		return result;
	}
	
	public static boolean isNew(long time) {
		return System.currentTimeMillis() - time < TIME_LIMIT;
	}
	
	/**
	 * 获得带符号的金钱的金额
	 * @param money
	 * @return
	 */
	public static String getSignedMoney(double money) {
		return RMB + scaleMoney(money);
	}
	
	public static String getMoney(double money) {
		return "" + scaleMoney(money);
	}
	
	public static String getScaleFormat(int scale) {
		return "%0" + scale + "d";
	}
	
	/**
	 * 设置double类型的小数精度
	 * @param d
	 * @param scale
	 * @return
	 */
	public static double scaleDouble(double d, int scale) {
		BigDecimal bd = new BigDecimal(d).setScale(scale, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}
	
	/**
	 * 
	 * @param d
	 * @param scale
	 * @return 返回指定长度的小数精度
	 */
	public static float scaleFloat(float d, int scale) {
		BigDecimal bd = new BigDecimal(d).setScale(scale, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}
	
	/**
	 * 设置金钱的小数精度为2
	 * @param money
	 * @return
	 */
	public static double scaleMoney(double money) {
		BigDecimal bd = new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}
	
	/**
	 * 输入的数字长度达不到指定位数时前面补0
	 * @param src 输入的数字
	 * @param length 指定位数
	 */
	public static String matchLength(int src, int length) {
		return matchLength(src + "", length);
	}
	
	/**
	 * 输入的数字长度达不到指定位数时前面补0
	 * @param src 输入的数字
	 * @param length 指定位数
	 */
	public static String matchLength(String src, int length) {
		while (src.length() < length) {
			src = "0" + src;
		}
		if (src.length() > length) {
			src = src.substring(src.length() - length);
		}
		return src;
	}
	
	/**
	 * 输入的数字转为十六进制，长度达不到指定位数时前面补0
	 * @param src 输入的数字
	 * @param length 指定位数
	 */
	public static String toHexMatchLength(int src, int length) {
		return matchLength(Integer.toHexString(src).toUpperCase(), length);
	}
	
	/**
	 * 将数字每隔3位用逗号分隔
	 * @param digits 数字
	 */
	public static String formatDigitsWithComma(int digits) {
		return formatDigits(digits, ",");
	}
	
	/**
	 * 将数字每隔3位用指定的符号分隔
	 * @param digits 数字
	 * @param separator 指定的符号
	 */
	public static String formatDigits(int digits, String separator) {
		DecimalFormat df = new DecimalFormat("###" + separator + "###");
		return df.format(digits);
	}
}
