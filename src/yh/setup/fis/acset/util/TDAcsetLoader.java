package yh.setup.fis.acset.util;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import yh.core.util.db.YHDBUtility;
import yh.setup.fis.acset.data.TDAcset;
import yh.setup.fis.acset.global.TDAcsetConst;

/**
 * 帐套装载
 * @author cly
 * @version 1.0
 * @date 2006-10-18
 */
public class TDAcsetLoader {
  /**
   * log
   */
  private static final Logger log = Logger.getLogger(
      "chly.com.td.fis.acset.dataload.TDAcsetLoader");
  
  
  public static TDAcset loadAcset(
      Connection dbConn,
      int seqId) throws Exception {
      TDAcset acset = null;
      Statement stmt = null;
      ResultSet rs = null;
      try {
        stmt = dbConn.createStatement();
        String sql = "select "
          + "isnull(SEQ_ID, 0), "
          + "isnull(ORG_SEQ_ID, ''), "
          + "isnull(ACSET_NO, ''), "
          + "isnull(ACSET_DB_NO, ''), "
          + "isnull(ACSET_DESC, ''), "
          + "isnull(TAX_NO, ''), "
          + "isnull(USED_FLAG, ''), "
          + "isnull(MAKE_AC_YM, ''), "
          + "isnull(P_ACCT_YEAR, ''), "
          + "isnull(ACCT_YM, '') "
         
          + " from ACCOUNT_SET "
          + " where seq_id = " + seqId
          + " order by ACSET_NO";
        rs = stmt.executeQuery(sql);

        if (rs.next()) {
          acset = new TDAcset();
          acset.setSeqId(rs.getInt(1));
          acset.setOrgSeqId(rs.getInt(2));
          acset.setAcsetNo(rs.getString(3));
          acset.setAcsetDbNo(rs.getString(4));
          acset.setAcsetDesc(rs.getString(5));
          acset.setTaxNo(rs.getString(6));
          acset.setUsedFlag(rs.getString(7));
          acset.setMakeAcYm(rs.getString(8));
          acset.setPAcctYear(rs.getString(9));
          acset.setAcctYm(rs.getString(10));
        }
      }catch(Exception ex) {
        throw ex;
      }finally {
        YHDBUtility.close(stmt, rs, log);
      }
      return acset;
    }
  

