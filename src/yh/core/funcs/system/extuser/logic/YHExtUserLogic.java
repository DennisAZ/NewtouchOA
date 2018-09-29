package yh.core.funcs.system.extuser.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import yh.core.funcs.system.extuser.data.YHExtUser;
import yh.core.util.db.YHDBUtility;

public class YHExtUserLogic {
  private static Logger log = Logger.getLogger(YHExtUserLogic.class);

  public boolean existsTableNo(Connection dbConn, String userId)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      String sql = "SELECT count(*) FROM oa_person_ext WHERE USER_ID = '" + userId
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

  public ArrayList<YHExtUser> getExtUser(Connection dbConn) throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    YHExtUser extUser = null;
    List list = new ArrayList();
    ArrayList<YHExtUser> extList = new ArrayList<YHExtUser>();
    try {
      stmt = dbConn.createStatement();
      String sql = "select SEQ_ID, USER_ID, PASSWORD, AUTH_MODULE, POSTFIX, USE_FLAG, SYS_USER, REMARK from oa_person_ext WHERE SYS_USER='0' order by USER_ID";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        extUser = new YHExtUser();
        extUser.setSeqId(rs.getInt("SEQ_ID"));
        extUser.setUserId(rs.getString("USER_ID"));
        extUser.setPassword(rs.getString("PASSWORD"));
        extUser.setAuthModule(rs.getString("AUTH_MODULE"));
        extUser.setPostfix(rs.getString("POSTFIX"));
        extUser.setUseFlag(rs.getString("USE_FLAG"));
        extUser.setRemark(rs.getString("REMARK"));
        extUser.setSysUser(rs.getString("SYS_USER"));
        extList.add(extUser);
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stmt, rs, log);
    }
    return extList;
  }

  public void deleteAll(Connection dbConn, int seqId) throws Exception {
    String sql = "DELETE FROM oa_person_ext WHERE SEQ_ID=" + seqId
        + " AND SYS_USER='0'";
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
}
