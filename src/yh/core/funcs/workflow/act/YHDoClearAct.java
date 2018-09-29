package yh.core.funcs.workflow.act;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.workflow.logic.YHConfigLogic;
import yh.core.funcs.workflow.logic.YHWorkflowSave2DataTableLogic;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHDBUtility;

public class YHDoClearAct {
  private static Logger log = Logger
    .getLogger("yh.core.funcs.workflow.act.YHDoClearAct");
  public String doPrcsClear(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String flowIdStr = request.getParameter("flowId");
      String runIdStr = request.getParameter("runId");
      String prcsIdStr = request.getParameter("prcsId");
      String flowPrcsStr = request.getParameter("flowPrcs");
      
      int runId = Integer.parseInt(runIdStr);
      int prcsId = Integer.parseInt(prcsIdStr);
      int flowId = Integer.parseInt(flowIdStr);
      int flowPrcs = Integer.parseInt(flowPrcsStr);
      doPrcsClear(dbConn ,  runId,  flowId ,  prcsId ,  flowPrcs);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "");
      request.setAttribute( YHActionKeys.RET_DATA,  "" );
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
///yh/core/funcs/workflow/act/YHDoClearAct/createSaveTable.act
  public String createSaveTable(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHWorkflowSave2DataTableLogic logic = new YHWorkflowSave2DataTableLogic();
      logic.createSaveTable(dbConn);
      //logic.convertData2Table(dbConn);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功生成表!");
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
///yh/core/funcs/workflow/act/YHDoClearAct/createSaveTableById.act?flowId=583
  public String createSaveTableById(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int flowId = Integer.parseInt(request.getParameter("flowId")); 
      YHWorkflowSave2DataTableLogic logic = new YHWorkflowSave2DataTableLogic();
      logic.createSaveTableById(dbConn , flowId);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功生成表!");
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
///yh/core/funcs/workflow/act/YHDoClearAct/createSaveTableSeq.act?tableName=FORM_DATA_743_702
  public String createSaveTableSeq(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String tableName = request.getParameter("tableName");
      YHWorkflowSave2DataTableLogic logic = new YHWorkflowSave2DataTableLogic();
      logic.createSaveTableSeq(dbConn , tableName);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "");
      request.setAttribute( YHActionKeys.RET_DATA,  "" );
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String doEndClear(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String runIdStr = request.getParameter("runId");
      int runId = Integer.parseInt(runIdStr);
      doEndClear(dbConn ,  runId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功生成表!");
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String refresh(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int runId = 9;
      int formId = 587;
      YHWorkFlowUtility ut = new YHWorkFlowUtility();
     // ut.refresh(dbConn, runId, formId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "");
      request.setAttribute( YHActionKeys.RET_DATA,  "" );
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public static void doPrcsClear(Connection conn , int runId, int flowId , int prcsId , int flowPrcs) throws Exception {
    String queryTmp = "delete from oa_fl_run_prcs where RUN_ID=" + runId  + " and PRCS_ID > " + prcsId;
    YHWorkFlowUtility.updateTableBySql(queryTmp, conn);
    String updateStr =  "update oa_fl_run_prcs set DELIVER_TIME=NULL,PRCS_FLAG='2' WHERE RUN_ID="+runId+" and PRCS_ID="+prcsId+" and FLOW_PRCS='"+flowPrcs+"' and PRCS_FLAG in ('3','4')";
    YHWorkFlowUtility.updateTableBySql(updateStr, conn);
  }
  public static void doEndClear(Connection conn , int runId) throws Exception{
    String query = "select max(a.PRCS_ID) from oa_fl_run_prcs as a where a.RUN_ID = '"+runId+"'";
    Statement stm = null;
    ResultSet rs = null;
    int prcsId = 0;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      if(rs.next()){
        prcsId = rs.getInt(1);
      }
    } catch(Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null); 
    }
    String updateFlag = "update oa_fl_run_prcs set  "
      + " PRCS_FLAG='2'"
      + " WHERE "
      + " RUN_ID='"+runId+"'  "
      + " and PRCS_ID= " + prcsId;
    YHWorkFlowUtility.updateTableBySql(updateFlag, conn);
    //更新当前主办人的转交时间为当前系统时间，仅更新当前步骤中当前主办人的记录（一条）091015
    String updateTime = "update oa_fl_run_prcs set  "
      + " DELIVER_TIME=NULL  "
      + " WHERE  "
      + " RUN_ID='" + runId + "'"
      + " and PRCS_ID=" + prcsId + " and OP_FLAG = 1";
    YHWorkFlowUtility.updateTableBySql(updateTime, conn);
  }
  public static void main(String[] args) {
    for (int i = 1 ; i <= 400 ; i++) {
      System.out.print("<input title=\"a"+i+"\" align=\"left\" id=\"DATA_"+i+"\" type=\"text\" hidden=\"0\" style=\"text-align: left;\" name=\"DATA_"+i+"\" /><br />");
    }
  }
}
