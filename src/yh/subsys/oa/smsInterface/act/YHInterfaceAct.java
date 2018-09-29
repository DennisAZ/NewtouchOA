package yh.subsys.oa.smsInterface.act;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.mobilesms.logic.YHMobileSms2Logic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.sms.data.YHSmsBack;
import yh.core.funcs.sms.logic.YHSmsUtil;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;

public class YHInterfaceAct {

  /**
   * 集成oa内部短信接口
   * 
   * @param content
   * @param fromId
   * @param toId
   * @param smsType
   * @param remindUrl
   * @return
   * @throws Exception
   */
  public String getSmsInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String smsContent = request.getParameter("content");
      String fromIdStr = request.getParameter("fromId");
      int fromId = Integer.parseInt(fromIdStr.substring(0, fromIdStr.length()-1));
      String toId = request.getParameter("toId");
      String smsType = request.getParameter("smsType");
      String remindUrl = request.getParameter("remindUrl").replaceAll("@", "&");
      
      request.getHeader("Referer");
      if(remindUrl.startsWith("1:")){
        String temp[] = request.getHeader("Referer").split("/");
        remindUrl = temp[0] + "//" + temp[1] + temp[2] + "/" + temp[3] + "/" + remindUrl.substring(2, remindUrl.length());
      }
      
      doSmsBackTime(dbConn, smsContent, fromId, toId, smsType, remindUrl, new Date());
      
      PrintWriter pw = response.getWriter();
      pw.println("var xxx = true;");
      pw.flush();
      
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  
  
  public String getSmsPhoneInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String smsContent = request.getParameter("content");
      String fromIdStr = request.getParameter("fromId");
      int fromId = Integer.parseInt(fromIdStr.substring(0, fromIdStr.length()-1));
      String toId = request.getParameter("toId");
      
      YHMobileSms2Logic sbl = new YHMobileSms2Logic();
      sbl.remindByMobileSms(dbConn, toId, fromId, smsContent, new Date());
      
      PrintWriter pw = response.getWriter();
      pw.println("var xxx = true;");
      pw.flush();
      
    } catch (Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  
  /**
   * 短信提醒(带时间)
   * 
   * @param conn
   * @param content
   * @param fromId
   * @param toId
   * @param type
   * @param remindUrl
   * @param sendDate
   * @throws Exception
   */
  public static void doSmsBackTime(Connection conn, String content, int fromId, String toId, String type, String remindUrl, Date sendDate)
      throws Exception {
    YHSmsBack sb = new YHSmsBack();
    sb.setContent(content);
    sb.setFromId(fromId);
    sb.setToId(toId);
    sb.setSmsType(type);
    sb.setRemindUrl(remindUrl);
    sb.setSendDate(sendDate);
    YHSmsUtil.smsBack(conn, sb);
  }
}
