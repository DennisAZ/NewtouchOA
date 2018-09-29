package yh.custom.attendance.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.custom.attendance.data.YHOvertimeRecord;

public class YHOvertimeRecordLogic {
  private static Logger log = Logger
  .getLogger("ljf.yh.core.act.action.YHSysMenuLog");

  public void addOvertime(Connection dbConn, YHOvertimeRecord overtime) throws Exception {
    YHORM orm = new YHORM();
    orm.saveSingle(dbConn, overtime);
  }
  public void updateOvertimeById(Connection dbConn, YHOvertimeRecord overtime) throws Exception {
    YHORM orm = new YHORM();
    orm.updateSingle(dbConn, overtime);
  }
  public void delOvertimeById(Connection dbConn, String seqId) throws Exception {
    YHORM orm = new YHORM();
    orm.deleteSingle(dbConn, YHOvertimeRecord.class, Integer.parseInt(seqId));
  }
  public YHOvertimeRecord selectOvertimeById(Connection dbConn, String seqId) throws Exception {
    YHORM orm = new YHORM();
    YHOvertimeRecord overtime = (YHOvertimeRecord) orm.loadObjSingle(dbConn, YHOvertimeRecord.class, Integer.parseInt(seqId));
    return overtime;
  }
  public List<YHOvertimeRecord> selectOvertime(Connection dbConn, String[] str) throws Exception {
    YHORM orm = new YHORM();
    List<YHOvertimeRecord> overtimeList = new ArrayList<YHOvertimeRecord>();
    overtimeList = orm.loadListSingle(dbConn, YHOvertimeRecord.class, str);
    return overtimeList;
  }
  
