package yh.subsys.oa.fillRegister.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.attendance.data.YHAttendConfig;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.fillRegister.data.YHAttendFill;
import yh.subsys.oa.fillRegister.data.YHAttendNoCheck;

public class YHAttendFillLogic {
  private static Logger log = Logger.getLogger(YHAttendFillLogic.class);
  
  public void addAttendFill(Connection dbConn, YHAttendFill scoreFlow) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, scoreFlow);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }
  
  public boolean getAttendScoreFlag(Connection dbConn, String registerType, java.util.Date createTime)
  throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      String sql = "SELECT count(*) from oa_attendance_integral where REGISTER_TYPE = '" + registerType + "' and "+YHDBUtility.getDayFilter("CREATE_TIME", createTime)+"";
      rs = stmt.executeQuery(sql);
      long count = 0;
      if (rs.next()) {
        count = rs.getLong(1);
      }
      if(count > 0){
        return true;
      }else{
        return false;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  /**
   * 处长主观分
   * @param conn
   * @param year
   * @param month
   * @param userId
   * @return
   * @throws Exception
   */
  public String getDirectorScore(Connection conn, String year, String month, String userId) throws Exception {
    String result = "";
    String ymd = "";
    if(year == null){
      ymd = year+"-"+month+"-"+"07";
   }else{
      ymd = year+"-"+month+"-"+"07";
   }
    String sql = " select SCORE from oa_score_data where PARTICIPANT='"+userId+"' and "+ YHDBUtility.getMonthFilter("RANK_DATE", YHUtility.parseDate(ymd));
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        String toId = rs.getString(1);
        if (toId != null) {
          result += toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 取得当前月的最大天数
   * @param dbConn
   * @param record
   * @param person
   * @throws Exception
   */
  public int getMaxDay(String ymd) throws Exception{
    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance(); 
    calendar.setTime(dateFormat1.parse(ymd + "-07")); 
    int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数 
    return maxDay;
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
   * 返回两个日期的相隔月份
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
  
  public String showTimeStr(Connection dbConn, String beginDate, String endDate) throws Exception{
    String data = "";
    String dataNo = "";
    String firstDate = "";
    String lastDate = "";
    StringBuffer sb = new StringBuffer();
    List list = getDateValue(beginDate, endDate);      
    if(list.size() == 2){
      for(int i = 0; i < list.size(); i++){
        String lenStr = String.valueOf(list.get(i));
        if(i == 0){
          firstDate = lenStr;
        }
        if(i == list.size()-1){
          lastDate = lenStr;
        }
      }
      data = firstDate + "～" + lastDate +",";
    }
    if(list.size() > 2){
      for(int i = 0; i < list.size(); i++){
        String lenStr = String.valueOf(list.get(i));
        if(lenStr.length() == 7){
          dataNo += lenStr + ",";
        }
        if(lenStr.length() == 10){
          if(i == 0){
            int maxDays = getMaxDay(lenStr);
            String endTime = beginDate.substring(0, 7) + "-" + String.valueOf(maxDays);
            data += lenStr + "～" + endTime + ",";
          }
          if(i == list.size()-1){
            String beginDates = lenStr.substring(0, 7) + "-" + "01";
            data += beginDates + "～" + lenStr + ",";
          }
        }
      }
    }
    if(data.length() > 0){
      data = data.substring(0, data.length()-1);
    }
    if(dataNo.length() > 0){
      dataNo = dataNo.substring(0, dataNo.length()-1);
    }
    sb.append("{");
    sb.append("data:\"" + data + "\"");
    sb.append(",dataNo:\"" + dataNo + "\"");
    sb.append("}");
    return sb.toString();
  }
  
  /**
   * 出国培训补登记
   * @param dbConn
   * @param person
   * @throws Exception
   */
  public void addAttendScore(Connection dbConn, YHPerson person, String startDateStr, String endDateStr, String proposer) throws Exception{
    YHAttendConfig attend = selectConfigById(dbConn, String.valueOf(person.getDutyType()));
    List list = getDateValue(startDateStr, endDateStr);
    YHAttendFill attendFill = new YHAttendFill();
    //YHUtility.getDaySpan(s,toDate);
    String firstDate = "";
    String lastDate = "";
    if(list.size() == 2){
      for(int i = 0; i < list.size(); i++){
        String lenStr = String.valueOf(list.get(i));
        if(i == 0){
          firstDate = lenStr;
        }
        if(i == list.size()-1){
          lastDate = lenStr;
        }
      }
      String dayList = getDayList(dbConn, firstDate, lastDate);
      String[] dayStr = dayList.split(",");
      for(int z = 0; z < dayStr.length; z++){
        if(isWeekend(dayStr[z])){
          continue;
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime1())){
          getAddRegisterFunc2(dbConn, attendFill, person, dayStr[z], "1", proposer);
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime2())){
          getAddRegisterFunc2(dbConn, attendFill, person, dayStr[z], "2", proposer);
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime3())){
          getAddRegisterFunc2(dbConn, attendFill, person, dayStr[z], "3", proposer);
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime4())){
          getAddRegisterFunc2(dbConn, attendFill, person, dayStr[z], "4", proposer);
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime5())){
          getAddRegisterFunc2(dbConn, attendFill, person, dayStr[z], "5", proposer);
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime6())){
          getAddRegisterFunc2(dbConn, attendFill, person, dayStr[z], "6", proposer);
        }
      }
    }
    if(list.size() > 2){
      String beginDate = "";
      String endDate = "";
      for(int i = 0; i < list.size(); i++){
        String lenStr = String.valueOf(list.get(i));
        if(lenStr.length() == 7){
          YHAttendNoCheck anc = new YHAttendNoCheck();
          anc.setNoCheckDate(YHUtility.parseDate(lenStr+"-01"));
          addAttendNoCheck(dbConn, anc);
        }
        if(lenStr.length() == 10){
          int num = 0;
          if(i == 0){
            beginDate = lenStr;
            int maxDays = getMaxDay(beginDate);
            String endTime = beginDate.substring(0, 7) + "-" + String.valueOf(maxDays);
            String daysStr = getDayList(dbConn, beginDate, endTime);
            String[] dayStr = daysStr.split(",");
            for(int x = 0; x < dayStr.length; x++){
              if(isWeekend(dayStr[x])){
                continue;
              }
              if(!YHUtility.isNullorEmpty(attend.getDutyTime1())){
                getAddRegisterFunc2(dbConn, attendFill, person, dayStr[x], "1", proposer);
              }
              if(!YHUtility.isNullorEmpty(attend.getDutyTime2())){
                getAddRegisterFunc2(dbConn, attendFill, person, dayStr[x], "2", proposer);
              }
              if(!YHUtility.isNullorEmpty(attend.getDutyTime3())){
                getAddRegisterFunc2(dbConn, attendFill, person, dayStr[x], "3", proposer);
              }
              if(!YHUtility.isNullorEmpty(attend.getDutyTime4())){
                getAddRegisterFunc2(dbConn, attendFill, person, dayStr[x], "4", proposer);
              }
              if(!YHUtility.isNullorEmpty(attend.getDutyTime5())){
                getAddRegisterFunc2(dbConn, attendFill, person, dayStr[x], "5", proposer);
              }
              if(!YHUtility.isNullorEmpty(attend.getDutyTime6())){
                getAddRegisterFunc2(dbConn, attendFill, person, dayStr[x], "6", proposer);
              }
            }
          }
          if(i == list.size()-1){
            endDate = lenStr;
            String beginDates = endDate.substring(0, 7) + "-" + "01";
            String daysStr = getDayList(dbConn, beginDates, endDate);
            String[] dayStr = daysStr.split(",");
            for(int x = 0; x < dayStr.length; x++){
              if(isWeekend(dayStr[x])){
                continue;
              }
              if(!YHUtility.isNullorEmpty(attend.getDutyTime1())){
                getAddRegisterFunc2(dbConn, attendFill, person, dayStr[x], "1", proposer);
              }
              if(!YHUtility.isNullorEmpty(attend.getDutyTime2())){
                getAddRegisterFunc2(dbConn, attendFill, person, dayStr[x], "2", proposer);
              }
              if(!YHUtility.isNullorEmpty(attend.getDutyTime3())){
                getAddRegisterFunc2(dbConn, attendFill, person, dayStr[x], "3", proposer);
              }
              if(!YHUtility.isNullorEmpty(attend.getDutyTime4())){
                getAddRegisterFunc2(dbConn, attendFill, person, dayStr[x], "4", proposer);
              }
              if(!YHUtility.isNullorEmpty(attend.getDutyTime5())){
                getAddRegisterFunc2(dbConn, attendFill, person, dayStr[x], "5", proposer);
              }
              if(!YHUtility.isNullorEmpty(attend.getDutyTime6())){
                getAddRegisterFunc2(dbConn, attendFill, person, dayStr[x], "6", proposer);
              }
            }
          }
        }
      }
    }
  }
  
  public void getAddRegisterFunc(Connection dbConn, YHAttendFill attendFill, YHPerson person,  String lenStr,  String dateStr,  String registerType) throws Exception{
    attendFill.setAssessingStatus("1");
    attendFill.setFillTime(YHUtility.parseDate(lenStr + "-" + dateStr));
    attendFill.setRegisterType(registerType);
    attendFill.setAssessingTime(new Date());
    addAttendFillScore(dbConn, attendFill);
  } 
  
  public void getAddRegisterFunc2(Connection dbConn, YHAttendFill attendFill, YHPerson person,  String dateStr,  String registerType, String proposer) throws Exception{
    attendFill.setAssessingStatus("1");
    attendFill.setFillTime(YHUtility.parseDate(dateStr));
    attendFill.setRegisterType(registerType);
    attendFill.setAssessingTime(new Date());
    attendFill.setAttendFlag("0");
    attendFill.setProposer(proposer);
    addAttendFillScore(dbConn, attendFill);
  } 
  
  public void addAttendFillScore(Connection dbConn, YHAttendFill attendFill) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, attendFill);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }
  
  /**
   * 添加不参加考核月份
   * @param dbConn
   * @param anc
   * @throws Exception
   */
  public void addAttendNoCheck(Connection dbConn, YHAttendNoCheck anc) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, anc);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
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
   * 年休假自动补登记--cc
   * @param dbConn
   * @param person
   * @param startDateStr
   * @param endDateStr
   * @param proposer
   * @throws Exception
   */
  public void addAttendScoreYear(Connection dbConn, YHPerson person, String startDateStr, String endDateStr, String proposer, String assessingOfficer) throws Exception{
    YHAttendConfig attend = selectConfigById(dbConn, String.valueOf(person.getDutyType()));
    YHAttendFill attendFill = new YHAttendFill();
    String firstDate = "";
    String lastDate = "";
    String beginDate = startDateStr.substring(0, 10);
    String endDate = endDateStr.substring(0,10);
    String daysStr = getDayList(dbConn, beginDate, endDate);
    String[] dayStr = daysStr.split(",");
    for(int x = 0; x < dayStr.length; x++){
      if(isWeekend(dayStr[x])){
        continue;
      }
      if(!YHUtility.isNullorEmpty(attend.getDutyTime1())){
        getAutoAddRegisterFunc(dbConn, attendFill, dayStr[x], "1", proposer, assessingOfficer);
      }
      if(!YHUtility.isNullorEmpty(attend.getDutyTime2())){
        getAutoAddRegisterFunc(dbConn, attendFill, dayStr[x], "2", proposer, assessingOfficer);
      }
      if(!YHUtility.isNullorEmpty(attend.getDutyTime3())){
        getAutoAddRegisterFunc(dbConn, attendFill, dayStr[x], "3", proposer, assessingOfficer);
      }
      if(!YHUtility.isNullorEmpty(attend.getDutyTime4())){
        getAutoAddRegisterFunc(dbConn, attendFill, dayStr[x], "4", proposer, assessingOfficer);
      }
      if(!YHUtility.isNullorEmpty(attend.getDutyTime5())){
        getAutoAddRegisterFunc(dbConn, attendFill, dayStr[x], "5", proposer, assessingOfficer);
      }
      if(!YHUtility.isNullorEmpty(attend.getDutyTime6())){
        getAutoAddRegisterFunc(dbConn, attendFill, dayStr[x], "6", proposer, assessingOfficer);
      }
    }
  }
  
  /**
   * 出差自动补登记--cc
   * @param dbConn
   * @param person
   * @param startDateStr
   * @param endDateStr
   * @param proposer
   * @throws Exception
   */
  public void addAttendScoreAbord(Connection dbConn, YHPerson person, String startDateStr, String endDateStr, String proposer, String assessingOfficer) throws Exception{
    YHAttendConfig attend = selectConfigById(dbConn, String.valueOf(person.getDutyType()));
    YHAttendFill attendFill = new YHAttendFill();
    String firstDate = "";
    String lastDate = "";
    String daysStr = getDayList(dbConn, startDateStr, endDateStr);
    String[] dayStr = daysStr.split(",");
    for(int x = 0; x < dayStr.length; x++){
      if(isWeekend(dayStr[x])){
        continue;
      }
      if(!YHUtility.isNullorEmpty(attend.getDutyTime1())){
        getAutoAddRegisterFunc(dbConn, attendFill, dayStr[x], "1", proposer, assessingOfficer);
      }
      if(!YHUtility.isNullorEmpty(attend.getDutyTime2())){
        getAutoAddRegisterFunc(dbConn, attendFill, dayStr[x], "2", proposer, assessingOfficer);
      }
      if(!YHUtility.isNullorEmpty(attend.getDutyTime3())){
        getAutoAddRegisterFunc(dbConn, attendFill, dayStr[x], "3", proposer, assessingOfficer);
      }
      if(!YHUtility.isNullorEmpty(attend.getDutyTime4())){
        getAutoAddRegisterFunc(dbConn, attendFill, dayStr[x], "4", proposer, assessingOfficer);
      }
      if(!YHUtility.isNullorEmpty(attend.getDutyTime5())){
        getAutoAddRegisterFunc(dbConn, attendFill, dayStr[x], "5", proposer, assessingOfficer);
      }
      if(!YHUtility.isNullorEmpty(attend.getDutyTime6())){
        getAutoAddRegisterFunc(dbConn, attendFill, dayStr[x], "6", proposer, assessingOfficer);
      }
    }
  }
  
  public String getDutyType(Connection conn, int userId) throws Exception {
    String result = "";
    String sql = " select DUTY_TYPE from PERSON where SEQ_ID = " + userId;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String toId = rs.getString(1);
        if (toId != null) {
          result = toId;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
  
  /**
   * 适用于出国记录和培训记录
   * @param dbConn
   * @param person
   * @param startDateStr
   * @param endDateStr
   * @param proposer
   * @param assessingOfficer
   * @throws Exception
   */
  public void autoAttendFill(Connection dbConn, YHPerson person, String startDateStr, String endDateStr, String proposer, String assessingOfficer) throws Exception{
    String dutyType = getDutyType(dbConn, Integer.parseInt(proposer));
    YHAttendConfig attend = selectConfigById(dbConn, dutyType);
    YHAttendFill attendFill = new YHAttendFill();
    String firstDate = "";
    String lastDate = "";
    String daysStr = getDayList(dbConn, startDateStr, endDateStr);
    String[] dayStr = daysStr.split(",");
    for(int x = 0; x < dayStr.length; x++){
      if(isWeekend(dayStr[x])){
        continue;
      }
      if(attend != null && !YHUtility.isNullorEmpty(attend.getDutyTime1())){
        getAutoAddRegisterFunc(dbConn, attendFill, dayStr[x], "1", proposer, assessingOfficer);
      }
      if(attend != null &&!YHUtility.isNullorEmpty(attend.getDutyTime2())){
        getAutoAddRegisterFunc(dbConn, attendFill, dayStr[x], "2", proposer, assessingOfficer);
      }
      if(attend != null &&!YHUtility.isNullorEmpty(attend.getDutyTime3())){
        getAutoAddRegisterFunc(dbConn, attendFill, dayStr[x], "3", proposer, assessingOfficer);
      }
      if(attend != null &&!YHUtility.isNullorEmpty(attend.getDutyTime4())){
        getAutoAddRegisterFunc(dbConn, attendFill, dayStr[x], "4", proposer, assessingOfficer);
      }
      if(attend != null &&!YHUtility.isNullorEmpty(attend.getDutyTime5())){
        getAutoAddRegisterFunc(dbConn, attendFill, dayStr[x], "5", proposer, assessingOfficer);
      }
      if(attend != null &&!YHUtility.isNullorEmpty(attend.getDutyTime6())){
        getAutoAddRegisterFunc(dbConn, attendFill, dayStr[x], "6", proposer, assessingOfficer);
      }
    }
  }
  
  /**
   * 自动补登记
   * @param dbConn
   * @param attendFill
   * @param person
   * @param dateStr
   * @param registerType
   * @param proposer
   * @throws Exception
   */
  public void getAutoAddRegisterFunc(Connection dbConn, YHAttendFill attendFill,  String dateStr,  String registerType, String proposer, String assessingOfficer) throws Exception{
    attendFill.setAssessingStatus("1");
    attendFill.setFillTime(YHUtility.parseDate(dateStr));
    attendFill.setRegisterType(registerType);
    attendFill.setAssessingTime(new Date());
    attendFill.setAttendFlag("0");
    attendFill.setProposer(proposer);
    attendFill.setAssessingOfficer(assessingOfficer);
    addAttendFillScore(dbConn, attendFill);
  } 
  
  /**
   * 请假自动补登记
   * @param dbConn
   * @param person
   * @param startDateStr
   * @param endDateStr
   * @param proposer
   * @param assessingOfficer
   * @throws Exception
   */
  public void addAttendScoreLeave(Connection dbConn, YHPerson person, String startDateStr, String endDateStr, String proposer, String assessingOfficer) throws Exception{
    String dutyType = getDutyType(dbConn, Integer.parseInt(proposer));
    YHAttendConfig attend = selectConfigById(dbConn, dutyType);
    YHAttendFill attendFill = new YHAttendFill();
    String firstDate = startDateStr.substring(0, 10);
    String lastDate = endDateStr.substring(0, 10);
    String beginTime = startDateStr.substring(11, 19);
    String endTime = endDateStr.substring(11, 19);
    String daysStr = getDayList(dbConn, startDateStr, endDateStr);
    String[] dayStr = daysStr.split(",");
    if(firstDate.compareTo(lastDate) == 0){
      if(!isWeekend(firstDate)){
        if(!YHUtility.isNullorEmpty(attend.getDutyTime1())){
          if(beginTime.compareTo(attend.getDutyTime1()) <= 0 && endTime.compareTo(attend.getDutyTime1()) >= 0){
            getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "1", proposer, assessingOfficer);
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime2())){
          if(beginTime.compareTo(attend.getDutyTime2()) <= 0 && endTime.compareTo(attend.getDutyTime2()) >= 0){
            getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "2", proposer, assessingOfficer);
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime3())){
          if(beginTime.compareTo(attend.getDutyTime3()) <= 0 && endTime.compareTo(attend.getDutyTime3()) >= 0){
            getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "3", proposer, assessingOfficer);
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime4())){
          if(beginTime.compareTo(attend.getDutyTime4()) <= 0 && endTime.compareTo(attend.getDutyTime4()) >= 0){
            getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "4", proposer, assessingOfficer);
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime5())){
          if(beginTime.compareTo(attend.getDutyTime5()) <= 0 && endTime.compareTo(attend.getDutyTime5()) >= 0){
            getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "5", proposer, assessingOfficer);
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime6())){
          if(beginTime.compareTo(attend.getDutyTime6()) <= 0 && endTime.compareTo(attend.getDutyTime6()) >= 0){
            getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "6", proposer, assessingOfficer);
          }
        }
      }
    }else{
      if(!isWeekend(startDateStr)){
        String endTimeStr = "23:59:59";
        if(!YHUtility.isNullorEmpty(attend.getDutyTime1())){
          if(beginTime.compareTo(attend.getDutyTime1()) <= 0 && endTimeStr.compareTo(attend.getDutyTime1()) >= 0){
            getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "1", proposer, assessingOfficer);
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime2())){
          if(beginTime.compareTo(attend.getDutyTime2()) <= 0 && endTimeStr.compareTo(attend.getDutyTime2()) >= 0){
            getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "2", proposer, assessingOfficer);
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime3())){
          if(beginTime.compareTo(attend.getDutyTime3()) <= 0 && endTimeStr.compareTo(attend.getDutyTime3()) >= 0){
            getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "3", proposer, assessingOfficer);
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime4())){
          if(beginTime.compareTo(attend.getDutyTime4()) <= 0 && endTimeStr.compareTo(attend.getDutyTime4()) >= 0){
            getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "4", proposer, assessingOfficer);
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime5())){
          if(beginTime.compareTo(attend.getDutyTime5()) <= 0 && endTimeStr.compareTo(attend.getDutyTime5()) >= 0){
            getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "5", proposer, assessingOfficer);
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime6())){
          if(beginTime.compareTo(attend.getDutyTime6()) <= 0 && endTimeStr.compareTo(attend.getDutyTime6()) >= 0){
            getAutoAddRegisterFunc(dbConn, attendFill, firstDate, "6", proposer, assessingOfficer);
          }
        }
      }
      if(!isWeekend(endDateStr)){
        String beginTimeStr = "00:00:00";
        if(!YHUtility.isNullorEmpty(attend.getDutyTime1())){
          if(beginTimeStr.compareTo(attend.getDutyTime1()) <= 0 && endTime.compareTo(attend.getDutyTime1()) >= 0){
            getAutoAddRegisterFunc(dbConn, attendFill, lastDate, "1", proposer, assessingOfficer);
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime2())){
          if(beginTimeStr.compareTo(attend.getDutyTime2()) <= 0 && endTime.compareTo(attend.getDutyTime2()) >= 0){
            getAutoAddRegisterFunc(dbConn, attendFill, lastDate, "2", proposer, assessingOfficer);
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime3())){
          if(beginTimeStr.compareTo(attend.getDutyTime3()) <= 0 && endTime.compareTo(attend.getDutyTime3()) >= 0){
            getAutoAddRegisterFunc(dbConn, attendFill, lastDate, "3", proposer, assessingOfficer);
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime4())){
          if(beginTimeStr.compareTo(attend.getDutyTime4()) <= 0 && endTime.compareTo(attend.getDutyTime4()) >= 0){
            getAutoAddRegisterFunc(dbConn, attendFill, lastDate, "4", proposer, assessingOfficer);
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime5())){
          if(beginTimeStr.compareTo(attend.getDutyTime5()) <= 0 && endTime.compareTo(attend.getDutyTime5()) >= 0){
            getAutoAddRegisterFunc(dbConn, attendFill, lastDate, "5", proposer, assessingOfficer);
          }
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime6())){
          if(beginTimeStr.compareTo(attend.getDutyTime6()) <= 0 && endTime.compareTo(attend.getDutyTime6()) >= 0){
            getAutoAddRegisterFunc(dbConn, attendFill, lastDate, "6", proposer, assessingOfficer);
          }
        }
      }
      for(int y = 1; y < dayStr.length - 1; y++){
        if(isWeekend(dayStr[y])){
          continue;
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime1())){
          getAutoAddRegisterFunc(dbConn, attendFill, dayStr[y], "1", proposer, assessingOfficer);
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime2())){
          getAutoAddRegisterFunc(dbConn, attendFill, dayStr[y], "2", proposer, assessingOfficer);
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime3())){
          getAutoAddRegisterFunc(dbConn, attendFill, dayStr[y], "3", proposer, assessingOfficer);
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime4())){
          getAutoAddRegisterFunc(dbConn, attendFill, dayStr[y], "4", proposer, assessingOfficer);
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime5())){
          getAutoAddRegisterFunc(dbConn, attendFill, dayStr[y], "5", proposer, assessingOfficer);
        }
        if(!YHUtility.isNullorEmpty(attend.getDutyTime6())){
          getAutoAddRegisterFunc(dbConn, attendFill, dayStr[y], "6", proposer, assessingOfficer);
        }
      }
    }
  }
}
