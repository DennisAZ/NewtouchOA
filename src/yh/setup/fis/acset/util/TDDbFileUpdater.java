package yh.setup.fis.acset.util;


import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import yh.core.exps.YHInvalidParamException;
import yh.core.util.YHI18n;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUtility;
import yh.setup.fis.acset.data.TDAcset;
import yh.setup.fis.acset.data.TDMaintainConst;
import yh.setup.util.YHERPSetupUitl;


/**
 * 由参数文件控制，更新数据库
 * @author jpt
 * @version 1.0
 * @date 2007-1-30
 */
public class TDDbFileUpdater {
  /**
   * log
   */
  private static final Logger log = Logger.getLogger(
      "yzq.com.td.maintain.datasaver.TDDbFileUpdater");
  
  /**
   * 执行一个文件中的Sql语句，把该文件中的Sql语句作为一个整体执行
   * @param inputFile          Sql语句文件
   *        第一行dbNameStr
   *          TDSYS：系统库更新
   *          TDACSET：帐套库更新，系统将根据帐套定义动态确定帐套数据库
   *          其他：自定义数据库名称，以逗号分隔
   *        第二行useTrns true = 启用；false = 不用
   *        #为注释符号
   *        GO表示批处理Sql语句的一次执行，用来分隔Sql语句的执行，如果一个Sql语句写成多行，也可以用作Sql语句之间的分隔
   * @throws Exception
   */
  public static void exectSqlInfileAsWhole(String inputFile,String dbName,String dbmsName) throws Exception {
    ArrayList descList = new ArrayList();
    YHFileUtility.loadLine2Array(inputFile, 0, 1, descList);
    if (descList.size() < 2) {
      throw new YHInvalidParamException(YHI18n.ln(TDDbFileUpdater.class, "1"));
    }
    String dbNameStr = ((String)descList.get(0)).trim();
    String useTransactionStr = ((String)descList.get(1)).trim();
    if (dbNameStr.startsWith("--")) {
      dbNameStr = dbNameStr.substring(2).trim();
    }
    if (useTransactionStr.startsWith("--")) {
      useTransactionStr = useTransactionStr.substring(2).trim();
    }
    String[] dbNameArray = null;    
    
    /** 是否处理事务 **/
    boolean useTrns = Boolean.valueOf(useTransactionStr).booleanValue();
    Connection dbConn = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      YHDBUtility dbUtil = new YHDBUtility(dbmsName);
      /** 取得数据库 **/
      String sysDbName = dbName;
      if (dbNameStr.equalsIgnoreCase(TDMaintainConst.UPDATE_DB_SYS)) {
        dbNameArray = new String[]{
            sysDbName
        };
      }else if (dbNameStr.equalsIgnoreCase(TDMaintainConst.UPDATE_DB_ACSET)) {
        dbConn = dbUtil.getConnection(true, sysDbName);
        stmt = dbConn.createStatement();
        ArrayList acsetList = TDAcsetLoader.loadAcsetList(dbConn);
        int acsetCnt = acsetList.size();
        dbNameArray = new String[acsetCnt];
        for (int i = 0; i < acsetCnt; i++) {
          TDAcset acset = (TDAcset)acsetList.get(i);
          String sql = "select DB_NAME from SYS_DATABASES where DB_NO='" + acset.getAcsetDbNo() + "'";
          rs = stmt.executeQuery(sql);
          if (rs.next()) {
            dbNameArray[i] = rs.getString(1).trim();
          }
        }
      }else {
        dbNameArray = dbNameStr.split(",");
      }
      ArrayList sqlList = new ArrayList();
      YHFileUtility.loadLine2Array(inputFile, 2, sqlList);

      exectBatchSql(dbNameArray, sqlList, useTrns,dbmsName);   
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
      YHDBUtility.closeDbConn(dbConn, log);      
    }
  }
  public static void exectSqlInfileAsWhole(String inputFile,String dbmsName) throws Exception {
    ArrayList descList = new ArrayList();
    YHFileUtility.loadLine2Array(inputFile, 0, 1, descList);
    if (descList.size() < 2) {
      throw new YHInvalidParamException(YHI18n.ln(TDDbFileUpdater.class, "1"));
    }
    String dbNameStr = ((String)descList.get(0)).trim();
    String useTransactionStr = ((String)descList.get(1)).trim();
    if (dbNameStr.startsWith("--")) {
      dbNameStr = dbNameStr.substring(2).trim();
    }
    if (useTransactionStr.startsWith("--")) {
      useTransactionStr = useTransactionStr.substring(2).trim();
    }
    String[] dbNameArray = null;    
    
    /** 是否处理事务 **/
    boolean useTrns = Boolean.valueOf(useTransactionStr).booleanValue();
    Connection dbConn = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      YHDBUtility dbUtil = new YHDBUtility(dbmsName);
      /** 取得数据库 **/
      String sysDbName = "TDSYS";
      if(dbmsName != null && !"".equals(dbmsName)){
        sysDbName = dbmsName;
      }
      if (dbNameStr.equalsIgnoreCase(TDMaintainConst.UPDATE_DB_SYS)) {
        dbNameArray = new String[]{
            sysDbName
        };
      }else if (dbNameStr.equalsIgnoreCase(TDMaintainConst.UPDATE_DB_ACSET)) {
        dbConn = dbUtil.getConnection(true, sysDbName);
        stmt = dbConn.createStatement();
        ArrayList acsetList = TDAcsetLoader.loadAcsetList(dbConn);
        int acsetCnt = acsetList.size();
        dbNameArray = new String[acsetCnt];
        for (int i = 0; i < acsetCnt; i++) {
          TDAcset acset = (TDAcset)acsetList.get(i);
          String sql = "select DB_NAME from SYS_DATABASES where DB_NO='" + acset.getAcsetDbNo() + "'";
          rs = stmt.executeQuery(sql);
          if (rs.next()) {
            dbNameArray[i] = rs.getString(1).trim();
          }
        }
      }else {
        dbNameArray = dbNameStr.split(",");
      }
      ArrayList sqlList = new ArrayList();
      YHFileUtility.loadLine2Array(inputFile, 2, sqlList);

      exectBatchSql(dbNameArray, sqlList, useTrns,dbmsName);   
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
      YHDBUtility.closeDbConn(dbConn, log);      
    }
  }
  /**
   * 执行批处理Sql语句
   * @param dbNameArray            数据库名称数组String[]{dbName, }
   * @param sqlList                批处理Sql语句列表
   * @param isUseTrnsact           是否使用事务 true=使用；false=不使用
   * @throws Exception
   */
