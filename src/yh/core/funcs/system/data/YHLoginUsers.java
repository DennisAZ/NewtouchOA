package yh.core.funcs.system.data;

import java.util.Date;

public class YHLoginUsers {
  private int seqId;
  private String sessionToken;
  private String userId;
  private String roleId;
  private String extData;
  private Date loginTime;
  
  public Date getLoginTime() {
    return loginTime;
  }
  public void setLoginTime(Date loginTime) {
    this.loginTime = loginTime;
  }
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getSessionToken() {
    return sessionToken;
  }
  public void setSessionToken(String sessionToken) {
    this.sessionToken = sessionToken;
  }
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public String getRoleId() {
    return roleId;
  }
  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }
  public String getExtData() {
    return extData;
  }
  public void setExtData(String extData) {
    this.extData = extData;
  }
  
}
