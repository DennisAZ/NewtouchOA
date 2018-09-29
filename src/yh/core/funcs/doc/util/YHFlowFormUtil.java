package yh.core.funcs.doc.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import yh.core.util.db.YHDBUtility;

public class YHFlowFormUtil {
  private static Logger log = Logger.getLogger(YHFlowFormUtil.class);
  
  public int deleteDeptMul(Connection dbConn, int seqId) {
    int deptName = 0;
    String name = "";
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "SELECT DEPT_PARENT FROM oa_department WHERE SEQ_ID = '" + seqId + "'";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        name = rs.getString("DEPT_PARENT");
        deptName = Integer.parseInt(name);
      }
      if(deptName != 0){
        seqId = deleteDeptMul(dbConn,deptName);
      }
    } catch (Exception ex) {
      // throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return seqId;
  }
  
  public String deleteDept(Connection dbConn, int seqId) {
    int deptName = 0;
    String name = "";
    String str = "";
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "SELECT MANAGER FROM oa_department WHERE SEQ_ID = '" + seqId + "'";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        name = rs.getString("MANAGER");
      }
    } catch (Exception ex) {
      // throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return name;
  }
  
}
