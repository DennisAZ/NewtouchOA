package yh.setup.fis.acset.data;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;



import org.apache.log4j.Logger;

import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.file.YHFileUtility;
import yh.setup.fis.acset.global.TDAcsetConst;
import yh.setup.fis.acset.util.TDAcsetBuilder;
import yh.setup.fis.acset.util.TDPeriodSave;
import yh.setup.util.YHERPSetupUitl;


/**
 * 
 * @author  cly
 * @date    2006-8-16
 * @duty   
 * 
 *
 */


public class TDAcsetCreateDatabase {
  /**
   * log
   */
  private static final Logger log = Logger.getLogger(
      "chly.com.td.acset.data.TDAcsetCreateDatabase");
  
  /**
   * 创建账套库后一系列的配置工作
   * @param sysDbConn
   * @param accountDbDesc
   * @param acsetNo
   * @param accountDbName
   * @param deptId
   * @param makeYM
   * @param acctYear
   * @param startYM
   * @param periodCntrl
   * @param contextPath
   * @param firstPeriod
   * @param homeCurrDesc
   * @param homeCurrSign
   * @throws Exception
   */
  private static void doAcsetConfig(Connection sysDbConn,
      String accountDbDesc,
      String reptAcsetNo,
      String acsetNo,
      String accountDbName,
      int deptId,
      String makeYM,
      String acctYear,
      String startYM,
      String periodCntrl,
      String contextPath,
      int firstPeriod,
      String homeCurrDesc,
      String homeCurrSign) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = sysDbConn.createStatement();
      //找最大的SYS_DATABASE 的 DB_NO
      String sql = "select max(DB_NO) from SYS_DATABASES where left(DB_NO, 1)='0'";   
      rs = stmt.executeQuery(sql);
      
      String dbNo = "";
      if (rs.next()) {
        if(rs.getString(1) == null) {
          dbNo = "000";
        }else {       
          dbNo = YHUtility.getNextNum(rs.getString(1), 1, 3);
        }
      }
      
      //再将建立的库名称写入TDSYS的 SYS_DATABASE
      sql = "insert into  SYS_DATABASES(DB_NO,DB_NAME,DB_DESC,DS_NAME,DBMS_NAME) values " +
          "('" + dbNo + "'," +
          "'" + accountDbName + "'," +
          "'" + accountDbDesc+ "'," +
          "'mssql/acset/" + accountDbName + "'," +
          "'sqlserver')";
  
      stmt.execute(sql);   
      
      //再将建立的库名称写入TDSYS的 ACCOUNT_SET
      sql = "insert into  ACCOUNT_SET(ORG_SEQ_ID, ACSET_NO, ACSET_DB_NO, ACSET_DESC, USED_FLAG, MAKE_AC_YM, P_ACCT_YEAR, ACCT_YM ) values " +
      // **********需要增加一个部门选择 
          "("+deptId+"," +
          "'"+acsetNo+"'," +
          "'"+dbNo+"'," +
          "'"+accountDbDesc+"'," +
                  "'0'," +
                  "'" + makeYM + "' ," +
                  "'" + acctYear + "' ," +
                  "'" + startYM + "')";
           
      stmt.execute(sql);
     
      //=注册数据库，为了财务参数设置的需要。
      //TDCDataSources.addDatabase(database);
      //TDCDataSources.registerDataSource(database);
      
      //注册新建帐套
      TDAcset acset = new TDAcset();
  
      acset.setOrgSeqId(deptId);
      acset.setAcsetNo(acsetNo);
      acset.setAcsetDbNo(dbNo);
      acset.setAcsetDesc(accountDbDesc);
      acset.setTaxNo("");
      acset.setUsedFlag("0");
      acset.setMakeAcYm(makeYM);
      acset.setPAcctYear(acctYear);
      acset.setAcctYm(startYM);
      
      //YHDataSources.addAcset(acset);
      
      if (YHUtility.isNullorEmpty(periodCntrl)) {
        return;
      }
      
      //设置会计期间。需要该数据库的连接
      TDPeriodSave.saveAccoutPeriod(stmt, periodCntrl  , acctYear, startYM, accountDbName);
  
      //更改所有的有acct_Year字段的表
      String tables = YHSysProps.getProp("acctYearTables");
      TDAcsetBuilder.updateAcctYearTables(stmt, accountDbName, tables, acctYear);
      
      String reptPath = contextPath + "templet\\";
      YHFileUtility.copyDir(reptPath + "init", reptPath + acsetNo);
      TDAcsetBuilder.updateReptTemptPaths(stmt, accountDbName, reptAcsetNo, acsetNo, contextPath.substring(0, contextPath.length() - 1));
      
