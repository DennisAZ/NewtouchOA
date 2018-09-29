package yh.core.esb.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.entity.NByteArrayEntity;
import org.apache.http.protocol.HTTP;

import yh.core.esb.common.util.PropertiesUtil;
import yh.core.esb.common.util.YHEsbUtil;
import yh.core.esb.common.util.YHHttpClientUtil;
import yh.core.esb.frontend.YHEsbFrontend;
import yh.core.esb.frontend.data.YHEsbUploadTask;
import yh.core.esb.frontend.logic.YHEsbFrontendLogic;
import yh.core.esb.frontend.oa.YHESBMessageServiceCaller;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHDigestUtility;
import yh.core.util.db.YHDBUtility;
import yh.user.api.core.db.YHDbconnWrap;

public class YHUploadTask {
  public static Integer maxSeqId = 0;
  public int taskSeqId = 0;
  public int count = 0 ;
  public static String LOGIN_URL = "/yh/yh/core/esb/server/system/act/SystemLoginAct/doLogin.act";
  public static final String UPLOAD_URL = "/yh/yh/core/esb/server/act/YHRangeUploadAct/transfer.act";
  public static final String UPLOAD_INITIALIZE_URL = "/yh/yh/core/esb/server/act/YHRangeUploadAct/initialize.act";
  public static final String UPLOAD_COMPLETE_URL = "/yh/yh/core/esb/server/act/YHRangeUploadAct/complete.act";
  private File file;
  private HttpHost host;
  private String toId;
  private String guid;
  private int completeResumeCnt;
  public String hasDone = "" ;
  public String isField = "";
  public String message = "";
  public String optGuid = "";
  
  public YHUploadTask(HttpHost host, File file, String toId, String guid , String optGuid , String message   ) {
    this.host = host;
    this.toId = toId;
    this.file = file;
    this.guid = guid;
    this.message = message ;
    this.optGuid = optGuid;
    
    synchronized(maxSeqId) {
      taskSeqId = ++maxSeqId;
    }
  }
  public HttpResponse initializeQuest(DefaultHttpClient hc) throws Exception {
    HttpPost post = new HttpPost(UPLOAD_INITIALIZE_URL);
    
    String md5 = YHDigestUtility.md5File(file.getAbsolutePath());
    
    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
    long contentLength = file.length();
    nvps.add(new BasicNameValuePair("Content-length", String.valueOf(contentLength)));
    nvps.add(new BasicNameValuePair("MD5", md5));
    nvps.add(new BasicNameValuePair("Content-name", file.getName()));
    nvps.add(new BasicNameValuePair("TO_ID", toId));
    nvps.add(new BasicNameValuePair("GUID", guid));
    nvps.add(new BasicNameValuePair("FileSeqId", String.valueOf(taskSeqId)));
    nvps.add(new BasicNameValuePair("optGuid", optGuid));
    nvps.add(new BasicNameValuePair("message", message));
    
    
    post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
    HttpResponse response = null;
    try {
     response = hc.execute(host, post);
     return response;
    } catch(Exception ex) {
      count++;
      ex.printStackTrace();
      if (count < 10) {
        return this.initializeQuest(hc);
      } else {
        ex.printStackTrace();
        return null;
      }
    }
  }
  /**
   * 初史化，并把md5值发送过去
   * @param hc
   * @param conn 
   * @param pwd 
   * @param userName 
   * @return
   * @throws Exception
   */
  public boolean initialize(DefaultHttpClient hc) throws Exception {
    if (YHUtility.isNullorEmpty(guid)) {
      this.guid = UUID.randomUUID().toString();
    }
    YHEsbFrontendLogic logic = new YHEsbFrontendLogic();
   // if (!logic.hasEsbUploadTask(conn , guid)) {
     // logic.addEsbUploadTask(conn, this.file.getAbsolutePath(), guid, 1, toId);
    //} else {
      logic.updateStatus( guid, "1");
   // }
    
    long begin = System.currentTimeMillis();
    HttpResponse response = this.initializeQuest(hc);
    boolean flag = false;
    if (response == null) {
      //异常处理
      flag = false;
    } else {
      //正常,返回guid
      Header[] headers = response.getAllHeaders();
      
      for (Header h : headers) {
        if ("Content-GUID".equals(h.getName()) ) {
          this.guid = h.getValue();
          long end = System.currentTimeMillis();
          int lastTime = (int)(end - begin);
          flag = true;
        }
        if ("SYS-FIELD".equals(h.getName()) ) {
          this.isField = h.getValue();
          if ("1".equals(isField)) {
            YHESBMessageServiceCaller caller = new YHESBMessageServiceCaller();
            caller.updateState(guid, -2, "");
            logic.updateStatus(guid, "-3");
            return false;
          }
          flag = true;
        }
      }
      HttpEntity et = response.getEntity();
      if (et != null) {
        InputStream is = et.getContent();
        String res = "";
        byte[] b = new byte[1024];
        for (int i = 0; (i = is.read(b)) > 0;) {
          res += new String(b);
        }
        if (!"".equals(res.trim())) {
          this.hasDone = res.trim();
        }
      }
      long end = System.currentTimeMillis();
      int lastTime = (int)(end - begin);
    }
    if (!flag) {
      //传输失败
        logic.updateStatus(guid, "-1");
    }
    return flag;
  }
  /**
   * 传文件
   * @param hc
   * @throws Exception
   */
  public void transfer(HttpClient hc) throws Exception {
    long size = PropertiesUtil.getUploadPartSize();
    long length = this.file.length();
    Collection<Runnable> tasks = new ArrayList<Runnable>();
    
    
//    ThreadPoolExecutor pool = (ThreadPoolExecutor)Executors.newFixedThreadPool(PropertiesUtil.getPerConcurrencyLimits());
    BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(100000);
    ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, workQueue);
    pool.setRejectedExecutionHandler(new RejectedExecutionHandler() {
      public void rejectedExecution(Runnable r, ThreadPoolExecutor pool) {
        YHEsbFrontendLogic logic = new YHEsbFrontendLogic();
        logic.updateStatus(guid, "-1");
      }
    });
    