  public void updateOvertimeById(Connection dbConn,String seqId,String status,String reason)throws Exception{ 
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "update  oa_timeout_record set STATUS = '"  + status + "'";
    if(!YHUtility.isNullorEmpty(reason)){
      sql = sql + " , REASON = '" + reason.replace("'", "''")  + "'";
    }
    sql = sql  + " where SEQ_ID = " + seqId;
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    }catch(Exception ex) {
      throw ex;
    }finally {
     YHDBUtility.close(stmt, rs, log);
    }
  }
  
  /**
   * 平时加班总时长
   * @param dbConn
   * @param beginDate
   * @param endDate
   * @param userId
   * @param status
   * @return
   * @throws Exception
   */
  public double getNormalAddLogic(Connection dbConn, String beginDate, String endDate, String userId, String status, String curDateStr, String year, String month) throws Exception {
    double result = 0;
    double score = 0;
    String sql = "";
    String whereStr = "";
    String ymd = year + "-" + month +"-01";
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      if(!YHUtility.isNullorEmpty(beginDate)){ 
        whereStr += " and "+ YHDBUtility.getDateFilter("BEGIN_TIME", beginDate, ">=");
      } 
      if(!YHUtility.isNullorEmpty(endDate)){ 
       whereStr += " and "+ YHDBUtility.getDateFilter("BEGIN_TIME", endDate, "<=");
      }
      
      if(YHUtility.isNullorEmpty(beginDate) && YHUtility.isNullorEmpty(endDate)){
        sql = "select HOUR from oa_timeout_record where USER_ID = '" + userId + "' and OVERTIME_TYPE = '" + status + "'and STATUS = '1' and "
        + YHDBUtility.getMonthFilter("BEGIN_TIME", YHUtility.parseDate(ymd));
      }else{
        sql = "select HOUR from oa_timeout_record where USER_ID = '" + userId + "' and OVERTIME_TYPE = '" + status + "'" + whereStr + "";
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
   * 周末加班总时长
   * @param dbConn
   * @param beginDate
   * @param endDate
   * @param userId
   * @param status
   * @return
   * @throws Exception
   */
  public double getWeekAddLogic(Connection dbConn, String beginDate, String endDate, String userId, String status, String curDateStr, String year, String month) throws Exception {
    double result = 0;
    String sql = "";
    String whereStr = "";
    String ymd = year + "-" + month +"-01";
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      if(!YHUtility.isNullorEmpty(beginDate)){ 
        whereStr += " and "+ YHDBUtility.getDateFilter("BEGIN_TIME", beginDate, ">=");
      } 
      if(!YHUtility.isNullorEmpty(endDate)){ 
       whereStr += " and "+ YHDBUtility.getDateFilter("BEGIN_TIME", endDate, "<=");
      }
      
      if(YHUtility.isNullorEmpty(beginDate) && YHUtility.isNullorEmpty(endDate)){
        sql = "select HOUR from oa_timeout_record where USER_ID = '" + userId + "' and OVERTIME_TYPE = '" + status + "' and "
        + YHDBUtility.getMonthFilter("BEGIN_TIME", YHUtility.parseDate(ymd));
      }else{
        sql = "select HOUR from oa_timeout_record where USER_ID = '" + userId + "' and OVERTIME_TYPE = '" + status + "'" + whereStr + "";
      }
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        double normalAdd = rs.getDouble(1);
        result += normalAdd;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return result;
  }
  
  /**
   * 节假日加班总时长
   * @param dbConn
   * @param beginDate
   * @param endDate
   * @param userId
   * @param status
   * @return
   * @throws Exception
   */
  public double getFestivalAddLogic(Connection dbConn, String beginDate, String endDate, String userId, String status, String curDateStr, String year, String month) throws Exception {
    double result = 0;
    String sql = "";
    String whereStr = "";
    String ymd = year + "-" + month +"-01";
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      if(!YHUtility.isNullorEmpty(beginDate)){ 
        whereStr += " and "+ YHDBUtility.getDateFilter("BEGIN_TIME", beginDate, ">=");
      } 
      if(!YHUtility.isNullorEmpty(endDate)){ 
       whereStr += " and "+ YHDBUtility.getDateFilter("BEGIN_TIME", endDate, "<=");
      }
      
      if(YHUtility.isNullorEmpty(beginDate) && YHUtility.isNullorEmpty(endDate)){
        sql = "select HOUR from oa_timeout_record where USER_ID = '" + userId + "' and OVERTIME_TYPE = '" + status + "' and "
        + YHDBUtility.getMonthFilter("BEGIN_TIME", YHUtility.parseDate(ymd));
      }else{
        sql = "select HOUR from oa_timeout_record where USER_ID = '" + userId + "' and OVERTIME_TYPE = '" + status + "'" + whereStr + "";
      }
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        double normalAdd = rs.getDouble(1);
        result += normalAdd;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return result;
  }
  
  /**
   * 取得用户名称--cc
   * 
   * @param conn
   * @param userId
   * @return
   * @throws Exception
   */

  public String getUserNameLogic(Connection conn, int userId) throws Exception {
    String result = "";
    String sql = " select USER_NAME from PERSON where SEQ_ID = " + userId;
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
   * 加班总时长
   * @param dbConn
   * @param beginDate
   * @param endDate
   * @param userId
   * @return
   * @throws Exception
   */
  public double getOverTimeHourLogic(Connection dbConn, String year, String month, String userId) throws Exception {
    double result = 0;
    double score = 0;
    String sql = "";
    String whereStr = "";
    String ymd = year + "-" + month + "-" + "01";
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      if(!YHUtility.isNullorEmpty(ymd)){
        sql = "select HOUR from oa_timeout_record where USER_ID = '" + userId + "' and STATUS = '1' and "
        + YHDBUtility.getMonthFilter("BEGIN_TIME", YHUtility.parseDate(ymd));
      }
      stmt = dbConn.prepareStatement(sql);
      rs = stmt.executeQuery();
      while (rs.next()) {
        double normalAdd = 0;
        try {
          normalAdd = rs.getDouble(1);
        } catch (Exception e) {
          try {
            normalAdd = Double.parseDouble(rs.getString(1));
          } catch (Exception ex) {
            
          }
        }
        
        score += normalAdd;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return score;
  }
  
  public double getOverTimeMoneyLogic(Connection dbConn, String year, String month, String userId) throws Exception {
    double result = 0;
    double score = 0;
    String sql = "";
    String whereStr = "";
    String ymd = year + "-" + month + "-" + "01";
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      if(!YHUtility.isNullorEmpty(ymd)){
        sql = "select OVERTIME_MONEY from oa_timeout_record where USER_ID = '" + userId + "' and STATUS = '1' and "
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
}
