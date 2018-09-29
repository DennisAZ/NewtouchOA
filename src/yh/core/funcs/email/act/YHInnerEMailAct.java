package yh.core.funcs.email.act;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.log4j.Logger;

import yh.core.data.YHDbRecord;
import yh.core.data.YHRequestDbConn;
import yh.core.funcs.email.data.YHEmail;
import yh.core.funcs.email.data.YHEmailBody;
import yh.core.funcs.email.data.YHEmailCont;
import yh.core.funcs.email.logic.YHInnerEMailLogic;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.email.logic.YHWebmailLogic;
import yh.core.funcs.jexcel.util.YHJExcelUtil;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.censorcheck.logic.YHCensorCheckLogic;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.file.YHFileUploadForm;
import yh.core.util.form.YHFOM;

public class YHInnerEMailAct{
  /**
   * log                                               
   */
  private YHInnerEMailUtilLogic emul = new YHInnerEMailUtilLogic();
  private  Logger log = Logger.getLogger(YHInnerEMailAct.class);
  
  /**
   * 列出所有收件箱的信息   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String listInBox(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    int userId = person.getSeqId();
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      Integer styleInSession = (Integer)request.getSession().getAttribute("STYLE_INDEX");
      StringBuffer data = ieml.toInBoxJson(dbConn, request.getParameterMap(), userId,request.getContextPath(),styleInSession);
      PrintWriter pw = response.getWriter();
      pw.println(data.toString());
      pw.flush();
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return null;
  }
  /**
   * 得到下一封/上一封的emailbodyid
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String pageForInBox(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    //System.out.println(person);
    int userId = person.getSeqId();
    try{
      String pageIndexStr = request.getParameter("pageIndex");
      String emailIdStr = request.getParameter("emailId");
      int pageIndex = Integer.valueOf(pageIndexStr.trim());
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      StringBuffer data = ieml.toInBoxPageJson(dbConn, request.getParameterMap(), userId, pageIndex);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  /**
   * 列出未读邮件箱的信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String listNewBox(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    //System.out.println(person);
    int userId = person.getSeqId();
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      Integer styleInSession = (Integer)request.getSession().getAttribute("STYLE_INDEX");
      StringBuffer data = ieml.toNewBoxJson(dbConn, request.getParameterMap(), userId,request.getContextPath(),styleInSession);
      log.debug(data.toString());
      PrintWriter pw = response.getWriter();
      pw.println(data.toString());

      pw.flush();
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return null;
  }
  /**
   * 查询
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String listDelBox(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    //System.out.println(person);
    int userId = person.getSeqId();
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      StringBuffer data = ieml.toDelBoxJson(dbConn, request.getParameterMap(), userId);
      PrintWriter pw = response.getWriter();
      pw.println(data.toString());
      pw.flush();
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return null;
  }
  /**
   * 得到下一封/上一封的emailbodyid
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String pageForDelBox(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    //System.out.println(person);
    int userId = person.getSeqId();
    try{
      String pageIndexStr = request.getParameter("pageIndex");
      int pageIndex = Integer.valueOf(pageIndexStr.trim());
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      StringBuffer data = ieml.toDelBoxPageJson(dbConn, request.getParameterMap(), userId, pageIndex);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String mailDeatil(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String seqIdStr = request.getParameter("seqId");
    int seqId = -1;
    Connection dbConn = null;
    try{
      seqId = Integer.valueOf(seqIdStr);
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHORM orm = new YHORM();
      YHEmailBody eb = (YHEmailBody) orm.loadObjSingle(dbConn, YHEmailBody.class, seqId);
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      ieml.repceipt(dbConn, person.getSeqId(), seqId, String.valueOf(eb.getFromId()), eb.getSubject(),request.getContextPath());
      String data = YHFOM.toJson(eb).toString();
      
      String[] tomail = this.replayMail(dbConn, eb.getSeqId(),person.getSeqId() );
      String data2  = "{data:" + data + ",LoginUser:\""+ person.getSeqId()  + "\",wtomail:\""+ YHUtility.encodeSpecial(tomail[1]) +"\",wccmail:\""+ YHUtility.encodeSpecial(tomail[0]) +"\"}";
      log.debug(data2);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "读取邮件详情成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data2);
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "读取邮件详情错误: " + e.getMessage());
      e.printStackTrace();
    }
    
    return "/core/inc/rtjson.jsp";
  }
  public String[] replayMail(Connection conn , int bodyId , int userId) throws Exception {
    
    String sql = "SELECT TO_MAIL,CC_MAIL,REPLY_MAIL FROM oa_internet_mailbody WHERE BODY_ID = '" + bodyId + "'";
    PreparedStatement ps = null;
    ResultSet rs = null;
    String[] result = new String[2];
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        result[0] = YHUtility.null2Empty(rs.getString(1));
        if (result[0].endsWith(",")) {
          result[0] += YHUtility.null2Empty(rs.getString(2));
        } else {
          if (YHUtility.isNullorEmpty(result[0])) {
            result[0] = YHUtility.null2Empty(rs.getString(2));
          } else {
            result[0] += "," +  YHUtility.null2Empty(rs.getString(2));
          }
        }
        if (result[0].endsWith(",")) {
          result[0] = result[0].substring(0 , result[0].length() - 1);
        }
        result[1] = YHUtility.null2Empty(rs.getString(3));
      } else {
        result[0] = "";
        result[1] = "";
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    String sql2 = "select email from oa_internet_mail where USER_ID ='" + userId + "'";
    PreparedStatement ps2 = null;
    ResultSet rs2 = null;
    try {
      ps2 = conn.prepareStatement(sql2);
      rs2 = ps2.executeQuery();
      while (rs2.next()) {
        String email = YHUtility.null2Empty(rs2.getString("EMAIL"));
        if (!YHUtility.isNullorEmpty(email)) 
          result[0] = this.getOutofStr(result[0],email);
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps2, rs2, null);
    }
    return result;
  }
  public String getOutofStr(String str, String s){
    String[] aStr = str.split(",");
    String strTmp = "";
    int count = 0 ;
    for(String s1 : aStr){
       if (!s1.equals(s) && s1.indexOf("<" + s + ">") == -1) {
         strTmp += s1 + ",";
         count++;
       }
    }
    if (count > 0 ) {
      strTmp = strTmp.substring(0 , strTmp.length() - 1);
    }
    return strTmp;
  }
  /**
   * webmailbody
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String webmailDeatil(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String seqIdStr = request.getParameter("seqId");
    int seqId = -1;
    Connection dbConn = null;
    try{
      seqId = Integer.valueOf(seqIdStr);
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHORM orm = new YHORM();
      YHEmailBody eb = (YHEmailBody) orm.loadObjSingle(dbConn, YHEmailBody.class, seqId);
      
      String[] tomail = this.getToMail(dbConn, seqId);
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      ieml.repceipt(dbConn, person.getSeqId(), seqId, String.valueOf(eb.getFromId()), eb.getSubject(),request.getContextPath());
      String data = YHFOM.toJson(eb).toString();
      String data2  = "{data:" + data + ",LoginUser:\""+ person.getSeqId() + "\" ,tomail:\""+ YHUtility.encodeSpecial(tomail[0]) +"\",ccmail:\""+ YHUtility.encodeSpecial(tomail[1]) +"\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "读取邮件详情成功！");
      request.setAttribute(YHActionKeys.RET_DATA, data2);
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "读取邮件详情错误: " + e.getMessage());
      e.printStackTrace();
    }
    
    return "/core/inc/rtjson.jsp";
  }
  public String[] getToMail(Connection conn , int bodyId) throws Exception {
    String sql = "SELECT TO_MAIL,CC_MAIL FROM oa_internet_mailbody WHERE BODY_ID = '" + bodyId + "'";
    PreparedStatement ps = null;
    ResultSet rs = null;
    String[] result = new String[2];
    try {
      ps = conn.prepareStatement(sql);
      rs = ps.executeQuery();
      if (rs.next()) {
        result[0] = rs.getString(1);
        result[1] = rs.getString(2);
      } else {
        result[0] = "";
        result[1] = "";
      }
    } catch (Exception e) {
      throw e;
    } finally {
      YHDBUtility.close(ps, rs, null);
    }
    return result;
  }
  /**
   * 列出所有草稿箱的信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String listOutBox(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    //System.out.println(person);
    int userId = person.getSeqId();
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      StringBuffer data = ieml.toOutBoxJson(dbConn, request.getParameterMap(), userId);
      PrintWriter pw = response.getWriter();
      pw.println(data.toString());
      pw.flush();
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return null;
  }
  /**
   * 列出所有发件箱的信息
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String listSendBox(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    //System.out.println(person);
    int userId = person.getSeqId();
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      Integer styleInSession = (Integer)request.getSession().getAttribute("STYLE_INDEX");
      StringBuffer data = ieml.toSendBoxJson(dbConn, request.getParameterMap(), userId,request.getContextPath(),styleInSession);
      PrintWriter pw = response.getWriter();
      pw.println(data.toString());
      pw.flush();
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return null;
  }
  /**
   * 得到下一封/上一封的emailbodyid
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String pageForSendBox(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    //System.out.println(person);
    int userId = person.getSeqId();
    try{
      String pageIndexStr = request.getParameter("pageIndex");
      int pageIndex = Integer.valueOf(pageIndexStr.trim());
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      StringBuffer data = ieml.toSendBoxPageJson(dbConn, request.getParameterMap(), userId, pageIndex);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
      throw e;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 保存邮件（写邮件，或发送邮件）
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String sendMail(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    request.setCharacterEncoding("utf-8");
    response.setCharacterEncoding("utf-8");
    Connection conn = null;
    String url = "";
    String msg = "";
    int data = 0;
    try{
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      data = ieml.sendMailLogic(conn, fileForm,person.getSeqId(),YHEmailCont.UPLOAD_HOME,person.getUserName(),request.getContextPath());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮件发送成功！");
    } catch (FileUploadException e) {
      msg = "邮件发送失败，请重发!";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, msg);
      log.debug(msg, e);
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG,  e.getMessage());
      msg = e.getMessage();
      log.debug(msg, e);
    }
    if(data == 0 || data == 3){
      request.setAttribute(YHActionKeys.RET_METHOD, YHActionKeys.RET_METHOD_REDIRECT);
      url = "/core/funcs/email/new/sendOk.jsp?sendFlag=1&msg=" + YHUtility.encodeURL(msg);
    }else if(data == 2){
      request.setAttribute(YHActionKeys.RET_METHOD, YHActionKeys.RET_METHOD_REDIRECT);
      url = YHCensorCheckLogic.getCensorMOD(conn, "0");
    }else{
      request.setAttribute(YHActionKeys.RET_METHOD, YHActionKeys.RET_METHOD_REDIRECT);
      url = YHCensorCheckLogic.getCensorBanned(conn, "0");
    }
    return url;
  }
  
  
  /**
   * add by zyy 张银友  
   * 从工作流中转发email
 * @param request
 * @param response
 * @return
 * @throws Exception
 */
public String sendMailFromFlow(HttpServletRequest request,
		  HttpServletResponse response) throws Exception{
	  request.setCharacterEncoding("utf-8");
	  response.setCharacterEncoding("utf-8");
	  Connection conn = null;
	  String url = "";
	  String msg = "";
	  int data = 0;
	  try{
		  YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
		  YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
		  conn = requestDbConn.getSysDbConn();
		  YHInnerEMailLogic ieml = new YHInnerEMailLogic();
		  Map<String,String[]> requestMap=request.getParameterMap();
	      String imgPath = YHWorkFlowUtility.getImgPath(request);

		  data = ieml.sendMailLogic(conn, requestMap,person,imgPath);
		  request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
		  request.setAttribute(YHActionKeys.RET_MSRG, "邮件发送成功！");
	  }catch(Exception e){
		  request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
		  request.setAttribute(YHActionKeys.RET_MSRG,  e.getMessage());
		  msg = e.getMessage();
		  log.debug(msg, e);
	  }
	  if(data == 0 || data == 3){
		  request.setAttribute(YHActionKeys.RET_METHOD, YHActionKeys.RET_METHOD_REDIRECT);
		  url = "/core/funcs/email/new/sendOk.jsp?sendFlag=1&msg=" + YHUtility.encodeURL(msg);
	  }else if(data == 2){
		  request.setAttribute(YHActionKeys.RET_METHOD, YHActionKeys.RET_METHOD_REDIRECT);
		  url = YHCensorCheckLogic.getCensorMOD(conn, "0");
	  }else{
		  request.setAttribute(YHActionKeys.RET_METHOD, YHActionKeys.RET_METHOD_REDIRECT);
		  url = YHCensorCheckLogic.getCensorBanned(conn, "0");
	  }
	  return url;
  }
  /**
   * 从草稿箱中发送邮件
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String sendMailAll(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection conn = null;
    String ids = request.getParameter("seqIds");
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      ieml.sendMailAll(conn, ids);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮件发送成功！");
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮件发送失败！" + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 存草稿
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String saveMail(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection conn = null;
    YHFileUploadForm fileForm = new YHFileUploadForm();
    fileForm.parseUploadRequest(request);
    String msg = "";
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      ieml.saveMailLogic(conn, fileForm,person.getSeqId(),YHEmailCont.UPLOAD_HOME);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮件保存成功！");
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮件保存失败！" + e.getMessage());
      msg =  e.getMessage();
    }
    return "/core/funcs/email/new/Trans.jsp?sendFlag=2&msg=" + msg;
  }
  /**
   * 存草稿

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String saveMailByTime(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection conn = null;
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      int data = ieml.saveMailByTimeLogic(conn, request.getParameterMap(),person.getSeqId(),YHEmailCont.UPLOAD_HOME);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + data + "\"");
      log.debug("\"" + data + "\"");
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮件保存失败！" + e.getMessage());
      e.printStackTrace();
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 存草稿
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String saveMailByUp(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection conn = null;
    YHFileUploadForm fileForm = new YHFileUploadForm();
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    int bId = -1 ;
    try{
      try {
        fileForm.parseUploadRequest(request);
      } catch (Exception e) {
        return "/core/funcs/email/new/attachFail.jsp";
      }
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      bId = ieml.saveMailLogic(conn, fileForm,person.getSeqId(),YHEmailCont.UPLOAD_HOME);
      request.setAttribute("bId", bId);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮件发送成功！");
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮件发送失败！" + e.getMessage());
      //throw e;
    }
    return "/core/funcs/email/new/index.jsp?resend=2&seqId=" + bId;
  }
  /**
   * 编辑
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String updateMail(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection conn = null;
    String msg = "";
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      ieml.updateMailLogic(conn, fileForm,person.getSeqId(),YHEmailCont.UPLOAD_HOME);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮件发送成功！");
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮件发送失败！" + e.getMessage());
      msg =  e.getMessage();
      //throw e;
    }
    return "/core/funcs/email/new/saveOk.jsp?msg=" + msg;
  }
  public String flag(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String queryParam = request.getParameter("queryParam");
      queryParam = queryParam.trim();
      String[] paramGroup = queryParam.split("/");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
          .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      for (String param : paramGroup) {
        String queryIds = param.trim().split(";")[0];
        String[] querys = param.split(";")[1].split(",");
        String tableName = querys[0].trim();
        String fieldName = querys[1].trim();
        String fieldValue = querys[2].trim();
        if (queryIds.endsWith(",")) {
          queryIds = queryIds.substring(0, queryIds.length() - 1);
        }
        YHInnerEMailUtilLogic ieml = new YHInnerEMailUtilLogic();
        ieml.updateFlag(dbConn, fieldName, fieldValue, queryIds, tableName,person.getSeqId());
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "加载代码成功");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "加载代码失败" + ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  
  public String readFlag(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String mailId = request.getParameter("mailId");
      String fieldValue = request.getParameter("fieldValue");
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      ieml.readFlagLogic(dbConn, mailId, fieldValue);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "加载代码成功");
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "加载代码失败" + ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 删除邮件
   * @param request
   * @param response
   * @return
   */
  public String deletM(HttpServletRequest request,
      HttpServletResponse response){
    Connection dbConn = null;
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deType = request.getParameter("deType");
      String bodyId = request.getParameter("bodyId");
      //int bodyId
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      if(bodyId != null){
        if (bodyId.endsWith(",")) {
          bodyId = bodyId.substring(0, bodyId.length() - 1);
        }
        ieml.doDeleteMail(dbConn, bodyId, deType,person.getSeqId()+"");
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功删除邮件！");
    }catch(Exception e){
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮件删除失败！可能原因：" + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 删除邮件
   * @param request
   * @param response
   * @return
   */
  public String deletMailAll(HttpServletRequest request,
      HttpServletResponse response){
    Connection dbConn = null;
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String deType = request.getParameter("deType");
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      String bodyId = ieml.getIdsForEx(dbConn, Integer.parseInt(deType.trim()),person.getSeqId());
      //int bodyId
      if(bodyId != null){
        ieml.doDeleteMail(dbConn, bodyId, deType,person.getSeqId()+"");
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功删除邮件！");
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮件删除失败！可能原因：" + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 快捷删除邮件
   * @param request
   * @param response
   * @return
   */
  public String deletMailForShort(HttpServletRequest request,
      HttpServletResponse response){
    Connection dbConn = null;
    try {
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String delType = request.getParameter("delType");
      String boxId = request.getParameter("boxId");
      String shortCutType = request.getParameter("shortCutType");
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      int userId = person.getSeqId();
      String bodyId = ieml.delForShortCut(dbConn, person.getSeqId(), delType, shortCutType, boxId);
      if(bodyId != null){
        String[] bodyIds = bodyId.split("\\*");
        for (int i = 0; i < bodyIds.length; i++) {
          if("".equals(bodyIds[i].trim())){
            continue;
          }
          ieml.doDeleteMail(dbConn, bodyIds[i], delType,String.valueOf(userId));
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功删除邮件！");
    }catch(Exception e){
      e.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮件删除失败！可能原因：" + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 得到邮件信息
   * @param request
   * @param response
   * @return
   */
  public String getCount(HttpServletRequest request,
      HttpServletResponse response){
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      StringBuffer data = ieml.getCount2JsonAll(dbConn, person.getSeqId());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
 
  public String getCountIn(HttpServletRequest request,
      HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      StringBuffer data=new StringBuffer();
      boolean result=true;
  
       result=ieml.createView(dbConn);

      if(result ){
        data = ieml.getCountIn(dbConn, person.getSeqId());
     }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    }catch(Exception e){

      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getCountSent(HttpServletRequest request,
      HttpServletResponse response){
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      StringBuffer data = ieml.getCountSent(dbConn, person.getSeqId());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getCountDel(HttpServletRequest request,
      HttpServletResponse response){
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      StringBuffer data = ieml.getCountDel(dbConn, person.getSeqId());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getCountNew(HttpServletRequest request,
      HttpServletResponse response){
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      StringBuffer data = ieml.getCountNew(dbConn, person.getSeqId());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getCountOut(HttpServletRequest request,
      HttpServletResponse response){
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      StringBuffer data = ieml.getCountOut(dbConn, person.getSeqId());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 得到邮件信息
   * @param request
   * @param response
   * @return
   */
  public String getSelfDefBox(HttpServletRequest request,
      HttpServletResponse response){
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      StringBuffer data = ieml.getSelfBoxLogic(dbConn, person.getSeqId());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "[" + data.toString() + "]");
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 附件上传
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public String fileLoad(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    PrintWriter pw = null;
    request.setCharacterEncoding(YHConst.DEFAULT_CODE);
    response.setCharacterEncoding(YHConst.DEFAULT_CODE);
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHFileUploadForm fileForm = new YHFileUploadForm();
      fileForm.parseUploadRequest(request);
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      StringBuffer sb = ieml.uploadMsrg2Json(fileForm, YHEmailCont.UPLOAD_HOME);
      String data = "{'state':'0','data':" + sb.toString() + "}";
      pw = response.getWriter();
      pw.println(data.trim());
      pw.flush();
    }catch(Exception e){
      pw = response.getWriter();
      pw.println("{'state':'1'}".trim());
      pw.flush();
    } finally {
      pw.close();
    }
    return null;
  }
  /**
   * 查看群发邮件状态
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String readStatus(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    request.setCharacterEncoding(YHConst.DEFAULT_CODE);
    response.setCharacterEncoding(YHConst.DEFAULT_CODE);
    String emailBody = request.getParameter("bodyId");
    String type = request.getParameter("type");

    String url = "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      if("1".equals(type)){
        url = "/core/funcs/email/inbox/read_email/readStatus.jsp";
      }else{
        url = "/core/funcs/email/sendbox/read_email/readStatus.jsp";
      }
      YHORM orm = new YHORM();
      int emailBodyId = Integer.valueOf(emailBody.trim());
      YHEmailBody eb = (YHEmailBody) orm.loadObjSingle(dbConn, YHEmailBody.class, emailBodyId);
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      ArrayList<YHEmail> stautsList = (ArrayList<YHEmail>) ieml.toStatusObj(dbConn, emailBodyId,eb);
     
      request.setAttribute("statusList", stautsList);
      request.setAttribute("bodyId", emailBodyId);
    }catch(Exception e){
      throw e;
    }
   
    return url;
  }
  /**
   * 取得邮件的状态
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getMailStatus(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    request.setCharacterEncoding(YHConst.DEFAULT_CODE);
    response.setCharacterEncoding(YHConst.DEFAULT_CODE);
    String emailBody = request.getParameter("bodyId");
    String emailIdStr = request.getParameter("emailId");
    String typeStr = request.getParameter("type");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      int emailBodyId = Integer.valueOf(emailBody.trim());
      int emailId = 0; 
      if(emailIdStr != null && !"".equals(emailIdStr)){
        emailId = Integer.valueOf(emailIdStr.trim());
      }
     
      int type = Integer.valueOf(typeStr.trim());
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      
      int status = ieml.getMailStatus(dbConn, emailBodyId,emailId, person.getSeqId(), type);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + status + "\"");
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮件状态查询失败！可能原因：" + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 得到新邮件数，得到已删除邮件数
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getNewDelCount(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    request.setCharacterEncoding(YHConst.DEFAULT_CODE);
    response.setCharacterEncoding(YHConst.DEFAULT_CODE);
    String boxIdStr = request.getParameter("boxId");
    String typeStr = request.getParameter("type");
    String queryType = request.getParameter("queryType");
    String email = request.getParameter("email");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      int boxId = Integer.valueOf(boxIdStr.trim());
      int type = Integer.valueOf(typeStr.trim());
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      
      int status = ieml.getNewMailCount(dbConn, type, person.getSeqId(), boxId , queryType , email);
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "\"" + status + "\"");
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮件状态查询失败！可能原因：" + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 得到新邮件数，得到已删除邮件数
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String changMail2OtherBox(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    request.setCharacterEncoding(YHConst.DEFAULT_CODE);
    response.setCharacterEncoding(YHConst.DEFAULT_CODE);
    String boxIdStr = request.getParameter("boxId");
    String ids = request.getParameter("bodyIds");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      int boxId = Integer.valueOf(boxIdStr.trim());
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      
      ieml.changeMailBoxLogic(dbConn, boxId, ids, person.getSeqId());
      
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    }catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "邮箱改变失败！可能原因：" + e.getLocalizedMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 桌面模块处理
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deskMoudel(HttpServletRequest request,
      HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String typeStr = request.getParameter("type");
      int type = Integer.parseInt(typeStr);
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      Integer styleInSession = (Integer)request.getSession().getAttribute("STYLE_INDEX");
      StringBuffer data = ieml.getDeskMoudel(dbConn, request.getParameterMap(), person.getSeqId(), type,request.getContextPath(),styleInSession);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "数据取得失败！可能原因：" + e.getLocalizedMessage());
      throw e;
    } 
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 取得最近联系人
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getLastLinkMan(HttpServletRequest request,
      HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      String data = ieml.getLinkMan(dbConn, person.getSeqId());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "数据取得失败！可能原因：" + e.getLocalizedMessage());
      throw e;
    } 
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 根据Email的seqId删除邮件
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String deleteMailByMailId(HttpServletRequest request,
      HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    try {
      String mailId = request.getParameter("mailId");
      String bodyIdStr = request.getParameter("bodyId");
      int bodyId = Integer.parseInt(bodyIdStr.trim());
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      YHORM orm = new YHORM();
      if(mailId != null && !"".equals(mailId) ){
        ArrayList<String> sqls = new ArrayList<String>();
        String isCanDelSeqIds = "";
        ArrayList<YHEmail> emails = (ArrayList<YHEmail>) orm.loadListSingle(dbConn, YHEmail.class, new String[]{" SEQ_ID IN(" + mailId + ")"});
        for (int i = 0; i < emails.size(); i++) {
          YHEmail email = emails.get(i);
          String sql = ieml.deleteMailForOa("2", mailId, email.getDeleteFlag(), email.getReadFlag());
          if("".equals(sql)){
            if(!"".equals(isCanDelSeqIds)){
              isCanDelSeqIds += ",";
            }
            isCanDelSeqIds += email.getSeqId();
          }else{
            sqls.add(sql);
          }
        }
        emul.updateFlag(dbConn, sqls);
        boolean isHasEmail = ieml.isHasEmail(dbConn, bodyId);
        YHWebmailLogic wbml = new YHWebmailLogic();
        if(!"".equals(isCanDelSeqIds)){
          ieml.deleteAll(dbConn, isCanDelSeqIds, "oa_email");
        }
        if(!isHasEmail){
          ieml.deleteAll(dbConn,String.valueOf(bodyId) , "EMALI_BODY");
//          wbml.deleteWebmail(dbConn, String.valueOf(bodyId));
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "数据取得失败！可能原因：" + e.getLocalizedMessage());
      throw e;
    } 
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 检查当前邮件是否被删除
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String checkIsDel(HttpServletRequest request,
      HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    try {
      String mailId = request.getParameter("mailId");
      String type = request.getParameter("type");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHORM orm = new YHORM();
   
      boolean isDel = false;
      if("1".equals(type)){
        YHEmail email = (YHEmail) orm.loadObjSingle(dbConn, YHEmail.class, Integer.valueOf(mailId.trim()));
        if(email!= null){
          String deletFlag = email.getDeleteFlag();
          if("0".equals(deletFlag) || "2".equals(deletFlag)|| "3".equals(deletFlag)|| "4".equals(deletFlag)){
            isDel = false;
          }else{
            isDel = true;
          }
        }else{
          isDel = true;
        }
      }
      String data = "{isDelete:" + isDel + "}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "数据取得失败！可能原因：" + e.getLocalizedMessage());
      throw e;
    } 
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 判断邮箱容量是否已满
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String isFull(HttpServletRequest request,
      HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHInnerEMailLogic iml = new YHInnerEMailLogic();
      StringBuffer data = iml.isFull(dbConn, person.getEmailCapacity(), person.getSeqId());
      //System.out.println(data.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "数据取得失败！可能原因：" + e.getLocalizedMessage());
      throw e;
    } 
    return "/core/inc/rtjson.jsp";
  }
  
  public String getSpareCapacity(HttpServletRequest request,
      HttpServletResponse response)throws Exception{
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
        .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      YHInnerEMailLogic iml = new YHInnerEMailLogic();
      StringBuffer data = iml.getSpareCapacity(dbConn, person.getEmailCapacity(), person.getSeqId());
      //System.out.println(data.toString());
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data.toString());
    } catch (Exception e) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "数据取得失败！可能原因：" + e.getLocalizedMessage());
      throw e;
    } 
    return "/core/inc/rtjson.jsp";
  }
  /**
   * 导出到EXCEL表格中
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String exportToExcel(HttpServletRequest request,HttpServletResponse response) throws Exception{
    OutputStream ops = null;
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String fileName = URLEncoder.encode("内部邮件.xls","UTF-8");
      String emailIds = request.getParameter("emailIds");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      ops = response.getOutputStream();
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      ArrayList<YHDbRecord > dbL = ieml.toExportMailData(conn, emailIds, person.getSeqId());
      YHJExcelUtil.writeExc(ops, dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
      ops.close();
    }
    return null;
  }
  /**
   * 导出到EXCEL 草稿箱 表格中

   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String exportToExcel2(HttpServletRequest request,HttpServletResponse response) throws Exception{
    OutputStream ops = null;
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String fileName = URLEncoder.encode("内部邮件.xls","UTF-8");
      String emailIds = request.getParameter("emailIds");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setContentType("application/vnd.ms-excel");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Content-disposition","attachment; filename=\"" + fileName + "\"");
      ops = response.getOutputStream();
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      ArrayList<YHDbRecord > dbL = ieml.toExportMailData2(conn, emailIds, person.getSeqId());
      YHJExcelUtil.writeExc(ops, dbL);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    } finally {
      ops.close();
    }
    return null;
  }
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String exportToEml(HttpServletRequest request,HttpServletResponse response) throws Exception{
    OutputStream ops = null;
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String fileName = URLEncoder.encode("OA内部邮件(" + YHUtility.getCurDateTimeStr()+ ").zip", "UTF-8");
      String emailIds = request.getParameter("emailIds");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setContentType("application/octet-stream");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Content-Disposition","attachment;filename=\"" + fileName + "\"");
      ops = response.getOutputStream();
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      Map<String, byte[]> map = ieml.toEmlByteMap(conn, person.getSeqId(), emailIds);
      ieml.zip(map, ops);
      ops.flush();
    } catch (Exception ex) {
      throw ex;
    }
    return null;
  }
  /**
   * 草稿箱
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String exportToEml2(HttpServletRequest request,HttpServletResponse response) throws Exception{
    OutputStream ops = null;
    Connection conn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      conn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      String fileName = URLEncoder.encode("OA内部邮件(" + YHUtility.getCurDateTimeStr()+ ").zip", "UTF-8");
      String emailIds = request.getParameter("emailIds");
      fileName = fileName.replaceAll("\\+", "%20");
      response.setHeader("Cache-control","private");
      response.setContentType("application/octet-stream");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Accept-Ranges","bytes");
      response.setHeader("Content-Disposition","attachment;filename=\"" + fileName + "\"");
      ops = response.getOutputStream();
      YHInnerEMailLogic ieml = new YHInnerEMailLogic();
      Map<String, byte[]> map = ieml.toEmlByteMap2(conn, person.getSeqId(), emailIds);
      ieml.zip(map, ops);
      ops.flush();
    } catch (Exception ex) {
      throw ex;
    }
    return null;
  }
 public String getAttachInfo(HttpServletRequest request,HttpServletResponse response) throws Exception{
   Connection conn = null;
   try {
     YHRequestDbConn requestDbConn = (YHRequestDbConn) request
     .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
     conn = requestDbConn.getSysDbConn();
     String embId = request.getParameter("emailId");
     YHInnerEMailLogic ieml  = new YHInnerEMailLogic();
     String data = ieml.getAttachInfoLogic(conn,embId);
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     request.setAttribute(YHActionKeys.RET_DATA, data.toString());
   } catch (Exception e) {
     throw e;
   }
   return "/core/inc/rtjson.jsp";
 }
 /**
  * 
  * @param request
  * @param response
  * @return
  * @throws Exception
  */
 public String transferFolderAct(HttpServletRequest request,HttpServletResponse response) throws Exception{
   try {
     String attachId = request.getParameter("attachId");
     String attachName = request.getParameter("attachName");
     String module = request.getParameter("module");

     if (module == null) {
       module = "email";
     }
     String separator = File.separator;
     String filePath = YHSysProps.getAttachPath() + separator + module + separator; // YHSysProps.getAttachPath()得到
     String folderPath = YHSysProps.getAttachPath() + separator + module;
     YHInnerEMailUtilLogic ieu = new YHInnerEMailUtilLogic();
     String[] attachNameArray = attachName.split("\\*");
     String[] attachIdArray = attachId.split(",");
     String[]  attachInfo = new String[2];
     for (int i = 0; i < attachNameArray.length; i++) {
      if("".equals(attachNameArray[i])){continue;}
      ieu.transferFolder(attachIdArray[i], attachNameArray[i], filePath, folderPath, attachInfo);
     }
     String data = "";
     if(attachInfo != null && attachInfo[0] != null &&  !"".equals(attachInfo[0])){
       data = "{attachId:\"" + attachInfo[0] + "\",attachName:\"" + YHUtility.encodeSpecial(attachInfo[1]) + "\"}";
     }else{
       data = "{}";
     }
     request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
     request.setAttribute(YHActionKeys.RET_DATA, data);
   } catch (Exception e) {
     throw e;
   }
   return "/core/inc/rtjson.jsp";
 }
 
}