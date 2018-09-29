package yh.core.funcs.doc.util;
import java.sql.Connection;

import org.apache.log4j.Logger;

import yh.core.autorun.YHAutoRun;
import yh.core.util.YHUtility;
/**
 * 工作流的后台服务

 * @author liuhan
 *
 */
public class YHWorkFlowAutoService extends YHAutoRun {
  private static final Logger log = Logger.getLogger("yh.core.funcs.doc.util.YHWorkFlowAutoService");

  /**
   *  设置工作超时标志

   */
  public void doTask() {
    //System.out.println("YHWorkFlowAutoService doTask Run " + YHUtility.getCurDateTimeStr());
    try {
     // requestDbConn = new YHRequestDbConn(acsetDbNo);
      Connection conn = getRequestDbConn().getSysDbConn();
      YHFlowRunUtility util = new YHFlowRunUtility();
      util.setTimeOutFlag(conn, "/yh");
    } catch (Exception e) {
      e.printStackTrace();
      log.debug(e.getMessage(),e);
    }
    //System.out.println("YHWorkFlowAutoService doTask Run END " + YHUtility.getCurDateTimeStr());
  }
}