    int partCnt = (length % size == 0) ? (int)(length / size) :  (int)(length / size + 1);
    int partIndex = 0;
    Set<Integer> set = new HashSet();
    String[] done = this.hasDone.split(",");
    for (String s : done) {
      if (!YHUtility.isNullorEmpty(s))
        set.add(Integer.parseInt(s));
    }
    for (int start = 0, i = 0; start < length;  i++) {
      if (start + size > length) {
        size = length - start;
      }
      if (set.contains(i)) {
        start += size;
        continue;
      }
   /*
      if (i == 0 || i==2 ) {
        start += size;
        continue;
      }
      */
      partIndex++;
      MultiThreadUploadRequest multiThreadRequest = new MultiThreadUploadRequest(pool, hc, host, i, guid, start, size, file, taskSeqId, partCnt, partIndex );
      tasks.add(multiThreadRequest);
      pool.execute(multiThreadRequest);
      start += size;
    }
    int loopCnt = 0;
    while (pool.getQueue().size() > 0) {
      Thread.sleep(1000);
      int connCnt = ((ThreadSafeClientConnManager)hc.getConnectionManager()).getConnectionsInPool();
    }
    pool.shutdown();
    int loopCnt2 = 0;
    while (!pool.awaitTermination(2, TimeUnit.SECONDS)) {
      //等待所有线程结束    }
  }
  /**
   * 校验
   * @param hc
   * @return
   * @throws Exception
   */
  public int complete(HttpClient hc) throws Exception {
    long begin = System.currentTimeMillis();
    try {
      HttpPost post = new HttpPost(UPLOAD_COMPLETE_URL);
      List<NameValuePair> nvps = new ArrayList<NameValuePair>();
      nvps.add(new BasicNameValuePair("GUID", guid));
      nvps.add(new BasicNameValuePair("FileSeqId", String.valueOf(taskSeqId)));
      post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
      
      HttpResponse response = hc.execute(host, post);
      
      int returnValue = -3;
      if (response == null) {
        returnValue = -3;
      }
      else {
        int code = response.getStatusLine().getStatusCode();
        response.getEntity().getContent().close();
        
        if (code == 200) {
          YHEsbUtil.println("文件 " + taskSeqId + " 发送成功!");
          YHESBMessageServiceCaller caller = new YHESBMessageServiceCaller();
          YHEsbUtil.println(caller.updateState(guid, 1, ""));
          returnValue = 0;
        } else if (code == 300) {
          YHEsbUtil.println("MD校验失败,文件 " + taskSeqId + " 发送失败!");
          YHESBMessageServiceCaller caller = new YHESBMessageServiceCaller();
          //YHEsbUtil.println(caller.updateState(guid, -1, ""));
          //caller.updateState(guid, -1, "");
          returnValue = -1;
        } else if (code == 400) {
          YHEsbUtil.println("文件 " + taskSeqId + " 发送失败!");
          YHESBMessageServiceCaller caller = new YHESBMessageServiceCaller();
          caller.updateState(guid, -2, "");
          returnValue = -4;
        }else  {
          YHEsbUtil.println("文件 " + taskSeqId + " 发送失败!");
          YHESBMessageServiceCaller caller = new YHESBMessageServiceCaller();
          //YHEsbUtil.println(caller.updateState(guid, -2, ""));
          returnValue = -2;
        }
      }
      if (returnValue != 0) {
        YHEsbFrontendLogic logic = new YHEsbFrontendLogic();
        logic.updateStatus(guid, "-1");
      } else if (returnValue == -4) {
        YHEsbFrontendLogic logic = new YHEsbFrontendLogic();
        logic.updateStatus(guid, "-3");
      } else  {
        YHEsbFrontendLogic logic = new YHEsbFrontendLogic();
        logic.updateStatus(guid, "0");
      }
      return returnValue;
    } catch (ConnectionPoolTimeoutException e) {
      //连接超时
      if (completeResumeCnt++ < 10) {
        return complete(hc);
      } else {
        YHEsbFrontendLogic logic = new YHEsbFrontendLogic();
        logic.updateStatus(guid, "-1");
        return -3;
      }
    }finally {
      long end = System.currentTimeMillis();
      int lastTime = (int)(end - begin);
      //System.out.println("file " + taskSeqId + " terminate use " + lastTime + " ms");
    }
  }
}

