package yh.core.funcs.calendar.data;

import java.util.Date;

public class YHAffair {
  private int seqId;
  private String userId;
  private Date beginTime;
  private Date endTime;
  private String type;
  private String remindDate;
  private String remindTime;
  private String content;
  private Date lastRemind;
  private String sms2Remind;
  private Date lastSms2Remind;
  private String managerId;
  private String isWeekend;
  public String getIsWeekend() {
    return isWeekend;
  }
  public void setIsWeekend(String isWeekend) {
    this.isWeekend = isWeekend;
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
  public Date getBeginTime() {
    return beginTime;
  }
  public void setBeginTime(Date beginTime) {
    this.beginTime = beginTime;
  }
  public Date getEndTime() {
    return endTime;
  }
  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }

  public String getRemindDate() {
    return remindDate;
  }
  public void setRemindDate(String remindDate) {
    this.remindDate = remindDate;
  }
  public String getRemindTime() {
    return remindTime;
  }
  public void setRemindTime(String remindTime) {
    this.remindTime = remindTime;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public Date getLastRemind() {
    return lastRemind;
  }
  public void setLastRemind(Date lastRemind) {
    this.lastRemind = lastRemind;
  }
  public String getSms2Remind() {
    return sms2Remind;
  }
  public void setSms2Remind(String sms2Remind) {
    this.sms2Remind = sms2Remind;
  }
  public Date getLastSms2Remind() {
    return lastSms2Remind;
  }
  public void setLastSms2Remind(Date lastSms2Remind) {
    this.lastSms2Remind = lastSms2Remind;
  }
  public String getManagerId() {
    return managerId;
  }
  public void setManagerId(String managerId) {
    this.managerId = managerId;
  }
}