      //处理起始期间设置
      if (firstPeriod > 1) {
        sql = "update " + accountDbName + ".dbo.ACCOUNTPERIOD set CLOSE_DATE=GETDATE(), CLOSE_USER='" + TDAcsetConst.TDFIS_CLOSE_USER + "' WHERE PERIOD_YEAR='" + acctYear + "' AND PERIOD_NUM<" + firstPeriod;
        stmt.executeUpdate(sql);
        sql = "select convert(char(10), START_DATE, 20) from " + accountDbName + ".dbo.ACCOUNTPERIOD where PERIOD_YEAR='" + acctYear + "' and PERIOD_NUM=" + firstPeriod;
        rs = stmt.executeQuery(sql);
        String acctYM = null;
        if (rs.next()) {
          acctYM = rs.getString(1).substring(0, 7);
        }
        sql = "UPDATE ACCOUNT_SET set P_ACCT_YEAR='" + acctYear + "', ACCT_YM='" + acctYM + "' where ACSET_NO='" + acsetNo + "'";
        stmt.executeUpdate(sql);
      }
      if (!homeCurrDesc.equals("人民币") || !homeCurrSign.equals("￥")) {
        //修改本位币
        sql = "update " + accountDbName + ".dbo.CURRENCY set CURR_DESC='" + homeCurrDesc + "', CURR_SIGN='" + homeCurrSign + "' where SEQ_ID=1";
        stmt.executeUpdate(sql);
        //删除其他同名币种
        sql = "delete " + accountDbName + ".dbo.CURRENCY where SEQ_ID<>1 and CURR_DESC='" + homeCurrDesc + "'";
        stmt.executeUpdate(sql);
        //确保币种中存在人民币
        sql = "insert into " + accountDbName + ".dbo.CURRENCY (MEMO_CODE,CURR_DESC,CURR_SIGN,USED_FLAG)"
          + " values ('RMB', '人民币', '￥', '0')";
        stmt.executeUpdate(sql);
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  /**
   * 取得下一个账套的编码
   * @param sysDbConn
   * @return
   */
  private static String getNextAcsetNo(Connection sysDbConn) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      //首先从 TDSYS 的 ACCOUNT_SET里面找到当前的最大的库名字 TD**，然后增加1，建立新库。
      stmt = sysDbConn.createStatement();
      String sql = "select max(ACSET_NO) from ACCOUNT_SET";
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        if(rs.getString(1) == null) {
          return "01";
        }else {
          return YHUtility.getNextNum(rs.getString(1), 1, 2);
        }
      }else {
        return "01";
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }

  /**
   * 创建系统数据库
   * @param sysDbConn
   * @param sysParams
   * @param acsetParam
   * @return
   * @throws Exception
   */
  public static boolean createDatabase(
      Connection sysDbConn,
      TDCSystemParams sysParams,
      TDAcsetBuildParam acsetParam,String dbmsName) throws Exception {
    
    String accountDbDesc = acsetParam.getAccountDbDesc();
    String periodCntrl = acsetParam.getPeriodCntrl();
    String makeYM = acsetParam.getMakeYM();
    String acctYear = acsetParam.getAcctYear();
    String startYM = acsetParam.getStartYM();
    String deptId_String = acsetParam.getDeptId();
    String contextPath = acsetParam.getContextPath();
    int rateLength = acsetParam.getRateLength();
    int rateDecimalLength = acsetParam.getRateDecimalLength();
    int moneyLength = acsetParam.getMoneyLength();
    int moneyDecimalLength = acsetParam.getMoneyDecimalLength();
    String homeCurrDesc = acsetParam.getHomeCurrDesc();
    String homeCurrSign = acsetParam.getHomeCurrSign();
    int firstPeriod = acsetParam.getFirstPeriod();

    String accountDbName = null;
    String acsetNo = null;    
    int deptId = 0;
    if(YHUtility.isNullorEmpty(deptId_String)) {
      deptId_String = "0";
    }
    deptId = 0; //Integer.parseInt(deptId_String);
    
    Statement stmt = null;
    ResultSet rs = null;
    try {
      //首先从 TDSYS 的 ACCOUNT_SET里面找到当前的最大的库名字 TD**，然后增加1，建立新库。
      stmt = sysDbConn.createStatement();
      acsetNo = getNextAcsetNo(sysDbConn);
      accountDbName = TDAcsetConst.getAcsetDbPrefix() + acsetNo;
      if (YHERPSetupUitl.isDbActive(accountDbName,dbmsName)) {
        try {
          YHERPSetupUitl.backupDb(accountDbName,"installbackup",dbmsName);
        }catch(Exception ex) {          
        }
        try {
          YHERPSetupUitl.forceDropDb(accountDbName,dbmsName);
        }catch(Exception ex) {          
        }
      }
      //再从file里面读取各个sql语句。包括 建立表，建立存储过程，建立函数。
      TDAcsetBuilder.executionSQLFile(
          accountDbName,
          contextPath,
          acctYear,
          rateLength,
          rateDecimalLength,
          moneyLength,
          moneyDecimalLength,
          dbmsName);
      
      doAcsetConfig(sysDbConn,
          accountDbDesc,
          "01",
          acsetNo,
          accountDbName,
          deptId,
          makeYM,
          acctYear,
          startYM,
          periodCntrl,
          contextPath,
          firstPeriod,
          homeCurrDesc,
          homeCurrSign);
      
      sysDbConn.commit();
      return true;
    }catch(Exception ex) {
      YHERPSetupUitl.forceDropDb(accountDbName,dbmsName);
      try {
        if (sysDbConn != null && !sysDbConn.getAutoCommit()) {
          sysDbConn.rollback();
        }
      }catch(Exception ex2) {        
      }
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
}
