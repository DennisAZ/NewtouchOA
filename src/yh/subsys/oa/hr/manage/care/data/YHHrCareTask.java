package yh.subsys.oa.hr.manage.care.data;

import java.util.Date;

public class YHHrCareTask {

  private int seqId;//   流水号
  private String content;//  提醒内容
  private String reminder;//   提醒人
  private String remindType;//  
  
  
  public int getSeqId() {
    return seqId;
  }
  public void setSeqId(int seqId) {
    this.seqId = seqId;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public String getReminder() {
    return reminder;
  }
  public void setReminder(String reminder) {
    this.reminder = reminder;
  }
  public String getRemindType() {
    return remindType;
  }
  public void setRemindType(String remindType) {
    this.remindType = remindType;
  }
}
