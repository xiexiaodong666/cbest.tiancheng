package com.welfare.common.util;

import org.springframework.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author yanlong.miao
 * @ClassName: DateUtil
 * @Description: 日期工具类
 * @date 2016年8月22日 下午8:12:33
 */
public class DateUtil {
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YEAR = "yyyy";
    public static final String FORMAT_YEAR_MONTH = "yyyy-MM";
    public static final String FORMAT_YEAR_MONTH_DAY = "yyyy-MM-dd";
    public static final String FORMAT_HOUR_MINUTE_SECOND = "HH:mm:ss";
    public static final String CN_DATE_FORMAT = "yyyy年MM月dd日";

    public static final String PURE_FORMATE_YEAR = "yyyy";
    public static final String PURE_FORMATE_MONTH = "yyyyMM";
    public static final String PURE_FORMATE_DAY = "yyyyMMdd";

    /**
     * 按指定格式将字符串转换为日期
     *
     * @param dateStr 日期串
     * @param pattern 格式
     * @return 日期
     * @throws Exception 异常
     */
    public static Date str2Date(String dateStr, String pattern) throws Exception {
        if (null == dateStr) {
            return null;
        }
        if (null == pattern) {
            pattern = DEFAULT_DATE_FORMAT;
        }
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern(pattern);
        return format.parse(dateStr);
    }

    /**
     * 按指定格式将字符串转换为日期时间
     *
     * @param dateStr 日期串
     * @param pattern 格式
     * @return 日期时间
     * @throws ParseException 解析异常
     */
    public static Date str2DateTime(String dateStr, String pattern) throws ParseException {
        if (null == dateStr) {
            return null;
        }
        if (null == pattern) {
            pattern = DEFAULT_DATE_TIME_FORMAT;
        }
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern(pattern);
        return format.parse(dateStr);
    }

    /**
     * 将日期格式化为字符串
     *
     * @param date    日期
     * @param pattern 格式
     * @return 格式化后的日期串
     */
    public static String date2Str(Date date, String pattern) {
        if (null == date) {
            return null;
        }
        if (null == pattern) {
            pattern = DEFAULT_DATE_FORMAT;
        }
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern(pattern);
        return format.format(date);
    }

    public static Integer getAgeByBirthday(Date date) {
        if (null == date) {
            return null;
        }
        String pattern = FORMAT_YEAR;
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern(pattern);
        int year = Integer.parseInt(format.format(date));
        int nowYear = Integer.parseInt(format.format(new Date()));

        return nowYear - year;
    }

    /**
     * 将日期时间格式化为字符串
     *
     * @param date    日期
     * @param pattern 格式
     * @return 格式化后的日期时间串
     */
    public static String dateTime2Str(Date date, String pattern) {
        if (null == date) {
            return null;
        }
        if (null == pattern) {
            pattern = DEFAULT_DATE_TIME_FORMAT;
        }
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern(pattern);
        return format.format(date);
    }

    /**
     * 取得当前时间戳
     *
     * @return 当前时间戳
     */
    public static String getCurrentTime() {
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }

