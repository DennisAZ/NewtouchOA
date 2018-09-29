package yh.custom.attendance.data;

import java.util.Date;

public class YHDuty {
  private int seqId;
  private String userId;
  private String leaderId;
  private String dutyDesc;
  private Date dutyTime;
  private String beginDate;
  private String endDate;
  private String status;
  private String reason;
  private double normalAdd;  //平时值班费(按照小时计算)
  private double festivalAdd; //节假日值班费(按照小时)
  private double weekAdd;     //周末值班（按照小时）
  private String dutyType; //值班类型
  private String hour;       //值班小时
  private double dutyMoney;  //总值班费
  
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
  public String getDutyDesc() {
    return dutyDesc;
  }
  public void setDutyDesc(String dutyDesc) {
    this.dutyDesc = dutyDesc;
  }
  public Date getDutyTime() {
    return dutyTime;
  }
  public void setDutyTime(Date dutyTime) {
    this.dutyTime = dutyTime;
  }
  public String getBeginDate() {
    return beginDate;
  }
  public void setBeginDate(String beginDate) {
    this.beginDate = beginDate;
  }
  public String getEndDate() {
    return endDate;
  }
  public void setEndDate(String endDate) {
    this.endDate = endDate;
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
  public double getNormalAdd() {
    return normalAdd;
  }
  public void setNormalAdd(double normalAdd) {
    this.normalAdd = normalAdd;
  }
  public double getFestivalAdd() {
    return festivalAdd;
  }
  public void setFestivalAdd(double festivalAdd) {
    this.festivalAdd = festivalAdd;
  }
  public double getWeekAdd() {
    return weekAdd;
  }
  public void setWeekAdd(double weekAdd) {
    this.weekAdd = weekAdd;
  }
  public String getDutyType() {
    return dutyType;
  }
  public void setDutyType(String dutyType) {
    this.dutyType = dutyType;
  }
  public String getHour() {
    return hour;
  }
  public void setHour(String hour) {
    this.hour = hour;
  }
  public double getDutyMoney() {
    return dutyMoney;
  }
  public void setDutyMoney(double dutyMoney) {
    this.dutyMoney = dutyMoney;
  }
}
