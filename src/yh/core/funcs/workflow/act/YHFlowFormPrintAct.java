package yh.core.funcs.workflow.act;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowRun;
import yh.core.funcs.workflow.data.YHFlowType;
import yh.core.funcs.workflow.logic.YHAttachmentLogic;
import yh.core.funcs.workflow.logic.YHFeedbackLogic;
import yh.core.funcs.workflow.logic.YHFlowRunLogic;
import yh.core.funcs.workflow.logic.YHFlowTypeLogic;
import yh.core.funcs.workflow.logic.YHMyWorkLogic;
import yh.core.funcs.workflow.util.YHPrcsRoleUtility;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;

public class YHFlowFormPrintAct {
  private static Logger log = Logger.getLogger(YHFlowFormPrintAct.class);
  
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
//      String attachmentId = request.getParameter("attachmentId");
//      String attachmentName = request.getParameter("attachmentName");
      
      YHPrcsRoleUtility roleUtility = new YHPrcsRoleUtility();
      //验证是否有权限,并取出权限字符串
      String roleStr = roleUtility.runRole(runId, flowId, prcsId, loginUser , dbConn);
      if ( "".equals(roleStr) ) {//没有权限
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "没有该流程办理权限，请与OA管理员联系");
      } else {
//        YHAttachmentLogic logic = new  YHAttachmentLogic();
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
      YHFlowRun flowRun = (YHFlowRun) orm.loadObjSingle(dbConn, YHFlowRun.class, map);
      String runName = YHWorkFlowUtility.getRunName(flowRun.getRunName());
      String[] ss = YHUtility.null2Empty(flowRun.getAipFiles()).split("\n");
      String s = "[";
      for (String s1 : ss) {
        if ("".equals(s1)) {
          continue;
        }
        String[] tmp = s1.split(":");
        String query = "select T_NAME FROM oa_fl_print_tpl where SEQ_ID='"+tmp[0]+"'";
        Statement stm = null;
        ResultSet rs = null;
        String name = "";
        try {
          stm = dbConn.createStatement();
          log.debug(query);
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
      String data = "{runName:'"+  runName +"',flowId:"+ flowRun.getFlowId() +",delFlag:"+flowRun.getDelFlag()+",aipFile:"+ s +"}";
      log.debug(data);
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
          YHFlowRun flowRun = (YHFlowRun) orm.loadObjSingle(dbConn, YHFlowRun.class, map);
          String ss = YHUtility.null2Empty(flowRun.getAipFiles());
          YHPerson user = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
           //YHFlowPrintLogic logic=new YHFlowPrintLogic();
          String data =this.getTempOptionLogic(dbConn,user,flowId,sRunId);
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
      YHFlowRun flowRun = frl.getFlowRunByRunId(Integer.parseInt(sRunId) , dbConn);
      YHFlowTypeLogic ftl = new YHFlowTypeLogic();
      YHFlowType ft = ftl.getFlowTypeById(Integer.parseInt(sFlowId),dbConn);
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
      log.debug(sb.toString());
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
      YHFlowRun flowRun = frl.getFlowRunByRunId(Integer.parseInt(sRunId) , dbConn);
      YHFlowTypeLogic ftl = new YHFlowTypeLogic();
      YHFlowType ft = ftl.getFlowTypeById(Integer.parseInt(sFlowId),dbConn);
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
    return "/core/funcs/workflow/flowrun/list/print/wordAndHtml.jsp";
  }
  public String getTempOptionLogic(Connection conn,YHPerson person,String flowId,String runId) throws Exception{
    Statement stmt=null;
    ResultSet rs=null;
    Statement stmt1=null;
    ResultSet rs1=null;
    String data="";
  try{
    String sql = "select SEQ_ID,T_NAME,FLOW_PRCS FROM oa_fl_print_tpl WHERE FLOW_ID='"+flowId+"' and T_TYPE = '1'";
   stmt=conn.createStatement();
   rs=stmt.executeQuery(sql);
    while(rs.next())
    {
        String flowPrcs = rs.getString("FLOW_PRCS");
        int seqId=rs.getInt("seq_id");
        String tName=rs.getString("t_name");
        if(YHUtility.isNullorEmpty(flowPrcs)){
          flowPrcs = "0";
        }
         if(flowPrcs.endsWith(",")){
           flowPrcs=flowPrcs.substring(0, flowPrcs.length()-1);
         }
       sql = "select * from oa_fl_run_prcs WHERE RUN_ID='"+runId+"' and USER_ID='"+person.getSeqId()+"' and FLOW_PRCS IN ("+flowPrcs+")";
        stmt1=conn.createStatement();
        rs1=stmt1.executeQuery(sql);
        if(rs1.next()){
          data+="{seqId:'"+seqId+"',tName:'"+tName+"'}";
          data+=",";
        }
          
    }     
  }catch(Exception ex){
    ex.printStackTrace();
  }finally{
    YHDBUtility.close(stmt, rs, null);
    YHDBUtility.close(stmt1, rs1, null);
  }
  if(data.endsWith(",")){
    data=data.substring(0,data.length()-1);
  }
  
 return data;
}
}
