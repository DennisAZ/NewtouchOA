package yh.core.autorun;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import yh.core.global.YHConst;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.auth.YHAuthAutoService;
import yh.core.util.file.YHFileUtility;

public class YHAutoRunThread extends Thread {
  //log
  private static Logger log = Logger.getLogger(YHAutoRunThread.class);
  
  //相邻两次运行的时间间隔 单位是秒
  private int sleepTime = 1;
  //后台服务对象链
  private Map<String, YHAutoRun> autoRuns = new LinkedHashMap<String, YHAutoRun>();
  //运行标志
  private boolean runFlag = true;
  //运行实例
  private static YHAutoRunThread mainService = null;
  
  /**
   * 加载后台线程配置
   */
  private void loadAutoRunConfig() {
    String confFile = YHSysProps.getWebInfPath() + File.separator + "config" + File.separator + "autoruntasksconfig.properties";
    Map<String, String> rawConfMap = new HashMap<String, String>();
    try {
      try {
        //增加系统内置的后台服务
        autoRuns.put("yh.sys.services.auth.YHAuthService", new YHAuthAutoService());
      }catch(Exception ex) {        
      }
      YHFileUtility.load2Map(confFile, rawConfMap);
      Map<String, String> confMap = YHUtility.startsWithMap(rawConfMap, "autoRunTask");
      Iterator<String> iKeys = confMap.keySet().iterator();
      while (iKeys.hasNext()) {
        String key = iKeys.next();
        String confJson = confMap.get(key);
        if (YHUtility.isNullorEmpty(confJson)) {
          continue;
        }
        try {
          YHAutoRun autoRun = YHAutoRun.buildAutoRun(confJson);
          if (!autoRun.isPause()) {
            autoRuns.put(key, autoRun);
          }
        }catch(Exception ex) {
          log.debug(ex.getMessage(), ex);
        }
      }
    }catch(Exception ex) {
      log.debug(ex.getMessage(), ex);
    }
  }

  //防止用户使用默认的构造函数  private YHAutoRunThread() {
  }

  /**
   * 构造函数 相邻两次运行的时间间隔 单位是分钟   * @param sleepTime
   */
  private YHAutoRunThread(int sleepTime) {
    this.sleepTime = sleepTime;
    loadAutoRunConfig();
  }
  
  /**
   * 取得当前运行实例
   * @return
   */
  public static YHAutoRunThread currInstance() {
    return mainService;
  }

  /**
   * 注册释放资源的对象   * @param releasor
   */
  public synchronized void registAutoRun(String key, YHAutoRun autoRun) {
    autoRuns.put(key, autoRun);
  }
  /**
   * 注册服务
   * @param releasor
   */
  public synchronized void registAutoRun(String key, String configJson) {
    try {
      this.registAutoRun(key, YHAutoRun.buildAutoRun(configJson));
    }catch(Exception ex) {      
    }
  }
  /**
   * 删除服务
   * @param key
   */
  public synchronized void removeAutoRun(String key) {
    YHAutoRun autoRun = this.autoRuns.get(key);
    if (autoRun != null) {
      autoRun.stopRun();
      autoRuns.remove(key);
    }
  }
  /**
   * 清除服务
   * @param key
   */
  public synchronized void clearAutoRun() {
    Iterator iKeys = autoRuns.keySet().iterator();
    while (iKeys.hasNext()) {
      YHAutoRun autoRun = (YHAutoRun)autoRuns.get(iKeys.next());
      autoRun.stopRun();
    }
    autoRuns.clear();
  }
  /**
   * 手工启动服务
   * @param key
   * @return 1=没有找到该服务；2=该服务正在运行；0=正常启动
   */
  public synchronized int manuStartAutoRun(String key) {
    YHAutoRun autoRun = this.autoRuns.get(key);
    if (autoRun == null) {
      return 1;
    }
    if (autoRun.isRunning()) {
      return 2;
    }
    try {
      autoRun.menuStartRun();
      
    }catch(Exception ex) {
      log.debug(ex.getMessage(), ex);
    }finally {
    }
    return 0;
  }

  /**
   * 释放资源
   */
  private void doRun() {
    try {
      Iterator iKeys = autoRuns.keySet().iterator();
      while (iKeys.hasNext()) {
        YHAutoRun autoRun = (YHAutoRun)autoRuns.get(iKeys.next());
        try {
          autoRun.startRun();
        }catch (Throwable ex) {
          try {
            log.debug(ex.getMessage(), ex);
          }catch(Throwable t) {          
          }
        }
      }
    }catch(Exception ex) {
      log.debug(ex.getMessage(), ex);
    }finally {
    }
  }

  /**
   * 启动该线程   */
  public static void startAutoRun(int sleepTime) {
    stopRun();
    mainService = new YHAutoRunThread(sleepTime);
    mainService.setRunFlag(true);
    mainService.start();
  }
  
  /**
   * 终止线程
   */
  public static void stopRun() {
    if (mainService == null) {
      return;
    }
    try {
      mainService.setRunFlag(false);
      mainService.interrupt();
      mainService.clearAutoRun();
    }catch(Exception ex) {      
    }finally {
      mainService = null;
    }
  }

  /**
   * 重载父类方法run
   */
  public void run() {
    log.info("后台线程开始运行...");
    while (runFlag) {
      try {        
        sleep(sleepTime * YHConst.DT_S);
        doRun();
      }catch (Throwable ex) {
        try {
          log.debug(ex.getMessage(), ex);
        }catch(Throwable t) {          
        }
      }
    }
    log.info("后台线程停止运行....");
  }

  /**
   * 设置runFlag
   * @param runFlag
   */
  public void setRunFlag(boolean runFlag) {
    this.runFlag = runFlag;
  }
}
