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

/**
 * 日期操作类
 */
public abstract class DateUtils {
    public static final String DATE_NUMBER_FORMAT = "yyyyMMdd";
    public static final String DATE_TIME_NUMBER_FORMAT = "yyyyMMddHHmmss";
    public static final String DATA_FORMAT = "yyyy-MM-dd";
    public static final String DATA_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 功能：进行日期的格式化
     * @param date 需要格式的日期
     * @param pattern 格式规则
     * @return 格式后的字符串
     */
    public static String format(Date date, String pattern) {
        DateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 功能：进行日期的格式化
     * @param date 需要格式的对象Object
     * @param formatString 格式化规则
     * @return 格式后的字符串
     */
    public static String formatDateString(Object date, String formatString) {
        DateTime dateTime = new DateTime(date);
        DateTimeFormatter fmt = DateTimeFormat.forPattern(formatString);

        return fmt.print(dateTime);
    }

    /**
     * 功能：进行日期的格式化
     * @param date 需要格式的字符串
     * @param pattern 格式规则
     * @return 格式后的日期
     */
    public static Date parse(String date,String pattern) {
        DateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 功能：进行日期的格式化
     * @param date 需要格式的日期
     * @param pattern 格式化规则
     * @return 格式后的日期
     */
    public static Date getDate(Date date, String pattern) {
        return parse(format(date, pattern),pattern);
    }

    /**
     * 功能：判断 开始时间是否在结束时间之前
     * @param startDateTime Object
     * @param endDateTime Object
     * @return 正解则返回true
     */
    public static boolean isBefore(Object startDateTime, Object endDateTime) {
        DateTime start = new DateTime(startDateTime);
        DateTime end = new DateTime(endDateTime);
        return start.isBefore(end.getMillis());
    }

    /**
     * 功能：获取日期当天开始的时间
     * @param date 传入的日期
     * @return 返回当天开始的时间
     */
    public static DateTime getStartDateTimeOfDay(Object date) {
        DateTime dateTime = new DateTime(date);
        dateTime = dateTime.withTime(0, 0, 0, 0);

        return dateTime;
    }

    /**
     * 功能：获取日期当天结束的时间
     * @param date 传入的日期
     * @return 返回当天结束的时间
     */
    public static DateTime getEndDateTimeOfDay(Object date) {
        DateTime dateTime = new DateTime(date);
        dateTime = dateTime.withTime(23, 59, 59, 999);

        return dateTime;
    }

    /**
     * 功能：得到当天24小时的正点时间列表
     * @return 返回当天正点的List集合对象
     */
    public static List<Date> getDay24HList() {
        Date date = getDate(new Date(), DATA_FORMAT);
        List<Date> dateList = new ArrayList<>();
        dateList.add(date);
        for (int i = 0; i < 24; i++) {
            date = addMinute(date, 60);
            dateList.add(date);
        }

        return dateList;
    }

    /**
     * 功能：获得指定两个日期内的24小时的正点时间表
     * @param startDate 开始日期(带小时)
     * @param endDate 结束日期(带小时)
     * @return 返回正点的List集合对象
     */
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
     * 功能：获取当年第一天
     * @param date 日期参数
     * @return 得到日期
     */
    public static Date getFirstDateOfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(getDate(date, DATA_FORMAT));
        c.set(Calendar.DAY_OF_YEAR, 1);
        return c.getTime();
    }

    /**
     * 功能：获取当月第一天
     * @param date 日期参数
     * @return 得到日期
     */
    public static Date getFirstDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(getDate(date, DATA_FORMAT));
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    /**
     * 功能：根据日期，得到这个月最后的一天
     * @param date 日期参数
     * @return 得到日期
     */
    public static Date getLastDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, getMaxDayOfMonth(date));

