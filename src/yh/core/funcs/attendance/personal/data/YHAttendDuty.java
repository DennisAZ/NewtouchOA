package yh.core.funcs.attendance.personal.data;

import java.util.Date;

public class YHAttendDuty {
  private int seqId;
  private String userId;
  private String registerType;
  private Date registerTime;
  private String registerIp;
  private String remark;
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
  public String getRegisterType() {
    return registerType;
  }
  public void setRegisterType(String registerType) {
    this.registerType = registerType;
  }
  public Date getRegisterTime() {
    return registerTime;
  }
  public void setRegisterTime(Date registerTime) {
    this.registerTime = registerTime;
  }
  public String getRegisterIp() {
    return registerIp;
  }
  public void setRegisterIp(String registerIp) {
    this.registerIp = registerIp;
  }
  public String getRemark() {
    return remark;
  }
  public void setRemark(String remark) {
    this.remark = remark;
  }
}
