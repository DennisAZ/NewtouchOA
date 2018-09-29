package yh.pda.calendar.data;

import java.util.Date;

public class YHPdaAffair {

  private int seqId;
  private String userId;
  private int type;
  private String remindDate;
  private String remindTime;
  private String content;
  
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
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public int getType() {
    return type;
  }
  public void setType(int type) {
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
  
}
