package yh.user.api.core.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;



public class YHDbconnWrap {
  YHIRequestDbConn requestDbConn = null;
  
  public YHDbconnWrap() {
    try {
      requestDbConn = (YHIRequestDbConn)Class.forName("yh.core.data.YHRequestDbConn").newInstance();
    }catch(Exception ex) {       
    }
  }
  public YHDbconnWrap(HttpServletRequest request) {
    requestDbConn = (YHIRequestDbConn) request.getAttribute("requestDbConnMgr");
  }
  /**
   * 关闭数据库资源

   * @param dbConn
   * @param stmt
   * @param rs
   */
  public static void close(
      Statement stmt, ResultSet rs, Logger log) {
    try {
      if (rs != null) {
        rs.close();
      }
    }catch(Exception ex) {
      if (log != null && log.isDebugEnabled()) {
        log.debug(ex.getMessage(), ex);
      }
    }
    try {
      if (stmt != null) {
        stmt.close();
      }
    }catch(Exception ex) {
      if (log != null && log.isDebugEnabled()) {
        log.debug(ex.getMessage(), ex);
      }
    }
  }
  
  /**
   * 关闭数据库资源

   * @param dbConn
   * @param rs
   */
  public static void closeDbConn(
      Connection dbConn, Logger log) {
    try {
      if (dbConn != null) {
        dbConn.close();
      }
    }catch(Exception ex) {
      if (log != null && log.isDebugEnabled()) {
        log.debug(ex.getMessage(), ex);
      }
    }    
  }
  
  /**
   * 取得数据库连接-用数据源名称
   * @param dsName
   * @return
   */
  public Connection getDbConnByDsName(String dsName) throws SQLException {
    return requestDbConn.getDbConnByDsName(dsName);
  }
  
  /**
   * 取得系统数据库连接

   * @return
   */
  public Connection getSysDbConn() 
    throws SQLException {
    return requestDbConn.getSysDbConn();
  }
  /**
   * 关闭所有的数据库连接

   */
  public void closeAllDbConns() {
    requestDbConn.closeAllDbConns(null);
  }
  /**
   * 关闭所有的数据库连接

   * @param log 
   */
  public void closeAllDbConns(Logger log) {
    requestDbConn.closeAllDbConns(log);
  }
  
  /**
   * 提交所有数据库连接
   *
   */
  public void commitAllDbConns() {
    requestDbConn.commitAllDbConns(null);
  }
  /**
   * 提交所有数据库连接
   * @param log
   */
  public void commitAllDbConns(Logger log) {
    requestDbConn.commitAllDbConns(log);
  }
  
  /**
   * 提交帐套数据库连接

   * @param log
   */
  public void commitAcsetDbConn(Logger log) {
    requestDbConn.commitAcsetDbConn(log);
  }
  
  /**
   * 按数据库编码提交指定数据库连接

   */
  public void commitDbConnByNo(String dbNo, Logger log) {    
    requestDbConn.commitDbConnByNo(dbNo, log);
  }
  /**
   * 按数据源名称提交指定数据库连接

   * dsName        数据源名称

   */
  public void commitDbConnByName(String dsName, Logger log) {    
    requestDbConn.commitDbConnByName(dsName, log);
  }
  
  /**
   * 回滚所有数据库连接
   */
  public void rollbackAllDbConns() {
    rollbackAllDbConns(null);
  }
  /**
   * 回滚所有数据库连接
   * @param log
   */
  public void rollbackAllDbConns(Logger log) {
    requestDbConn.rollbackAllDbConns(log);
  }
}
