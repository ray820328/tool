package com.ray.tool.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
	public static enum DATE_FORMATER{
		yyyy_MM_dd_HH_mm_ss(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")),
		yyyy_MM_dd(new SimpleDateFormat("yyyy-MM-dd")),
		HH_mm_ss(new SimpleDateFormat("HH:mm:ss")),
		CHINA_yyyy_MM_dd(new SimpleDateFormat("yyyy年MM月dd日")),
		CHINA_yyyy_MM_dd_HH_mm_ss(new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒"));
		SimpleDateFormat formater;
		private DATE_FORMATER(SimpleDateFormat formater) {
			this.formater = formater;
		}
		public String format(Date date){
			return formater.format(date);
		}
		public Date parse(String str) throws Exception{
			return formater.parse(str);
		}
	}
	public final static int SECOND = 1;
	public final static int MINUTE_SECOND = 60 * SECOND;
	public final static int HOUR_SECOND = 60 * MINUTE_SECOND;
	public final static int DAY_SECOND = 24 * HOUR_SECOND;
	
	public final static long SECOND_MILLIS = 1000;
	public final static long MINUTE_MILLIS = MINUTE_SECOND * SECOND_MILLIS;
	public final static long HOUR_MILLIS = 60 * MINUTE_MILLIS;
	public final static long DAY_MILLIS = 24 * HOUR_MILLIS;
	
	public static int getNowSecond(){
		return (int)(System.currentTimeMillis() / SECOND_MILLIS);
	}
	
	public static long getNowMillis(){
		return System.currentTimeMillis();
	}                                                                                                                                         
	
	public static Date parseDate(String dateStr, DATE_FORMATER formater) {
		try {
			return formater.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static String formatDate(Date date, DATE_FORMATER formater) {
		return formater.format(date);
	}
	
	public static Date getTodayTime(String time){
		Date date = parseDate(time, DATE_FORMATER.HH_mm_ss);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
		calendar1.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
		calendar1.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
		calendar1.set(Calendar.MILLISECOND, 0);
		return calendar1.getTime();
	}
	
	/**
	 * 
	 * @param day [Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY]
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	public static Date getDateOfWeek(int day, int hour, int minute, int second){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, day);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		Date date = TimeUtil.getNowDate();
		while(calendar.getTimeInMillis() > date.getTime()){
			calendar.setTime(new Date(calendar.getTimeInMillis() - 7 * TimeUtil.DAY_MILLIS));
		}
		return calendar.getTime();
	}
	
	public static Date getDateOfWeek(int day, String time){
		Date date = parseDate(time, DATE_FORMATER.HH_mm_ss);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(Calendar.DAY_OF_WEEK, day);
		calendar1.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
		calendar1.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
		calendar1.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
		calendar1.set(Calendar.MILLISECOND, 0);
		return calendar1.getTime();
	}
	
	/**
	 * 得到绝对下一次时间
	 * @param dateTime
	 * @param amount 天数
	 */
	public static long getNextDateTime(long dateTime, int amount){
		return dateTime + amount * DAY_MILLIS;
	}
	
	/**
	 * 得到下一次时间
	 * @param date
	 * @return
	 */
	public static Date getNextDate(Date date, int field, int amount){
		Calendar calendar = Calendar.getInstance();
		if(calendar.getTimeInMillis() < date.getTime()){
			return date;
		}else{
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(date);
			calendar1.add(field, amount);
			while(calendar1.getTimeInMillis() < date.getTime() || calendar1.getTimeInMillis() < calendar.getTimeInMillis()){
				calendar1.add(field, amount);
			}
			return calendar1.getTime();
		}
	}
	
	
	public static Date getDate(int hour, int minute, int second){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static Date getDate(int second){
		return new Date(second * TimeUtil.SECOND_MILLIS);
	}
	
	public static int getTime(Date date){
		return getSecond(date.getTime());
	}
	
	public static boolean before(Date date1, Date date2){
		return date1.before(date2);
	}
	
	public static int getInterval(Date date1, Date date2){
		return getSecond(date2.getTime()) - getSecond(date1.getTime());
	}
	
	public static Date getNowDate(){
		return new Date();
	}
	
	public static int getSecond(long time){
		return (int)(time / SECOND_MILLIS);
	}
	
	public static boolean after(Date date1, Date date2){
		return date1.after(date2);
	}
	
	public static int getYearNumber(){   //TODO 获取年份
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}
	
	public static int getMonthNumber(){   //TODO 获取月份
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MONTH) + 1;
	}
	
	public static int getDayNumber(){   //TODO 获取天数
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DATE);
	}
	
	public static int getMonthByMaxDayNumber(){  //TODO 本月最大天数
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		calendar.roll(Calendar.DATE, -1);
		return calendar.get(Calendar.DATE);
	}
	
	public static Date getZeroTime(Date date, int hour, int minute, int second){ //获取当天凌晨指定时间
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static boolean judgeEqualDay(Date date1, Date date2){  //TODO 判断是否同一天
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime(date1);
		calendar2.setTime(date2);
		if(calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)){
			if(calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)){
				if(calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)){
					return true;
				}
			}
		}
		return false;
	}
	
	
	public static boolean contrastTime(Date date1, Date date2){
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime(date1);
		calendar2.setTime(date2);
		if(calendar1.get(Calendar.HOUR_OF_DAY) == calendar2.get(Calendar.HOUR_OF_DAY)){
			if(calendar1.get(Calendar.MINUTE) == calendar2.get(Calendar.MINUTE)){
				if(calendar1.get(Calendar.SECOND) == calendar2.get(Calendar.SECOND)){
					return true;
				}
			}
		}
		return false;
	}
	

	public static Date getDateOfMonthDayTime(int day, int hour, int minute, int second){  //TODO 获取本月某一天某时刻
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, day);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		return calendar.getTime();
	}
	
	public static Date getDateOfYearDayTime(String time){  //TODO 获取一年中某一天某时刻
		Date date = parseDate(time, DATE_FORMATER.yyyy_MM_dd_HH_mm_ss);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTime();
	}
	
	public static Date getDateOfTodayTime(String time){   //TODO 获取本日某个时刻
		Date date = parseDate(time, DATE_FORMATER.HH_mm_ss);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
		calendar1.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
		calendar1.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
		return calendar1.getTime();
	}

	
	/**
	 * 获取下一次时间
	 */
	public static Date getNextTime(Date date, int field, int amount){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date); 
		calendar.add(field, amount);
		return calendar.getTime();
	}
	
	public static int setTime(int year, int month, int day, int hour, int minute, int second){
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day, hour, minute, second);
		Date date = calendar.getTime();
		return getSecond(date.getTime());
	}

	public static void main(String args[]){
		System.out.println(TimeUtil.parseDate("2014-04-27 00:00:00", DATE_FORMATER.yyyy_MM_dd_HH_mm_ss).getTime() / 1000);
		System.out.println(TimeUtil.parseDate("2014-04-28 00:00:00", DATE_FORMATER.yyyy_MM_dd_HH_mm_ss).getTime() / 1000);
	}
}
