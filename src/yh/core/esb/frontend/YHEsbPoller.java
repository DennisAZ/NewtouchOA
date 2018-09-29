package yh.core.esb.frontend;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import yh.core.autorun.YHAutoRun;
import yh.core.esb.common.YHDownloadTask;
import yh.core.esb.common.util.ClientPropertiesUtil;
import yh.core.esb.common.util.YHEsbUtil;
import yh.core.esb.frontend.data.YHEsbDownTask;
import yh.core.esb.frontend.data.YHEsbUploadTask;
import yh.core.esb.frontend.logic.YHEsbPollerLogic;
import yh.core.esb.frontend.oa.YHESBMessageServiceCaller;
import yh.core.funcs.doc.util.YHWorkFlowUtility;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.form.YHFOM;

public class YHEsbPoller extends YHAutoRun {
  public static final String PULL_URL = "/yh/yh/core/esb/server/act/YHEsbServerAct/query.act";
  public void doTask() {
    try {
      
      if (!YHEsbFrontend.isOnline()) {
        int code = YHEsbFrontend.login();
        
        if (code != 0) {
          YHEsbUtil.println("YHEsbPoller: 登陆失败,错误代码 - " + code + ", - 等待下一次登陆");
          return;
        }
      }
      YHEsbPollerLogic logic = new YHEsbPollerLogic();
      List<YHEsbDownTask> list = logic.getDownTaskByStatus( "1,-1");
      String tasks = "";
     
      HttpHost host = ClientPropertiesUtil.getHttpHost();
      HttpGet request = new HttpGet(PULL_URL);
      
      HttpResponse response = YHEsbFrontend.getHc().execute(host, request);
      InputStream is = response.getEntity().getContent();
      String res = "";
      byte[] b = new byte[1024];
      for (int i = 0; (i = is.read(b)) > 0;) {
        res += new String(b);
      }
      
      if (!"".equals(res.trim())) {
        Map<String, String> map = YHFOM.json2Map(res.trim());
        if (map != null) {
           tasks = map.get("tasks");
          String msg = null;
          for (Header h : response.getAllHeaders()) {
            if ("SYS-MSG".equals(h.getName())) {
              msg = h.getValue();
              break;
            }
          }
          if (!YHUtility.isNullorEmpty(msg)) {
            //更新状态            Map<String, String> m = YHFOM.json2Map(msg);
           // if ("-4".equals(m.get("code"))) {
             // logic.updateStatus(m.get("guid"), "-3");
            //} else {
              YHESBMessageServiceCaller caller = new YHESBMessageServiceCaller();
              caller.updateState(m.get("guid"), Integer.parseInt(m.get("code")), m.get("to"));
           // }
          }
        }
      }
      else {
        YHEsbUtil.println("没有下载任务");
      }
      
      tasks = this.getTasks(list, tasks);
      if (!YHUtility.isNullorEmpty(tasks)) {
        for (String s : tasks.split(",")) {
          if (s != null && !"".equals(s.trim())) {
          //是否已经上传中
            synchronized(YHEsbFrontend.loc1) {
              boolean uploading  =  YHEsbFrontend.nowDownTask.contains(s);
              if (uploading) {
                continue;
              }
            }
            DefaultHttpClient hc = YHEsbFrontend.getHc();
            YHDownloadTask task = new YHDownloadTask(host, new File(ClientPropertiesUtil.getCacheDir()), s);
            
            boolean flag = task.initialize(hc);
            if (flag) {
              task.transfer(hc);
              int code = task.complete(hc);
              YHEsbUtil.println("EsbPoller: 文件" + s + "下载返回代码 - " + code);
            }
          }
        }
      }
      else {
        YHEsbUtil.println("没有下载任务");
      }
      //is.close();
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public String getTasks(List<YHEsbDownTask> list  , String tasks) {
    for (YHEsbDownTask d : list) {
      if (!this.findId(tasks, d.getGuid())){
        if (YHUtility.isNullorEmpty(tasks) || tasks.endsWith(",")) {
          tasks += d.getGuid() + ",";
        } else {
          tasks += "," + d.getGuid();
        }
      }
    }
    return tasks;
  }
  public  boolean findId(String str, String id) {
    if(str == null || id == null || "".equals(str) || "".equals(id)){
      return false;
    }
    String[] aStr = str.split(",");
    for(String tmp : aStr){
      if(tmp.equals(id)){
        return true;
      }
    }
    return false;
  }
}
