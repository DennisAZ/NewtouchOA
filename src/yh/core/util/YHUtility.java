package yh.core.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;

import yh.core.data.YHMapComparator;
import yh.core.exps.YHInvalidParamException;
import yh.core.global.YHConst;
import yh.core.global.YHTokenConst;

public class YHUtility {
  //log
  private static Logger log = null;
  //三个一分组，保留两位小数
  public static final int WITHGROUP = 0;
  //不分组，保留两位小数
  public static final int WITHOUTGROUP = 1;

  //数字串的格式（在多用户的环境中使用静态是不合适的）
  private static DecimalFormat numFormat = new DecimalFormat("#0.00");
  private static DecimalFormat numFormatG = new DecimalFormat("#,##0.00");
  private static FastDateFormat dateFormat =
	  FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
  private static FastDateFormat dateFormatS =
	  FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss:SSS");
  public static String DATE_FORMAT_NOSPLIT = "yyyyMMddHHmmssSSS";
  private static FastDateFormat dateFormatEn =
	  FastDateFormat.getInstance("dd-MMM-yyyy", Locale.ENGLISH);
  private static FastDateFormat dateFormatCn =
	  FastDateFormat.getInstance("yyyy年MM月dd日", Locale.CHINESE);
  private static FastDateFormat dateFormatYMEn =
	  FastDateFormat.getInstance("MMM-yyyy", Locale.ENGLISH);
  private static FastDateFormat dateFormatYMCn =
	  FastDateFormat.getInstance("yyyy年MM月", Locale.CHINESE);
  private static FastDateFormat dateFormatMDEn =
	  FastDateFormat.getInstance("dd-MMM", Locale.ENGLISH);
  private static FastDateFormat dateFormatMDCn =
	  FastDateFormat.getInstance("MM-dd", Locale.CHINESE);
  
  private static final String[] enSmallNumber = {"", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "ELEVEN", "TWELVE", "THIRTEEN", "FOURTEEN", "FIFTEEN", "SIXTEEN", "SEVENTEEN", "EIGHTEEN", "NINETEEN" };
  private static final String[] enLargeNumber = {"TWENTY", "THIRTY", "FORTY", "FIFTY", "SIXTY", "SEVENTY", "EIGHTY", "NINETY" };
  private static final String[] enUnit = {"", "THOUSAND", "MILLION", "BILLION", "TRILLION" };
  
  /**
   * 判断是否为空串
   * @param str
   * @return    true: null/""
   *            false: 其它
   */
  public static boolean isNullorEmpty(String str) {
    if (str == null) {
      return true;
    }
    str = str.trim();
    if (str.length() < 1) {
      return true;
    }
    return false;
  }
  
  /**
   * 处理空值的字符串
   * @param str
   * @return
   */
  public static String null2Empty(String str) {
    if (str == null) {
      return "";
    }
    return str;
  }
  
  /**
   * 处理空值的字符串
   * @param str             源字符串
   * @param defaultStr      默认字符串
   * @return
   */
  public static String empty2Default(String str,
      String defaultStr) {

    if (isNullorEmpty(str)) {
      return defaultStr;
    }
    return str;
  }
  
  /**
   * 取得格式化后的字符串
   * @param num
   * @return
   */
  public static String getFormatedStr(BigDecimal num, int pattern) {
    if (pattern == WITHOUTGROUP) {
      return numFormat.format(num);
    }
    return numFormatG.format(num);
  }
  
  /**
   * 取得格式化后的字符串
   * @param num
   * @return
   */
  public static String getFormatedStr(double num, int pattern) {
    if (pattern == WITHOUTGROUP) {
      return numFormat.format(num);
    }
    return numFormatG.format(num);
  }

  /**
   * 取得格式化后的字符串
   * @param num
   * @return
   */
  public static String getFormatedStr(String numStr, int pattern) {
    String rtStr = "0.00";
    try {
      double dValue = parseDouble(numStr);
      if (pattern == WITHOUTGROUP) {
        rtStr = numFormat.format(dValue);
      }else {
        rtStr = numFormatG.format(dValue);
      }
    } catch (Exception ex) { //非数字
    }
    return rtStr;
  }

  /**
   * 取得当前的时间，格式为 yyyy-MM-dd HH:mm:ss 
   * @return
   */
  public static String getCurDateTimeStr() {
    return dateFormat.format(new Date());
  }
  
  /**
   * 取得当前的时间，格式为 yyyy-MM-dd HH:mm:ss 
   * @return
   */
  public static String getCurDateTimeStr(String pattern) {
	    if (pattern == null) {
	      return getCurDateTimeStr();
	    }
	    FastDateFormat format = FastDateFormat.getInstance(pattern);
	    return format.format(new Date());
	  }
  /**
   * 取得当前的时间，格式为 yyyy-MM-dd HH:mm:ss 
   * @return
   */
  public static String getCurDateTimeStr(FastDateFormat format) {
	    if (format == null) {
	      return getCurDateTimeStr();
	    }
	    return format.format(new Date());
	  }
  
  /**
   * 取得的时间串，格式为 yyyy-MM-dd HH:mm:ss 
   * @return
   */
  public static String getDateTimeStr(Date date) {
    if (date == null) {
      return getCurDateTimeStr();
    }
    return dateFormat.format(date);
  }
  
  /**
   * 取得的英文习惯的时间串，格式为 dd-MMM-yyyy
   * @return
   */
  public static String getDateTimeStrEn(Date date) {
	    if (date == null) {
	      return getCurDateTimeStr(dateFormatEn);
	    }
	    return dateFormatEn.format(date);
	  }
  /**
   * 取得的中文习惯的时间串，格式为 yyyy年MM月dd日 
   * @return
   */
  public static String getDateTimeStrCn(Date date) {
    if (date == null) {
      return getCurDateTimeStr(dateFormatCn);
    }
    return dateFormatCn.format(date);
  }
  
  /**
   * 取得的英文习惯的时间串，格式为 dd-MMM-yyyy 
   * @return
   */
  public static String getDateTimeStrEn(String dateStr) throws Exception {
    if (dateStr == null) {
      return getCurDateTimeStr(dateFormatEn);
    }
    return getDateTimeStrEn(parseDate(dateStr));
  }
  /**
   * 取得的中文习惯的时间串，格式为 yyyy年MM月dd日 
   * @return
   */
  public static String getDateTimeStrCn(String dateStr) throws Exception {
    if (dateStr == null) {
      return getCurDateTimeStr(dateFormatCn);
    }
    return getDateTimeStrCn(parseDate(dateStr));
  }
  
  /**
   * 取得的英文习惯的时间串，格式为 dd-MMM-yyyy
   * @return
   */
  public static String getDateTimeStrYMEn(Date date) {
    if (date == null) {
      return getCurDateTimeStr(dateFormatYMEn);
    }
    return dateFormatYMEn.format(date);
  }
  /**
   * 取得的中文习惯的时间串，格式为 yyyy年MM月dd日 
   * @return
   */
  public static String getDateTimeStrYMCn(Date date) {
    if (date == null) {
      return getCurDateTimeStr(dateFormatYMCn);
    }
    return dateFormatYMCn.format(date);
  }
  
  /**
   * 取得的英文习惯的时间串，格式为 dd-MMM-yyyy 
   * @return
   */
  public static String getDateTimeStrYMEn(String dateStr) throws Exception {
    if (dateStr == null) {
      return getCurDateTimeStr(dateFormatYMEn);
    }
    return getDateTimeStrYMEn(parseDate(dateStr));
  }
  /**
   * 取得的中文习惯的时间串，格式为 yyyy年MM月dd日 
   * @return
   */
  public static String getDateTimeStrYMCn(String dateStr) throws Exception {
    if (dateStr == null) {
      return getCurDateTimeStr(dateFormatYMCn);
    }
    return getDateTimeStrYMCn(parseDate(dateStr));
  }
  
