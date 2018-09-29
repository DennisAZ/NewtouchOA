package yh.core.funcs.workflow.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHPageDataList;
import yh.core.data.YHPageQueryParam;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.load.YHPageLoader;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;

public class YHWorkLogLogic {
  private static Logger log = Logger.getLogger(YHWorkLogLogic.class);

  public StringBuffer getWorklogListLogic(Connection conn,Map request) throws Exception{
    StringBuffer result = new StringBuffer();
    try {
      String sql =" SELECT " 
          + " FLOW_RUN_LOG.SEQ_ID "
          + " ,RUN_ID "
          + " ,RUN_NAME "
          + " ,PRCS_ID "
          + " ,FLOW_PRCS "
          + " ,FLOW_RUN_LOG.USER_ID "
          + " ,TIME "
          + " ,IP "
          + " ,CONTENT "
          + " ,FLOW_ID "
          + " FROM " 
          + " oa_fl_run_log as FLOW_RUN_LOG, oa_fl_type as FLOW_TYPE "
          + " WHERE FLOW_TYPE.SEQ_ID = FLOW_RUN_LOG.FLOW_ID ";
 
      String where = toSerachWhere(request);
      if(!"".equals(where)){
        sql += where;
      }
      String query = " order by TIME DESC";
      sql += query;
     
      YHPageQueryParam queryParam = (YHPageQueryParam)YHFOM.build(request,YHPageQueryParam.class,null);
      YHPageDataList pageDataList = YHPageLoader.loadPageList(conn,queryParam,sql);
      result.append(pageDataList.toJson());
    } catch (Exception e) {
      throw e;
    }
    return result;
  }
  /**
   * 得到导出数据
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public ArrayList<YHDbRecord> toExportData(Connection conn,Map request) throws Exception{
    ArrayList<YHDbRecord> result = new ArrayList<YHDbRecord>();
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String sql =" SELECT " 
          + " FLOW_RUN_LOG.SEQ_ID "
          + " ,RUN_ID "
          + " ,RUN_NAME "
          + " ,PRCS_ID "
          + " ,FLOW_PRCS "
          + " ,FLOW_RUN_LOG.USER_ID "
          + " ,PERSON.USER_NAME "
          + " ,TIME "
          + " ,IP "
          + " ,CONTENT "
          + " ,FLOW_ID "
          + " FROM " 
          + " oa_fl_run_log as FLOW_RUN_LOG , PERSON , oa_fl_type as FLOW_TYPE "
          + " WHERE FLOW_TYPE.SEQ_ID = FLOW_RUN_LOG.FLOW_ID and  FLOW_RUN_LOG.USER_ID= PERSON.SEQ_ID ";
 
      String where = toSerachWhere(request);
      if(!"".equals(where)){
        sql += where;
      }
      String query = " order by TIME DESC";
      sql += query;
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      while (rs.next()) {
        String runId = rs.getString(2);
        String runName = rs.getString(3);
        String prcsId = rs.getString(4);
        String flowPrcs = rs.getString(5);
        String userName = rs.getString(7);
        String time = YHUtility.getDateTimeStr(rs.getTimestamp(8));
        String ip = rs.getString(9);
        String content = rs.getString(10);
        String flowId = rs.getString(11);
        YHDbRecord  dbr = new YHDbRecord();
        dbr.addField("流水号", runId);
        dbr.addField("工作名/文号", runName);
        dbr.addField("步骤号", prcsId);
        dbr.addField("步骤名", getPrcsName(conn, flowId, flowPrcs));
        dbr.addField("相关人员", userName);
        dbr.addField("发生时间", time);
        dbr.addField("IP地址", ip);
        dbr.addField("内容", content);
        result.add(dbr);
      }
   
    } catch (Exception e) {
      throw e;
    }
    return result;
  }
  /**
   * 组装查询条件
   * @param request
   * @return
   * @throws Exception 
   */
  private String toSerachWhere(Map request) throws Exception{
    String whereStr = "";
    String flowId = request.get("flowId") != null ? ((String[])request.get("flowId"))[0] : null;
    String runId = request.get("runId") != null ? ((String[])request.get("runId"))[0] : null;
    String runName = request.get("runName") != null ? ((String[])request.get("runName"))[0] : null;
    String startTime = request.get("startTime") != null ? ((String[])request.get("startTime"))[0] : null;
    String endTime = request.get("endTime") != null ? ((String[])request.get("endTime"))[0] : null;
    String userId = request.get("userId") != null ? ((String[])request.get("userId"))[0] : null;
    String logType = request.get("logType") != null ? ((String[])request.get("logType"))[0] : null;
    String ipAddrss = request.get("ipAddrss") != null ? ((String[])request.get("ipAddrss"))[0] : null;
    String sortId = request.get("sortId") != null ? ((String[])request.get("sortId"))[0] : "";
    
    if(flowId != null && !"".equals(flowId)){
      whereStr +=  " and FLOW_ID=" + flowId +"";     
    }
    if (!YHUtility.isNullorEmpty(sortId)) {
      sortId = YHWorkFlowUtility.getOutOfTail(sortId);
      whereStr +=  " and FLOW_TYPE.FLOW_SORT IN (" + sortId +") ";     
    }
    // 如果流水号 不等于空   加条件
    if(!"".equals(runId) && runId != null){
      whereStr +=   " and RUN_ID=" + runId +""; 
    }
    // 如果文件名称不为空的 话   加条件
    if(!"".equals(runName) && runName != null){
      runName = runName.replace("'", "''");
      whereStr +=  " and RUN_NAME like " + "'%" + YHUtility.encodeLike(runName) + "%'"  + YHDBUtility.escapeLike() ;
    }
    // --- “日期范围”条件，对应流程实例的创建时间BEGIN_TIME ---
    if(startTime != null && !"".equals(startTime)){
      String dbDateF = YHDBUtility.getDateFilter("TIME", startTime, " >= ");
      whereStr += " and " + dbDateF;
    }
    if(endTime != null && !"".equals(endTime)){
      String dbDateF = YHDBUtility.getDateFilter("TIME", endTime, " <= ");
      whereStr += " and " + dbDateF;
    }
    if(userId != null && !"".equals(userId)){
      whereStr += " AND USER_ID= " + userId;
    }
    if(logType != null && !"".equals(logType)){
      whereStr += " AND TYPE= " + logType;
    }
    if(ipAddrss != null && !"".equals(ipAddrss)){
      ipAddrss += " AND IP= '" + ipAddrss + "'";
    }
    return whereStr;
  }
 
