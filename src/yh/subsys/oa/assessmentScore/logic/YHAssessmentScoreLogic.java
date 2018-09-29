package yh.subsys.oa.assessmentScore.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import yh.core.funcs.person.data.YHPerson;
import org.apache.log4j.Logger;

import yh.core.funcs.system.attendance.data.YHAttendConfig;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.coefficient.logic.YHCoefficientLogic;
import yh.subsys.oa.fillRegister.data.YHAttendFill;
import yh.subsys.oa.fillRegister.data.YHAttendScore;

public class YHAssessmentScoreLogic {
  private static Logger log = Logger
      .getLogger("yh.subsys.oa.assessmentScore.logic.YHAssessmentScoreLogic.java");
  
  public boolean strValue(String[] str, String deptIdStr){
    for(int y = 0; y < str.length; y++){
      if(str[y].equals(deptIdStr)){
        return true;
      }else{
        continue;
      }
    }
    return false;
  }

  /**
   * 判断日期是否为周六日
   * 
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

  /**
   * 获取月份的天
   * @param dbConn
   * @param beginTime
   * @param endTime
   * @return
   * @throws Exception
   */
  public String getDayList(Connection dbConn, String beginTime, String endTime)
      throws Exception {
    // 相隔多少天
    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
    long daySpace = YHUtility.getDaySpan(dateFormat1.parse(beginTime),
        dateFormat1.parse(endTime)) + 1;
    // 得到到之间的天数数组
    List daysList = new ArrayList();
    String days = "";
    Calendar calendar = new GregorianCalendar();
    for (int i = 0; i < daySpace; i++) {
      calendar.setTime(dateFormat1.parse(beginTime));
      calendar.add(Calendar.DATE, +i);
      Date dateTemp = calendar.getTime();
      String dateTempStr = dateFormat1.format(dateTemp);
      daysList.add(dateTempStr);
      days = days + dateTempStr + ",";
    }
    if (daySpace > 0) {
      days = days.substring(0, days.length() - 1);
    }
    return days;
  }

