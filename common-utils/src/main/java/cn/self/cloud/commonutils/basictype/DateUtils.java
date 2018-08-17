package cn.self.cloud.commonutils.basictype;

import jodd.datetime.JDateTime;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期操作类
 */
public abstract class DateUtils {
    public static final String DATE_NUMBER_FORMAT = "yyyyMMdd";
    public static final String DATE_TIME_NUMBER_FORMAT = "yyyyMMddHHmmss";
    public static final String DATA_FORMAT = "yyyy-MM-dd";
    public static final String DATA_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 功能：进行日期的格式化 , 根据给定的格式与时间(Date类型的)，返回时间字符串。最为通用。
     * @param date 需要格式的日期
     * @param pattern 格式规则
     * @return 格式后的字符串
     */
    public static String format(Date date, String pattern) {
        DateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 返回指定时间字符串。
     * 格式：yyyy-MM-dd HH:mm:ss
     * @return String 指定格式的日期字符串.
     */
    public static String format(long microsecond) {
        return format(new Date(microsecond), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 根据主机的默认 TimeZone，来获得指定形式的时间字符串。
     * @param dateFormat
     * @return 返回日期字符串，形式和formcat一致。
     */
    public static String getCurrentDateString(String dateFormat) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setTimeZone(TimeZone.getDefault());

        return sdf.format(cal.getTime());
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
     * 功能：获取日期当天开始的时间（获取时间凌晨）
     * @param date 传入的日期
     * @return 返回当天开始的时间
     */
    public static DateTime getStartDateTimeOfDay(Object date) {
        DateTime dateTime = new DateTime(date);
        dateTime = dateTime.withTime(0, 0, 0, 0);

        return dateTime;
    }

    /**
     * 功能：获取日期当天结束的时间（获取时间最后一刻）
     * @param date 传入的日期
     * @return 返回当天结束的时间
     */
    public static DateTime getEndDateTimeOfDay(Object date) {
        DateTime dateTime = new DateTime(date);
        dateTime = dateTime.withTime(23, 59, 59, 999);

        return dateTime;
    }

    /**
     * 获取时间凌晨
     * @param date
     * @return
     */
    public static Date getDateStartTime(java.util.Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取时间最后一刻
     * @param date
     * @return
     */
    public static Date getDateEndTime(java.util.Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
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
        // c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1); // 设置为1号,当前日期既为本月第一天
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

    /***
     * 获取指定月的最后一天
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfMonth(int year, int month) {
        Calendar ca = Calendar.getInstance();
        ca.set(year, month - 1, 1);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        return ca.getTime();
    }

    /***
     * 获取本月的第一天
     * @return
     */
    public static Date getFirstDayOfThisMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        return c.getTime();
    }
    /***
     * 获取上月的第一天
     * @return
     */
    public static Date getFirstDayOfLastMonth() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
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
     * 返回当前日期是星期几。例如：星期日、星期一、星期六等等。
     * @param date 格式为 yyyy/MM/dd 或者 yyyy-MM-dd
     * @return 返回当前日期是星期几
     */
    public static String getChinaDayOfWeek(Date date) {
        String[] weeks = new String[] { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        int week = getDayOfWeek(date);
        return weeks[week - 1];
    }

    /**
     * 功能：根据指定的年、月、日返回当前是星期几。1表示星期天、2表示星期一、7表示星期六。
     * @param date
     * @return 返回一个代表当期日期是星期几的数字。1表示星期天、2表示星期一、7表示星期六。
     */
    public static int getDayOfWeek(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 根据指定的年、月、日返回当前是星期几。1表示星期天、2表示星期一、7表示星期六。
     * @param year
     * @param month month是从1开始的12结束
     * @param day
     * @return 返回一个代表当期日期是星期几的数字。1表示星期天、2表示星期一、7表示星期六。
     */
    public static int getDayOfWeek(int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year,month - 1, day);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 功能：取得指定年月日的日期对象.
     * @param year 年
     * @param month 月注意是从1到12
     * @param day 日
     * @return 一个java.util.Date()类型的对象
     */
    public static Date getDateObj(int year, int month, int day) {
        Calendar c = new GregorianCalendar();
        c.set(year, month - 1, day);
        return c.getTime();
    }

    /**
     * 根据指定的年月 返回指定月份（yyyy-MM）有多少天。
     * @param time yyyy-MM
     * @return 天数，指定月份的天数。
     */
    public static int getDaysOfCurMonth(final String time) {
        if(time.length()!=7){
            throw new NullPointerException("参数的格式必须是yyyy-MM");
        }
        String[] timeArray = time.split("-");
        int curyear = new Integer(timeArray[0]).intValue(); // 当前年份
        int curMonth = new Integer(timeArray[1]).intValue();// 当前月份
        if(curMonth>12){
            throw new NullPointerException("参数的格式必须是yyyy-MM，而且月份必须小于等于12。");
        }
        int mArray[] = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30,
                31 };
        // 判断闰年的情况 ，2月份有29天；
        if ((curyear % 400 == 0) || ((curyear % 100 != 0) && (curyear % 4 == 0))) {
            mArray[1] = 29;
        }
        if (curMonth == 12) {
            return mArray[0];
        }
        return mArray[curMonth - 1];
        // 如果要返回下个月的天数，注意处理月份12的情况，防止数组越界；
        // 如果要返回上个月的天数，注意处理月份1的情况，防止数组越界；
    }

    /**
     * 根据指定的年月日小时分秒，返回一个java.Util.Date对象。
     * @param year 年
     * @param month 月 注意是从1到12
     * @param day 日
     * @param hourOfDay 小时 0-23
     * @param minute 分 0-59
     * @param second 秒 0-59
     * @return 一个Date对象。
     */
    public static Date getDateObj(int year, int month, int day, int hourOfDay, int minute, int second) {
        Calendar cal = new GregorianCalendar();
        cal.set(year, month-1, day, hourOfDay, minute, second);
        return cal.getTime();
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
     * 功能：起始年月yyyy-MM与终止月yyyy-MM之间跨度的月数。
     * @param beginMonth 格式为yyyy-MM
     * @param endMonth 格式为yyyy-MM
     * @return 整数。
     */
    public static int getMonthInterval(String beginMonth, String endMonth) {
        int intBeginYear = Integer.parseInt(beginMonth.substring(0, 4));
        int intBeginMonth = Integer.parseInt(beginMonth.substring(beginMonth.indexOf("-") + 1));
        int intEndYear = Integer.parseInt(endMonth.substring(0, 4));
        int intEndMonth = Integer.parseInt(endMonth.substring(endMonth.indexOf("-") + 1));

        return ((intEndYear - intBeginYear) * 12) + (intEndMonth - intBeginMonth) + 1;
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
     * 功能：求两个日期相差天数
     * @param sd  起始日期，格式yyyy-MM-dd
     * @param ed  终止日期，格式yyyy-MM-dd
     * @return 两个日期相差天数
     */
    public static long getDayDiff(String sd, String ed) {
        return ((java.sql.Date.valueOf(ed)).getTime() - (java.sql.Date.valueOf(sd)).getTime()) / (3600 * 24 * 1000);
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
        // Calendar cal = new GregorianCalendar();
        // cal.setTime(date);
        // cal.add(GregorianCalendar.DATE, day);
        // 或者
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

    /**
     * 获取小时/分钟/秒
     * @param second 秒
     * @return 包含小时、分钟、秒的时间字符串，例如3小时23分钟13秒。
     */
    public static String getHour(long second) {
        long hour = second / 60 / 60;
        long minute = (second - hour * 60 * 60) / 60;
        long sec = (second - hour * 60 * 60) - minute * 60;

        return hour + "小时" + minute + "分钟" + sec + "秒";
    }

    /**
     * <p>
     * 功能：根据给定的分析位置开始分析日期/时间字符串。
     * </p>
     * 例如：
     * <pre>
     * 时间文本 "07/10/96 4:5 PM, PDT" 会分析成等同于Date(837039928046) 的 Date。
     * </pre>
     * @param sDate
     * @param dateFormat
     * @return
     */
    public static Date analysisDate(String sDate, String dateFormat) {
        SimpleDateFormat fmt = new SimpleDateFormat(dateFormat);
        ParsePosition pos = new ParsePosition(0);
        return fmt.parse(sDate, pos);
    }


    /**
     * 判断一个日期是否在开始日期和结束日期之间。
     * @param srcDate 目标日期 yyyy/MM/dd 或者 yyyy-MM-dd
     * @param startDate 开始日期 yyyy/MM/dd 或者 yyyy-MM-dd
     * @param endDate 结束日期 yyyy/MM/dd 或者 yyyy-MM-dd
     * @return 大于等于开始日期小于等于结束日期，那么返回true，否则返回false
     */
    public static boolean isInStartEnd(String srcDate, String startDate, String endDate) {
        if (startDate.compareTo(srcDate) <= 0 && endDate.compareTo(srcDate) >= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 返回年份的下拉框。
     * @param selectName 下拉框名称
     * @param value 当前下拉框的值
     * @param startYear 开始年份
     * @param endYear 结束年份
     * @return 年份下拉框的html
     */
    public static String getYearSelect(String selectName, String value, int startYear, int endYear) {
        int start = startYear;
        int end = endYear;
        if (startYear > endYear) {
            start = endYear;
            end = startYear;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("<select name=\"" + selectName + "\">");
        for (int i = start; i <= end; i++) {
            if (!value.trim().equals("") && i == Integer.parseInt(value)) {
                sb.append("<option value=\"" + i + "\" selected>" + i + "</option>");
            } else {
                sb.append("<option value=\"" + i + "\">" + i + "</option>");
            }
        }
        sb.append("</select>");
        return sb.toString();
    }

    /**
     * 返回年份的下拉框。
     * @param selectName 下拉框名称
     * @param value 当前下拉框的值
     * @param startYear 开始年份
     * @param endYear 结束年份  例如开始年份为2001结束年份为2005那么下拉框就有五个值。（2001、2002、2003、2004、2005）。
     * @return 返回年份的下拉框的html。
     */
    public static String getYearSelect(String selectName, String value, int startYear, int endYear, boolean hasBlank) {
        int start = startYear;
        int end = endYear;
        if (startYear > endYear) {
            start = endYear;
            end = startYear;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("<select name=\"" + selectName + "\">");
        if (hasBlank) {
            sb.append("<option value=\"\"></option>");
        }
        for (int i = start; i <= end; i++) {
            if (!value.trim().equals("") && i == Integer.parseInt(value)) {
                sb.append("<option value=\"" + i + "\" selected>" + i + "</option>");
            } else {
                sb.append("<option value=\"" + i + "\">" + i + "</option>");
            }
        }
        sb.append("</select>");
        return sb.toString();
    }

    /**
     * 返回年份的下拉框。
     * @param selectName 下拉框名称
     * @param value 当前下拉框的值
     * @param startYear 开始年份
     * @param endYear 结束年份
     * @param js 这里的js为js字符串。例如 " onchange=\"changeYear()\" " ,这样任何js的方法就可以在jsp页面中编写，方便引入。
     * @return 返回年份的下拉框。
     */
    public static String getYearSelect(String selectName, String value, int startYear, int endYear, boolean hasBlank, String js) {
        int start = startYear;
        int end = endYear;
        if (startYear > endYear) {
            start = endYear;
            end = startYear;
        }
        StringBuffer sb = new StringBuffer();

        sb.append("<select name=\"" + selectName + "\" " + js + ">");
        if (hasBlank) {
            sb.append("<option value=\"\"></option>");
        }
        for (int i = start; i <= end; i++) {
            if (!value.trim().equals("") && i == Integer.parseInt(value)) {
                sb.append("<option value=\"" + i + "\" selected>" + i + "</option>");
            } else {
                sb.append("<option value=\"" + i + "\">" + i + "</option>");
            }
        }
        sb.append("</select>");
        return sb.toString();
    }

    /**
     * 返回年份的下拉框。
     * @param selectName 下拉框名称
     * @param value 当前下拉框的值
     * @param startYear 开始年份
     * @param endYear 结束年份
     * @param js  这里的js为js字符串。例如 " onchange=\"changeYear()\" " ,这样任何js的方法就可以在jsp页面中编写，方便引入。
     * @return 返回年份的下拉框。
     */
    public static String getYearSelect(String selectName, String value, int startYear, int endYear, String js) {
        int start = startYear;
        int end = endYear;
        if (startYear > endYear) {
            start = endYear;
            end = startYear;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("<select name=\"" + selectName + "\" " + js + ">");
        for (int i = start; i <= end; i++) {
            if (!value.trim().equals("") && i == Integer.parseInt(value)) {
                sb.append("<option value=\"" + i + "\" selected>" + i + "</option>");
            } else {
                sb.append("<option value=\"" + i + "\">" + i + "</option>");
            }
        }
        sb.append("</select>");
        return sb.toString();
    }

    /**
     * 获取月份的下拉框
     * @param selectName
     * @param value
     * @param hasBlank
     * @return 返回月份的下拉框。
     */
    public static String getMonthSelect(String selectName, String value, boolean hasBlank) {
        StringBuffer sb = new StringBuffer();
        sb.append("<select name=\"" + selectName + "\">");
        if (hasBlank) {
            sb.append("<option value=\"\"></option>");
        }
        for (int i = 1; i <= 12; i++) {
            if (!value.trim().equals("") && i == Integer.parseInt(value)) {
                sb.append("<option value=\"" + i + "\" selected>" + i + "</option>");
            } else {
                sb.append("<option value=\"" + i + "\">" + i + "</option>");
            }
        }
        sb.append("</select>");
        return sb.toString();
    }

    /**
     * 获取月份的下拉框
     * @param selectName
     * @param value
     * @param hasBlank
     * @param js
     * @return 返回月份的下拉框。
     */
    public static String getMonthSelect(String selectName, String value, boolean hasBlank, String js) {
        StringBuffer sb = new StringBuffer();
        sb.append("<select name=\"" + selectName + "\" " + js + ">");
        if (hasBlank) {
            sb.append("<option value=\"\"></option>");
        }
        for (int i = 1; i <= 12; i++) {
            if (!value.trim().equals("") && i == Integer.parseInt(value)) {
                sb.append("<option value=\"" + i + "\" selected>" + i + "</option>");
            } else {
                sb.append("<option value=\"" + i + "\">" + i + "</option>");
            }
        }
        sb.append("</select>");
        return sb.toString();
    }

    /**
     * 获取天的下拉框，默认的为1-31。 注意：此方法不能够和月份下拉框进行联动。
     * @param selectName
     * @param value
     * @param hasBlank
     * @return 获得天的下拉框
     */
    public static String getDaySelect(String selectName, String value, boolean hasBlank) {
        StringBuffer sb = new StringBuffer();
        sb.append("<select name=\"" + selectName + "\">");
        if (hasBlank) {
            sb.append("<option value=\"\"></option>");
        }
        for (int i = 1; i <= 31; i++) {
            if (!value.trim().equals("") && i == Integer.parseInt(value)) {
                sb.append("<option value=\"" + i + "\" selected>" + i + "</option>");
            } else {
                sb.append("<option value=\"" + i + "\">" + i + "</option>");
            }
        }
        sb.append("</select>");
        return sb.toString();
    }

    /**
     * 获取天的下拉框，默认的为1-31
     * @param selectName
     * @param value
     * @param hasBlank
     * @param js
     * @return 获取天的下拉框
     */
    public static String getDaySelect(String selectName, String value, boolean hasBlank, String js) {
        StringBuffer sb = new StringBuffer();
        sb.append("<select name=\"" + selectName + "\" " + js + ">");
        if (hasBlank) {
            sb.append("<option value=\"\"></option>");
        }
        for (int i = 1; i <= 31; i++) {
            if (!value.trim().equals("") && i == Integer.parseInt(value)) {
                sb.append("<option value=\"" + i + "\" selected>" + i + "</option>");
            } else {
                sb.append("<option value=\"" + i + "\">" + i + "</option>");
            }
        }
        sb.append("</select>");
        return sb.toString();
    }

    /**
     * 获取天的下拉框，默认的为1-4。 注意：此方法不能够和月份下拉框进行联动。
     * @param selectName
     * @param value
     * @param hasBlank
     * @return 获得季度的下拉框
     */
    public static String getQuarterSelect(String selectName, String value, boolean hasBlank) {
        StringBuffer sb = new StringBuffer();
        sb.append("<select name=\"" + selectName + "\">");
        if (hasBlank) {
            sb.append("<option value=\"\"></option>");
        }
        for (int i = 1; i <= 4; i++) {
            if (!value.trim().equals("") && i == Integer.parseInt(value)) {
                sb.append("<option value=\"" + i + "\" selected>" + i + "</option>");
            } else {
                sb.append("<option value=\"" + i + "\">" + i + "</option>");
            }
        }
        sb.append("</select>");
        return sb.toString();
    }

    /**
     * 获取季度的下拉框，默认的为1-4
     * @param selectName
     * @param value
     * @param hasBlank
     * @param js
     * @return 获取季度的下拉框
     */
    public static String getQuarterSelect(String selectName, String value, boolean hasBlank, String js) {
        StringBuffer sb = new StringBuffer();
        sb.append("<select name=\"" + selectName + "\" " + js + ">");
        if (hasBlank) {
            sb.append("<option value=\"\"></option>");
        }
        for (int i = 1; i <= 4; i++) {
            if (!value.trim().equals("") && i == Integer.parseInt(value)) {
                sb.append("<option value=\"" + i + "\" selected>" + i + "</option>");
            } else {
                sb.append("<option value=\"" + i + "\">" + i + "</option>");
            }
        }
        sb.append("</select>");
        return sb.toString();
    }

    /**
     * 将格式为yyyy或者yyyy.MM或者yyyy.MM.dd的日期转换为yyyy/MM/dd的格式。位数不足的，都补01。<br>
     * 例如.1999 = 1999/01/01 再如：1989.02=1989/02/01
     * @param argDate 需要进行转换的日期。格式可能为yyyy或者yyyy.MM或者yyyy.MM.dd
     * @return 返回格式为yyyy/MM/dd的字符串
     */
    public static String changeDate(String argDate) {
        if (argDate == null || argDate.trim().equals("")) {
            return "";
        }
        String result = "";
        // 如果是格式为yyyy/MM/dd的就直接返回
        if (argDate.length() == 10 && argDate.indexOf("/") > 0) {
            return argDate;
        }
        String[] str = argDate.split("[.]"); // .比较特殊
        int LEN = str.length;
        for (int i = 0; i < LEN; i++) {
            if (str[i].length() == 1) {
                if (str[i].equals("0")) {
                    str[i] = "01";
                } else {
                    str[i] = "0" + str[i];
                }
            }
        }
        if (LEN == 1) {
            result = argDate + "/01/01";
        }
        if (LEN == 2) {
            result = str[0] + "/" + str[1] + "/01";
        }
        if (LEN == 3) {
            result = str[0] + "/" + str[1] + "/" + str[2];
        }
        return result;
    }

    /**
     * 将格式为yyyy或者yyyy.MM或者yyyy.MM.dd的日期转换为yyyy/MM/dd的格式。位数不足的，都补01。<br>
     * 例如.1999 = 1999/01/01 再如：1989.02=1989/02/01
     * @param argDate 需要进行转换的日期。格式可能为yyyy或者yyyy.MM或者yyyy.MM.dd
     * @return 返回格式为yyyy/MM/dd的字符串
     */
    public static String changeDateWithSplit(String argDate,String split) {
        if (argDate == null || argDate.trim().equals("")) {
            return "";
        }
        if (split == null || split.trim().equals("")) {
            split="-";
        }
        String result = "";
        // 如果是格式为yyyy/MM/dd的就直接返回
        if (argDate.length() == 10 && argDate.indexOf("/") > 0) {
            return argDate;
        }
        //   如果是格式为yyyy-MM-dd的就直接返回
        if (argDate.length() == 10 && argDate.indexOf("-") > 0) {
            return argDate;
        }
        String[] str = argDate.split("[.]"); // .比较特殊
        int LEN = str.length;
        for (int i = 0; i < LEN; i++) {
            if (str[i].length() == 1) {
                if (str[i].equals("0")) {
                    str[i] = "01";
                } else {
                    str[i] = "0" + str[i];
                }
            }
        }
        if (LEN == 1) {
            result = argDate + split+"01"+split+"01";
        }
        if (LEN == 2) {
            result = str[0] + split + str[1] + split+ "01";
        }
        if (LEN == 3) {
            result = str[0] + split + str[1] + split + str[2];
        }
        return result;
    }

    /**
     * 返回指定日期的的下一个月的天数。
     * @param argDate 格式为yyyy-MM-dd或者yyyy/MM/dd
     * @return 下一个月的天数。
     */
    public static int getNextMonthDays(String argDate){
        String[] temp = null;
        if (argDate.indexOf("/") > 0) {
            temp = argDate.split("/");
        }
        if (argDate.indexOf("-") > 0) {
            temp = argDate.split("-");
        }
        Calendar cal = new GregorianCalendar(new Integer(temp[0]).intValue(), new Integer(temp[1]).intValue() - 1, new Integer(temp[2]).intValue());
        int curMonth = cal.get(Calendar.MONTH);
        cal.set(Calendar.MONTH,curMonth+1);

        int curyear = cal.get(Calendar.YEAR);// 当前年份
        curMonth = cal.get(Calendar.MONTH);// 当前月份,0-11

        int mArray[] = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        // 判断闰年的情况 ，2月份有29天；
        if ((curyear % 400 == 0) || ((curyear % 100 != 0) && (curyear % 4 == 0))) {
            mArray[1] = 29;
        }
        return mArray[curMonth];
    }

    /**
     * 返回指定为年度为year月度month的月份内，第weekOfMonth个星期的第dayOfWeek天是当月的几号。<br>
     * 00 00 00 01 02 03 04 <br>
     * 05 06 07 08 09 10 11<br>
     * 12 13 14 15 16 17 18<br>
     * 19 20 21 22 23 24 25<br>
     * 26 27 28 29 30 31 <br>
     * 2006年的第一个周的1到7天为：05 06 07 01 02 03 04 <br>
     * 2006年的第二个周的1到7天为：12 13 14 08 09 10 11 <br>
     * 2006年的第三个周的1到7天为：19 20 21 15 16 17 18 <br>
     * 2006年的第四个周的1到7天为：26 27 28 22 23 24 25 <br>
     * 2006年的第五个周的1到7天为：02 03 04 29 30 31 01 。本月没有就自动转到下个月了。
     * @param year 形式为yyyy <br>
     * @param month 形式为MM,参数值在[1-12]。<br>
     * @param weekOfMonth 在[1-6],因为一个月最多有6个周。<br>
     * @param dayOfWeek 数字在1到7之间，包括1和7。1表示星期天，7表示星期六  -6为星期日-1为星期五，0为星期六 <br>
     * @return <type>int</type>
     */
    public static int getDayofWeekInMonth(String year, String month,String weekOfMonth, String dayOfWeek) {
        Calendar cal = new GregorianCalendar();
        // 在具有默认语言环境的默认时区内使用当前时间构造一个默认的 GregorianCalendar。
        int y = new Integer(year).intValue();
        int m = new Integer(month).intValue();
        cal.clear();// 不保留以前的设置
        cal.set(y, m - 1, 1);// 将日期设置为本月的第一天。
        cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, new Integer(weekOfMonth) .intValue());
        cal.set(Calendar.DAY_OF_WEEK, new Integer(dayOfWeek).intValue());
        // System.out.print(cal.get(Calendar.MONTH)+" ");
        // System.out.print("当"+cal.get(Calendar.WEEK_OF_MONTH)+"\t");
        // WEEK_OF_MONTH表示当天在本月的第几个周。不管1号是星期几，都表示在本月的第一个周
        return cal.get(Calendar.DAY_OF_MONTH);
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
