package yh.core.autorun;

import yh.core.util.YHUtility;

public class YHWorkFlowRemind extends YHAutoRun {
  /**
   * 抽取文件信息到文件中心
   */
  public void doTask() {
    System.out.println("YHWorkFlowRemind doTask Run" + YHUtility.getCurDateTimeStr());
  }
}
