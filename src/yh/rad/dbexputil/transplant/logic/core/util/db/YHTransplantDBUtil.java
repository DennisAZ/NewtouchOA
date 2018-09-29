package yh.rad.dbexputil.transplant.logic.core.util.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.load.YHConfigLoader;
import yh.core.util.YHTokenSplit;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHAuthenticator;
import yh.core.util.db.YHDBUtility;

/**
 * 数据库工具类
 * 1.取得数据库连接
 * @author Think
 *
 */
public class YHTransplantDBUtil {

  //log
  private static Logger log =
      Logger.getLogger("yzq.yh.core.util.db.TDCDBUtility");

  //驱动是否已经加载的标志

  private boolean isDriverLoad = false;

  //dbms名称
  private String dbmsName = null;
  //驱动类的名称
  private String sDBDriver = null;
  //连接url串

  private String sConnStr = null;
  //用户名

  private String userId = null;
  //用户密码
  private String userPassword = null;
  
  /**
   * 取得数据库主机地址
   */
  public static String getDbHost() {
    String dbConnStr = YHSysProps.getProp("db.jdbc.conurl.sqlserver");
    int index1 = dbConnStr.indexOf("//");
    int index2 = dbConnStr.lastIndexOf(":");
    String host = dbConnStr.substring(index1 + 2, index2);
    return host;
  }
  
  /**
   * 取得数据库主机地址
   */
  public static String getDbPort() {
    String dbConnStr = YHSysProps.getProp("db.jdbc.conurl.sqlserver");
    int index2 = dbConnStr.lastIndexOf(":");
    String port = dbConnStr.substring(index2 + 1, dbConnStr.length() - 1);
    return port;
  }
  
  /**
   * 设置数据库配置

   * @throws Exception
   */
  private void setDbConfig() throws Exception {
    //驱动类的名称
    sDBDriver = YHSysProps.getProp("db.jdbc.driver." + dbmsName);
    //连接url串

    sConnStr = YHSysProps.getProp("db.jdbc.conurl." + dbmsName);
    //用户名

    userId = YHSysProps.getProp("db.jdbc.userName." + dbmsName);
    //用户密码   
    userPassword = YHAuthenticator.ciphDecryptStr(
      YHSysProps.getProp("db.jdbc.passward." + dbmsName));
    YHSysProps.getProp("db.jdbc.datasource." + dbmsName);
  }
  
  /**
   * 构造函数

   */
  public YHTransplantDBUtil() throws Exception {
    this.dbmsName = YHSysProps.getProp("db.jdbc.dbms");
    if (YHUtility.isNullorEmpty(this.dbmsName)) {
      this.dbmsName = "sqlserver";
    }
    setDbConfig();
    //加载驱动，在不使用连接池时有效

    try {
      if (!isDriverLoad) {
        Class.forName(sDBDriver);
        isDriverLoad = true;
      }
    } catch (Exception ex) {
      throw ex;
    }
  }
  
  /**
   * 构造函数，连接非SQLServer用

   * @param dbmsName
   */
  public YHTransplantDBUtil(String dbmsName) throws Exception {
    if(YHUtility.isNullorEmpty(dbmsName)){
      dbmsName = "sqlserver";
    }
    this.dbmsName = dbmsName;
    setDbConfig();
    //加载驱动，在不使用连接池时有效

    try {
      if (!isDriverLoad) {
        Class.forName(sDBDriver);
        isDriverLoad = true;
      }
    } catch (Exception ex) {
      throw ex;
    }
  }

