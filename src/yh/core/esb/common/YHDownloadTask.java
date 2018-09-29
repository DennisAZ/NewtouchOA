package yh.core.esb.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import yh.core.esb.common.util.ClientPropertiesUtil;
import yh.core.esb.common.util.PropertiesUtil;
import yh.core.esb.common.util.YHEsbUtil;
import yh.core.esb.common.util.YHHttpClientUtil;
import yh.core.esb.common.util.YHSerializer;
import yh.core.esb.frontend.logic.YHEsbPollerLogic;
import yh.core.esb.frontend.oa.YHESBMessageServiceCaller;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHDigestUtility;

public class YHDownloadTask {
  public static String LOGIN_URL = "/yh/yh/core/esb/server/system/act/SystemLoginAct/doLogin.act";
  public static final String DOWNLOAD_TRANSFER_URL = "/yh/yh/core/esb/server/act/YHRangeDownloadAct/transfer.act";
  public static final String DOWNLOAD_INITIALIZE_URL = "/yh/yh/core/esb/server/act/YHRangeDownloadAct/initialize.act";
  public static final String DOWNLOAD_COMPLETE_URL = "/yh/yh/core/esb/server/act/YHRangeDownloadAct/complete.act";
  private File file;
  private File dir;
  private HttpHost host;
  private String guid;
  private String type;
  private String md5;
  private String fromId;
  private long length;
  private int completeResumeCnt;
  private YHEsbTaskInfo taskInfo;
  private int count = 0;
  
  public YHDownloadTask(HttpHost host, File dir, String guid) {
    this.host = host;
    this.dir = dir;
    this.guid = guid;
  }
  
  public void initTaskInfo() {
    if (file != null) {
      File dir = file.getParentFile();
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
    }
    if (taskInfo == null) {
      taskInfo = new YHEsbTaskInfo();
    }
  }
  
  private void saveTaskInfo() throws IOException {
    File dir = file.getParentFile();
    YHSerializer<YHEsbTaskInfo> s = new YHSerializer<YHEsbTaskInfo>();
    String path = dir.getAbsolutePath() + File.separator + "taskinfo.esb";
    s.serialize(new File(path), taskInfo);
  }
  
