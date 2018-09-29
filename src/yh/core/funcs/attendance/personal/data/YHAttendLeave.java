package yh.core.funcs.attendance.personal.data;

import java.util.Date;

public class YHAttendLeave {
  private int seqId;
  private String userId;
  private String leaderId;
  private String leaveType;
  private Date leaveDate1;
  private Date leaveDate2;
  private int annualLeave;
  private String status;
  private String allow;
  private String reason;
  private Date destroyTime;
  private String registerIp;
  private double hour;
  private double leaveDays;
 
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
  public String getLeaveType() {
    return leaveType;
  }
  public void setLeaveType(String leaveType) {
    this.leaveType = leaveType;
  }
  public Date getLeaveDate1() {
    return leaveDate1;
  }
  public void setLeaveDate1(Date leaveDate1) {
    this.leaveDate1 = leaveDate1;
  }
  public Date getLeaveDate2() {
    return leaveDate2;
  }
  public void setLeaveDate2(Date leaveDate2) {
    this.leaveDate2 = leaveDate2;
  }
  public int getAnnualLeave() {
    return annualLeave;
  }
  public void setAnnualLeave(int annualLeave) {
    this.annualLeave = annualLeave;
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
  public Date getDestroyTime() {
    return destroyTime;
  }
  public void setDestroyTime(Date destroyTime) {
    this.destroyTime = destroyTime;
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
  public double getLeaveDays() {
    return leaveDays;
  }
  public void setLeaveDays(double leaveDays) {
    this.leaveDays = leaveDays;
  }

}
