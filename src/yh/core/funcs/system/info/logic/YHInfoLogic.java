package yh.core.funcs.system.info.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.util.db.YHDBUtility;
import yh.core.funcs.system.logic.YHSystemLogic;

public class YHInfoLogic {
  
  private static Logger log = Logger.getLogger(YHInfoLogic.class);
  
  /**
   * 获取版本信息
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public Map<String,String> getVersion(Connection conn) throws Exception{
    Statement st = null;
    ResultSet rs = null;
    try{
      Map<String,String> map = new HashMap<String,String>();
      String sql = "select VER" +
          ",VERSION_NUM" +
          ",SN" +
          ",CODE" +
          ",USER_VERSION" +
          " from VERSION";
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      if (rs.next()){
        map.put("ver", YHSystemLogic.parseString(rs.getString("VER")));
        map.put("userVer", YHSystemLogic.parseString(rs.getString("USER_VERSION")));
        map.put("versionNum", YHSystemLogic.parseString(String.valueOf(rs.getInt("VERSION_NUM")), "1"));
        map.put("sn", YHSystemLogic.parseString(rs.getString("SN")));
        map.put("code", YHSystemLogic.parseString(rs.getString("CODE")));
      }
      return map;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(st, rs, log);
    }
  }
  
  public int getUserAmount(Connection conn) throws Exception{
    Statement st = null;
    ResultSet rs = null;
    try{
      int amount = 0;
      
      String sql = "select count(1) as COUNT" +
      " from PERSON";
      
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      
      if (rs.next()){
        amount = rs.getInt("COUNT");
      }
      return amount;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(st, rs, log);
    }
  }
  
  public int getUserAmountNotLogin(Connection conn) throws Exception{
    Statement st = null;
    ResultSet rs = null;
    try{
      int amount = 0;
      
      String sql = "select count(1) as COUNT" +
      " from PERSON" +
      " where NOT_LOGIN = '1'";
      
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      
      if (rs.next()){
        amount = rs.getInt("COUNT");
      }
      return amount;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(st, rs, log);
    }
  }
  
  public String getUnitName(Connection conn) throws Exception{
    Statement st = null;
    ResultSet rs = null;
    try{
      String sql = "select UNIT_NAME" +
      " from oa_organization";
      
      st = conn.createStatement();
      rs = st.executeQuery(sql);
      
      if (rs.next()){
        return YHSystemLogic.parseString(rs.getString("UNIT_NAME"));
      }
      
      return "";
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(st, rs, log);
    }
  }
  
  public void updateSN(Connection conn, String sn) throws Exception{
    PreparedStatement ps = null;
    try{
      String sql = "update VERSION" +
      		" set SN = ?";
      
      ps = conn.prepareStatement(sql);
      ps.setString(1, sn);
      ps.executeUpdate();
      
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 更新注册信息
   */
  public void updateIM(Connection conn, int imUserCnt) {
    PreparedStatement ps = null;
    try {
      int userCnt = imUserCnt;
      if (userCnt <= 0) {
        userCnt = 30;
      }
      
      String sql = null;
      
      if (this.hasProperty(conn, "IM_USER_CNT")) {
        sql = "update SYS_PARA" +
        " set PARA_VALUE = ?" +
        " where PARA_NAME = 'IM_USER_CNT'";
      }
      else {
        sql = "insert into SYS_PARA(PARA_NAME, PARA_VALUE) values('IM_USER_CNT', ?)";
      }
      
      ps = conn.prepareStatement(sql);
      ps.setInt(1, userCnt);
      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      log.debug(e.getMessage(), e);
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  private boolean hasProperty(Connection dbConn, String name) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String sql = "select count(1) AMOUNT" +
          " from SYS_PARA" +
          " where PARA_NAME = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, name);
      rs = ps.executeQuery();
      if (rs.next()) {
        return rs.getInt("AMOUNT") > 0;
      }
    } catch (Exception e) {
      e.printStackTrace();
      log.debug(e.getMessage(), e);
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return false;
  }
}
