package yh.setup.util;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHAuthenticator;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUtility;
import yh.core.util.file.YHPropUtility;
import yh.core.util.form.YHFOM;

/**
 * 
 * @author tulaike
 *
 */
public class YHERPSetupUitl {
  private static Logger log = Logger.getLogger(YHERPSetupUitl.class);

  /**
   * 判断运行平台是否已经安装
   * @return
   */
  public static boolean isInstalledErpPlatform (String installPath,String setupContentPath){
    boolean result = false;
    int installFlag = getIsInstalledInfo(installPath, setupContentPath, "erp");
    if(installFlag == 1){
      result = true;
    }
    return result;
  }
  /**
   * 判断进销存系统是否安装
   * @return
   */
  public static boolean isInstalledEa(String installPath,String setupContentPath){
    boolean result = false;
    int installFlag = getIsInstalledInfo(installPath, setupContentPath, "ea");
    if(installFlag == 1){
      result = true;
    }
    return result;
  }
  /**
   * 判断财务系统是否安装
   * @return
   */
  public static boolean isInstalledFis(String installPath,String setupContentPath){
    boolean result = false;
    int installFlag = getIsInstalledInfo(installPath, setupContentPath, "fis");
    if(installFlag == 1){
      result = true;
    }
    return result;
  }
  /**
   * 取得安装信息
   * @return 0表示已安装没有集成，1表示已安装以集成  ,2表示没有安装没有集成
   *  此处的安装是指是否初始化
   */
  public static int getIsInstalledInfo(String installPath,String setupContentPath,String platType){
    int result = 2;
    String setupPath = installPath + "\\webroot\\" + setupContentPath + "\\WEB-INF\\installedInfo\\" + platType ;
    File installedPlatDir = new File(setupPath);
    Map<String,String> rtMap = new HashMap<String, String>();
    if(installedPlatDir.exists()){
      File[] files = installedPlatDir.listFiles();
      for (File file : files) {
        if("Installed.properties".equalsIgnoreCase(file.getName())){
          try {
            YHFileUtility.load2Map(file.getPath(), rtMap);
            String installedInfo = rtMap.get("installInfo");
            if(installedInfo != null){
              result = Integer.valueOf(installedInfo.trim());
            }
          } catch (Exception e) {
            result = 0;
          }
        }
      }
    }
    return result;
  }
  /**
   * 得到erp的安装信息
   * @param installPath
   * @param setupContentPath
   * @return
   */
  public String getErpInstallInfo(String installPath,String setupContentPath){
    
    String erpPlatType = "erp";
    String eaPlatType = "ea";
    String fisPlatType = "fis";
    int erpInstallFlag = getIsInstalledInfo(installPath, setupContentPath, erpPlatType);
    int eaInstallFlag = getIsInstalledInfo(installPath, setupContentPath, eaPlatType);
    int fisInstallFlag = getIsInstalledInfo(installPath, setupContentPath, fisPlatType);
    
    String result = "{erpInstall:\"" + erpInstallFlag + "\"," +
    		"eaInstall:\"" + eaInstallFlag + "\"," +
    		"fisInstall:\"" + fisInstallFlag + "\"" +
    		"}";
    
    return result;
  }
  /**
   * 执行数据库语句
   * @param path
   * @throws Exception
   */
  public static void executionSQLFile(String path,String dbName,String[] strSrc) throws Exception {

    Connection dbConn = null;
    // 数据库创建
    Statement stmt = null;
   
    String sql = "CREATE DATABASE " + dbName;
    YHDBUtility dbUtil = new YHDBUtility("sqlserver");
    try {
      dbConn = dbUtil.getConnection(true, "master");
      if(true){
        dropDatabase( dbName , dbConn );
      }
      stmt = dbConn.createStatement();
      stmt.execute(sql);
      for (int i = 0; i < strSrc.length; i++) {
        String filePath = path + strSrc[i];
       // YHInstallConfig ic = new YHInstallConfig();
        exeSql(filePath,"sqlserver",dbName);
      }
    } catch (Exception Ex) {
      Ex.printStackTrace();
      YHDBUtility.forceDropDb(dbName);
    } finally {
      YHDBUtility.closeDbConn(dbConn, null);
      YHDBUtility.close(stmt, null, null);
    }
  }
  
