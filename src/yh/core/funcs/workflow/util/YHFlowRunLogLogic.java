package yh.core.funcs.workflow.util;

import java.sql.Connection;
import java.util.Date;

import yh.core.funcs.workflow.data.YHFlowRun;
import yh.core.funcs.workflow.data.YHFlowRunLog;
import yh.core.util.db.YHORM;

public class YHFlowRunLogLogic {
  /**
   * 
   * 保存日志
   */
  public void saveLog(YHFlowRunLog runLog , Connection conn) throws Exception{
    YHORM orm = new YHORM();
    orm.saveSingle(conn, runLog);
  }
  /**
   * 写入日志
   * @param runId
   * @param prcsId
   * @param flowPrcs
   * @param userId
   * @param logType
   * @param content
   * @param ip
   * @throws Exception
   */
  public void runLog(int runId , int prcsId , int flowPrcs , int userId , int logType , String content,String ip ,Connection conn) throws Exception{
    YHFlowRunLog runLog = new YHFlowRunLog();
    runLog.setRunId(runId);
    runLog.setPrcsId(prcsId);
    runLog.setFlowPrcs(flowPrcs);
    runLog.setUserId(userId);
    runLog.setType(logType);
    runLog.setContent(content);
    runLog.setIp(ip);
    runLog.setTime(new Date());
    YHFlowRunLogic runLogic = new YHFlowRunLogic();
    YHFlowRun flowRun = runLogic.getFlowRunByRunId(runId ,conn);
    if(flowRun != null){
      runLog.setRunName(flowRun.getRunName());
      runLog.setFlowId(flowRun.getFlowId());
    }
    this.saveLog(runLog , conn);
  }
}
