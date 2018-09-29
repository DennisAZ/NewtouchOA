package yh.subsys.oa.addworkfee.data;
/**
 * 
 */

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 *
 */
public class YHCalendary {
	
	public static String PATTERN = "yyyy-MM-dd";
	
	public static String PATTEN_SECOND = "yyyy-MM-dd HH:mm:ss";
	
	public static long DAY = 24 * 60 * 60 * 1000;
	
	public static long HOUR = 60 * 60 * 1000 ;
	
	public static long MINIT =  60 * 1000;
	
	/**
	 * 判断是否是周末
	 * @param date  传入的时间串， 如2010-11-12
	 * @param pattern 时间格式 如:yyyy-MM-dd
	 * @return
	 * @throws Exception
	 */
	public static boolean isWeekend(String date, String pattern) throws Exception{
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		Date time = df.parse(date);
		Calendar cal = Calendar.getInstance();  
		cal.setTime(time);
		int day = cal.get(Calendar.DAY_OF_WEEK);
		if(day == Calendar.SUNDAY || day == Calendar.SATURDAY){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isWeekend(Date date, String pattern) throws Exception{
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		String time = df.format(date);
		return isWeekend(time, pattern);
	}
	
	/**
	 * 是否是节假日
	 * @param fest  节假日
	 * @param date  传入的时间
	 * @param pattern  时间格式 如:yyyy-MM-dd
	 * @return
	 * @throws ParseException 
	 */
	public static boolean isFestival(List<YHFestival> fest, String date, String pattern) throws ParseException{
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		if(fest == null || fest.size() == 0){
			return false;
		}
		for(int i=0; i<fest.size(); i++){
			YHFestival fet = fest.get(i);
			Date comp = df.parse(date);
			if(comp.getTime() <= fet.getEndDate().getTime() && comp.getTime() >= fet.getBeginDate().getTime()){
				return true;
			}
		}
		return false;
	}
	
	/**
   * 是否是调休
   * @param fest  调休
   * @param date  传入的时间
   * @param pattern  时间格式 如:yyyy-MM-dd
   * @return
   * @throws ParseException 
   */
  public static boolean isChangeRest(List<YHChangeRest> fest, String date, String pattern) throws ParseException{
    SimpleDateFormat df = new SimpleDateFormat(pattern);
    if(fest == null || fest.size() == 0){
      return false;
    }
    for(int i=0; i<fest.size(); i++){
      YHChangeRest fet = fest.get(i);
      Date comp = df.parse(date);
      if(comp.getTime() <= fet.getEndDate().getTime() && comp.getTime() >= fet.getBeginDate().getTime()){
        return true;
      }
    }
    return false;
  }
  
  /**
   * 是否是节假日
   * @param fest  节假日
   * @param date  传入的时间
   * @param pattern  时间格式 如:yyyy-MM-dd
   * @return
   * @throws ParseException 
   */
  public static boolean isChangeRest(List<YHChangeRest> fest, Date date, String pattern) throws ParseException{
    String time = parseDate(date, pattern);
    return isChangeRest(fest, time, pattern);
  }
  
	
	/**
	 * 是否是节假日
	 * @param fest  节假日
	 * @param date  传入的时间
	 * @param pattern  时间格式 如:yyyy-MM-dd
	 * @return
	 * @throws ParseException 
	 */
	public static boolean isFestival(List<YHFestival> fest, Date date, String pattern) throws ParseException{
		String time = parseDate(date, pattern);
		return isFestival(fest, time, pattern);
	}
	
	 
	
    /**
     * 两个日期的天数差
     * @param begin
     * @param end
     * @param pattern
     * @return
     * @throws ParseException
     */
	public  static double getDateDiff(String begin, String end, String pattern) throws ParseException{
		Date beginDate = parseDate(begin, pattern);
		Date endDate = parseDate(end, pattern);
		double diff = toDiff(beginDate, endDate, DAY);
		return round(diff, null);
	}
	
	/**
	 * 计算两天的差
	 * @param first
	 * @param second
	 * @param dFlag  天 小时 分钟
	 * @return
	 */
	public static double toDiff(Date first, Date second, long dFlag){
		if(first != null && second != null){
		  return (second.getTime() - first.getTime()) * 100/(dFlag*100.0);
		}else if(first != null && second == null){
			return 0;
		}else if(first == null && second != null){
			return 0;
		}else if(first == null && second == null){
			return -1;
		}
		return -1;
	}
	
	/**
	 * 相隔多少小时
	 * @param begin
	 * @param end
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static double getHourDiff(String begin, String end, String pattern) throws ParseException{
		Date beginDate = parseDate(begin, pattern);
		Date endDate = parseDate(end, pattern);
		double diff = toDiff(beginDate, endDate, HOUR); 
		return round(diff, null);
	}
	/**
	 * 相隔多少分钟
	 * @param begin
	 * @param end
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static double getMinitDiff(String begin, String end, String pattern)throws ParseException{
		Date beginDate = parseDate(begin, pattern);
		Date endDate = parseDate(end, pattern);
		double diff =  toDiff(beginDate, endDate, MINIT); 
		return round(diff, null);
	}
	
	/**
	 * 把日期字符串转换为日期类型
	 * @param date
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String date, String pattern) throws ParseException{
		SimpleDateFormat df = null;
		if(pattern==null || pattern.length() < 1){
			df = new SimpleDateFormat(YHCalendary.PATTERN);
		}else{
			df = new SimpleDateFormat(pattern);
		}
		if(date != null){
			return df.parse(date);
		}
		return null;
	}
	
	/**
	 * 把日期类型转换为日期字符串
	 * @param date
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static String parseDate(Date d, String pattern){
		SimpleDateFormat df = null;
		if(pattern==null || pattern.length() < 1){
			df = new SimpleDateFormat(YHCalendary.PATTERN);
		}else{
			df = new SimpleDateFormat(pattern);
		}
		return df.format(d);
	}
	/**
	 * 某个日期的下一天
	 * @param date
	 * @param pattern
	 * @param cnt
	 * @return
	 * @throws ParseException
	 */
	public static Date afterDate(String date, String pattern, int cnt) throws ParseException{
		Date start = parseDate(date, pattern);
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		cal.add(Calendar.DATE, cnt);
		return cal.getTime();
	}
	
	/**
	 * 某一天属于是节假日还是周末
	 * @param begin
	 * @param end
	 * @param pattern
	 * @param fest
	 * @throws Exception
	 */
	public static void checkDateType(String begin, String end, String pattern, List<YHFestival> fest) throws Exception{
		double diff = getDateDiff(begin, end, YHCalendary.PATTERN);
		if(diff == 0){
			Date d = afterDate(begin, "yyyy-MM-dd", 0);
			if(isWeekend(d, YHCalendary.PATTERN)){
				System.out.println(parseDate(d, null)+"是周末");
			}
			if(isFestival(fest, d,  YHCalendary.PATTERN)){
				System.out.println(parseDate(d, null)+"是假日");
			}
		}else if(diff > 0){
		for(int i=0; i<diff; i++){
			Date d = afterDate(begin, "yyyy-MM-dd", i+1);
			if(isWeekend(d, YHCalendary.PATTERN)){
				System.out.println(parseDate(d, null)+"是周末");
			}
			if(isFestival(fest, d,  YHCalendary.PATTERN)){
				System.out.println(parseDate(d, null)+"是假日");
			}
			if(isWeekend(d, YHCalendary.PATTERN) && isFestival(fest, d,  YHCalendary.PATTERN)){
				System.out.println(parseDate(d, null)+"即是假日又是周末");
			}
		}
		}
	}
	
	
	/**
	 * 返回一个List, 比如一个节假日为国庆(10.1--10.7), 传入一个时间段(10.4--10.6)
	 * 会把时间段在这个节假日的所有的日期存为一个List，返回这个List
	 * @param fest  一个节假日
	 * @param begin  
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public static List<Date> getFestivalList(YHFestival fest, String begin, String end) throws ParseException{
		if(fest == null){
			return null;
		}
		Date first = parseDate(begin, null);
		Date second = parseDate(end, null);
		double diff = toDiff(first, second, DAY) + 1;
		List<Date> dats = new ArrayList<Date>();
		for(int i=0; i<diff; i++){
			Date d = afterDate(begin, PATTERN, i);
			//System.out.println(parseDate(d,null) + "&&&&&&&&&&&&&");
			if(d.getTime() >= fest.getBeginDate().getTime() && d.getTime() <= fest.getEndDate().getTime()){
				dats.add(d);
			}
		}
		return dats;
	}
	
	/**
	 * 返回日期范围在这个节假日的日期
	 * @param fest  一个节假日
	 * @param begin  输入的日期范围的开始日期
	 * @param end    输入的日期范围的结束日期
	 * @return
	 * @throws ParseException
	 */
	public static Map<String, List<Date>> getFestivalMap(YHFestival fest, String begin, String end) throws ParseException{
		if(fest == null){
			return null;
		}
		List<Date> list = getFestivalList(fest,  begin,  end);
		Map<String, List<Date>> maps = new HashMap<String, List<Date>>();
		maps.put(fest.getYear()+"_"+fest.getFeName(), list);
		return maps;
	}
	
	/**
	 * 给定一段时间范围，根据库中返回的节假日，设定各个节假日的时间
	 * @param fest  从库中返回的节假日列表
	 * @param begin
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, List<Date>>> getAllFestivalMap(List<YHFestival> fest, String begin, String end) throws Exception{
		List<Map<String, List<Date>>> list = new ArrayList<Map<String, List<Date>>>();
		if(fest == null || fest.size() == 0){
			return null;
		}
		for(int i=0; i<fest.size(); i++){
			Map<String, List<Date>> map = getFestivalMap(fest.get(i), begin, end);
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 计算精度
	 */
	public static double  round(double number, String partten){
    if(partten == null || partten == ""){
      partten = "#.00";
    }
    return Double.parseDouble(new DecimalFormat(partten).format(number));
  }
	public static void main(String[] args) throws ParseException{
		String begin = "2010-11-12 12:12:09";
		String end = "2010-11-12 13:23:45";
		SimpleDateFormat df = new SimpleDateFormat(YHCalendary.PATTERN);
		double k = getDateDiff(begin, end, "yyyy-MM-dd");
		double kk = getHourDiff(begin, end, "yyyy-MM-dd HH:mm:ss");
		/*for(int i=0; i<10; i++){
			Date d = afterDate(begin, "yyyy-MM-dd", i+1);
			System.out.println(df.format(d)+"*****");
		}*/
		System.out.println(kk);
	}
	
}
