package yh.custom.attendance.data;

import java.util.Date;

public class YHOvertimeRecord {
  private int seqId;
  private String userId;
  private Date overtimeTime;
  private String leaderId;
  private String overtimeDesc;
  private Date beginTime;
  private String beginDate;
  private String endDate;
  private String status;
  private String reason;
  private double normalAdd;  //平时加班费(按照小时计算)
  private double festivalAdd; //节假日加班费(按照小时)
  private double weekAdd;     //周末加班（按照小时）
  private String overtimeType; //加班类型
  private String hour;       //加班小时
  private double overtimeMoney;  //总加班费
  
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
  public String getOvertimeType() {
    return overtimeType;
  }
  public void setOvertimeType(String overtimeType) {
    this.overtimeType = overtimeType;
  }
  public String getHour() {
    return hour;
  }
  public void setHour(String hour) {
    this.hour = hour;
  }
  public double getOvertimeMoney() {
    return overtimeMoney;
  }
  public void setOvertimeMoney(double overtimeMoney) {
    this.overtimeMoney = overtimeMoney;
  }
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
  public Date getOvertimeTime() {
    return overtimeTime;
  }
  public void setOvertimeTime(Date overtimeTime) {
    this.overtimeTime = overtimeTime;
  }
  public String getLeaderId() {
    return leaderId;
  }
  public void setLeaderId(String leaderId) {
    this.leaderId = leaderId;
  }
  public String getOvertimeDesc() {
    return overtimeDesc;
  }
  public void setOvertimeDesc(String overtimeDesc) {
    this.overtimeDesc = overtimeDesc;
  }

  public Date getBeginTime() {
    return beginTime;
  }
  public void setBeginTime(Date beginTime) {
    this.beginTime = beginTime;
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


}
