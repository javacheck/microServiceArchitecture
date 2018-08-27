package cn.self.cloud.commonutils.o.jodd_2016.commonUtil;

import org.apache.log4j.Logger;
import org.junit.Test;
import jodd.datetime.JDateTime;
/**
* JODD中的时间操作类
* @author DJZHOU
*
*/
public class JDateTimeUtil {
private Logger log = Logger.getLogger(JDateTimeUtil.class) ;
@Test
public void testConstructor()
{
/*
* 构造函数的使用
*/
JDateTime jdt = new JDateTime();       // set current date and time
jdt = new JDateTime(2012, 12, 21);     // set 21st December 2012, midnight
jdt = new JDateTime(System.currentTimeMillis());
jdt = new JDateTime(2012, 12, 21, 11, 54, 22, 124); // set 21st December 2012, 11:54:22.124
jdt = new JDateTime("2012-12-21 11:54:22.124");    // -//-
jdt = new JDateTime("12/21/2012", "MM/DD/YYYY");   // set 21st December 2012, midnight
}
@Test
public void testSet()
{
JDateTime jdt = new JDateTime();       // set current date and time
/*
* set方法的使用:设定日期时间
*/
jdt.set(2012, 12, 21, 11, 54, 22, 124);    // set 21st December 2012, 11:54:22.124
jdt.set(2012, 12, 21);                     // set 21st December 2012, midnight
jdt.setDate(2012, 12, 21);                 // change date to 21st December 2012, do not change te time
jdt.setCurrentTime();                      // set current date and time
jdt.setYear(1973);                         // change the year
jdt.setHour(22);                           // change the hour
jdt.setTime(18, 00, 12, 853);
}
@Test
public void testGet()
{
JDateTime jdt = new JDateTime();       // set current date and time
/*
* get方法的使用:读取日期和时间
*/
jdt.getYear();
jdt.getDateTimeStamp();
log.warn(jdt.getDateTimeStamp());//获取当前时间
log.warn(jdt.getJulianDate());
log.warn(jdt.getDay()) ;
log.warn(jdt.getDayOfMonth()) ;
log.warn(jdt.getDayOfWeek()) ;
log.warn(jdt.getDayOfYear()) ;
log.warn(jdt.getEra()) ;
log.warn(jdt.getFirstDayOfWeek()) ;
log.warn(jdt.getFormat()) ;
}
@Test
public void testAdd()
{
JDateTime jdt = new JDateTime();       // set current date and time
jdt.add(1, 2, 3, 4, 5, 6, 7);   // add 1 year, 2 months, 3 days, 4 hours...
jdt.add(4, 2, 0);          // add 4 years and 2 months
jdt.addMonth(-120);        // go back 120 months
jdt.subYear(1);            // go back one year
jdt.addHour(1234);         // add 1234 hours
}
@Test
public void testAdd2()
{
JDateTime jdt = new JDateTime() ;
log.warn(jdt.toString("YYYY-MM-DD")) ;
jdt.addDay(-20) ;
log.warn(jdt.toString("YYYY-MM-DD")) ;
jdt.addDay(10, true) ;
log.warn(jdt.toString("YYYY-MM-DD")) ;
jdt.addYear(1);
log.warn(jdt.toString("YYYY-MM-DD")) ;
}
@Test
public void testFormat()
{
JDateTime jdt = new JDateTime();       // set current date and time
/**
* 转换说明
YYYY   年
MM     月
DD     日
D      一周中的第几天 从周一算起
MML    月,长型
MMS    月,短行
DL     一周中的第几天 长型 从周一算起
DS     一周中的第几天 短型 从周一算起
hh     小时
mm     分钟
ss     秒
mss    毫秒
DDD    一年中的第几天
WW     一年中的第几周
WWW    一年中的第几周并用W标识
W      一个月中的第几周
E      年代,公元前还是公元后
TZL    时间长型
TZS    时间短型
*
*/
log.warn(jdt.convertToDate()) ;
log.warn(jdt.toString("YYYY-MM-DD"));
log.warn(jdt.toString("YYYY-MM-DD hh:mm:ss"));//转换成字符串
log.warn(jdt.toString("WW"));//本年度第几周
log.warn(jdt.toString("YYYY")) ;
}
}

