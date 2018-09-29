package yh.core.autorun;

import java.util.Calendar;
import org.apache.log4j.Logger;

import yh.core.data.YHRequestDbConn;
import yh.core.global.YHConst;
import yh.core.global.YHSysPropKeys;
import yh.core.global.YHSysProps;
import yh.core.util.YHUtility;
import yh.core.util.form.YHFOM;

public abstract class YHAutoRun implements Runnable {
  /**
   * log
   */
  private static final Logger log = Logger.getLogger("yzq.yh.core.autorun.YHAutoRun");
  //最后一次执行检查的时间
  protected long lastRuntime = System.currentTimeMillis();
  //运行间隔时间，运行周期驱动情况下单位是秒，时间点驱动下，单位是天
  protected int intervalSeconds = 300;
  //时间点驱动的时间，格式HH:mm
  protected String runTime = null;
  //时间点驱动-小时, 取值范围：[0,23]
  protected int runTimeHour = 0;
  //时间点驱动-分钟, 取值范围：[0,59]
  protected int runTimeMinit = 0;
  //时间点驱动的下一个时间点
  protected long runTimePoint = 0;
  //数据库连接资源
  protected YHRequestDbConn requestDbConn = null;
  //是否被暂停
  protected boolean isPause = false;
  //当前线程
  protected Thread currThread = null;

  /**
   * 执行任务
   */
  public abstract void doTask() throws Exception;
  
  
  /**
   * 构造YHAutoRun 对象
   * @param jsonStr
   * @return
   * @throws Exception
   */
  public static YHAutoRun buildAutoRun(String jsonStr) throws Exception {
    YHAutoRunConfig config = (YHAutoRunConfig)YHFOM.json2Obj(jsonStr, YHAutoRunConfig.class);
    YHAutoRun autoRun = (YHAutoRun)Class.forName(config.getCls()).newInstance();
    autoRun.setIntervalSeconds(config.getIntervalSecond());
    autoRun.setRunTime(config.getRunTime());
    autoRun.setPause(config.getIsUsed().equals("0"));
    return autoRun;
  }
  /**
   * 开始执行线程   */
  public synchronized void startRun() {
    if (!shouldRun()) {
      return;
    }
    this.currThread = new Thread(this);
    this.currThread.start();
  }
  /**
   * 手工执行线程
   */
  public synchronized void menuStartRun() {
    if (this.currThread != null) {
      return;
    }
    this.currThread = new Thread(this);
    this.currThread.start();
  }
  /**
   * 终止线程执行
   */
  public synchronized void stopRun() {
    if (currThread == null) {
      return;
    }
    try {
      if (!currThread.isInterrupted()) {
        this.currThread.interrupt();
      } 
    }catch(Exception ex) {      
    }finally {
      this.currThread = null;
    }
  }
  /**
   * 执行任务
   */
  public void run() {
    this.setRequestDbConn(new YHRequestDbConn(""));
    try {
      doTask();
      this.requestDbConn.commitAllDbConns();
    }catch(Exception ex) {
      this.requestDbConn.rollbackAllDbConns();
      log.debug(ex);
    }finally {
      setLastRuntime(System.currentTimeMillis());
      if (!YHUtility.isNullorEmpty(this.runTime)) {
        this.runTimePoint = this.findNextRuntime();
      }
      if (this.requestDbConn != null) {
        this.requestDbConn.closeAllDbConns();
      }
      this.currThread = null;
      this.requestDbConn = null;
    }
  }
  /**
   * 设置间隔时间
   */
  public void setIntervalSeconds(int intervalSeconds) {
    this.intervalSeconds = intervalSeconds;
  }
  /**
   * 设置最后一次运行的时间
   */
  protected void setLastRuntime(long lastRuntime) {
    this.lastRuntime = lastRuntime;
  }
  /**
   * 是否正在运行
   * @return
   */
  public boolean isRunning() {
    return this.currThread != null;
  }
  /**
   * 是否应该运行
   */
  protected synchronized boolean shouldRun() {
    if (this.isPause()) {
      return false;
    }
    if (this.currThread != null) {
      return false;
    }
    //时间间隔驱动的任务
    if (YHUtility.isNullorEmpty(runTime)) {
      if (System.currentTimeMillis() - lastRuntime > intervalSeconds * YHConst.DT_S) {
        return true;
      }else {
        return false;
      }
    //时间点驱动的任务
    }else {
      if (System.currentTimeMillis() > this.runTimePoint) {
        return true;
      }else {
        return false;
      }
    }
  }
  
  protected long findNextRuntime() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(lastRuntime);
    
    calendar.set(Calendar.HOUR_OF_DAY, this.runTimeHour);
    calendar.set(Calendar.MINUTE, this.runTimeMinit);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    //这里用天(YHConst.DT_D)是正确的，往前推移一小时以弥补线程执行周期造成的时间差
    long nextTimeCompare = lastRuntime
      + this.intervalSeconds * YHConst.DT_D
      - YHConst.DT_H;
    for (int i = 0; ;i++) {
      calendar.add(Calendar.DATE, 1);
      long nextTime = calendar.getTime().getTime();
      if (nextTime > nextTimeCompare) {
        return nextTime;
      }
    }
  }

  protected YHRequestDbConn getRequestDbConn() {
    return requestDbConn;
  }

  private void setRequestDbConn(YHRequestDbConn requestDbConn) {
    this.requestDbConn = requestDbConn;
  }

  public String getRunTime() {
    return runTime;
  }

  public void setRunTime(String runTime) {
    this.runTime = runTime;
    if (YHUtility.isNullorEmpty(this.runTime)) {
      return;
    }
    this.runTime = this.runTime.trim();
    int tmpInt = this.runTime.indexOf(":");
    if (tmpInt <= 0) {
      this.runTime = null;
      return;
    }
    try {
      this.runTimeHour = Integer.parseInt(this.runTime.substring(0, tmpInt));
      this.runTimeMinit = Integer.parseInt(this.runTime.substring(tmpInt + 1));
      
      this.runTimePoint = this.findNextRuntime();
    }catch(Exception ex) {
      this.runTime = null;
    }
  }

  public boolean isPause() {
    return isPause;
  }

  public void setPause(boolean isPause) {
    this.isPause = isPause;
  }
}
