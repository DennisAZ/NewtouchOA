package yh.core.esb.server.act;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.util.ByteArrayBuffer;
import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.esb.common.YHEsbTaskInfo;
import yh.core.esb.common.data.YHTaskInfo;
import yh.core.esb.common.util.PropertiesUtil;
import yh.core.esb.common.util.YHEsbUtil;
import yh.core.esb.common.util.YHSerializer;
import yh.core.esb.server.logic.YHEsbServerLogic;
import yh.core.esb.server.task.YHEsbServerTasksMgr;
import yh.core.esb.server.user.data.TdUser;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHDigestUtility;

public class YHRangeUploadAct {
  private static Logger log = Logger.getLogger(YHRangeUploadAct.class);
  public static String UPLOAD_PATH = PropertiesUtil.getUploadPath();
  
  public String initialize(HttpServletRequest request, HttpServletResponse response) throws Exception {
    long begin = System.currentTimeMillis();
    String contentName = "";
    String fileSeqId = "";
    try {
      HttpSession session = request.getSession();
      String contentLength = request.getParameter("Content-length");
      long len = Long.parseLong(contentLength);
      contentName = request.getParameter("Content-name");
      String md5 = request.getParameter("MD5");
      String toId = request.getParameter("TO_ID");
      fileSeqId = request.getParameter("FileSeqId");
      String optGuid = YHUtility.null2Empty(request.getParameter("optGuid"));
      String message = YHUtility.null2Empty(request.getParameter("message"));
      
      String guid = request.getParameter("GUID");
      if (guid == null) {
        guid = UUID.randomUUID().toString();
      }
      
      TdUser user = (TdUser)session.getAttribute("ESB_LOGIN_USER");
      if (user == null) {
        log.error(request.getAttribute(YHActionKeys.RET_MSRG));
        log.error("initialize - 服务器接收文件异常,异常信息:用户未登陆");
      }
      int userId = user.getSeqId();
      
      File dir = new File(UPLOAD_PATH + File.separator + guid);
      if (!dir.exists()) {
       dir.mkdirs();
      }
      File file = new File(UPLOAD_PATH + File.separator + guid + File.separator + contentName);
      //开始上传的时候或者重新上传
      YHTaskInfo info = YHEsbServerTasksMgr.getUploadTask(guid);
      //续传
      String ss = "";
      if ( info != null  ) {
        ss = info.hasDone();
      } 
      Connection dbConn = null;
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      YHEsbServerLogic logic = new  YHEsbServerLogic();
      boolean flag = logic.hasTransferTask(dbConn, guid);
      if ( info == null ) {
        if (flag && logic.isTransferTaskField(dbConn, guid)) {
          response.setHeader("SYS-FIELD", "1");
        } else {
          response.setHeader("SYS-FIELD", "0");
        }
        
        info = new YHTaskInfo();
        info.setFile(file);
        info.setFileLength(len);
        info.setGuid(guid);
        info.setMd5(md5);
        info.setFromId(userId);
        
        ByteBuffer bab = ByteBuffer.allocate((int)len);
        info.setBytes(bab);
        YHEsbServerTasksMgr.addUploadTask(guid, info);
      } 
      toId = logic.codeStr2IdStr(dbConn, toId);
      info.setToId(toId);
      if (!flag){
        logic.addTransferTask(dbConn, guid, userId, info.getFile().getAbsolutePath(), info.getContent(), "0", info.getToId() , optGuid , message);
      } 
      
      response.addHeader("Content-GUID", guid);
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      PrintWriter pw = response.getWriter();
      pw.write(ss);
      pw.flush();
      pw.close();
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      log.error("initialize - 服务器开始接收文件" + contentName + "异常,异常信息:" + ex.getMessage());
      throw ex;
    }finally {
      long end = System.currentTimeMillis();
      int lastTime = (int)(end - begin);
      //System.out.println("File " + fileSeqId + " init use " + lastTime + " ms");
    }
    return "";
  }
  
