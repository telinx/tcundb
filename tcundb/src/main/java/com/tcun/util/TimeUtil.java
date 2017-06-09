/**
 *
 * Copyright (c) 2015 Chutong Technologies All rights reserved.
 *
 */
package com.tcun.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @author pcdalao
 * @version 0.0.1
 * @since
 */
public class TimeUtil {
    /** 
     * 缺省的日期显示格式： yyyy-MM-dd 
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /** 
     * 缺省的日期时间显示格式：yyyy-MM-dd HH:mm:ss 
     */
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss"; 

    /** 
     * 得到系统当前日期时间 
     * @return 当前日期时间 
     */
    public static Date getNow() {
        return Calendar.getInstance().getTime();
    }

    /** 
     * 得到用缺省方式格式化的当前日期 
     * @return 当前日期 
     */
    public static String getDate() {
        return getDateTime(DEFAULT_DATE_FORMAT);
    }

    /** 
     * 得到用缺省方式格式化的当前日期及时间 
     * @return 当前日期及时间 
     */
    public static String getDateTime() {
        return getDateTime(DEFAULT_DATETIME_FORMAT);
    }

    /** 
     * 得到系统当前日期及时间，并用指定的方式格式化 
     * @param pattern 显示格式 
     * @return 当前日期及时间 
     */
    public static String getDateTime(String pattern) {
        Date datetime = Calendar.getInstance().getTime();
        return getDateTime(datetime, pattern);
    }