  public boolean initialize(DefaultHttpClient hc) throws Exception {
    HttpResponse response = null;
    HttpPost post = null;
    try {
      post = new HttpPost(DOWNLOAD_INITIALIZE_URL);
      List<NameValuePair> nvps = new ArrayList<NameValuePair>();
      nvps.add(new BasicNameValuePair("GUID", guid));
      
      post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
      response = hc.execute(host, post);
      
      String optGuid = "";
      String message = "";
        
      boolean flag = false;
      if (response == null) {
        
      } else {
        Header h0 = response.getFirstHeader("File-Length");
        Header h1 = response.getFirstHeader("Content-MD5");
        Header h2 = response.getFirstHeader("File-Name");
        Header h3 = response.getFirstHeader("File-Type");
        Header h4 = response.getFirstHeader("From-ID");
        Header h5 = response.getFirstHeader("SYS-FIELD");
        Header h6 = response.getFirstHeader("OptGuid");
        if (response.getEntity() != null && response.getEntity().getContent() != null) {
          HttpEntity et = response.getEntity();
          if (et != null) {
            InputStream is = et.getContent();
            String res = "";
            byte[] b = new byte[1024];
            for (int i = 0; (i = is.read(b)) > 0;) {
              res += new String(b);
            }
            if (!"".equals(res.trim())) {
              message = res.trim();
            }
            response.getEntity().getContent().close();
          }
        }
        
        if (h5 != null) {
          String value = h5.getValue();
          if ("1".equals(value)) {
            //超时
            YHEsbPollerLogic logic = new YHEsbPollerLogic();
            logic.updateStatus(guid, "-3");
            return false;
          }
        }
        if (h0 != null && h1 != null && h2 != null && h3 != null && h4 != null ) {
          flag = true;
        }
        
        
        if (h0 != null) {
          length = Long.parseLong(h0.getValue());
        }
        if (h1 != null) {
          this.md5 = h1.getValue();
        }
        String name = "";
        if (h2 != null) {
          if (!this.dir.exists()) {
            this.dir.mkdirs();
          }
          
          File dir = new File(this.dir.getAbsolutePath() + File.separator + this.guid);
          dir.mkdirs();
          
          String n = h2.getValue();
          n = java.net.URLDecoder.decode(n, "UTF-8");
          
          name = dir.getAbsolutePath() + File.separator + n;
          this.file = new File(name);
        }
        if (h3 != null) {
          this.type = h3.getValue();
        }
        if (h4 != null) {
          this.fromId = h4.getValue();
        }
        if (h6 != null) {
          optGuid = h6.getValue();
        }
        
        if (h2 != null  && h4 != null ) {
          YHEsbPollerLogic logic = new YHEsbPollerLogic();
          if (!logic.hasEsbDownTask(guid)) {
            logic.addEsbDownTask(name, guid, 1, fromId , optGuid , message);
          } else {
            logic.updateStatus(guid, "1");
          }
        }
      }
      initTaskInfo();
      return flag;
    } catch (Exception e) {
     e.printStackTrace();
      count++;
      if (count < 10) {
        return this.initialize(hc);
      } else {
        e.printStackTrace();
        return false;
      }
      //throw e;
    } finally {
      YHHttpClientUtil.releaseConnection(response);
    }
  }
  public void transfer(HttpClient hc) throws Exception {
    long size = PropertiesUtil.getDownloadPartSize();
    Collection<Runnable> tasks = new ArrayList<Runnable>();
    BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(100000);
    ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, workQueue);
    //ThreadPoolExecutor pool = (ThreadPoolExecutor)Executors.newFixedThreadPool(PropertiesUtil.getPerConcurrencyLimits());
    long fileSize = this.length;
    int i = 0;
    long s = 0;
    while (fileSize >  s) {
      if (s + size > fileSize) {
        size = fileSize - s;
      }
      if (taskInfo.isDone(i)) {
        s += size;
        i++;
        continue;
      }
      /*
      if (i == 0 || i==2 ) {
        s += size;
        continue;
      }*/
      
      MultiThreadDownloadRequest multiThreadRequest = new MultiThreadDownloadRequest(pool, hc, host, taskInfo, i++, guid, s, size, file  );
      tasks.add(multiThreadRequest);
      pool.execute(multiThreadRequest);
      s += size;
    }
    
    while (pool.getQueue().size() > 0) {
      Thread.sleep(100);
    }
    pool.shutdown();
    while (!pool.awaitTermination(2, TimeUnit.SECONDS)) {
      //等待所有线程结束    }
  }
  
