package yh.core.funcs.system.ispirit.sms.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.email.logic.YHInnerEMailUtilLogic;
import yh.core.funcs.message.logic.YHMessageLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.logic.YHSmsLogic;
import yh.core.funcs.system.ispirit.sms.logic.YHSmsBoxLogic;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;

public class YHSmsBox {
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getNewsSmsForBox(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int toId = person.getSeqId();
      YHSmsBoxLogic sl = new YHSmsBoxLogic();
      String sb = sl.getRemindInBox(dbConn, toId);
      PrintWriter pw = response.getWriter();
      pw.write(sb);
      pw.flush();
      pw.close();
    }catch(Exception ex) {
      throw ex;
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
  public String viewDetails(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    String smsIds = request.getParameter("smsIds");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int toId = person.getSeqId();
      YHSmsBoxLogic sl = new YHSmsBoxLogic();
      List<Map<String, String>> l = sl.viewDetailsLogic(dbConn, smsIds,toId);
      request.setAttribute("pageData", l);
    } catch (Exception e) {
      throw e;
    }
    return "/core/frame/ispirit/nav.jsp";
  }
  
  /**
   * 获取未读邮件和短信的数量
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryUnreadCount(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      PrintWriter pw = response.getWriter();
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      YHInnerEMailUtilLogic emailLogic = new YHInnerEMailUtilLogic();
      int emailCount = emailLogic.getCount(dbConn, " T1.TO_ID ='" + person.getSeqId() + "' "
          + " AND T1.READ_FLAG= '0'" + " AND T1.BOX_ID= 0"
          + " AND T1.DELETE_FLAG IN('0','2') " + " AND T0.SEND_FLAG='1' "
          + " AND T1.BODY_ID = T0.SEQ_ID ", 1, "count(T0.SEQ_ID)");
      
      YHSmsLogic smsLogic = new YHSmsLogic();
      int smsCount = smsLogic.isRemind(dbConn, person.getSeqId());
      
      pw.format("%d|%d", emailCount, smsCount);
      pw.flush();
    } catch (Exception e) {
      throw e;
    }
    return "";
  }
  
  /**
   * 
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String getNewsMessageForBox(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int toId = person.getSeqId();
      YHSmsBoxLogic sl = new YHSmsBoxLogic();
      String sb = sl.getRemindIn(dbConn, toId);
      PrintWriter pw = response.getWriter();
      pw.write(sb);
      pw.flush();
      pw.close();
    }catch(Exception ex) {
      //throw ex;
      ex.printStackTrace();
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
  public String viewMessageDetails(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    String smsIds = request.getParameter("smsIds");
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      int toId = person.getSeqId();
      YHSmsBoxLogic sl = new YHSmsBoxLogic();
      List<Map<String, String>> l = sl.viewDetailsLogic(dbConn, smsIds,toId);
      request.setAttribute("pageData", l);
    } catch (Exception e) {
      throw e;
    }
    return "/core/frame/ispirit/nav.jsp";
  }
  
  /**
   * 获取未读邮件和短信的数量
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String queryMessageUnreadCount(HttpServletRequest request,
      HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    try {
      PrintWriter pw = response.getWriter();
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      
      YHInnerEMailUtilLogic emailLogic = new YHInnerEMailUtilLogic();
      int emailCount = emailLogic.getCount(dbConn, " T1.TO_ID ='" + person.getSeqId() + "' "
          + " AND T1.READ_FLAG= '0'" + " AND T1.BOX_ID= 0"
          + " AND T1.DELETE_FLAG IN('0','2') " + " AND T0.SEND_FLAG='1' "
          + " AND T1.BODY_ID = T0.SEQ_ID ", 1, "count(T0.SEQ_ID)");
      
      YHMessageLogic smsLogic = new YHMessageLogic();
      int smsCount = smsLogic.isRemind(dbConn, person.getSeqId());
      
      pw.format("%d|%d", emailCount, smsCount);
      pw.flush();
    } catch (Exception e) {
      throw e;
    }
    return "";
  }
}
