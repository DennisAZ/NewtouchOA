package yh.subsys.oa.fillRegister.attendScore.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import org.apache.log4j.Logger;

import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.subsys.oa.fillRegister.attendScore.data.YHAttendScore;

public class YHAttendScoreLogic {
  private static Logger log = Logger.getLogger(YHAttendScoreLogic.class);

  public void add(Connection dbConn, YHAttendScore record) throws Exception {
    try {
      YHORM orm = new YHORM();
      orm.saveSingle(dbConn, record);
    } catch (Exception ex) {
      throw ex;
    } finally {

    }
  }
  
  public void updateRecord(Connection conn, YHAttendScore record) throws Exception {
    try {
          YHORM orm = new YHORM();
          orm.updateSingle(conn, record);
        } catch (Exception ex) {
          throw ex;
        } finally {
      }
    }
  
  public boolean getAttendScoreFlag(Connection dbConn, String userId, int flowId)
  throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      String sql = "SELECT count(*) from oa_score_data where FLOW_ID = " + flowId + " and RANKMAN = '" + userId + "'";
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
   * 出差统计天数－－进行自动补登记
   * @param conn
   * @param year
   * @param month
   * @param userId
   * @return
   * @throws Exception
   */
  public long getAttendEvection(Connection conn, String year, String month, String userId) throws Exception {
    long result = 0;
    String ymd = "";
    if(year == null){
      ymd = year+"-"+month+"-"+"07";
   }else{
      ymd = year+"-"+month+"-"+"07";
   }
    long totalScore = 0;
    String sql = " select EVECTION_DATE1, EVECTION_DATE2 from oa_attendance_trip where ALLOW ='1' and STATUS = '1' and PARTICIPANT='"+userId+"' and "+ YHDBUtility.getMonthFilter("EVECTION_DATE1", YHUtility.parseDate(ymd));
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        Date beginDate = rs.getDate(1);
        Date endDate = rs.getDate(2);
        long datSpace = YHUtility.getDaySpan(beginDate,endDate) + 1;
        totalScore += datSpace * 9;
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return totalScore;
  }
}