  public String transfer(HttpServletRequest request, HttpServletResponse response) throws Exception {
    long begin = System.currentTimeMillis();
    String fileName = "";
    String fileInfo = "";
    Connection dbConn = null;
    try {
      int contentLength = Integer.parseInt(request.getHeader("Con-length"));
      String guid = request.getHeader("GUID");
      YHTaskInfo info = YHEsbServerTasksMgr.getUploadTask(guid);
      String md5 = request.getHeader("MD5");
      String start = request.getHeader("START");
      fileInfo = request.getHeader("FileInfo");
      String no = request.getHeader("NO");
      
      if (info != null) {
        InputStream is = request.getInputStream();
        ByteArrayBuffer bab = new ByteArrayBuffer(contentLength);
        byte[] tmp = new byte[1024];
        for (int i = 0; (i = is.read(tmp)) > 0;) {
          bab.append(tmp, 0, i);
        }
        
        if (YHDigestUtility.isMatch(bab.toByteArray(), md5)) {
          info.getDateCache().put(start, bab.toByteArray());
          if (YHUtility.isInteger(no)) {
            //完成一个线程
            info.done(Integer.parseInt(no));
          }
        } else {
          response.setStatus(300);
        }
        bab.clear();
      }
      else {
        //文件上传超时
        YHEsbServerLogic logic = new  YHEsbServerLogic();
        YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
        dbConn = requestDbConn.getSysDbConn();
        boolean flag = logic.isTransferTaskField(dbConn, guid);
        if (flag) {
          response.setStatus(301);
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "成功查询桌面属性");
      request.setAttribute(YHActionKeys.RET_DATA, "");
    }catch(Exception ex) {
      ex.printStackTrace();
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      log.error("transfer - 服务器接收文件" + fileName + "异常,异常信息:" + ex.getMessage());
      throw ex;
    }finally {
      long end = System.currentTimeMillis();
      int lastTime = (int)(end - begin);
      //System.out.println(fileInfo + " use " + lastTime + " ms");
    }
    return "";
  }
  
  public String complete(HttpServletRequest request, HttpServletResponse response) throws Exception {
    long begin = System.currentTimeMillis();
    String fileSeqId = "";
    Connection dbConn = null;
    String fileName= "";
    try {
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      String guid = request.getParameter("GUID");
      fileSeqId = request.getParameter("FileSeqId");
      YHTaskInfo info = YHEsbServerTasksMgr.getUploadTask(guid);
      if (info != null) {
        File file = info.getFile();
        writeByte(info.getDateCache(), info.getBytes());
        writeFile(file, info.getBytes().array());
        
        YHEsbServerLogic logic2 = new  YHEsbServerLogic();
        boolean flag = logic2.hasTransferTask(dbConn, guid);
        if (flag && logic2.isTransferTaskField(dbConn, guid)) {
          response.setHeader("SYS-FIELD", "1");
          response.setStatus(400);
        } else {
          if (YHDigestUtility.isMatch(info.getBytes().array(), info.getMd5())) {
            YHEsbUtil.println("YHRangeUploadAct: MD5校验成功");
            response.setStatus(200);
            YHEsbServerLogic logic = new YHEsbServerLogic();
            logic.uploadComplete(dbConn, info);
            info.getBytes().clear();
            YHEsbServerTasksMgr.removeUploadTask(guid);
          } else {
            YHEsbUtil.println("YHRangeUploadAct: MD5校验失败");
            response.setStatus(300);
            YHEsbServerTasksMgr.removeUploadTask(guid);
            String msg = "文件完整性校验失败";
            YHEsbServerLogic.setUploadFailedMessage(dbConn, info.getGuid(), msg);
          }
        }
      } else {
        response.setStatus(301);
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "");
      request.setAttribute(YHActionKeys.RET_DATA, "");
    }catch(Exception ex) {
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());
      log.error("complete - 文件" + fileName + "完整性校验异常,异常信息:" + ex.getMessage());
      throw ex;
    }finally {
      long end = System.currentTimeMillis();
      int lastTime = (int)(end - begin);
      //System.out.println("File " + fileSeqId + " terminate use " + lastTime + " ms");
    }
    return "";
  }
  /**
   * 写byte，加上同步锁很重要
   * @param start
   * @param bytes
   * @throws IOException
   * @throws InterruptedException
   */
 static private void writeByte(Map<String , byte[]> dataCache ,  ByteBuffer bb) throws IOException, InterruptedException {
    Set<String> keys = dataCache.keySet();
    Set<Integer> treeKey = new TreeSet();
    for (String str : keys) {
      Integer it = Integer.parseInt(str);
      treeKey.add(it);
    }
    for (Integer it : treeKey) {
      byte[] data = dataCache.get(it + "");
      bb.put(data);
    }
    dataCache.clear();
  }
  
  /**
   * 写文件，加上同步锁很重要
   * @param start
   * @param bytes
   * @throws IOException
   * @throws InterruptedException
   */
  synchronized static private void writeFile(File file ,  byte[] bts) throws IOException, InterruptedException {
    FileOutputStream out = new FileOutputStream(file);
    out.write(bts);
    out.flush();
    out.close();
  }
  public YHEsbTaskInfo getTaskInfo(File file) {
    File dir = file.getParentFile();
    YHEsbTaskInfo taskInfo = null;
    for (File f: dir.listFiles()) {
      String name = f.getName();
      if (!YHUtility.isNullorEmpty(name)) {
        if (name.endsWith(".esb")) {
          YHSerializer<YHEsbTaskInfo> s = new YHSerializer<YHEsbTaskInfo>();
          try {
            taskInfo = s.deserialize(f);
          } catch (Exception e) {
            e.printStackTrace();
          }
          break;
        }
      }
    }
    if (taskInfo == null) {
      taskInfo = new YHEsbTaskInfo();
    }
    return taskInfo;
  }
  
  private void saveTaskInfo(File file ,YHEsbTaskInfo taskInfo) throws IOException {
    File dir = file.getParentFile();
    YHSerializer<YHEsbTaskInfo> s = new YHSerializer<YHEsbTaskInfo>();
    String path = dir.getAbsolutePath() + File.separator + "taskinfo.esb";
    s.serialize(new File(path), taskInfo);
  }
}