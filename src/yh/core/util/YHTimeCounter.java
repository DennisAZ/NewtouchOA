package yh.core.util;

import org.apache.log4j.Logger;

public class YHTimeCounter {
  /**
   * 启始时间
   */
  private long startTime = System.currentTimeMillis();
  /**
   * 上一次输出的时间
   */
  private long lastOutTime = startTime;
  
  /**
   * 输出时间
   * @param log
   * @param prefix
   */
  public void logTime(Logger log, String prefix) {
    long endTime = System.currentTimeMillis();    
    long lastTime2LastOut = endTime - lastOutTime;
    long lastTime2Start = endTime - startTime;
    lastOutTime = endTime;
    
    if (log.isDebugEnabled()) {
      log.debug(prefix + ">>" + lastTime2LastOut + "/" + lastTime2Start);
    }
  }
  
  /**
   * 输出时间
   * @param log
   * @param prefix
   */
  public void logTime(String prefix) {
    long endTime = System.currentTimeMillis();    
    long lastTime2LastOut = endTime - lastOutTime;
    long lastTime2Start = endTime - startTime;
    lastOutTime = endTime;

    System.out.println(prefix + ">>" + lastTime2LastOut + "/" + lastTime2Start);
  }
}
