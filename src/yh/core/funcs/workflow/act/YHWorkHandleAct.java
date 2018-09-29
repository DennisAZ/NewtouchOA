package yh.core.funcs.workflow.act;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowProcess;
import yh.core.funcs.workflow.data.YHFlowRun;
import yh.core.funcs.workflow.data.YHFlowType;
import yh.core.funcs.workflow.logic.YHFlowProcessLogic;
import yh.core.funcs.workflow.logic.YHFlowRunAssistLogic;
import yh.core.funcs.workflow.logic.YHFlowRunLogic;
import yh.core.funcs.workflow.logic.YHFlowTypeLogic;
import yh.core.funcs.workflow.util.YHPrcsRoleUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHWorkHandleAct {
  private static Logger log = Logger
  .getLogger("yh.core.funcs.workflow.act.YHWorkHandleAct");
  /**
   * 取得工作办理界面的一相关数据,主要有表单，附件，会签意见
   * @param request
   * @param response
   * @return json对象 rtData的格式为 {formMsg:'',attach:[],feedBack:[]}
   * @throws Exception
   */
  public String getHandlerData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowIdStr = request.getParameter("flowId");
    String runIdStr = request.getParameter("runId");
    String prcsIdStr = request.getParameter("prcsId");
    String flowPrcsStr = request.getParameter("flowPrcs");
    String isWriteLog =request.getParameter("isWriteLog");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      //验证用户是否登陆
      if(loginUser == null){
        String message = YHWorkFlowUtility.Message("用户未登录，请<a href='" + request.getContextPath() +"'>重新登录!</a>",2);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }else{
        if (YHUtility.isNullorEmpty(runIdStr)) {
          String tmp = "此工作未新建成功，请重新办理！";
          this.setRequestError(request, tmp);
          return "/core/inc/rtjson.jsp";
        }
        int runId = Integer.parseInt(runIdStr);
        int prcsId = Integer.parseInt(prcsIdStr);
        int flowId = Integer.parseInt(flowIdStr);
        
        int flowPrcs = 0;
        if (flowPrcsStr != null && !"".equals(flowPrcsStr) && !"null".equals(flowPrcsStr)){
          flowPrcs = Integer.parseInt(flowPrcsStr);
        }
        YHFlowRunLogic flowRunLogic = new YHFlowRunLogic();
        if (!flowRunLogic.canHandlerWrok(runId, prcsId, flowPrcs, loginUser.getSeqId(), dbConn)) {
          String tmp = "此工作已经收回或转交至下一步或结束，您不能办理！";
          this.setRequestError(request, tmp);
          return "/core/inc/rtjson.jsp";
        }
        if (flowRunLogic.hasDelete(runId, dbConn)) {
          String tmp = "此工作已经删除，您不能办理！";
          this.setRequestError(request, tmp);
        } else {
        //取表单相关信息
          String imgPath = YHWorkFlowUtility.getImgPath(request);
          String msg = flowRunLogic.getHandlerMsg(loginUser , runId , prcsId , flowPrcsStr   , request.getRemoteAddr() , dbConn , imgPath ,isWriteLog);
          this.setRequestSuccess(request, "get Success", msg);
        }
      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public boolean getWebsign(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowIdStr = request.getParameter("flowId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String sql = "select 1 FROM oa_fl_type as FLOW_TYPE , oa_fl_form_item WHERE FLOW_TYPE.SEQ_ID=" + flowIdStr + " and FLOW_TYPE.FORM_SEQ_ID = oa_fl_form_item.FORM_ID AND CLAZZ='SIGN'";
      Statement stm1 = null;
      ResultSet rs1 = null;
      try {
        stm1 = dbConn.createStatement();
        rs1 = stm1.executeQuery(sql);
        if (rs1.next()) {
          return true;
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm1, rs1, null); 
      }
      return false;
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
  }
  public boolean getAip(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowIdStr = request.getParameter("flowId");
    String flowPrcsStr =  request.getParameter("flowPrcs");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowTypeLogic flowTypeLogic =  new YHFlowTypeLogic();
      YHFlowType flowType = flowTypeLogic.getFlowTypeById(Integer.parseInt(flowIdStr), dbConn);
      String dispAip = "";
      if ("1".equals(flowType.getFlowType())) {
        YHFlowProcessLogic flowPrcsLogic = new  YHFlowProcessLogic();
        //查出相关步骤
        YHFlowProcess flowProcess = flowPrcsLogic.getFlowProcessById(Integer.parseInt(flowIdStr), flowPrcsStr , dbConn);
        dispAip = flowProcess.getDispAip() + "";
      }
      if (dispAip == null || "0".equals(dispAip) || "".equals(dispAip)) {
        return false;
      }
      return true;
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
  }
  /**
         * 　取得草稿箱的内容
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getOutline(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowIdStr = request.getParameter("flowId");
    String runIdStr = request.getParameter("runId");
    int flowId = Integer.parseInt(flowIdStr);
    int runId = Integer.parseInt(runIdStr);
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHFlowRunAssistLogic logic = new YHFlowRunAssistLogic();
      String array = logic.getOutline(flowId , runId , loginUser.getSeqId(), dbConn);
      this.setRequestSuccess(request, "get Success", array);
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 保存表单
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String saveFormData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowIdStr = request.getParameter("flowId");
    String runIdStr = request.getParameter("runId");
    String prcsIdStr = request.getParameter("prcsId");
    String flowPrcsStr = request.getParameter("flowPrcs");
    String hiddenStr = request.getParameter("hiddenStr");
    String readOnlyStr = request.getParameter("readOnlyStr");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int runId = Integer.parseInt(runIdStr);
      int prcsId = Integer.parseInt(prcsIdStr);
      int flowId = Integer.parseInt(flowIdStr);
      int flowPrcs = Integer.parseInt(flowPrcsStr);
      if (hiddenStr == null ) {
        hiddenStr = "";
      }
      if (readOnlyStr == null) {
        readOnlyStr = "";
      }
      YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      //验证是否有权限      String roleStr = roleUtility.runRole(runId, flowId, prcsId, loginUser , dbConn);
      if("".equals(roleStr)){//没有权限
        String message = YHWorkFlowUtility.Message("没有该流程权限，请与OA管理员联系",0);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }else{
        YHFlowRunLogic flowRunLogic = new YHFlowRunLogic();
        //取表单相关信息
        String msg = flowRunLogic.saveFormData(loginUser, flowId, runId, prcsId, flowPrcs, request , hiddenStr , readOnlyStr,dbConn);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "保存成功!");
      }
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 取消工作 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String cancelRun(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowIdStr = request.getParameter("flowId");
    String runIdStr = request.getParameter("runId");
    String prcsIdStr = request.getParameter("prcsId");
    String flowPrcsStr = request.getParameter("flowPrcs");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int runId = Integer.parseInt(runIdStr);
      int prcsId = Integer.parseInt(prcsIdStr);
      int flowId = Integer.parseInt(flowIdStr);
      int flowPrcs = Integer.parseInt(flowPrcsStr);
      YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      //验证是否有权限
      String roleStr = roleUtility.runRole(runId, flowId, prcsId, loginUser , dbConn);
      if(!YHWorkFlowUtility.findId(roleStr, "2")){//没有权限
        String message = YHWorkFlowUtility.Message("没有该流程权限，请与OA管理员联系",0);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }else{
        YHFlowRunLogic flowRunLogic = new YHFlowRunLogic();
        //取表单相关信息
        flowRunLogic.cancelRun( flowId, runId, prcsId, flowPrcs , dbConn);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "删除成功!");
      }
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 取出以前的所有步骤
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getPreRunPrcs(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowIdStr = request.getParameter("flowId");
    String runIdStr = request.getParameter("runId");
    String prcsIdStr = request.getParameter("prcsId");
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
      //验证是否有权限
      String roleStr = roleUtility.runRole(runId, flowId, prcsId, loginUser , dbConn);
      if(!YHWorkFlowUtility.findId(roleStr, "2")){//没有权限
        String message = YHWorkFlowUtility.Message("没有该流程权限，请与OA管理员联系",0);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }else{
        YHFlowRunLogic flowRunLogic = new YHFlowRunLogic();
        //取表单相关信息
        String str = flowRunLogic.getPreRunPrcs(runId , prcsId , flowId , dbConn);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "取得成功!");
        request.setAttribute(YHActionKeys.RET_DATA, "[" + str + "]");
      }
    } catch (Exception ex) {
      String message = YHWorkFlowUtility.Message(ex.getMessage(),1);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, message);
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
