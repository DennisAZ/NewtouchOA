package yh.core.menu.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;


import yh.core.menu.data.YHSysFunction;
import yh.core.util.db.YHDBUtility;

public class YHSysMenuLog {
  private static Logger log = Logger.getLogger(YHSysMenuLog.class);
  
  public void deleteSysMenu(Connection conn, String seqId, String menuId) throws Exception{

    Statement stmt = null;
    try{
      stmt = conn.createStatement();
      
      String sql = "delete from oa_sys_func where MENU_ID = '" + menuId + "'";
      stmt.executeUpdate(sql);
      
      sql = "delete from SYS_MENU where SEQ_ID = " + seqId;
      stmt.executeUpdate(sql);
    }catch(Exception ex) {
      throw ex;
    }finally {
        YHDBUtility.close(stmt, null, log);
    }
  }
  
  public ArrayList<YHSysFunction> listFunction(Connection conn, String menuId) throws Exception{
    Statement stmt = null;
    ResultSet rs = null; 
    ArrayList<YHSysFunction> functionList = null;
    try{
      String queryStr = "select SEQ_ID, MENU_ID, FUNC_NAME, FUNC_CODE from oa_sys_func where MENU_ID like '" + menuId + "%' order by MENU_ID";
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      YHSysFunction function = null;
      functionList = new ArrayList<YHSysFunction>();
      
      while(rs.next()){
        function = new YHSysFunction();
        function.setSeqId(rs.getInt("SEQ_ID"));
        function.setMenuId(rs.getString("MENU_ID"));
        function.setFuncName(rs.getString("FUNC_NAME"));
        function.setFuncCode(rs.getString("FUNC_CODE"));
        functionList.add(function);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
        YHDBUtility.close(stmt, null, log);
    }
    return functionList;
  }
  
  public int selectMenu(Connection conn, String menuId)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      String queryStr = "select count(*) from SYS_MENU where MENU_ID='" + menuId + "'"; 
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      int num = 0;
      if(rs.next()){      
        num = rs.getInt(1);
      }
      return num;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  
  public int selectFunction(Connection conn, String menuId, String menuSort)throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      String queryStr = "select count(*) from oa_sys_func where MENU_ID = '" + menuId + menuSort + "'";
      //System.out.println(queryStr);
      stmt = conn.createStatement();
      rs = stmt.executeQuery(queryStr);
      int num = 0;
      if(rs.next()){      
        num = rs.getInt(1);
      }
      return num;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
  
  public void updateMenu(Connection conn, String menuId, String menuName, String image)throws Exception {
    PreparedStatement pstmt = null;
    try {
      String updateStr = "update SYS_MENU set MENU_NAME = ?, IMAGE = ? where MENU_ID = ?";
      pstmt = conn.prepareStatement(updateStr);
      pstmt.setString(1 , menuName);
      pstmt.setString(2 , image);
      pstmt.setString(3 , menuId);
      pstmt.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(pstmt, null, log);
    }
  }
  
  public void insertMenu(Connection conn, String menuId, String menuName, String image)throws Exception {
    PreparedStatement pstmt = null;
    try {
      String seqStr = "insert into SYS_MENU (MENU_ID, MENU_NAME, IMAGE) values(?, ?, ?)";
      pstmt = conn.prepareStatement(seqStr);
      pstmt.setString(1 , menuId);
      pstmt.setString(2 , menuName);
      pstmt.setString(3 , image);
      pstmt.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(pstmt, null, log);
    }
  }
  
  public void updateFunction(Connection conn, String menuId, String menuIdOld, String funcMenuId)throws Exception {
    PreparedStatement pstmt = null;
    try {     
      String updateStr = "update oa_sys_func set MENU_ID = ? where MENU_ID = ?";
      pstmt = conn.prepareStatement(updateStr);
      pstmt.setString(1 , menuId + funcMenuId.substring(2));
      pstmt.setString(2 , menuIdOld + funcMenuId.substring(2));
      pstmt.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(pstmt, null, log);
    }
  }
  
  public void deleteMenu(Connection conn, String menuId) throws Exception {
    Statement stmt = null;
    try {
      //今天stmt对象没有创建，所以出空指针异常
      stmt = conn.createStatement();
      String deleteStr = "delete from SYS_MENU where MENU_ID = '" + menuId + "'";
      stmt.executeUpdate(deleteStr);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
}
