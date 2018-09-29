package yh.core.funcs.attendance.personal.data;

import java.util.Date;

public class YHAttendOut {
  private int seqId;
  private String userId;
  private String leaderId;
  private String outType;
  private Date createDate;
  private Date submitTime;
  private String outTime1;
  private String outTime2;
  private String allow;
  private String status;
  private String reason;
  private String registerIp;
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
  public String getLeaderId() {
    return leaderId;
  }
  public void setLeaderId(String leaderId) {
    this.leaderId = leaderId;
  }
  public String getOutType() {
    return outType;
  }
  public void setOutType(String outType) {
    this.outType = outType;
  }
  public Date getCreateDate() {
    return createDate;
  }
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
  public Date getSubmitTime() {
    return submitTime;
  }
  public void setSubmitTime(Date submitTime) {
    this.submitTime = submitTime;
  }
  public String getOutTime1() {
    return outTime1;
  }
  public void setOutTime1(String outTime1) {
    this.outTime1 = outTime1;
  }
  public String getOutTime2() {
    return outTime2;
  }
  public void setOutTime2(String outTime2) {
    this.outTime2 = outTime2;
  }
  public String getAllow() {
    return allow;
  }
  public void setAllow(String allow) {
    this.allow = allow;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public String getReason() {
    return reason;
  }
  public void setReason(String reason) {
    this.reason = reason;
  }
  public String getRegisterIp() {
    return registerIp;
  }
  public void setRegisterIp(String registerIp) {
    this.registerIp = registerIp;
  }
}