class MultiThreadUploadRequest implements Runnable {
  private final int taskSeqId;
  private final int totalParts;
  private final int seqId;
  private final HttpClient hc;
  private final HttpHost host;
  private final int no;
  private final String guid;
  private final File file;
  private long start;
  private long limit;
  private int resumeCnt;
  private final ExecutorService es;
  public static final int E_NO_RESPONSE = -1;
  public static final int E_VALIDATION = -2;
  public static final int E_SERVER_TIMEOUT = -3;
  public static final int E_UNKNOW = -100;
  
  public String getName() {
    return "[task " + taskSeqId + " the part " + seqId + "/" + totalParts + "]";
  }
  public MultiThreadUploadRequest(ExecutorService es, HttpClient hc, HttpHost host, int no, String guid, long start, long limit, File file, int taskSeqId, int totalParts, int seqId ) {
    this.hc = hc;
    this.host = host;
    this.no = no;
    this.guid = guid;
    this.file = file;
    this.start = start;
    this.limit = limit;
    this.es = es;
    this.taskSeqId = taskSeqId;
    this.totalParts = totalParts;
    this.seqId = seqId;
  }
  
  public void run() {
    long begin = System.currentTimeMillis();
    HttpResponse response = null;
    try {
      response = this.request();
    } catch (Exception e) {
      YHEsbUtil.println("MultiThreadRequest: 线程" + getName() + " - 请求出现异常,取消请求");
      e.printStackTrace();
      this.resume();
      return;
    } finally {
      
    }
    
    try {
      int code = this.parseResponse(response);
      //YHEsbUtil.println("MultiThreadRequest: Thread " + no + " - code: " + code);
      
      if (code == 0) {
        if (resumeCnt > 0) {
          YHEsbUtil.println(getName() + "重传成功");
        }
      }
      else if (code == E_NO_RESPONSE) {
        this.resume();
      }
      else if (code == E_SERVER_TIMEOUT) {
        YHEsbFrontendLogic logic = new YHEsbFrontendLogic();
        logic.updateStatus(guid, "-3");
        this.stopAll();
      }
      else if (code == E_VALIDATION) {
        this.resume();
      }
      else if (code == E_UNKNOW) {
        this.resume();
      }
      long end = System.currentTimeMillis();
      long lastTime = end - begin;
//      System.out.println("trans " + getName() + " use time >>" + lastTime + " ms >> total time >> " + (totalParts * lastTime / 1000) + "S");
    } catch (Exception e) {
      this.resume();
    } finally {
      YHHttpClientUtil.releaseConnection(response);
//      hc.getConnectionManager().closeExpiredConnections();
    }
  }
  
