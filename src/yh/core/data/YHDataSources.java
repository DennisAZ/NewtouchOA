package yh.core.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import yh.core.exps.YHInvalidParamException;
import yh.core.global.YHConst;
import yh.core.global.YHDbKeys;
import yh.core.global.YHMessageKeys;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.load.YHDataSourceLoader;
import yh.core.servlet.YHServletUtility;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHAuthenticator;

/**
 * 系统数据源存储
 * @author jpt
 * @version 1.0
 * @date 2006-8-29
 */
public class YHDataSources {
  /**
   * log
   */
  private static final Logger log = Logger.getLogger(
      "yzq.yh.core.data.TDCDataSources");

  /**
   * 数据库列表
   */
  private static HashMap databaseMap = new LinkedHashMap();  
  /**
   * 数据源哈希表
   */
  private static HashMap dataSourceMap = new HashMap();
  
  /**
   * 清理所有资源
   */
  public static void clearAll() {
    databaseMap.clear();
    dataSourceMap.clear();
  }
  
  /**
   * 构建缺省数据源
   */
  public static void buildDataSourceMap(String fileName) throws Exception {
    ArrayList databaseList = YHDataSourceLoader.loadDataBases(fileName);
    addDatabase(databaseList);
    
    //构建数据源
    int databaseCnt = databaseList.size();
    for (int i = 0; i < databaseCnt; i++) {
      YHDatabase database = (YHDatabase)databaseList.get(i);
      registerDataSource(database);
    }
  }
  
  /**
   * 构建数据源
   */
  public static void buildDataSourceMap(Connection dbConn) throws Exception {
    ArrayList databaseList = YHDataSourceLoader.loadDataBases(
        dbConn);
    addDatabase(databaseList);
    
    //构建数据源
    int databaseCnt = databaseList.size();
    for (int i = 0; i < databaseCnt; i++) {
      YHDatabase database = (YHDatabase)databaseList.get(i);
      registerDataSource(database);
    }
  }
  public static void main(String[] args) throws Exception {
	  System.out.println(YHAuthenticator.ciphEncryptStr("1"));
	  System.out.println(YHAuthenticator.ciphEncryptStr("root"));
	  System.out.println(YHAuthenticator.ciphDecryptStr("faNAHaC6/Dg="));
}
  /**
   * 注册数据源
   * @param database    数据库定义对象
   * @throws Exception
   */
  public static void registerDataSource(YHDatabase database) throws Exception {
    
    String dbName = database.getDbName();
    String dsName = database.getDsName();
    String dbmsName = database.getDbmsName();
    String driver = YHSysProps.getString(YHSysPropKeys.DBCONN_DRIVER + "." + dbmsName);
    
    DataSource ds = null;
    HashMap dbConnProps = new HashMap();
    dbConnProps.put(YHDbKeys.DSPARAM_DRIVER_CLASS_NAME,
        YHSysProps.getString(YHSysPropKeys.DBCONN_DRIVER + "." + dbmsName));
    String url = null;
    url = YHSysProps.getString(YHSysPropKeys.DBCONN_CONURL + "." + dbmsName);
    if (dbmsName.startsWith(YHConst.DBMS_SQLSERVER)) {
      url += "DatabaseName=" + dbName + ";SelectMethod=Cursor";
    }else if (dbmsName.startsWith(YHConst.DBMS_MYSQL)) {
      url += dbName + "?characterEncoding=UTF8";
    }
    dbConnProps.put(YHDbKeys.DSPARAM_URL, url);
    String userName = YHSysProps.getString(YHSysPropKeys.DBCONN_USER_NAME + "." + dbmsName);
    if (dbmsName.startsWith(YHConst.DBMS_ORACLE)) {
      if (!YHUtility.isNullorEmpty(dbName)) {
        userName = dbName;
      }
    }
    dbConnProps.put(YHDbKeys.DSPARAM_USER_NAME, userName);
    String pass = YHSysProps.getString(YHSysPropKeys.DBCONN_PASSWARD + "." + dbmsName);
    if (dbmsName.startsWith(YHConst.DBMS_ORACLE)) {
      if (!YHUtility.isNullorEmpty(database.getPassword())) {
        pass = database.getPassword();
      }
    }
    dbConnProps.put(YHDbKeys.DSPARAM_PASS_WORD, YHAuthenticator.ciphDecryptStr(pass));
    dbConnProps.put(YHDbKeys.DSPARAM_MAX_ACTIVE, 
        YHSysProps.getString(YHSysPropKeys.DBCONN_MAX_ACTIVE + "." + dbmsName));
    int maxIdle = YHSysProps.getInt(YHSysPropKeys.DBCONN_MAX_IDLE + "." + dbmsName);
    if (maxIdle > 0) {
      dbConnProps.put(YHDbKeys.DSPARAM_MAX_IDLE, String.valueOf(maxIdle));
    }
    dbConnProps.put(YHDbKeys.DSPARAM_MAX_WAIT, 
        YHSysProps.getString(YHSysPropKeys.DBCONN_MAX_WAIT + "." + dbmsName));
    dbConnProps.put(YHDbKeys.DSPARAM_DEFAULT_AUTO_COMMIT, 
        YHSysProps.getString(YHSysPropKeys.DBCONN_DEFAULT_AUTO_COMMIT + "." + dbmsName));
    dbConnProps.put(YHDbKeys.DSPARAM_DEFAULT_READ_ONLY, 
        YHSysProps.getString(YHSysPropKeys.DBCONN_DEFAULT_READONLY + "." + dbmsName));
    dbConnProps.put("removeAbandoned", "true");
    dbConnProps.put("logAbandoned", "true");
    dbConnProps.put("removeAbandonedTimeout", "60");
//    dbConnProps.put("logAbandoned", "true");
    ds = (DataSource)YHServletUtility.applicationInstance(
        YHSysProps.getString(YHSysPropKeys.DBCONN_DATASOURCE_TYPE));
    BeanUtils.populate(ds, dbConnProps);
    dataSourceMap.put(dsName, ds);
    
    log.info("datasource " + dsName + " is built");
  }
  
