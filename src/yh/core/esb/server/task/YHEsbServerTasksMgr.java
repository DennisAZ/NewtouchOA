package yh.core.esb.server.task;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;

import yh.core.autorun.YHAutoRun;
import yh.core.esb.common.data.YHTaskInfo;
import yh.core.esb.common.util.PropertiesUtil;
import yh.core.esb.common.util.YHEsbUtil;
import yh.core.esb.server.logic.YHEsbServerLogic;
import yh.core.util.YHUtility;

public class YHEsbServerTasksMgr extends YHAutoRun {
  public static final int MAX_UPLOAD_TASKS = 500;
  public static final int MAX_DOWNLOAD_TASKS = 500;
  //public static final long MAX_UPLOAD_TIME = PropertiesUtil.getMaxUploadTime();
  //public static final long MAX_DOWNLOAD_TIME = PropertiesUtil.getMaxDownloadTime();
  
  private static Map<String, YHTaskInfo> uploadTasks = new ConcurrentHashMap<String, YHTaskInfo>();
  private static Map<String, Map<Integer, YHTaskInfo>> downloadTasks = new ConcurrentHashMap<String, Map<Integer, YHTaskInfo>>();
  
  public static boolean addUploadTask(String guid, YHTaskInfo task) {
    if (uploadTasks.entrySet().size() < YHEsbServerTasksMgr.MAX_UPLOAD_TASKS) {
      uploadTasks.put(guid, task);
      return true;
    }
    return false;
  }
  public static boolean isDownloading(String guid) {
    return downloadTasks.get(guid) != null;
  }
  /**
   * 添加下载任务(暂时未做数量限制)
   * @param guid
   * @param toId
   * @param task
   * @return
   */
  public static boolean addDownloadTask(String guid, int toId, YHTaskInfo task) {
    Map<Integer, YHTaskInfo> m = downloadTasks.get(guid);
    if (m == null) {
      m = new ConcurrentHashMap<Integer, YHTaskInfo>();
      downloadTasks.put(guid, m);
    }
    m.put(toId, task);
    task.setStartTime(new Date());
    
    return true;
  }
  
  public static void removeUploadTask(String guid) {
    uploadTasks.remove(guid);
  }
  
  public static void removeDownloadTask(String guid) {
    downloadTasks.remove(guid);
  }
  
  public static YHTaskInfo getDownloadTask(String guid, int toId) {
    Map<Integer, YHTaskInfo> m  = downloadTasks.get(guid);
    if (m == null) {
      return null;
    }
    return m.get(toId);
  }
  
  public static YHTaskInfo getUploadTask(String guid) {
    return uploadTasks.get(guid);
  }

  public void doTask() throws Exception {
    YHEsbUtil.println("服务器端超时检查");
    checkUpload(uploadTasks, PropertiesUtil.getMaxUploadTime());
    checkDownload(downloadTasks, PropertiesUtil.getMaxDownloadTime());
    YHEsbUtil.println("上传任务数" + uploadTasks.entrySet().size());
    YHEsbUtil.println("下载任务数" + uploadTasks.entrySet().size());
  }
  
  private void checkUpload(Map<String, YHTaskInfo> tasks, long time) throws Exception {
    for (Entry<String, YHTaskInfo> e : tasks.entrySet()) {
      if (e.getValue() != null) {
        if (new Date().getTime() - e.getValue().getStartTime().getTime() > time) {
          long l = new Date().getTime() - e.getValue().getStartTime().getTime();
          String msg = "文件传送超时:文件发送超过" + l / 1000 + "秒,发送超时";
          YHEsbUtil.println(msg);
          tasks.remove(e.getKey());
          Connection dbConn = requestDbConn.getSysDbConn();
          YHEsbServerLogic.setUploadFailedMessage(dbConn, e.getValue().getGuid(), msg);
        }
      }
      else {
        tasks.remove(e.getKey());
      }
    }
  }
  
  private void checkDownload(Map<String, Map<Integer, YHTaskInfo>> tasks, long time) throws Exception {
    for (Entry<String, Map<Integer, YHTaskInfo>> en : tasks.entrySet()) {
      if (en.getValue() != null) {
        for (Entry<Integer, YHTaskInfo> e : en.getValue().entrySet()) {
          if (new Date().getTime() - e.getValue().getStartTime().getTime() > time) {
            long l = new Date().getTime() - e.getValue().getStartTime().getTime();
            String msg = "文件传送超时:文件下载超过" + l / 1000 + "秒,下载超时";
            YHEsbUtil.println(msg);
            
            en.getValue().remove(e.getKey());
            
            if (en.getValue().entrySet().size() == 0) {
              tasks.remove(en.getKey());
            }
            Connection dbConn = requestDbConn.getSysDbConn();
            YHEsbServerLogic.setDownloadFailedMessage(dbConn, e.getValue().getGuid(), msg, e.getValue().getToId() , e.getValue().getFromId() , true);
          }
        }
      }
      else {
        tasks.remove(en.getKey());
      }
    }
  }
  
  public static void main(String[] argc) {
  }
}
