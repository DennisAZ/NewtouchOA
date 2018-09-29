package yh.core.funcs.workflow.act;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.logic.YHDelegateLogic;
import yh.core.funcs.workflow.logic.YHFlowRunLogic;
import yh.core.funcs.workflow.util.YHPrcsRoleUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHDelegateAct {
  private static Logger log = Logger
    .getLogger("yh.core.funcs.workflow.act.YHDelegateAct");
  public String getDelegateMsg(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      int runId = Integer.parseInt(request.getParameter("runId"));
      int prcsId = Integer.parseInt(request.getParameter("prcsId"));
      int flowId = Integer.parseInt(request.getParameter("flowId"));
      String sFlowPrcs = request.getParameter("flowPrcs");
      String sIsManage = request.getParameter("isManage");
      boolean isManage = Boolean.valueOf(sIsManage);
      //主要是区分自由流程
      int flowPrcs  = 0 ;
      if (sFlowPrcs != null) {
        flowPrcs = Integer.parseInt(sFlowPrcs);
      }
      YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      //验证是否有权限,并取出权限字符串
      String roleStr = roleUtility.runRole(runId, flowId, prcsId, loginUser ,dbConn);
      if ( "5,".equals(roleStr) ) {//没有权限
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG,"没有该流程委托权限，请与OA管理员联系");
      } else {
        YHDelegateLogic logic = new YHDelegateLogic();
        if (!isManage) {
          boolean flag = logic.checkHandlerState(runId, prcsId, loginUser.getSeqId(), dbConn);
          if (!flag) {
            this.setRequestError(request, "对不起，您没有该流程委托权限!");
            return "/core/inc/rtjson.jsp";
          }
        }
        int freeOther = logic.getFreeOther(flowId, dbConn);
        if (freeOther  == 0) {
          this.setRequestError(request, "此流程禁止委托！");
          return "/core/inc/rtjson.jsp";
        }
        YHFlowRunLogic runLogic = new YHFlowRunLogic();
        boolean hasDelete = runLogic.hasDelete(runId, dbConn);
        if (hasDelete) {
          this.setRequestError(request, "对不起，此工作已经被删除!");
          return "/core/inc/rtjson.jsp";
        }
        String data  = "";
        if (flowPrcs != 0) {
          data = logic.getDelegateMsg(runId, prcsId, flowId, flowPrcs, dbConn, loginUser , isManage);
        } else {
          data = logic.getFreeDelegateMsg(runId , prcsId ,flowId , dbConn, loginUser  ,isManage);
        }
        this.setRequestSuccess(request, "成功取得信息", data);
      }
    } catch (Exception ex){
      this.setRequestError(request, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String delegate(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      int runId = Integer.parseInt(request.getParameter("runId"));
      int prcsId = Integer.parseInt(request.getParameter("prcsId"));
      int flowId = Integer.parseInt(request.getParameter("flowId"));
      int flowPrcs = Integer.parseInt(request.getParameter("flowPrcs"));
      int toId = Integer.parseInt(request.getParameter("user"));
      String opFlag = request.getParameter("opFlag");
      
      String remindFlag = request.getParameter("smsRemind");
      String remindFlag2 = request.getParameter("sms2Remind");
      String remindContent = request.getParameter("smsContent");
      //暂不考虑管理人中的情况
      int userOldId = Integer.parseInt(request.getParameter("userOldId"));
      //int userOldId = loginUser.getSeqId();
      
      YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      //验证是否有权限,并取出权限字符串
      String roleStr = roleUtility.runRole(runId, flowId, prcsId, loginUser ,dbConn);
      if ( "".equals(roleStr) ) {//没有权限
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG,"没有该流程委托权限，请与OA管理员联系");
      } else {
        YHDelegateLogic logic = new YHDelegateLogic();
        int freeOther = logic.getFreeOther(flowId, dbConn);
        if (freeOther  == 0) {
          this.setRequestError(request, "此流程禁止委托！");
          return "/core/inc/rtjson.jsp";
        }
        String sortId = request.getParameter("sortId");
        if (sortId == null) {
          sortId = "";
        }
        String skin = request.getParameter("skin");
        if (skin == null) {
          skin = "";
        }
        String msg = logic.delegate(runId, prcsId, flowId, flowPrcs, loginUser, toId, opFlag, remindFlag , remindFlag2, userOldId, remindContent, request.getContextPath() ,sortId , skin , dbConn);
        this.setRequestSuccess(request, msg);
      }
    } catch (Exception ex){
      this.setRequestError(request, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 取得实际步骤经办人
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getPrcsOpUsers(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      int runId = Integer.parseInt(request.getParameter("runId"));
      int prcsId = Integer.parseInt(request.getParameter("prcsId"));
      String search = request.getParameter("search");
      YHDelegateLogic logic = new YHDelegateLogic();
      String users = logic.getPrcsOpUsers(prcsId, runId,search, dbConn); 
      this.setRequestSuccess(request, "数据取得成功" , users);
    } catch (Exception ex){
      this.setRequestError(request, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String freeDelegate(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      int runId = Integer.parseInt(request.getParameter("runId"));
      int prcsId = Integer.parseInt(request.getParameter("prcsId"));
      int flowId = Integer.parseInt(request.getParameter("flowId"));
      int toId = Integer.parseInt(request.getParameter("user"));
      String opFlag = request.getParameter("opFlag");
      
      String remindFlag = request.getParameter("smsRemind");
      String remindFlag2 = request.getParameter("sms2Remind");
      String remindContent = request.getParameter("smsContent");
      //暂不考虑管理人中的情况
      int userOldId = Integer.parseInt(request.getParameter("userOldId"));
      
      YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      //验证是否有权限,并取出权限字符串
      String roleStr = roleUtility.runRole(runId, flowId, prcsId, loginUser ,dbConn);
      if ( "".equals(roleStr) ) {//没有权限
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG,"没有该流程委托权限，请与OA管理员联系");
      } else {
        YHDelegateLogic logic = new YHDelegateLogic();
        int freeOther = logic.getFreeOther(flowId, dbConn);
        if (freeOther  == 0) {
          this.setRequestError(request, "此流程禁止委托！");
          return "/core/inc/rtjson.jsp";
        }
        String sortId = request.getParameter("sortId");
        if (sortId == null) {
          sortId = "";
        }
        String skin = request.getParameter("skin");
        if (skin == null) {
          skin = "";
        }
        String msg = logic.freeDelegate(runId, prcsId, flowId, loginUser, toId, opFlag, remindFlag , remindFlag2, userOldId, remindContent, request.getContextPath() , sortId, skin, dbConn);
        this.setRequestSuccess(request, msg);
      }
    } catch (Exception ex){
      this.setRequestError(request, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 设置错误信息
   * @param request
   * @param message
   */
  public  void setRequestError(HttpServletRequest request , String message) {
    request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
    request.setAttribute(YHActionKeys.RET_MSRG, message);
  }
  /**
   * 设置成功信息
   * @param request
   * @param message
   */
  public  void setRequestSuccess(HttpServletRequest request , String message) {
    request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG, message);
  }
  /**
   * 设置成功信息 
   * @param request  
   * @param message 
   * @param data
   */
  public  void setRequestSuccess(HttpServletRequest request , String message , String data) {
    request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
    request.setAttribute(YHActionKeys.RET_MSRG, message);
    request.setAttribute(YHActionKeys.RET_DATA, data);
  }
}
