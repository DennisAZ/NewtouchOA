package yh.subsys.jtgwjh.sealmanage.data;

import java.util.ArrayList;
import java.util.Date;

public class YHJhSeal {
  private int seqId;
  private String sealId;
  private String deptId;
  private String sealName;
  private String certStr;
  private String userStr;
  private String sealData;
  private Date createTime;
  //公文交换
  private String deptStr;
  private String isFlag;
  private String outDeptId;
  private String outDeptName;
  public String getIsFlag() {
    return isFlag;
  }
  public void setIsFlag(String isFlag) {
    this.isFlag = isFlag;
  }
  public String getDeptStr() {
    return deptStr;
  }
  public void setDeptStr(String deptStr) {
    this.deptStr = deptStr;
  }
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
  public String getOutDeptId() {
	return outDeptId;
}
public void setOutDeptId(String outDeptId) {
	this.outDeptId = outDeptId;
}
public String getOutDeptName() {
	return outDeptName;
}
public void setOutDeptName(String outDeptName) {
	this.outDeptName = outDeptName;
}
}
