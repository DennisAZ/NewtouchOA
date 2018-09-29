package yh.core.funcs.doc.act;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.funcs.doc.logic.YHConfigLogic;
import yh.core.funcs.doc.util.YHFlowRunUtility;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.db.YHDBUtility;

public class YHDoClearAct {
  private static Logger log = Logger
    .getLogger("yh.core.funcs.doc.act.YHDoClearAct");
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
    String queryTmp = "delete from "+ YHWorkFlowConst.FLOW_RUN_PRCS +" where RUN_ID=" + runId  + " and PRCS_ID > " + prcsId;
    YHWorkFlowUtility.updateTableBySql(queryTmp, conn);
    String updateStr =  "update "+ YHWorkFlowConst.FLOW_RUN_PRCS +" set DELIVER_TIME=NULL,PRCS_FLAG='2' WHERE RUN_ID="+runId+" and PRCS_ID="+prcsId+" and FLOW_PRCS='"+flowPrcs+"' and PRCS_FLAG in ('3','4')";
    YHWorkFlowUtility.updateTableBySql(updateStr, conn);
  }
  public static void doEndClear(Connection conn , int runId) throws Exception{
    String query = "select max(a.PRCS_ID) from "+ YHWorkFlowConst.FLOW_RUN_PRCS +" as a where a.RUN_ID = '"+runId+"'";
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
    String updateFlag = "update "+ YHWorkFlowConst.FLOW_RUN_PRCS +" set  "
      + " PRCS_FLAG='2'"
      + " WHERE "
      + " RUN_ID='"+runId+"'  "
      + " and PRCS_ID= " + prcsId;
    YHWorkFlowUtility.updateTableBySql(updateFlag, conn);
    //更新当前主办人的转交时间为当前系统时间，仅更新当前步骤中当前主办人的记录（一条）091015
    String updateTime = "update "+ YHWorkFlowConst.FLOW_RUN_PRCS +" set  "
      + " DELIVER_TIME=NULL  "
      + " WHERE  "
      + " RUN_ID='" + runId + "'"
      + " and PRCS_ID=" + prcsId + " and OP_FLAG = 1";
    YHWorkFlowUtility.updateTableBySql(updateTime, conn);
  }
  public static void main(String[] args) {
    Connection dbConn = null;
    try {
      Class.forName("com.mysql.jdbc.Driver");
      dbConn = DriverManager.getConnection("jdbc:mysql://localhost:3396/yh?characterEncoding=UTF8","root" , "myoa888");
      //doPrcsClear(dbConn ,  79,  568 ,  1 ,  1);
      doEndClear(dbConn , 79);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        dbConn.close();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }
  public String clearTable(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      for (String t : YHWorkFlowConst.T) {
        if (!"DOC_RUN_SEQ_ID".equals(t)) {
          String sql = "delete from " + t;
          yh.core.funcs.doc.util.YHWorkFlowUtility.updateTableBySql(sql, dbConn);
        }
      }
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