    /**
     * 取得当前日期
     *
     * @return 当前日期
     */
    public static String thisDate() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(calendar.getTime());
    }

    /**
     * 取得当前日期
     *
     * @return 当前日期
     */
    public static String thisDate(String pattern) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        return new SimpleDateFormat(pattern).format(calendar.getTime());
    }

    /**
     * 取得当前时间
     *
     * @return 当前时间
     */
    public static String thisTime() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        return new SimpleDateFormat(DEFAULT_TIME_FORMAT).format(calendar.getTime());
    }

    /**
     * 取得当前完整日期时间
     *
     * @return 当前完整日期时间
     */
    public static String thisDateTime() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        return new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT).format(calendar.getTime());
    }

    /**
     * 获取某月最后一天的日期数字
     *
     * @param firstDate 日期
     * @return 某月最后一天的日期数字
     * @throws Exception 异常
     */
    public static int getLastDayOfMonth(Date firstDate) throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.setTime(firstDate);
        return cal.getActualMaximum(Calendar.DATE);
    }

    /**
     * 取得今天的最小时间
     *
     * @return 今天的最小时间
     */
    public static Date getTodayMin() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    /**
     * 取得今天的最大时间
     *
     * @return 今天的最大时间
     */
    public static Date getTodayMax() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);

        return cal.getTime();
    }

    /**
     * 取得明天的最小时间
     *
     * @return 明天的最小时间
     */
    public static Date getTomorrowMin() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, 1);

        return cal.getTime();
    }

    /**
     * 取得明天的最大时间
     *
     * @return 明天的最大时间
     */
    public static Date getTomorrowMax() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        cal.add(Calendar.DATE, 1);

        return cal.getTime();
    }

    /**
     * 由指定时间、时间域、数额，计算时间值
     *
     * @param standard 指定时间
     * @param type     时间域
     * @param amount   数额
     * @return 时间值
     */
    public static Date genDiffDate(Date standard, int type, int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(standard);
        cal.add(type, amount);

        return cal.getTime();
    }

    /**
     * 生成指定时间所在的小时段（清空：分钟、秒、毫秒）
     *
     * @param standard 指定时间
     * @return 指定时间所在的小时段
     */
    public static Date genHourStart(Date standard) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(standard);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    /**
     * 取得当前天后，减去指定天数后的最小时间
     *
     * @param date      当前日期
     * @param beforeDay 天数
     * @return 当前天后，减去指定天数后的最小时间
     */
    public static Date getBeforeDayMin(Date date, int beforeDay) {

        return getDayMin(date, -beforeDay);
    }

    /**
     * 取得当前天后，减去指定天数后的最大时间
     *
     * @param date      当前日期
     * @param beforeDay 天数
     * @return 当前天后，减去指定天数后的最大时间
     */
    public static Date getBeforeDayMax(Date date, int beforeDay) {

        return getDayMax(date, -beforeDay);
    }

    /**
     * 取得当前天后，加上指定天数后的最小时间
     *
     * @param date     当前日期
     * @param afterDay 天数
     * @return 当前天后，加上指定天数后的最小时间
     */
    public static Date getAfterDayMin(Date date, int afterDay) {

        return getDayMin(date, afterDay);
    }

    /**
     * 取得当前天后，加上指定天数后的最大时间
     *
     * @param date     当前日期
     * @param afterDay 天数
     * @return 当前天后，加上指定天数后的最大时间
     */
    public static Date getAfterDayMax(Date date, int afterDay) {

        return getDayMax(date, afterDay);
    }

    /**
     * 取得当前天后，加上指定天数后的最小时间
     *
     * @param date   当前日期
     * @param addDay 天数
     * @return 当前天后，加上指定天数后的最小时间
     */
    public static Date getDayMin(Date date, int addDay) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, addDay);

        return cal.getTime();
    }

    /**
     * 取得当前天 ,加上指定天数后的最大时间
     *
     * @param date   当前日期
     * @param addDay 天数
     * @return 当前天 ,加上指定天数后的最大时间
     */
    public static Date getDayMax(Date date, int addDay) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        cal.add(Calendar.DATE, addDay);

        return cal.getTime();
    }

    /**
     * 通过月份获取当月最大日期
     *
     * @param monthStr
     * @return
     * @throws ParseException
     */
    public static Date getDayMaxByMontStr(String monthStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_YEAR_MONTH);
        Date date = format.parse(monthStr);

        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));

        return ca.getTime();
    }

    /**
     * 通过月份获取当月最小日期
     *
     * @param monthStr
     * @return
     * @throws ParseException
     */
    public static Date getDayMinByMonthStr(String monthStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_YEAR_MONTH);
        Date date = format.parse(monthStr);

        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMinimum(Calendar.DAY_OF_MONTH));

        return ca.getTime();
    }

    /**
     * 根据传入日期获取对应月分最大日期
     *
     * @param date
     * @return
     */
    public static Date getMonthDayMaxByDate(Date date) {

        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));

        return ca.getTime();
    }

    /**
     * 根据传入日期获取对应月分最小日期
     *
     * @param date
     * @return
     */
    public static Date getMonthDayMinByDate(Date date) {

        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMinimum(Calendar.DAY_OF_MONTH));

        return ca.getTime();
    }


    public static Date getDaySecondMax(Date date, int addDay) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.add(Calendar.DATE, addDay);
        return cal.getTime();
    }

    /**
     * 取得当前天 ,加上指定天数后的时间
     *
     * @param date   当前日期
     * @param addDay 天数
     * @return 当前天 ,加上指定天数后的时间
     */
    public static Date addDays(Date date, int addDay) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, addDay);
        return cal.getTime();
    }

    /**
     * 取得当前天 ,加上指定月份数后的时间
     *
     * @param date   当前日期
     * @param months 月份数
     * @return 当前天 ,加上指定月份数后的时间
     */
    public static Date addMonths(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    /**
     * 日期差天数(按照时间比较,如果不足一天会自动补足)
     *
     * @param date1 开始日期
     * @param date2 结束日期
     * @return 两日期差天数
     * @throws Exception
     */
    public static int diff(Date date1, Date date2) throws Exception {
        long day = 24L * 60L * 60L * 1000L;
        String str1 = date2Str(date1, "yyyy-MM-dd");
        date1 = str2Date(str1, "yyyy-MM-dd");
        String str2 = date2Str(date2, "yyyy-MM-dd");
        date2 = str2Date(str2, "yyyy-MM-dd");
        Assert.notNull(date1, "date1日期为空");
        Assert.notNull(date2, "date2日期为空");
        return (int) (((date2.getTime() - date1.getTime()) / day));
        //return (int) Math.ceil((((date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000d))));
    }

    /**
     * 日期差天数(和当前时间比)
     *
     * @param date 比较日期
     * @return 和当前日期差天数
     * @throws Exception
     */
    public static int diff(Date date) throws Exception {
        return diff(new Date(), date);
    }

    /**
     * 按固定格式比较两个日期
     *
     * @param date1   日期
     * @param date2   日期2
     * @param pattern 格式 	默认：yyyy-MM-dd
     * @return 比较结果
     */
    public static int compareDate(Date date1, Date date2, String pattern) {
        String d1 = date2Str(date1, pattern);
        String d2 = date2Str(date2, pattern);
        Assert.notNull(d1, "d1日期为空");
        Assert.notNull(d2, "d2日期为空");
        return d1.compareTo(d2);
    }

    /**
     * 按固定格式比较两个日期+时间
     *
     * @param time1   日期时间
     * @param time2   日期时间2
     * @param pattern 格式	默认： yyyy-MM-dd HH:mm:ss
     * @return 比较结果
     */
    public static int compareDateTime(Date time1, Date time2, String pattern) {
        String d1 = dateTime2Str(time1, pattern);
        String d2 = dateTime2Str(time2, pattern);
		Assert.notNull(d1, "d1日期为空");
		Assert.notNull(d2, "d2日期为空");
        return d1.compareTo(d2);
    }

    /**
     * 判断是否是闰年
     *
     * @param date 日期
     * @return boolean
     */
    public static boolean isLeapyear(Date date) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        return gregorianCalendar.isLeapYear(gregorianCalendar.get(Calendar.YEAR));
    }

    /**
     * 根据传入日期得到本月月末
     *
     * @param date 传入日期
     * @return date 月末日期
     * @author sy
     */
    public static Date getLastDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return getLastDateOfMonth(c);
    }

    /**
     * Description: 根据传入日期得到本月月初
     *
     * @param
     * @return Date
     * @throws
     * @Author liufei
     * Create Date: 2013-11-14 下午7:22:53
     */
    public static Date getFirstDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return getFirstDateOfMonth(c);
    }

    /**
     * 根据传入日期得到本月月初
     *
     * @param calendar 传入日期
     * @return date 月末日期
     * @author sy
     */
    public static Date getFirstDateOfMonth(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 根据传入日期得到本月月末
     *
     * @param calendar 传入日期
     * @return date 月末日期
     * @author sy
     */
    public static Date getLastDateOfMonth(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 根据传入日期得到本月月末，如果传入日期为月末则返回传入日期
     *
     * @param date 传入日期
     * @return boolean true为是
     * @author sy
     */
    public static boolean isLastDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return isLastDateOfMonth(c);
    }

    /**
     * 根据传入日期得到本月月末，如果传入日期为月末则返回传入日期
     *
     * @param date 传入日期
     * @return boolean false为不是
     * @author sy
     */
    public static boolean isLastDateOfMonth(Calendar date) {
        if (date.getActualMaximum(Calendar.DAY_OF_MONTH) == date.get(Calendar.DAY_OF_MONTH)) {
            return true;
        }
        return false;
    }

    /**
     * 根据日期得到年份
     *
     * @param date 日期
     * @return 年份
     */
    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 根据日期得到月份
     *
     * @param date 日期
     * @return 月份
     */
    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 根据日期得到日
     *
     * @param date 日期
     * @return 日
     */
    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 时间格式化
     *
     * @param millonSeconds 毫秒
     * @param language      语言
     * @return time
     */
    public static String formatMilliseconds(long millonSeconds, String language) {
        String v = "";
        long second = millonSeconds / 1000;//转换为秒
        long millonSecond = millonSeconds - second * 1000;//多出的不足一秒的毫秒数
        boolean isChinese = language.equalsIgnoreCase("chinese");
        if (isChinese) {
            v += millonSecond + "毫秒";
        } else {
            v += millonSecond + "ms.";
        }
        if (second > 0)//如果还有秒
        {
            long minutes = second / 60;//分钟取整
            second = second - minutes * 60;//不足一分钟的秒数
            if (isChinese) {
                v = second + "秒" + v;
            } else {
                v = second + "s" + v;
            }
            if (minutes > 0)//如果还有分钟
            {
                long hours = minutes / 60;//小时取整
                minutes = minutes - hours * 60;//不足一小时的分钟数
                if (isChinese) {
                    v = minutes + "分" + v;
                } else {
                    v = minutes + "minutes " + v;
                }
                if (hours > 0) {
                    long days = hours / 24;//天取整
                    hours = hours - days * 24;//不足一天的小时数
                    if (isChinese) {
                        v = hours + "小时" + v;
                    } else {
                        v = hours + "hours " + v;
                    }
                    if (days > 0) {
                        if (isChinese) {
                            v += days + "天" + v;
                        } else {
                            v += days + " days " + v;
                        }
                    }
                }
            }
        }
        return v;
    }

    /**
     * 时间格式化
     *
     * @param millonSeconds 毫秒
     * @return time
     */
    public static String formatMilliseconds(long millonSeconds) {

        return formatMilliseconds(millonSeconds, "CHINESE");
    }

    /**
     * 判断当前日期是否在某段日期范围之内
     *
     * @param sendStartTime
     * @param sendEndTime
     * @return
     */
    public static boolean isSendDate(Date sendStartTime, Date sendEndTime) {
        if (sendStartTime == null && sendEndTime == null) {
            return true;
        }
        Date date = new Date();
        if ((sendStartTime == null || date.compareTo(sendStartTime) > -1) &&
                (sendEndTime == null || date.compareTo(sendEndTime) < 0)) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前时间点是否在某段时间范围之内
     *
     * @param sendStartTime
     * @param sendEndTime
     * @return
     */
    public static boolean isSendTime(Date sendStartTime, Date sendEndTime) {
        if (sendStartTime == null && sendEndTime == null) {
            return true;
        }
        Date date = new Date();

        long startTime = new Long(date2Str(sendStartTime, "HHmmss"));
        long endTime = new Long(date2Str(sendEndTime, "HHmmss"));
        long currentTime = new Long(date2Str(date, "HHmmss"));

        if (currentTime >= startTime && currentTime <= endTime) {
            return true;
        }
        return false;
    }

    public static boolean isCurrentDate(Date date) {
        Calendar currentDate = Calendar.getInstance();
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);

        return currentDate.get(Calendar.YEAR) == ca.get(Calendar.YEAR) &&
                currentDate.get(Calendar.MONTH) == ca.get(Calendar.MONTH) &&
                currentDate.get(Calendar.DAY_OF_MONTH) == ca.get(Calendar.DAY_OF_MONTH);
    }

    public static Date getNow() {
        return new Date();
    }
}