//  public static void exectBatchSql(
//      String dbName,
//      ArrayList sqlList,
//      boolean useTrns) throws Exception {
//    
//    exectBatchSql(new String[]{dbName}, sqlList, useTrns,"sqlserver");
//  }
  
  /** by cly
   * 
   * 执行批处理Sql语句
   * @param dbNameArray            数据库名称数组String[]{dbName, }
   * @param sqlList                批处理Sql语句列表
   * @param isUseTrnsact           是否使用事务 true=使用；false=不使用
   * @throws Exception
   */
  public static void exectBatchSql(
      String dbName,
      ArrayList sqlList,
      boolean useTrns,
      String dbmsName) throws Exception {
    
    exectBatchMySql(new String[]{dbName}, sqlList, useTrns, dbmsName);
  }
  
  
  /**
   * 执行批处理Sql语句
   * @param dbNameArray            数据库名称数组String[]{dbName, }
   * @param sqlList                批处理Sql语句列表
   * @param isUseTrnsact           是否使用事务 true=使用；false=不使用
   * @throws Exception
   */
  public static void exectBatchSql(
      String[] dbNameArray,
      ArrayList sqlList,
      boolean useTrns,String dbmsName) throws Exception {
    
    Connection dbConn = null;
    Statement stmt = null;  
    String exSql = null;
    try {
      YHDBUtility dbUtil = new YHDBUtility(dbmsName);

      /** 对所指定的库执行Sql语句 **/
      for (int i = 0; i < dbNameArray.length; i++) {
        String dbName = dbNameArray[i].trim();
        if (!YHERPSetupUitl.isDbActive(dbName,dbmsName)) {
          continue;
        }
        dbConn = dbUtil.getConnection(!useTrns, dbName);
        stmt = dbConn.createStatement();
        
        StringBuffer sqlBuff = new StringBuffer();
        boolean hasBeenExecute = false;
        for (int j = 0; j < sqlList.size(); j++) {
          String sqlStr = ((String)sqlList.get(j)).trim();
          if (sqlStr.length() < 1 || sqlStr.startsWith("#")
              || sqlStr.startsWith("--")) {
            continue;
          }
          hasBeenExecute = false;
          if (sqlStr.toUpperCase().equals("GO")) {
            exSql = sqlBuff.toString();
            stmt.execute(exSql);
            sqlBuff.delete(0, sqlBuff.length());
            hasBeenExecute = true;
            continue;
          }else {
            sqlBuff.append("\n");
            sqlBuff.append(sqlStr);
          }          
        }
        if (!hasBeenExecute && sqlBuff.length() > 0) {
          stmt.execute(sqlBuff.toString());
          sqlBuff.delete(0, sqlBuff.length());
        }

        if (useTrns) {
          dbConn.commit();
        }
        YHDBUtility.closeDbConn(dbConn, log);
      }     
    }catch(Exception ex) {
      try {
        if (useTrns) {
          ex.printStackTrace();
          dbConn.rollback();
        }
      }catch(Exception ex2) {        
      }
//      log.debug("exSql>>" + exSql);
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, log);
      YHDBUtility.closeDbConn(dbConn, log);
    }
  }
  
  
  /**  by cly 2007-12-18
   * 
   * 执行批处理Sql语句
   * @param dbNameArray            数据库名称数组String[]{dbName, }
   * @param sqlList                批处理Sql语句列表
   * @param isUseTrnsact           是否使用事务 true=使用；false=不使用
   * @throws Exception
   */
  public static void exectBatchMySql(
      String[] dbNameArray,
      ArrayList sqlList,
      boolean useTrns,
      String dbmsName) throws Exception {
    
    Connection dbConn = null;
    Statement stmt = null;  
    String exSql = null;
    try {
      YHDBUtility dbUtil = new YHDBUtility(dbmsName);

      /** 对所指定的库执行Sql语句 **/
      for (int i = 0; i < dbNameArray.length; i++) {
        String dbName = dbNameArray[i].trim();
        dbConn = dbUtil.getConnection(!useTrns, dbName);
        stmt = dbConn.createStatement();
        
        StringBuffer sqlBuff = new StringBuffer();
        boolean hasBeenExecute = false;
        for (int j = 0; j < sqlList.size(); j++) {          
          String sqlStr = ((String)sqlList.get(j)).trim();
          if (sqlStr.length() < 1 || sqlStr.startsWith("#")
              || sqlStr.startsWith("--")) {
            continue;
          }          
          stmt.execute(YHUtility.transferCode(sqlStr,"UTF-8","ISO8859_1"));        
        }
        
        if (useTrns) {
          dbConn.commit();
        }
        
        YHDBUtility.closeDbConn(dbConn, log);
      }     
    }catch(Exception ex) {
      try {
        if (useTrns) {
          ex.printStackTrace();
          dbConn.rollback();
        }
      }catch(Exception ex2) {        
      }
//      log.debug("exSql>>" + exSql);
      throw ex;
    }finally {
      YHDBUtility.close(stmt, null, log);
      YHDBUtility.closeDbConn(dbConn, log);
    }
  }
  
  
  /**
   * 执行指定路径下的Sql文件，按文件名从小到大执行
   * @param path
   */
  public static void executeSqlInPath(String path,String dbmsName) throws Exception {
    File pathFile = new File(path);
    if (!pathFile.exists()) {
      return;
    }
    if (!pathFile.isDirectory()) {
      return;
    }
    String[] fileNameArray = YHFileUtility.sortFileList(pathFile);
    for (int i = 0; i < fileNameArray.length; i++) {
      String currPath = path + "\\" + fileNameArray[i];
      if (new File(currPath).isFile() && currPath.endsWith(".sql")) {
        TDDbFileUpdater.exectSqlInfileAsWhole(currPath,dbmsName);
      }
    }
  }
}
