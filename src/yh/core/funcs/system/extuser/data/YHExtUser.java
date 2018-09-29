package yh.core.funcs.system.extuser.data;

import java.util.ArrayList;
import java.util.Iterator;

import yh.core.data.YHDsField;

public class YHExtUser {
  private int seqId;
  private String userId;
  private String password;
  private String useFlag;
  private String authModule;
  private String postfix;
  private String remark;
  private String sysUser;
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public String getUseFlag() {
    return useFlag;
  }
  public void setUseFlag(String useFlag) {
    this.useFlag = useFlag;
  }
  public String getAuthModule() {
    return authModule;
  }
  public void setAuthModule(String authModule) {
    this.authModule = authModule;
  }
  public String getPostfix() {
    return postfix;
  }
  public void setPostfix(String postfix) {
    this.postfix = postfix;
  }
  public String getRemark() {
    return remark;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }
  public String getSysUser() {
    return sysUser;
  }
  public void setSysUser(String sysUser) {
    this.sysUser = sysUser;
  }
}
