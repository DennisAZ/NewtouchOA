package yh.core.funcs.system.accesscontrol.data;

import java.util.ArrayList;
import java.util.Iterator;

import yh.core.data.YHDsField;

public class YHIpRule {
  private int seqId;
  private String beginIp;
  private String endIp;
  private String type;
  private String remark;
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getBeginIp() {
    return beginIp;
  }
  public void setBeginIp(String beginIp) {
    this.beginIp = beginIp;
  }
  public String getEndIp() {
    return endIp;
  }
  public void setEndIp(String endIp) {
    this.endIp = endIp;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getRemark() {
    return remark;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }
}
