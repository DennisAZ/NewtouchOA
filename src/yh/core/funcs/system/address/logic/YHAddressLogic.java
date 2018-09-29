package yh.core.funcs.system.address.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import yh.core.util.db.YHDBUtility;

public class YHAddressLogic {
  private static Logger log = Logger.getLogger(YHAddressLogic.class);

  /**
   * 判断是否有重复的分组名称  
   * @param dbConn
   * @param groupName  分组名称
   * @return
   * @throws Exception
   */
  public boolean existsGroupName(Connection dbConn, String groupName)
      throws Exception {
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = dbConn.createStatement();
      String sql = "SELECT count(*) FROM oa_address_team WHERE GROUP_NAME = '"
          + groupName + "'";
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

  public boolean existsGroupId(Connection dbConn, int groupId, String psnName)
  throws Exception {
    long count = 0;
    String sql = "SELECT count(*) FROM oa_address WHERE GROUP_ID = " + groupId + " AND PSN_NAME = '" +psnName+"'";
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = dbConn.prepareStatement(sql);
      rs = ps.executeQuery();
      //System.out.println(sql);
      if (rs.next()) {
        count = rs.getLong(1);
      }
      if (count >= 1) {
        return true;
      } else {
        return false;
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
  }
  
  public int getGroupSeqIdLogic(Connection conn , int groupId, String psnName) throws Exception{
    int seqId = 0;
    String sql = "SELECT SEQ_ID FROM oa_address WHERE GROUP_ID = " + groupId + " AND PSN_NAME = '" +psnName+"'";
    PreparedStatement ps = null;
    ResultSet rs = null ;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        seqId = rs.getInt(1);
       
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return seqId;
  }
}