  public int parseResponse(HttpResponse response) {
    int code = response.getStatusLine().getStatusCode();
    if (code == 300) {
      return E_VALIDATION;
    }
    else if (code == 301) {
      return E_SERVER_TIMEOUT;
    }
    else {
      return 0;
    }
  }
  
  public HttpResponse request() throws IOException {
    HttpResponse response = null;
    HttpPost request = new HttpPost(YHUploadTask.UPLOAD_URL);
    if (start >= 0 && limit > 0 && start < file.length()) {
      request.addHeader("GUID", this.guid);
      if (start + limit > file.length()) {
        limit = file.length() - start;
      }
      RandomAccessFile raf = new RandomAccessFile(file, "r");
      raf.seek(start);
      byte[] bytes = new byte[(int) limit];
      raf.readFully(bytes);
      String md5 = YHDigestUtility.md5Hex(bytes);
      request.addHeader("MD5", md5);
      request.addHeader("START", String.valueOf(start));
      request.addHeader("Con-length", String.valueOf(limit));
      request.addHeader("FileInfo", getName());
      request.addHeader("NO", String.valueOf(this.no));
      raf.close();
      NByteArrayEntity entity = new NByteArrayEntity(bytes);
      request.setEntity(entity);
      boolean flag = false;
      for (int i = 0; i < 10; i++) {
        try {
          response = hc.execute(host, request);
          flag = true;
          break;
        }catch(Exception ex) {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
          }
          try {
            hc.getConnectionManager().closeIdleConnections(20, TimeUnit.MILLISECONDS);
          }catch(Exception ex2) {  
          }
          if (i <= 9) {
            YHEsbUtil.println("重新试传" + getName() + " 第" + (i + 1)  + " 次");
          }
        }
      }
      if (!flag) {
        YHEsbFrontendLogic logic = new YHEsbFrontendLogic();
        logic.updateStatus( guid, "-1");
      }
    }
    return response;
  }
  
  /**
   * 重传,超过重传限制则终止整个下载任务
   */
  public void resume() {
    YHEsbUtil.println("MultiThreadRequest: 线程" + getName() + "异常,进行重传");
    if (resumeCnt++ < 10) { 
      es.execute(this);
    }
    else {
      YHEsbFrontendLogic logic = new YHEsbFrontendLogic();
      logic.updateStatus( guid, "-1");
      YHEsbUtil.println("MultiThreadRequest: 线程" + getName() + "失败" + resumeCnt + "次,下载任务失败");
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
