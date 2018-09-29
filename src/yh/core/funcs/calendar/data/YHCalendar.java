package yh.core.funcs.calendar.data;

import java.util.Date;

public class YHCalendar {
  private int seqId;
  private String userId;
  private String calType;
  private String calLevel;
  private String content;
  private String managerId;
  private Date calTime;
  private Date endTime;
  private String overStatus;
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
  public String getCalType() {
    return calType;
  }
  public void setCalType(String calType) {
    this.calType = calType;
  }
  public String getCalLevel() {
    return calLevel;
  }
  public void setCalLevel(String calLevel) {
    this.calLevel = calLevel;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public String getManagerId() {
    return managerId;
  }
  public void setManagerId(String managerId) {
    this.managerId = managerId;
  }
  public Date getCalTime() {
    return calTime;
  }
  public void setCalTime(Date calTime) {
    this.calTime = calTime;
  }
  public Date getEndTime() {
    return endTime;
  }
  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }
  public String getOverStatus() {
    return overStatus;
  }
  public void setOverStatus(String overStatus) {
    this.overStatus = overStatus;
  }

}
