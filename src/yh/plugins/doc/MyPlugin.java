package yh.plugins.doc;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.doc.data.YHDocFlowRunData;
import yh.core.funcs.doc.util.YHFlowRunUtility;
import yh.core.funcs.doc.util.YHIWFPlugin;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.global.YHBeanKeys;

/**
 * 工作流插件接口
 * @author jpt
 *
 */
public class MyPlugin implements YHIWFPlugin{
  /**
   * 节点执行前执行
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String before(HttpServletRequest request, HttpServletResponse response) throws Exception {
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection conn = requestDbConn.getSysDbConn();
      //流程id
      String flowIdStr = request.getParameter("flowId");
      //工作id即流水号
      String runIdStr = request.getParameter("runId");
      //实际步骤号,即现在执行到几步
      String prcsIdStr = request.getParameter("prcsId");
      //设计步骤号,即现在执行所在的流程步骤
      String flowPrcsStr = request.getParameter("flowPrcs");
      //用户选择的步骤号(不是实际步骤号),这时步骤号是以逗号分割的(并发时用户可能先多个步骤)，如：2,
      String prcsChoose = request.getParameter("prcsChoose");
      int runId = Integer.parseInt(runIdStr);
      int prcsId = Integer.parseInt(prcsIdStr);
      int flowId = Integer.parseInt(flowIdStr);
      int flowPrcs = Integer.parseInt(flowPrcsStr);
     
      YHFlowRunUtility wf = new YHFlowRunUtility();
     // doc rd =  wf.getFlowRunData(conn, flowId, runId, "固定资产名称");
    //  String ss = rd.getItemData();
      
    } catch(Exception ex) {
      throw ex;
    }
    boolean isSuccess = false;
    if (isSuccess) {
      
    } else {
      
    }
    return null;
  }
  /**
   * 节点执行完毕执行
   * @param request
   * @param response
   * @return
   */
  public String after(HttpServletRequest request, HttpServletResponse response) throws Exception {
    //System.out.println("------------结束啦");
    return null;
  }
}
