package yh.core.funcs.system.notify.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;
import yh.core.funcs.system.diary.data.YHDiary;
import yh.core.util.db.YHDBUtility;

public class YHNotifyLogic {
  private static Logger log = Logger.getLogger(YHNotifyLogic.class);

  public YHDiary get(Connection conn) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHDiary org = null;
    try {
      String queryStr = "select SEQ_ID, PARA_NAME, PARA_VALUE from SYS_PARA";
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      //System.out.println(queryStr);
      while (rs.next()) {
        org = new YHDiary();
        org.setSeqId(rs.getInt("SEQ_ID"));
        org.setParaName(rs.getString("PARA_NAME"));
        org.setParaValue(rs.getString("PARA_VALUE"));
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return org;
  }
  
  public YHDiary getNotify(Connection conn) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHDiary org = null;
    String days = "1";
    try {
      String queryStr = "select SEQ_ID, PARA_NAME, PARA_VALUE from SYS_PARA where PARA_NAME='NOTIFY_TOP_DAYS'";
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      //System.out.println(queryStr);
      while (rs.next()) {
        org = new YHDiary();
        org.setSeqId(rs.getInt("SEQ_ID"));
        org.setParaName(rs.getString("PARA_NAME"));
        days = rs.getString("PARA_VALUE");
        if (days != null) {
          int index = days.indexOf(".");
          if (index != -1) {
            days = days.substring(0 , index);
          }
        }
        org.setParaValue(days);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return org;
  }
  
  public YHDiary getNotifySingle(Connection conn) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHDiary org = null;
    try {
      String queryStr = "select SEQ_ID, PARA_NAME, PARA_VALUE from SYS_PARA where PARA_NAME='NOTIFY_AUDITING_SINGLE'";
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      //System.out.println(queryStr);
      while (rs.next()) {
        org = new YHDiary();
        org.setSeqId(rs.getInt("SEQ_ID"));
        org.setParaName(rs.getString("PARA_NAME"));
        org.setParaValue(rs.getString("PARA_VALUE"));
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return org;
  }
  
  public YHDiary getNotifyRangeAll(Connection conn) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHDiary org = null;
    try {
      String queryStr = "select SEQ_ID, PARA_NAME, PARA_VALUE from SYS_PARA where PARA_NAME='NOTIFY_AUDITING_ALL'";
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      //System.out.println(queryStr);
      while (rs.next()) {
        org = new YHDiary();
        org.setSeqId(rs.getInt("SEQ_ID"));
        org.setParaName(rs.getString("PARA_NAME"));
        org.setParaValue(rs.getString("PARA_VALUE"));
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return org;
  }
  
  public YHDiary getNotifyExceptionId(Connection conn) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHDiary org = null;
    try {
      String queryStr = "select SEQ_ID, PARA_NAME, PARA_VALUE from SYS_PARA where PARA_NAME='NOTIFY_AUDITING_EXCEPTION'";
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      //System.out.println(queryStr);
      while (rs.next()) {
        org = new YHDiary();
        org.setSeqId(rs.getInt("SEQ_ID"));
        org.setParaName(rs.getString("PARA_NAME"));
        org.setParaValue(rs.getString("PARA_VALUE"));
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return org;
  }
  
  public void updateNotifyDays(Connection conn, int seqId, String sumStr) throws Exception{
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String queryStr = "update SYS_PARA set PARA_VALUE='" + sumStr + "' where SEQ_ID=" + seqId;
      stmt.executeUpdate(queryStr);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  
  public void updateNotifyCheck(Connection conn, int seqId, String sumStr) throws Exception{
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String queryStr = "update SYS_PARA set PARA_VALUE='" + sumStr + "' where SEQ_ID=" + seqId;
      stmt.executeUpdate(queryStr);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  
  public void updateNotifyExceptionId(Connection conn, int seqId, String sumStr) throws Exception{
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String queryStr = "update SYS_PARA set PARA_VALUE='" + sumStr + "' where SEQ_ID=" + seqId;
      stmt.executeUpdate(queryStr);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  
  public void addDays(Connection conn, String subStr)throws Exception {
    PreparedStatement pstmt = null;
    String lock = "NOTIFY_TOP_DAYS";
    try{
      String queryStr = "insert into SYS_PARA (PARA_NAME, PARA_VALUE) values (?, ?)";
      pstmt = conn.prepareStatement(queryStr);
      pstmt.setString(1, lock);
      pstmt.setString(2, subStr);
      pstmt.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(pstmt, null, log);
    }
  }
  
  public void addCheck(Connection conn, String subStr)throws Exception {
    PreparedStatement pstmt = null;
    String lock = "NOTIFY_AUDITING_SINGLE";
    try{
      String queryStr = "insert into SYS_PARA (PARA_NAME, PARA_VALUE) values (?, ?)";
      pstmt = conn.prepareStatement(queryStr);
      pstmt.setString(1, lock);
      pstmt.setString(2, subStr);
      pstmt.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(pstmt, null, log);
    }
  }
  
  public void addExceptionId(Connection conn, String subStr)throws Exception {
    PreparedStatement pstmt = null;
    String lock = "NOTIFY_AUDITING_EXCEPTION";
    try{
      String queryStr = "insert into SYS_PARA (PARA_NAME, PARA_VALUE) values (?, ?)";
      pstmt = conn.prepareStatement(queryStr);
      pstmt.setString(1, lock);
      pstmt.setString(2, subStr);
      pstmt.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(pstmt, null, log);
    }
  }
  
  public void addRangeAll(Connection conn, String subStr)throws Exception {
    PreparedStatement pstmt = null;
    String lock = "NOTIFY_AUDITING_ALL";
    try{
      String queryStr = "insert into SYS_PARA (PARA_NAME, PARA_VALUE) values (?, ?)";
      pstmt = conn.prepareStatement(queryStr);
      pstmt.setString(1, lock);
      pstmt.setString(2, subStr);
      pstmt.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(pstmt, null, log);
    }
  }
}
