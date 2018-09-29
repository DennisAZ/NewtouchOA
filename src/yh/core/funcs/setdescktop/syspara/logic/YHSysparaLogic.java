package yh.core.funcs.setdescktop.syspara.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.funcs.system.logic.YHSystemLogic;
import yh.core.util.db.YHDBUtility;

public class YHSysparaLogic {
  private static Logger log = Logger.getLogger(YHSysparaLogic.class);
  
  public String queryLogoutText(Connection conn) throws Exception{
    Statement sm = null;
    ResultSet rs = null;
    try{
      String logoutText = null;
      String sql = "select" +
      		" PARA_VALUE" +
      		" from SYS_PARA" +
      		" where PARA_NAME = 'LOG_OUT_TEXT'";
      
      sm = conn.createStatement();
      rs = sm.executeQuery(sql);
      if (rs.next()){
        logoutText = rs.getString("PARA_VALUE");
        if (logoutText == null){
          logoutText = "";
        }
      }
      return logoutText;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(sm, rs, log);
    }
  }
  
  public String queryStatusMarquee(Connection conn) throws Exception{
    Statement sm = null;
    ResultSet rs = null;
    try{
      String sql = "select" +
      " PARA_VALUE" +
      " from SYS_PARA" +
      " where PARA_NAME = 'STATUS_TEXT_MARQUEE'";
      
      sm = conn.createStatement();
      rs = sm.executeQuery(sql);
      
      String value = null;
      
      if (rs.next()) {
        value = rs.getString("PARA_VALUE");
      }
      
      return value;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(sm, rs, log);
    }
  }
  
  
  public int queryUserCount(Connection conn) throws Exception{
    Statement sm = null;
    ResultSet rs = null;
    try{
      String sql = "select count(distinct(user_ID)) as COUNT" +
      		" from oa_online";
      
      sm = conn.createStatement();
      rs = sm.executeQuery(sql);
      
      if (rs.next()) {
        return rs.getInt("COUNT");
      }
      else {
        return 0;
      }
      
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(sm, rs, log);
    }
  }
  
  public String queryStatusText(Connection conn) throws Exception{
    Statement sm = null;
    ResultSet rs = null;
    try{
      String sql = "select STATUS_TEXT from oa_inf";
      
      sm = conn.createStatement();
      rs = sm.executeQuery(sql);
      
      String value = null;
      
      if (rs.next()) {
        value = rs.getString("STATUS_TEXT");
      }
      
      return value;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(sm, rs, log);
    }
  }
  
  public String queryFuncId(Connection conn, String name) throws Exception{
    Statement sm = null;
    ResultSet rs = null;
    try{
      String sql = "select MENU_ID" +
      		" from oa_sys_func" +
      		" where FUNC_NAME = '" + name + "'";
      
      sm = conn.createStatement();
      rs = sm.executeQuery(sql);
      
      String value = null;
      
      if (rs.next()) {
        value = rs.getString("MENU_ID");
      }
      
      return value;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(sm, rs, log);
    }
  }
  
  public Map<String,String> queryHeaderImg(Connection conn) throws Exception{
    Statement sm = null;
    ResultSet rs = null;
    try{
      String path = "";
      String sql = "select" +
            " ATTACHMENT_ID" +
            ",ATTACHMENT_NAME" +
            ",IMG_WIDTH" +
            ",IMG_HEIGHT" +
            " from oa_inf";
      
      sm = conn.createStatement();
      rs = sm.executeQuery(sql);
      
      Map<String,String> map = new HashMap<String,String>();
      if (rs.next()){
        String id = YHSystemLogic.parseString(rs.getString("ATTACHMENT_ID"));
        String name = YHSystemLogic.parseString(rs.getString("ATTACHMENT_NAME"));
        path = id + System.getProperty("file.separator") + name;
        int width = rs.getInt("IMG_WIDTH");
        int height = rs.getInt("IMG_HEIGHT");
        
        map.put("id", id);
        map.put("name", name);
        map.put("width", width + "");
        map.put("height", height + "");
      }
      return map;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(sm, rs, log);
    }
  }
}
