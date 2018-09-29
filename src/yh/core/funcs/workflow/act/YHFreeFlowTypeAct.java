package yh.core.funcs.workflow.act;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.logic.YHFlowRunLogic;
import yh.core.funcs.workflow.logic.YHFlowTypeLogic;
import yh.core.funcs.workflow.logic.YHFreeFlowLogic;
import yh.core.funcs.workflow.util.YHPrcsRoleUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHFreeFlowTypeAct {
  private static Logger log = Logger.getLogger(YHFreeFlowTypeAct.class);
  public String getNewPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String sFlowId = request.getParameter("flowId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int flowId = Integer.parseInt(sFlowId);
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      String data = flowTypeLogic.getNewPriv(flowId, dbConn);
      request.setAttribute(YHActionKeys.RET_DATA, data);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "全部取出流程分类数据！");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String updateNewPriv(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String userId = request.getParameter("user");
    String dept = request.getParameter("dept");
    String role = request.getParameter("role");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int flowId = Integer.parseInt(request.getParameter("flowId"));
      YHFlowTypeLogic flowTypeLogic = new YHFlowTypeLogic();
      flowTypeLogic.updateNewPriv(flowId, userId, dept, role, dbConn);

      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "修改成功");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 取得自由流程流转页面相关数据
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getTurnData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowIdStr = request.getParameter("flowId");
    String runIdStr = request.getParameter("runId");
    String prcsIdStr = request.getParameter("prcsId");
    String sIsManage = request.getParameter("isManage");
    if (sIsManage == null ) {
      sIsManage = "false";
    }
    boolean isManage = Boolean.valueOf(sIsManage);
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int runId = Integer.parseInt(runIdStr);
      int prcsId = Integer.parseInt(prcsIdStr);
      int flowId = Integer.parseInt(flowIdStr);
      
      YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      //验证是否有权限,并取出权限字符串
      String roleStr = roleUtility.runRole(runId, flowId, prcsId, loginUser, dbConn);
      if("".equals(roleStr) && !isManage){//没有权限
        String message = YHWorkFlowUtility.Message("没有该流程办理权限，请与OA管理员联系",0);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }else{
        YHFreeFlowLogic flowRunLogic = new YHFreeFlowLogic();
        //取转交相关数据
        String msg = flowRunLogic.getTurnData(loginUser , flowId , runId , prcsId  ,dbConn , isManage);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "get success");
        request.setAttribute(YHActionKeys.RET_DATA, msg);
      }
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String turnNext(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String itemOld = request.getParameter("freeItemOld");
    String remindContent = request.getParameter("smsContent");
    String sPreSet = request.getParameter("preSet");
    String sMaxPrcs = request.getParameter("maxPrcs");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int runId = Integer.parseInt(request.getParameter("runId"));
      int prcsId = Integer.parseInt(request.getParameter("prcsId"));
      int flowId = Integer.parseInt(request.getParameter("flowId"));
      String sms2Remind = request.getParameter("sms2Remind") ;
      String smsRemind = request.getParameter("smsRemind") ;
      
      boolean preSet = false ;
      if (sPreSet != null) {
        preSet= true;
      }
      int maxPrcs = prcsId;
      if (sMaxPrcs != null) {
        maxPrcs = Integer.parseInt(sMaxPrcs);
      }
      List<Map> preList = new ArrayList();
      for (int i = prcsId + 1 ; i <= maxPrcs ; i ++){
        Map map = new HashMap();
        map.put("prcsId", i);
        String tmp = "";
        if (i != prcsId + 1) {
          tmp = String.valueOf(i);
        } 
        map.put("prcsUser", request.getParameter("prcsUser" + tmp));
        map.put("prcsOpUser", request.getParameter("prcsOpUser" + tmp));
        map.put("freeItem", request.getParameter("freeItem" + tmp));
        map.put("topFlag", request.getParameter("topFlag" + tmp));
        preList.add(map);
      }
      //YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      //验证是否有权限,并取出权限字符串
      //String roleStr = roleUtility.runRole(runId, flowId, prcsId, loginUser , dbConn);
      YHFreeFlowLogic freeFlowLogic = new YHFreeFlowLogic();
      String remindUser = freeFlowLogic.turnNext(loginUser, flowId, runId, prcsId, dbConn, preSet, preList, itemOld);
      if ("on".equals(sms2Remind)) {
        YHMobileSms2Logic ms2l = new YHMobileSms2Logic(); 
        ms2l.remindByMobileSms(dbConn, remindUser, loginUser.getSeqId(), remindContent, null);
      }
      if ("on".equals(smsRemind)) {
        YHFlowRunLogic flowRunLogic = new YHFlowRunLogic();
        String sortId = request.getParameter("sortId");
        if (sortId == null) {
          sortId = "";
        }
        String skin = request.getParameter("skin");
        if (skin == null) {
          skin = "";
        }
        flowRunLogic.remindNext(dbConn, runId ,  flowId , prcsId + 1 ,  0 , remindContent, request.getContextPath(), remindUser, loginUser.getSeqId() ,sortId , skin);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功转交!");
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String stop(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int runId = Integer.parseInt(request.getParameter("runId"));
      int prcsId = Integer.parseInt(request.getParameter("prcsId"));
      int flowId = Integer.parseInt(request.getParameter("flowId"));
      YHFreeFlowLogic freeFlowLogic = new YHFreeFlowLogic();
      String msg = freeFlowLogic.stop(runId ,flowId , prcsId, loginUser , dbConn);
      if (msg == null) {
        msg = "操作成功！";
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, msg);
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
