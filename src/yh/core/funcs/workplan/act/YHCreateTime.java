package yh.core.funcs.workplan.act;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import yh.core.util.YHUtility;
public class YHCreateTime {
  public static void main(String[] args) throws  Exception { 
    String startday2 = "2010-03-20";//开始时间 
    String endday2 = "2010-04-16";//结束时间
    Date startday = YHUtility.parseDate(startday2);
    Date endday = YHUtility.parseDate(endday2);
    Calendar c = Calendar.getInstance(); 
    c.setTime(startday); 
    int weekthInt = c.get(Calendar.WEEK_OF_MONTH);   
    String start = getFullTimeStr(getStartEnd(Integer.parseInt(startday2.substring(0,4)),weekthInt)[0]);//本周的第一天
    Date startDate = YHUtility.parseDate(start); //到哪一天 
    //String end = getFullTimeStr(getStartEnd(2010,weekthInt)[1]); //本周最后一天
    c.setTime(endday);
    int weekthInt2 = c.get(Calendar.WEEK_OF_YEAR);  
    String end2 = getFullTimeStr(getStartEnd(Integer.parseInt(endday2.substring(0,4)),weekthInt2)[1]);//本周的最后一天天

    //得到相差多少天 
    int spanDays = getIntervalDays(startDate,YHUtility.parseDate(end2)); 
//    List list2 = getDays(startDate, endday, spanDays);
//    for (int i = 0 ; i < list2.size() ; i ++) {
//      System.out.println(list2.get(i).toString().substring(0, 11)); 
//    }
//    System.out.println(endday2.substring(8,10));

        int days = YHCreateTime.getIntervalDays(startday, endday);
        int week = days/7;
        if (days%7 != 0 && days%7 <= 3) {
          //System.out.println(days%7 + "dd");
          week = days/7 + 1;
        }
        if (days%7 != 0 && days%7 >= 4) {
          //System.out.println(days%7 + "bb");
          week = days/7 + 2;
        }
        if (week == 0) {
          week = 1;
        }
        //System.out.println(week);
  }
  public static Calendar[] getStartEnd(int year,int weeknum) { 
    /*参数说明 
     * int year 年分 例如 2005 
     * int weeknum 第几周 例如33 
     * 返回一个Calendar数组，长度为2 
     * 分别是开始日期和结束日期 
     */ 
    Calendar cal = Calendar.getInstance(); 
    cal.set(Calendar.YEAR, year); 
    cal.set(Calendar.WEEK_OF_YEAR, weeknum); 
    int nw = cal.get(Calendar.DAY_OF_WEEK); 
    Calendar start = (Calendar) cal.clone(); 
    Calendar end = (Calendar) cal.clone(); 
    start.add(Calendar.DATE, 1 - nw); 
    end.add(Calendar.DATE, 7 - nw); 
    Calendar[] darr = { start, end }; 
    return darr; 
  } 
  public static String getFullTimeStr(Calendar d) { 
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
    return dateFormat.format(d.getTime()); 
  } 

  /** 
   * 得到本周第一天到最后一天的全部日起 
   */ 
  public static List getDays(Date start,Date end,int day){ 
    List dayList = new ArrayList(); 
    String days = ""; 
    Calendar calendar = new GregorianCalendar(); 
    for(int i = 0;i<=day;i++){ 
      calendar.setTime(start); 
      calendar.add(Calendar.DATE,+i) ; 
      Date dateTemp = calendar.getTime(); 
      String dateTempStr = YHUtility.getDateTimeStr(dateTemp); 
      dayList.add(dateTempStr); 
      days = days + dateTempStr + ","; 
    } 
    if(day>0){ 
      days = days.substring(0,days.length()-1); 
    } 
    // return days; 
    return dayList; 
  }
  /** 
   * 相隔多少天 
   * @param args 
   * @throws ParseException 
   */ 
  public static int getIntervalDays(Date startday,Date endday){ 
    if(startday.after(endday)){ 
      Date cal=startday; 
      startday=endday; 
      endday=cal; 
    } 
    long sl=startday.getTime(); 
    long el=endday.getTime(); 
    long ei=el-sl; 
    return (int)(ei/(1000*60*60*24)); 
  } 
}
