package yh.setup.fis.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;



import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.setup.fis.acset.data.TDAcsetBuildParam;
import yh.setup.fis.acset.data.TDAcsetCreateDatabase;
import yh.setup.fis.acset.data.TDCSystemParams;
import yh.setup.fis.acset.global.TDAcsetConst;
import yh.setup.fis.acset.util.TDDbFileUpdater;
import yh.setup.util.YHERPSetupUitl;
import yh.oa.tools.StaticData;


public class YHFISSetupUtil {
  private static Logger log = Logger.getLogger(YHFISSetupUtil.class);

  /**
   * 创建默认账套库
   * @param installPath
   * @param contextPath
   * @param dbmsName  财务进销存的数据库类型
   * @param sysdbName 财务进销存的系统库
   * @throws Exception
   */
  public  void createDefaultAcset(String installPath, String contextPath,String dbmsName,String sysdbName) throws Exception {
    String acsetPrefix = YHSysProps.getProp("acsetDbPrefix");
    if(acsetPrefix == null || "".equals(acsetPrefix.trim())){
      acsetPrefix = "TD";
    }
    for (int i = 1; i <= 9; i++) {
      String dbName = acsetPrefix + "0" + i;
      if (YHERPSetupUitl.isDbActive(dbName,dbmsName)) {
        try {
          YHERPSetupUitl.backupDb(dbName, "installbackup",dbmsName);
        }catch(Exception ex) {
          log.debug(ex.getMessage(), ex);
        }
        try {
          YHERPSetupUitl.forceDropDb(dbName,dbmsName);
        }catch(Exception ex) {
          log.debug(ex.getMessage(), ex);
        }        
      }
     // deleteDbFile(installPath, dbName);
    }
    Connection sysDbConn = null;
    Connection acsetDbConn = null;
    Statement sysStmt = null;
    Statement acsetStmt = null;
    try {      
      YHDBUtility dbUtil = null;
      dbUtil = new YHDBUtility(dbmsName);
      sysDbConn = dbUtil.getConnection(true, sysdbName);
      //删除原来的账套注册
      sysStmt = sysDbConn.createStatement();
      String sql = "delete from ACCOUNT_SET";
      sysStmt.executeUpdate(sql);
      sql = "delete from SYS_DATABASES where left(DS_NAME, 12)='mssql/acset/'";
      sysStmt.executeUpdate(sql);
      String currTimeStr = YHUtility.getCurDateTimeStr();
      TDCSystemParams sysParams = new TDCSystemParams();
      TDAcsetBuildParam acsetBuildParam = new TDAcsetBuildParam();
      acsetBuildParam.setAccountDbDesc(StaticData.SOFTCOMPANY_SHORTNAME+"协同财务演示帐套");
      acsetBuildParam.setPeriodCntrl("1");
      
      String acctYear = currTimeStr.substring(0, 4);
      acsetBuildParam.setMakeYM(currTimeStr.substring(0, 7));
      acsetBuildParam.setAcctYear(acctYear);
      acsetBuildParam.setStartYM(acctYear + "-01");
      acsetBuildParam.setDeptId("1");
      acsetBuildParam.setContextPath(installPath + "\\webroot\\" + contextPath + "\\");
      acsetBuildParam.setRateLength(18);
      acsetBuildParam.setRateDecimalLength(4);
      acsetBuildParam.setMoneyLength(18);
      acsetBuildParam.setMoneyDecimalLength(2);
      acsetBuildParam.setHomeCurrDesc("人民币");
      acsetBuildParam.setHomeCurrSign("￥");
      TDAcsetCreateDatabase.createDatabase(
          sysDbConn,
          sysParams,
          acsetBuildParam,
          dbmsName);
      //插入测试数据
      insertTestData(installPath, contextPath,dbmsName);
      
      //更新余额表和凭证表的会计年度
      String accountDbName = TDAcsetConst.getAcsetDbPrefix() + "01";
      acsetDbConn = dbUtil.getConnection(true, accountDbName);
      acsetStmt = acsetDbConn.createStatement();
      sql = "update ACCTBLNS set ACCT_YEAR='" + acctYear + "'";
      acsetStmt.executeUpdate(sql);
      sql = "update VOUCHER set ACCT_YEAR='" + acctYear + "'";
      acsetStmt.executeUpdate(sql);
      sql = "update VOUCHER set VOUC_DATE='" + acctYear + "-01-01 00:00:00" + "'";
      acsetStmt.executeUpdate(sql);
      sql = "update REPT_TEMPLT set REPT_PATH=replace(REPT_PATH, 'D:\\TD_ERP\\webroot\\fis', '" + installPath + "\\webroot\\" + contextPath + "')";
      acsetStmt.executeUpdate(sql);
      String roleSql = setRoleByTest(YHSysProps.getString("db.jdbc.dbms"), YHSysProps.getProp("yhsysDbName." + YHSysProps.getString("db.jdbc.dbms")), "01");
      sysStmt.executeUpdate(roleSql);
      //关闭期初录入
      closeInit(acsetDbConn);
    }catch(Exception ex) {
      log.debug(ex.getMessage(), ex);
      throw ex;
    }finally {
      YHDBUtility.close(sysStmt, null, log);
      YHDBUtility.closeDbConn(sysDbConn, log);
      YHDBUtility.close(acsetStmt, null, log);
      YHDBUtility.closeDbConn(acsetDbConn, log);
    }
  }

 /**
  * 
  * @param yhdbms
  * @param yhsysdbname
  * @param acsetNo
  * @return
  * @throws Exception
  */
  public String setRoleByTest(String yhdbms,String yhsysdbname,String acsetNo) throws Exception{
    Connection yhsysDbConn = null;
    Statement yhst = null;
    ResultSet yhrs = null;
    YHDBUtility dbutil = null;
    String rtSql = "";
    int adminSeqId = 0;
    try {
      dbutil = new YHDBUtility(yhdbms);
      yhsysDbConn = dbutil.getConnection(false, yhsysdbname);
      yhst = yhsysDbConn.createStatement();
      String sql = "select seq_id from person where user_id='admin'";
      yhrs = yhst.executeQuery(sql);
      if(yhrs.next()){
        adminSeqId = yhrs.getInt(1);
      }
      rtSql = "insert into USER_ACSET(USER_SEQ_ID,ACSET_NO,IF_DEFAULT) values(" + adminSeqId + ",'" + acsetNo + "','" + 1 + "') ";
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(yhst, yhrs, log);
      YHDBUtility.closeDbConn(yhsysDbConn, log);
    }
    return rtSql;
  }
  /**
   * 关闭期初录入
   * @param dbConn
   * @throws Exception
   */
  public  void closeInit(Connection dbConn) throws Exception {
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
  /**
   * 插入测试数据
   */
  private  void insertTestData(String installPath, String contextPath,String dbmsName) {    
    String insertTestDataFiles = YHSysProps.getString("insertSqlTestFiles");
    if (YHUtility.isNullorEmpty(insertTestDataFiles)) {
      return;
    }
    String[] fileArray = insertTestDataFiles.split(",");
    String sqlFilePath = installPath + "\\webroot\\" + contextPath + "\\sqlfiles\\";
    for (int i = 0; i < fileArray.length; i++) {
      try {
        TDDbFileUpdater.exectSqlInfileAsWhole(sqlFilePath + fileArray[i].trim(),dbmsName);
      }catch(Exception ex) {
        log.debug(ex.getMessage(), ex);
      }
    }
  }
}
