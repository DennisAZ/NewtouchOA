package yh.core.esb.server.act;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.esb.common.data.YHTaskInfo;
import yh.core.esb.server.data.YHEsbTransfer;
import yh.core.esb.server.logic.YHEsbServerLogic;
import yh.core.esb.server.task.YHEsbServerTasksMgr;
import yh.core.esb.server.user.data.TdUser;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.auth.YHDigestUtility;

public class YHRangeDownloadAct {
  private static Logger log = Logger.getLogger(YHRangeDownloadAct.class);
  public String initialize(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String fileName = "";
    try {
      HttpSession session = request.getSession();
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String guid = request.getParameter("GUID");
      TdUser user = (TdUser)session.getAttribute("ESB_LOGIN_USER");
      
      if (user == null) {
        log.error("initialize - 服务器发送文件异常,异常信息:用户未登陆");
        return "";
      }
      //int userId = Integer.parseInt(request.getParameter("userId"));
//如果文件正在下载则等待//      if (YHEsbServerTasksMgr.isDownloading(guid)) {
//        return "";
//      }
      //response.setCharacterEncoding("UTF-8");
      YHTaskInfo info = YHEsbServerTasksMgr.getDownloadTask(guid, user.getSeqId());
      YHEsbServerLogic logic = new YHEsbServerLogic();
      boolean hasField = logic.hasStatus(dbConn, guid, user.getSeqId(), YHEsbServerLogic.TRANSFER_STATUS_FAILED);
      
      String message = "";
      if (info == null) {
        if (hasField) {
          response.setHeader("SYS-FIELD", "1");
          return "";
        } else {
          response.setHeader("SYS-FIELD", "0");
        } 
        YHEsbTransfer t = logic.queryDownloadInfo(dbConn, guid);
        
        //更改状态-下载中        logic.setStatus(dbConn, guid, user.getSeqId(), YHEsbServerLogic.TRANSFER_STATUS_DOWNLOADING);
        //更新下载开始时间        logic.setDownloadCreate(dbConn, guid, user.getSeqId());
        File f = new File(t.getFilePath());
        
        info = new YHTaskInfo();
        info.setFile(f);
        info.setType(t.getType());
        info.setGuid(guid);
        info.setFileLength(f.length());
        info.setFromId(t.getFromId());
        info.setFromCode(logic.seqId2UserCode(dbConn, t.getFromId()));
        info.setToId(String.valueOf(user.getSeqId()));
        info.setMd5(YHDigestUtility.md5File(t.getFilePath()));
        YHEsbServerTasksMgr.addDownloadTask(guid, user.getSeqId(), info);
        
        response.addHeader("OptGuid", t.getOptGuid());
        message = t.getMessage();
      }
      else {
        response.addHeader("OptGuid", "");
      }
      //response.setHeader("Content-type", "text/html; charset=utf-8");
      String fileName2 = java.net.URLEncoder.encode(info.getFile().getName(),"UTF-8") ;
      response.addHeader("File-Length", String.valueOf(info.getFile().length()));
      response.addHeader("Content-MD5", YHDigestUtility.md5File(info.getFile().getAbsolutePath()));
      response.addHeader("File-Name", fileName2);
      response.addHeader("File-Type", info.getType());
      response.addHeader("From-ID", info.getFromCode());
      PrintWriter pw = response.getWriter();
      pw.write(message);
      pw.flush();
      pw.close();
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      log.error("initialize - 服务器开始发送文件" + fileName + "异常,异常信息:" + ex.getMessage());
      throw ex;
    }
    return "";
  }
  