  public static TDAcset loadAcset(
      Connection dbConn,
      String acsetNo) throws Exception {
      TDAcset acset = null;
      Statement stmt = null;
      ResultSet rs = null;
      try {
        stmt = dbConn.createStatement();
        String sql = "select "
          + "isnull(SEQ_ID, 0), "
          + "isnull(ORG_SEQ_ID, ''), "
          + "isnull(ACSET_NO, ''), "
          + "isnull(ACSET_DB_NO, ''), "
          + "isnull(ACSET_DESC, ''), "
          + "isnull(TAX_NO, ''), "
          + "isnull(USED_FLAG, ''), "
          + "isnull(MAKE_AC_YM, ''), "
          + "isnull(P_ACCT_YEAR, ''), "
          + "isnull(ACCT_YM, '') "
         
          + " from ACCOUNT_SET "
          + " where ACSET_NO = '" + acsetNo + "'"
          + " order by ACSET_NO";
        rs = stmt.executeQuery(sql);

        if (rs.next()) {
          acset = new TDAcset();
          acset.setSeqId(rs.getInt(1));
          acset.setOrgSeqId(rs.getInt(2));
          acset.setAcsetNo(rs.getString(3));
          acset.setAcsetDbNo(rs.getString(4));
          acset.setAcsetDesc(rs.getString(5));
          acset.setTaxNo(rs.getString(6));
          acset.setUsedFlag(rs.getString(7));
          acset.setMakeAcYm(rs.getString(8));
          acset.setPAcctYear(rs.getString(9));
          acset.setAcctYm(rs.getString(10));
        }
      }catch(Exception ex) {
        throw ex;
      }finally {
        YHDBUtility.close(stmt, rs, log);
      }
      return acset;
    }
  /**
   * 取得当前账套的个数
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static int getAcsetCnt(Connection dbConn) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      String sql = "select count(SEQ_ID) from ACCOUNT_SET";

      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        return rs.getInt(1);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return 0;
  }
  
  /**
   * 加载账套列表
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static ArrayList loadAcsetList(Connection dbConn) throws Exception {

    ArrayList acsetList = new ArrayList();

    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      String sql = "select " + "isnull(SEQ_ID, 0), "
          + "isnull(ORG_SEQ_ID, ''), " + "isnull(ACSET_NO, ''), "
          + "isnull(ACSET_DB_NO, ''), " + "isnull(ACSET_DESC, ''), "
          + "isnull(TAX_NO, ''), " + "isnull(USED_FLAG, ''), "
          + "isnull(MAKE_AC_YM, ''), " + "isnull(P_ACCT_YEAR, ''), "
          + "isnull(ACCT_YM, '') " + " from ACCOUNT_SET "
          + " order by ACSET_NO";

      rs = stmt.executeQuery(sql);
      TDAcset acset = null;
      while (rs.next()) {
        acset = new TDAcset();
        acset.setSeqId(rs.getInt(1));
        acset.setOrgSeqId(rs.getInt(2));
        acset.setAcsetNo(rs.getString(3));
        acset.setAcsetDbNo(rs.getString(4));
        acset.setAcsetDesc(rs.getString(5));
        acset.setTaxNo(rs.getString(6));
        acset.setUsedFlag(rs.getString(7));
        acset.setMakeAcYm(rs.getString(8));
        acset.setPAcctYear(rs.getString(9));
        acset.setAcctYm(rs.getString(10));

        acsetList.add(acset);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return acsetList;
  }
  

  public static void delAcset(
      Connection dbConn,
      String acsetNo ) throws Exception {      
      ArrayList acsetList = new ArrayList();
      Statement stmt = null;
      ResultSet rs = null;
      try {
        stmt = dbConn.createStatement();
        String sql = "delete from ACCOUNT_SET where acset_no = '" + acsetNo + "'";
//        System.out.print(sql);
//        if (log.isDebugEnabled()) {
//          log.debug(sql);
//        }
        stmt.execute(sql);
      }catch(Exception ex) {
//        if (log.isDebugEnabled()) {
//          log.debug(ex.toString());
//        }
        throw ex;
      }finally {
        YHDBUtility.close(stmt, rs, log);
      }
    }
  
  public static void delUserAcset(
      Connection dbConn,
      String acsetNo ) throws Exception {      
      ArrayList acsetList = new ArrayList();
      Statement stmt = null;
      ResultSet rs = null;
      try {
        stmt = dbConn.createStatement();
        String sql = "delete from User_ACSET where acset_no = '" + acsetNo + "'";
//        System.out.print(sql);
//        if (log.isDebugEnabled()) {
//          log.debug(sql);
//        }
        stmt.execute(sql);
      }catch(Exception ex) {
//        if (log.isDebugEnabled()) {
//          log.debug(ex.toString());
//        }
        throw ex;
      }finally {
        YHDBUtility.close(stmt, rs, log);
      }
    }
  
  
  public static void delSysDatabase(
    Connection dbConn,
    String dbNo ) throws Exception {      
    ArrayList acsetList = new ArrayList();
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      String sql = "delete from sys_databases where db_no = '" + dbNo + "'"  ;

      stmt.execute(sql);
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  /**
   * 加载账套期初关闭标记
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static String loadAcsetCloseFlag(Connection dbConn) throws Exception {      
    String rtFlag = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      String sql = null;      
      //处理老账套兼容
      if (!YHDBUtility.existsTable(dbConn, "ACSETPROPS")) {
        sql = "select count(SEQ_ID) from VOUCHER";
        rs = stmt.executeQuery(sql);
        int vouchCnt = 0;
        if (rs.next()) {
          vouchCnt = rs.getInt(1);
        }
        //已经存在凭证，则创建表，同时关闭期初录入
        if (vouchCnt > 0) {
          TDAcsetBuilder.closeInit(dbConn);
        }else {
          return "0";
        }
      }
      sql = "select PROP_VALUE from ACSETPROPS where PROP_KEY='" + TDAcsetConst.TDFIS_CLOSE_INIT + "'";
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        return rs.getString(1);
      }else {
        return "0";
      }
    }catch(Exception ex) {
      throw ex;
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
  
  /**
   * 加载账套版本
   * @param dbConn
   * @return
   * @throws Exception
   */
  public static String loadAcsetVersion(Connection dbConn) throws Exception {      
    String rtFlag = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      String sql = "select VERSION_DESC from VERSION_LOG";
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        return rs.getString(1);
      }else {
        return rs.getString("版本未知");
      }
    }catch(Exception ex) {
      ex.printStackTrace();
      return rs.getString("版本未知");
    }finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }
}