  /**
   * 获取考勤表对象(ATTEND_CONFIG)
   * 
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
   * 月考勤分数
   * 
   * @param conn
   * @param year
   * @param month
   * @param userId
   * @param person
   * @return
   * @throws Exception
   */
  public double getAttendScoreOld(Connection conn, String year, String month,
      String userId, YHPerson person) throws Exception {
    YHAttendConfig attend = selectConfigById(conn, String.valueOf(person
        .getDutyType()));
    String ymd = year + "-" + month + "-01";
    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(dateFormat1.parse(ymd));
    int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);// 本月份的天数
    long time = System.currentTimeMillis();
    String endTime = year + "-" + month + "-" + String.valueOf(maxDay);
    String dayStr = getDayList(conn, ymd, endTime);
    String[] days = dayStr.split(",");
    int count = 0;
    double totalScore = 0;
    for (int i = 0; i < days.length; i++) {
      if (isWeekend(days[i])) {
        count++;
        continue;
      }
      // calendar.setTime(dateFormat1.parse(days[i]));
      // calendar.add(Calendar.DATE,+i) ;
      // Date dateTemp = calendar.getTime();
      // String dateTempStr = dateFormat1.format(dateTemp);
      String beginDate = days[i] + " 00:00:00";
      String endDate = days[i] + " 23:59:59";
      double reduceScore1 = 0; // 扣分
      double reduceScore2 = 0;
      double reduceScore3 = 0;
      double reduceScore4 = 0;
      double reduceScore5 = 0;
      double reduceScore6 = 0;
      double type1 = 0;
      double type2 = 0;
      double type3 = 0;
      double type4 = 0;
      double type5 = 0;
      double type6 = 0;
      if (!YHUtility.isNullorEmpty(attend.getDutyTime1())) {
        reduceScore1 = getAttendScoreStr(conn, beginDate, endDate, userId, "1");
      }
      if (!YHUtility.isNullorEmpty(attend.getDutyTime2())) {
        reduceScore2 = getAttendScoreStr(conn, beginDate, endDate, userId, "2");
      }
      if (!YHUtility.isNullorEmpty(attend.getDutyTime3())) {
        reduceScore3 = getAttendScoreStr(conn, beginDate, endDate, userId, "3");
      }
      if (!YHUtility.isNullorEmpty(attend.getDutyTime4())) {
        reduceScore4 = getAttendScoreStr(conn, beginDate, endDate, userId, "4");
      }
      if (!YHUtility.isNullorEmpty(attend.getDutyTime5())) {
        reduceScore5 = getAttendScoreStr(conn, beginDate, endDate, userId, "5");
      }
      if (!YHUtility.isNullorEmpty(attend.getDutyTime6())) {
        reduceScore6 = getAttendScoreStr(conn, beginDate, endDate, userId, "6");
      }
      // 回冲分
      if (!YHUtility.isNullorEmpty(attend.getDutyTime1())) {
        type1 = Double.parseDouble(getAttendFillStr(conn, beginDate, endDate,
            userId, "1"));
      }
      if (!YHUtility.isNullorEmpty(attend.getDutyTime2())) {
        type2 = Double.parseDouble(getAttendFillStr(conn, beginDate, endDate,
            userId, "2"));
      }
      if (!YHUtility.isNullorEmpty(attend.getDutyTime3())) {
        type3 = Double.parseDouble(getAttendFillStr(conn, beginDate, endDate,
            userId, "3"));
      }
      if (!YHUtility.isNullorEmpty(attend.getDutyTime4())) {
        type4 = Double.parseDouble(getAttendFillStr(conn, beginDate, endDate,
            userId, "4"));
      }
      if (!YHUtility.isNullorEmpty(attend.getDutyTime5())) {
        type5 = Double.parseDouble(getAttendFillStr(conn, beginDate, endDate,
            userId, "5"));
      }
      if (!YHUtility.isNullorEmpty(attend.getDutyTime6())) {
        type6 = Double.parseDouble(getAttendFillStr(conn, beginDate, endDate,
            userId, "6"));
      }
      double num = reduceScore1 * type1 + reduceScore2 * type2 + reduceScore3
          * type3 + reduceScore4 * type4 + reduceScore5 * type5 + reduceScore6
          * type6;

      if (num > Double.parseDouble(getMaxScore(conn))) {
        num = Double.parseDouble(getMaxScore(conn));
      }
      totalScore += num;
    }
//    totalScore = (maxDay - count) * Double.parseDouble(getMaxScore(conn))
//        - totalScore;
    totalScore = 100 - totalScore;
    if(totalScore < 0){
      totalScore = 0;
    }
    long time1 = System.currentTimeMillis();
    long time2 = time1 - time;
    // System.out.println(time2);
    return totalScore;
  }
  
  public static List<YHAttendScore> selectData(Connection dbConn,String[] str) throws Exception {
    YHORM orm = new YHORM();
    List<YHAttendScore> dataList = new ArrayList<YHAttendScore>();
    dataList = orm.loadListSingle(dbConn, YHAttendScore.class, str);
    return dataList;
  }
  
  public static List<YHAttendFill> selectFill(Connection dbConn,String[] str) throws Exception {
    YHORM orm = new YHORM();
    List<YHAttendFill> dataList = new ArrayList<YHAttendFill>();
    dataList = orm.loadListSingle(dbConn, YHAttendFill.class, str);
    return dataList;
  }
  
  /**
   * 获取年考勤分
   * @param conn
   * @param year
   * @param month
   * @param userId
   * @param person
   * @return
   * @throws Exception
   */
  public double getAttendScore(Connection conn, String year, String month,
      String userId, YHPerson person) throws Exception {
    YHAttendConfig attend = selectConfigById(conn, String.valueOf(person
        .getDutyType()));
    String ymd = year + "-01-01";
    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
    int count = 0;
    double num = 0;
    double totalScore = 0;
    double reduceScore1 = 0; // 扣分
    double reduceScore2 = 0;
    double reduceScore3 = 0;
    double reduceScore4 = 0;
    double reduceScore5 = 0;
    double reduceScore6 = 0;
    double type1 = 0;
    double type2 = 0;
    double type3 = 0;
    double type4 = 0;
    double type5 = 0;
    double type6 = 0;
    String[] str = {"USER_ID=" + userId + " and "+YHDBUtility.getYearFilter("CREATE_TIME", YHUtility.parseDate(ymd)) + " order by CREATE_TIME, REGISTER_TYPE asc"};
    List<YHAttendScore> scorelist = selectData(conn, str);
    
    String[] strFill = {"PROPOSER = " + userId + " and "+YHDBUtility.getYearFilter("FILL_TIME", YHUtility.parseDate(ymd)) + " and ASSESSING_STATUS = '1' order by FILL_TIME, REGISTER_TYPE asc"};
    List<YHAttendFill> scoreFill = selectFill(conn, strFill);
    
    long time = System.currentTimeMillis();
    if(scorelist.size() != 0 || scoreFill.size() != 0){
      for (int y = 1; y <= 12; y++) {
        String moth = String.valueOf(y);
        if (moth.length() == 1) {
          month = "0" + moth;
        } else {
          month = moth;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFormat1.parse(year + "-" + month + "-01"));
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);// 本月份的天数
        String startTime = year + "-" + month + "-01";
        String endTime = year + "-" + month + "-" + String.valueOf(maxDay);
        String dayStr = getDayList(conn, startTime, endTime);
        String[] days = dayStr.split(",");
        for (int d = 0; d < days.length; d++) {
          if (isWeekend(days[d])) {
            count++;
            continue;
          }
          for (int i = 0; i < scorelist.size(); i++) {
            if (!YHUtility.isNullorEmpty(attend.getDutyTime1())) {
              if(String.valueOf(scorelist.get(i).getCreateTime()).substring(0,10).equals(days[d]) && (scorelist.get(i).getRegisterType()).equals("1")){
                reduceScore1 = scorelist.get(i).getScore();
              }else{
                
              }
            }
            if (!YHUtility.isNullorEmpty(attend.getDutyTime2())) {
              if(String.valueOf(scorelist.get(i).getCreateTime()).substring(0,10).equals(days[d]) && (scorelist.get(i).getRegisterType()).equals("2")){
                reduceScore2 = scorelist.get(i).getScore();
              }
            }
            if (!YHUtility.isNullorEmpty(attend.getDutyTime3())) {
              if(String.valueOf(scorelist.get(i).getCreateTime()).substring(0,10).equals(days[d]) && (scorelist.get(i).getRegisterType()).equals("3")){
                reduceScore3 = scorelist.get(i).getScore();
              }
            }
            if (!YHUtility.isNullorEmpty(attend.getDutyTime4())) {
              if(String.valueOf(scorelist.get(i).getCreateTime()).substring(0,10).equals(days[d]) && (scorelist.get(i).getRegisterType()).equals("4")){
                reduceScore4 = scorelist.get(i).getScore();
              }
            }
            if (!YHUtility.isNullorEmpty(attend.getDutyTime5())) {
              if(String.valueOf(scorelist.get(i).getCreateTime()).substring(0,10).equals(days[d]) && (scorelist.get(i).getRegisterType()).equals("5")){
                reduceScore5 = scorelist.get(i).getScore();
              }
            }
            if (!YHUtility.isNullorEmpty(attend.getDutyTime6())) {
              if(String.valueOf(scorelist.get(i).getCreateTime()).substring(0,10).equals(days[d]) && (scorelist.get(i).getRegisterType()).equals("6")){
                reduceScore6 = scorelist.get(i).getScore();
              }
            }
            
            if (!YHUtility.isNullorEmpty(attend.getDutyTime1())) {
              if(String.valueOf(scoreFill.get(i).getFillTime()).substring(0,10).equals(days[d]) && (scoreFill.get(i).getRegisterType()).equals("1")){
                type1 = Double.parseDouble(scoreFill.get(i).getAttendFlag());
              }else{
                type1 = 1;
              }
            }
            if (!YHUtility.isNullorEmpty(attend.getDutyTime2())) {
              if(String.valueOf(scoreFill.get(i).getFillTime()).substring(0,10).equals(days[d]) && (scoreFill.get(i).getRegisterType()).equals("2")){
                type2 = Double.parseDouble(scoreFill.get(i).getAttendFlag());
              }else{
                type2 = 1;
              }
            }
            if (!YHUtility.isNullorEmpty(attend.getDutyTime3())) {
              if(String.valueOf(scoreFill.get(i).getFillTime()).substring(0,10).equals(days[d]) && (scoreFill.get(i).getRegisterType()).equals("3")){
                type3 = Double.parseDouble(scoreFill.get(i).getAttendFlag());
              }else{
                type3 = 1;
              }
            }
            if (!YHUtility.isNullorEmpty(attend.getDutyTime4())) {
              if(String.valueOf(scoreFill.get(i).getFillTime()).substring(0,10).equals(days[d]) && (scoreFill.get(i).getRegisterType()).equals("4")){
                type4 = Double.parseDouble(scoreFill.get(i).getAttendFlag());
              }else{
                type4 = 1;
              }
            }
            if (!YHUtility.isNullorEmpty(attend.getDutyTime5())) {
              if(String.valueOf(scoreFill.get(i).getFillTime()).substring(0,10).equals(days[d]) && (scoreFill.get(i).getRegisterType()).equals("5")){
                type5 = Double.parseDouble(scoreFill.get(i).getAttendFlag());
              }else{
                type5 = 1;
              }
            }
            if (!YHUtility.isNullorEmpty(attend.getDutyTime6())) {
              if(String.valueOf(scoreFill.get(i).getFillTime()).substring(0,10).equals(days[d]) && (scoreFill.get(i).getRegisterType()).equals("6")){
                type6 = Double.parseDouble(scoreFill.get(i).getAttendFlag());
              }else{
                type6 = 1;
              }
            }
            if(String.valueOf(scorelist.get(i).getCreateTime()).substring(0,10).equals(days[d])){
              num += reduceScore1 * type1 + reduceScore2 * type2 + reduceScore3
              * type3 + reduceScore4 * type4 + reduceScore5 * type5 + reduceScore6
              * type6;
              if(num > Double.parseDouble(getMaxScore(conn))){
                num = Double.parseDouble(getMaxScore(conn));
              }
            }else{
              num += Double.parseDouble(getMaxScore(conn));
            }
            reduceScore1 = 0; // 扣分
            reduceScore2 = 0;
            reduceScore3 = 0;
            reduceScore4 = 0;
            reduceScore5 = 0;
            reduceScore6 = 0;
            type1 = 0;
            type2 = 0;
            type3 = 0;
            type4 = 0;
            type5 = 0;
            type6 = 0;
          }
          totalScore += num;
          num = 0;
        }
      }
      if(scorelist.size() == 0 && scoreFill.size() == 0){
        totalScore = 100;
      }
      totalScore = 100 - totalScore;
      if(totalScore < 0){
        totalScore = 0;
      }
    }
    long time1 = System.currentTimeMillis();
    long time2 = time1 - time;
    return totalScore;
  }
  
  /**
   * 获取每个月的考勤分数
   * @param conn
   * @param year
   * @param month
   * @param userId
   * @param person
   * @return
   * @throws Exception
   */
  public double getMonthAttendScoreLogic(Connection conn, String year, String month,
      String userId, YHPerson person) throws Exception {
    YHAttendConfig attend = selectConfigById(conn, String.valueOf(person
        .getDutyType()));
    String ymd = year + "-" + month + "-01";
    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
    long time = System.currentTimeMillis();
    int count = 0;
    double num = 0;
    double totalScore = 0;
    double reduceScore1 = 0; // 扣分
    double reduceScore2 = 0;
    double reduceScore3 = 0;
    double reduceScore4 = 0;
    double reduceScore5 = 0;
    double reduceScore6 = 0;
    double type1 = 0;
    double type2 = 0;
    double type3 = 0;
    double type4 = 0;
    double type5 = 0;
    double type6 = 0;
    String[] str = {"USER_ID=" + userId + " and "+YHDBUtility.getMonthFilter("CREATE_TIME", YHUtility.parseDate(ymd)) + " order by CREATE_TIME, REGISTER_TYPE asc"};
    List<YHAttendScore> scorelist = selectData(conn, str);
    
    String[] strFill = {"PROPOSER = " + userId + " and "+YHDBUtility.getMonthFilter("FILL_TIME", YHUtility.parseDate(ymd)) + " and ASSESSING_STATUS = '1' order by FILL_TIME, REGISTER_TYPE asc"};
    List<YHAttendFill> scoreFill = selectFill(conn, strFill);
    
    if(scorelist.size() == 0 && scoreFill.size() == 0){
      
    }else{
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(dateFormat1.parse(year + "-" + month + "-01"));
      int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);// 本月份的天数
      String startTime = year + "-" + month + "-01";
      String endTime = year + "-" + month + "-" + String.valueOf(maxDay);
      String dayStr = getDayList(conn, startTime, endTime);
      String[] days = dayStr.split(",");
      for (int d = 0; d < days.length; d++) {
        if (isWeekend(days[d])) {
          count++;
          continue;
        }
        for (int i = 0; i < scorelist.size(); i++) {
          if (!YHUtility.isNullorEmpty(attend.getDutyTime1())) {
            if(String.valueOf(scorelist.get(i).getCreateTime()).substring(0,10).equals(days[d]) && (scorelist.get(i).getRegisterType()).equals("1")){
              reduceScore1 = scorelist.get(i).getScore();
            }
          }
          if (!YHUtility.isNullorEmpty(attend.getDutyTime2())) {
            if(String.valueOf(scorelist.get(i).getCreateTime()).substring(0,10).equals(days[d]) && (scorelist.get(i).getRegisterType()).equals("2")){
              reduceScore2 = scorelist.get(i).getScore();
            }
          }
          if (!YHUtility.isNullorEmpty(attend.getDutyTime3())) {
            if(String.valueOf(scorelist.get(i).getCreateTime()).substring(0,10).equals(days[d]) && (scorelist.get(i).getRegisterType()).equals("3")){
              reduceScore3 = scorelist.get(i).getScore();
            }
          }
          if (!YHUtility.isNullorEmpty(attend.getDutyTime4())) {
            if(String.valueOf(scorelist.get(i).getCreateTime()).substring(0,10).equals(days[d]) && (scorelist.get(i).getRegisterType()).equals("4")){
              reduceScore4 = scorelist.get(i).getScore();
            }
          }
          if (!YHUtility.isNullorEmpty(attend.getDutyTime5())) {
            if(String.valueOf(scorelist.get(i).getCreateTime()).substring(0,10).equals(days[d]) && (scorelist.get(i).getRegisterType()).equals("5")){
              reduceScore5 = scorelist.get(i).getScore();
            }
          }
          if (!YHUtility.isNullorEmpty(attend.getDutyTime6())) {
            if(String.valueOf(scorelist.get(i).getCreateTime()).substring(0,10).equals(days[d]) && (scorelist.get(i).getRegisterType()).equals("6")){
              reduceScore6 = scorelist.get(i).getScore();
            }
          }
          
          if (!YHUtility.isNullorEmpty(attend.getDutyTime1())) {
            if(String.valueOf(scoreFill.get(i).getFillTime()).substring(0,10).equals(days[d]) && (scoreFill.get(i).getRegisterType()).equals("1")){
              type1 = Double.parseDouble(scoreFill.get(i).getAttendFlag());
            }else{
              type1 = 1;
            }
          }
          if (!YHUtility.isNullorEmpty(attend.getDutyTime2())) {
            if(String.valueOf(scoreFill.get(i).getFillTime()).substring(0,10).equals(days[d]) && (scoreFill.get(i).getRegisterType()).equals("2")){
              type2 = Double.parseDouble(scoreFill.get(i).getAttendFlag());
            }else{
              type2 = 1;
            }
          }
          if (!YHUtility.isNullorEmpty(attend.getDutyTime3())) {
            if(String.valueOf(scoreFill.get(i).getFillTime()).substring(0,10).equals(days[d]) && (scoreFill.get(i).getRegisterType()).equals("3")){
              type3 = Double.parseDouble(scoreFill.get(i).getAttendFlag());
            }else{
              type3 = 1;
            }
          }
          if (!YHUtility.isNullorEmpty(attend.getDutyTime4())) {
            if(String.valueOf(scoreFill.get(i).getFillTime()).substring(0,10).equals(days[d]) && (scoreFill.get(i).getRegisterType()).equals("4")){
              type4 = Double.parseDouble(scoreFill.get(i).getAttendFlag());
            }else{
              type4 = 1;
            }
          }
          if (!YHUtility.isNullorEmpty(attend.getDutyTime5())) {
            if(String.valueOf(scoreFill.get(i).getFillTime()).substring(0,10).equals(days[d]) && (scoreFill.get(i).getRegisterType()).equals("5")){
              type5 = Double.parseDouble(scoreFill.get(i).getAttendFlag());
            }else{
              type5 = 1;
            }
          }
          if (!YHUtility.isNullorEmpty(attend.getDutyTime6())) {
            if(String.valueOf(scoreFill.get(i).getFillTime()).substring(0,10).equals(days[d]) && (scoreFill.get(i).getRegisterType()).equals("6")){
              type6 = Double.parseDouble(scoreFill.get(i).getAttendFlag());
            }else{
              type6 = 1;
            }
          }
          if(String.valueOf(scorelist.get(i).getCreateTime()).substring(0,10).equals(days[d])){
            num += reduceScore1 * type1 + reduceScore2 * type2 + reduceScore3
            * type3 + reduceScore4 * type4 + reduceScore5 * type5 + reduceScore6
            * type6;
            if(num > Double.parseDouble(getMaxScore(conn))){
              num = Double.parseDouble(getMaxScore(conn));
            }
          }else{
            num = Double.parseDouble(getMaxScore(conn));
          }
          reduceScore1 = 0; // 扣分
          reduceScore2 = 0;
          reduceScore3 = 0;
          reduceScore4 = 0;
          reduceScore5 = 0;
          reduceScore6 = 0;
          type1 = 0;
          type2 = 0;
          type3 = 0;
          type4 = 0;
          type5 = 0;
          type6 = 0;
        }
        totalScore += num;
        num = 0;
      }
    }
      if(scorelist.size() == 0 && scoreFill.size() == 0){
        totalScore = 100;
      }
    totalScore = 100 - totalScore;
    if(totalScore < 0){
      totalScore = 0;
    }
    long time1 = System.currentTimeMillis();
    long time2 = time1 - time;
    //System.out.println(time2);
    return totalScore;
  }

  /**
   * 月考核分
   * 
   * @param conn
   * @param year
   * @param month
   * @param attendScore
   * @return
   * @throws Exception
   */
  public double getMonthTotalScore(Connection conn, String year, String month,
      double reduceAttendScore, double staffScore, double directorScore)
      throws Exception {
    YHCoefficientLogic cof = new YHCoefficientLogic();
    double checkCof = cof.getCheckScoreLogic(conn); // 月考勤系数
    double chiefCof = cof.getChiefScoreLogic(conn); // 处长主关分系数    double awardCof = cof.getAwardScoreLogic(conn); // 奖惩分系数//    String ymd = year + "-" + month + "-" + "01";
//    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
//    Calendar calendar = Calendar.getInstance();
//    calendar.setTime(dateFormat1.parse(ymd));
//    int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);// 本月份的天数
//    int maxScore = Integer.parseInt(getMaxScore(conn));
    double attendScore = reduceAttendScore;
    double totalScore = attendScore * checkCof + staffScore * awardCof
        + directorScore * chiefCof;

    return totalScore;
  }

  /**
   * 获取年考核分数
   * 
   * @param conn
   * @param year
   * @param userId
   * @param person
   * @param checkFlag
   * @return
   * @throws Exception
   */
  public double getYearTotalScore(Connection conn, String year, String userId,
      YHPerson person) throws Exception {
    YHCoefficientLogic cof = new YHCoefficientLogic();
    String ymd = "";
    String month = "";
    double totalScore = 0;
//    double checkCof = cof.getCheckScoreLogic(conn); // 月考勤系数
//    double chiefCof = cof.getChiefScoreLogic(conn); // 处长主关分系数//    double awardCof = cof.getAwardScoreLogic(conn); // 奖惩分系数//    double yearCof = cof.getYearScoreLogic(conn); // 年终考核分系数    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar = Calendar.getInstance();
    for (int i = 1; i <= 12; i++) {
      String moth = String.valueOf(i);
      if (moth.length() == 1) {
        month = "0" + moth;
      } else {
        month = moth;
      }
      ymd = year + "-" + month + "-" + "01";
      calendar.setTime(dateFormat1.parse(ymd));
      double attendScore = 0;//getAttendScore(conn, year, month, userId, person); // 月考勤分数
      double directorScore = getDirectorScore(conn, year, month, userId); // 月处长考核分数
      double staffScore = getMonthScoreLogic(conn, year, month, userId); // 月奖惩分
      totalScore += getMonthTotalScore(conn, year, month, attendScore,
          staffScore, directorScore);
    }
    return totalScore;
  }

  /**
   * 获取扣的分数
   * 
   * @param conn
   * @param year
   * @param userId
   * @return
   * @throws Exception
   */
  public double getAttendScoreStr(Connection conn, String beginDate,
      String endDate, String userId, String registerType) throws Exception {
    double result = 0;
    String sql = " select SCORE from oa_attendance_integral where USER_ID = '" + userId
        + "' and " + YHDBUtility.getDateFilter("CREATE_TIME", beginDate, ">=")
        + " and " + YHDBUtility.getDateFilter("CREATE_TIME", endDate, "<=")
        + " and REGISTER_TYPE = '" + registerType + "'";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        String toId = rs.getString(1);
        if (!YHUtility.isNullorEmpty(toId)) {
          result += Double.parseDouble(toId);
          if (result > Double.parseDouble(getMaxScore(conn))) {
            result = Double.parseDouble(getMaxScore(conn));
          }
        } else {
          result += Double.parseDouble(getMaxScore(conn));
        }
      } else {
        result += Double.parseDouble(getMaxScore(conn));
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }

  /**
   * 获取补登记状态
   * 
   * @param conn
   * @param year
   * @param userId
   * @return
   * @throws Exception
   */
  public String getAttendFillStr(Connection conn, String beginDate,
      String endDate, String userId, String registerType) throws Exception {
    String result = "0";
    String sql = " select ATTEND_FLAG from oa_attendance_add where PROPOSER ='"
        + userId + "' and "
        + YHDBUtility.getDateFilter("FILL_TIME", beginDate, ">=") + " and "
        + YHDBUtility.getDateFilter("FILL_TIME", endDate, "<=")
        + " and REGISTER_TYPE = '" + registerType
        + "' and ASSESSING_STATUS = '1'";
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
      } else {
        result = "1";
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }

  /**
   * 获取最大扣除的分数
   * 
   * @param conn
   * @return
   * @throws Exception
   */
  public String getMaxScore(Connection conn) throws Exception {
    String result = "0";
    String sql = " select MAX(SCORE) from oa_attendance_time";
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
   * 月处长考核分数
   * @param conn
   * @param year
   * @param month
   * @param userId
   * @return
   * @throws Exception
   */
  public double getDirectorScore(Connection conn, String year, String month,
      String userId) throws Exception {
    double result = 0;
    String data = getDirectorScoreStr(conn, year, month, userId);
    try {
      String dataStr[] = data.split(",");
      for (int i = 0; i < dataStr.length; i++) {
        if (!YHUtility.isNullorEmpty(dataStr[i])) {
          double val = Double.parseDouble(dataStr[i]);
          result = result + val;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      // YHDBUtility.close(ps, rs, log);
    }
    return result;
  }

  /**
   * 年终处长考核分数
   * 
   * @param conn
   * @param year
   * @param month
   * @param userId
   * @return
   * @throws Exception
   */
  public double getDirectorYearScore(Connection conn, String year,
      String month, String userId) throws Exception {
    double result = 0;
    String data = getDirectorScoreYearStr(conn, year, userId);
    try {
      String dataStr[] = data.split(",");
      for (int i = 0; i < dataStr.length; i++) {
        if (!YHUtility.isNullorEmpty(dataStr[i])) {
          double val = Double.parseDouble(dataStr[i]);
          result = result + val;
        }
      }
    } catch (Exception e) {
      throw e;
    } finally {
      // YHDBUtility.close(ps, rs, log);
    }
    return result;
  }

  /**
   * 处长主观分－ 月
   * 
   * @param conn
   * @param year
   * @param month
   * @param userId
   * @return
   * @throws Exception
   */
  public String getDirectorScoreStr(Connection conn, String year, String month,
      String userId) throws Exception {
    String result = "";
    String ymd = year + "-" + month + "-07";
    
    String sql = " select SCORE from oa_score_show where CHECK_FLAG = '0' and PARTICIPANT='" + userId + "' and "
        + YHDBUtility.getMonthFilter("RANK_DATE", YHUtility.parseDate(ymd));
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
   * 处长主观分－ 年
   * 
   * @param conn
   * @param year
   * @param month
   * @param userId
   * @return
   * @throws Exception
   */
  public String getDirectorScoreYearStr(Connection conn, String year,
      String userId) throws Exception {
    String result = "";
    String ymd = year + "-10-07";
    
    String sql = " select SCORE from oa_score_data where CHECK_FLAG = '1' and PARTICIPANT='" + userId + "' and "
        + YHDBUtility.getYearFilter("RANK_DATE", YHUtility.parseDate(ymd));
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
   *获取不考核月份信息
   * 
   * @param dbConn
   * @param date
   * @return
   * @throws Exception
   */
  public double getNoCheckInfo(Connection dbConn, String date) throws Exception {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    int dbCount = 0;
    double count = 0;
    date = date + "-01" + "-01";
    try {
      String sql = "select count(*) from oa_attendance_donot where "
          + YHDBUtility.getYearFilter("NO_CHECK_DATE", YHUtility
              .parseDate(date));
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      if (rs.next()) {
        dbCount = rs.getInt(1);
      }
      count = 12 - dbCount;
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return count;
  }

  /**
   * 年奖惩分
   * 
   * @param dbConn
   * @param year
   * @param month
   * @param userIdStr
   * @return
   * @throws Exception
   */
  public double getStaffYearScoreLogic(Connection dbConn, String year,
      String userIdStr) throws Exception {
    double addScore = 0;
    double reduceScore = 0;
    double score = 0;
    double yearScore = 0;
    if (YHUtility.isNullorEmpty(userIdStr)) {
      userIdStr = "0";
    }
    if (userIdStr.endsWith(",")) {
      userIdStr = userIdStr.substring(0, userIdStr.length() - 1);
    }
    String ymd = year + "-10-07";
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      String sql = "select ADD_SCORE,REDUCE_SCORE,YEAR_SCORE from oa_pm_employee_encouragement where "
          + YHDBUtility.findInSet(userIdStr, "STAFF_NAME")
          + " and "
          + YHDBUtility.getYearFilter("INCENTIVE_TIME", YHUtility
              .parseDate(ymd));
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        String yearScoreStr = rs.getString("YEAR_SCORE");
        if (YHUtility.isNullorEmpty(yearScoreStr) || "0".equals(yearScoreStr)) {
          yearScore = 0;
        } else {
          yearScore = rs.getDouble("YEAR_SCORE");
        }
        addScore += rs.getDouble("ADD_SCORE") * yearScore;
        reduceScore += rs.getDouble("REDUCE_SCORE") * yearScore;
      }
      score = addScore - reduceScore;
      return score;
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }

  /**
   * 月奖惩分
   * 
   * @param dbConn
   * @param year
   * @param month
   * @param userIdStr
   * @return
   * @throws Exception
   */
  public double getMonthScoreLogic(Connection dbConn, String year,
      String month, String userIdStr) throws Exception {
    if (YHUtility.isNullorEmpty(userIdStr)) {
      userIdStr = "0";
    }
    if (userIdStr.endsWith(",")) {
      userIdStr = userIdStr.substring(0, userIdStr.length() - 1);
    }
    String ymd = year + "-" + month + "-07";
    double addScore = 0;
    double reduceScore = 0;
    double score = 0;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      String sql = "select ADD_SCORE,REDUCE_SCORE,YEAR_SCORE from oa_pm_employee_encouragement where "
          + YHDBUtility.findInSet(userIdStr, "STAFF_NAME")
          + " and "
          + YHDBUtility.getMonthFilter("INCENTIVE_TIME", YHUtility
              .parseDate(ymd));
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        String yearScoreStr = rs.getString("YEAR_SCORE");
//        if(!"0".equals(yearScoreStr)){
//          continue;
//        }
        addScore += rs.getDouble("ADD_SCORE");
        reduceScore += rs.getDouble("REDUCE_SCORE");
      }
      score = addScore - reduceScore;
      return score;
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  /**
   * 取得用户名称
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */
  
  public String getUserPrivLogic(Connection conn , int userId) throws Exception{
    String result = "";
    String sql = " select USER_PRIV from PERSON where SEQ_ID = " + userId ;
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        String toId = rs.getString(1);
        if(toId != null){
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

}
