package yh.core.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import yh.core.global.YHSysProps;
import yh.user.api.core.db.YHIRequestDbConn;

/**
 * 请求中使用数据库连接管理器
 * @author jpt
 *
 */
public class YHRequestDbConn implements YHIRequestDbConn {
  /**
   * 数据库连接
   */
  private HashMap dbConns = new HashMap();
  /**
   * 账套库编码
   */
  private String acsetDbNo = null;
  
  /**
   * 缺省构造函数
   * @param acsetDbNo
   */
  public YHRequestDbConn() {
  }
  /**
   * 构造函数
   * @param acsetDbNo
   */
  public YHRequestDbConn(String acsetDbNo) {
    this.acsetDbNo = acsetDbNo;
  }
  
  /**
   * 取得数据库连接
   * @param dsName
   * @return
   */
  private Connection getDbConn(String dsName)
    throws SQLException {
    
    if (dbConns.get(dsName) != null) {
      return (Connection)dbConns.get(dsName);
    }
    DataSource ds = YHDataSources.getDataSource(dsName);
    if (ds == null) {
      throw new SQLException("不存在数据源");
    }
    Connection dbConn = ds.getConnection();
    try {
      dbConn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    }catch(Exception ex) {      
    }
    dbConns.put(dsName, dbConn);
    return dbConn;
  }
  
  /**
   * 取得数据库连接-用数据源名称
   * @param dsName
   * @return
   */
  public Connection getDbConnByDsName(String dsName) throws SQLException {
    return getDbConn(dsName);
  }
  
  /**
   * 取得系统数据库连接
   * @return
   */
  public Connection getSysDbConn() throws SQLException {
    
    return getDbConn(YHSysProps.getSysDbDsName());
  }
  
  /**
   * 取得帐套库的连接
   * @return
   * @throws YHInvalidParamException
   * @throws SQLException
   */
//  public Connection getAcsetDbConn() 
//    throws YHInvalidParamException, SQLException {
//
//    if (acsetDbNo == null) {
//      throw new YHInvalidParamException("没有设置账套数据库编码");
//    }
//    return getDbConn(YHDataSources.getDsNameByDbNo(acsetDbNo));
//  }
  
  /**
   * 取得帐套库的连接
   * @return
   * @throws YHInvalidParamException
   * @throws SQLException
   */
//  public Connection getOaDbConn() 
//    throws YHInvalidParamException, SQLException {
//    return getDbConn(YHSysProps.getString(YHSysPropKeys.DBCONN_DATASOURCE_OA_DS_NAME));
//  }
  
  /**
   * 取得进销存库的连接
   * @return
   * @throws YHInvalidParamException
   * @throws SQLException
   */
//  public Connection getErpDbConn() 
//    throws YHInvalidParamException, SQLException {
//    return getDbConn(YHSysProps.getString(YHSysPropKeys.DBCONN_DATASOURCE_ERP_DS_NAME));
//  }
  
  /**
   * 关闭所有的数据库连接
   */
  public void closeAllDbConns() {
    closeAllDbConns(null);
  }
  /**
   * 关闭所有的数据库连接
   * @param log 
   */
  public void closeAllDbConns(Logger log) {
    Iterator iKeys = dbConns.keySet().iterator();
    while (iKeys.hasNext()) {
      Object key = iKeys.next();
      Connection dbConn = (Connection)dbConns.get(key);
      try {
        if (dbConn != null && !dbConn.isClosed()) {
          dbConn.close();
        }
      }catch(Exception ex) {
        if (log != null) {
          log.debug(ex.getMessage(), ex);
        }
      }      
    }    
  }
  
  /**
   * 提交所有数据库连接
   *
   */
  public void commitAllDbConns() {
    commitAllDbConns(null);
  }
  /**
   * 提交所有数据库连接
   * @param log
   */
  public void commitAllDbConns(Logger log) {
    Iterator iKeys = dbConns.keySet().iterator();
  while (iKeys.hasNext()) {
      Object key = iKeys.next();
      Connection dbConn = (Connection)dbConns.get(key);
      try {
        if (dbConn != null && !dbConn.getAutoCommit()) {
          dbConn.commit();
        }
      }catch(Exception ex) {
        if (log != null) {
          log.debug(ex.getMessage(), ex);
        }
      }      
    }
  }
  
  /**
   * 提交帐套数据库连接
   * @param log
   */
  public void commitAcsetDbConn(Logger log) {
    commitDbConnByNo(acsetDbNo, log);
  }
  
  /**
   * 按数据库编码提交指定数据库连接
   */
  public void commitDbConnByNo(String dbNo, Logger log) {    
    try { 
      String dsName = YHDataSources.getDsNameByDbNo(dbNo);
      if (dsName != null) {
        commitDbConnByName(dsName, log);
      }
    }catch(Exception ex) {
      if (log != null) {
        log.debug(ex.getMessage(), ex);
      }
    }  
  }
  /**
   * 按数据源名称提交指定数据库连接
   * dsName        数据源名称
   */
  public void commitDbConnByName(String dsName, Logger log) {    
    try {
      Connection dbConn = (Connection)dbConns.get(dsName);
      if (dbConn != null && !dbConn.getAutoCommit()) {
        dbConn.commit();
      }
    }catch(Exception ex) {
      if (log != null) {
        log.debug(ex.getMessage(), ex);
      }
    } 
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
    Iterator iKeys = dbConns.keySet().iterator();
    while (iKeys.hasNext()) {
      Object key = iKeys.next();
      Connection dbConn = (Connection)dbConns.get(key);
      try {
        if (dbConn != null) {
          dbConn.rollback();
        }
      }catch(Exception ex) {
        if (log != null) {
          log.debug(ex.getMessage(), ex);
        }
      }      
    }
  }
}
