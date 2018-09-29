package yh.core.funcs.setdescktop.mypriv.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHMyprivLogic {
  private static Logger log = Logger.getLogger(YHMyprivLogic.class);
  
  /**
   * 设置别名
   * @param conn
   * @param byName
   * @param userId
   * @throws Exception
   */
  public void setByName(Connection conn, String byName, int userId) throws Exception{
    PreparedStatement ps = null;
    try{
      String sql = "update person set" +
      " BYNAME = ?" +
      "where SEQ_ID = ?";
      ps = conn.prepareStatement(sql);
      
      ps.setString(1, byName);
      ps.setInt(2, userId);
      ps.executeUpdate();
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  /**
   * 检查别名是否已经存在
   * @param conn
   * @param byName
   * @return
   * @throws Exception
   */
  public int checkByName(Connection conn, String byName) throws Exception{
    if (YHUtility.isNullorEmpty(byName.trim())) {
      return 0;
    }
    
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      int i = 0;
      String sql = "select count(SEQ_ID) as AMOUNT" +
      		" from PERSON" +
      		" where USER_ID = ?" +
      		" or BYNAME = ?";
      ps = conn.prepareStatement(sql);
      ps.setString(1, byName);
      ps.setString(2, byName);
      rs = ps.executeQuery();
      if(rs.next()){
        i = rs.getInt("AMOUNT");
      }
      return i;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 获取页面显示的信息
   * @param conn
   * @param seqId
   * @return
   * @throws Exception
   */
  public Map getPriv(Connection conn, int seqId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try{
      int i = 0;
      String sql = "select" +
      		" (select PRIV_NAME from USER_PRIV where SEQ_ID = p.USER_PRIV) as USER_PRIV" +
      		",POST_PRIV" +
      		",POST_DEPT" +
      		",USER_PRIV_OTHER" +
      		",NOT_VIEW_USER" +
      		",NOT_VIEW_TABLE" +
      		",USEING_KEY" +
      		",EMAIL_CAPACITY" +
      		",FOLDER_CAPACITY" +
      		",BYNAME" +
      		" from PERSON p" +
      		" where SEQ_ID = ? ";
      
      ps = conn.prepareStatement(sql);
      ps.setInt(1, seqId);
      rs = ps.executeQuery();
      
      return this.resultSet2Map(rs);
      
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  /**
   * 获取用户角色名称
   * @param conn
   * @param privId
   * @return
   * @throws Exception
   */
  public String getUserPrivOther(Connection conn, int privId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try{
      
      String sql = "select PRIV_NAME" +
      		" from USER_PRIV" +
      		" where SEQ_ID = ?";
      
      ps = conn.prepareStatement(sql);
      ps.setInt(1, privId);
      rs = ps.executeQuery();
      
      String name = "";
      
      if (rs.next()){
        name = rs.getString("PRIV_NAME");
      }
      return name;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  
  /**
   * 获取用户管理范围的部门名称
   * @param conn
   * @param deptId
   * @return
   * @throws Exception
   */
  public String getPostDept(Connection conn, int deptId) throws Exception{
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    try{
      String sql = "select DEPT_NAME" +
      " from oa_department" +
      " where SEQ_ID = ?";
      
      ps = conn.prepareStatement(sql);
      ps.setInt(1, deptId);
      rs = ps.executeQuery();
      
      String name = "";
      
      if(rs.next()){
        name = rs.getString("DEPT_NAME");
      }
      return name;
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(ps, rs, log);
    }
  }
  
  
  private Map resultSet2Map(ResultSet rs) throws SQLException{
    Map<String,String> map = new HashMap<String,String>();
    if(rs.next()){
      ResultSetMetaData rsMeta = rs.getMetaData();
      for(int i = 0; i < rsMeta.getColumnCount(); ++i){   
        String columnName = rsMeta.getColumnName(i+1);
        map.put(rsMeta.getColumnName(i+1), null == rs.getString(columnName) ? "" : rs.getString(columnName).trim().replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "")); 
     }
    }
    return map;
  }
}
