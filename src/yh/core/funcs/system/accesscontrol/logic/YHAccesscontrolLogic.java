package yh.core.funcs.system.accesscontrol.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;
import yh.core.funcs.system.accesscontrol.data.YHAccessControl;
import yh.core.funcs.system.diary.data.YHDiary;
import yh.core.util.db.YHDBUtility;

public class YHAccesscontrolLogic {
  private static Logger log = Logger.getLogger(YHAccesscontrolLogic.class);

  public YHAccessControl getAccessControl(Connection conn) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHAccessControl org = null;
    try {
      String queryStr = "select SEQ_ID, PARA_NAME, PARA_VALUE from SYS_PARA WHERE PARA_NAME='IP_UNLIMITED_USER'";
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      //System.out.println(queryStr);
      while (rs.next()) {
        org = new YHAccessControl();
        org.setSeqId(rs.getInt("SEQ_ID"));
        org.setParaName(rs.getString("PARA_NAME"));
        org.setParaValue(rs.getString("PARA_VALUE"));
        //System.out.println(rs.getString("PARA_VALUE")+"TTTTTTTTTTT");
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
    try {
      String queryStr = "select SEQ_ID, PARA_NAME, PARA_VALUE from SYS_PARA where PARA_NAME='NOTIFY_TOP_DAYS'";
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
  
  public YHDiary getNotifyAE(Connection conn) throws Exception {
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
  
  public void updateAccessControl(Connection conn, String sumStr) throws Exception{
    Statement stmt = null;
    try {
      stmt = conn.createStatement();
      String queryStr = "update SYS_PARA set PARA_VALUE='" + sumStr + "' where PARA_NAME='IP_UNLIMITED_USER'";
      //System.out.println(queryStr);
      stmt.executeUpdate(queryStr);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  
  public void add(Connection conn, String subStr)throws Exception {
    PreparedStatement pstmt = null;
    String lock = "LOCK_TIME";
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
