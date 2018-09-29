package com.psit.struts.util.format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类 <br>
 */
public class GetDate {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static SimpleDateFormat sTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat monFormat = new SimpleDateFormat("yyyy-MM");
	private static Calendar cal = Calendar.getInstance();
	
	public static String getDateHql(String startDate,String endDate,String dateName){
		String hql = "";
    	if (!StringFormat.isEmpty(startDate) && StringFormat.isEmpty(endDate)) {
    		hql = dateName+" >= '"+ startDate + "' ";
		} else if (!StringFormat.isEmpty(startDate) && !StringFormat.isEmpty(endDate)) {
			hql = dateName + " between '" + startDate + "' and '" + endDate + "' ";
		} else if (StringFormat.isEmpty(startDate) && !StringFormat.isEmpty(endDate)) {
			hql = dateName + " <= '" + endDate + "' ";
		}
    	return hql;
	}
	
	/**
	 * 按自定义格式转换日期字符 <br>
	 * @param date 日期
	 * @param fmtStr 格式
	 * @return String 转换后的日期
	 */
	public static String formatDate(Date date,String fmtStr){
		if(date!=null){
			SimpleDateFormat format = new SimpleDateFormat(fmtStr);
			return format.format(date);
		}
		else{
			return null;
		}
	}
	
	/**
	 * 得到两个月之间的月份数组 <br>
	 * @param mon1 月份开始范围[yyyy-mm]
	 * @param mon2 月份结束范围[yyyy-mm]
	 * @return String[] 间隔月份数组
	 */
	public static String[] getMonths(String mon1,String mon2){
		String startMon,endMon;
		int startYear,startMonth,endYear,endMonth;
		int yearsBetween, monthsBetween,monthsNum;
		String[]monArray = null;
		if(mon1.compareTo(mon2)>0){
			startMon = mon2;
			endMon = mon1;
		}
		else {
			startMon = mon1;
			endMon = mon2;
		}
		if(startMon.equals(endMon)){
			monArray = new String[]{startMon};
		}
		else{
			startYear = Integer.parseInt(startMon.substring(0,4));
			startMonth = Integer.parseInt(startMon.substring(5));
			endYear = Integer.parseInt(endMon.substring(0,4));
			endMonth = Integer.parseInt(endMon.substring(5));
			yearsBetween = endYear - startYear;
			monthsBetween = endMonth - startMonth;
			monthsNum = (yearsBetween*12 + monthsBetween)+1;
			monArray = new String[monthsNum];
			int month = startMonth;
			int year = startYear;
			for(int i=0; i<monthsNum; i++){
				if(month>12){
					year++;
					month=1;
				}
				monArray[i] = Integer.toString(year)+"-"+((month<10)?"0":"")+Integer.toString(month);
				month++;
			}
		}
		return monArray;
	}
	