  /**
   * 删除工作流日志
   * @param conn
   * @param seqIds  1,2,3,4
   * @throws Exception 
   */
  public int deleteLog(Connection conn , String seqIds) throws Exception{
    int result = 0;
    String sql = "delete from oa_fl_run_log WHERE SEQ_ID in(" + seqIds + ")";
    PreparedStatement ps = null;
    try {
      ps = conn.prepareStatement(sql);
      result = ps.executeUpdate();
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
   return result;
  }
  /**
   * 删除工作流日志
   * @param conn
   * @param seqIds  1,2,3,4
   * @throws Exception 
   */
  public String getPrcsName(Connection conn , String flowId,String flowPrcs) throws Exception{
    String result = "";
    String sql = "select PRCS_NAME from oa_fl_process WHERE FLOW_SEQ_ID=" + flowId + " AND PRCS_ID=" + flowPrcs;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if(rs.next()){
        result = rs.getString(1);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, null, log);
    }
   return result;
  }
  /**
   * 得到所有日志的seqId
   * @param conn
   * @param request
   * @return
   * @throws Exception
   */
  public StringBuffer getAlldeleteSeqId(Connection conn,Map request) throws Exception{
    StringBuffer result = new StringBuffer();
    String sql =" SELECT " 
      + " FLOW_RUN_LOG.SEQ_ID "
      + " FROM " 
      + " oa_fl_run_log as FLOW_RUN_LOG, oa_fl_type as FLOW_TYPE "
          + " WHERE FLOW_TYPE.SEQ_ID = FLOW_RUN_LOG.FLOW_ID ";
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String where = toSerachWhere(request);
      if(!"".equals(where)){
        sql += where;
      }
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      int i = 0;
      while(rs.next()){
        int seqId = rs.getInt(1);
        if(!"".equals(result.toString())){
          if(i%200 == 0){
            result.append("*");
          }else{
            result.append(",");
          }
        }
        result.append(seqId);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, log);
    }
    return result;
  }
}
