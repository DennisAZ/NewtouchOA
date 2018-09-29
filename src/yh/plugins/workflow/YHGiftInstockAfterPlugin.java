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

public class YHGiftInstockAfterPlugin implements YHIWFPlugin{
  /**
   * 节点执行前执行

   * @param request
   * @param response
   * @return
   */
  public String before(HttpServletRequest request, HttpServletResponse response)  throws Exception {
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
      YHFlowRunData rd1 =  wf.getFlowRunData(conn, flowId, runId, "礼品名称");
      String giftName = rd1.getItemData();
      YHFlowRunData rd2 =  wf.getFlowRunData(conn, flowId, runId, "上缴部门");
      String deptId= rd2.getItemData();
      YHFlowRunData rd3 =  wf.getFlowRunData(conn, flowId, runId, "礼品类别");
      String giftType = rd3.getItemData();
      YHFlowRunData rd4 =  wf.getFlowRunData(conn, flowId, runId, "计量单位");
      String giftUnit = rd4.getItemData();
      
      YHFlowRunData rd5 =  wf.getFlowRunData(conn, flowId, runId, "单价");
      String giftPrice = rd5.getItemData();
      YHFlowRunData rd6 =  wf.getFlowRunData(conn, flowId, runId, "数量");
      String giftQty= rd6.getItemData();
      YHFlowRunData rd7 =  wf.getFlowRunData(conn, flowId, runId, "供应商");
      String giftSupplier = rd7.getItemData();
      YHFlowRunData rd8 =  wf.getFlowRunData(conn, flowId, runId, "经手人");
      String giftCreator = rd8.getItemData();
      
      YHFlowRunData rd9 =  wf.getFlowRunData(conn, flowId, runId, "保管员");
      String giftKeeper = rd9.getItemData();
      YHFlowRunData rd10 =  wf.getFlowRunData(conn, flowId, runId, "备注");
      String giftMemo= rd10.getItemData();
      YHFlowRunData rd11 =  wf.getFlowRunData(conn, flowId, runId, "礼品来源");
      String giftDesc = rd11.getItemData();
      YHGiftInstockAct giftInstockAct = new YHGiftInstockAct();
      YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      int userId = user.getSeqId();
      Date curDate = new Date();
      YHGiftInstock giftInstock =  new YHGiftInstock(); //(YHGiftInstock) YHFOM.build(request.getParameterMap());
      giftInstock.setCreateDate(curDate);
      if(giftType!=null&&!giftType.equals("")){
        if(deptId!=null&&!deptId.equals("")){
          giftInstock.setDeptId(Integer.parseInt(deptId));
        }
        if(giftName!=null){
          giftInstock.setGiftName(giftName);
        }
        if(giftType!=null){
          giftInstock.setGiftType(giftType);
        }
        if(giftUnit!=null){
          giftInstock.setGiftUnit(giftUnit);
        }
        if(giftPrice!=null&&!giftPrice.equals("")&&YHUtility.isNumber(giftPrice)){
          giftInstock.setGiftPrice(Double.parseDouble(giftPrice));
        }
        if(giftQty!=null&&!giftQty.equals("")&&YHUtility.isInteger(giftQty)){
          giftInstock.setGiftQty(Integer.parseInt(giftQty));
        }
        if(giftSupplier!=null){
          giftInstock.setGiftSupplier(giftSupplier);
        }
        if(giftKeeper!=null){
          giftInstock.setGiftKeeper(giftKeeper);
        }
        if(giftMemo!=null){
          giftInstock.setGiftMemo(giftMemo);
        }
        if(giftDesc!=null){
          giftInstock.setGiftDesc(giftDesc);
        }
        giftInstock.setGiftCreator(String.valueOf(userId));
        giftInstock.setRunId(runId);
        giftInstockAct.addGiftInstockWork(request, response,giftInstock);
      }
     
    } catch(Exception ex) {
      throw ex;
    }
    
    //System.out.println("------------结束啦");
    return null;
  }
}
