package yh.rad.dbexputil.transplant.logic.core.util.db;

import java.sql.Connection;

import yh.core.global.YHSysProps;
import yh.core.load.YHConfigLoader;
import yh.core.util.db.YHDBUtility;

public class YHTransplantUtil {
 
  private static String sysConfigFile = "D:\\project\\yh\\webroot\\yh\\rad\\transplant\\config\\dbconfig.properties";
  private static Connection newConn = null; 
  private static Connection oldConn = null; 

  static {
    YHSysProps.setProps(YHConfigLoader.loadSysProps(sysConfigFile));
  }
  /**
   * 
   * @param autoCommit
   * @param dbName
   * @param dbType 1.old 2.new
   * @return
   * @throws Exception
   */
  public static Connection getDBConn(boolean autoCommit, String dbName,int dbType) throws Exception {
    String dbmsName = null;
    if(dbType == 1){
      dbmsName = YHSysProps.getProp("db.jdbc.olddbms");
    }else if(dbType == 2){
      dbmsName = YHSysProps.getProp("db.jdbc.newdbms");
    }
    YHTransplantDBUtil dbUtil = new YHTransplantDBUtil(dbmsName);
    return dbUtil.getConnection(autoCommit, dbName,dbmsName);
  }
  /**
   * 
   * @param autoCommit
   * @param dbName
   * @param dbType
   * @return
   * @throws Exception
   */
  public static Connection getDBConn(boolean autoCommit, int dbType) throws Exception {
    String dbName = null;
    if(dbType == 1){
      dbName = YHSysProps.getProp("db.jdbc.olddbms.dbname");
    }else if(dbType == 2){
      dbName = YHSysProps.getProp("db.jdbc.newdbms.dbname");
    }
    return getDBConn(autoCommit, dbName, dbType);
  }
  
  /**
   * 
   * @param autoCommit
   * @param dbName
   * @param dbType
   * @return
   * @throws Exception
   */
  public static Connection getDBConn2(boolean autoCommit, int dbType) throws Exception {
    Connection conn = null;
    if(dbType == 2){
      if(newConn == null){
        newConn = getDBConn( autoCommit,  dbType);
      }
      conn = newConn;
    }else if(dbType == 1){
      if(oldConn == null){
        oldConn = getDBConn( autoCommit,  dbType);
      }
      conn = oldConn;
    }
    return conn;
  }
}