  /**
   * 用数据库编码取得数据源名称
   * @return
   */
  public static String getDsNameByDbNo(String dbNo) 
    throws YHInvalidParamException {
    
    if (dbNo == null) {
      new YHInvalidParamException(YHMessageKeys.COMMON_ERROR_INVALID_DB_NO);
    }

    YHDatabase database = (YHDatabase)databaseMap.get(dbNo);
    return database.getDsName();
  }
   
  /**
   * 批量增加数据库定义
   * @param databaseList
   */
  public static void addDatabase(ArrayList databases) {
    if (databases == null) {
      return;
    }
    for (int i = 0; i < databases.size(); i++) {
      YHDatabase database = (YHDatabase)databases.get(i);
      databaseMap.put(database.getDbNo(), database);
    }
  }
  
  /**  by cly
   * 
   * 单个增加数据库定义  
   * @param databaseList
   */
  public static void addDatabase(YHDatabase database) {
    if (database == null) {
      return;
    }
      databaseMap.put(database.getDbNo(), database);
  }
  
  /**
   * 用数据库编码取得数据库对象
   * @param dbNo          数据库编码
   * @return
   */
  public static YHDatabase getDb(String dbNo) {
    
    return (YHDatabase)databaseMap.get(dbNo);
  }
    
  /**
   * 取得数据源
   * @param key    数据源名称
   * @return
   */
  public static DataSource getDataSource(String key) {
    return (DataSource)dataSourceMap.get(key);
  }
  
  /**
   * 添加数据源
   * @param key              数据源名称
   * @param dataSource       数据源
   */   
  public static void addDataSource(String key, DataSource dataSource) {
    dataSourceMap.put(key, dataSource);
  }

  /**
   * 关闭某个帐套库的连接
   * @param dbNo
   * @return
   * @throws Exception
   */
  public static void closeDbConn(String dbNo) throws Exception {
    String dsName = getDsNameByDbNo(dbNo);
    if (dsName == null) {
      throw new YHInvalidParamException(YHMessageKeys.COMMON_ERROR_INVALID_DB_NO);
    }
    BasicDataSource ds = (BasicDataSource)getDataSource(dsName);
    ds.close(); 
  }

  /**
   * 关闭所有数据库连接池
   * @throws Exception
   */
  public static void closeConnPool() {
    for (Iterator iEntry = dataSourceMap.entrySet().iterator(); iEntry.hasNext();) {
      Map.Entry entry = (Map.Entry)iEntry.next();
      String dsName = (String)entry.getKey();
      String logMsrg = "Datasource " + dsName + " is closed.";
      log.debug(logMsrg);
      BasicDataSource ds = (BasicDataSource)entry.getValue();
      try {
        ds.close();
      }catch(Exception ex) {        
      }
    }
  }
}
