package yh.core.funcs.workflow.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.logic.YHAttachmentLogic;
import yh.core.funcs.workflow.logic.YHFlowRunLogic;
import yh.core.funcs.workflow.logic.YHFormEditLogic;
import yh.core.funcs.workflow.util.YHPrcsRoleUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.file.YHFileUploadForm;

public class YHFormEditAct {
  private static Logger log = Logger
    .getLogger("yh.core.funcs.workflow.act.YHFormEditAct");
  /**
   * 取得编辑界面的一相关数据,主要有表单，附件
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getEditData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String flowIdStr = request.getParameter("flowId");
    String runIdStr = request.getParameter("runId");
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
        int runId = Integer.parseInt(runIdStr);
        YHFlowRunLogic flowRunLogic = new YHFlowRunLogic();
        if (flowRunLogic.hasDelete(runId, dbConn)) {
          String tmp = "此工作已经删除，您不能办理！";
          this.setRequestError(request, tmp);
        } else {
        //取表单相关信息
          YHFormEditLogic edit = new YHFormEditLogic();
          String imgPath = YHWorkFlowUtility.getImgPath(request);
          String msg = edit.getEditMsg(loginUser , runId  , request.getRemoteAddr() , dbConn , imgPath);
          this.setRequestSuccess(request, "get Success", msg);
        }
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
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int runId = Integer.parseInt(runIdStr);
      int flowId = Integer.parseInt(flowIdStr);
      YHFormEditLogic logic = new YHFormEditLogic();
      //验证是否有权限
      boolean hasRight = logic.hasEditRight(flowId  , loginUser , dbConn);
      if(!hasRight){//没有权限
        String message = YHWorkFlowUtility.Message("没有该流程编辑权限，请与OA管理员联系",0);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }else{
        //取表单相关信息
        String msg = logic.saveFormData(loginUser, flowId, runId, request, dbConn);
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
  /**
   * 上传附件处理
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String uploadFile(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    request.setCharacterEncoding("UTF-8");
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
       
      YHFileUploadForm fileForm = new YHFileUploadForm();
      //注意这里的
      fileForm.parseUploadRequest(request);
      int runId = Integer.parseInt(fileForm.getParameter("runId"));
      int flowId = Integer.parseInt(fileForm.getParameter("flowId"));
      
      YHFormEditLogic logic1 = new YHFormEditLogic();
      //验证是否有权限
      boolean hasRight = logic1.hasEditRight(flowId  , loginUser , dbConn);
      if(!hasRight){//没有权限
        String message = YHWorkFlowUtility.Message("没有该流程编辑权限，请与OA管理员联系",0);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      } else {
        YHAttachmentLogic logic = new  YHAttachmentLogic();
        logic.addAttachment(runId , fileForm, dbConn);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        response.setHeader("Cache-Control", "no-cache");  
        PrintWriter out = response.getWriter();
        out.print("<body onload=\"window.parent.callBack()\"/>");
        out.flush();
        out.close();
      }
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  public String getAttachments(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    request.setCharacterEncoding("UTF-8");
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      int runId = Integer.parseInt(request.getParameter("runId"));
      int flowId = Integer.parseInt(request.getParameter("flowId"));
      
      YHFormEditLogic logic1 = new YHFormEditLogic();
      //验证是否有权限
      boolean hasRight = logic1.hasEditRight(flowId  , loginUser , dbConn);
      if(!hasRight){//没有权限
        String message = YHWorkFlowUtility.Message("没有该流程编辑权限，请与OA管理员联系",0);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      } else {
        YHAttachmentLogic logic = new  YHAttachmentLogic();
        String data = "[" + logic.getAttachments(loginUser, runId  , flowId , dbConn) + "]";
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "取得成功 ");
        request.setAttribute(YHActionKeys.RET_DATA, data);
      }
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String delAttachment(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      int runId = Integer.parseInt(request.getParameter("runId"));
      int flowId = Integer.parseInt(request.getParameter("flowId"));
      String attachmentId = request.getParameter("attachmentId");
      String attachmentName = request.getParameter("attachmentName");
      
      YHFormEditLogic logic1 = new YHFormEditLogic();
      //验证是否有权限
      boolean hasRight = logic1.hasEditRight(flowId  , loginUser , dbConn);
      if(!hasRight){//没有权限
        String message = YHWorkFlowUtility.Message("没有该流程编辑权限，请与OA管理员联系",0);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }  else {
        YHAttachmentLogic logic = new  YHAttachmentLogic();
        logic.delAttachment(runId, attachmentId, attachmentName , dbConn);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "删除成功 ");
      }
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String createAttachment(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      int runId = Integer.parseInt(request.getParameter("runId"));
      int flowId = Integer.parseInt(request.getParameter("flowId"));
      String newType = request.getParameter("newType");
      String newName = request.getParameter("newName");
      
      YHFormEditLogic logic1 = new YHFormEditLogic();
      //验证是否有权限
      boolean hasRight = logic1.hasEditRight(flowId  , loginUser , dbConn);
      if(!hasRight){//没有权限
        String message = YHWorkFlowUtility.Message("没有该流程编辑权限，请与OA管理员联系",0);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      } else {
        YHAttachmentLogic logic = new  YHAttachmentLogic();
        String realPath = request.getRealPath("/");
        logic.createAttachment(runId, newType, newName , dbConn , realPath);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "添加成功 ");
      }
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