	/**
	 * 得到两个日期之间的间隔日期数组 <br>
	 * @param date1 日期开始范围[yyyy-mm-dd]
	 * @param date2 日期结束范围[yyyy-mm-dd]
	 * @return String[] 间隔日期数组
	 */
	public static String[] getDates(String date1,String date2){
		String startDate,endDate;
		String[]dateArray = null;
		if(date1.compareTo(date2)>0){
			startDate = date2;
			endDate = date1;
		}
		else {
			startDate = date1;
			endDate = date2;
		}
		if(startDate.equals(endDate)){
			dateArray = new String[]{startDate};
		}
		else{
			int dateNum = new Long((formatDate(endDate).getTime() - formatDate(startDate).getTime())
						/ (3600 * 24 * 1000)).intValue()+1;
			dateArray = new String[dateNum];
			cal.setTime(formatDate(startDate));
			for(int i=0; i<dateNum; i++){
				dateArray[i]=parseStrDate(cal.getTime());
				cal.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		return dateArray;
	}
	
	/**
	 * 转换为日期字符 <br>
	 * @param date 日期
	 * @return String 转换后的日期
	 */
	public static String parseStrDate(Date date){
		if(date!=null){
			return dateFormat.format(date);
		}
		else{
			return null;
		}
	}
	
	/**
	 * 转换为时间字符 <br>
	 * @param date 时间
	 * @return String 转换后的时间
	 */
	public static String parseStrTime(Date date){
		if(date!=null){
			return timeFormat.format(date);
		}
		else{
			return null;
		}
	}
	
	/**
	 * 转换为时间(秒)字符 <br>
	 * @param date 时间
	 * @return String 转换后的时间
	 */
	public static String parseStrSTime(Date date){
		if(date!=null){
			return sTimeFormat.format(date);
		}
		else{
			return null;
		}
	}
	
	/**
	 * 转换为年月字符 <br>
	 * @param date 时间
	 * @return String 年月格式的字符
	 */
	public static String parseStrMon(Date date){
		if(date!=null){
			return monFormat.format(date);
		}
		else{
			return null;
		}
	}
	
	/**
	 * 增减天数 <br>
	 * @param date 日期
	 * @param d 增减天数
	 * @return Date 计算后的日期<br>
	 */
	public static Date getDate(String date, int d) {
		if(date != null && !date.equals("")){
			try {
				cal.setTime(dateFormat.parse(date));
				cal.add(Calendar.DAY_OF_MONTH, d);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return cal.getTime();
		}
		else {
			return null;
		}
	}
	
	/**
	 * 返回当前时间 格式为 yyyy-MM-dd HH:mm:ss<br>
	 * @return Date 
	 */
	public static Date getCurTime() {
		return new Date(new Date().getTime());
	}
	
	/**
	 * 返回当前日期 格式为 yyyy-MM-dd<br>
	 * @return Date 
	 */
	public static Date getCurDate() {
		Date date = null;
		try {
			date = dateFormat.parse(parseStrDate(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 得到当前年份 <br>
	 * @return int 年份
	 */
	public static int getCurYear(){
		cal.setTime(new Date());
		return cal.get(Calendar.YEAR);
	}
	/**
	 * 得到当前月份 <br>
	 * @return int 月份
	 */
	public static int getCurMon(){
		cal.setTime(new Date());
		int curMon = cal.get(Calendar.MONTH)+1;
		return curMon;
	}
	/**
	 * 得到当前日 <br>
	 * @return int 日
	 */
	public static int getCurDay(){
		cal.setTime(new Date());
		return cal.get(Calendar.DATE);
	}
	/**
	 * 格式化日期（y-m-d） <br>
	 * @param dateStr 日期字符串
	 * @return Date 格式化后的日期
	 */
	public static Date formatDate(String dateStr){
		return formatByType(dateStr,"yyyy-MM-dd");
	}
	/**
	 * 格式化日期（y-m-d h:m） <br>
	 * @param timeStr 日期字符串
	 * @return Date 格式化后的日期
	 */
	public static Date formatTime(String timeStr){
		return formatByType(timeStr,"yyyy-MM-dd HH:mm");
	}
	/**
	 * 格式化日期（y-m-d h:m:s） <br>
	 * @param timeStr 日期字符串
	 * @return Date 格式化后的日期
	 */
	public static Date formatTimeS(String timeStr){
		return formatByType(timeStr,"yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 根据格式格式化日期 <br>
	 * @param dateStr 日期
	 * @param formatStr 格式
	 * @return Date 
	 */
	public static Date formatByType(String dateStr, String formatStr){
		if(dateStr.indexOf("/")!=-1){
			dateStr = dateStr.replaceAll("/","-");
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
		Date date = null;
		if(dateStr != null && !dateStr.equals("")){
			try {
				date = dateFormat.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}
	
	/*public static void main(String[] args){
		System.out.println(">>>"+GetDate.getCurTime());
//		String[] test = GetDate.getDates("2001-03-01", "2001-01-02");
//		for(int i=0; i<test.length; i++){
//			System.out.println(cal.get(2)+">>>"+test[i]);
//		}
	}*/
}
