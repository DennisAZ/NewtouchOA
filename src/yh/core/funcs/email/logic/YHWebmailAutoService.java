package yh.core.funcs.email.logic;

import java.sql.Connection;

import org.apache.log4j.Logger;

import yh.core.autorun.YHAutoRun;
import yh.core.util.YHUtility;
/**
 * 抽取外部邮件的后台服务
 * @author tulaike
 *
 */
public class YHWebmailAutoService extends YHAutoRun {
  private static final Logger log = Logger.getLogger("yzq.yh.core.funcs.email.logic.YHWebmailAutoService");

  /**
   * 抽取webEmail到邮件中心
   */
  public void doTask() {
    YHWebmailLogic wml = new YHWebmailLogic();
    try {
      wml.loadWebMail();
    } catch (Exception e) {
      log.debug(e.getMessage(),e);
    }
  }
}
