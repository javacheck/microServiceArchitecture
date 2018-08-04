package cn.lastmiles.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jodd.datetime.JDateTime;

public abstract class DateUtils {
	public static void main(String[] args){
		Date startDate = new Date(DateUtils.parse("yyyy-MM-dd HH:mm:ss", "2016-04-17 20:10:10").getTime());
		Date endDate = new Date(DateUtils.parse("yyyy-MM-dd HH:mm:ss", "2016-04-18 02:20:10").getTime());
		
		System.out.println(getDay24HList(startDate,endDate));
		
		List<Date> dateList = getDay24HList(startDate,endDate);
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (int i = 0; i < dateList.size() - 1; i++){
			if (startDate.getTime() >= dateList.get(i).getTime() && startDate.getTime() < dateList.get(i+1).getTime() 
					){
				if ( dateList.get(i+1).getTime() < endDate.getTime()){
					Map<String,Object> m = new HashMap<String, Object>();
					m.put("start", startDate);
					m.put("end", dateList.get(i+1));
					startDate = dateList.get(i+1);
					list.add(m);
				}else {
					Map<String,Object> m = new HashMap<String, Object>();
					m.put("start", startDate);
					m.put("end", endDate);
					list.add(m);
					break;
				}
			}
		}
		
		System.out.println(list);
		
		startDate = new Date(DateUtils.parse("yyyy-MM-dd HH:mm:ss", "2016-04-17 20:10:10").getTime());
		
		System.out.println(getWeek(startDate));
		
		System.out.println(addDay(startDate,1));
		System.out.println(startDate);
	}
	
	public static List<Date> getDay24HList(Date startDate,Date endDate){
		startDate = getDate(startDate, "yyyy-MM-dd HH");
		endDate = getDate( addMinute(endDate, 60), "yyyy-MM-dd HH");
		
		List<Date> dateList = new ArrayList<Date>();
		while (startDate.getTime() <= endDate.getTime()){
			dateList.add(startDate);
			startDate = addMinute(startDate, 60);
		}
		
		return dateList;
	}
	
	/**
	 * 一天24小时的正点时间列表
	 * @return
	 */
	public static List<Date> getDay24HList(){
		Date date = getDate(new Date(), "yyyy-MM-dd");
		List<Date> dateList = new ArrayList<Date>();
		dateList.add(date);
		for (int i = 0; i < 24; i++){
			date = addMinute(date, 60);
			dateList.add(date);
		}
		
		return dateList;
	}
	
	public static Date getDate(Date date, String pattern) {
		return parse(pattern,format(date, pattern));
	}
	
	/**
	 * 相差天数
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int getDayDiff(Date d1, Date d2) {
		return new JDateTime(d1).daysBetween(new JDateTime(d2));
	}
	
	/**
	 * 相差秒
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static long getSecondsDiff(Date d1, Date d2) {
		return Math.abs(d1.getTime() - d2.getTime()) / 1000L;
	}

	/**
	 * 相差分钟
	 * 
	 * @param d1
	 * @param d2
	 */
	public static Long getMinuteDiff(Date d1, Date d2) {
		return getSecondsDiff(d1, d2) / 60;
	}
	
	/**
	 * 当年第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(getDate(date, "yyyy-MM-dd"));
		c.set(Calendar.DAY_OF_YEAR, 1);
		return c.getTime();
	}
	
	/**
	 * 当月第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(getDate(date, "yyyy-MM-dd"));
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}

	/**
	 * 根据日期，得到这个月最后的一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, getMaxDayOfMonth(date));

		return c.getTime();
	}
	
	/**
	 * 这个月最大的一天
	 * 
	 * @param date
	 * @return
	 */
	public static int getMaxDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
//	public static Date parse(String date,String pattern){
//		DateFormat sdf = new SimpleDateFormat(pattern);
//		try {
//			return sdf.parse(date);
//		} catch (ParseException e) {
//			throw new RuntimeException(e);
//		}
//	}
	
	public static Date parse(String pattern,String date){
		DateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String format(Date date, String pattern) {
		DateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	public static Date addMinute(Date date,int minute){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, minute);
		return c.getTime();
	}
	
	public static Date addDay(Date date,int day){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, day);
		return c.getTime();
	}
	
	/**
	 * 加减年
	 * @param date
	 * @param year
	 * @return
	 */
	public static Date addYear(Date date,int year){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, year);
		return c.getTime();
	}
	
	/**
	 * 2个时间差值
	 * @param date
	 * @param date1
	 * @return
	 */
	public static Long mathDateMinus(Date date,Date date1){
		return date.getTime()-date1.getTime();
	}
	/**
	 * 上个月
	 * @param date
	 * @return
	 */
	public static Date getLastDate(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, -1);
		return c.getTime();
	}
	
	/**
	 * 是否是同一天
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static boolean theSameDay(Date d1,Date d2){
		String pattern = "yyyy-MM-dd";
		String s1 = format(d1, pattern);
		String s2 = format(d2, pattern);
		
		return s1.equals(s2);
	}
	
	/**
	 * 取小时
	 * @param d
	 * @return
	 */
	public static Integer getHour(Date d){
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c.get(Calendar.HOUR_OF_DAY);
	}
	
	public static int getWeek(Date d){
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		return c.get(Calendar.DAY_OF_WEEK);
	}
}
