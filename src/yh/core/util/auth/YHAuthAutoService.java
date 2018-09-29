package yh.core.util.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import yh.core.autorun.YHAutoRun;
import yh.core.global.YHRegistProps;
import yh.core.util.db.YHDBUtility;

public class YHAuthAutoService extends YHAutoRun {
  private static final Logger log = Logger.getLogger("yzq.yh.core.util.auth.YHAuthAutoService");

  public YHAuthAutoService() {
    setIntervalSeconds(60 * 60 * 3);
    setPause(false);
  }
  /**
   * 更新注册信息
   */
  public void doTask() {
    PreparedStatement ps = null;
    try {
      Connection conn = getRequestDbConn().getSysDbConn();
      int userCnt = YHRegistProps.getInt("im.userCnt.yh");
      if (userCnt <= 0) {
        userCnt = 30;
      }
      
      String sql = null;
      
      if (this.hasProperty(conn, "IM_USER_CNT")) {
        sql = "update SYS_PARA" +
        " set PARA_VALUE = ?" +
        " where PARA_NAME = 'IM_USER_CNT'";
      }
      else {
        sql = "insert into SYS_PARA(PARA_NAME, PARA_VALUE) values('IM_USER_CNT', ?)";
      }
      
      ps = conn.prepareStatement(sql);
      ps.setInt(1, userCnt);
      ps.executeUpdate();
    } catch (Exception e) {
      e.printStackTrace();
      log.debug(e.getMessage(), e);
    } finally {
      YHDBUtility.close(ps, null, log);
    }
  }
  
  private boolean hasProperty(Connection dbConn, String name) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String sql = "select count(1) AMOUNT" +
      		" from SYS_PARA" +
          " where PARA_NAME = ?";
      ps = dbConn.prepareStatement(sql);
      ps.setString(1, name);
      rs = ps.executeQuery();
      if (rs.next()) {
        return rs.getInt("AMOUNT") > 0;
      }
    } catch (Exception e) {
      e.printStackTrace();
      log.debug(e.getMessage(), e);
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return false;
  }
}
