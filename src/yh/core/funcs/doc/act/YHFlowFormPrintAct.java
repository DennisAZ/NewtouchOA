package yh.core.funcs.doc.act;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.doc.util.YHWorkFlowConst;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.doc.logic.YHFlowPrintLogic;
import yh.core.funcs.doc.util.YHFlowRunUtility;
import yh.core.funcs.doc.data.YHDocRun;
import yh.core.funcs.doc.data.YHDocFlowType;
import yh.core.funcs.doc.logic.YHAttachmentLogic;
import yh.core.funcs.doc.logic.YHFeedbackLogic;
import yh.core.funcs.doc.logic.YHFlowRunLogic;
import yh.core.funcs.doc.logic.YHFlowTypeLogic;
import yh.core.funcs.doc.logic.YHMyWorkLogic;
import yh.core.funcs.doc.util.YHPrcsRoleUtility;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHFlowFormPrintAct {
  private static Logger log = Logger
  .getLogger("yh.core.funcs.doc.act.YHFlowFormPrintAct");
  public boolean getAip(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
        String sRunId = request.getParameter("runId");
        String flowId = request.getParameter("flowId");
        Connection dbConn = null;
        try{
          YHRequestDbConn requestDbConn = (YHRequestDbConn) request
              .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
          dbConn = requestDbConn.getSysDbConn();
          int runId = Integer.parseInt(sRunId);
          YHORM orm = new YHORM();
          HashMap map = new HashMap();
          map.put("RUN_ID", runId);
          YHDocRun flowRun = (YHDocRun) orm.loadObjSingle(dbConn, YHDocRun.class, map);
          String ss = YHUtility.null2Empty(flowRun.getAipFiles());
          YHPerson user = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
           YHFlowPrintLogic logic=new YHFlowPrintLogic();
          String data =logic.getTempOptionLogic(dbConn,user,flowId,sRunId);
          if ("".equals(ss) && "".equals(data)) {
            return false;
          } else {
            return true;
          }
        }catch(Exception ex){
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
          request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
          throw ex;
        }
      }
  public String restoreFile(HttpServletRequest request,
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
      String attachmentId = request.getParameter("attachmentId");
      String attachmentName = request.getParameter("attachmentName");
      
      YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      //验证是否有权限,并取出权限字符串
      String roleStr = roleUtility.runRole(runId, flowId, prcsId, loginUser , dbConn);
      if ( "".equals(roleStr) ) {//没有权限
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "没有该流程办理权限，请与OA管理员联系");
      } else {
        YHAttachmentLogic logic = new  YHAttachmentLogic();
        
        
//        YHFlowRunLogic runLogic = new YHFlowRunLogic();
//        YHFlowRun flowRun = runLogic.getFlowRunByRunId(runId , conn);
//        attachmentId = flowRun.getAttachmentId() != null ? flowRun.getAttachmentId() : "";
//        attachmentName = flowRun.getAttachmentName() != null ? flowRun.getAttachmentName() : "";
//        attachmentIdStr += attachmentId;
//        attachmentNameStr += attachmentName;
//        flowRun.setAttachmentId(attachmentIdStr);
//        flowRun.setAttachmentName(attachmentNameStr);
//        orm.updateSingle(conn, flowRun);
        //logic.createAttachment(runId, newType, newName , dbConn);
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
  public String getFormPrintInfo(HttpServletRequest request,
  HttpServletResponse response) throws Exception{
    String sRunId = request.getParameter("runId");
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int runId = Integer.parseInt(sRunId);
      YHORM orm = new YHORM();
      HashMap map = new HashMap();
      map.put("RUN_ID", runId);
      YHDocRun flowRun = (YHDocRun) orm.loadObjSingle(dbConn, YHDocRun.class, map);
      String runName = YHWorkFlowUtility.getRunName(flowRun.getRunName());
      String[] ss = YHUtility.null2Empty(flowRun.getAipFiles()).split("\n");
      String s = "[";
      for (String s1 : ss) {
        if ("".equals(s1)) {
          continue;
        }
        String[] tmp = s1.split(":");
        String query = "select T_NAME FROM "+ YHWorkFlowConst.FLOW_PRINT_TPL +" where SEQ_ID='"+tmp[0]+"'";
        Statement stm = null;
        ResultSet rs = null;
        String name = "";
        try {
          stm = dbConn.createStatement();
          rs = stm.executeQuery(query);
          if (rs.next()) {
            name = rs.getString("T_NAME");
          }
        } catch(Exception ex) {
          throw ex;
        } finally {
          YHDBUtility.close(stm, rs, null); 
        }
        s += "{name:'"+name+"' , value:'"+ tmp[1] +"'},";
      }
      if (s.endsWith(",")) {
        s = s.substring(0, s.length() - 1);
      }
      s += "]";
      YHFlowRunUtility wf = new YHFlowRunUtility();
      String webrootPath = request.getRealPath("/");
      String[] str = YHWorkFlowUtility.getDocFormTitle(webrootPath );
      String docTitle = wf.getData(dbConn, flowRun.getFlowId(), runId, str[0]);
      docTitle =  YHWorkFlowUtility.encodeSpecial(docTitle);
      String data = "{runName:'"+  runName +"',flowId:"+ flowRun.getFlowId() +",delFlag:"+flowRun.getDelFlag()+",aipFile:"+ s +",docTitle:\""+ docTitle +"\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取得数据");
      request.setAttribute(YHActionKeys.RET_DATA, data);
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getFormPrintMsg(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String flowView = request.getParameter("flowView");
    String sRunId = request.getParameter("runId");
    String sFlowId = request.getParameter("flowId");
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowRunLogic frl = new YHFlowRunLogic();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHDocRun flowRun = frl.getFlowRunByRunId(Integer.parseInt(sRunId) , dbConn);
      YHFlowTypeLogic ftl = new YHFlowTypeLogic();
      YHDocFlowType ft = ftl.getFlowTypeById(Integer.parseInt(sFlowId),dbConn);
      StringBuffer sb = new StringBuffer("{");
      YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");
      String imgPath = YHWorkFlowUtility.getImgPath(request);
      Map form =  frl.getPrintForm(user, flowRun , ft , false, dbConn , imgPath) ;
      sb.append("js:'" + form.get("script") + "'") ;
      sb.append(",css:'" + form.get("css") + "'") ;
      if(flowView.indexOf("1") != -1){
        sb.append(",form:'" + (String)form.get("form")+ "'");
      }
      if(flowView.indexOf("2") != -1){
        if(sb.length() != 1){
          sb.append(",");
        }
        YHAttachmentLogic attachLogic = new YHAttachmentLogic();
        sb.append("attachment:["+ attachLogic.getAttachments(loginUser, flowRun.getRunId() , Integer.parseInt(sFlowId) , dbConn) +"]");
      }
      if(flowView.indexOf("3") != -1){
        if(sb.length() != 1){
          sb.append(",");
        }
        YHFeedbackLogic feedbackLogic = new YHFeedbackLogic();
        String feedbacks = feedbackLogic.getFeedbacks(loginUser, flowRun.getFlowId() , flowRun.getRunId() ,dbConn);
        sb.append("feedbacks:" + feedbacks);
      }
      if(flowView.indexOf("4") != -1){
        if(sb.length() != 1){
          sb.append(",");
        }
        YHMyWorkLogic workLogic = new YHMyWorkLogic();
        workLogic.getPrcsList(flowRun.getRunId(), ft , dbConn , sb);
      }
      sb.append("}");
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功取得数据");
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getWordAndHtml(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    String flowView = request.getParameter("flowView");
    String sRunId = request.getParameter("runId");
    String sFlowId = request.getParameter("flowId");
    String ext = request.getParameter("ext");//扩展名
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFlowRunLogic frl = new YHFlowRunLogic();
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHDocRun flowRun = frl.getFlowRunByRunId(Integer.parseInt(sRunId) , dbConn);
      YHFlowTypeLogic ftl = new YHFlowTypeLogic();
      YHDocFlowType ft = ftl.getFlowTypeById(Integer.parseInt(sFlowId),dbConn);
      if(flowView.indexOf("1") != -1){
        YHPerson user = (YHPerson) request.getSession().getAttribute("LOGIN_USER");
        String imgPath = YHWorkFlowUtility.getImgPath(request);
        Map result = frl.getPrintForm(user, flowRun, ft ,true, dbConn , imgPath) ;
        String  form = (String)result.get("form");
        form = form.replaceAll("\\\\\"", "\"");
        request.setAttribute("form", form);
      }
      if(flowView.indexOf("2") != -1){
        YHAttachmentLogic attachLogic = new YHAttachmentLogic();
        String attachment = attachLogic.getAttachmentsHtml(loginUser, flowRun.getRunId() , dbConn);
        request.setAttribute("attachment", attachment);
      }
      if(flowView.indexOf("3") != -1){
        YHFeedbackLogic feedbackLogic = new YHFeedbackLogic();
        String feedbacks = feedbackLogic.getFeedbacksHtml(loginUser, flowRun.getFlowId() , flowRun.getRunId() ,dbConn);
        request.setAttribute("feedback", feedbacks);
      }
      if(flowView.indexOf("4") != -1){
        YHMyWorkLogic workLogic = new YHMyWorkLogic();
        String prcs =  workLogic.getPrcsHtml(flowRun.getRunId(), ft , dbConn );
        request.setAttribute("prcs", prcs);
      }
      request.setAttribute("ext", ext);
      request.setAttribute("runName", flowRun.getRunName());
    }catch(Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return YHWorkFlowConst.MODULE_CONTEXT_PATH + "/flowrun/list/print/wordAndHtml.jsp";
  }
}
