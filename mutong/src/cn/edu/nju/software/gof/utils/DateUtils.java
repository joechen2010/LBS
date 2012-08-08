package cn.edu.nju.software.gof.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utilities of common usage of date.
 * 
 * @author ehanrli
 * 
 */
public final class DateUtils {
    
    public static final long              ONE_HOUR     = 60 * 60 * 1000;
    
    public static final long              ONE_DAY     = 24 * 60 * 60 * 1000;
    
    public static final long              ONE_MINUTE   = 60 * 1000;
    
    public static final String            DAY_PATTERN  = "yyyy-MM-dd";
    
    public static final String            TIME_PATTERN = "yyyy-MM-dd HH:mm";
    public static final String            HOUR_PATTERN = "yyyyMMddHH";
    
    private static TimeZone               timeZone     = TimeZone.getDefault();
    
    
    private DateUtils() {
        
    }
    
    /**
     * Set the current timezone
     * 
     * @param timeZoneVal
     */
    public static void setTimeZone(TimeZone timeZoneVal) {
        timeZone = timeZoneVal;
    }
    
    public static TimeZone getTimeZone() {
        return timeZone;
    }
    
    public static long toLong(String day) {
        try {
        	SimpleDateFormat sdf          = new SimpleDateFormat(DAY_PATTERN);
            sdf.setTimeZone(timeZone);
            return sdf.parse(day).getTime();
        } catch (ParseException e) {
            throw new IllegalArgumentException("The " + day + " is invalid, the right pattern should be " + DAY_PATTERN);
        }
    }
    
    public static long toLong(String time, String pattern) {
        try {
            
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            simpleDateFormat.setTimeZone(timeZone);
            return simpleDateFormat.parse(time).getTime();
        } catch (ParseException e) {
            throw new IllegalArgumentException("The " + time + " is invalid, the right pattern should be " + pattern);
        }
    }
    
    public static String toString(long day) {
        Date date = new Date(day);
        SimpleDateFormat sdf          = new SimpleDateFormat(DAY_PATTERN);
        sdf.setTimeZone(timeZone);
        return sdf.format(date);
    }
    
    public static String toString(long day, String pattern) {
        Date date = new Date(day);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(date);
    }
    
    /**
     * return a string follow the "yyyyMMddHH" pattern 
     * @return
     */
    public static String getHourTimeByStringType() {
        return getHourTimeByStringType(System.currentTimeMillis());
    } 
    
    /**
     * return a string follow the "yyyyMMddHH" pattern 
     * @param time
     * @return
     */
    public static String getHourTimeByStringType(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        return sdf.format(date);
    } 
    
  
    
    public static Integer getDayTimeByIntType() {
        return getDayTimeByIntType(System.currentTimeMillis());
    }

    /**
     * return a integer follow this pattern ""yyyyMMdd""
     * @param time
     * @return
     */
    public static Integer getDayTimeByIntType(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return Integer.valueOf(Integer.valueOf(sdf.format(date)));
    }

    public static void validate(long time) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime(new Date(time));
    }
    
    
    public static int getHourTimeByIntType() {
		return Integer.parseInt(DateUtils.toString(System.currentTimeMillis(), HOUR_PATTERN));
	}
    
    /**
     * return a integer as current time with the formate "yyyyMMddHH"
     * @return
     */
    public static long getHourTimeByLongType() {
    	return getHourTimeByLongType(System.currentTimeMillis());
    }
    
    public static long getHourTimeByLongType(long timeStamp) {
		return  Long.parseLong(DateUtils.toString(timeStamp, HOUR_PATTERN));
	}
    
    public static long getNextHourTimeByLongType(long hour) {
		String temp =  "" + hour;
		long time = toLong(temp, HOUR_PATTERN) + 1000*60*60;
		return Long.parseLong(DateUtils.toString(time, HOUR_PATTERN));
	}
    
    public static long getPreviousHourTimeByIntType(long hour) {
  		String temp =  "" + hour;
  		long time = toLong(temp, HOUR_PATTERN) - 1000*60*60;
  		return Long.parseLong(DateUtils.toString(time, HOUR_PATTERN));
  	}
    /**
     * compare to the current date to decide how many days remain
     * @param compareDate
     * @return
     */
    public static int getRemainsDay(String compareDate) {
        
        String today = DateUtils.toString(System.currentTimeMillis(), "yyyy-MM-dd");
        long consuming = DateUtils.toLong(compareDate, "yyyy-MM-dd") - System.currentTimeMillis();
        boolean negative = consuming < 0;
        consuming = Math.abs(consuming);
        int day = 0;
        int days = (int) (consuming/ONE_DAY);
        if (today.equals(compareDate)) {
            day = 0;
        } else if (consuming <= ONE_DAY) {
            day = 1;
        } else { 
            day = days > 1 && !negative ? days+1 : days;
        }
        return negative ? 0 - day : day;
    }

	public static int toDayTimeByHourLongType(long hour) {
		return Integer.parseInt((""+ hour).substring(0, 8));
	}
}
