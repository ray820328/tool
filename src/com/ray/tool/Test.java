package com.ray.tool;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.ray.tool.util.TimeUtil;

public class Test {

	static SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static void main(String[] args){
		try{
		Map<String, Long> tokenHistory = new HashMap<String, Long>();
		for(int i=0; i<100; i++){
			tokenHistory.put("" + i, Long.valueOf(i));
		}
		Iterator<String> it = tokenHistory.keySet().iterator();
		while(it.hasNext()){
			if(tokenHistory.get(it.next()).longValue() == 50L){
				it.remove();
			}
		}
		System.out.println(tokenHistory.get("51"));
		System.out.println(tokenHistory.get("50"));
		System.out.println(tokenHistory.size());
		
		Integer v1 = new Integer(11), v2 = new Integer(11);
		System.out.println(v1==v2);
		System.out.println(v1.equals(v2));
		
		System.out.println(Math.pow(2 + (5-1)*3, 2));
		
		Date now = new Date();
		System.out.println("now=" + now.getTime() / 1000);
		System.out.println("now - 8=" + (now.getTime() - 8 * TimeUtil.DAY_MILLIS) / 1000);
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(now);
		calendar1.add(Calendar.HOUR_OF_DAY, 1);
		System.out.println("time=" + (calendar1.getTimeInMillis() - now.getTime()) / 1000);
		
		Date date = new Date(1419591600000L);
		System.out.println("date1=" + formater.format(date));
		date = new Date(1420803600 * 1000L);
		System.out.println("date2=" + formater.format(date));
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
//		calendar.set(Calendar.DAY_OF_WEEK, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date dateCalendar = calendar.getTime();
		System.out.println("dateCalendar=" + formater.format(dateCalendar));
		
		System.out.println("1111=" + new Date().getTime() / 1000);
		
		long startTime = 1418832000000L;
		System.out.println("d1=" + startTime + ", str=" + formater.format(new Date(startTime)));
		startTime = TimeUtil.getNextDate(new Date(startTime), Calendar.DAY_OF_YEAR, 7).getTime();
		System.out.println("d2=" + startTime + ", str=" + formater.format(new Date(startTime)));
		System.out.println("data1=" + formater.parse("2014-12-25 10:00:00").getTime());
		
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
