package yh.core.esb.server.act;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import yh.core.data.YHRequestDbConn;
import yh.core.esb.common.util.YHEsbUtil;
import yh.core.esb.server.data.YHEsbSysMsg;
import yh.core.esb.server.data.YHEsbTransfer;
import yh.core.esb.server.data.YHEsbTransferStatus;
import yh.core.esb.server.logic.YHEsbServerLogic;
import yh.core.esb.server.user.data.TdUser;
import yh.core.global.YHBeanKeys;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;

public class YHEsbServerAct {
  public static final String SESSION_DOWNLOAD_MAP = "Download-map";
  public static final int DOWNLOADS_LIMIT = 20;
  /**
   * 查询有无该应用的文件
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public String query(HttpServletRequest request, HttpServletResponse response) throws Exception{
    HttpSession session = request.getSession();
    Connection dbConn = null;
    try {
      TdUser user = (TdUser)session.getAttribute("ESB_LOGIN_USER");
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHEsbServerLogic logic = new YHEsbServerLogic();
      if (user == null) {
        YHEsbUtil.println("YHEsbServerAct: 登陆失败 客户端地址 - " + request.getRemoteAddr());
        return "";
      }
      List<YHEsbTransfer> status = logic.queryDownloadTask(dbConn, user.getSeqId());
      PrintWriter pw = response.getWriter();
      
      Map<String, String> result = new HashMap<String, String>();
      
      String tasks = "";
      String msg = "";
      for (YHEsbTransfer s : status) {
        if (s == null) {
          continue;
        }
        tasks += s.getGuid();
        tasks += ",";
      }
      
      List<YHEsbSysMsg> list = logic.querySysMsg(dbConn, user.getSeqId());
      for (YHEsbSysMsg m : list) {
        response.setHeader("SYS-MSG", m.getContent());
        logic.setSysMsgStatus(dbConn, m.getSeqId(), "1");
        break;
        //暂时一次执行一个命令      }
      result.put("tasks", tasks);
      pw.write(YHFOM.toJson(result).toString());
      pw.flush();
      pw.close();
    } catch (Exception e) {
      throw e;
    }
    return "";
  }
  
  public String isOnline(HttpServletRequest request, HttpServletResponse response) throws Exception{
    HttpSession session = request.getSession();
    Connection dbConn = null;
    try {
      TdUser user = (TdUser)session.getAttribute("ESB_LOGIN_USER");
      String res = "";
      if (user != null) {
        res = "{\"user\": \" " + user.getSeqId() + " \"}";
      }
     // System.out.println(res);
      response.getWriter().write(res);
    } catch (Exception e) {
      throw e;
    }
    return "";
  }
  public String reSend (HttpServletRequest request, HttpServletResponse response) throws Exception{
    HttpSession session = request.getSession();
    Connection dbConn = null;
    try {
      TdUser user = (TdUser)session.getAttribute("ESB_LOGIN_USER");
      YHEsbServerLogic logic = new YHEsbServerLogic();
      if (user == null) {
        YHEsbUtil.println("YHEsbServerAct: 登陆失败 客户端地址 - " + request.getRemoteAddr());
        return "";
      }
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String guid = request.getParameter("guid");
      String toId = request.getParameter("toId");
      
      if (logic.isTransferTaskField(dbConn, guid)) {
        logic.setTransStatus(dbConn, guid, "1");
      } else {
        logic.setStatus(dbConn, guid, toId, YHEsbServerLogic.TRANSFER_STATUS_READY);
      }
      PrintWriter pw = response.getWriter();
      Map<String , String> result = new HashMap();
      result.put("rtState", "0");
      pw.write(YHFOM.toJson(result).toString());
      pw.flush();
      pw.close();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return "";
  }
  public String reDown (HttpServletRequest request, HttpServletResponse response) throws Exception{
    HttpSession session = request.getSession();
    Connection dbConn = null;
    try {
      TdUser user = (TdUser)session.getAttribute("ESB_LOGIN_USER");
      YHEsbServerLogic logic = new YHEsbServerLogic();
      if (user == null) {
        YHEsbUtil.println("YHEsbServerAct: 登陆失败 客户端地址 - " + request.getRemoteAddr());
        return "";
      }
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String guid = request.getParameter("guid");
      int toId = user.getSeqId();
          
      logic.setStatus2(dbConn, guid, toId, YHEsbServerLogic.TRANSFER_STATUS_READY);
      
      PrintWriter pw = response.getWriter();
      Map<String , String> result = new HashMap();
      result.put("rtState", "0");
      pw.write(YHFOM.toJson(result).toString());
      pw.flush();
      pw.close();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return "";
  }
}