  public int complete(HttpClient hc) {
    HttpResponse response = null;
    try {
      String result = null;
      if (YHDigestUtility.isFileMatch(this.file.getAbsolutePath(), this.md5)) {
        result = "ok";
        YHEsbUtil.println("YHDwonloadTask: complete - MD5校验成功");
        if ("1".equals(this.type)) {
          String dirPath = YHSysProps.getWebInfPath() + File.separator + "config" + File.separator;
          File dir = new File(dirPath);
          if (!dir.exists()) {
            dir.mkdirs();
          }
          
          File old = new File(dirPath + "esbconfig.properties");
          if (old.delete()) {
            this.file.renameTo(old);
            ClientPropertiesUtil.refresh();
            YHEsbUtil.println("客户端的配置已经更新");
          }
        }
        HttpPost post = new HttpPost(DOWNLOAD_COMPLETE_URL);
        
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("RESULT", result));
        nvps.add(new BasicNameValuePair("GUID", this.guid));
        post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        
        response = hc.execute(host, post);
        
        if (response == null) {
        }else {
          Header h5 = response.getFirstHeader("SYS-FIELD");
          if (h5 != null) {
            String value = h5.getValue();
            if ("1".equals(value)) {
              //超时
              YHEsbPollerLogic logic = new YHEsbPollerLogic();
              logic.updateStatus(guid, "-3");
              saveTaskInfo();
              return -3;
            }
          }
        }
        if ("0".equals(this.type)) {
          YHESBMessageServiceCaller caller = new YHESBMessageServiceCaller();
          caller.recvMessage(this.file.getAbsolutePath(), this.guid, this.fromId);
        }
        
        response.getEntity().getContent().close();
        YHEsbPollerLogic logic = new YHEsbPollerLogic();
        logic.updateStatus(guid, "0");
        return 0;
      } else {
        YHEsbPollerLogic logic = new YHEsbPollerLogic();
        if (!logic.hasEsbDownTaskField(guid)) {
          logic.updateStatus(guid, "-1");
          YHEsbUtil.println("YHDwonloadTask: complete - MD5校验失败");
        } 
      }
      HttpPost post = new HttpPost(DOWNLOAD_COMPLETE_URL);
      
      List<NameValuePair> nvps = new ArrayList<NameValuePair>();
      nvps.add(new BasicNameValuePair("RESULT", result));
      nvps.add(new BasicNameValuePair("GUID", this.guid));
      post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
      response = hc.execute(host, post);
      if (response == null) {
      } else {
        Header h5 = response.getFirstHeader("SYS-FIELD");
        if (h5 != null) {
          String value = h5.getValue();
          if ("1".equals(value)) {
            //超时
            YHEsbPollerLogic logic = new YHEsbPollerLogic();
            logic.updateStatus(guid, "-3");
          }
        }
      }
      response.getEntity().getContent().close();
      saveTaskInfo();
      return -1;
    } catch (Exception e) {
      e.printStackTrace();
      YHHttpClientUtil.releaseConnection(response);
      if (completeResumeCnt++ < 10) {
        return complete(hc);
      }
      else {
        try {
          saveTaskInfo();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
        YHEsbPollerLogic logic = new YHEsbPollerLogic();
        logic.updateStatus( guid, "-1");
        return -3;
      }
    }
    finally {
      YHHttpClientUtil.releaseConnection(response);
    }
  }
}

class MultiThreadDownloadRequest implements Runnable {
  public static final int E_NO_RESPONSE = -1;
  public static final int E_VALIDATION = -2;
  public static final int E_SERVER_TIMEOUT = -3;
  public static final int E_UNKNOW = -100;
  public static final int E_DOWNLOAD_TIMEOUT = 400;
  
  
  private final HttpClient hc;
  private final HttpHost host;
  private final int no;
  private final String guid;
  private final File file;
  private long start;
  private long limit;
  private int resumeCnt;
  private final ExecutorService es;
  private final  YHEsbTaskInfo taskInfo;
  
  public MultiThreadDownloadRequest(ExecutorService es, HttpClient hc, HttpHost host, YHEsbTaskInfo taskInfo, int no, String guid, long start, long limit, File file ) {
    this.es = es;
    this.hc = hc;
    this.host = host;
    this.no = no;
    this.start = start;
    this.limit = limit;
    this.guid = guid;
    this.file = file;
    this.taskInfo = taskInfo;
  }
  
