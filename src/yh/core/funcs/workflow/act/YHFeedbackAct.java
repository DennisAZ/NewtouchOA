package yh.core.funcs.workflow.act;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowRunFeedback;
import yh.core.funcs.workflow.logic.YHFeedbackLogic;
import yh.core.funcs.workflow.logic.YHFlowRunLogic;
import yh.core.funcs.workflow.util.YHPrcsRoleUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;

public class YHFeedbackAct {
  private static Logger log = Logger
    .getLogger("yh.core.funcs.workflow.act.YHFeedbackAct");
  public String handlerFinish(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
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
      YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      //验证是否有权限,并取出权限字符串
      String roleStr = roleUtility.runRole(runId, flowId, prcsId, loginUser , dbConn);
      if("".equals(roleStr)){//没有权限
        String message = YHWorkFlowUtility.Message("没有该流程办理权限，请与OA管理员联系",0);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }else{
        YHFlowRunLogic flowRunLogic = new YHFlowRunLogic();
        String sortId = request.getParameter("sortId");
        if (sortId == null) {
          sortId = "";
        }
        String skin = request.getParameter("skin");
        if (skin == null) {
          skin = "";
        }
        String nextPage = flowRunLogic.handlerFinish(loginUser, runId, flowId, prcsId, flowPrcs,request.getRemoteAddr() , sortId , skin , dbConn ,request.getContextPath());
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "办理完毕!");
        request.setAttribute( YHActionKeys.RET_DATA,  nextPage );
      }
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String feedback(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
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
      
      YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      //验证是否有权限,并取出权限字符串
      String roleStr = roleUtility.runRole(runId, flowId, prcsId, loginUser , dbConn);
      if("".equals(roleStr)){//没有权限
        String message = YHWorkFlowUtility.Message("没有该流程办理权限，请与OA管理员联系",0);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, message);
      }else{
        YHFlowRunLogic flowRunLogic = new YHFlowRunLogic();
        String sortId = request.getParameter("sortId");
        if (sortId == null) {
          sortId = "";
        }
        String skin = request.getParameter("skin");
        if (skin == null) {
          skin = "";
        }
        flowRunLogic.handlerFinish(loginUser, runId, flowId, prcsId, flowPrcs , request.getRemoteAddr() , sortId , skin ,dbConn , request.getContextPath());
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "办理完毕!");
      }
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getRecFeedback(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      StringBuffer sb = new StringBuffer("[");
      int count = 0 ;
      String query  = "SELECT CONTENT from oa_fl_run_feedback where USER_ID='"+loginUser.getSeqId()+"' order by EDIT_TIME desc";
      Statement stm5 = null;
      ResultSet rs5 = null;
      String contentAll = "";
      try {
        stm5 = dbConn.createStatement();
        rs5 = stm5.executeQuery(query);
        while(rs5.next()) {
          String content = YHUtility.null2Empty(rs5.getString("CONTENT")).trim();
          if (YHUtility.isNullorEmpty(content) 
              || contentAll.indexOf("<-->" + content + "<-->") != -1) {
            continue;
          }
          contentAll += "<-->" + content + "<-->";
          String contentView = YHUtility.encodeSpecial(content);
          if (contentView.length() > 35) {
            contentView = contentView.substring(0 , 35) + "...";
          }
          sb.append("{content:\"").append(content).append("\"").append(",contentView:\"").append(contentView).append("\"},");
          count++;
          if (count >= 50) {
            break;
          }
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm5, rs5, null); 
      }
      if (count > 0 ) {
        sb.deleteCharAt(sb.length() -  1);
      }
      sb.append("]");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 保存会签
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String saveFeedback(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String content = request.getParameter("content");
    String seqIdStr = request.getParameter("seqId");
    String attachmentId = request.getParameter("attachmentId");
    if (attachmentId == null) {
      attachmentId = "";
    }
    String attachmentName = request.getParameter("attachmentName");
    if (attachmentName == null) {
      attachmentName = "";
    }
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int runId = 0;
      String sRunId = request.getParameter("runId");
      if (YHUtility.isInteger(sRunId)) {
        runId = Integer.parseInt(sRunId);
      }
      int prcsId = 0;
      String sPrcsId = request.getParameter("prcsId");
      if (YHUtility.isInteger(sPrcsId)) {
        prcsId = Integer.parseInt(sPrcsId);
      }
      int flowId = 0;
      String sFlowId = request.getParameter("flowId");
      if (YHUtility.isInteger(sFlowId)) {
        flowId = Integer.parseInt(sFlowId);
      }
      int flowPrcs = 0;
      String sflowPrcs = request.getParameter("flowPrcs");
      if (YHUtility.isInteger(sflowPrcs)) {
        flowPrcs = Integer.parseInt(sflowPrcs);
      }
      YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      //验证是否有权限
      String roleStr = roleUtility.runRole ( runId, flowId, prcsId, loginUser , dbConn) ;
      if ( "".equals ( roleStr ) ) {//没有权限
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "没有该流程权限，请与OA管理员联系");
      } else {
        String signData = request.getParameter("signData");
        if ( seqIdStr != null && !"".equals(seqIdStr) ) {
          YHFeedbackLogic fbLogic = new YHFeedbackLogic();
          fbLogic.updateFeedback(Integer.parseInt(seqIdStr), content , attachmentId , attachmentName , dbConn);
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
          request.setAttribute(YHActionKeys.RET_MSRG, "修改成功!");
        }else{
          YHFeedbackLogic fbLogic = new YHFeedbackLogic();
          YHFlowRunFeedback fb = new YHFlowRunFeedback();
          if (content.startsWith("<p>")) {
            content = content.replaceFirst("<p>", "");
          }
          if (content.endsWith("</p>")) {
            content = content.substring(0, content.lastIndexOf("</p>"));
          }
          fb.setContent(content);
          Date date = new Date();
          fb.setEditTime(date);
          fb.setPrcsId(prcsId);
          fb.setRunId(runId);
          fb.setUserId(loginUser.getSeqId());
          fb.setAttachmentId(attachmentId);
          fb.setAttachmentName(attachmentName);
          fb.setFlowPrcs(flowPrcs);
          fb.setSignData(signData);
          fbLogic.saveFeedback(fb , dbConn);
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
          request.setAttribute(YHActionKeys.RET_MSRG, "保存成功!");
        }
      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.toString());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getSignData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("feedId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFeedbackLogic fbLogic = new YHFeedbackLogic(); 
      String signData = fbLogic.getSignData(Integer.parseInt(seqId), dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "get success");
      request.setAttribute(YHActionKeys.RET_DATA, "'" + signData + "'");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.toString());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 取得会签
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getFeedbacks(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int runId = Integer.parseInt(request.getParameter("runId"));
      int prcsId = Integer.parseInt(request.getParameter("prcsId"));
      int flowId = Integer.parseInt(request.getParameter("flowId"));
      YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      //验证是否有权限
      String roleStr = roleUtility.runRole ( runId, flowId, prcsId, loginUser ,dbConn) ;
      if ( "".equals ( roleStr ) ) {//没有权限
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "没有该流程权限，请与OA管理员联系");
      } else {
        YHFeedbackLogic fbLogic = new YHFeedbackLogic();
        String feedbacks = "";
        if ( seqId != null && !"".equals(seqId) ) {
          feedbacks = fbLogic.getFeedback(Integer.parseInt(seqId), dbConn);
        } else {
          feedbacks = "[" +  fbLogic.getFeedbacks(loginUser, runId, prcsId, flowId , dbConn)+ "]";
        }
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "get success");
        request.setAttribute(YHActionKeys.RET_DATA, feedbacks );
      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.toString());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String delFeedback(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int runId = Integer.parseInt(request.getParameter("runId"));
      int prcsId = Integer.parseInt(request.getParameter("prcsId"));
      int flowId = Integer.parseInt(request.getParameter("flowId"));
      int seqId = Integer.parseInt(request.getParameter("seqId"));
      YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      //验证是否有权限
      String roleStr = roleUtility.runRole ( runId, flowId, prcsId, loginUser , dbConn) ;
      if ( "".equals ( roleStr ) ) {//没有权限
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "没有该流程权限，请与OA管理员联系");
      } else {
        YHFeedbackLogic fbLogic = new YHFeedbackLogic();
        fbLogic.delFeedback(seqId , dbConn);
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
        request.setAttribute(YHActionKeys.RET_MSRG, "删除成功!");
      }
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.toString());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
}