  /**
   * 取得的英文习惯的时间串，格式为 dd-MMM-yyyy
   * @return
   */
  public static String getDateTimeStrMDEn(Date date) {
    if (date == null) {
      return getCurDateTimeStr(dateFormatMDEn);
    }
    return dateFormatMDEn.format(date);
  }
  /**
   * 取得的中文习惯的时间串，格式为 yyyy年MM月dd日 
   * @return
   */
  public static String getDateTimeStrMDCn(Date date) {
    if (date == null) {
      return getCurDateTimeStr(dateFormatMDCn);
    }
    return dateFormatMDCn.format(date);
  }
  
  /**
   * 取得的英文习惯的时间串，格式为 dd-MMM-yyyy 
   * @return
   */
  public static String getDateTimeStrMDEn(String dateStr) throws Exception {
    if (dateStr == null) {
      return getCurDateTimeStr(dateFormatMDEn);
    }
    return getDateTimeStrMDEn(parseDate(dateStr));
  }
  /**
   * 取得的中文习惯的时间串，格式为 yyyy年MM月dd日 
   * @return
   */
  public static String getDateTimeStrMDCn(String dateStr) throws Exception {
    if (dateStr == null) {
      return getCurDateTimeStr(dateFormatMDCn);
    }
    return getDateTimeStrMDCn(parseDate(dateStr));
  }
  /**
   * Date清零
   * @param date 
   * @param clearNum 1=毫秒, 2=秒, 3=分钟, 4=小时, 5=天, 6=月份
   * @return
   */
  private static Calendar clearDate(Date date, int clearNum) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    //毫秒
    if (clearNum > 0) {
      cal.set(Calendar.MILLISECOND, 0);
    }
    //秒
    if (clearNum > 1) {
      cal.set(Calendar.SECOND, 0);
    }
    //分钟
    if (clearNum > 2) {
      cal.set(Calendar.MINUTE, 0);
    }
    //小时
    if (clearNum > 3) {
      cal.set(Calendar.HOUR_OF_DAY, 0);
    }
    //天
    if (clearNum > 4) {
      cal.set(Calendar.DATE, 0);
    }
    //月份
    if (clearNum > 5) {
      cal.set(Calendar.MONTH, 0);
    }
    return cal;
  }
  
  /**
   * 取得指定日期的起始时间串
   * @param date
   * @return
   */
  public static String[] getDateLimitStr() throws Exception {   
    return getDateLimitStr(new Date());
  }
  /**
   * 取得指定日期当周的起始时间串
   * @param date
   * @return
   */
  public static String[] getWeekLimitStr() throws Exception {   
    return getWeekLimitStr(new Date());
  }
  /**
   * 取得指定日期当月的起始时间串
   * @param date
   * @return
   */
  public static String[] getMonthLimitStr() throws Exception {   
    return getMonthLimitStr(new Date());
  }
  /**
   * 取得指定日期当年的起始时间串
   * @param date
   * @return
   */
  public static String[] getYearLimitStr() throws Exception {   
    return getYearLimitStr(new Date());
  }
  
  /**
   * 取得指定日期的起始时间串
   * @param date
   * @return
   */
  public static String[] getDateLimitStr(Date date) throws Exception {
    Date[] rtDateArray = getDateLimit(date);
    return new String[]{getDateTimeStr(rtDateArray[0]), getDateTimeStr(rtDateArray[1])};
  }
  /**
   * 取得指定日期当周的起始时间串
   * @param date
   * @return
   */
  public static String[] getWeekLimitStr(Date date) throws Exception {
    Date[] rtDateArray = getWeekLimit(date);
    return new String[]{getDateTimeStr(rtDateArray[0]), getDateTimeStr(rtDateArray[1])};
  }
  /**
   * 取得指定日期当月的起始时间串
   * @param date
   * @return
   */
  public static String[] getMonthLimitStr(Date date) throws Exception {
    Date[] rtDateArray = getMonthLimit(date);
    return new String[]{getDateTimeStr(rtDateArray[0]), getDateTimeStr(rtDateArray[1])};
  }
  /**
   * 取得指定日期当年的起始时间串
   * @param date
   * @return
   */
  public static String[] getYearLimitStr(Date date) throws Exception {
    Date[] rtDateArray = getYearLimit(date);
    return new String[]{getDateTimeStr(rtDateArray[0]), getDateTimeStr(rtDateArray[1])};
  }
  
  /**
   * 取得指定日期的起始时间
   * @param date
   * @return
   */
  public static Date[] getDateLimit(Date date) throws Exception {
    Calendar cal = clearDate(date, 4);
    Date date1 = cal.getTime();
    
    cal.add(Calendar.DATE, 1);
    cal.add(Calendar.SECOND, -1);
    Date date2 = cal.getTime();
    
    return new Date[]{date1, date2};
  }

  /**
   * 取得当前日期所在周的第一天
   * @param date
   * @return
   */
  public static Date getFirstDayOfWeek(Date date) { 
    Calendar c = clearDate(date, 4);
    c.setFirstDayOfWeek(Calendar.MONDAY);
    c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
    return c.getTime ();
  }
  /**
   * 取得当前日期所在周的最后一天
   * @param date
   * @return
   */
  public static Date getLastDayOfWeek(Date date) {
    Calendar c = clearDate(date, 4);
    c.setFirstDayOfWeek(Calendar.MONDAY);
    c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6);
    c.set(Calendar.HOUR_OF_DAY, 23);
    c.set(Calendar.MINUTE, 59);
    c.set(Calendar.SECOND, 59);
    c.set(Calendar.MILLISECOND, 000);
    return c.getTime();
  }
  
  /**
   * 取得指定日期的当周的起始时间
   * @param date
   * @return
   */
  public static Date[] getWeekLimit(Date date) throws Exception {
    Date date1 = getFirstDayOfWeek(date);
    Date date2 = getLastDayOfWeek(date);
    return new Date[]{date1, date2};
  }
  
  /**
   * 取得指定日期的当月起始时间
   * @param date
   * @return
   */
  public static Date[] getMonthLimit(Date date) throws Exception {
    Calendar cal = clearDate(date, 5);
    cal.set(Calendar.DATE, 1);
    Date date1 = cal.getTime();
    
    cal.add(Calendar.MONTH, 1);
    cal.add(Calendar.SECOND, -1);
    Date date2 = cal.getTime();
    
    return new Date[]{date1, date2};
  }

  /**
   * 取得指定日期的当年起始时间
   * @param date
   * @return
   */
  public static Date[] getYearLimit(Date date) throws Exception {
    Calendar cal = clearDate(date, 6);    
    cal.set(Calendar.MONTH, 0);
    cal.set(Calendar.DATE, 1);
    Date date1 = cal.getTime();
    
    cal.add(Calendar.YEAR, 1);
    cal.add(Calendar.SECOND, -1);
    Date date2 = cal.getTime();
    
    return new Date[]{date1, date2};
  }
  
  /**
   * 取得天数间隔
   * @param fromDate
   * @param toDate
   * @return
   */
  public static int getDaySpan(String toDateStr) throws Exception {
    return (int)((parseDate(toDateStr).getTime() - new Date().getTime()) / YHConst.DT_D);
  }
  
  /**
   * 取得天数间隔
   * @param fromDate
   * @param toDate
   * @return
   */
  public static int getDaySpan(Date toDate) {
    return (int)((toDate.getTime() - new Date().getTime()) / YHConst.DT_D);
  }
  
  /**
   * 取得天数间隔
   * @param fromDate
   * @param toDate
   * @return
   */
  public static int getDaySpan(Date fromDate, Date toDate) {
    return (int)((toDate.getTime() - fromDate.getTime()) / YHConst.DT_D);
  }
  
  /**
   * 取得前一天的时间
   * @param dateStr
   * @return
   */
  public static Date getDayBefore(String dateStr, int dayCnt) throws Exception {
    return getDayBefore(parseDate(dateStr), dayCnt);
  }
  
  /**
   * 取得前一天的时间
   * @param date
   * @return
   */
  public static Date getDayBefore(Date date, int dayCnt) {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(date);
    cal.add(Calendar.DATE, 0 - dayCnt);
    return cal.getTime();
  }
  
  /**
   * 取得后一天的时间字
   * @param dateStr
   * @return
   */
  public static Date getDayAfter(String dateStr, int dayCnt) throws Exception {
    return getDayAfter(parseDate(dateStr), dayCnt);
  }
  
  /**
   * 取得后一天的时间
   * @param date
   * @return
   */
  public static Date getDayAfter(Date date, int dayCnt) {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(date);
    cal.add(Calendar.DATE, dayCnt);
    return cal.getTime();
  }
  
  /**
   * 取得指定天数差的时间字
   * @param dateStr
   * @return
   */
  public static Date getDayDiff(String dateStr, int dayCnt) throws Exception {
    return getDayDiff(parseDate(dateStr), dayCnt);
  }
  
  /**
   * 取得指定天数差的时间
   * @param date
   * @return
   */
  public static Date getDayDiff(Date date, int dayCnt) {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(date);
    cal.add(Calendar.DATE, dayCnt);
    return cal.getTime();
  }
  
  /**
   * 取得前一天的时间字
   * @param dateStr
   * @return
   */
  public static Date getYestday(String dateStr) throws Exception {
    return getYestday(parseDate(dateStr));
  }
  
  /**
   * 取得前一天的时间
   * @param date
   * @return
   */
  public static Date getYestday(Date date) {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(date);
    cal.add(Calendar.DATE, -1);
    return cal.getTime();
  }
  
  /**
   * 取得前一天的时间字符串
   * @param dateStr
   * @return
   */
  public static String getYestdayStr(String dateStr) throws Exception {
    return getYestdayStr(parseDate(dateStr));
  }
  
  /**
   * 取得前一天的时间字符串
   * @param date
   * @return
   */
  public static String getYestdayStr(Date date) {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(date);
    cal.add(Calendar.DATE, -1);
    return getDateTimeStr(cal.getTime());
  }
  
  /**
   * 取得前一月的时间字符串
   * @param date
   * @return
   */
  public static String getMonthBefore(String dateStr, int diff) throws Exception {
    return getMonthBefore(parseDate(dateStr), diff);
  }
  
  /**
   * 取得前一月的时间字符串
   * @param date
   * @return
   */
  public static String getMonthBefore(Date date, int diff) {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(date);
    cal.add(Calendar.MONTH, 0 - diff);
    return getDateTimeStr(cal.getTime());
  }
  
  /**
   * 取得前一月的时间字符串
   * @param date
   * @return
   */
  public static String getMonthAfter(String dateStr, int diff) throws Exception {
    return getMonthAfter(parseDate(dateStr), diff);
  }
  
  /**
   * 取得前一月的时间字符串
   * @param date
   * @return
   */
  public static String getMonthAfter(Date date, int diff) {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(date);
    cal.add(Calendar.MONTH, 0 + diff);
    return getDateTimeStr(cal.getTime());
  }

  /**
   * 从以“逗号”分隔的数串取得double类型的值
   * @param numStr
   * @return
   */
  public static double getDoubleFromStr(String numStr) {
    double rtValue = 0.00d;
    try {
      rtValue = numFormatG.parse(numStr).doubleValue();
    } catch (Exception ex) {
    }
    return rtValue;
  }

  /**
   * 从以“逗号”分隔的数串取得long类型的值
   * @param numStr
   * @return
   */
  public static long getLongFromStr(String numStr) {
    long rtValue = 0;
    try {
      rtValue = numFormatG.parse(numStr).longValue();
    } catch (Exception ex) {
    }
    return rtValue;
  }

  /**
   * 从以“逗号”分隔的数串取得int类型的值
   * @param numStr
   * @return
   */
  public static int getIntFromStr(String numStr) {
    int rtValue = 0;
    try {
      rtValue = numFormatG.parse(numStr).intValue();
    } catch (Exception ex) {
    }
    return rtValue;
  }

  /**
   * 取得文件的精确的大小，单位是字节
   * @return
   */
  public static long getFileSize(String fileName) {
    long rtSize = 0;
    File file = new File(fileName);
    rtSize = file.length();
    return rtSize;
  }

  public static String getRequestPath(String uri) {
    String rtStr = null;
    int tmpIndex = uri.lastIndexOf("/");
    if (tmpIndex >= 0) {
      rtStr = uri.substring(0, tmpIndex);
    }
    return rtStr;
  }

  /**
   * 取得指定长度的数字
   * @param str     原始的数串
   * @param length  长度
   * @return        length <= str.length str
   *                length > str.length  字符串前面补零到指定长度的字符串
   */
  public static String getFixLengthNum(String str, int length) {
    if (str == null) {
      str = "";
    }
    String preFix = "";
    for (int i = 0; i < length - str.length(); i++) {
      preFix += "0";
    }
    return preFix + str;
  }
  
  /**
   * 10进制转换为36进制字符串
   * @param num                   10进制数字
   * @param length                返回字符串长度
   * @return                      36进制字符串
   * @throws Exception
   */
  public static String get36BaseStr(long num, int length) throws Exception {
    String numStr = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    long[] tmpArray = get10ArrayFromNum(num, 36, new long[]{});
    String tmpStr = "";
    for (int i = 0; i < tmpArray.length; i++) {
      tmpStr += String.valueOf(numStr.charAt((int)tmpArray[i]));
    }
    String rtStr = getFixLengthNum(tmpStr, length);
    return rtStr;
  }
  
  /**
   * 36进制数转换为10进制数
   * @param srcNum             36进制数字串
   * @return
   * @throws Exception
   */
  public static long get10BaseNum(String srcNum) throws Exception {
    if (srcNum == null) {
      return 0;
    }
    String numStr = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    char[] charArray = srcNum.toCharArray();
    int exponent = 0;
    long num36 = 0;
    for (int i = charArray.length - 1; i >= 0; i--) {
      char tmpChar = charArray[i];
      int tmpNum = numStr.indexOf(tmpChar);
      if (tmpNum >= 0) {
        num36 += tmpNum * Math.pow(36, exponent++);
      }else {
        throw new Exception(YHI18n.ln(YHUtility.class, "1"));
      }
    }
    return num36;
  }

  /**
   * 取得下一个固定长度的编码，36进制
   * @param srcNum   36进制的数串
   * @param length   返回字符串的长度
   * @param diff     返回数字与当前数字的差
   * @return         36进制表示的数串
   */
  public static String getNextNum(String srcNum,
      int diff, int length) throws Exception {
    String rtStr = null;
    if (srcNum == null || length <= 0) {
      return rtStr;
    }
    String numStr = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    char[] charArray = srcNum.toCharArray();
    int exponent = 0;
    long num36 = 0;
    for (int i = charArray.length - 1; i >= 0; i--) {
      char tmpChar = charArray[i];
      int tmpNum = numStr.indexOf(tmpChar);
      if (tmpNum >= 0) {
        num36 += tmpNum * Math.pow(36, exponent++);
      }else {
//        throw new Exception("无效的36进制数字");
        throw new Exception(YHI18n.ln(YHUtility.class, "1"));
      }
    }
    num36 = num36 + diff;
    long[] tmpArray = get10ArrayFromNum(num36, 36, new long[]{});
    String tmpStr = "";
    for (int i = 0; i < tmpArray.length; i++) {
      tmpStr += String.valueOf(numStr.charAt((int)tmpArray[i]));
    }
    rtStr = getFixLengthNum(tmpStr, length);
    return rtStr;
  }

  /**
   * 把任意进制的数转换为十进制数的数组
   * @param num       十进制表示的任意进制的数
   * @param baseNum   基数
   * @param num10
   * @return
   * @throws java.lang.Exception
   */
  public static long[] get10ArrayFromNum(long num,
    int baseNum, long[] num10) throws Exception {
    long[] tmpArray = new long[num10.length + 1];
    tmpArray[0] = num % baseNum;
    if (num10.length > 0) {
      System.arraycopy(num10, 0, tmpArray, 1, num10.length);
    }
    if (num / baseNum == 0) {
      return tmpArray;
    }else {
      return get10ArrayFromNum(num / baseNum, baseNum, tmpArray);
    }
  }

  /**
   * 把字符串前面补“0”到length长度
   * @param str 需要补足位数的字符串
   * @param length 返回字符串的长度
   * @return str是null：                null
   *         str的长度小于length：       前面补“0”到length长度的字符串
   *         str的长度大于或者等于length： str本身
   */
  public static String getFixLengthStringFront(String str, int length) {
    String rtStr = str;
    if (rtStr == null) {
      return rtStr;
    }
    if (rtStr.length() >= length) {
      return rtStr;
    }
    for (int i = rtStr.length(); i < length; i++) {
      rtStr = "0" + rtStr;
    }
    return rtStr;
  }

  /**
   * 把字符串后面补“0”到length长度
   * @param str 需要补足位数的字符串
   * @param length 返回字符串的长度
   * @return str是null：                null
   *         str的长度小于length：       后面补“0”到length长度的字符串
   *         str的长度大于或者等于length： str本身
   */
  public static String getFixLengthStringPost(String str, int length) {
    String rtStr = str;
    if (rtStr == null) {
      return rtStr;
    }
    if (rtStr.length() >= length) {
      return rtStr;
    }
    for (int i = rtStr.length(); i < length; i++) {
      rtStr += "0";
    }
    return rtStr;
  }

  /**
   * 输入字符串必须正整数，只允许前导空格(必须右对齐)，不宜有前导零
   * @param NumStr
   * @return
   */
  private static String positiveIntegerToChinsesStr(String numStr) {
    String chineseDigiStr[] = new String[] {
      "零", "壹", "贰", "叁", "肆",
      "伍", "陆", "柒", "捌", "玖"};

    String chineseDiviStr[] = new String[] {
        "", "拾", "佰", "仟", "万",
        "拾", "佰", "仟", "亿", "拾",
        "佰", "仟", "万", "拾", "佰",
        "仟", "亿", "拾", "佰", "仟",
        "万", "拾", "佰", "仟"};
    String rmbStr = "";
    boolean lastzero = false;
    boolean hasvalue = false; // 亿、万进位前有数值标记
    int len, n;
    len = numStr.length();
    if (len > 15) {
//      return "数值过大!";
      return YHI18n.ln(YHUtility.class, "2");
    }
    for (int i = len - 1; i >= 0; i--) {
      if (numStr.charAt(len - i - 1) == ' ') {
        continue;
      }
      n = numStr.charAt(len - i - 1) - '0';
      if (n < 0 || n > 9) {
//        return "输入含非数字字符!";
        return YHI18n.ln(YHUtility.class, "3");
      }

      if (n != 0) {
        if (lastzero) {
          rmbStr += chineseDigiStr[0]; // 若干零后若跟非零值，只显示一个零
          // 除了亿万前的零不带到后面
          //if( !( n==1 && (i%4)==1 && (lastzero || i==len-1) ) )    // 如十进位前有零也不发壹音用此行
        }
        if (! (n == 1 && (i % 4) == 1 && i == len - 1)) { // 十进位处于第一位不发壹音
          rmbStr += chineseDigiStr[n];
        }
        rmbStr += chineseDiviStr[i]; // 非零值后加进位，个位为空
        hasvalue = true; // 置万进位前有值标记

      } else {
        if ( (i % 8) == 0 || ( (i % 8) == 4 && hasvalue)) { // 亿万之间必须有非零值方显示万
          rmbStr += chineseDiviStr[i]; // “亿”或“万”
        }
      }
      if (i % 8 == 0) {
        hasvalue = false; // 万进位前有值标记逢亿复位
      }
      lastzero = (n == 0) && (i % 4 != 0);
    }

    if (rmbStr.length() == 0) {
      return chineseDigiStr[0]; // 输入空字符或"0"，返回"零"
    }
    return rmbStr;
  }

  /**
   * 将阿拉伯数字的金额转换为汉字的大写金额
   * @param val
   * @return
   */
  public static String numToRMBStr(double val) {
    String chineseDigiStr[] = new String[] {
      "零", "壹", "贰", "叁", "肆",
      "伍", "陆", "柒", "捌", "玖"};

    String SignStr = "";
    String TailStr = "";
    long fraction, integer;
    int jiao, fen;

    if (val < 0) {
      val = -val;
//      SignStr = "负";
      SignStr = "负";
    }
    if (val > 99999999999999.999 || val < -99999999999999.999) {
//      return "数值位数过大!";
      return YHI18n.ln(YHUtility.class, "2");
    }
    // 四舍五入到分
    long temp = Math.round(val * 100);
    integer = temp / 100;
    fraction = temp % 100;
    jiao = (int) fraction / 10;
    fen = (int) fraction % 10;
    if (jiao == 0 && fen == 0) {
//      TailStr = "整";
      TailStr = YHI18n.ln(YHUtility.class, "5");
    } else {
      TailStr = chineseDigiStr[jiao];
      if (jiao != 0) {
//        TailStr += "角";
        TailStr += YHI18n.ln(YHUtility.class, "6");
      }
      if (integer == 0 && jiao == 0) { // 零元后不写零几分
        TailStr = "";
      }
      if (fen != 0) {
//        TailStr += chineseDigiStr[fen] + "分";
        TailStr += chineseDigiStr[fen] + YHI18n.ln(YHUtility.class, "7");
      }
    }

    // 下一行可用于非正规金融场合，0.03只显示“叁分”而不是“零元叁分”
    //        if( !integer ) return  SignStr+TailStr;

    return SignStr + positiveIntegerToChinsesStr(String.valueOf(integer))
//        + "元" + TailStr;
    + YHI18n.ln(YHUtility.class, "8") + TailStr;
  }
  
  /**
   * 将阿拉伯数字的金额转换为汉字的大写金额
   * @param val
   * @param currName 币种的名称
   * @return
   */
  public static String numToRMBStr(double val, String currName) {
    String chineseDigiStr[] = new String[] {
      "零", "壹", "贰", "叁", "肆",
      "伍", "陆", "柒", "捌", "玖"};

    String SignStr = "";
    String TailStr = "";
    long fraction, integer;
    int jiao, fen;

    if (val < 0) {
      val = -val;
      SignStr = "负";
//      SignStr = TDCI18n.ln(TDCUtility.class, "4");
    }
    if (val > 99999999999999.999 || val < -99999999999999.999) {
      return "数值位数过大!";
//      return "数值位数过大!";
    }
    // 四舍五入到分
    long temp = Math.round(val * 100);
    integer = temp / 100;
    fraction = temp % 100;
    jiao = (int) fraction / 10;
    fen = (int) fraction % 10;
    if (jiao == 0 && fen == 0) {
      TailStr = "整";
//      TailStr = TDCI18n.ln(TDCUtility.class, "5");
    } else {
      TailStr = chineseDigiStr[jiao];
      if (jiao != 0) {
        TailStr += "角";
//        TailStr += TDCI18n.ln(TDCUtility.class, "6");
      }
      if (integer == 0 && jiao == 0) { // 零元后不写零几分
        TailStr = "";
      }
      if (fen != 0) {
        TailStr += chineseDigiStr[fen] + "分";
//        TailStr += chineseDigiStr[fen] + TDCI18n.ln(TDCUtility.class, "7");
      }
    }

    // 下一行可用于非正规金融场合，0.03只显示“叁分”而不是“零元叁分”
    //        if( !integer ) return  SignStr+TailStr;

    return currName + SignStr + positiveIntegerToChinsesStr(String.valueOf(integer))
        + "元" + TailStr;
//    + TDCI18n.ln(TDCUtility.class, "8") + TailStr;
  }
  
  /**
   * 把金额格式化为英文大写
   * @param val          金额数
   * @return
   */
  public static String number2En(double val) {
    String moneyString = getFormatedStr(val, WITHOUTGROUP);
    String[] tmpString = moneyString.split("\\.");
    // 默认为整数
    String intString = moneyString;
    // 保存小数部分字串
    String decString = "";
    // 保存英文大写字串
    String engCapital = "";
    StringBuffer strBuff1 = null;
    StringBuffer strBuff2 = null;
    StringBuffer strBuff3 = null;
    int curPoint = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    int k = 0;
    int n = 0;

    if (tmpString.length > 1) {
      intString = tmpString[0]; // 取整数部分
      decString = tmpString[1]; // 取小数部分
    }
    decString += "00";
    decString = decString.substring(0, 2); // 保留两位小数位

    try {
      // 以下处理整数部分
      curPoint = intString.length() - 1;
      if (curPoint >= 0 && curPoint < 15) {
        k = 0;
        while (curPoint >= 0) {
          strBuff1 = new StringBuffer("");
          strBuff2 = new StringBuffer("");
          strBuff3 = new StringBuffer("");
          if (curPoint >= 2) {
//            n = Integer.parseInt(intString.substring(curPoint - 2, 3));
            n = Integer.parseInt(intString.substring(curPoint - 2, curPoint + 1));
            if (n != 0) {
              i1 = n / 100; // 取佰位数值
              i2 = (n - i1 * 100) / 10; // 取拾位数值
              i3 = n - i1 * 100 - i2 * 10; // 取个位数值
              if (i1 != 0) {
                strBuff1.append(enSmallNumber[i1] + " HUNDRED ");
              }
              if (i2 != 0) {
                if (i2 == 1) {
                  strBuff2.append(enSmallNumber[i2 * 10 + i3] + " ");
                } else {
                  strBuff2.append(enLargeNumber[i2 - 2] + " ");
                  if (i3 != 0) {
                    strBuff3.append(enSmallNumber[i3] + " ");
                  }
                }
              } else {
                if (i3 != 0) {
                  strBuff3.append(enSmallNumber[i3] + " ");
                }
              }
              engCapital = strBuff1.toString() + strBuff2.toString()
                  + strBuff3.toString() + enUnit[k] + " " + engCapital;
            }
          } else {
            n = Integer.parseInt(intString.substring(0, curPoint + 1));
            if (n != 0) {
              i2 = n / 10; // 取拾位数值
              i3 = n - i2 * 10; // 取个位数值
              if (i2 != 0) {
                if (i2 == 1) {
                  strBuff2.append(enSmallNumber[i2 * 10 + i3] + " ");
                } else {
                  strBuff2.append(enLargeNumber[i2 - 2] + " ");
                  if (i3 != 0) {
                    strBuff3.append(enSmallNumber[i3] + " ");
                  }
                }
              } else {
                if (i3 != 0) {
                  strBuff3.append(enSmallNumber[i3] + " ");
                }
              }
              engCapital = strBuff2.toString() + strBuff3.toString()
                  + enUnit[k] + " " + engCapital;
            }
          }

          ++k;
          curPoint -= 3;
        }
        engCapital = engCapital.trim();
      }

      // 以下处理小数部分
      strBuff2 = new StringBuffer();
      strBuff3 = new StringBuffer();
      n = Integer.parseInt(decString);
      if (n != 0) {
        i2 = n / 10; // 取拾位数值
        i3 = n - i2 * 10; // 取个位数值
        if (i2 != 0) {
          if (i2 == 1) {
            strBuff2.append(enSmallNumber[i2 * 10 + i3] + " ");
          } else {
            strBuff2.append(enLargeNumber[i2 - 2] + " ");
            if (i3 != 0) {
              strBuff3.append(enSmallNumber[i3] + " ");
            }
          }
        } else {
          if (i3 != 0) {
            strBuff3.append(enSmallNumber[i3] + " ");
          }
        }

        // 将小数字串追加到整数字串后
        if (engCapital.length() > 0) {
          engCapital = engCapital + " AND CENTS " + strBuff2 + strBuff3; // 有整数部分时
        } else {
          engCapital = "CENTS " + strBuff2 + strBuff3; // 只有小数部分时
        }
      }

      engCapital = engCapital.trim();
      return engCapital;
    } catch (Exception ex) {
      ex.printStackTrace();
      return ""; // 含非数字字符时，返回零长字串
    }
  }
  
  /**
   * 判断是否是数字
   * 
   * @param str
   * @return
   */
  public static boolean isNumber(String str) {
    if (str == null || str.equals("")) {
      return false;
    }
    return Pattern.matches("-?\\d*\\.?\\d+(E\\d+)?", str);
  }
  
  /**
   * 判断是否是金额数字
   * @param str
   * @return
   */
  public static boolean isAmtNumber(String str) {
    if (str == null || str.equals("")) {
      return false;
    }
    return Pattern.matches("-?\\d{1,3}(\\,\\d{3})*\\.\\d{0,8}", str);
  }
  
  /**
   * 判断是否是数字
   * 
   * @param str
   * @return
   */
  public static boolean isInteger(String str) {
    if (str == null || str.equals("")) {
      return false;
    }
    return Pattern.matches("(-?[1-9]\\d*)|0", str);
  }
  
  /**
   * 判断是否是数字
   * 
   * @param str
   * @return
   */
  public static boolean isPositiveInteger(String str) {
    if (str == null || str.equals("")) {
      return false;
    }
    return Pattern.matches("[1-9]\\d*", str);
  }
  
  /**
   * 判断是否符合帐务年月的格式
   * 
   * @param str
   * @return
   */
  public static boolean isAcctYMFormat(String str) {
    if (str == null || str.equals("")) {
      return false;
    }
    return Pattern.matches("\\d{4}-\\d{2}", str);
  }
  
  /**
   * 判断是否符合系统规定的日期格式
   * @param str
   * @return
   */
  public static boolean isSysDateFormat(String str) {
    if (str == null || str.equals("")) {
      return false;
    }
    if (str.length() < 10) {
      return false;
    }
    if (str.length() == 10) {
      return Pattern.matches("\\d{4}-\\d{2}-\\d{2}", str);
    }else {
      return Pattern.matches("\\d{4}-\\d{2}-\\d{2}\\s{1}\\d{2}:\\d{2}:\\d{2}", str);
    }
  }
  /**
   * 判断是否符合系统规定的日期格式
   * @param str
   * @return
   */
  public static boolean isDay(String str) {
    if (str == null || str.equals("")) {
      return false;
    }
    if (str.length() < 10) {
      return false;
    }
    return Pattern.matches("\\d{4}-\\d{2}-\\d{2}", str);
  }
  
  /**
   * 判断是否符合系统规定的日期格式
   * @param str
   * @return
   */
  public static boolean isDayTime(String str) {
    if (str == null || str.equals("")) {
      return false;
    }
    if (str.length() < 19) {
      return false;
    }
    return Pattern.matches("\\d{4}-\\d{2}-\\d{2}\\s{1}\\d{2}:\\d{2}:\\d{2}", str);
  }
  
  /**
   * 判断表达式字符串中的括号是否成对匹配
   * 
   * @param expr
   *          表达式
   * @return
   */
  public static boolean isBracketMatched(String expr) {
    if (expr == null || expr.equals("")) {
      return true;
    }
    int tmpIndex = expr.indexOf(YHTokenSplit.TOKEN_SPLIT_LEFTBRACKET);
    if (tmpIndex < 0) {
      return true;
    }
    
    // tmpIndex >= 0的情况
    int stackInt = 1;
    tmpIndex++;
    while (tmpIndex < expr.length()) {
      char c = expr.charAt(tmpIndex++);
      if (c == YHTokenSplit.TOKEN_SPLIT_LEFTBRACKET) {
        stackInt++;
      }else if (c == YHTokenSplit.TOKEN_SPLIT_RIGHTBRACKET) {
        stackInt--;
      }
    }
    if (stackInt == 0) {
      return true;
    }else {
      return false;
    }
  }
  
  /**
   * 把数字转换位英文字母
   * @param i
   * @return
   */
  public static String num2Charactor(int num) {
    if (num < 10 || num > 35) {
      return String.valueOf(num);
    }
    return String.valueOf((char)(55 + num));
  }
  
  /**
   * A-0;B-1;....;AA;AB
   * @param charactor
   * @return
   */
  public static int str2Num(String charactor) {
    if (charactor == null || charactor.equals("")) {
      return 0;
    }
    int rtInt = 0;
    String tmpStr = charactor.toUpperCase();    
    for (int i = 0; i < tmpStr.length(); i++) {
      char c = tmpStr.charAt(tmpStr.length() - 1 - i);
      rtInt += ((int)(c - 65 + 1) * (int)Math.pow(26, i));
    }
    return rtInt - 1;
  }
  
  /**
   * 把字符串转化为Date
   * @param dateStr
   * @return
   */
  public static Date parseDate(String formatStr, String dateStr) throws ParseException {
	    FastDateFormat format = FastDateFormat.getInstance(formatStr);
	    return DateUtils.parseDate(dateStr, format.getPattern());
	  }
  
  /**
   * 把字符串转化为Date
   * @param dateStr
   * @return
   */
  public static Date parseDate(String dateStr) throws ParseException {
	    if (dateStr == null || "".equals(dateStr)) {
	      return null;
	    }
	    
	    FastDateFormat format = null;
	    if (Pattern.matches("\\d{4}-\\d{1,2}-\\d{1,2}", dateStr)) {
	      format = FastDateFormat.getInstance("yyyy-MM-dd");
	    }else if (Pattern.matches("\\d{4}\\d{2}\\d{2}", dateStr)) {
	      format = FastDateFormat.getInstance("yyyyMMdd");
	    }else if (Pattern.matches("\\d{4}年\\d{2}月\\d{2}日", dateStr)) {
	      format = FastDateFormat.getInstance("yyyy年MM月dd日",Locale.CHINA);
	    }else if (Pattern.matches("\\d{4}年\\d{1,2}月\\d{1,2}日", dateStr)) {
	      format = FastDateFormat.getInstance("yyyy年M月d日",Locale.CHINA);
	    }else if (Pattern.matches("\\d{1,2}\\w{3}\\d{4}", dateStr)) {
	      format = FastDateFormat.getInstance("dMMMyyyy",Locale.ENGLISH);
	    }else if (Pattern.matches("\\d{1,2}-\\w{3}-\\d{4}", dateStr)) {
	      format = FastDateFormat.getInstance("d-MMM-yyyy",Locale.ENGLISH);
	    }else if (dateStr.length() > 20 ) {
	      format = dateFormatS;
	    }else {
	      format = dateFormat;
	    }
	    
	    return DateUtils.parseDate(dateStr,format.getPattern());
	  }
  
  /**
   * 当前日期转化成java.sql.Date对象
   * @param dateStr
   * @return
   */
  public static java.sql.Timestamp parseTimeStamp() throws ParseException {
    return parseTimeStamp(null);
  }
  /**
   * 把字符串转化为java.sql.Date对象
   * @param longDate
   * @return
   */
  public static java.sql.Timestamp parseTimeStamp(long longDate) throws ParseException {
    Date utilDate = new Date(longDate);
    java.sql.Timestamp sqlDate = new java.sql.Timestamp(utilDate.getTime());
    return sqlDate;
  }
  /**
   * 把字符串转化为java.sql.Date对象
   * @param dateStr
   * @return
   */
  public static java.sql.Timestamp parseTimeStamp(String dateStr) throws ParseException {
    Date utilDate = null;
    if (dateStr == null) {
      utilDate = new Date();
    }else {
      utilDate = YHUtility.parseDate(dateStr);
    }
    java.sql.Timestamp sqlDate = new java.sql.Timestamp(utilDate.getTime());
    return sqlDate;
  }
  
  /**
   * 当前日期转化成java.sql.Date对象
   * @param dateStr
   * @return
   */
  public static java.sql.Date parseSqlDate() throws ParseException {
    return parseSqlDate(null);
  }
  /**
   * 把字符串转化为java.sql.Date对象
   * @param dateStr
   * @return
   */
  public static java.sql.Date parseSqlDate(String dateStr) throws ParseException {
    Date utilDate = null;
    if (dateStr == null) {
      utilDate = new Date();
    }else {
      utilDate = YHUtility.parseDate(dateStr);
    }
    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
    return sqlDate;
  }
  
  /**
   * 解析数值
   * @param numStr
   * @return
   */
  public static double parseDouble(String numStr) throws ParseException {
    if (isNullorEmpty(numStr)) {
      return 0.00;
    }
    if (numStr.indexOf(",") > 0) {
      return numFormatG.parse(numStr).doubleValue();
    }
    return Double.parseDouble(numStr);
  }
 
  /**
   * 转换编码 
   * @return
   */
  public static String transferCode(String srcStr, String srcCode,
      String desCode) throws UnsupportedEncodingException {
    if (srcStr == null) {
      return null;
    }
    return new String(srcStr.getBytes(srcCode), desCode);
  }
  
  /**
   * 把编码为ISO-8859-1的转换为GBK
   * @param srcStr
   * @return
   * @throws UnsupportedEncodingException
   */
  public static String iso88591ToGbk(String srcStr) throws UnsupportedEncodingException {
    return transferCode(srcStr, "ISO-8859-1", "GBK");
  }
  
  /**
   * 把编码为ISO-8859-1的转换为UTF-8
   * @param srcStr
   * @return
   * @throws UnsupportedEncodingException
   */
  public static String iso88591ToUTF8(String srcStr) throws UnsupportedEncodingException {
    return transferCode(srcStr, "ISO-8859-1", "UTF-8");
  }
  
  /**
   * 取得HTML形式的缩进字符串
   * @return
   */
  public static String getHtmlIdent(int identCnt) {
    return getIdentStr(identCnt, YHTokenConst.HTML_SPACE);
  }
  /**
   * 取得中文空格形式的缩进字符串
   * @return
   */
  public static String getChinaSpaceIdent(int identCnt) {
    return getIdentStr(identCnt, YHTokenConst.CHINA_SPACE);
  }
  
  /**
   * 判断金额是否相等，这里考虑舍入因素，并非严格数学意义上的相等
   * @param amt1          金额1
   * @param amt2          金额2
   * @param amtScale      金额的小数位
   * @return
   */
  public static boolean isEqualAmt(
      double amt1, double amt2,
      int amtScale) {
    
    if (amt1 == amt2) {
      return true;
    }

    if (Math.abs(amt1 - amt2) < Math.pow(10, (0 - amtScale))) {
      return true;
    }
    return false;
  }
  
  /**
   * 取得中文空格的缩进
   * @param identCnt        缩进层次数
   * @param identStr        缩进字符串
   * @return
   */
  private static String getIdentStr(int identCnt, String identStr) {
    StringBuffer buff = new StringBuffer();
    
    for (int i = 0; i < identCnt; i++) {
      buff.append(identStr);
    }
    return buff.toString();
  }
  
  //read an input-stream into a String
  public static String loadStream(InputStream in) throws Exception {
    ByteArrayOutputStream out = null;
    try {
      out = new ByteArrayOutputStream();
      byte[] buff = new byte[1024];
      int readLen = 0; 
      while ((readLen = in.read(buff)) > 0) {
        out.write(buff, 0, readLen);
      }
    }catch(Exception ex) {
      log.debug(ex.getMessage(), ex);
      throw ex;
    }finally {
      try {
        if (out != null) {
          out.close();
        }
      }catch(Exception ex) {        
      }
    }
    return out.toString();
  }  
  
  /**
   * 执行命令
   * @param cmd
   */
  public static String exCmd(String cmd) {
    try {
      Runtime  r = Runtime.getRuntime();
      Process process = r.exec(cmd);      
      InputStream in = null;
      in = process.getInputStream();
      String outMsrg = loadStream(in);
      
      process.waitFor();
      process.destroy();
      
      return outMsrg;
    }catch(Exception ex) {
      log.debug(ex.getMessage(), ex);
    }
    return null;
  }
  
  /**
   * 避免取得计数法的String形式的双精度型字符串
   * @param bgd
   * @return
   */
  public static String decimal2DoubleStr(BigDecimal bgd) {
    return getFormatedStr(bgd.doubleValue(), WITHOUTGROUP);
  }
  
  /**
   * 带精度要求的四则运算-加法
   * @param opt1              操作数1
   * @param opt2              操作数2
   * @param scale             小数位精度
   * @return
   */
  public static BigDecimal add(double opt1, double opt2, int scale) {
    BigDecimal bgd1 = new BigDecimal(opt1);
    BigDecimal bgd2 = new BigDecimal(opt2);
    
    return add(bgd1, bgd2, scale);
  }
  
  /**
   * 带精度要求的四则运算-加法
   * @param bgd1              操作数1
   * @param opt2              操作数2
   * @param scale             小数位精度
   * @return
   */
  public static BigDecimal add(BigDecimal bgd1, double opt2, int scale) {
    BigDecimal bgd2 = new BigDecimal(opt2);
    
    return add(bgd1, bgd2, scale);
  }
  
  /**
   * 带精度要求的四则运算-加法
   * @param bgd1              操作数1
   * @param opt2              操作数2
   * @param scale             小数位精度
   * @return
   */
  public static BigDecimal add(BigDecimal opt1, BigDecimal opt2, int scale) {
    if (opt1 == null) {
      opt1 = new BigDecimal(0.00);
    }
    if (opt2 == null) {
      opt2 = new BigDecimal(0.00);
    }
    opt1 = opt1.setScale(scale, BigDecimal.ROUND_HALF_UP);
    opt2 = opt2.setScale(scale, BigDecimal.ROUND_HALF_UP);
    
    return opt1.add(opt2);
  }
  
  /**
   * 带精度要求的四则运算-减法
   * @param opt1              操作数1
   * @param opt2              操作数2
   * @param scale             小数位精度
   * @return
   */
  public static BigDecimal subtract(double opt1, double opt2, int scale) {
    BigDecimal bgd1 = new BigDecimal(opt1);
    BigDecimal bgd2 = new BigDecimal(opt2);
    
    return subtract(bgd1, bgd2, scale);
  }
  
  /**
   * 带精度要求的四则运算-减法
   * @param bgd1              操作数1
   * @param opt2              操作数2
   * @param scale             小数位精度
   * @return
   */
  public static BigDecimal subtract(BigDecimal bgd1, double opt2, int scale) {
    BigDecimal bgd2 = new BigDecimal(opt2);
    
    return subtract(bgd1, bgd2, scale);
  }
  
  /**
   * 带精度要求的四则运算-减法
   * @param opt1              操作数1
   * @param opt2              操作数2
   * @param scale             小数位精度
   * @return
   */
  public static BigDecimal subtract(BigDecimal opt1, BigDecimal opt2, int scale) {
    if (opt1 == null) {
      opt1 = new BigDecimal(0.00); 
    }
    if (opt2 == null) {
      opt2 = new BigDecimal(0.00); 
    }
    opt1 = opt1.setScale(scale, BigDecimal.ROUND_HALF_UP);
    opt2 = opt2.setScale(scale, BigDecimal.ROUND_HALF_UP);
    
    return opt1.subtract(opt2);
  }
  
  /**
   * 带精度要求的四则运算-乘法
   * @param opt1              操作数1
   * @param opt2              操作数2
   * @param scale             小数位精度
   * @return
   */
  public static BigDecimal multiply(double opt1, double opt2, int scale) {
    BigDecimal bgd1 = new BigDecimal(opt1);
    BigDecimal bgd2 = new BigDecimal(opt2);
    
    return multiply(bgd1, bgd2, scale);
  }
 
  /**
   * 带精度要求的四则运算-乘法
   * @param bgd1              操作数1
   * @param opt2              操作数2
   * @param scale             小数位精度
   * @return
   */
  public static BigDecimal multiply(BigDecimal bgd1, double opt2, int scale) {
    BigDecimal bgd2 = new BigDecimal(opt2);
    
    return multiply(bgd1, bgd2, scale);
  }
  
  /**
   * 带精度要求的四则运算-乘法
   * @param bgd1              操作数1
   * @param opt2              操作数2
   * @param scale             小数位精度
   * @return
   */
  public static BigDecimal multiply(BigDecimal opt1, BigDecimal opt2, int scale) {
    if (opt1 == null) {
      opt1 = new BigDecimal(0.00);
    }
    if (opt2 == null) {
      opt2 = new BigDecimal(0.00);
    }
    opt1 = opt1.setScale(scale, BigDecimal.ROUND_HALF_UP);
    opt2 = opt2.setScale(scale, BigDecimal.ROUND_HALF_UP);
    
    return opt1.multiply(opt2);
  }
  
  /**
   * 带精度要求的四则运算-除法
   * @param opt1              操作数1
   * @param opt2              操作数2
   * @param scale             小数位精度
   * @return
   */
  public static BigDecimal divide(double opt1, double opt2, int scale) {
    BigDecimal bgd1 = new BigDecimal(opt1);
    BigDecimal bgd2 = new BigDecimal(opt2);
    
    return divide(bgd1, bgd2, scale);
  }
  
  /**
   * 带精度要求的四则运算-除法
   * @param bgd1              操作数1
   * @param opt2              操作数2
   * @param scale             小数位精度
   * @return
   */
  public static BigDecimal divide(BigDecimal bgd1, double opt2, int scale) {
    BigDecimal bgd2 = new BigDecimal(opt2);
    bgd2.setScale(scale, BigDecimal.ROUND_HALF_UP);
    
    return divide(bgd1, bgd2, scale);
  }
  
  /**
   * 带精度要求的四则运算-除法
   * @param bgd1              操作数1
   * @param opt2              操作数2
   * @param scale             小数位精度
   * @return
   */
  public static BigDecimal divide(BigDecimal opt1, BigDecimal opt2, int scale) {
    if (opt1 == null) {
      opt1 = new BigDecimal(0.00);
    }
    if (opt2 == null) {
      opt2 = new BigDecimal(1.00);
    }
    opt1 = opt1.setScale(scale, BigDecimal.ROUND_HALF_UP);
    opt2 = opt2.setScale(scale, BigDecimal.ROUND_HALF_UP);
    
    return opt1.divide(opt2, scale * 2, BigDecimal.ROUND_HALF_UP);
  }
  
  /**
   * 取得双精度型字符串，解决科学计数法问题
   * @param bg
   * @return
   */
  public static String parseDoubleStr(BigDecimal bg, int format) {
    return YHUtility.getFormatedStr(bg.doubleValue(), format);
  }
  /**
   * 解析双精度型字符串，解决科学计数法问题
   * @param bg
   * @return
   */
  public static double parseDouble(BigDecimal bg) {
    if (bg == null) {
      return 0.00;
    }
    return Double.parseDouble(parseDoubleStr(bg, WITHOUTGROUP));
  }
  
  /**
   * 用系统时间和随机数产生系统唯一的文件名
   * @return
   */
  public static String getRandomName() {
    String currTimeStr = YHUtility.getCurDateTimeStr(YHUtility.DATE_FORMAT_NOSPLIT);
    String randomStr = String.valueOf((int)Math.random() * 10000);
    return currTimeStr + randomStr;
  }
  
  /**
   * 把字符串转换为属性哈希表
   * @param propStr
   * @param rtMap
   */
  public static void str2Map(String propStr, Map rtMap) {
    String[] propArray = propStr.split("\\\r*\\\n{1}");
    for (int i = 0; i < propArray.length; i++) {
      String tmpStr = propArray[i].trim();
      if (tmpStr.length() < 1 || tmpStr.startsWith("#")) {
        continue;
      }
      int tmpIndex = tmpStr.indexOf("=");
      if (tmpIndex < 0) {
        continue;
      }
      String key = tmpStr.substring(0, tmpIndex).trim();
      String value = tmpStr.substring(tmpIndex + 1).trim();
      if (key.length() < 1 || value.length() < 1) {
        continue;
      }
      rtMap.put(key, value);
    }
  }
  
  /**
   * Copy 哈希表
   * @param fromMap
   * @param toMap
   */
  public static void copyMap(Map fromMap, Map toMap) {
    Iterator iKeys = fromMap.keySet().iterator();
    while (iKeys.hasNext()) {
      Object key = iKeys.next();
      Object value = fromMap.get(key);
      toMap.put(key, value);
    }
  }
  
  /**
   * 清理指定前缀的属性
   * @param srcMap
   * @param postFix
   */
  public static void clearMapPre(Map<String, String> srcMap, String preFix) {
    List<String> keyList = new ArrayList<String>();
    
    Iterator<String> iKeys = srcMap.keySet().iterator();
    while (iKeys.hasNext()) {
      String key = iKeys.next();
      if (key.startsWith(preFix)) {
        keyList.add(key);
      }
    }
    for (int i = 0; i < keyList.size(); i++) {
      srcMap.remove(keyList.get(i));
    }
  }
  /**
   * 清理指定后缀的属性
   * @param srcMap
   * @param postFix
   */
  public static void clearMapPost(Map<String, String> srcMap, String postFix) {
    List<String> keyList = new ArrayList<String>();
    
    Iterator<String> iKeys = srcMap.keySet().iterator();
    while (iKeys.hasNext()) {
      String key = iKeys.next();
      if (key.endsWith(postFix)) {
        keyList.add(key);
      }
    }
    for (int i = 0; i < keyList.size(); i++) {
      srcMap.remove(keyList.get(i));
    }
  }
  
  /**
   * URL编码
   */
  public static String encodeURL(String srcStr) throws Exception {
    if (srcStr == null) {
      return "";
    }
    return URLEncoder.encode(srcStr, YHConst.DEFAULT_CODE);
  }
  
  /**
   * URL编码
   */
  public static String decodeURL(String srcStr) throws Exception {
    if (srcStr == null) {
      return "";
    }
    return URLDecoder.decode(srcStr, YHConst.DEFAULT_CODE);
  }
  /**
   * 处理\ " '
   * @param srcStr
   * @return
   * @throws Exception
   */
  public static String encodeHtml(String srcStr) {
    if (srcStr == null) {
      return "";
    }
    return srcStr.replace("\\", "&#92;").replace("\"", "&#34;").replace("\'", "&#39;").replace("\r\n", "&#10;&#13;").replace("\n", "&#13;").replace("\r", "&#10;");
  }
  /**
   * 处理\ " '
   * @param srcStr
   * @return
   * @throws Exception
   */
  public static String encodeQuote(String srcStr) {
    if (srcStr == null) {
      return "";
    }
    return srcStr.replace("\\", "\\\\").replace("\"", "\\\"").replace("\'", "\\\'");
  }
  /**
   * 处理\ " '
   * @param srcStr
   * @return
   * @throws Exception
   */
  public static String encodeSpecial(String srcStr) {
    if (srcStr == null) {
      return "";
    }
    return srcStr.replace("\\", "\\\\").replace("\"", "\\\"").replace("\'", "\\\'").replace("\r\n", "").replace("\n", "").replace("\r", "");
  }
  
  /**
   * 处理' % _
   * @param srcStr
   * @return
   * @throws Exception
   */
  public static String encodeLike(String srcStr) {
    if (srcStr == null) {
      return "";
    }
    return srcStr.replace("\\", "\\\\").replace("'", "''").replace("%", "\\%").replace("_", "\\_");
  }
  
  /**
   * 获取Key前面部分相同的子哈希表
   * @param srcMap
   * @param prefix
   * @return
   */
  public static Map startsWithMap(Map srcMap, String prefix) {
    Map rtMap = new HashMap<String, Object>();
    
    if (isNullorEmpty(prefix)) {
      return rtMap;
    }
    Iterator<String> iKeys = srcMap.keySet().iterator();
    while (iKeys.hasNext()) {
      String key = iKeys.next();
      if (key.startsWith(prefix)) {
        rtMap.put(key, srcMap.get(key));
      }
    }
    return rtMap;
  }
  /**
   * 获取Key后面部分相同的子哈希表
   * @param srcMap
   * @param prefix
   * @return
   */
  public static Map endsWithMap(Map srcMap, String postfix) {
    Map rtMap = new HashMap<String, Object>();
    
    if (isNullorEmpty(postfix)) {
      return rtMap;
    }
    Iterator<String> iKeys = srcMap.keySet().iterator();
    while (iKeys.hasNext()) {
      String key = iKeys.next();
      if (key.endsWith(postfix)) {
        rtMap.put(key, srcMap.get(key));
      }
    }
    return rtMap;
  }
  /**
   * 降序排序
   * @param list
   * @param key
   * @param type
   */
  public static void sortDesc(List<Map<String, String>> list, String key, int type) {
    java.util.Collections.sort(list, new YHMapComparator(type, key, "DESC"));
  }
  /**
   * 升序排序
   * @param list
   * @param key
   * @param type
   */
  public static void sortAsc(List<Map<String, String>> list, String key, int type) {
    java.util.Collections.sort(list, new YHMapComparator(type, key, "ASC"));
  }
  /**
   * 去掉所有html标签
   * @param html
   * @return
   */
  public static String  cutHtml(String html){
    String result = "";
    result = html.replaceAll("(<[^/\\s][\\w]*)[\\s]*([^>]*)(>)", "$1$3").replaceAll("<[^>]*>", "");
    return result;
  }
  
  /**
   * 类型转换成Long
   * @param value
   * @return
   */
  public static Long cast2Long(Object value) throws YHInvalidParamException {
    if (value == null || value.toString().equals("")) {
      return new Long(0);
    }
    try {
      return (Long)value;
    }catch(Exception ex) {
      try {
        return new Long(((Integer)value));
      }catch(Exception ex2) {
        try {          
          int valueInt = (int)((Double)value).doubleValue();
          return Long.valueOf(valueInt);
        }catch(Exception ex3) {
          throw new YHInvalidParamException("不能转换成长整型 >> " + value.toString() + " >> " + value.getClass().getName() );
        }
      }
    }
  }
}