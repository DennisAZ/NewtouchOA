package yh.core.funcs.system.censorwords.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import yh.core.util.YHUtility;
import yh.core.funcs.system.censorwords.data.YHCensorModule;
import yh.core.util.db.YHDBUtility;

public class YHCensorModuleLogic {
  private static Logger log = Logger.getLogger(YHCensorModuleLogic.class);

  public boolean existsCensorModule(Connection dbConn, String moduleCode)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      String sql = "SELECT count(*) FROM oa_examine_module WHERE MODULE_CODE = '" + moduleCode
          + "'";
      rs = stmt.executeQuery(sql);
      long count = 0;
      if (rs.next()) {
        count = rs.getLong(1);
      }
      if (count == 1) {
        return true;
      } else {
        return false;
      }

    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }

  public long existsCount(Connection dbConn, int userId) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    long count = 0;
    try {
      stmt = dbConn.createStatement();
      String sql = "SELECT count(*) FROM oa_person_ext WHERE SYS_USER='0'";
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        count = rs.getLong(1);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return count;
  }

  public ArrayList<YHCensorModule> getCensorModule(Connection dbConn)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHCensorModule censorModule = null;
    List list = new ArrayList();
    ArrayList<YHCensorModule> moduleList = new ArrayList<YHCensorModule>();
    try {
      stmt = dbConn.createStatement();
      String sql = "select SEQ_ID, MODULE_CODE, USE_FLAG, CHECK_USER, SMS_REMIND, SMS2_REMIND, BANNED_HINT, MOD_HINT, FILTER_HINT from oa_examine_module order by MODULE_CODE";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        censorModule = new YHCensorModule();
        censorModule.setSeqId(rs.getInt("SEQ_ID"));
        censorModule.setModuleCode(rs.getString("MODULE_CODE"));
        censorModule.setUseFlag(rs.getString("USE_FLAG"));
        censorModule.setCheckUser(rs.getString("CHECK_USER"));
        censorModule.setSmsRemind(rs.getString("SMS_REMIND"));
        censorModule.setSms2Remind(rs.getString("SMS2_REMIND"));
        censorModule.setBannedHint(rs.getString("BANNED_HINT"));
        censorModule.setModHint(rs.getString("MOD_HINT"));
        censorModule.setFilterHint(rs.getString("FILTER_HINT"));
        moduleList.add(censorModule);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return moduleList;
  }

  public void deleteAll(Connection dbConn, String seqId) throws Exception {

    String sql = "DELETE FROM oa_examine_module WHERE SEQ_ID IN (" + seqId + ")";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }

  public void updateSingleWords(Connection dbConn, String find,
      String replacement) throws Exception {

    String sql = "update oa_examine_words set REPLACEMENT='" + replacement
        + "' WHERE FIND='" + find + "'";
    //System.out.println(sql);
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      stmt.executeUpdate(sql);
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }

  public void deleteAllFast(Connection dbConn, int userId) throws Exception {
    String sql = "DELETE FROM oa_examine_words WHERE USER_ID=" + userId;
    //System.out.println(sql);
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }

  public void deleteAllWords(Connection dbConn) throws Exception {
    String sql = "DELETE FROM oa_examine_words";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      rs = stmt.executeQuery(sql);
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
  }

  public String getUserId(Connection conn, String idStrs) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    String userId = "";
    try {
      if(YHUtility.isNullorEmpty(idStrs)){
        userId = "" + ",";
      }else{
      String[] ids = idStrs.split(",");
      for (int i = 0; i < ids.length; i++) {
        stmt = conn.createStatement();
        String queryStr = "select USER_NAME from PERSON where SEQ_ID = "
            + Integer.parseInt(ids[i]);
        rs = stmt.executeQuery(queryStr);
        while (rs.next()) {
          userId += rs.getString("USER_NAME") + ",";
        }
      }
    }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return userId;
  }
}