  public void run() {
    HttpResponse response = null;
    try {
      response = this.request();
    } catch (Exception e) {
      YHEsbUtil.println("MultiThreadRequest: 线程" + no + " - 请求出现异常,取消请求");
      e.printStackTrace();
      this.resume();
      //return;
    } finally {
      
    }
    
    try {
      int code = this.parseResponse(response);
      
      if (code == 0) {
        taskInfo.done(no);
        //YHEsbUtil.println("分片" + no + "传输成功");
      }
      else if (code == E_NO_RESPONSE) {
        this.resume();
      }
      else if (code == E_SERVER_TIMEOUT) {
        this.stopAll();
      }
      else if (code == E_VALIDATION) {
        this.resume();
      }
      else if (code == E_UNKNOW) {
        this.resume();
      } else if (code == E_DOWNLOAD_TIMEOUT) {
        this.stopAll();
      }
    } catch (Exception e) {
      e.printStackTrace();
      this.resume();
    } finally {
      YHHttpClientUtil.releaseConnection(response);
    }
  }
  
  /**
   * 处理返回数据
   * @param response
   * @return
   * @throws IllegalStateException
   * @throws IOException
   */
  public int parseResponse(HttpResponse response) throws IllegalStateException, IOException {
    if (response == null) {
      return E_NO_RESPONSE;
    }
    else {
      Header h5 = response.getFirstHeader("SYS-FIELD");
      if (h5 != null) {
        String value = h5.getValue();
        if ("1".equals(value)) {
          //超时
          YHEsbPollerLogic logic = new YHEsbPollerLogic();
          logic.updateStatus(guid, "-3");
          response.getEntity().getContent().close();
          return E_DOWNLOAD_TIMEOUT;
        }
      }
      Header mdHeader = response.getFirstHeader("Content-MD5");
      if (mdHeader == null || YHUtility.isNullorEmpty(mdHeader.getValue())) {
        return E_SERVER_TIMEOUT;
      }
      
      InputStream is = response.getEntity().getContent();
      String md5 = mdHeader.getValue();
      ByteBuffer bb = ByteBuffer.allocate(Integer.parseInt(response.getFirstHeader("Data-Length").getValue()));
      byte[] tmp = new byte[1024];
      int i = 0;
      try {
        for (i = 0; (i = is.read(tmp)) > 0;) {
          bb.put(tmp, 0, i);
          //System.out.println("第 " + i + "次");
        }
      }catch(Exception ex) {
        ex.printStackTrace();
      }
      byte[] b = bb.array();
      bb.clear();
      RandomAccessFile rf = new RandomAccessFile(file, "rw");
      rf.seek(start);
      if (YHDigestUtility.isMatch(b, md5)) {
        rf.write(b);
        rf.close();
        return 0;
      }
      else {
        rf.close();
        return E_VALIDATION;
      }
    }
  }
  
  /**
   * 请求数据
   * @return
   * @throws ClientProtocolException
   * @throws IOException
   */
  public HttpResponse request() throws ClientProtocolException, IOException {
    HttpPost request = new HttpPost(YHDownloadTask.DOWNLOAD_TRANSFER_URL);
    //System.out.println("RANGE" + start + "-" + (start + limit));
    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
    nvps.add(new BasicNameValuePair("RANGE", start + "-" + (start + limit)));
    nvps.add(new BasicNameValuePair("GUID", guid));
    request.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
    return hc.execute(host, request);
  }
  
  /**
   * 重传,超过重传限制则终止整个下载任务   */
  public void resume() {
    YHEsbUtil.println("MultiThreadRequest: 线程" + no + "异常,进行重传");
    if (resumeCnt++ < 10) {
      try {
        es.execute(this);
      } catch (Exception e) {
        YHEsbPollerLogic logic = new YHEsbPollerLogic();
        logic.updateStatus( guid, "-1");
        YHEsbUtil.println("MultiThreadRequest: 线程" + no + "加入线程池失败");
      }
    }
    else {
      YHEsbPollerLogic logic = new YHEsbPollerLogic();
      logic.updateStatus( guid, "-1");
      YHEsbUtil.println("MultiThreadRequest: 线程" + no + "失败" + resumeCnt + "次,下载任务失败");
      this.stopAll();
    }
  }
  
  /**
   * 终止整个下载任务
   */
  public void stopAll() {
    es.shutdownNow();
  }
  
}
