package yh.user.api.core.db;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public interface YHIRequestDbConn {
  /**
   * 取得数据库连接-用数据源名称
   * @param dsName
   * @return
   */
  public Connection getDbConnByDsName(String dsName) throws SQLException;
  
  /**
   * 取得系统数据库连接

   * @return
   */
  public Connection getSysDbConn() throws SQLException;
  
  /**
   * 关闭所有的数据库连接

   */
  public void closeAllDbConns();
  /**
   * 关闭所有的数据库连接

   * @param log 
   */
  public void closeAllDbConns(Logger log);
  
  /**
   * 提交所有数据库连接
   *
   */
  public void commitAllDbConns();
  /**
   * 提交所有数据库连接
   * @param log
   */
  public void commitAllDbConns(Logger log);
  
  /**
   * 提交帐套数据库连接

   * @param log
   */
  public void commitAcsetDbConn(Logger log);
  
  /**
   * 按数据库编码提交指定数据库连接

   */
  public void commitDbConnByNo(String dbNo, Logger log);
  /**
   * 按数据源名称提交指定数据库连接

   * dsName        数据源名称

   */
  public void commitDbConnByName(String dsName, Logger log);
  
  /**
   * 回滚所有数据库连接
   */
  public void rollbackAllDbConns();
  /**
   * 回滚所有数据库连接
   * @param log
   */
  public void rollbackAllDbConns(Logger log);
}
