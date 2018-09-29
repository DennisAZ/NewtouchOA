package yh.plugins.workflow;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.workflow.data.YHFlowRunData;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHIWFPlugin;
import yh.core.global.YHBeanKeys;
import yh.core.util.YHUtility;
import yh.subsys.inforesouce.docmgr.logic.YHDocReceiveLogic;

public class YHDocReciveAfterPlugin implements YHIWFPlugin{

  public String after(HttpServletRequest request, HttpServletResponse response)
      throws Exception{
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection conn = requestDbConn.getSysDbConn();
      String flowIdStr = request.getParameter("flowId");
      String runIdStr = request.getParameter("runId");
      String prcsIdStr = request.getParameter("prcsId");
      String flowPrcsStr = request.getParameter("flowPrcs");
      int runId = Integer.parseInt(runIdStr);
      int prcsId = Integer.parseInt(prcsIdStr);
      int flowId = Integer.parseInt(flowIdStr);
      int flowPrcs = Integer.parseInt(flowPrcsStr);
      YHFlowRunUtility wf = new YHFlowRunUtility();
      YHFlowRunData rd6 =  wf.getFlowRunData(conn, flowId, runId, "收文ID");
      YHFlowRunData rd7 =  wf.getFlowRunData(conn, flowId, runId, "行文类型ID");
      String id= rd6.getItemData();
      String docId = rd7.getItemData();
      YHDocReceiveLogic logic = new YHDocReceiveLogic();
      if (!YHUtility.isNullorEmpty(id) && !YHUtility.isNullorEmpty(docId)) {
        logic.updateDocReceive(conn, Integer.parseInt(id), String.valueOf(runId), Integer.parseInt(docId));
      }
    
    } catch(Exception ex) {
      throw ex;
    }
    return null;
  }

  public String before(HttpServletRequest request, HttpServletResponse response)
      throws Exception{
    
    return null;
  }

}
