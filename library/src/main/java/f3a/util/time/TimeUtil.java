package f3a.util.time;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class TimeUtil {

	public static final String yyyy_MM_dd_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public static final String yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
	public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
	public static final String yyyy_MM_dd = "yyyy-MM-dd";
	public static final String yyyy年MM月dd日EEEE = "yyyy年MM月dd日 EEEE";
	public static final String yyyy年MM月dd日_HH_mm_ss = "yyyy年MM月dd日 HH:mm:ss";
	public static final String HH_mm = "HH:mm";
	
	public static int getMonthDuration(Calendar start, Calendar end) {
		if (start.after(end)) {
			return Integer.MIN_VALUE;
		} else {
			return (end.get(Calendar.YEAR) - start.get(Calendar.YEAR))*12 +
					(end.get(Calendar.MONTH) - start.get(Calendar.MONTH));
		}
	}

	public static String long2String(Context context, long time) {
		if (time < 0) {
			throw new IllegalArgumentException("time必须大于等于0");
		}
		Date date = new Date(time);
		java.text.DateFormat dateFormat = DateFormat.getDateFormat(context);
		java.text.DateFormat timeFormat = DateFormat.getTimeFormat(context);
		return dateFormat.format(date) + " " + timeFormat.format(date);
	}
	
	public static String long2String(String regex, long time) {
		return new SimpleDateFormat(regex).format(time);
	}
	
	public static long string2Long(String regex, String time) {
		try {
			return new SimpleDateFormat(regex).parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0L;
		}
	}
	
	public static boolean isToday(long time) {
		Calendar target = Calendar.getInstance();
		target.setTimeInMillis(time);
		Calendar today = Calendar.getInstance();
        return target.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                target.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR);
    }
    
    public static int getField(long time, int fieldName) {
        Calendar target = Calendar.getInstance();
        target.setTimeInMillis(time);
	    return target.get(fieldName);
    }
}
