package yh.core.funcs.dept.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.log4j.Logger;
import yh.core.util.db.YHDBUtility;

public class YHUserGroupLogic {
  private static Logger log = Logger.getLogger(YHUserGroupLogic.class);
 
  /**
   * 删除公共自定义组记录
   * @param conn
   * @param seqId
   * @throws Exception
   */
  
  public void deleteUserGroup(Connection conn, String seqId) throws Exception {
    String sql = "DELETE FROM oa_person_group WHERE SEQ_ID IN(" + seqId + ")";
    PreparedStatement pstmt = null;
    try {
      pstmt = conn.prepareStatement(sql);
      pstmt.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(pstmt, null, null);
    }
  }
}
