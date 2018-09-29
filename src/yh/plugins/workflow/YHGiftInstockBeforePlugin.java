package yh.plugins.workflow;

import java.sql.Connection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowRunData;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHIWFPlugin;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.subsys.oa.giftProduct.instock.act.YHGiftInstockAct;
import yh.subsys.oa.giftProduct.instock.data.YHGiftInstock;

public class YHGiftInstockBeforePlugin implements YHIWFPlugin{
  /**
   * 节点执行前执行

   * @param request
   * @param response
   * @return
   */
  public String before(HttpServletRequest request, HttpServletResponse response)throws Exception {
    //System.out.println("------------开始啦");
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
    
      YHFlowRunData rd5 =  wf.getFlowRunData(conn, flowId, runId, "单价");
      String giftPrice = rd5.getItemData();
      YHFlowRunData rd6 =  wf.getFlowRunData(conn, flowId, runId, "数量");
      String giftQty= rd6.getItemData();
      if(!YHUtility.isNumber(giftPrice)){
        return "礼品的单价应为数字类型！";
      }
      if(!YHUtility.isInteger(giftQty)){
        return "礼品的数量应为整数类型！";
      }
    } catch(Exception ex) {
      throw ex;
    }
    return null;
  }
  /**
   * 节点执行完毕执行
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String after(HttpServletRequest request, HttpServletResponse response) throws Exception {
 
    
    //System.out.println("------------结束啦");
    return null;
  }
}
