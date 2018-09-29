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

public class YHGiftOutstockAfterPlugin  implements YHIWFPlugin{
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
      String operator =  wf.getData(conn, flowId, runId, "领用人");
      String transDate=  wf.getData(conn, flowId, runId, "领用日期");
      String transUser =  wf.getData(conn, flowId, runId, "操作者");
      String giftId =  wf.getData(conn, flowId, runId, "礼品ID");;
      String transQty =  wf.getData(conn, flowId, runId, "礼品数量");
      String transUses =  wf.getData(conn, flowId, runId, "礼品用途");
      String transMemo =  wf.getData(conn, flowId, runId, "领用备注");
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      Date curDate = new Date();
      int transQtyInt = 0;
       //判断是否合格
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
       // int useGiftQty = instockLogic.selectGiftQty(conn, Integer.parseInt(giftId));
        if(instock.getGiftQty()<Integer.parseInt(transQty)){
          return "领用数量大于库存数量，请仔细操作!";
        }
      }
      
      
      YHGiftInstockLogic instockLogic = new YHGiftInstockLogic();
      YHGiftOutstockLogic outstockLogic = new YHGiftOutstockLogic();
      if(operator!=null&&!operator.equals("")&&giftId!=null&&!giftId.equals("")){
        if(!transQty.equals("")&&YHUtility.isInteger(transQty)){
          transQtyInt = Integer.parseInt(transQty);
        }
        YHGiftOutstock giftOutstock = new YHGiftOutstock();
        giftOutstock.setGiftId(Integer.parseInt(giftId));
        giftOutstock.setOperator(String.valueOf(userId));
        giftOutstock.setTransDate(curDate);
        giftOutstock.setTransUser(String.valueOf(userId));
        giftOutstock.setTransQty(transQtyInt);
        giftOutstock.setTransUses(transUses);
        giftOutstock.setTransMemo(transMemo);
        giftOutstock.setRunId(runId);
        int seqId = outstockLogic.addGiftOutstock(conn, giftOutstock);
        
        //库存减去
        instockLogic.updateInstockById(conn, giftId, transQtyInt);
      }
    } catch(Exception ex) {
      throw ex;
    }
    
    //System.out.println("------------结束啦");
    return null;
  }
}
