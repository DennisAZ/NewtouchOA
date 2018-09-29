package yh.core.funcs.attendance.personal.logic;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.attendance.personal.data.YHAttendDuty;
import yh.core.funcs.system.attendance.data.YHAttendConfig;
import yh.core.funcs.system.attendance.data.YHAttendHoliday;
import yh.core.funcs.system.attendance.logic.YHSysParaLogic;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHAttendDutyLogic {
  private static Logger log = Logger
      .getLogger("yh.core.act.action.YHSysMenuLog");

  public void addDuty(Connection dbConn, YHAttendDuty duty) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, duty);
  }

  public List<YHAttendHoliday> selectHoliday(Connection dbConn, Map map)
      throws Exception {
    List<YHAttendHoliday> holidayList = new ArrayList<YHAttendHoliday>();
    YHORM orm = new YHORM();
    holidayList = orm.loadListSingle(dbConn, YHAttendHoliday.class, map);
    return holidayList;
  }

  public void deleteDutyById(Connection dbConn, YHAttendDuty duty)
      throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, duty);
  }

  public List<YHAttendHoliday> selectHoliday(Connection dbConn, String[] str)
      throws Exception {
    List<YHAttendHoliday> holidayList = new ArrayList<YHAttendHoliday>();
    YHORM orm = new YHORM();
    holidayList = orm.loadListSingle(dbConn, YHAttendHoliday.class, str);
    return holidayList;
  }

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

  // 得到参数
  public String getParaValue(Connection dbConn, String paraName)
      throws Exception {
    YHSysParaLogic yhpl = new YHSysParaLogic();
    String paraValue = "";
    paraValue = yhpl.selectPara(dbConn, paraName);
    return paraValue;
  }

  public List<YHAttendDuty> selectDuty(Connection dbConn, String userId,
      String dateValue1, String dateValue2, String registerType)
      throws Exception {
    List<YHAttendDuty> dutyList = new ArrayList<YHAttendDuty>();
    Statement stmt = null;
    ResultSet rs = null;
    String newUserIds = "";
    if (!userId.trim().equals("")) {
      String[] userIdArray = userId.split(",");
      for (int i = 0; i < userIdArray.length; i++) {
        newUserIds = newUserIds + "'" + userIdArray[i] + "',";
      }
      if (userIdArray.length > 0) {
        newUserIds = newUserIds.substring(0, newUserIds.length() - 1);
      }
    }
    if (!userId.equals("")) {
      String sql = "select * from oa_attendance_duty  where USER_ID in (" + newUserIds
          + ") and " + dateValue1;
      if (!dateValue2.equals("")) {
        sql = sql + " and " + dateValue2;
      }
      if (!registerType.equals("")) {
        sql = sql + " and REGISTER_TYPE =" + registerType;
      }
      sql = sql + " order by REGISTER_TIME";
      //System.out.println(sql);
      try {
        stmt = dbConn.createStatement();
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
          YHAttendDuty duty = new YHAttendDuty();
          duty.setSeqId(rs.getInt("SEQ_ID"));
          duty.setUserId(rs.getString("USER_ID"));
          duty.setRegisterIp(rs.getString("REGISTER_IP"));
          duty.setRegisterType(rs.getString("REGISTER_TYPE"));
          duty.setRegisterTime(YHUtility.parseDate("yyyy-MM-dd HH:mm:ss.s", rs.getString("REGISTER_TIME")));
          duty.setRemark(rs.getString("REMARK"));
          dutyList.add(duty);
        }
      } catch (Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stmt, rs, log);
      }
    }
    return dutyList;
  }

  public YHAttendDuty selectDutyById(Connection dbConn, String seqIds)
      throws Exception {
    YHORM orm = new YHORM();
    YHAttendDuty duty = new YHAttendDuty();
    int seqId = 0;
    if (seqIds != null) {
      seqId = Integer.parseInt(seqIds);
      duty = (YHAttendDuty) orm
          .loadObjSingle(dbConn, YHAttendDuty.class, seqId);
    }
    return duty;
  }

  public void updateRemarkById(Connection dbConn, String seqId, String remark)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    remark = remark.replace("'", "''");
    String sql = "update oa_attendance_duty set REMARK = '" + remark
        + "' where SEQ_ID = " + seqId;
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }

  public void updateDutyById(Connection dbConn, YHAttendDuty duty)
      throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, duty);
  }

  public List<Map<String, String>> selectDutyByDate(Connection dbConn,
      String userId, String beginDate, String endDate, String seqId)
      throws Exception {
    List<Map<String, String>> dutyList = new ArrayList<Map<String, String>>();
    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String dutyIntervalBefore1 = getParaValue(dbConn, "DUTY_INTERVAL_BEFORE1");
    String dutyIntervalAfter1 = getParaValue(dbConn, "DUTY_INTERVAL_AFTER1");
    String dutyIntervalBefore2 = getParaValue(dbConn, "DUTY_INTERVAL_BEFORE2");
    String dutyIntervalAfter2 = getParaValue(dbConn, "DUTY_INTERVAL_AFTER2");
    long before1 = 0;
    long after1 = 0;
    long before2 = 0;
    long after2 = 0;
    if (dutyIntervalBefore1!=null&&!dutyIntervalBefore1.equals("")) {
      //System.out.println(dutyIntervalBefore1.length());
      before1 = Long.parseLong(dutyIntervalBefore1) * 60;
    }
    if (dutyIntervalAfter1!=null&&!dutyIntervalAfter1.equals("")) {
      after1 = Long.parseLong(dutyIntervalAfter1) * 60;
    }
    if (dutyIntervalBefore2!=null&&!dutyIntervalBefore2.equals("")) {
      before2 = Long.parseLong(dutyIntervalBefore2) * 60;
    }
    if (dutyIntervalAfter2!=null&&!dutyIntervalAfter2.equals("")) {
      after2 = Long.parseLong(dutyIntervalAfter2) * 60;
    }
    //System.out.println(before1 + ":" + after1 + ":" + before2 + ":" + after2);
    long temp = 0;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "select * from oa_attendance_duty where USER_ID = '" + userId
        + "' and " + beginDate + " and " + endDate + " order by REGISTER_TIME";
    //System.out.println(sql);
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String c_zStatus = "0";
        Map<String, String> map = new HashMap<String, String>();
        map.put("seqId", rs.getString("SEQ_ID"));
        map.put("userId", rs.getString("USER_ID"));
        map.put("registerType", rs.getString("REGISTER_TYPE"));
        map.put("remark", rs.getString("REMARK"));
        if (IsGeneral(dbConn, rs.getString("REGISTER_TIME"), seqId)) {
          map.put("generalStatus", "1");// 为公休日
        } else {
          map.put("generalStatus", "2");
        }
        //System.out.println(rs.getString("REGISTER_TIME"));
        //System.out.println(rs.getString("REMARK") + rs.getString("SEQ_ID"));
        String registerTime = dateFormat.format(dateFormat1.parse(rs
            .getString("REGISTER_TIME")));
        //System.out.println(registerTime);
        temp = getLongByDutyTime(registerTime);
        //System.out.println(temp);
        // 得到上下班和登记时间点
        String typeTime = selectConfigByIdDutyTypeTime(dbConn, rs
            .getString("REGISTER_TYPE"), seqId);
        String tt[] = typeTime.split(",");
        long dutyTime = getLongByDutyTime(tt[1]);
        if (tt[0].equals("1")) {
          if (temp - dutyTime > 0) {
            c_zStatus = "1";
          }
        } else {
          //System.out.println(tt[0]
//              + "---------------------------------------------------->");

          //System.out.println(dutyTime + ":" + temp);
          if (dutyTime - temp > 0) {
            c_zStatus = "2";
          }
        }
        map.put("c_zStatus", c_zStatus);
        SimpleDateFormat formatter1 = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatter2 = new SimpleDateFormat("E");
        //System.out.println(rs.getString("REGISTER_TIME"));
        String mydate = formatter2.format(formatter1.parse(rs
            .getString("REGISTER_TIME")));
        //System.out.println(mydate);
        map.put("registerTime", rs.getString("REGISTER_TIME") + "(" + mydate
            + ")");
        dutyList.add(map);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return dutyList;
  }

  // 是否为公休日
  public boolean IsGeneral(Connection dbConn, String date, String seqId)
      throws Exception {
    String general = selectConfigByIdGeneral(dbConn, seqId);
    String arrayGeneral[] = general.split(",");
    //System.out.println(arrayGeneral.length);
    if (general!=null&&general.equals("")) {
      return false;
    }
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    GregorianCalendar d = new GregorianCalendar();
    Date mydate = formatter.parse(date);
    d.setTime(mydate);
    int today = d.get(Calendar.DAY_OF_WEEK);
    if (today == 1) {
      today = 7;
    } else {
      today = today - 1;
    }
    for (int i = 0; i < arrayGeneral.length; i++) {
      //System.out.println(arrayGeneral[i]);
      if ((today) == Integer.parseInt(arrayGeneral[i])) {
        return true;
      }
    }
    //System.out.println(today);
    return false;
  }

  // 排班公休日类型
  public String selectConfigByIdGeneral(Connection dbConn, String seqIds)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String general = "";
    String sql = "select GENERAL from oa_attendance_conf where SEQ_ID = " + seqIds;
    //System.out.println(sql);
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        if (rs.getString("GENERAL") != null
            && !rs.getString("GENERAL").equals("")) {
          general = rs.getString("GENERAL");
        }
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return general;
  }

  // 得到排班的类型(上下班,上班时间)
  public String selectConfigByIdDutyTypeTime(Connection dbConn,
      String registerType, String seqIds) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String dutyType = "";
    String dutyTime = "";
    String sql = "select DUTY_TYPE" + registerType + ",DUTY_TIME"
        + registerType + " from oa_attendance_conf where SEQ_ID = " + seqIds;
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        dutyType = rs.getString("DUTY_TYPE" + registerType);
        dutyTime = rs.getString("DUTY_TIME" + registerType);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return dutyType + "," + dutyTime;
  }

  public List<Map<String, String>> selectDutyByDate(
      List<Map<String, String>> dutyList) throws Exception {
    String type = "1";
    List<Map<String, String>> dayDutyList = new ArrayList<Map<String, String>>();

    for (int i = 0; i < dutyList.size(); i++) {
      Map<String, String> tempMap1 = dutyList.get(i);
      if (dayDutyList.size() > 0) {
        for (int j = 0; j < dayDutyList.size(); j++) {
          Map<String, String> tempMap2 = dayDutyList.get(j);
          //System.out.println(tempMap2.toString());
          //System.out.println(tempMap2
//              .get("registerTime" + tempMap2.get("type")).substring(0, 10));
          //System.out.println(tempMap1.get("registerTime").substring(0, 10));
          if (tempMap1.get("registerTime").substring(0, 10).equals(
              tempMap2.get("registerTime" + tempMap2.get("type")).substring(0,
                  10))) {
            tempMap2.put("seqId" + tempMap1.get("registerType"), tempMap1
                .get("seqId"));
            tempMap2.put("userId" + tempMap1.get("registerType"), tempMap1
                .get("userId"));
            tempMap2.put("registerTime" + tempMap1.get("registerType"),
                tempMap1.get("registerTime"));
            tempMap2.put("registerType" + tempMap1.get("registerType"),
                tempMap1.get("registerType"));
            tempMap2.put("remark" + tempMap1.get("registerType"), tempMap1
                .get("remark"));
            tempMap2.put("c_zStatus" + tempMap1.get("registerType"), tempMap1
                .get("c_zStatus"));
            break;
          } else {
            if (j == dayDutyList.size() - 1) {
              Map<String, String> map = new HashMap<String, String>();
              map.put("seqId" + tempMap1.get("seqId"), tempMap1.get("seqId"));
              map
                  .put("userId" + tempMap1.get("userId"), tempMap1
                      .get("userId"));
              map.put("registerTime" + tempMap1.get("registerType"), tempMap1
                  .get("registerTime"));
              map.put("registerType" + tempMap1.get("registerType"), tempMap1
                  .get("registerType"));
              map.put("type", tempMap1.get("registerType"));
              map.put("remark" + tempMap1.get("registerType"), tempMap1
                  .get("remark"));
              map.put("generalStatus", tempMap1.get("generalStatus"));
              map.put("today", tempMap1.get("registerTime").substring(0, 10)
                  + tempMap1.get("registerTime").substring(
                      tempMap1.get("registerTime").indexOf("("),
                      tempMap1.get("registerTime").length()));
              map.put("c_zStatus" + tempMap1.get("registerType"), tempMap1
                  .get("c_zStatus"));
              dayDutyList.add(map);
              break;
            }
          }
        }
      } else {
        Map map = new HashMap();
        type = dutyList.get(i).get("registerType");
        map.put("seqId" + type, dutyList.get(i).get("seqId"));
        map.put("userId" + type, dutyList.get(i).get("userId"));
        map.put("registerTime" + type, dutyList.get(i).get("registerTime"));
        map.put("registerType" + type, type);
        map.put("remark" + type, dutyList.get(i).get("remark"));
        map.put("generalStatus", dutyList.get(i).get("generalStatus"));
        map.put("c_zStatus" + type, dutyList.get(i).get("c_zStatus"));
        map.put("today", dutyList.get(i).get("registerTime").substring(0, 10)
            + dutyList.get(i).get("registerTime").substring(
                dutyList.get(i).get("registerTime").indexOf("("),
                dutyList.get(i).get("registerTime").length()));
        map.put("type", type);
        dayDutyList.add(map);
      }
    }
    return dayDutyList;
  }

  public long getLongByDutyTime(String dutyTime) {
    long time = 0;
    String times[] = dutyTime.split(":");
    int length = times.length;
    for (int i = 0; i < times.length; i++) {
      time = time + Long.parseLong(times[i])
          * (long) (Math.pow(60, length - 1 - i));
    }
    return time;
  }
  
  /**
   * 值班次数
   * @param dbConn
   * @param year
   * @param month
   * @param userId
   * @return
   * @throws Exception
   */
  public int getAttendDutyCountLogic(Connection dbConn, String year, String month, String userId) throws Exception {
    int result = 0;
    String sql = "";
    String ymd = year + "-" + month + "-" + "01";
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      if(!YHUtility.isNullorEmpty(ymd)){
        sql = "select count(*) from oa_duty where USER_ID = '" + userId + "' and STATUS = '1' and "
        + YHDBUtility.getMonthFilter("DUTY_TIME", YHUtility.parseDate(ymd));
      }
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      if (rs.next()) {
        result = rs.getInt(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return result;
  }
  
  public double getAttendDutyMoneyLogic(Connection dbConn, String year, String month, String userId) throws Exception {
    double result = 0;
    String sql = "";
    String ymd = year + "-" + month + "-" + "01";
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      if(!YHUtility.isNullorEmpty(ymd)){
        sql = "select DUTY_MONEY from oa_duty where USER_ID = '" + userId + "' and STATUS = '1' and "
        + YHDBUtility.getMonthFilter("DUTY_TIME", YHUtility.parseDate(ymd));
      }
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        double dutyMoney = rs.getDouble(1);
        result += dutyMoney;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return result;
  }

  public static void main(String[] args) throws ParseException {

  }
}
