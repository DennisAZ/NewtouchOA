package yh.setup.fis.acset.util;

//Source file: D:\\project\\td_erp\\src\\com\\td\\acctset\\estab\\TDAcctEstab.java


import java.io.File;
import java.sql.*;
import java.util.ArrayList;


import org.apache.log4j.Logger;


import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUtility;
import yh.setup.ea.logic.YHEASetupUtil;
import yh.setup.fis.acset.global.TDAcsetConst;
import yh.setup.fis.logic.YHFISSetupUtil;
import yh.setup.util.YHERPSetupUitl;



/**
 * 创建日期 2006-8-14 功能 帐套建设
 * 
 * @author cly
 */
public class TDAcsetBuilder {
  // log
  private static Logger log = Logger
      .getLogger("chly.com.td.acset.business.TDAcsetBuilder");
  /**
   * 构造函数
   * 
   * @roseuid 4418BF2D00EF
   */
  public static void executionSQLFile(
      String dbName,
      String contextPath,
      String acctYear,
      int rateLength,
      int rateDecimalLength,
      int moneyLength,
      int moneyDecimalLength,
      String dbmsName) throws Exception {

    // SQL文件改写
    String[] strSrc = null;
    String[] B_TYPES = null;
    String propsSQLFiles = YHSysProps.getProp("acsetCreateSQLFiles");
    if (!YHUtility.isNullorEmpty(YHSysProps.getProp("insertSQLFiles"))) {
      propsSQLFiles = propsSQLFiles + ","
          + YHSysProps.getProp("insertSQLFiles");
    }

    strSrc = propsSQLFiles.split(",");
    String propsB_TypeFields = YHSysProps.getProp("B_TypeFileds");
    B_TYPES = propsB_TypeFields.split(","); 
    
    // 数据库创建
    String sql = "CREATE DATABASE " + dbName;
    
    YHDBUtility dbUtil = null;
    Connection sysDbConn = null;
    Statement stmt = null;
    try {
      dbUtil = new YHDBUtility(dbmsName);
      sysDbConn = dbUtil.getConnection(true, "TDSYS");
      stmt = sysDbConn.createStatement();
      stmt.execute(sql);

      String strSrcName = null;
      String strDescName = contextPath
          + "sqlfiles\\temp";
      StringBuffer strSql = null;

      String timeStamp = YHUtility
          .getCurDateTimeStr(YHUtility.DATE_FORMAT_NOSPLIT);
      String tmpDir = contextPath + "sqlfiles\\"
          + timeStamp;
      File tmpDirFile = new File(tmpDir);
      if (!tmpDirFile.exists()) {
        tmpDirFile.mkdirs();
      }
      
      
      for (int i = 0; i < strSrc.length; i++) {
        strSrcName = contextPath + "sqlfiles\\" + strSrc[i];

        strDescName = tmpDir + "\\" + i + ".sql";
        strSql = TDAcsetFileUtility.readTextFile2Buffer(
            acctYear,
            rateLength,
            rateDecimalLength,
            moneyLength,
            moneyDecimalLength,
            strSrcName,
            B_TYPES);
        TDAcsetFileUtility.writeTextFileFromBuffer(strDescName, strSql);
        ArrayList sqlList = new ArrayList();
        YHFileUtility.loadLine2Array(strDescName, sqlList);
        YHERPSetupUitl.exectBatchSql(new String[]{dbName}, sqlList, true,0,dbmsName);
      }
      YHFileUtility.deleteAll(tmpDirFile);
    } catch (Exception ex) {
      ex.printStackTrace();
      YHERPSetupUitl.forceDropDb(dbName,dbmsName);
      throw ex;
    }finally {
      YHDBUtility.closeDbConn(sysDbConn, log);
    }
  }

  /**
   * 
   * @roseuid
   */
  public static void updateAcctYearTables(Statement stmt, String dbName,
      String tables, String acctYear) throws Exception {

    String table[] = tables.split(",");
    for (int i = 0; i < table.length; i++) {
      String sql = "update " + dbName + ".DBO." + table[i]
          + " set acct_Year ='" + acctYear + "'";
      stmt.executeUpdate(sql);
    }
  }

  /**
   * 更新帐套中报表模板的路径
   * @param stmt            与帐套数据库关联的数据库语句
   * @param acsetNo         帐套编码
   * @throws Exception
   */
  public static void updateReptTemptPaths(Statement stmt,
      String acsetDbName,
      String oldAcsetNo,
      String acsetNo,
      String contextPhysicalPath)throws Exception {
    
    String sql = null;
    if (contextPhysicalPath != null) {
      sql = "update " + acsetDbName + ".DBO.REPT_TEMPLT set REPT_PATH=replace(REPT_PATH, 'D:\\TD_ERP\\webroot\\fis', '" + contextPhysicalPath + "')";
      stmt.executeUpdate(sql);
    }
    sql = "update " + acsetDbName + ".DBO.REPT_TEMPLT"
      + " set REPT_PATH=replace(REPT_PATH, 'templet\\" + oldAcsetNo + "', 'templet\\" + acsetNo + "')";
    stmt.executeUpdate(sql);
  }
  /**
   * 关闭期初录入
   * @param dbConn
   * @throws Exception
   */
  public static void closeInit(Connection dbConn) throws Exception {
    Statement stmt = null;
    try {
      stmt = dbConn.createStatement();
      String sql = null;
      if (!YHDBUtility.existsTable(dbConn, "ACSETPROPS")) {
        sql = "CREATE TABLE [dbo].[ACSETPROPS] ("
          + "[SEQ_ID] [int] IDENTITY (1, 1) NOT NULL ,"
          + "[PROP_KEY] [varchar] (50) NOT NULL ,"
          + "[PROP_VALUE] [varchar] (200) COLLATE Chinese_PRC_CI_AS NOT NULL"
          + ") ON [PRIMARY]";
        stmt.executeUpdate(sql);
      }
      sql = "delete from ACSETPROPS where PROP_KEY='" + TDAcsetConst.TDFIS_CLOSE_INIT + "'";
      stmt.executeUpdate(sql);
      sql = "insert into ACSETPROPS (PROP_KEY, PROP_VALUE)"
        + " values ('" + TDAcsetConst.TDFIS_CLOSE_INIT + "', '1')";
      stmt.executeUpdate(sql);
    }catch(Exception ex) {      
    }finally {
      YHDBUtility.close(stmt, null, log);
    }
  }
}