  public String transfer(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String fileName = "";
    Connection dbConn = null;
    try {
      HttpSession session =request.getSession();
      String url = request.getParameter("URL");
      String guid = request.getParameter("GUID");
      
      //int userId = Integer.parseInt(request.getParameter("userId"));
       TdUser user = (TdUser)session.getAttribute("ESB_LOGIN_USER");
      String range = request.getParameter("RANGE");
      if (range == null || (url == null && guid == null)) {
        request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
        request.setAttribute(YHActionKeys.RET_MSRG, "参数错误");
        request.setAttribute(YHActionKeys.RET_DATA, "");
        return "";
      }
      
      YHEsbServerLogic logic = new YHEsbServerLogic();
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      boolean hasField = logic.hasStatus(dbConn, guid, user.getSeqId(), YHEsbServerLogic.TRANSFER_STATUS_FAILED);
      File f = null;
      if (guid == null) {
        String webrootDir = request.getSession().getServletContext().getRealPath(File.separator);
        f = new File(webrootDir + url.replaceFirst("/yh", ""));
        if (!f.exists()) {
          request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
          request.setAttribute(YHActionKeys.RET_MSRG, "请求文件不存在");
          request.setAttribute(YHActionKeys.RET_DATA, "");
          return "";
        }
      }
      else {
        YHTaskInfo info = YHEsbServerTasksMgr.getDownloadTask(guid, user.getSeqId());
        if (info == null) {
          if (hasField) {
            response.setHeader("SYS-FIELD", "1");
          }
          return "";
        }
        else {
          f = info.getFile();
        }
      }
      
      
      long start = 0;
      long end = 0;
      String[] ranges = range.split("-");
      try {
        if (ranges.length > 1) {
          start = Long.parseLong(ranges[0]);
          end = Long.parseLong(ranges[1]);
        }
      } catch (NumberFormatException e) {
        start = 0;
        end = 0;
      }
      
      
      if (end > 0 && start < end) {
        if (end > f.length()) {
          end = f.length();
        }
        OutputStream out = response.getOutputStream();
        byte[] bytes = new byte[(int) (end - start)];
        
//        FileInputStream fis = new FileInputStream(f);
//        fis.skip(start);
//        fis.read(bytes);
//        fis.close();
//        Thread.sleep(1000);
        
        RandomAccessFile raf = new RandomAccessFile(f, "r");
        raf.seek(start);
        raf.readFully(bytes);
        raf.close();
        
        String md5 = YHDigestUtility.md5Hex(bytes);
        response.setHeader("Data-Length", String.valueOf(bytes.length));
        response.setHeader("Content-MD5", md5);
        out.write(bytes);
        out.flush();
        out.close();
//        bab.clear();
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功查询桌面属性");
      request.setAttribute(YHActionKeys.RET_DATA, "");
      
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      String msg = "transfer - 服务器发送文件" + fileName + "异常,异常信息:" + ex.getMessage();
      log.error(msg);
      ex.printStackTrace();
      return "";
    }
    return "";
  }
  
  public String complete(HttpServletRequest request, HttpServletResponse response) throws Exception {
    Connection dbConn = null;
    String fileName = "";
    try {
      YHEsbServerLogic logic = new YHEsbServerLogic();
      HttpSession session =request.getSession();
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String guid = request.getParameter("GUID");
      
      
      TdUser user = (TdUser)session.getAttribute("ESB_LOGIN_USER");
      YHTaskInfo info = YHEsbServerTasksMgr.getDownloadTask(guid, user.getSeqId());
      String fromId = "";
      File file = null;
      if (info != null) {
        file = info.getFile();
        fromId = String.valueOf(info.getFromId());
      }
      
      String result = request.getParameter("RESULT");
      
      //此任务是否被超时停止，如果已经超时停止，则阻止其“下载成功”保证此任务此前发送出去的信息是正确的。
      boolean hasField = logic.hasStatus(dbConn, guid, user.getSeqId(), YHEsbServerLogic.TRANSFER_STATUS_FAILED);
      if (!hasField) {
        if ("ok".equalsIgnoreCase(result)) {
          logic.recvCompleted(dbConn, guid,  user.getSeqId(), fromId,  file);
          YHEsbServerTasksMgr.removeDownloadTask(guid);
        } else {
          YHEsbServerLogic.setDownloadFailedMessage(dbConn, guid, "MD5校验失败", String.valueOf( user.getSeqId()), info.getFromId() , false);
        }
      } else {
        response.setHeader("SYS-FIELD", "1");
      }
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      String msg = "complete - 文件" + fileName + "完整性校验异常,异常信息:" + ex.getMessage();
      log.error(msg);
      throw ex;
    }
    return "";
  }
}
