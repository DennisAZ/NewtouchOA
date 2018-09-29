package yh.core.funcs.system.censorcheck.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import yh.core.data.YHDsField;

public class YHCensorCheck {
  private int seqId;
  private String moduleCode;
  private String censorFlag;
  private String checkUser;
  private Date checkTime;
  private String content;
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getModuleCode() {
    return moduleCode;
  }
  public void setModuleCode(String moduleCode) {
    this.moduleCode = moduleCode;
  }
  public String getCensorFlag() {
    return censorFlag;
  }
  public void setCensorFlag(String censorFlag) {
    this.censorFlag = censorFlag;
  }
  public String getCheckUser() {
    return checkUser;
  }
  public void setCheckUser(String checkUser) {
    this.checkUser = checkUser;
  }
  public Date getCheckTime() {
    return checkTime;
  }
  public void setCheckTime(Date checkTime) {
    this.checkTime = checkTime;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  
}