  /**
   * 取得数据库连接

   * @return
   */
  public Connection getConnection(boolean isAutoCommit
      , String dbName) throws SQLException {
    Connection conn = null;
    String conStr = null;
    String userName = null;
    if (dbmsName.equals("sqlserver")) {
      conStr = sConnStr + "DatabaseName=" + dbName;
      if (!isAutoCommit) {
        conStr += ";SelectMethod=Cursor";
      }
      userName = userId;
    }else if (dbmsName.equals("mysql")) {
      conStr = sConnStr + dbName + "?characterEncoding=UTF8";
      userName = userId;
    }else if (dbmsName.equals("oracle")) {
      conStr = sConnStr;
      userName = dbName;
    }else {
      throw new SQLException("not accepted dbms");
    }
    conn = DriverManager.getConnection(conStr, userName, userPassword);
    conn.setAutoCommit(isAutoCommit);
    return conn;
  }
  /**
   * 
   * @param isAutoCommit
   * @param dbName
   * @return
   * @throws SQLException
   */
  public Connection getConnection(boolean isAutoCommit
      , String dbName,String dbmsName) throws SQLException {
    Connection conn = null;
    String conStr = null;
    String userName = null;
    if(dbmsName == null){
      dbmsName = this.dbmsName;
    }
    if (dbmsName.equals("sqlserver")) {
      conStr = sConnStr + "DatabaseName=" + dbName;
      if (!isAutoCommit) {
        conStr += ";SelectMethod=Cursor";
      }
      userName = userId;
    }else if (dbmsName.equals("mysql")) {
      conStr = sConnStr + dbName + "?characterEncoding=UTF8";
      userName = userId;
    }else if (dbmsName.equals("oracle")) {
      conStr = sConnStr;
      userName = dbName;
    }else {
      throw new SQLException("not accepted dbms");
    }
    conn = DriverManager.getConnection(conStr, userName, userPassword);
    conn.setAutoCommit(isAutoCommit);
    return conn;
  }
  /**
   * 执行一个查询SQL语句，把结果封装在ArrayList
   * @param dataBaseName  数据库名称

   * @param sql           select Sql语句, 不可以用“*”查询所有字段的形式
   * @return  如果没有数据，则返回没有 元素的ArrayList
   * 如果有数据，结构如下：

   * ArrayList（

         * 　　String[]{field1Value, field2Value},    //第一行

   *    String[]{field1Value, field2Value},    //第二行

   *    ...                                    //其他行

   * ）

   * @throws java.lang.Exception
   */
  public static ArrayList getRSStringArray(Statement stmt,
      String sql) throws Exception {
    ArrayList rtList = new ArrayList();
    if (sql == null || sql.length() == 0) {
      return rtList;
    }
    String tmpSql = sql.toUpperCase();
    int tmpIndex1 = tmpSql.indexOf("SELECT");
    int tmpIndex2 = tmpSql.indexOf("FROM");
    tmpSql = tmpSql.substring(tmpIndex1 + 1, tmpIndex2);
    int tokenCnt = getFieldCnt(tmpSql);

    ResultSet rs = null;
    try {
      rs = stmt.executeQuery(sql);      
      while (rs.next()) {
        String[] buff = new String[tokenCnt];
        for (int i = 0; i < tokenCnt; i++) {
          String tmpStr = rs.getString(i + 1);
          if (tmpStr == null) {
            tmpStr = "";
          }
          buff[i] = tmpStr;
        }
        rtList.add(buff);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      try {
        rs.close();
      } catch (Exception ex) {
      }
    }

    return rtList;
  }
  
  /**
   * 用缺省的数据执行更新Sql语句
   * @param sql
   * @throws Exception
   */
  public static void executeUpdate(String sql) throws Exception {
    YHRequestDbConn requestDbConn = new YHRequestDbConn("");
    Connection dbConn = null;
    Statement stmt = null;
    try {
      dbConn =  requestDbConn.getSysDbConn();
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
      
      dbConn.commit();
    } catch (Exception ex) {
      try {
        dbConn.rollback();
      }catch(Exception ex2) {        
      }
      throw ex;
    } finally {
      requestDbConn.closeAllDbConns();
    }
  }
  
  /**
   * 取得Sql语句中查询字段的数目
   * @param str
   * @return
   */
  private static int getFieldCnt(String str) {
    if (str == null || str.equals("")) {
      return 0;
    }
    str = str.trim();
    int fieldCnt = 0;
    int stackInt = 0;
    
    char c = 0;
    for (int i = 0; i < str.length(); i++) {
      c = str.charAt(i);
      
      if (c == YHTokenSplit.TOKEN_SPLIT_LEFTBRACKET) {
        stackInt++;
      }else if (c == YHTokenSplit.TOKEN_SPLIT_RIGHTBRACKET) {
        stackInt--;
      }
      
      if (stackInt == 0 && c == YHTokenSplit.TOKEN_SPLIT_COMMA) {
        fieldCnt++;
      }
    }
    if (c != YHTokenSplit.TOKEN_SPLIT_COMMA) {
      fieldCnt++;
    }
    return fieldCnt;
  }
  
  /**
   * 取得行数
   * @param rs
   * @return
   * @throws Exception
   */
  public static int getRowCnt(ResultSet rs) throws Exception {
    if (rs.next()) {
      return rs.getInt(1);
    }
    return 0;
  }
  
  /**
   * 强制删除数据库，解决在数据库连接没有清除时删除失败的问题
   * @param dbConn
   * @param dbName
   * @throws Exception
   */
  public static  boolean isDbActive(String dbName) throws Exception{
    Connection dbConn = null;
    try {
      YHDBUtility dbUtil = new YHDBUtility();
      dbConn = dbUtil.getConnection(true, dbName);
      return true;
    }catch(Exception ex) {      
      return false;
    }finally {
      closeDbConn(dbConn, null);
    }    
  }
  
  /**
   * 强制删除数据库，解决在数据库连接没有清除时删除失败的问题
   * @param dbConn
   * @param dbName
   * @throws Exception
   */
  public static void forceDropDb(String dbName) throws Exception{
    Connection dbConn = null;
    try {
      YHDBUtility dbUtil = new YHDBUtility();
      dbConn = dbUtil.getConnection(true, "master");
      forceDropDb(dbConn, dbName);
    }catch(Exception ex) {
      throw ex;
    }finally {
      closeDbConn(dbConn, null);
    }
  }
  
  /**
   * 强制删除数据库，解决在数据库连接没有清除时删除失败的问题
   * @param dbConn
   * @param dbName
   * @throws Exception
   */
  public static void forceDropDb(Connection dbConn,
      String dbName) throws Exception{
    Statement stmt = null;
    ResultSet rs = null;
    try {
      String sql = null;
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery("execute sp_who");
      List spidList = new ArrayList();
      while (rs.next()) {
        String currDbName = rs.getString("dbname");
        if (dbName.equalsIgnoreCase(currDbName)) {
          spidList.add(rs.getString("spid"));
        }
      }
      for (int i = 0; i < spidList.size(); i++) {
        String spid = (String)spidList.get(i);
        sql = "kill " + spid;
        stmt.execute(sql);
        Thread.sleep(1000);
      }      
      sql = "DROP DATABASE " + dbName;
      stmt.execute(sql);
    }catch(Exception ex) {
      throw ex;
    }finally {
      close(stmt, rs, null);
    }
  }
  
  /**
   * 备份数据库

   * @param dbArray
   * @throws Exception
   */
  public static String backupDb(String dbName) throws Exception{
    String outDir = null;
    String backDir = YHSysProps.getString(YHSysPropKeys.DATABASE_BACKUP_DIR);
    if (backDir != null) {
      outDir = backDir + "\\"
        + YHUtility.getCurDateTimeStr(YHUtility.DATE_FORMAT_NOSPLIT);
    }
    return backupDb(new String[]{dbName}, outDir)[0];
  }
  
  /**
   * 备份数据库

   * @param dbArray
   * @throws Exception
   */
  public static String backupDb(String dbName,
      String outDir) throws Exception{
    return backupDb(new String[]{dbName}, outDir)[0];
  }
  
  /**
   * 备份数据库

   * @param dbArray
   * @throws Exception
   */
  public static String[] backupDb(String[] dbNameArray,
      String outDir) throws Exception{
    Connection dbConn = null;
    try {
      YHDBUtility dbUtil = new YHDBUtility();
      dbConn = dbUtil.getConnection(true, "master");
      return backupDb(dbConn, dbNameArray, outDir);
    }catch(Exception ex) {
      throw ex;
    }finally {
      closeDbConn(dbConn, null);
    }
  }
  
  /**
   * 备份数据库

   * @param dbArray
   * @throws Exception
   */
  public static String[] backupDb(Connection dbConn,
      String[] dbNameArray,
      String outDir) throws Exception{
    String[] rtArray = new String[dbNameArray.length];
    Statement stmt = null;
    try {      
      if (outDir == null) {
        outDir = YHSysProps.getString(YHSysPropKeys.DATABASE_BACKUP_DIR) + "\\"
          + YHUtility.getCurDateTimeStr(YHUtility.DATE_FORMAT_NOSPLIT);
      }else if (outDir.indexOf("\\") < 0) {
        outDir = YHSysProps.getString(YHSysPropKeys.DATABASE_BACKUP_DIR) + "\\" + outDir;
      }
      stmt = dbConn.createStatement();
      String dateStr = YHUtility.getCurDateTimeStr(YHUtility.DATE_FORMAT_NOSPLIT);
      for (int i = 0; i < dbNameArray.length; i++) {
        String dbName = dbNameArray[i];
        File dirFile = new File(outDir);
        if (!dirFile.exists()) {
          dirFile.mkdirs();
        }
        String fileName = outDir + "\\" + dbName + dateStr;
        rtArray[i] = fileName;
        String sql ="backup database " 
          + dbName + " to disk = '" + fileName + "'";
        try {
          stmt.execute(sql);
        }catch(Exception ex) {
          ex.printStackTrace();
        }
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      close(stmt, null, null);
    }
    return rtArray;
  }
  /**
   * 取得日期筛选条件

   * @param dbms
   * @param fieldName
   * @param fieldValue
   * @param opt
   * @return
   * @throws Exception
   */
  public static String getDateFilter(String fieldName, String fieldValue, String opt) throws Exception {
    if (YHUtility.isNullorEmpty(fieldValue)) {
      return "1=1";
    }
    StringBuffer rtBuf = new StringBuffer();
    
    String fullStr = YHUtility.getDateTimeStr(YHUtility.parseDate(fieldValue));
    
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    rtBuf.append(wrapDateField(fieldName, dbms));
    rtBuf.append(opt);
    rtBuf.append("\'");
    rtBuf.append(fullStr);
    rtBuf.append("\'");
    
    return rtBuf.toString();
  }
  /**
   * 取得日期筛选条件

   * @param dbms
   * @param fieldName
   * @param fieldValue
   * @param opt
   * @return
   * @throws Exception
   */
  public static String getDateFilter(String dbms, String fieldName, String fieldValue, String opt) throws Exception {
    if (YHUtility.isNullorEmpty(fieldValue)) {
      return "1=1";
    }
    StringBuffer rtBuf = new StringBuffer();
    
    String fullStr = YHUtility.getDateTimeStr(YHUtility.parseDate(fieldValue));
    
    rtBuf.append(wrapDateField(fieldName, dbms));
    rtBuf.append(opt);
    rtBuf.append("\'");
    rtBuf.append(fullStr);
    rtBuf.append("\'");
    
    return rtBuf.toString();
  }
  
  /**
   * 取得年月筛选排序

   * @param dbms
   * @param fieldName
   * @param fieldValue
   * @param opt
   * @return
   * @throws Exception
   */
  public static String getYMOrderby(String fieldName) throws Exception {
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    
    return wrapYMField(fieldName, dbms);
  }
  
  /**
   * 取得年月筛选条件

   * @param dbms
   * @param fieldName
   * @param fieldValue
   * @param opt
   * @return
   * @throws Exception
   */
  public static String getYMFilter(String fieldName, String fieldValue, String opt) throws Exception {
    if (YHUtility.isNullorEmpty(fieldValue)) {
      return "1=1";
    }
    StringBuffer rtBuf = new StringBuffer();
    
    String fullStr = fieldValue;
    
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    rtBuf.append(wrapYMField(fieldName, dbms));
    rtBuf.append(opt);
    rtBuf.append("\'");
    rtBuf.append(fullStr);
    rtBuf.append("\'");
    
    return rtBuf.toString();
  }
  /**
   * 取得年月筛选条件

   * @param dbms
   * @param fieldName
   * @param fieldValue
   * @param opt
   * @return
   * @throws Exception
   */
  public static String getYMFilter(String dbms, String fieldName, String fieldValue, String opt) throws Exception {
    if (YHUtility.isNullorEmpty(fieldValue)) {
      return "1=1";
    }
    StringBuffer rtBuf = new StringBuffer();
    
    String fullStr = fieldValue;
    
    rtBuf.append(wrapYMField(fieldName, dbms));
    rtBuf.append(opt);
    rtBuf.append("\'");
    rtBuf.append(fullStr);
    rtBuf.append("\'");
    
    return rtBuf.toString();
  }
  
  /**
   * 取得当天筛选条件
   * @param dbms
   * @param fieldName
   * @param fieldValue
   * @param opt
   * @return
   * @throws Exception
   */
  public static String curDayFilter(String fieldName) throws Exception {
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    
    return curDayFilter(dbms, fieldName);
  }
  /**
   * 取得当周筛选条件

   * @param dbms
   * @param fieldName
   * @param fieldValue
   * @param opt
   * @return
   * @throws Exception
   */
  public static String curWeekFilter(String fieldName) throws Exception {
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    
    return curWeekFilter(dbms, fieldName);
  }
  /**
   * 取得当月筛选条件

   * @param dbms
   * @param fieldName
   * @param fieldValue
   * @param opt
   * @return
   * @throws Exception
   */
  public static String curMonthFilter(String fieldName) throws Exception {
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    
    return curMonthFilter(dbms, fieldName);
  }
  /**
   * 取得当年筛选条件

   * @param dbms
   * @param fieldName
   * @param fieldValue
   * @param opt
   * @return
   * @throws Exception
   */
  public static String curYearFilter(String fieldName) throws Exception {
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    
    return curYearFilter(dbms, fieldName);
  }
  /**
   * 取得当天筛选条件
   * @param dbms
   * @param fieldName
   * @param fieldValue
   * @param opt
   * @return
   * @throws Exception
   */
  public static String curDayFilter(String dbms, String fieldName) throws Exception {
    return getDayFilter(dbms, fieldName, null);
  }
  /**
   * 取得当周筛选条件

   * @param dbms
   * @param fieldName
   * @param fieldValue
   * @param opt
   * @return
   * @throws Exception
   */
  public static String curWeekFilter(String dbms, String fieldName) throws Exception {
    return getWeekFilter(dbms, fieldName, null);
  }
  /**
   * 取得当月筛选条件

   * @param dbms
   * @param fieldName
   * @param fieldValue
   * @param opt
   * @return
   * @throws Exception
   */
  public static String curMonthFilter(String dbms, String fieldName) throws Exception {
    return getMonthFilter(dbms, fieldName, null);
  }
  /**
   * 取得当年筛选条件

   * @param dbms
   * @param fieldName
   * @param fieldValue
   * @param opt
   * @return
   * @throws Exception
   */
  public static String curYearFilter(String dbms, String fieldName) throws Exception {
    return getYearFilter(dbms, fieldName, null);
  }
  
  /**
   * 取得当天筛选条件

   * @param dbms
   * @param fieldName
   * @param fieldValue
   * @param opt
   * @return
   * @throws Exception
   */
  public static String getDayFilter(String fieldName, Date date) throws Exception {
    String dbms = YHSysProps.getProp("db.jdbc.dbms");    
    return getDayFilter(dbms, fieldName, date);
  }
  
  /**
   * 取得当周筛选条件

   * @param dbms
   * @param fieldName
   * @param fieldValue
   * @param opt
   * @return
   * @throws Exception
   */
  public static String getWeekFilter(String fieldName, Date date) throws Exception {
    String dbms = YHSysProps.getProp("db.jdbc.dbms");    
    return getWeekFilter(dbms, fieldName, date);
  }
  
  /**
   * 取得当月筛选条件

   * @param dbms
   * @param fieldName
   * @param fieldValue
   * @param opt
   * @return
   * @throws Exception
   */
  public static String getMonthFilter(String fieldName, Date date) throws Exception {
    String dbms = YHSysProps.getProp("db.jdbc.dbms");    
    return getMonthFilter(dbms, fieldName, date);
  }
  
  /**
   * 取得当年筛选条件

   * @param dbms
   * @param fieldName
   * @param fieldValue
   * @param opt
   * @return
   * @throws Exception
   */
  public static String getYearFilter(String fieldName, Date date) throws Exception {
    String dbms = YHSysProps.getProp("db.jdbc.dbms");    
    return getYearFilter(dbms, fieldName, date);
  }
  
  /**
   * 取得当天筛选条件

   * @param dbms
   * @param fieldName
   * @param fieldValue
   * @param opt
   * @return
   * @throws Exception
   */
  public static String getDayFilter(String dbms, String fieldName, Date date) throws Exception {
    if (date == null) {
      date = new Date();
    }
    
    StringBuffer rtBuf = new StringBuffer();
    
    String[] dateLimitStr = YHUtility.getDateLimitStr(date);
    
    String wrapFieldName = wrapDateField(fieldName, dbms);
    rtBuf.append(wrapFieldName);
    rtBuf.append(">=");
    rtBuf.append("\'");
    rtBuf.append(dateLimitStr[0]);
    rtBuf.append("\' and ");
    rtBuf.append(wrapFieldName);
    rtBuf.append("<=");
    rtBuf.append("\'");
    rtBuf.append(dateLimitStr[1]);         
    rtBuf.append("\'");
    
    return rtBuf.toString();
  }
  /**
   * 取得当周筛选条件

   * @param dbms
   * @param fieldName
   * @param fieldValue
   * @param opt
   * @return
   * @throws Exception
   */
  public static String getWeekFilter(String dbms, String fieldName, Date date) throws Exception {
    if (date == null) {
      date = new Date();
    }
    StringBuffer rtBuf = new StringBuffer();
    
    String[] dateLimitStr = YHUtility.getWeekLimitStr(date);
    
    String wrapFieldName = wrapDateField(fieldName, dbms);
    rtBuf.append(wrapFieldName);
    rtBuf.append(">=");
    rtBuf.append("\'");
    rtBuf.append(dateLimitStr[0]);
    rtBuf.append("\' and ");
    rtBuf.append(wrapFieldName);
    rtBuf.append("<=");
    rtBuf.append("\'");
    rtBuf.append(dateLimitStr[1]);         
    rtBuf.append("\'");
    
    return rtBuf.toString();
  }
  /**
   * 取得当月筛选条件

   * @param dbms
   * @param fieldName
   * @param fieldValue
   * @param opt
   * @return
   * @throws Exception
   */
  public static String getMonthFilter(String dbms, String fieldName, Date date) throws Exception {
    if (date == null) {
      date = new Date();
    }
    StringBuffer rtBuf = new StringBuffer();
    
    String[] dateLimitStr = YHUtility.getMonthLimitStr(date);
    
    String wrapFieldName = wrapDateField(fieldName, dbms);
    rtBuf.append(wrapFieldName);
    rtBuf.append(">=");
    rtBuf.append("\'");
    rtBuf.append(dateLimitStr[0]);
    rtBuf.append("\' and ");
    rtBuf.append(wrapFieldName);
    rtBuf.append("<=");
    rtBuf.append("\'");
    rtBuf.append(dateLimitStr[1]);         
    rtBuf.append("\'");
    
    return rtBuf.toString();
  }
  /**
   * 取得当年筛选条件

   * @param dbms
   * @param fieldName
   * @param fieldValue
   * @param opt
   * @return
   * @throws Exception
   */
  public static String getYearFilter(String dbms, String fieldName, Date date) throws Exception {
    if (date == null) {
      date = new Date();
    }
    StringBuffer rtBuf = new StringBuffer();
    
    String[] dateLimitStr = YHUtility.getYearLimitStr(date);
    
    String wrapFieldName = wrapDateField(fieldName, dbms);
    rtBuf.append(wrapFieldName);
    rtBuf.append(">=");
    rtBuf.append("\'");
    rtBuf.append(dateLimitStr[0]);
    rtBuf.append("\' and ");
    rtBuf.append(wrapFieldName);
    rtBuf.append("<=");
    rtBuf.append("\'");
    rtBuf.append(dateLimitStr[1]);         
    rtBuf.append("\'");
    
    return rtBuf.toString();
  }
  
  /**
   * 日期转换函数封装
   * @param dateStr
   * @return
   */
  private static String wrapDateField(String fieldName, String dbms) throws SQLException  {
    if (dbms.equals("sqlserver")) {
      return "CONVERT(varchar, " + fieldName + ", 20)";
    }else if (dbms.equals("mysql")) {
      return "date_format("+fieldName +", \'%Y-%m-%d %H:%i:%S\')";
    }else if (dbms.equals("oracle")) {
      return "to_char(" + fieldName + ", \'yyyy-MM-dd hh24:mi:ss\')";
    }else {
      throw new SQLException("not accepted dbms");
    }
  }
  
  /**
   * 月份转换函数封装
   * @param dateStr
   * @return
   */
  private static String wrapYMField(String fieldName, String dbms) throws SQLException  {
    if (dbms.equals("sqlserver")) {
      return "left(CONVERT(varchar, " + fieldName + ", 20), 5)";
    }else if (dbms.equals("mysql")) {
      return "date_format("+fieldName +", \'%m-%d\')";
    }else if (dbms.equals("oracle")) {
      return "to_char(" + fieldName + ", \'MM-dd\')";
    }else {
      throw new SQLException("not accepted dbms");
    }
  }
  
  /**
   * 日期时间转换成字符串-秒级
   * @param dateStr
   * @return
   */
  public static String formatDateS(String fieldName) throws SQLException  {
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    return formatDateS(fieldName, dbms);
  }
  
  /**
   * 日期时间转换成字符串-秒级
   * @param dateStr
   * @return
   */
  public static String formatDateS(String fieldName, String dbms) throws SQLException  {
    if (dbms.equals("sqlserver")) {
      return "CONVERT(varchar, " + fieldName + ", 20)";
    }else if (dbms.equals("mysql")) {
      return "date_format("+fieldName +", \'%Y-%m-%d %H:%i:%S\')";
    }else if (dbms.equals("oracle")) {
      return "to_char(" + fieldName + ", \'yyyy-MM-dd hh24:mi:ss\')";
    }else {
      throw new SQLException("not accepted dbms");
    }
  }
  
  /**
   * 日期时间转换成字符串-日期级

   * @param dateStr
   * @return
   */
  public static String formatDateD(String fieldName) throws SQLException  {
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    return formatDateD(fieldName, dbms);
  }
  
  /**
   * 日期时间转换成字符串-日期级

   * @param dateStr
   * @return
   */
  public static String formatDateD(String fieldName, String dbms) throws SQLException  {
    if (dbms.equals("sqlserver")) {
      return "left(CONVERT(varchar, " + fieldName + ", 20), 10)";
    }else if (dbms.equals("mysql")) {
      return "date_format("+fieldName +", \'%Y-%m-%d\')";
    }else if (dbms.equals("oracle")) {
      return "to_char(" + fieldName + ", \'yyyy-MM-dd\')";
    }else {
      throw new SQLException("not accepted dbms");
    }
  }
  
  /**
   * 日期时间转换成字符串-仅取小时
   * @param dateStr
   * @return
   */
  public static String formatDateHOnly(String fieldName) throws SQLException  {
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    return formatDateHOnly(fieldName, dbms);
  }
  
  /**
   * 日期时间转换成字符串-仅取小时
   * @param dateStr
   * @return
   */
  public static String formatDateHOnly(String fieldName, String dbms) throws SQLException  {
    if (dbms.equals("sqlserver")) {
      return "right(left(CONVERT(varchar, " + fieldName + ", 20), 13), 2)";
    }else if (dbms.equals("mysql")) {
      return "date_format("+fieldName +", \'%H\')";
    }else if (dbms.equals("oracle")) {
      return "to_char(" + fieldName + ", \'hh24\')";
    }else {
      throw new SQLException("not accepted dbms");
    }
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
   * 在数据库中是否已经存在指定表
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static boolean existsTable(Connection dbConn,
      String tableName) throws Exception {
    
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      String sql = "select count(*) from dbo.sysobjects where id = object_id(N'[dbo].[" + tableName + "]') and OBJECTPROPERTY(id, N'IsUserTable') = 1";
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        if (rs.getInt(1) > 0) {
          return true;
        }
      }
      return false;
    }catch(Exception ex) {
      throw ex;      
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  /**
   * mysql findInSet 处理
   * @param str
   * @param dbFieldName
   * @return
   * @throws SQLException
   */
  public static String findInSet(String str,String dbFieldName) throws SQLException{
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    String result = "";
    if (dbms.equals("sqlserver")) {
      result = "dbo.find_in_set('" +str+ "'," + dbFieldName + ")>0";
    }else if (dbms.equals("mysql")) {
      result = " find_in_set('" +str+ "'," + dbFieldName + ")>0";
    }else if (dbms.equals("oracle")) {
      result = "instr(','||" + dbFieldName + "||',','," +str+ ",') > 0";
    }else {
      throw new SQLException("not accepted dbms");
    }
    
    return result;
  }
  
  public static String findNoInSet(String str,String dbFieldName) throws SQLException{
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    String result = "";
    if (dbms.equals("sqlserver")) {
      result = "dbo.find_in_set('" +str+ "'," + dbFieldName + ")<=0";
    }else if (dbms.equals("mysql")) {
      result = "find_in_set('" +str+ "'," + dbFieldName + ")<=0";
    }else if (dbms.equals("oracle")) {
      result = "instr(','||" + dbFieldName + "||',','," +str+ ",') <= 0";
    }else {
      throw new SQLException("not accepted dbms");
    }
    
    return result;
  }
  /**
   * 
   * @param str
   * @param dbFieldName
   * @return
   * @throws SQLException
   */
  public static String escapeLike(String str) throws SQLException{
    String dbms = YHSysProps.getProp("db.jdbc.dbms");
    String result = "";
    if (dbms.equals("sqlserver")) {
      result = str.replace("[", "[[]").replace("_", "[_]").replace("%", "[%]").replace("\'", "\'\'");
    }else if (dbms.equals("mysql")) {
      result = str.replace("\\", "\\\\").replace("\"", "\\\"").replace("'", "\\'").replace("&", "\\&").replace("%", "\\%").replace("_", "\\_").replace("％", "\\％");
    }else if (dbms.equals("oracle")) {
      result = str.replace("\'", "\'\'").replace("&", "\\&").replace("%", "\\%").replace("_", "\\_").replace("％", "\\％");
    }else {
      throw new SQLException("not accepted dbms");
    }
    
    return result;
  }
  /**
   * 
   * mysql subString_index 处理
   * @param str
   * @param dbFieldName
   * @param index
   * @param startIndex
   * @return
   * @throws SQLException
   */
    public static String subStringIndex(String str,String dbFieldName,int index,int startIndex) throws SQLException {
      String dbms = YHSysProps.getProp("db.jdbc.dbms");
      String result = "";
      if (dbms.equals("sqlserver")) {
        if(startIndex == -1){
          index = -1;
        }
        result = "SUBSTRING_INDEX(" + dbFieldName + ",'" + str + "'," + index + ")";
      }else if (dbms.equals("mysql")) {
        if(startIndex == -1){
          index = -1;
        }
        result = "SUBSTRING_INDEX(" + dbFieldName + ",'" + str + "'," + index + ")";
      }else if (dbms.equals("oracle")) {
        result = "substr(" + dbFieldName + ",1,instr(" + dbFieldName + ",'" + str + "'," + index + "," + startIndex + ")-1) ";
      }else {
        throw new SQLException("not accepted dbms");
      }
      
      return result;
    }
    
    /**
     * 处理当前时间的可移植性

     * @return
     * @throws SQLException
     */
    public static String currDateTime() throws SQLException {
      String dbms = YHSysProps.getProp("db.jdbc.dbms");
      if (dbms.equals("sqlserver")) {
        return "getDate()";
      }else if (dbms.equals("mysql")) {
        return "now()";
      }else if (dbms.equals("oracle")) {
        return "sysdate";
      }else {
        throw new SQLException("not accepted dbms");
      }
    }
    
    /**
     * 处理escape的数据库可移植性

     * @return
     * @throws SQLException
     */
    public static String escapeLike() throws SQLException {
      String dbms = YHSysProps.getProp("db.jdbc.dbms");
      if (dbms.equals("sqlserver")) {
        return "{escape '\\'}";
      }else if (dbms.equals("mysql")) {
        return "escape '\\\\'";
      }else if (dbms.equals("oracle")) {
        return "{escape '\\'}";
      }else {
        throw new SQLException("not accepted dbms");
      }
    }
}
