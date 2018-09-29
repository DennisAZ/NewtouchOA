package yh.pda.workflow.data;

public class YHPdaFlowProcess {

  private int prcsId;
  private String prcsName;
  private String prcsIn;
  private String prcsInSet;
  private String conditionDesc;
  private String userLock;
  private String topDefault;
  private int childFlow;
  private int autoBaseUser;
  public String getPrcsName() {
    return prcsName;
  }
  public void setPrcsName(String prcsName) {
    this.prcsName = prcsName;
  }
  public String getPrcsIn() {
    return prcsIn;
  }
  public void setPrcsIn(String prcsIn) {
    this.prcsIn = prcsIn;
  }
  public String getPrcsInSet() {
    return prcsInSet;
  }
  public void setPrcsInSet(String prcsInSet) {
    this.prcsInSet = prcsInSet;
  }
  public String getConditionDesc() {
    return conditionDesc;
  }
  public void setConditionDesc(String conditionDesc) {
    this.conditionDesc = conditionDesc;
  }
  public String getUserLock() {
    return userLock;
  }
  public void setUserLock(String userLock) {
    this.userLock = userLock;
  }
  public String getTopDefault() {
    return topDefault;
  }
  public void setTopDefault(String topDefault) {
    this.topDefault = topDefault;
  }
  public int getChildFlow() {
    return childFlow;
  }
  public void setChildFlow(int childFlow) {
    this.childFlow = childFlow;
  }
  public int getAutoBaseUser() {
    return autoBaseUser;
  }
  public void setAutoBaseUser(int autoBaseUser) {
    this.autoBaseUser = autoBaseUser;
  }
  public int getPrcsId() {
    return prcsId;
  }
  public void setPrcsId(int prcsId) {
    this.prcsId = prcsId;
  }
  
}
