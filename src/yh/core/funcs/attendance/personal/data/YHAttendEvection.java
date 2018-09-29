package yh.core.funcs.attendance.personal.data;

import java.util.Date;

public class YHAttendEvection {
  private int seqId;
  private String userId;
  private String leaderId;
  private String evectionDest;
  private Date evectionDate1;
  private Date evectionDate2;
  private String status;
  private String allow;
  private String reason;
  private String notReason;
  private String registerIp;
  private double hour;
  
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
  public String getEvectionDest() {
    return evectionDest;
  }
  public void setEvectionDest(String evectionDest) {
    this.evectionDest = evectionDest;
  }
  public Date getEvectionDate1() {
    return evectionDate1;
  }
  public void setEvectionDate1(Date evectionDate1) {
    this.evectionDate1 = evectionDate1;
  }
  public Date getEvectionDate2() {
    return evectionDate2;
  }
  public void setEvectionDate2(Date evectionDate2) {
    this.evectionDate2 = evectionDate2;
  }
  public String getStatus() {
    return status;
  }
  public void setStatus(String status) {
    this.status = status;
  }
  public String getAllow() {
    return allow;
  }
  public void setAllow(String allow) {
    this.allow = allow;
  }
  public String getReason() {
    return reason;
  }
  public void setReason(String reason) {
    this.reason = reason;
  }
  public String getNotReason() {
    return notReason;
  }
  public void setNotReason(String notReason) {
    this.notReason = notReason;
  }
  public String getRegisterIp() {
    return registerIp;
  }
  public void setRegisterIp(String registerIp) {
    this.registerIp = registerIp;
  }
  public double getHour() {
    return hour;
  }
  public void setHour(double hour) {
    this.hour = hour;
  }
}
