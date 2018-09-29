package com.psit.struts.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * 日期操作类 <br>
 *
 * create_date: Aug 20, 2010,5:44:39 PM<br>
 * @author zjr
 */
public class OperateDate {
	/**
	 * 增减天数 <br>
	 * create_date: Aug 20, 2010,5:46:56 PM <br>
	 * @param date 日期
	 * @param x 增减天数
	 * @return Date 计算后的日期<br>
	 */
	public static Date getDate(Date date, int x) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, x);
		return (Date) cal.getTime();
	}

	/**
	 * 
	 * 增减月数 <br>
	 * create_date: Aug 23, 2010,9:54:13 AM <br>
	 * @param month 当前月份
	 * @param x 增减月数
	 * @return Date 计算后的日期<br>
	 */
	public static Date getMonth(Date month, int x) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(month);
		cal.add(Calendar.MONTH, x);
		return (Date) cal.getTime();
	}

	/**
	 * 
	 * 得到两个日期之间月数 <br>
	 * create_date: Aug 23, 2010,10:06:46 AM <br>
	 * @param date1 日期1（不能为null）
	 * @param date2 日期2（不能为null）
	 * @return int 日期间隔月数<br>
	 */
	public static int getMonthNum(Date date1, Date date2) {
		Date startDate,endDate;
		if(date1.before(date2)){
			startDate = date1;
			endDate = date2;
		}
		else{
			startDate = date2;
			endDate = date1;
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(startDate);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(endDate);
		return (cal2.get(1) - cal1.get(1)) * 12 + (cal2.get(2) - cal1.get(2));
	}

	/**
	 * 计算两个日期相差天数 <br>
	 * create_date: Aug 23, 2010,10:09:24 AM <br>
	 * @param date1 日期1
	 * @param date2 日期2
	 * @return long 返回天数<br>
	 */
	public static long getDayNum(Date date1, Date date2) {
		long num = date2.getTime() - date1.getTime();
		long day = num / (3600 * 24 * 1000);
		return day;
	}

	/**
	 * 
	 * 查询条件中日期判断 <br>
	 * create_date: Aug 23, 2010,10:21:35 AM <br>
	 * @param date1 日期1
	 * @param date2 日期2
	 * @return String[] 返回日期数组<br>
	 */
	public static String[] checkDate(String date1, String date2) {
		String date[] = new String[2];
		Date inpDate = new Date(new Date().getTime());
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String cdate1 = "";
		String cdate2 = "";
		if (date1 == null || date1.equals("")) {
			//cdate1=dateFormat.format(inpDate);
			cdate1 = date1;
		} else {
			cdate1 = date1;
		}
		if (date2 == null || date2.equals("")) {
			cdate2 = dateFormat.format(inpDate);
		} else {
			cdate2 = date2;
		}
		if ((date1 == null || date1.equals(""))
				&& (date2 == null || date2.equals(""))) {
			cdate1 = "";
			cdate2 = "";
		}
		date[0] = cdate1;
		date[1] = cdate2;
		return date;
	}
}
