package yh.core.funcs.workflow.util;
import java.sql.Connection;

import org.apache.log4j.Logger;

import yh.core.autorun.YHAutoRun;
import yh.core.util.db.YHDBUtility;
/**
 * 工作流的后台服务
 * @author liuhan
 *
 */
public class YHFlowTimerAutoService extends YHAutoRun {
  private static final Logger log = Logger.getLogger("yh.core.funcs.workflow.util.YHFlowTimerAutoService");

  public void doTask() {
    Connection conn = null;
    try {
      conn = getRequestDbConn().getSysDbConn();
      YHFlowTimerLogic util = new YHFlowTimerLogic();
      util.timeRun(conn);
    } catch (Exception e) {
      log.debug(e.getMessage(),e);
    } finally {
      YHDBUtility.closeDbConn(conn, null);
    }
  }
}