  public static void dropDatabase(String dbName ,Connection dbConn ){

    Statement stmt = null;
    String sql = "drop DATABASE " + dbName;
    try {
      stmt = dbConn.createStatement();
      stmt.execute(sql);
    } catch (Exception Ex) {
      Ex.printStackTrace();
    } finally {
      YHDBUtility.close(stmt, null, null);
    }
  }
  /**
   * 
   * @param sqlPath
   * @throws Exception
   */
  public static void exeSql(String sqlPath,String dbmsName,String dbName) throws Exception {
    File file = new File(sqlPath);
    if (!file.exists() || !file.isFile()) {
      return;
    }
    List sqlList = new ArrayList();
    YHFileUtility.loadLine2Array(sqlPath, sqlList);
    if (sqlList.size() < 1) {
      return;
    }
    Map<String, String> optsMap = new HashMap<String, String>();
    
    String firstLine = sqlList.get(0).toString().trim();
    if (firstLine.startsWith("--")
        && firstLine.indexOf("useTrans") > 0 
        && firstLine.indexOf("versionNum") > 0) {
      firstLine = firstLine.substring(2).trim();
      optsMap = YHFOM.json2Map(firstLine);
    }
    String useTransStr = YHUtility.null2Empty(optsMap.get("useTrans"));
    String versionStr = YHUtility.null2Empty(optsMap.get("versionNum"));
    boolean useTrans = true;
    if (useTransStr.equalsIgnoreCase("true") || useTransStr.equalsIgnoreCase("false")) {
      useTrans = useTransStr.equalsIgnoreCase("true");
    }
    int updateVersion = 0;
    try {
      if (versionStr.length() > 0) {
        updateVersion = Integer.parseInt(versionStr);
      }
    }catch(Exception ex) {      
    }
    exectBatchSql(new String[]{dbName}, sqlList, useTrans, updateVersion,dbmsName);
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
      List sqlList,
      boolean useTrns,
      int version,
      String dbmsName) throws Exception {
    
    Connection dbConn = null;
    Statement stmt = null;
    ResultSet rs = null;
    String exSql = null;
    try {
      YHDBUtility dbUtil = new YHDBUtility(dbmsName);
      /** 对所指定的库执行Sql语句 **/
      for (int i = 0; i < dbNameArray.length; i++) {
        String dbName = dbNameArray[i].trim();
        dbConn = dbUtil.getConnection(!useTrns, dbName);
        stmt = dbConn.createStatement();
        if (version > 0) {
          try {
            String versionSql = "select VERSION_NUM from version";
            rs = stmt.executeQuery(versionSql);
            int currVersion = -1;
            if (rs.next()) {
              String versionStr = rs.getString(1);
              if (versionStr != null) {
                currVersion = Integer.parseInt(versionStr);
              }
            }
            if (currVersion != version) {
              continue;
            }
          }catch(Exception ex) {          
          }
        }
        
        StringBuffer sqlBuff = new StringBuffer();
        boolean hasBeenExecute = false;
        for (int j = 0; j < sqlList.size(); j++) {
          String sqlStr = ((String)sqlList.get(j)).trim();
          if (sqlStr.length() < 1 || sqlStr.startsWith("#") || sqlStr.startsWith("--")) {
            continue;
          }
          hasBeenExecute = false;
          if (sqlStr.endsWith(";")) {
            sqlBuff.append(sqlStr.substring(0, sqlStr.length() - 1));
            exSql = sqlBuff.toString();
            System.out.println(exSql);
            stmt.execute(exSql);
            sqlBuff.delete(0, sqlBuff.length());
            hasBeenExecute = true;
            continue;
          }else if (sqlStr.toUpperCase().equals("GO")) {
            exSql = sqlBuff.toString();
            System.out.println(exSql);
            stmt.execute(exSql);
            sqlBuff.delete(0, sqlBuff.length());
            hasBeenExecute = true;
            continue;
          }else {
            sqlBuff.append("\r\n");
            sqlBuff.append(sqlStr);
          }          
        }
        if (!hasBeenExecute && sqlBuff.length() > 0) {
          System.out.println(sqlBuff.toString());
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
      //log.debug("exSql >> " + exSql);
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
      YHDBUtility.closeDbConn(dbConn, log);
    }
  }
  

  
  /**
   * 注册TDERP到TDSYS数据库
   * @param path
   * @param dbName
   * @param strSrc
   * @throws Exception
   */
  public static void regEaDbms2TDSYS(String path,String dbName,String[] strSrc) throws Exception{
    for (int i = 0; i < strSrc.length; i++) {
      String filePath = path + strSrc[i];
      YHERPSetupUitl.exeSql(filePath,"sqlserver",dbName);
    }
  }
  /**
   * 清空YH中ERP的信息
   * @param flag
   * @throws Exception
   */
  public static void deleteErpMenu2YH(String flag,String sysDbName) throws Exception{
    Statement st = null;
    String sql = "delete from sys_menu where menu_id='" + flag + "'";
    String sql2 = "delete from oa_sys_func where menu_id like '" + flag + "%' ";
    Connection conn = null;
    YHDBUtility DBUtil = null;
    try {
      DBUtil = new YHDBUtility();
      conn = DBUtil.getConnection(false, sysDbName);
      st = conn.createStatement();
      st.execute(sql);
      st.execute(sql2);
      conn.commit();
    } catch (Exception e) {
      if(conn != null){
        conn.rollback();
      }
      throw e;
    } finally {
      YHDBUtility.close(st, null, null);
      YHDBUtility.closeDbConn(conn, null);
    }
  }
  /**
   * 测试数据库连接
   * @param paramsMap
   * @param dbmsName
   * @return
   * @throws Exception
   */
  public static boolean testDbConn(Map paramsMap,String dbmsName) throws Exception{
    String conIp = ((String[])paramsMap.get("conIp"))[0];
    String conPort = ((String[])paramsMap.get("conPort"))[0];
    String driver = ((String[]) paramsMap.get("driver"))[0];
    String conurl = "";
    String userName = ((String[]) paramsMap.get("userName"))[0];
    String passward = ((String[]) paramsMap.get("passward"))[0];
    passward = YHAuthenticator.ciphEncryptStr(passward);
    conurl = "jdbc:microsoft:sqlserver://" + conIp + ":" + conPort + ";";
    YHSysProps.updateProp("db.jdbc.driver." + dbmsName,driver);
    YHSysProps.updateProp("db.jdbc.conurl." + dbmsName,conurl);
    YHSysProps.updateProp("db.jdbc.userName." + dbmsName,userName);
    YHSysProps.updateProp("db.jdbc.passward." + dbmsName,passward);
    YHDBUtility dbutil = new YHDBUtility(dbmsName);
    Connection conn = null;
    try {
      conn = dbutil.getConnection(false, "master");
      return conn == null ? false : true;
    } catch (Exception e) {
      return false;
    } finally {
      YHDBUtility.closeDbConn(conn, log);
    }
  }
  /**
   * 修改安装配置项
   * @param type
   * @throws Exception 
   */
  public static void updateInstallInfo(String installPath,String setupContentPath,String type,String installValue) throws Exception{
    String setupPath = installPath + "\\webroot\\" + setupContentPath + "\\WEB-INF\\installedInfo\\" + type ;
    File installedPlatDir = new File(setupPath);
    Map<String,String> rtMap = new HashMap<String, String>();
    if(installedPlatDir.exists()){
      File[] files = installedPlatDir.listFiles();
      for (File file : files) {
        if("Installed.properties".equalsIgnoreCase(file.getName())){
          try {
            YHFileUtility.load2Map(file.getPath(), rtMap);
            //String installedInfo = rtMap.get("installInfo");
            rtMap.put("installInfo", installValue);
            YHPropUtility.updatePropFile(file.getPath(), rtMap);
          } catch (Exception e) {
            throw e;
          }
        }
      }
    }
  }
  
  /**
   * 修改系统配置文件中的数据库配置 1.如果TDSYS已经安装则无需修改配置 2.如果没有安装则需修改
   */
  public static void modifySysConfig(Map configMap,String fileName) {
    YHPropUtility.updatePropFile(fileName, configMap);
  }
  
  /**
   * 备份数据库

   * @param dbArray
   * @throws Exception
   */
  public  static String backupDb(String dbName,
      String outDir,String dbmsName) throws Exception{
    return backupDb(new String[]{dbName}, outDir,dbmsName)[0];
  }
  
  /**
   * 备份数据库

   * @param dbArray
   * @throws Exception
   */
  public  static String[] backupDb(String[] dbNameArray,
      String outDir,String dbmsName) throws Exception{
    Connection dbConn = null;
    try {
      YHDBUtility dbUtil = new YHDBUtility(dbmsName);
      dbConn = dbUtil.getConnection(true, "master");
      return YHDBUtility.backupDb(dbConn, dbNameArray, outDir);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.closeDbConn(dbConn, null);
    }
  }
  
  /**
   * 
   * @param dbName
   * @throws Exception
   */
  public static void forceDropDb(String dbName,String dbmsName) throws Exception{
    Connection dbConn = null;
    try {
      YHDBUtility dbUtil = new YHDBUtility(dbmsName);
      dbConn = dbUtil.getConnection(true, "master");
      YHDBUtility.forceDropDb(dbConn, dbName);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.closeDbConn(dbConn, null);
    }
  }
  
  /**
   * 
   * @param dbName
   * @param dbmsName
   * @return
   * @throws Exception
   */
  public static boolean isDbActive(String dbName,String dbmsName) throws Exception{
    Connection dbConn = null;
    try {
      YHDBUtility dbUtil = new YHDBUtility(dbmsName);
      dbConn = dbUtil.getConnection(true, dbName);
      return true;
    }catch(Exception ex) {      
      return false;
    }finally {
      YHDBUtility.closeDbConn(dbConn, null);
    }    
  }
}
