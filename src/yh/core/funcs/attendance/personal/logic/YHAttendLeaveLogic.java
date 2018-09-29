package yh.core.funcs.attendance.personal.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.attendance.personal.data.YHAttendLeave;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.attendance.data.YHAttendConfig;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.addworkfee.data.YHCalendary;
import yh.subsys.oa.fillRegister.data.YHAttendFill;

public class YHAttendLeaveLogic {
  public static long HOUR = 60 * 60 * 1000 ;
  private static Logger log = Logger.getLogger(YHAttendLeaveLogic.class);
  public void addLeave(Connection dbConn, YHAttendLeave leave) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, leave);  
  }
  public List<YHAttendLeave> selectLeave(Connection dbConn,int userId) throws Exception {
    List<YHAttendLeave> leaveList = new ArrayList<YHAttendLeave>();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    Statement stmt = null;
    ResultSet rs = null;
    //String sql = "select * from (select * from oa_attendance_off where USER_ID = '" + userId+"' and STATUS = '1' ) a where ALLOW = '0' or ALLOW = '1' or ALLOW = '2' or ALLOW='3' order by LEAVE_DATE1" ;
    //显示所有请假
    String sql = "select * from oa_attendance_off where USER_ID = '" + userId+"' and ALLOW in ('2', '3','0') order by LEAVE_DATE1" ;
    //System.out.println(sql);
      try {
        stmt = dbConn.createStatement();
        rs = stmt.executeQuery(sql);
        while(rs.next()){
          YHAttendLeave yhal = new YHAttendLeave();
          yhal.setSeqId(rs.getInt("SEQ_ID"));
          yhal.setUserId(rs.getString("USER_ID"));
          yhal.setLeaderId(rs.getString("LEADER_ID"));
          yhal.setAnnualLeave(rs.getInt("ANNUAL_LEAVE"));
          yhal.setLeaveType(rs.getString("LEAVE_TYPE"));
          yhal.setReason(rs.getString("REASON"));
          yhal.setRegisterIp(rs.getString("REGISTER_IP"));
          yhal.setAllow(rs.getString("ALLOW"));
          yhal.setStatus(rs.getString("STATUS"));
          String destroyTime=rs.getString("DESTROY_TIME");
          String leaceDate=rs.getString("LEAVE_DATE1");
          String leaceDate2=rs.getString("LEAVE_DATE2");
          
          int index1=leaceDate.lastIndexOf(".");
          String dateTime1=leaceDate.substring(0,index1);
          int index2=leaceDate2.lastIndexOf(".");
          String dateTime2=leaceDate2.substring(0,index1);
         
          if(rs.getString("DESTROY_TIME") != null){
        	   int index=destroyTime.lastIndexOf(".");
               String dateTime=destroyTime.substring(0,index);
            yhal.setDestroyTime(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss",dateTime ));
          }
          //System.out.println(rs.getDate("DESTROY_TIME"));
          yhal.setLeaveDate1(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss", dateTime1));
          yhal.setLeaveDate2(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss", dateTime2));
          leaveList.add(yhal);
        }  
      }catch(Exception ex) {
         throw ex;
      }finally {
        YHDBUtility.close(stmt, rs, log);
    } 
    return leaveList;
  }
  public long selectLeaveDaysByUserId(Connection dbConn,String userId) throws Exception {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    Statement stmt = null;
    ResultSet rs = null;
    long leaveDaysTotal = 0;
    String sql = "select LEAVE_DATE1,LEAVE_DATE2  from  oa_attendance_off where USER_ID = '" + userId+"' and STATUS = '2' and allow = '3' and " + YHDBUtility.getYearFilter("LEAVE_DATE2", new Date()) ;
    //System.out.println(sql);
      try {
        stmt = dbConn.createStatement();
        rs = stmt.executeQuery(sql);
        while(rs.next()){
          if(!YHUtility.isNullorEmpty(rs.getString("LEAVE_DATE1"))&&!YHUtility.isNullorEmpty(rs.getString("LEAVE_DATE2"))){
            leaveDaysTotal = leaveDaysTotal + getDateTime(formatter.parse(rs.getString("LEAVE_DATE1")) ,formatter.parse(rs.getString("LEAVE_DATE2")));
          }
        }  
      }catch(Exception ex) {
         throw ex;
      }finally {
        YHDBUtility.close(stmt, rs, log);
    } 
    return leaveDaysTotal;
  }
  /**
   * 根据两个日期得到相隔多长时长精确到时
   * @param date1
   * @param date2
   * @return
   */
  public  long getDateTime(Date date1, Date date2){
    long  dateTime1 = date1.getTime(); 
    long  dateTime2 = date2.getTime(); 
    long  dateTime = (dateTime2 - dateTime1)/(1000*3600);//精确到分
    return dateTime;
  }
  public List<YHAttendLeave> selectLeave(Connection dbConn,String[] str) throws Exception {
    List<YHAttendLeave> leaveList = new ArrayList<YHAttendLeave>();
    YHORM orm = new YHORM();
    leaveList = orm.loadListSingle(dbConn, YHAttendLeave.class, str);
    return leaveList;
  }
  public List<YHAttendLeave>   selectHistroyLeave(Connection dbConn,String[] map) throws Exception {
    List<YHAttendLeave> leaveList = new ArrayList<YHAttendLeave>();
    YHORM orm = new YHORM();
    leaveList = orm.loadListSingle(dbConn, YHAttendLeave.class, map);
    return leaveList;
  }
  public void deleteLeaveById(Connection dbConn,String seqId) throws Exception {
    
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHAttendLeave.class, Integer.parseInt(seqId));
  }
  public YHAttendLeave selectLeaveById(Connection dbConn,String seqId) throws Exception {
    YHORM orm = new YHORM();
    YHAttendLeave leave = (YHAttendLeave) orm.loadObjSingle(dbConn, YHAttendLeave.class, Integer.parseInt(seqId));
    return leave;
  }
  public void updateLeave(Connection dbConn,YHAttendLeave leave) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, leave);
  }
  public void updateStatus(Connection dbConn,Map map) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, "attendLeave",map);
  }
  
  /**
   * 获取考勤表对象(ATTEND_CONFIG)
   * @param dbConn
   * @param seqIds
   * @return
   * @throws Exception
   */
  public YHAttendConfig selectConfigById(Connection dbConn, String seqIds)
  throws Exception {
    YHORM orm = new YHORM();
    YHAttendConfig config = new YHAttendConfig();
    int seqId = 0;
    if (!seqIds.equals("")) {
      seqId = Integer.parseInt(seqIds);
      config = (YHAttendConfig) orm.loadObjSingle(dbConn, YHAttendConfig.class,
      seqId);
    }
    return config;
  }
  
  /**
   * 获取开始日期和结束日期之间的所有日期串
   * @param dbConn
   * @param beginTime
   * @param endTime
   * @return
   * @throws Exception
   */
  public String getDayList(Connection dbConn, String beginTime,String endTime) throws Exception {
    //相隔多少天
    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
    long daySpace = YHUtility.getDaySpan(dateFormat1.parse(beginTime),dateFormat1.parse(endTime))+1;
    //得到到之间的天数数组
    List daysList = new ArrayList();
    String days = "";
    Calendar calendar = new GregorianCalendar();
    for(int i = 0;i<daySpace;i++){
      calendar.setTime(dateFormat1.parse(beginTime));
      calendar.add(Calendar.DATE,+i) ;
      Date dateTemp = calendar.getTime();
      String dateTempStr = dateFormat1.format(dateTemp);
      daysList.add(dateTempStr);
      days = days + dateTempStr + ",";
    }
    if(daySpace>0){
      days = days.substring(0,days.length()-1);
    }
    return days;
  }
  
  /**
   * 判断日期是否为周六日
   * @param dateStr
   * @return
   * @throws Exception
   */
  public boolean isWeekend(String dateStr) throws Exception {
    boolean flag = false;
    try {
      SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
      Calendar calendar = new GregorianCalendar();
      calendar.setTime(dateFormat1.parse(dateStr));
      Date dateTemp = calendar.getTime();
      if (getDateWeek(dateTemp) == 6 || getDateWeek(dateTemp) == 7) {
        flag = true;
      }
    } catch (Exception e) {
      throw e;
    }
    return flag;
  }
  
  public static int getDateWeek(Date date) throws ParseException {
    GregorianCalendar d = new GregorianCalendar();
    d.setTime(date);
    int today = d.get(Calendar.DAY_OF_WEEK);
    if (today == 1) {
      today = 7;
    } else {
      today = today - 1;
    }
    return today;
  }
  
  public String showTimeStr(Connection dbConn, YHPerson person, String startDateStr, String endDateStr) throws Exception{
    YHAttendConfig attend = selectConfigById(dbConn, String.valueOf(person.getDutyType()));
    YHAttendFill attendFill = new YHAttendFill();
    StringBuffer sb = new StringBuffer();
    String data = "";
    String dataStr = "";
    String firstDate = startDateStr.substring(0, 10);
    String lastDate = endDateStr.substring(0, 10);
    String beginTime = startDateStr.substring(11, 19);
    String endTime = endDateStr.substring(11, 19);
    String daysStr = getDayList(dbConn, startDateStr, endDateStr);
    List list = getDateValue(firstDate, lastDate);     
    if(list.size() > 2){
      for(int i = 0; i < list.size(); i++){
        String lenStr = String.valueOf(list.get(i));
        if(lenStr.length() == 7){
          dataStr += lenStr + ",";
        }
      }
    }
    String[] dayStr = daysStr.split(",");
    if(firstDate.compareTo(lastDate) == 0){
      if(!isWeekend(firstDate)){
        if(!YHUtility.isNullorEmpty(attend.getDutyTime1())){
          if(beginTime.compareTo(attend.getDutyTime1()) <= 0 && endTime.compareTo(attend.getDutyTime1()) >= 0){
            //getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "1", proposer, assessingOfficer);
            data += firstDate + " 第一次登记,";
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime2())){
          if(beginTime.compareTo(attend.getDutyTime2()) <= 0 && endTime.compareTo(attend.getDutyTime2()) >= 0){
            //getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "2", proposer, assessingOfficer);
            data += firstDate + " 第二次登记,";
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime3())){
          if(beginTime.compareTo(attend.getDutyTime3()) <= 0 && endTime.compareTo(attend.getDutyTime3()) >= 0){
            //getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "3", proposer, assessingOfficer);
            data += firstDate + " 第三次登记,";
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime4())){
          if(beginTime.compareTo(attend.getDutyTime4()) <= 0 && endTime.compareTo(attend.getDutyTime4()) >= 0){
            //getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "4", proposer, assessingOfficer);
            data += firstDate + " 第四次登记,";
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime5())){
          if(beginTime.compareTo(attend.getDutyTime5()) <= 0 && endTime.compareTo(attend.getDutyTime5()) >= 0){
            //getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "5", proposer, assessingOfficer);
            data += firstDate + " 第五次登记,";
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime6())){
          if(beginTime.compareTo(attend.getDutyTime6()) <= 0 && endTime.compareTo(attend.getDutyTime6()) >= 0){
            //getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "6", proposer, assessingOfficer);
            data += firstDate + " 第六次登记,";
          }
        }
      }
    }else{
      if(!isWeekend(startDateStr)){
        String endTimeStr = "23:59:59";
        if(!YHUtility.isNullorEmpty(attend.getDutyTime1())){
          if(beginTime.compareTo(attend.getDutyTime1()) <= 0 && endTimeStr.compareTo(attend.getDutyTime1()) >= 0){
            //getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "1", proposer, assessingOfficer);
            data += firstDate + " 第一次登记,";
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime2())){
          if(beginTime.compareTo(attend.getDutyTime2()) <= 0 && endTimeStr.compareTo(attend.getDutyTime2()) >= 0){
            //getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "2", proposer, assessingOfficer);
            data += firstDate + " 第二次登记,";
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime3())){
          if(beginTime.compareTo(attend.getDutyTime3()) <= 0 && endTimeStr.compareTo(attend.getDutyTime3()) >= 0){
            //getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "3", proposer, assessingOfficer);
            data += firstDate + " 第三次登记,";
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime4())){
          if(beginTime.compareTo(attend.getDutyTime4()) <= 0 && endTimeStr.compareTo(attend.getDutyTime4()) >= 0){
            //getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "4", proposer, assessingOfficer);
            data += firstDate + " 第四次登记,";
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime5())){
          if(beginTime.compareTo(attend.getDutyTime5()) <= 0 && endTimeStr.compareTo(attend.getDutyTime5()) >= 0){
            //getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "5", proposer, assessingOfficer);
            data += firstDate + " 第五次登记,";
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime6())){
          if(beginTime.compareTo(attend.getDutyTime6()) <= 0 && endTimeStr.compareTo(attend.getDutyTime6()) >= 0){
            //getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "6", proposer, assessingOfficer);
            data += firstDate + " 第六次登记,";
          }
        }
      }
      if(!isWeekend(endDateStr)){
        String beginTimeStr = "00:00:00";
        if(!YHUtility.isNullorEmpty(attend.getDutyTime1())){
          if(beginTimeStr.compareTo(attend.getDutyTime1()) <= 0 && endTime.compareTo(attend.getDutyTime1()) >= 0){
            //getAutoAddRegisterFunc(dbConn, attendFill, lastDate, "1", proposer, assessingOfficer);
            data += lastDate + " 第一次登记,";
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime2())){
          if(beginTimeStr.compareTo(attend.getDutyTime2()) <= 0 && endTime.compareTo(attend.getDutyTime2()) >= 0){
            //getAutoAddRegisterFunc(dbConn, attendFill, lastDate, "2", proposer, assessingOfficer);
            data += lastDate + " 第二次登记,";
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime3())){
          if(beginTimeStr.compareTo(attend.getDutyTime3()) <= 0 && endTime.compareTo(attend.getDutyTime3()) >= 0){
            //getAutoAddRegisterFunc(dbConn, attendFill, lastDate, "3", proposer, assessingOfficer);
            data += lastDate + " 第三次登记,";
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime4())){
          if(beginTimeStr.compareTo(attend.getDutyTime4()) <= 0 && endTime.compareTo(attend.getDutyTime4()) >= 0){
            //getAutoAddRegisterFunc(dbConn, attendFill, lastDate, "4", proposer, assessingOfficer);
            data += lastDate + " 第四次登记,";
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime5())){
          if(beginTimeStr.compareTo(attend.getDutyTime5()) <= 0 && endTime.compareTo(attend.getDutyTime5()) >= 0){
            //getAutoAddRegisterFunc(dbConn, attendFill, lastDate, "5", proposer, assessingOfficer);
            data += lastDate + " 第五次登记,";
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime6())){
          if(beginTimeStr.compareTo(attend.getDutyTime6()) <= 0 && endTime.compareTo(attend.getDutyTime6()) >= 0){
           // getAutoAddRegisterFunc(dbConn, attendFill, lastDate, "6", proposer, assessingOfficer);
            data += lastDate + " 第六次登记,";
          }
        }
      }
      for(int y = 1; y < dayStr.length - 1; y++){
//        if(isWeekend(dayStr[y])){
//          continue;
//        }
        if(y == 1){
          data += dayStr[1] + "～";
        }
        if(y == (dayStr.length-2)){
          String aa = String.valueOf(dayStr.length-2);
          data+= dayStr[dayStr.length - 2];
        }
      }
    }
    sb.append("{");
    sb.append("data:\"" + data + "\"");
    sb.append(",dataStr:\"" + dataStr + "\"");
    sb.append("}");
    return sb.toString();
  }
  
  /**
   * 返回两个日期的相隔月份--cc 20101126
   * @param startDate
   * @param endDate
   * @return
   * @throws Exception
   */
  public List<String> getDateValue(String startDateStr, String endDateStr) throws Exception {
    List<String> list = new ArrayList<String>();
    if (YHUtility.isNullorEmpty(endDateStr) && !YHUtility.isNullorEmpty(startDateStr)) {
      endDateStr = startDateStr;
      list.add(startDateStr);
      list.add(endDateStr);
      return list;
    } else if (YHUtility.isNullorEmpty(startDateStr) && !YHUtility.isNullorEmpty(endDateStr)) {
      startDateStr = endDateStr;
      list.add(startDateStr);
      list.add(endDateStr);
      return list;
    }
    try {
      if (!YHUtility.isNullorEmpty(startDateStr) && !YHUtility.isNullorEmpty(endDateStr)) {
        String startDateArry[] = startDateStr.split("-");
        String endDateArry[] = endDateStr.split("-");
        int startYear = Integer.parseInt(startDateArry[0]);
        int startMonth = Integer.parseInt(startDateArry[1]);
        int endMonth = Integer.parseInt(endDateArry[1]);
        String result = "";
        if (startMonth < endMonth) {
          list.add(startDateStr);
          int tmp = endMonth - startMonth;
          if (tmp <= 11) {
            for (int i = 1; i < tmp; i++) {
              int tmpMonth = startMonth + i;
              String str = "";
              if (tmpMonth < 10) {
                str = "0";
              }
              result = startYear + "-" + str + tmpMonth;
              list.add(result);
            }
          }
          list.add(endDateStr);
        } else if (startMonth == endMonth) {
          list.add(startDateStr);
          list.add(endDateStr);

        } else if (startMonth > endMonth) {
        }
      }
    } catch (Exception e) {
      throw e;
    }
    return list;
  }
  
  public double getAttendLeaveLogic(Connection dbConn, String year, String month, String userId) throws Exception {
    double result = 0;
    double score = 0;
    String sql = "";
    String whereStr = "";
    String ymd = year + "-" + month + "-" + "01";
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      if(!YHUtility.isNullorEmpty(ymd)){
        sql = "select HOUR from oa_attendance_off where USER_ID = '" + userId + "' and STATUS = '1' and "
        + YHDBUtility.getMonthFilter("BEGIN_TIME", YHUtility.parseDate(ymd));
      }
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        double normalAdd = rs.getDouble(1);
        score += normalAdd;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return score;
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
   * 计算精度
   */
  public static double  round(double number, String partten){
    if(partten == null || partten == ""){
      partten = "#.00";
    }
    return Double.parseDouble(new DecimalFormat(partten).format(number));
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
   * 请假总时长
   * @param dbConn
   * @param year
   * @param month
   * @param userId
   * @return
   * @throws Exception
   */
  public double getAttendLeaveHourLogic(Connection dbConn, String year, String month, String userId) throws Exception {
    double score = 0;
    String sql = "";
    String ymd = year + "-" + month + "-" + "01";
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      if(!YHUtility.isNullorEmpty(ymd)){
        sql = "select HOUR from oa_attendance_off where USER_ID = '" + userId + "' and ALLOW = '1' and "
        + YHDBUtility.getMonthFilter("LEAVE_DATE2", YHUtility.parseDate(ymd));
      }
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        double normalAdd = rs.getDouble(1);
        score += normalAdd;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return score;
  }
  
  public double getOverTimeHourLogic(Connection conn, String year, String month, String userId) throws Exception {
    double result = 0;
    String ymd = year + "-" + month + "-" + "01";
    String sql = " select OVERTIME_HOUR from oa_pm_salary_month where USER_ID = '" + userId + "' and YEAR = '" + year + "' and MONTH = '" + month + "'";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        String toId = rs.getString(1);
        if(toId != null){
          result = Double.parseDouble(toId);
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
}
