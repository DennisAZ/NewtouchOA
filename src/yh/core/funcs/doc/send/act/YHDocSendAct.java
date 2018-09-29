package yh.core.funcs.doc.send.act;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.logic.YHPersonLogic;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.funcs.workflow.util.YHConfigLogic;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.funcs.doc.receive.data.YHDocConst;
import yh.core.funcs.doc.send.logic.YHDocLogic;
import yh.core.funcs.doc.send.logic.YHDocSendLogic;
import yh.subsys.oa.rollmanage.logic.YHRmsFileLogic;

/**
 * ddddd
 * @author liuhan
 *
 */
public class YHDocSendAct {
  
  private YHRmsFileLogic logic = new YHRmsFileLogic();
  
  
  private static Logger log = Logger
    .getLogger("yh.core.funcs.doc.send.act.YHDocSendAct");
  /**
   * 取得发文的状态

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getSendState(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String sRunId = request.getParameter("runId");
      String sFlowId = request.getParameter("flowId");
      String sPrcsId = request.getParameter("flowPrcs");
      int prcsId = 0 ;
      int flowId = 0 ;
      int runId = 0 ;
      if (YHUtility.isInteger(sRunId)) {
        runId = Integer.parseInt(sRunId);
      }
      if (YHUtility.isInteger(sFlowId)) {
        flowId = Integer.parseInt(sFlowId);
      }
      if (YHUtility.isInteger(sPrcsId)) {
        prcsId = Integer.parseInt(sPrcsId);
      }
      YHDocLogic logic = new YHDocLogic();
      boolean flag = logic.getSendState(runId, prcsId, flowId, dbConn);
      //加载流程数据
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, " ");
      request.setAttribute(YHActionKeys.RET_DATA, flag + "");
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  } 
  /**
   * 取得发文
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getSendMessage(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String sRunId = request.getParameter("runId");
      YHDocLogic logic = new YHDocLogic();
      String data = logic.getSendMesage(Integer.parseInt(sRunId), dbConn , request.getParameterMap());
      PrintWriter pw = response.getWriter();
      pw.println(data);
      pw.flush();
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  } 
  /**
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRemindInfo(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String seqId = request.getParameter("seqId");
      Statement stm = null;
      ResultSet rs = null;
      String title = "";
      String toDept = "";
      String doc = "";
      String query = "select"
        + " TITLE"
        + " , oa_officialdoc_fl_run.DOC"
        + ", oa_officialdoc_send.DOC_NAME"
        + ", oa_officialdoc_send.DOC_ID"
        + ", TO_DEPT"
        + ", IS_OUT"
        + " from oa_officialdoc_send ,oa_officialdoc_fl_run  where oa_officialdoc_fl_run.RUN_ID = oa_officialdoc_send.RUN_ID AND oa_officialdoc_send.seq_id = " + seqId;
      try {
        stm = dbConn.createStatement();
        rs = stm.executeQuery(query);
        if (rs.next()) {
          title = YHUtility.null2Empty(YHUtility.encodeSpecial(rs.getString("TITLE")));
          doc =  YHUtility.null2Empty(YHUtility.encodeSpecial(rs.getString("DOC")));
          toDept = YHUtility.null2Empty(YHUtility.encodeSpecial(rs.getString("TO_DEPT")));
        }
      } catch (Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, rs, null);
      }
      
      String type = request.getParameter("type");
      String content = "";
      String url = "";
      String users = "";
      if ("1".equals(type)) {
        YHDocSendLogic logic = new YHDocSendLogic();
        String[] depts = new String[1];
        depts[0] = toDept;
        users = logic.getRoleByDepts(dbConn,depts );
        content = "收到来文：" + title + "，对方文号："+ doc +"，请签收！";
        url = "/core/funcs/doc/receive/sign/sign.jsp";
      } else if ("2".equals(type)) {
        users = this.getUsers(dbConn, toDept);
        content = "收到未登记的来文：" + title + "，对方文号："+ doc +"，请尽快登记并办理！";
      } else if ("3".equals(type)) {
        YHDocSendLogic send = new YHDocSendLogic();
        users = send.getRegisterUser(dbConn, seqId);
        content = "你登记的来文：" + title + "，对方文号："+ doc +"，已经收回，请停止办理！";
      }
      YHPersonLogic pL = new YHPersonLogic();
      String userNames = pL.getNameBySeqIdStr(users, dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, content);
      request.setAttribute(YHActionKeys.RET_DATA, "{userIds:\""+YHWorkFlowUtility.getOutOfTail(users)+"\" , userNames:\""+userNames+"\"}");
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  } 
  
  public String getUsers(Connection conn , String deptId) throws Exception {
    Statement stm = null;
    ResultSet rs = null;
    String r = "";
    String query = "select SEQ_ID from PERSON  where DEPT_ID = '" + deptId + "'";
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(query);
      while (rs.next()) {
        r += rs.getInt("SEQ_ID") + ",";
      }
    } catch (Exception ex) {
      throw ex;
    } finally {
      YHDBUtility.close(stm, rs, null);
    }
    return r;
  }
  public String remindUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String toId = request.getParameter("toId");
    String content = request.getParameter("content");
    String type = request.getParameter("type");
    String seqId = request.getParameter("seqId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      String url = "";
      if ("1".equals(type)) {
        url =  "/core/funcs/doc/receive/sign/sign.jsp";
      } else if ("2".equals(type)) {
        url =  "/core/funcs/doc/receive/register/docReg.jsp?rec_seqId=" + seqId;
      } else if ("3".equals(type)) {
      }
      if (!YHUtility.isNullorEmpty(toId)) {
        YHSmsBack sb = new YHSmsBack();
        sb.setSmsType(YHDocConst.remindType);
        sb.setContent(content);
        sb.setFromId(loginUser.getSeqId());
        sb.setToId(toId);
        sb.setRemindUrl(url);
        YHSmsUtil.smsBack(dbConn, sb);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "催办短信已发送 ");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String cancel(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");
    String status = request.getParameter("status");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      YHDocSendLogic send = new YHDocSendLogic();
      send.cancel(dbConn, seqId, status, loginUser.getSeqId());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "催办短信已发送 ");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String resend(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String seqId = request.getParameter("seqId");
    String status = request.getParameter("status");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      YHDocSendLogic send = new YHDocSendLogic();
      send.resend(dbConn, seqId, status, loginUser.getSeqId());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "催办短信已发送 ");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String cancelAll(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String runId = request.getParameter("runId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson loginUser = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      Statement stm = null;
      ResultSet rs = null;
      String query = "select"
        + " SEQ_ID"
        + ", STATUS "
        + " from  oa_officialdoc_send  where RUN_ID  = " + runId + " and is_cancel = '0'";
      try {
        stm = dbConn.createStatement();
        rs = stm.executeQuery(query);
        while (rs.next()) {
           String seqId=  String.valueOf(rs.getInt("SEQ_ID"));
           String status =  rs.getString("STATUS");
           YHDocSendLogic send = new YHDocSendLogic();
           send.cancel(dbConn, seqId, status, loginUser.getSeqId());
        }
      } catch (Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, rs, null);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "催办短信已发送 ");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 取得提醒方式
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getRemindType(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection conn = null;
    try{
      YHPerson loginUser = (YHPerson) request.getSession().getAttribute(
          YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHConfigLogic logic = new YHConfigLogic();
      
      StringBuffer sb = new StringBuffer();
      String paraValue = logic.getSysPar("SMS_REMIND", conn);
      String[] remindArray = paraValue.split("\\|");
      String smsRemind = "";
      String sms2remind = "";
      String smsRemindPrivStr = "";
      
      if (remindArray.length == 1) {
        smsRemind = remindArray[0];
      } else if (remindArray.length  == 2) {
        smsRemind = remindArray[0];
        sms2remind = remindArray[1];
      } else if (remindArray.length == 3) {
        smsRemind = remindArray[0];
        sms2remind = remindArray[1];
        smsRemindPrivStr = remindArray[2];
      }
      boolean smsPriv = false ;
      if (YHWorkFlowUtility.findId(smsRemindPrivStr, "70")) {
        smsPriv = true;
      }
      boolean smsRemindB = false;
      if (YHWorkFlowUtility.findId(smsRemind, "70")) {
        smsRemindB = true;
      }
      boolean sms2RemindB = false;
      if (YHWorkFlowUtility.findId(sms2remind, "70")) {
        sms2RemindB = true;
      }
      sb.append("{smsPriv:"+ smsPriv +",smsRemind:"  + smsRemindB + ", sms2Remind:" + sms2RemindB + "");
      String query = "select TYPE_PRIV,SMS2_REMIND_PRIV from oa_msg2_priv";
      String typePriv = "";
      String sms2RemindPriv = "";
      Statement stm = null;
      ResultSet rs = null;
      try {
        stm = conn.createStatement();
        rs = stm.executeQuery(query);
        if(rs.next()){
          typePriv = rs.getString("TYPE_PRIV");
          sms2RemindPriv = rs.getString("SMS2_REMIND_PRIV");
        }
      } catch(Exception ex) {
        throw ex;
      } finally {
        YHDBUtility.close(stm, rs, null); 
      }
      //检查该模块是否允许手机提醒
      boolean sms2Priv = false ;
      if (YHWorkFlowUtility.findId(typePriv, "70") 
          && YHWorkFlowUtility.findId(sms2RemindPriv , String.valueOf(loginUser.getSeqId()))) {
        sms2Priv = true;
      }
      sb.append (", sms2Priv:" + sms2Priv + "}") ;
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, sb.toString());
      
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  } 
  /**
   * 发文
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String sendDoc(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String runIdStr = request.getParameter("runId");
      int runId = Integer.parseInt(runIdStr);
      String deptId = request.getParameter("deptId");
      String deptId2 = YHUtility.null2Empty(request.getParameter("deptId2"));
      YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHDocSendLogic docSendLogic = new YHDocSendLogic();
      String remindDocSend = request.getParameter("remindDocSend");
      String remindDocSend2 = request.getParameter("remindDocSend2");
      boolean remind = false;
      if (remindDocSend != null && remindDocSend.equals("on")) {
        remind = true;
      }
      boolean remind2 = false;
      if (remindDocSend2 != null && remindDocSend2.equals("on")) {
        remind2 = true;
      }
      String webroot = request.getRealPath("/");
      //String attachmentId = request.getParameter("recDocId");
      //String attachmentName = request.getParameter("recDocName");
      if (!YHUtility.isNullorEmpty(deptId)) {
        docSendLogic.sendDocToDept(deptId, runId, dbConn, webroot, loginUser.getSeqId() ,  remind  , remind2 );
      }
      if (!YHUtility.isNullorEmpty(deptId2)) {
        docSendLogic.sendDocToEsbDept(deptId2, runId, dbConn, webroot, loginUser.getSeqId() ,  remind );
      }
      //docSendLogic.setSendFlag(runId , deptId , deptId2, dbConn);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, " ");
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  } 
}
