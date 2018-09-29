package yh.core.funcs.doc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import yh.core.util.db.YHDBUtility;

public class YHFlowFormLogic {
  private static Logger log = Logger.getLogger(YHFlowFormLogic.class);
  int seqID = 0;
  
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
  
  public String deleteDeptd(Connection dbConn, Object object) {
    String name = "";
    String str = "";
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "SELECT MANAGER FROM oa_department WHERE SEQ_ID = '" + object + "'";
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
  
  public List deptLocal(Connection dbConn, Object object, int seqId) {
    List list = new ArrayList();
    int str = 0;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "SELECT T1.SEQ_ID FROM PERSON T1, USER_PRIV T2 WHERE T1.DEPT_ID = '" + object 
    + "' AND NOT T1.SEQ_ID = '" + seqId 
    + "' AND T1.USER_PRIV = T2.SEQ_ID ORDER BY T2.PRIV_NO, USER_NAME, PRIV_NAME";
    
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        str = rs.getInt("SEQ_ID");
        list.add(str);
      }
    } catch (Exception ex) {
      // throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return list;
  }
  
  public List getPerson(Connection dbConn) {
    List list = new ArrayList();
    int str = 0;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "SELECT T1.SEQ_ID FROM PERSON T1, USER_PRIV T2 WHERE T1.USER_PRIV = T2.SEQ_ID ORDER BY T2.PRIV_NO, USER_NAME, PRIV_NAME";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        str = rs.getInt("SEQ_ID");
        list.add(str);
      }
    } catch (Exception ex) {
      // throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return list;
  }
  
  public List deptParent(Connection dbConn, Object object) {
    List list = new ArrayList();
    String name = "";
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "SELECT MANAGER FROM oa_department WHERE SEQ_ID = '" + object + "'";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        name = rs.getString("MANAGER");
        list.add(name);
      }
    } catch (Exception ex) {
      // throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return list;
  }
  
  public int deptFirstLogic(Connection dbConn, int seqId) {
    int deptParent = 0;
    List list = new ArrayList();
    String name = "";
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "SELECT DEPT_PARENT, SEQ_ID FROM oa_department WHERE SEQ_ID = '" + seqId + "'";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        name = rs.getString("DEPT_PARENT"); 
        seqID = rs.getInt("SEQ_ID");  
        list.add(seqId);
        deptParent = Integer.parseInt(name);
      }
      if(deptParent != 0){
        seqID = deptFirstLogic(dbConn,deptParent);
      }
    } catch (Exception ex) {
      // throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return seqID;
  }
  
  public List getDeptNameLong(Connection dbConn, int seqId) {
    int deptParent = 0;
    List list = new ArrayList();
    int seqIdLong = 0;
    String name = "";
    String deptName = "";
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "SELECT DEPT_NAME, DEPT_PARENT, SEQ_ID FROM oa_department WHERE SEQ_ID = '" + seqId + "'";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        name = rs.getString("DEPT_NAME"); 
        deptName = rs.getString("DEPT_PARENT");
        seqIdLong = rs.getInt("SEQ_ID");  
        deptParent = Integer.parseInt(deptName);
        //System.out.println("SEQ_ID:" + seqIdLong);
        //System.out.println("NAME:" + name);
        list.add(seqIdLong);
      }
      if(deptParent != 0){
        List ll = getDeptNameLong(dbConn,deptParent);
        list.addAll(ll);
      }
    } catch (Exception ex) {
      // throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return list;
  }
  
  public List getDeptLocalInput(Connection dbConn, Object object) {
    List list = new ArrayList();
    String name = "";
    int str = 0;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "SELECT T1.SEQ_ID, T1.DEPT_ID, USER_NAME, PRIV_NO FROM PERSON T1, USER_PRIV T2 WHERE T1.DEPT_ID = '" + object 
               + "' AND T1.USER_PRIV = T2.SEQ_ID ORDER BY T2.PRIV_NO , USER_NAME";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        str = rs.getInt("SEQ_ID");
        name = rs.getString("USER_NAME");
        list.add(str);
      }
    } catch (Exception ex) {
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return list;
  }
  
  public List getDeptFirstInput(Connection dbConn, int seqId) throws Exception {
    List list = new ArrayList();
    String name = "";
    int str = 0;
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "SELECT T1.SEQ_ID, USER_NAME, PRIV_NO FROM PERSON T1, USER_PRIV T2 WHERE T1.DEPT_ID = '" + seqId 
               + "' AND T1.USER_PRIV = T2.SEQ_ID ORDER BY T2.PRIV_NO , USER_NAME";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        str = rs.getInt("SEQ_ID");
        name = rs.getString("USER_NAME");
        list.add(str);
      }
    } catch (Exception ex) {
       throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return list;
  }
  
  public void updateJs(Connection conn, int seqId, String script)throws Exception {
    PreparedStatement pstmt = null;
    try{   
      String sql = "UPDATE "+ YHWorkFlowConst.FLOW_FORM_TYPE +" SET SCRIPT = ? where SEQ_ID =" + seqId;
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, script);
      pstmt.executeUpdate();
      conn.commit();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(pstmt, null, log);
    }
  }
  
  public void updateCss(Connection conn, int seqId, String css)throws Exception {
    PreparedStatement pstmt = null;
    try{   
      String sql = "UPDATE "+ YHWorkFlowConst.FLOW_FORM_TYPE +" SET CSS = ? where SEQ_ID =" + seqId;
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, css);
      pstmt.executeUpdate();
      conn.commit();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(pstmt, null, log);
    }
  }
  
  public List getSeqList(Connection dbConn, String sql) throws Exception {
    int deptParent = 0;
    List list = new ArrayList();
    int seqIdLong = 0;
    String name = "";
    String deptName = "";
    Statement stmt = null;
    ResultSet rs = null;
    //String sqld = "SELECT DEPT_NAME, DEPT_PARENT, SEQ_ID FROM oa_department WHERE SEQ_ID = '" + seqId + "'";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String test = rs.getString(1);
        list.add(test);
      }
    } catch (Exception ex) {
       throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return list;
  }
  
  public String getSeq(Connection dbConn, String sql) throws Exception {
    List list = new ArrayList();
    String name = "";
    String deptName = "";
    Statement stmt = null;
    ResultSet rs = null;
    //String sqld = "SELECT DEPT_NAME, DEPT_PARENT, SEQ_ID FROM oa_department WHERE SEQ_ID = '" + seqId + "'";
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
         name = rs.getString(1);
        list.add(name);
      }
    } catch (Exception ex) {
       throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return name;
  }
  
  public String getSql(Connection dbConn, String sql) throws Exception {
    List list = new ArrayList();
    String name = "";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
         name = rs.getString(1);
      }
    } catch (Exception ex) {
       throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return name;
  }
  
  public String getPrcsList(Connection dbConn, int seqId) throws Exception {
    List list = new ArrayList();
    int seqIdProc = 0;
    String prcsUser = "";
    Statement stmt = null;
    ResultSet rs = null;
    String sql = "SELECT PRCS_USER, SEQ_ID FROM "+ YHWorkFlowConst.FLOW_PROCESS +" WHERE SEQ_ID = " + seqId;
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        prcsUser = rs.getString("PRCS_USER"); 
        seqIdProc = rs.getInt("SEQ_ID");  
      }
    } catch (Exception ex) {
       throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return prcsUser;
  }
}
