package yh.core.funcs.system.resManage.act;

import java.io.File;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Connection;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.system.resManage.logic.YHResManageLogic;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;

public class YHResManageAct {
  private static Logger log = Logger
    .getLogger("yh.core.funcs.system.resManage.act.YHResManageAct");
  public String getRes(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try{
      String root = "";
      if (YHSysProps.isLinux()) {
        root =  "/";
      } else {
        String rootPath = YHSysProps.getRootPath();
         root = rootPath.substring(0,3);
      }
      File file = new File(root);
      
      long space = file.getFreeSpace() ;
      long container = file.getTotalSpace();
      long used = container - space;
      DecimalFormat format = new DecimalFormat("0.0"); 
      
      double usedGb = (double) used / 1024 / 1024 / 1024;
      double spaceGb = (double) space / 1024 / 1024 / 1024;
      double containerGb = (double) container / 1024 / 1024 / 1024;
      DecimalFormat formatL = new DecimalFormat("#,##0");
      String data = "{space:'" + formatL.format(space) +"',container:'" + formatL.format(container) + "',used:'" + formatL.format(used) + "',spaceGb:"+format.format(spaceGb)+",usedGb:"+format.format(usedGb)+",containerGb:"+format.format(containerGb)+"}";
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getModuleData(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHResManageLogic logic = new YHResManageLogic();
      String data = logic.getModuleData(dbConn);
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, data);
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String deleteDelBox(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person =  (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHResManageLogic logic = new YHResManageLogic();
      logic.deleteDelBox(dbConn);
      logic.woriteLog(dbConn, "清空所有内部邮件废件箱邮件", person.getSeqId() , this.getIpAddr(request));
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String deleteDeletedDelbox(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person =  (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHResManageLogic logic = new YHResManageLogic();
      logic.deleteDeletedDelbox(dbConn);
      logic.woriteLog(dbConn, "清空收件人已删内部邮件", person.getSeqId() , this.getIpAddr(request));
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String deleteSms(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person =  (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHResManageLogic logic = new YHResManageLogic();
      logic.deleteSms(dbConn);
      logic.woriteLog(dbConn, "删除所有已读内部短信", person.getSeqId() , this.getIpAddr(request));
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String deleteDeletedSms(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person =  (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHResManageLogic logic = new YHResManageLogic();
      logic.deleteDeletedSms(dbConn);
      logic.woriteLog(dbConn, "清空收信人已删内部短信", person.getSeqId() , this.getIpAddr(request));
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getDeptRes(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String deptIds = request.getParameter("deptId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int deptId = 0;
      if (YHUtility.isInteger(deptIds)) {
        deptId = Integer.parseInt(deptIds);
      }
      YHResManageLogic logic = new YHResManageLogic();
      String data = logic.getDeptRes(dbConn , deptId);
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
      
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getUserRes(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String userIds = request.getParameter("userId");
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      int userId = 0;
      if (YHUtility.isInteger(userIds)) {
        userId = Integer.parseInt(userIds);
      }
      YHResManageLogic logic = new YHResManageLogic();
      String data = logic.getUserRes(dbConn , userId);
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
      
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String delGarbageCon(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person =  (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHResManageLogic logic = new YHResManageLogic();
      String data = logic.delGarbageCon(dbConn);
      String remark = "系统垃圾清理[存在以下用户的系统垃圾：" + data + "]"; 
      logic.woriteLog(dbConn, remark, person.getSeqId() , this.getIpAddr(request));
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, "'" + data + "'" );
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String deleteGarbage(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person =  (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHResManageLogic logic = new YHResManageLogic();
      String data = logic.delGarbage(dbConn);
      logic.woriteLog(dbConn, data, person.getSeqId() , this.getIpAddr(request));
      String CONFIRM_FLAG = request.getParameter("CONFIRM_FLAG");
      response.setContentType("text/html");
      response.setCharacterEncoding("UTF-8");
      PrintWriter out = response.getWriter();
      if ("Y".equals(CONFIRM_FLAG)) {
         out.print("<body onload=\"parent.delete_return(\'"+data+"\')\"></body>");
      } else {
        out.print("<body onload=\"parent.delete_return(\'"+data+"\')\"></body>");
      }
      out.flush();
      out.close();
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  public String openUser(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
 
      YHResManageLogic logic = new YHResManageLogic();
      String data = logic.getUser(dbConn);
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA,data);
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String delRes(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person =  (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String endDate = request.getParameter("END_DATE");
      String startDate = request.getParameter("START_DATE");
      String sms = request.getParameter("SMS");
      String email = request.getParameter("EMAIL");
      String emailInbox = request.getParameter("EMAIL_INBOX");
    
      
      YHResManageLogic logic = new YHResManageLogic();
      String data = logic.delRs(dbConn ,  endDate , startDate , sms ,email , emailInbox);
      logic.woriteLog(dbConn, data, person.getSeqId() , this.getIpAddr(request));
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String delUserRes(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHPerson person =  (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
      String toId = request.getParameter("TO_ID");
      String address = request.getParameter("ADDRESS");
      String calendar = request.getParameter("CALENDAR");
      String diary = request.getParameter("DIARY");
      String url = request.getParameter("URL");
      String email = request.getParameter("EMAIL");
      String folder = request.getParameter("FOLDER");
      YHResManageLogic logic = new YHResManageLogic();
      String data = logic.delUserRs(dbConn ,toId, address,calendar, diary ,url, email, folder );
      logic.woriteLog(dbConn, data, person.getSeqId() , this.getIpAddr(request));
      request.setAttribute(YHActionKeys.RET_STATE,YHConst.RETURN_OK);
    } catch (Exception ex){
       request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return "/core/inc/rtjson.jsp";
  }
  public String batRes(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String endDate = request.getParameter("END_DATE");
      String startDate = request.getParameter("START_DATE");
      String sms = request.getParameter("SMS");
      String email = request.getParameter("EMAIL");
      String emailInbox = request.getParameter("EMAIL_INBOX");
      String path = request.getParameter("EXPORT_PATH");
      
      YHResManageLogic logic = new YHResManageLogic();
      String data = logic.batRes(dbConn ,  endDate , startDate , sms ,email , emailInbox , path);
      response.setContentType("application/octet-stream");
      response.setHeader("Cache-control","private");
      response.setHeader("Cache-Control","maxage=3600");
      response.setHeader("Pragma","public");
      response.setHeader("Accept-Ranges","bytes");
      response.setCharacterEncoding("GBK");
      String fileName = URLEncoder.encode("内部邮件.bat", "UTF-8");
      response.setHeader("Content-Disposition", "attachment; filename=" + fileName );
      PrintWriter out = response.getWriter();
      if (!"".equals(data)) {
        out.print("@echo 正在导出内部邮件附件...\r\n");
        out.print(data);
        out.print("\r\n@echo 内部邮件附件导出完毕!\r\npause");
      } else {
        out.print("@echo 无符合条件的内部邮件附件\r\npause");
      }
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  public String batUserRes(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn)request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String diary = request.getParameter("DIARY");
      String email = request.getParameter("EMAIL");
      String folder = request.getParameter("FOLDER");
      String path = request.getParameter("EXPORT_PATH");
      String toId = request.getParameter("TO_ID");
      
      YHResManageLogic logic = new YHResManageLogic();
      String data = logic.batUserRes(dbConn ,  diary , email ,folder , toId, path);
      response.setContentType("application/octet-stream");
      response.setHeader("Cache-control","private");
      response.setHeader("Accept-Ranges","bytes");
      String fileName = URLEncoder.encode("指定用户.bat", "UTF-8");
      response.setCharacterEncoding("GBK");
      response.setHeader("Content-Disposition", "attachment; filename=" + fileName );
      PrintWriter out = response.getWriter();
      if (!"".equals(data)) {
        out.print("@echo 正在导出附件...\r\n");
        out.print(data);
        out.print("\r\n@echo 附件导出完毕!\r\npause");
      } else {
        out.print("@echo 无符合条件的附件\r\npause");
      }
    } catch (Exception ex){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      throw ex;
    }
    return null;
  }
  /**
   * 取得IP地址
   * @param request
   * @return
   */
  public static String getIpAddr(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }
}