        return c.getTime();
    }

    /**
     * 功能：根据日期，得到这个月最大的一天
     * @param date 日期参数
     * @return 天数
     */
    public static int getMaxDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 功能：根据日期，得到上个月
     * @param date 日期参数
     * @return 得到日期
     */
    public static Date getLastDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -1);
        return c.getTime();
    }

    /**
     * 功能：获取日期所在的月份第一天开始时间
     * @param object 传入的日期
     * @return 返回开始的时间
     */
    public static DateTime getMonthFirstDayTime(Object object) {
        DateTime dateTime = new DateTime(object);
        dateTime = dateTime.withTime(0, 0, 0, 0);

        return dateTime;
    }

    /**
     * 功能：获取日期所在的月份最后一天结束时间
     * @param object 传入的日期
     * @return 返回结束的时间
     */
    public static DateTime getMonthLastDayTime(Object object) {
        DateTime dateTime = new DateTime(object);

        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        int lastDay = dateTime.dayOfMonth().withMaximumValue().dayOfMonth().get();

        dateTime = new DateTime(year, month, lastDay, 23, 59, 59, 999);

        return dateTime;
    }

    /**
     * 功能：判断两个日期是否是同一天
     * @param d1 日期1
     * @param d2 日期2
     * @return 相同则返回true
     */
    public static boolean theSameDay(Date d1, Date d2) {
        String s1 = format(d1, DATA_FORMAT);
        String s2 = format(d2, DATA_FORMAT);

        return s1.equals(s2);
    }

    /**
     * 功能：获取传入日期是第几周
     * @param d 日期参数
     * @return 周数
     */
    public static int getWeek(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 功能：获得传入日期的小时
     * @param d 日期参数
     * @return 小时数
     */
    public static Integer getHour(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 功能：获得日期相加月份之后的第一天开始时间
     * @param date 日期参数
     * @param monthsLater 月份数
     * @return 时间戳
     */
    public static Timestamp getMonthRangeStart(Object date, int monthsLater) {
        DateTime dateTime = new DateTime(date);
        DateTime plusMonthsDateTime = dateTime.plusMonths(monthsLater).withTime(0, 0, 0, 0);

        return new Timestamp(plusMonthsDateTime.dayOfMonth().withMinimumValue().getMillis());
    }

    /**
     * 功能：获得日期相加月份之后的最后一天结束时间
     * @param date 日期参数
     * @param monthsLater 月份数
     * @return 时间戳
     */
    public static Timestamp getMonthRangeEnd(Object date, int monthsLater) {
        DateTime dateTime = new DateTime(date);
        DateTime plusMonthsDateTime = dateTime.plusMonths(monthsLater).withTime(23, 59, 59, 999);

        return new Timestamp(plusMonthsDateTime.dayOfMonth().withMaximumValue().getMillis());
    }

    /**
     * 功能：获取两个日期区间每一天的日期
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param format 格式化规则
     * @return 返回日期的List集合对象
     * @throws ParseException
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
     * 功能：获取两个日期区间相差的月份数
     * @param startDateTime 开始日期
     * @param endDateTime 结束日期
     * @return 相差的月份数
     */
    public static int getMonthInterval(Object startDateTime, Object endDateTime) {
        DateTime start = new DateTime(startDateTime);
        DateTime end = new DateTime(endDateTime);

        return Months.monthsBetween(start, end).getMonths();
    }

    /**
     * 功能：获取两个日期区间相差的分钟数
     * @param startDateTime 开始日期
     * @param endDateTime 结束日期
     * @return 相差的分钟数
     */
    public static int getMinutesInterval(Object startDateTime, Object endDateTime) {
        DateTime start = new DateTime(startDateTime);
        DateTime end = new DateTime(endDateTime);

        return Minutes.minutesBetween(start, end).getMinutes();
    }

    /**
     * 功能：获取两个日期区间相差的小时数
     * @param startDateTime 开始日期
     * @param endDateTime 结束日期
     * @return 相差的小时数
     */
    public static int getHoursInterval(Object startDateTime, Object endDateTime) {
        DateTime start = new DateTime(startDateTime);
        DateTime end = new DateTime(endDateTime);

        return Hours.hoursBetween(start, end).getHours();
    }

    /**
     * 功能：两个日期 相差天数
     * @param d1 日期1
     * @param d2 日期2
     * @return 相差的天数
     */
    public static int getDayDiff(Date d1, Date d2) {
        return new JDateTime(d1).daysBetween(new JDateTime(d2));
    }

    /**
     * 功能：两个日期 相差秒
     * @param d1 日期1
     * @param d2 日期2
     * @return 相差的秒数
     */
    public static long getSecondsDiff(Date d1, Date d2) {
        return Math.abs(d1.getTime() - d2.getTime()) / 1000L;
    }

    /**
     * 功能：两个日期 相差分钟
     * @param d1 日期1
     * @param d2 日期2
     * @return 相差的分钟数
     */
    public static Long getMinuteDiff(Date d1, Date d2) {
        return getSecondsDiff(d1, d2) / 60;
    }

    /**
     * 功能：获取两个时间的毫秒差值
     * @param date 日期1
     * @param date1 日期2
     * @return 毫秒差值
     */
    public static Long mathDateMinus(Date date, Date date1) {
        return date.getTime() - date1.getTime();
    }

    /**
     * 功能：在指定的日期上加上或者减去分钟数，返回结果
     * @param date 日期参数
     * @param minute 需要增加或者减少的分钟数 正/负
     * @return 得到日期
     */
    public static Date addMinute(Date date, int minute) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, minute);
        return c.getTime();
    }

    /**
     * 功能：在指定的日期上加上或者减去天数，返回结果
     * @param date 日期参数
     * @param day 需要增加或者减少的天数 正/负
     * @return 得到日期
     */
    public static Date addDay(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, day);
        return c.getTime();
    }

    /**
     * 功能：在指定的日期上加上或者减去年数，返回结果
     * @param date 日期参数
     * @param year 需要增加或者减少的年数 正/负
     * @return 得到日期
     */
    public static Date addYear(Date date, int year) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, year);
        return c.getTime();
    }

    /**
     * 功能：时间计算（传入的时间加上分钟数，返回相加之后的时间）
     * @param dateTime 日期参数
     * @param minutes 分钟数
     * @return 时间戳
     */
    public static Timestamp addMinuteToTimestamp(Object dateTime, int minutes) {
        DateTime dateTimeObj = new DateTime(dateTime);
        DateTime dateTimePlusObj = dateTimeObj.plusMinutes(minutes);

        Timestamp timestamp = new Timestamp(dateTimePlusObj.getMillis());

        return timestamp;
    }

    // 单元测试
    public static void main(String[] args) {
        System.out.println("格式化日期：" + format(new Date(),DATA_TIME_FORMAT));
        System.out.println("格式化日期：" + formatDateString(new Date(),DATE_TIME_NUMBER_FORMAT));
        System.out.println("格式化日期：" + parse("2017-07-23 23:22:10",DATE_NUMBER_FORMAT));
        System.out.println("格式化日期：" + getDate(new Date(),DATA_FORMAT));
        System.out.println("查看日期是否在某日期之前：" + isBefore(new Date(),new Date()));
        System.out.println("得到传入日期的开始时间：" + getStartDateTimeOfDay(new Date()));
        System.out.println("得到传入日期的结束时间：" + getEndDateTimeOfDay(new Date()));
        System.out.println("获取当天的整点：" + getDay24HList());
        System.out.println("获取指定时间的正点时间列表：" + getDay24HList(new Date(),new Date()));
        System.out.println("获取当年第一天：" + getFirstDateOfYear(new Date()));
        System.out.println("获取当月第一天：" + getFirstDate(new Date()));
        System.out.println("得到这个月最后的一天：" + getLastDay(new Date()));
        System.out.println("得到这个月最大的一天：" + getMaxDayOfMonth(new Date()));
        System.out.println("得到上个月：" + getLastDate(new Date()));
        System.out.println("获取日期所在的月份第一天开始时间：" + getMonthFirstDayTime(new Date()));
        System.out.println("获取日期所在的月份最后一天结束时间：" + getMonthLastDayTime(new Date()));
        System.out.println("判断两个日期是否是同一天：" + theSameDay(new Date(),new Date()));
        System.out.println("获取传入日期是第几周：" + getWeek(new Date()));
        System.out.println("获得传入日期的小时：" + getHour(new Date()));
        System.out.println("获得日期相加月份之后的第一天开始时间：" +getMonthRangeStart(new Date(),2) );
        System.out.println("获得日期相加月份之后的最后一天结束时间：" + getMonthRangeEnd(new Date(),3));
        try {
            System.out.println("获取两个日期区间每一天的日期：" + getEveryDayDateFromStartDateAndEndDate(new Date(),new Date(),DATE_TIME_NUMBER_FORMAT));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("获取两个日期区间相差的月份数：" + getMonthInterval(new Date(),new Date()));
        System.out.println("获取两个日期区间相差的分钟数：" + getMinutesInterval(new Date(),new Date()));
        System.out.println("获取两个日期区间相差的小时数：" + getHoursInterval(new Date(),new Date()));
        System.out.println("两个日期 相差天数：" + getDayDiff(new Date(),new Date()));
        System.out.println("两个日期 相差秒：" + getSecondsDiff(new Date(),new Date()));
        System.out.println("两个日期 相差分钟：" + getMinuteDiff(new Date(),new Date()));
        System.out.println("获取两个时间的毫秒差值：" + mathDateMinus(new Date(),new Date()));
        System.out.println("在指定的日期上加上或者减去分钟数，返回结果：" + addMinute(new Date(),3));
        System.out.println("在指定的日期上加上或者减去天数，返回结果：" + addDay(new Date(),5));
        System.out.println("在指定的日期上加上或者减去年数，返回结果：" + addYear(new Date(),10));
        System.out.println("时间计算（传入的时间加上分钟数，返回相加之后的时间）:" + addMinuteToTimestamp(new Date(),3));
    }
}
