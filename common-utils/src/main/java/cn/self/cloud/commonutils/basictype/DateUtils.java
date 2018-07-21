package cn.self.cloud.commonutils.basictype;

import jodd.datetime.JDateTime;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class DateUtils {
    public static final String DATE_NUMBER_FORMAT = "yyyyMMdd";
    public static final String DATE_TIME_NUMBER_FORMAT = "yyyyMMddHHmmss";
    public static final String DATA_FORMAT = "yyyy-MM-dd";
    public static final String DATA_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 获得日期相加月份之后的第一天开始时间
     */
    public static Timestamp getMonthRangeStart(Object date, int monthsLater) {
        DateTime dateTime = new DateTime(date);
        DateTime plusMonthsDateTime = dateTime.plusMonths(monthsLater).withTime(0, 0, 0, 0);

        return new Timestamp(plusMonthsDateTime.dayOfMonth().withMinimumValue().getMillis());
    }

    /**
     * 获得日期相加月份之后的最后一天结束时间
     */
    public static Timestamp getMonthRangeEnd(Object date, int monthsLater) {
        DateTime dateTime = new DateTime(date);
        DateTime plusMonthsDateTime = dateTime.plusMonths(monthsLater).withTime(23, 59, 59, 999);

        return new Timestamp(plusMonthsDateTime.dayOfMonth().withMaximumValue().getMillis());
    }

    public static String formatDate2String(Object date, String formatString) {
        DateTime dateTime = new DateTime(date);
        DateTimeFormatter fmt = DateTimeFormat.forPattern(formatString);

        return fmt.print(dateTime);
    }

    /**
     * 获取两个日期区间每一天的日期
     *
     * @return List
     */
    public static List<String> getEveryDayDateFromStartDateAndEndDate(Object startDate, Object endDate, String format) throws ParseException {
        List<String> dateTimeList = new ArrayList<String>();
        DateTimeFormatter fmt = DateTimeFormat.forPattern(format);

        DateTime start = new DateTime(startDate);
        DateTime end = new DateTime(endDate);
        int daysInterval = Days.daysBetween(start, end).getDays();

        for (int i = 0; i <= daysInterval; i++) {
            DateTime nextDateTime = start.plusDays(i);
            dateTimeList.add(fmt.print(nextDateTime));
        }

        return dateTimeList;
    }

    /**
     * 获取两个日期区间相差的月份数
     */
    public static int getMonthInterval(Object startDateTime, Object endDateTime) {
        DateTime start = new DateTime(startDateTime);
        DateTime end = new DateTime(endDateTime);

        return Months.monthsBetween(start, end).getMonths();
    }

    /**
     * 获取两个日期区间相差的分钟数
     */
    public static int getMinutesInterval(Object startDateTime, Object endDateTime) {
        DateTime start = new DateTime(startDateTime);
        DateTime end = new DateTime(endDateTime);

        return Minutes.minutesBetween(start, end).getMinutes();
    }

    /**
     * 获取两个日期区间相差的小时数
     */
    public static int getHoursInterval(Object startDateTime, Object endDateTime) {
        DateTime start = new DateTime(startDateTime);
        DateTime end = new DateTime(endDateTime);

        return Hours.hoursBetween(start, end).getHours();
    }

    /**
     * 时间计算（传入的时间加上分钟数，返回相加之后的时间）
     */
    public static Timestamp addMinuteToTimestamp(Object dateTime, int minutes) {
        DateTime dateTimeObj = new DateTime(dateTime);
        DateTime dateTimePlusObj = dateTimeObj.plusMinutes(minutes);

        Timestamp timestamp = new Timestamp(dateTimePlusObj.getMillis());

        return timestamp;
    }

    /**
     * 开始时间是否在结束时间之前
     */
    public static boolean isBefore(Object startDateTime, Object endDateTime) {
        DateTime start = new DateTime(startDateTime);
        DateTime end = new DateTime(endDateTime);

        return start.isBefore(end.getMillis());
    }

    /**
     * 获取日期当天结束的时间
     */
    public static DateTime getStartDateTimeOfDay(Object date) {
        DateTime dateTime = new DateTime(date);
        dateTime = dateTime.withTime(0, 0, 0, 0);

        return dateTime;
    }

    /**
     * 获取日期当天结束的时间
     */
    public static DateTime getEndDateTimeOfDay(Object date) {
        DateTime dateTime = new DateTime(date);
        dateTime = dateTime.withTime(23, 59, 59, 999);

        return dateTime;
    }

    public static List<Date> getDay24HList(Date startDate, Date endDate) {
        startDate = getDate(startDate, "yyyy-MM-dd HH");
        endDate = getDate(addMinute(endDate, 60), "yyyy-MM-dd HH");

        List<Date> dateList = new ArrayList<Date>();
        while (startDate.getTime() <= endDate.getTime()) {
            dateList.add(startDate);
            startDate = addMinute(startDate, 60);
        }

        return dateList;
    }

    /**
     * 一天24小时的正点时间列表
     *
     * @return
     */
    public static List<Date> getDay24HList() {
        Date date = getDate(new Date(), "yyyy-MM-dd");
        List<Date> dateList = new ArrayList<Date>();
        dateList.add(date);
        for (int i = 0; i < 24; i++) {
            date = addMinute(date, 60);
            dateList.add(date);
        }

        return dateList;
    }

    public static Date getDate(Date date, String pattern) {

        return parse(pattern, format(date, pattern));
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

    public static Date parse(String pattern, String date) {
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

    public static Date addMinute(Date date, int minute) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, minute);
        return c.getTime();
    }

    public static Date addDay(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, day);
        return c.getTime();
    }

    /**
     * 加减年
     *
     * @param date
     * @param year
     * @return
     */
    public static Date addYear(Date date, int year) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, year);
        return c.getTime();
    }

    /**
     * 2个时间差值
     *
     * @param date
     * @param date1
     * @return
     */
    public static Long mathDateMinus(Date date, Date date1) {

        return date.getTime() - date1.getTime();
    }

    /**
     * 上个月
     *
     * @param date
     * @return
     */
    public static Date getLastDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -1);
        return c.getTime();
    }

    /**
     * 是否是同一天
     *
     * @param d1
     * @param d2
     * @return
     */
    public static boolean theSameDay(Date d1, Date d2) {
        String pattern = "yyyy-MM-dd";
        String s1 = format(d1, pattern);
        String s2 = format(d2, pattern);

        return s1.equals(s2);
    }

    /**
     * 取小时
     *
     * @param d
     * @return
     */
    public static Integer getHour(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public static int getWeek(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c.get(Calendar.DAY_OF_WEEK);
    }


    /**
     * 获取日期所在的月份第一天开始时间
     */
    public static DateTime getMonthFirstDayTime(Object object) {
        DateTime dateTime = new DateTime(object);
        dateTime = dateTime.withTime(0, 0, 0, 0);

        return dateTime;
    }

    /**
     * 获取日期所在的月份最后一天结束时间
     */
    public static DateTime getMonthLastDayTime(Object object) {
        DateTime dateTime = new DateTime(object);

        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        int lastDay = dateTime.dayOfMonth().withMaximumValue().dayOfMonth().get();

        dateTime = new DateTime(year, month, lastDay, 23, 59, 59, 999);

        return dateTime;
    }
}
