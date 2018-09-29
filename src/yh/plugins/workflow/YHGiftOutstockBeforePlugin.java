package yh.plugins.workflow;

import java.sql.Connection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.util.YHFlowRunUtility;
import yh.core.funcs.workflow.util.YHIWFPlugin;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.subsys.oa.giftProduct.instock.data.YHGiftInstock;
import yh.subsys.oa.giftProduct.instock.logic.YHGiftInstockLogic;
import yh.subsys.oa.giftProduct.outstock.data.YHGiftOutstock;
import yh.subsys.oa.giftProduct.outstock.logic.YHGiftOutstockLogic;

public class YHGiftOutstockBeforePlugin  implements YHIWFPlugin{
  /**
   * 节点执行前执行

   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String before(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
    
      String giftId =  wf.getData(conn, flowId, runId, "礼品ID");
      String transQty =  wf.getData(conn, flowId, runId, "礼品数量");
 
      if(giftId==null||giftId.equals("")){
        String returnStr = "礼品不能为空！";
        return returnStr;
      }
      if(!YHUtility.isNumber(transQty)){
        String returnStr = "礼品数量必须为数字！";
        return returnStr;
      }
      if(giftId!=null&&!giftId.equals("")&&YHUtility.isInteger(giftId)){
        YHGiftInstockLogic instockLogic = new YHGiftInstockLogic();
        YHGiftInstock instock = instockLogic.selectGiftInstockById(conn, Integer.parseInt(giftId));
        //int useGiftQty = instockLogic.selectGiftQty(conn, Integer.parseInt(giftId));
        if(instock.getGiftQty()<Integer.parseInt(transQty)){
          return "领用数量大于库存数量，请仔细操作!";
        }
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
