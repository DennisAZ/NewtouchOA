package yh.core.autorun;

/**
 * 自动运行配置
 * @author jpt
 *
 */
public class YHAutoRunConfig {
  private String name = null;
  private String cls = null;
  private int intervalSecond = 300;
  private String runTime = null;
  private String isUsed = "1";

  public String getCls() {
    return cls;
  }
  public void setCls(String cls) {
    this.cls = cls;
  }
  public int getIntervalSecond() {
    return intervalSecond;
  }
  public void setIntervalSecond(int intervalSecond) {
    this.intervalSecond = intervalSecond;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getRunTime() {
    return runTime;
  }
  public void setRunTime(String runTime) {
    this.runTime = runTime;
  }
  public String getIsUsed() {
    return isUsed;
  }
  public void setIsUsed(String isUsed) {
    this.isUsed = isUsed;
  }
}
