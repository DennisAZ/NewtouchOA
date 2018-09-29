package yh.core.funcs.system.sealmanage.data;

import java.util.ArrayList;
import java.util.Date;

public class YHSeal {
  private int seqId;
  private String sealId;
  private String deptId;
  private String sealName;
  private String certStr;
  private String userStr;
  private String sealData;
  private Date createTime;
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getSealId() {
    return sealId;
  }
  public void setSealId(String sealId) {
    this.sealId = sealId;
  }
  public String getDeptId() {
    return deptId;
  }
  public void setDeptId(String deptId) {
    this.deptId = deptId;
  }
  public String getSealName() {
    return sealName;
  }
  public void setSealName(String sealName) {
    this.sealName = sealName;
  }
  public String getCertStr() {
    return certStr;
  }
  public void setCertStr(String certStr) {
    this.certStr = certStr;
  }
  public String getUserStr() {
    return userStr;
  }
  public void setUserStr(String userStr) {
    this.userStr = userStr;
  }
  public String getSealData() {
    return sealData;
  }
  public void setSealData(String sealData) {
    this.sealData = sealData;
  }
  public Date getCreateTime() {
    return createTime;
  }
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
  
}
