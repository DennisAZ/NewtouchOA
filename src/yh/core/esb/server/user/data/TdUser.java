package yh.core.esb.server.user.data;

public class TdUser {

  private int seqId; // 用户ID，自增字段
  private String userCode; // 用户账号
  private String userName; // 用户名称
  private String password;  // 用户密码
  private String description; //  用户描述
  private int appId;  // 所属具体应用的ID
  private int userType; // 用户类型  0-  下级单位1-  总部
  private int status;  // 用户状态：  0-  启用  1-  未启用
  private int isOnline; // 是否在线  0-  否1- 是
  private String onlineIp; // 在线地址
  
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getUserCode() {
    return userCode;
  }
  public void setUserCode(String userCode) {
    this.userCode = userCode;
  }
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public int getAppId() {
    return appId;
  }
  public void setAppId(int appId) {
    this.appId = appId;
  }
  public int getUserType() {
    return userType;
  }
  public void setUserType(int userType) {
    this.userType = userType;
  }
  public int getStatus() {
    return status;
  }
  public void setStatus(int status) {
    this.status = status;
  }
  public int getIsOnline() {
    return isOnline;
  }
  public void setIsOnline(int isOnline) {
    this.isOnline = isOnline;
  }
  public String getOnlineIp() {
    return onlineIp;
  }
  public void setOnlineIp(String onlineIp) {
    this.onlineIp = onlineIp;
  }

  
}