    /** 
     * 得到用指定方式格式化的日期格式 
     * @param date 需要进行格式化的日期 
     * @param pattern 显示格式 
     * @return 日期时间字符串 
     */
    public static String getDateTime(Date date, String pattern) {
        if (null == pattern || "".equals(pattern)) {
            pattern = DEFAULT_DATETIME_FORMAT;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }
    
    /** 
     * 得到用指定方式格式化的日期格式 
     * @param date 需要进行格式化的日期 
     * @param pattern 显示格式 
     * @return 日期时间字符串 
     */
    public static String getDateTime(Object date, String pattern) {
        if (null == pattern || "".equals(pattern)) {
            pattern = DEFAULT_DATETIME_FORMAT;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
    * 字符串转换成日期
    * @param str 格式要求：yyyy-MM-dd HH:mm:ss
    * @return date
    */
    public static Date StrToDate(String str) {
        Date date = null;
        date = StrToDate(str, DEFAULT_DATETIME_FORMAT);
        return date;
    }

    /**
     * 字符串转换成日期
     * @param str
     * @param pattern 自定义格式
     * @return
     * @author pcdalao
     * @since
     */
    public static Date StrToDate(String str, String pattern) {
        if (null == pattern || "".equals(pattern)) {
            pattern = DEFAULT_DATETIME_FORMAT;
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /** 
     * 得到当前年份 
     * @return 当前年份 
     */
    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /** 
     * 得到当前月份 
     * @return 当前月份 
     */
    public static int getCurrentMonth() {
        //用get得到的月份数比实际的小1，需要加上  
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /** 
     * 得到当前日 
     * @return 当前日 
     */
    public static int getCurrentDay() {
        return Calendar.getInstance().get(Calendar.DATE);
    }

    /** 
     * 取得当前日期以后若干天的日期。如果要得到以前的日期，参数用负数。 
     * 例如要得到上星期同一天的日期，参数则为-7 
     * @param days 增加的日期数 
     * @return 增加以后的日期 
     */
    public static Date addDays(int days) {
        return add(getNow(), days, Calendar.DATE);
    }

    /** 
     * 取得指定日期以后若干天的日期。如果要得到以前的日期，参数用负数。 
     * @param date 基准日期 
     * @param days 增加的日期数 
     * @return 增加以后的日期 
     */
    public static Date addDays(Date date, int days) {
        return add(date, days, Calendar.DATE);
    }

    /** 
     * 取得当前日期以后某月的日期。如果要得到以前月份的日期，参数用负数。 
     * @param months 增加的月份数 
     * @return 增加以后的日期 
     */
    public static Date addMonths(int months) {
        return add(getNow(), months, Calendar.MONTH);
    }

    /** 
     * 取得指定日期以后某月的日期。如果要得到以前月份的日期，参数用负数。 
     * 注意，可能不是同一日子，例如2003-1-31加上一个月是2003-2-28 
     * @param date 基准日期 
     * @param months 增加的月份数 
    
     * @return 增加以后的日期 
     */
    public static Date addMonths(Date date, int months) {
        return add(date, months, Calendar.MONTH);
    }

    /** 
     * 内部方法。为指定日期增加相应的天数或月数 
     * @param date 基准日期 
     * @param amount 增加的数量 
     * @param field 增加的单位，年，月或者日 
     * @return 增加以后的日期 
     */
    private static Date add(Date date, int amount, int field) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    /** 
     * 计算两个日期相差天数。 
     * 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数 
     * @param one 第一个日期数，作为基准 
     * @param two 第二个日期数，作为比较 
     * @return 两个日期相差天数 
     */
    public static long diffDays(Date one, Date two) {
        return (one.getTime() - two.getTime()) / (24 * 60 * 60 * 1000);
    }

    /** 
     * 计算两个日期相差月份数 
     * 如果前一个日期小于后一个日期，则返回负数 
     * @param one 第一个日期数，作为基准 
     * @param two 第二个日期数，作为比较 
     * @return 两个日期相差月份数 
     */
    public static int diffMonths(Date one, Date two) {
        Calendar calendar = Calendar.getInstance();
        //得到第一个日期的年分和月份数  
        calendar.setTime(one);
        int yearOne = calendar.get(Calendar.YEAR);
        int monthOne = calendar.get(Calendar.MONDAY);
        //得到第二个日期的年份和月份  
        calendar.setTime(two);
        int yearTwo = calendar.get(Calendar.YEAR);
        int monthTwo = calendar.get(Calendar.MONDAY);
        return (yearOne - yearTwo) * 12 + (monthOne - monthTwo);
    }

    /** 
     * 将一个字符串用给定的格式转换为日期类型。<br> 
     * 注意：如果返回null，则表示解析失败 
     * @param datestr 需要解析的日期字符串 
     * @param pattern 日期字符串的格式，默认为“yyyy-MM-dd”的形式 
     * @return 解析后的日期 
     */
    public static Date parse(String datestr, String pattern) {
        Date date = null;
        if (null == pattern || "".equals(pattern)) {
            pattern = DEFAULT_DATE_FORMAT;
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            date = dateFormat.parse(datestr);
        } catch (ParseException e) {
        }
        return date;
    }

    /** 
     * 返回本月的最后一天 
     * @return 本月最后一天的日期 
     */
    public static Date getMonthLastDay() {
        return getMonthLastDay(getNow());
    }

    /** 
     * 返回给定日期中的月份中的最后一天 
     * @param date 基准日期 
     * @return 该月最后一天的日期 
     */
    public static Date getMonthLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //将日期设置为下一月第一天  
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);
        //减去1天，得到的即本月的最后一天  
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    /**
     * 今天是星期几
     * @param date Date类型参数
     * @return
     * @author pcdalao
     * @since
     */
    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK); //weekday=1，当天是周日；weekday=2，当天是周一；...;weekday=7，当天是周六
    }
    
    /**
     * 根据出生年月日精确计算年龄
     * @param dateOfBirth
     * @return
     */
    public static Integer getAge(Date dateOfBirth) {
        int age = 0;
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        if (dateOfBirth != null) {
            now.setTime(new Date());
            born.setTime(dateOfBirth);
            if (born.after(now)) {
                throw new IllegalArgumentException("年龄不能超过当前日期");
            }
            age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
            int nowDayOfYear = now.get(Calendar.DAY_OF_YEAR);
            int bornDayOfYear = born.get(Calendar.DAY_OF_YEAR);
            //System.out.println("nowDayOfYear:" + nowDayOfYear + " bornDayOfYear:" + bornDayOfYear);
            if (nowDayOfYear < bornDayOfYear) {
                age -= 1;
            }
        }
        return age;
    }
    
    /**
     * 根据出生年月日精确计算年龄
     * @param dateOfBirth 格式为：yyyyMMdd
     * @return
     */
    public static Integer getAge(String dateOfBirthStr) {
    	int age = 0;
    	try {
	        //SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
	        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyyMMdd");
	        java.util.Date dateOfBirth = myFormatter.parse(dateOfBirthStr);
	        
	    	Calendar born = Calendar.getInstance();
	    	Calendar now = Calendar.getInstance();
	    	if (dateOfBirth != null) {
	    		now.setTime(new Date());
	    		born.setTime(dateOfBirth);
	    		if (born.after(now)) {
	    			throw new IllegalArgumentException("年龄不能超过当前日期");
	    		}
	    		age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
	    		int nowDayOfYear = now.get(Calendar.DAY_OF_YEAR);
	    		int bornDayOfYear = born.get(Calendar.DAY_OF_YEAR);
	    		//System.out.println("nowDayOfYear:" + nowDayOfYear + " bornDayOfYear:" + bornDayOfYear);
	    		if (nowDayOfYear < bornDayOfYear) {
	    			age -= 1;
	    		}
	    	}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return age;
    }
    
    /**
     * 计算工龄
     * @param dateOfBirth 格式为：yyyy-MM-dd
     * @return
     */
    public static Double getWorkingAge(String dateOfBirthStr) {
    	Double workingAge = 0.0;
    	try {
    		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
    		java.util.Date dateOfBirth = myFormatter.parse(dateOfBirthStr);
    		DecimalFormat df = new DecimalFormat("######0.00"); 
    		
    		Calendar born = Calendar.getInstance();
    		Calendar now = Calendar.getInstance();
    		if (dateOfBirth != null) {
    			now.setTime(new Date());
    			born.setTime(dateOfBirth);
    			if (born.after(now)) {
    				throw new IllegalArgumentException("时间不能超过当前日期");
    			}
    			
    			long days = diffDays(new Date(), dateOfBirth);
    			workingAge = new Double(df.format(days / 365.0)); 
    		}
    	} catch (ParseException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	return workingAge;
    }

    public static void main(String[] args) throws Exception {
        String sss = getDateTime("yyyy年MM月dd日");
        String test = "2003-1-31";
        Date date;
        try {
            date = parse(test, "");

            System.out.println("得到当前日期 － getDate():" + TimeUtil.getDate());
            System.out.println("得到当前日期时间 － getDateTime():" + TimeUtil.getDateTime());
            System.out.println("字符串转日期 － StrToDate():" + TimeUtil.StrToDate("2005-07-12 12:23:23").toString());

            System.out.println("得到当前年份 － getCurrentYear():" + TimeUtil.getCurrentYear());
            System.out.println("得到当前月份 － getCurrentMonth():" + TimeUtil.getCurrentMonth());
            System.out.println("得到当前日子 － getCurrentDay():" + TimeUtil.getCurrentDay());

            System.out.println("解析 － parse(" + test + "):" + getDateTime(date, "yyyy-MM-dd"));

            System.out.println("自增月份 － addMonths(3):" + getDateTime(addMonths(3), "yyyy-MM-dd"));
            System.out.println("增加月份 － addMonths(" + test + ",3):" + getDateTime(addMonths(date, 3), "yyyy-MM-dd"));
            System.out.println("增加日期 － addDays(" + test + ",3):" + getDateTime(addDays(date, 3), "yyyy-MM-dd"));
            System.out.println("自增日期 － addDays(3):" + getDateTime(addDays(3), "yyyy-MM-dd"));

            System.out.println("相差天数 － diffDays():" + TimeUtil.diffDays(TimeUtil.getNow(), date));
            System.out.println("相差月份数 － diffMonths():" + TimeUtil.diffMonths(TimeUtil.getNow(), date));

            System.out.println("得到" + test + "所在月份的最后一天:" + getDateTime(getMonthLastDay(date), "yyyy-MM-dd"));

            System.out.println("年龄是：" + getAge("1984-05-29"));
            System.out.println("工龄是：" + getWorkingAge("2016-03-09"));
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
}
